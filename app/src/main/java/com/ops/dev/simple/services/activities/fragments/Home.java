package com.ops.dev.simple.services.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesAdapter;
import com.ops.dev.simple.services.models.CategoriesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends Fragment {

	static final String ARG_PARAM1 = "param1";
	static final String ARG_PARAM2 = "param2";

	String mParam1;
	String mParam2;

	OnFragmentInteractionListener mListener;

	//Vars
	RecyclerView rvCategories;
    List<CategoriesModel> listCategories;
    CategoriesAdapter categoriesAdapter;
    Context context ;
	RequestQueue queue;

	public Home() {

	}

	public static Home newInstance(String param1, String param2) {
		Home fragment = new Home();
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
		final View view = inflater.inflate(R.layout.fragment_home, viewGroup, false);
		final View layout = view.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		rvCategories = view.findViewById(R.id.rvCategories);
		listCategories = new ArrayList<>();

		queue = Volley.newRequestQueue(context);
		ListCategories();

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


	private void ListCategories() {
		String url = Network.ListCategories;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray jsonArray = response.getJSONArray("categories");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						CategoriesModel category = new CategoriesModel();
						category.setId(jsonObject.getString("_id"));
						category.setName(jsonObject.getString("name"));
						category.setDescription(jsonObject.getString("description"));
						int icon = context.getResources().getIdentifier(jsonObject.getString("icon"), "drawable", context.getPackageName());
						category.setIcon(icon);
						listCategories.add(category);
					}
					RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
					rvCategories.setLayoutManager(layoutManager);
					categoriesAdapter = new CategoriesAdapter(context, listCategories);
					rvCategories.setAdapter(categoriesAdapter);
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
}