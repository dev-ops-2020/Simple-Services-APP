package com.ops.dev.simple.services.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.Random;

public class Home extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	//Vars
	private RecyclerView rvCategories;
	private List<CategoriesModel> listCategories;
	private CategoriesAdapter categoriesAdapter;
	private Context context ;

	private OnFragmentInteractionListener mListener;

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
		final LinearLayout container = view.findViewById(R.id.container);

		int[] colors = {R.color.red, R.color.blue};
		int randomColor = new Random().nextInt(colors.length);
		container.setBackgroundResource(colors[randomColor]);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		rvCategories = view.findViewById(R.id.rvCategories);
		listCategories = new ArrayList<>();

		listCategories.add(new CategoriesModel("1", "Comida", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("2", "Bebidas", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("3", "Medicinas", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("4", "Canasta básica", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("5", "Otros", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("6", "Something here...", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("7", "Otros 2 ", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("8", "Something here 2", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("9", "Otros 3 ", "",R.drawable._fav));
		listCategories.add(new CategoriesModel("10", "Something here 3", "",R.drawable._fav));

		RecyclerView.LayoutManager layoutManager = new GridLayoutManager( context,2);
		rvCategories.setLayoutManager(layoutManager);
		categoriesAdapter = new CategoriesAdapter(context, listCategories);
		rvCategories.setAdapter(categoriesAdapter);

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
}