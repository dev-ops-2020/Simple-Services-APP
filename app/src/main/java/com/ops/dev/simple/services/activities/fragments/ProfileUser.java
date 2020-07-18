package com.ops.dev.simple.services.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.EditProfileUser;
import com.ops.dev.simple.services.activities.MainMenuUser;
import com.ops.dev.simple.services.activities.SignIn;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.IntentAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.UsersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ProfileUser extends Fragment {

	static final String ARG_PARAM1 = "param1";
	static final String ARG_PARAM2 = "param2";

	String mParam1;
	String mParam2;

	OnFragmentInteractionListener mListener;

	// Vars
	TextView alias, name, phone, email;
	ImageView settings, picture, qrCode;
	CardView pictures;
	LinearLayout info, main;

	static int lSettings = R.layout.__modal_settings;
	static int lQrCode = R.layout.__modal_qr;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	Context context ;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

	UsersModel user;

	public ProfileUser() {

	}

	public static ProfileUser newInstance(String param1, String param2) {
		ProfileUser fragment = new ProfileUser();
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
		final View rootView = inflater.inflate(R.layout.fragment_profile_user, container, false);
		View layout = rootView.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		alias = rootView.findViewById(R.id.alias);
		name = rootView.findViewById(R.id.name);
		phone = rootView.findViewById(R.id.phone);
		email = rootView.findViewById(R.id.email);
		settings = rootView.findViewById(R.id.settings);
		picture = rootView.findViewById(R.id.picture);
		qrCode = rootView.findViewById(R.id.qrCode);

		pictures = rootView.findViewById(R.id.pictures);
		info = rootView.findViewById(R.id.info);
		main = rootView.findViewById(R.id.main);

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lSettings);
			}
		});
		/*
		qrCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lQrCode);
			}
		});
 		*/
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

		if (layout == lSettings) {
			//Vars
			final LinearLayout theme = layoutView.findViewById(R.id.theme);
			final LinearLayout edit = layoutView.findViewById(R.id.edit);
			final LinearLayout log_out = layoutView.findViewById(R.id.log_out);

			theme.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toastAdapter.makeToast(R.drawable._fav, "Esta opción estará disponible pronto...");
				}
			});
			edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditProfileUser.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("user", user);
					startActivity(intent);
				}
			});
			log_out.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					preferencesAdapter.deletePreferences();
					Objects.requireNonNull(getActivity()).finish();
					Intent intent = new Intent(context, SignIn.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});
		} else {
			final ImageView picture = layoutView.findViewById(R.id.picture);
			glideAdapter.setImageCircle(picture, user.getPicture());
		}
		alertDialog = builder.create();
		if (layout == lQrCode)
			alertDialog.setCancelable(true);
		alertDialog.show();
	}

	private void getProfile() {
		String url = Network.Users+ preferencesAdapter.getId();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					user = new UsersModel();
					JSONObject jsonObject = response.getJSONObject("user");
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

					glideAdapter.setImageCircle(picture, user.getPicture());
					pictures.setVisibility(View.VISIBLE);
					info.setVisibility(View.VISIBLE);
					main.setVisibility(View.VISIBLE);
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