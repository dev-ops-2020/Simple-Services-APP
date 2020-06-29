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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ops.dev.simple.services.activities.MainMenu;
import com.ops.dev.simple.services.adapters.CommentsAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.ProductsAdapter;
import com.ops.dev.simple.services.adapters.ProductsCartAdapter;
import com.ops.dev.simple.services.adapters.SharedPreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.CommentsModel;
import com.ops.dev.simple.services.models.ProductsCartModel;
import com.ops.dev.simple.services.models.ProductsModel;

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
	List<ProductsCartModel> listProductsCart;
	ProductsCartAdapter productsCartAdapter;
	LinearLayout footer;
	Context context ;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	SharedPreferencesAdapter sharedPreferencesAdapter;

	JSONArray productsArray;

	String __message;
	TextView price;
	Button order;

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
		sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		footer = view.findViewById(R.id.footer);

		rvProductsCart = view.findViewById(R.id.rvProductsCart);
		listProductsCart = new ArrayList<>();

		getProducts(sharedPreferencesAdapter.getCartId());

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

	private void getProducts(String cartId) {
		String url = Network.Cart+cartId;
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
							goToCategories();
						}
					} else {
						goToCategories();
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

	private void showCart(JSONArray list) {
		try {
			for (int i = 0; i < list.length(); i++) {
				JSONObject jsonObject = list.getJSONObject(i);
				final ProductsCartModel productCart = new ProductsCartModel();
				final int qty = jsonObject.getInt("qty");
				String url = Network.Products+jsonObject.getString("productId");
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONObject jsonObject = response.getJSONObject("product");
							productCart.setId(jsonObject.getString("_id"));
							productCart.setName(jsonObject.getString("name"));
							productCart.setDescription(jsonObject.getString("description"));
							JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
							for (int j = 0 ; j <jsonArrayPictures.length(); j++ ) {
								JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(j);
								productCart.setPicture(jsonObjectPicture.getString("picture"));
							}
							JSONArray jsonArrayPrices = jsonObject.getJSONArray("prices");
							for (int j = 0 ; j <jsonArrayPrices.length(); j++ ) {
								JSONObject jsonObjectPrice = jsonArrayPrices.getJSONObject(j);
								productCart.setPrice(jsonObjectPrice.getString("price"));
							}
							productCart.setQuantity(qty);
							listProductsCart.add(productCart);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						footer.setVisibility(View.VISIBLE);
						LinearLayoutManager layoutManager = new LinearLayoutManager(context);
						layoutManager.setOrientation(RecyclerView.VERTICAL);
						rvProductsCart.setLayoutManager(layoutManager);
						rvProductsCart.setHasFixedSize(true);
						productsCartAdapter = new ProductsCartAdapter(context, listProductsCart);
						rvProductsCart.setAdapter(productsCartAdapter);
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
	}

	private void goToCategories() {
		toastAdapter.makeToast(R.drawable.__warning, "El carrito aún está vacío, prueba agregar algún producto 😉");
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(context, MainMenu.class);
				intent.putExtra("screen", R.id.categories);
				intent.putExtra("number", 1);
				startActivity(intent);
			}
		}, 1000);
	}
}