package com.ops.dev.simple.services.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.CategoriesModel;
import com.ops.dev.simple.services.models.CommentsModel;
import com.ops.dev.simple.services.models.UsersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Profile extends Fragment {

	static final String ARG_PARAM1 = "param1";
	static final String ARG_PARAM2 = "param2";

	String mParam1;
	String mParam2;

	OnFragmentInteractionListener mListener;

	// Vars
	TextView alias, name, phone, email;
	ImageView settings, picture;

	static int lSettings = R.layout.__modal_settings;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	Context context ;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	PreferencesAdapter preferencesAdapter;

	public Profile() {

	}

	public static Profile newInstance(String param1, String param2) {
		Profile fragment = new Profile();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		View layout = rootView.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		alias = rootView.findViewById(R.id.alias);
		name = rootView.findViewById(R.id.name);
		phone = rootView.findViewById(R.id.phone);
		email = rootView.findViewById(R.id.email);
		settings = rootView.findViewById(R.id.settings);
		picture = rootView.findViewById(R.id.picture);

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lSettings);
			}
		});


		toastAdapter = new ToastAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);
		getProfile();

		return rootView;
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

	private void showModal(final int layout) {
		final View layoutView = getLayoutInflater().inflate(layout, null);
		builder = new AlertDialog.Builder(getActivity());
		builder.setView(layoutView);

		//Vars
		final LinearLayout theme = layoutView.findViewById(R.id.theme);

		theme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toastAdapter.makeToast("Esta opción estará disponible pronto...", R.drawable._fav);
			}
		});
		alertDialog = builder.create();
		alertDialog.show();
	}

	private void getProfile() {
		String url = Network.Profile+preferencesAdapter.getId();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject jsonObject = response.getJSONObject("user");
					UsersModel user = new UsersModel();
					user.setId(jsonObject.getString("_id"));
					user.setName(jsonObject.getString("name"));
					user.setAlias(jsonObject.getString("alias"));
					user.setPhone(jsonObject.getString("phone"));
					user.setEmail(jsonObject.getString("email"));
					user.setPicture(jsonObject.getString("picture"));

					alias.setText(user.getAlias());
					name.setText(user.getName());
					phone.setText(user.getPhone());
					email.setText(user.getEmail());

					Glide
							.with(context)
							.load(user.getPicture())
							.transform(new RoundedCorners(R.dimen.med_margin))
							.into(picture);

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