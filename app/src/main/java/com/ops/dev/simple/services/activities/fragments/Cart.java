package com.ops.dev.simple.services.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.MainMenuUser;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.ProductsCartAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.CartsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart extends Fragment {

	static final String ARG_PARAM1 = "param1";
	static final String ARG_PARAM2 = "param2";

	String mParam1;
	String mParam2;

	OnFragmentInteractionListener mListener;

	// Vars
	RecyclerView rvProductsCart;
	List<CartsModel> listProductsCart;
	ProductsCartAdapter productsCartAdapter;
	JSONArray productsArray;

	TextView container_message;
	RelativeLayout main;
	Context context ;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

	String __message;
	TextView total_price;
	Button order;

	public static Fragment cart;

	public Cart() {

	}

	public static Cart newInstance(String param1, String param2) {
		Cart fragment = new Cart();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cart = this;
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_cart, viewGroup, false);
		final View layout = view.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		container_message = view.findViewById(R.id.container_message);
		main = view.findViewById(R.id.main);

		rvProductsCart = view.findViewById(R.id.rvProductsCart);
		listProductsCart = new ArrayList<>();

		total_price = view.findViewById(R.id.total_price);

		getProducts(preferencesAdapter.getCartId(), preferencesAdapter.getId(), preferencesAdapter.getBusinessId());

		return view;
	}

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

	private void getProducts(String cartId, String userId, String businessId) {
		String url = Network.Cart+cartId+"/"+userId+"/"+businessId;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					__message = response.getString("message");
					if (__message.equals("Ok")) {
						productsArray = response.getJSONArray("products");
						if (productsArray.length() > 0) {
							showCart(productsArray);
						} else {
							main.setVisibility(View.INVISIBLE);
							container_message.setVisibility(View.VISIBLE);
							container_message.setText("Esto parece estar bastante vacío, prueba agregar algún producto 😊");
						}
					} else {
						main.setVisibility(View.INVISIBLE);
						container_message.setVisibility(View.VISIBLE);
						container_message.setText("Esto parece estar bastante vacío, prueba agregar algún producto 😊");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		queue.add(request);
	}

	private void showCart(final JSONArray list) {
		try {
			for (int i = 0; i < list.length(); i++) {
				JSONObject jsonObject = list.getJSONObject(i);
				final CartsModel productCart = new CartsModel();
				final int qty;
				qty = jsonObject.getInt("qty");
				String url = Network.Products+jsonObject.getString("productId");
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONObject jsonObject = response.getJSONObject("product");
							productCart.setId(jsonObject.getString("_id"));
							productCart.setName(jsonObject.getString("name"));
							productCart.setPrice(jsonObject.getString("price"));
							JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
							JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(0);
							productCart.setPicture(jsonObjectPicture.getString("picture"));
							productCart.setQuantity(qty);
							listProductsCart.add(productCart);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						container_message.setVisibility(View.GONE);
						main.setVisibility(View.VISIBLE);
						LinearLayoutManager layoutManager = new LinearLayoutManager(context);
						layoutManager.setOrientation(RecyclerView.VERTICAL);
						rvProductsCart.setLayoutManager(layoutManager);
						rvProductsCart.setHasFixedSize(true);
						productsCartAdapter = new ProductsCartAdapter(context, listProductsCart, preferencesAdapter.getCartId() , preferencesAdapter.getId(), preferencesAdapter.getBusinessId());
						productsCartAdapter.notifyDataSetChanged();
						productsCartAdapter.onAttachedToRecyclerView(rvProductsCart);
						rvProductsCart.setAdapter(productsCartAdapter);
						rvProductsCart.setItemAnimator(new DefaultItemAnimator());
						total_price.setText(String.valueOf(productsCartAdapter.getTotal()));
						ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
							@Override
							public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
								return false;
							}

							@Override
							public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
								int position = viewHolder.getAdapterPosition();
								deleteCartProduct(preferencesAdapter.getCartId(), listProductsCart.get(position).getId(), listProductsCart.get(position).getQuantity());
								listProductsCart.remove(position);
							}
						});
						itemTouchHelper.attachToRecyclerView(rvProductsCart);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
				queue.add(request);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listProductsCart.size() > 0)
			listProductsCart.clear();
	}

	private void deleteCartProduct(final String cartId, final String productId, final int qty) {
		String url = Network.Cart+cartId+"/"+productId;
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("productId", productId);
			jsonParams.put("qty", qty);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				refreshFragment();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		queue.add(request);
	}

	public void refreshFragment() {
		getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
		getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
	}
}