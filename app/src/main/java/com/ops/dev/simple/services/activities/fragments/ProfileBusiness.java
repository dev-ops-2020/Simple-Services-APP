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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.SignIn;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.IntentAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.SchedulesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.SchedulesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ProfileBusiness extends Fragment {

	static final String ARG_PARAM1 = "param1";
	static final String ARG_PARAM2 = "param2";

	String mParam1;
	String mParam2;

	OnFragmentInteractionListener mListener;

	// Vars
	TextView alias, owner, email;
	ImageView icon, settings, picture;
	CardView pictures;
	LinearLayout info;

	String _businessDesc, _businessSlogan, _businessPhone, _businessAddress;
	TextInputLayout businessDesc, businessSlogan, businessPhone, businessAddress;

	RecyclerView rvSchedules;
	List<SchedulesModel> listSchedules;
	SchedulesAdapter schedulesAdapter;
	JSONArray scheduleArray;

	static int lSettings = R.layout.__modal_settings;
	static int lPicture = R.layout.__modal_qr;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	Context context ;
	RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

	BusinessesModel business;

	public ProfileBusiness() {

	}

	public static ProfileBusiness newInstance(String param1, String param2) {
		ProfileBusiness fragment = new ProfileBusiness();
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
		final View rootView = inflater.inflate(R.layout.fragment_profile_business, container, false);
		View layout = rootView.findViewById(android.R.id.content);

		context = Objects.requireNonNull(getActivity()).getApplicationContext();

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		icon = rootView.findViewById(R.id.icon);
		alias = rootView.findViewById(R.id.alias);
		owner = rootView.findViewById(R.id.owner);
		email = rootView.findViewById(R.id.email);
		settings = rootView.findViewById(R.id.settings);
		picture = rootView.findViewById(R.id.picture);

		businessDesc = rootView.findViewById(R.id.businessDesc);
		businessSlogan = rootView.findViewById(R.id.businessSlogan);
		businessPhone = rootView.findViewById(R.id.businessPhone);
		businessAddress = rootView.findViewById(R.id.businessAddress);

		pictures = rootView.findViewById(R.id.pictures);
		info = rootView.findViewById(R.id.info);

		rvSchedules = rootView.findViewById(R.id.rvSchedules);
		listSchedules = new ArrayList<>();
		scheduleArray = new JSONArray();

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lSettings);
			}
		});

		picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lPicture);
			}
		});

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
					toastAdapter.makeToast(R.drawable._fav, "Esta opción estará disponible pronto...");
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
			glideAdapter.setImageCircle(picture, business.getPicture());
		}
		alertDialog = builder.create();
		if (layout == lPicture)
			alertDialog.setCancelable(true);
		alertDialog.show();
	}

	private void getProfile() {
		String url = Network.Business+ preferencesAdapter.getId();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					business = new BusinessesModel();
					JSONObject jsonObject = response.getJSONObject("business");
					business.setId(jsonObject.getString("_id"));
					// Owner info
					business.setOwner(jsonObject.getString("owner"));
					business.setEmail(jsonObject.getString("email"));
					// Business info
					business.setLogo(jsonObject.getString("logo"));
					business.setName(jsonObject.getString("name"));
					business.setDesc(jsonObject.getString("desc"));
					business.setSlogan(jsonObject.getString("slogan"));
					business.setPhone(jsonObject.getString("phone"));
					business.setAddress(jsonObject.getString("address"));
					business.setFb(jsonObject.getString("fb"));
					business.setIg(jsonObject.getString("ig"));
					business.setWa(jsonObject.getString("wa"));
					business.setSchedule(jsonObject.getString("schedule"));
					business.setCategories(jsonObject.getString("categories"));
					JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
					JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(0);
					business.setPicture(jsonObjectPicture.getString("picture"));
					business.setPictures(jsonObject.getString("pictures"));
					business.setLat(jsonObject.getDouble("lat"));
					business.setLng(jsonObject.getDouble("lng"));
					//business.setMembershipId(jsonObject.getString("membershipId"));

					alias.setText(business.getName());
					owner.setText(business.getOwner());
					email.setText(business.getEmail());

					glideAdapter.setImageCircle(icon, business.getLogo());
					glideAdapter.setImageDefault(picture, business.getPicture());
					Objects.requireNonNull(businessDesc.getEditText()).setText(business.getDesc());
					Objects.requireNonNull(businessSlogan.getEditText()).setText(business.getSlogan());
					Objects.requireNonNull(businessPhone.getEditText()).setText(business.getPhone());
					Objects.requireNonNull(businessAddress.getEditText()).setText(business.getAddress());

					scheduleArray = new JSONArray(business.getSchedule());

					getSchedules();
					pictures.setVisibility(View.VISIBLE);
					info.setVisibility(View.VISIBLE);
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

	private void getSchedules() {
		try {
			for (int i = 0; i < scheduleArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) scheduleArray.get(i);
				Iterator<String> keys = jsonObject.keys();
				SchedulesModel schedule = new SchedulesModel();
				while(keys.hasNext()) {
					String key = keys.next();
					schedule.setDay(key);
					schedule.setTime(jsonObject.get(key).toString());
					//days.add(key);
				}
				listSchedules.add(schedule);
			}
			LinearLayoutManager layoutManager = new LinearLayoutManager(context);
			layoutManager.setOrientation(RecyclerView.VERTICAL);
			rvSchedules.setLayoutManager(layoutManager);
			rvSchedules.setHasFixedSize(true);
			schedulesAdapter = new SchedulesAdapter(context, listSchedules);
			rvSchedules.setAdapter(schedulesAdapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}