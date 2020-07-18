package com.ops.dev.simple.services.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.AddProduct;
import com.ops.dev.simple.services.activities.MainMenuBusiness;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ProductsAdapter;
import com.ops.dev.simple.services.adapters.ProductsCatalogAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.ProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Catalog extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;


	private OnFragmentInteractionListener mListener;


	// Vars
	TextView container_message, available, unavailable;
	LinearLayout lAvailable, lUnavailable;
	RecyclerView rvAvailable, rvUnavailable;
	List<ProductsModel> listAvailable, listUnavailable;
	ProductsCatalogAdapter availableAdapter, unavailableAdapter;

	String __message;
	Context context;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;
	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	FloatingActionButton addProduct;

	public static Fragment catalog;

	public Catalog() {

	}

	public static Catalog newInstance(String param1, String param2) {
		Catalog fragment = new Catalog();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		catalog = this;
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.floating_catalog, container, false);
		final View layout = view.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		container_message = view.findViewById(R.id.container_message);
		available = view.findViewById(R.id.available);
		unavailable = view.findViewById(R.id.unavailable);
		lAvailable = view.findViewById(R.id.lAvailable);
		lUnavailable = view.findViewById(R.id.lUnavailable);

		rvAvailable = view.findViewById(R.id.rvAvailable);
		listAvailable = new ArrayList<>();

		rvUnavailable = view.findViewById(R.id.rvUnavailable);
		listUnavailable = new ArrayList<>();

		addProduct = view.findViewById(R.id.addProduct);
		addProduct.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addProduct();
			}
		});

		view.findViewById(R.id.available).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getAvailableProducts();
			}
		});
		view.findViewById(R.id.unavailable).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getUnavailableProducts();
			}
		});
		getAvailableProducts();

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

	private void getAvailableProducts() {
		available.setBackgroundResource(R.drawable.border_selected);
		unavailable.setBackgroundResource(0);
		String url = Network.ProductsAvailable+preferencesAdapter.getId();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					__message = response.getString("message");
					if (__message.equals("Ok")) {
						if (response.getJSONArray("products").length() > 0) {
							if (listAvailable.size() > 0)
								listAvailable.clear();
							JSONArray jsonArray = response.getJSONArray("products");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								ProductsModel product = new ProductsModel();
								product.setId(jsonObject.getString("_id"));
								product.setType(jsonObject.getString("type"));
								product.setName(jsonObject.getString("name"));
								product.setDesc(jsonObject.getString("desc"));
								product.setPrice(jsonObject.getString("price"));
								product.setAvailable(jsonObject.getBoolean("available"));
								JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
								JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(0);
								product.setPicture(jsonObjectPicture.getString("picture"));
								product.setPictures(jsonObject.getString("pictures"));
								product.setTags(jsonObject.getString("tags"));
								product.setBusinessId(jsonObject.getString("businessId"));
								listAvailable.add(product);
							}
							container_message.setText("");
							lUnavailable.setVisibility(View.GONE);
							lAvailable.setVisibility(View.VISIBLE);
							LinearLayoutManager layoutManager = new LinearLayoutManager(context);
							layoutManager.setOrientation(RecyclerView.VERTICAL);
							rvAvailable.setLayoutManager(layoutManager);
							rvAvailable.setHasFixedSize(true);
							availableAdapter = new ProductsCatalogAdapter(context, listAvailable);
							rvAvailable.setAdapter(availableAdapter);
						}
					} else {
						lUnavailable.setVisibility(View.GONE);
						container_message.setText("No encontramos productos disponibles, prueba agregar algunos");
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

	private void getUnavailableProducts() {
		unavailable.setBackgroundResource(R.drawable.border_selected);
		available.setBackgroundResource(0);
		String url = Network.ProductsUnavailable+preferencesAdapter.getId();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					__message = response.getString("message");
					if (__message.equals("Ok")) {
						if (response.getJSONArray("products").length() > 0) {
							if (listUnavailable.size() > 0)
								listUnavailable.clear();
							JSONArray jsonArray = response.getJSONArray("products");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								ProductsModel product = new ProductsModel();
								product.setId(jsonObject.getString("_id"));
								product.setType(jsonObject.getString("type"));
								product.setName(jsonObject.getString("name"));
								product.setDesc(jsonObject.getString("desc"));
								product.setPrice(jsonObject.getString("price"));
								product.setAvailable(jsonObject.getBoolean("available"));
								JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
								JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(0);
								product.setPicture(jsonObjectPicture.getString("picture"));
								product.setPictures(jsonObject.getString("pictures"));
								product.setTags(jsonObject.getString("tags"));
								product.setBusinessId(jsonObject.getString("businessId"));
								listUnavailable.add(product);
							}
							container_message.setText("");
							lAvailable.setVisibility(View.GONE);
							lUnavailable.setVisibility(View.VISIBLE);
							LinearLayoutManager layoutManager = new LinearLayoutManager(context);
							layoutManager.setOrientation(RecyclerView.VERTICAL);
							rvUnavailable.setLayoutManager(layoutManager);
							rvUnavailable.setHasFixedSize(true);
							unavailableAdapter = new ProductsCatalogAdapter(context, listUnavailable);
							rvUnavailable.setAdapter(unavailableAdapter);
						}
					} else {
						lAvailable.setVisibility(View.GONE);
						container_message.setText("No encontramos productos no disponibles, prueba agregar algunos");
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

	private void addProduct() {
		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("AGREGAR");
		builder.setMessage("¿Vamos a agregar un producto o un servicio?");
		builder.setNegativeButton("Producto", null);
		builder.setPositiveButton("Servicio", null);
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
				Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				btnCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, AddProduct.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type", "Product");
						context.startActivity(intent);
						alertDialog.dismiss();
					}
				});
				btnOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, AddProduct.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type", "Service");
						context.startActivity(intent);
						alertDialog.dismiss();
					}
				});
			}
		});
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

	public void refreshFragment() {
		getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
		getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
	}
}