package com.ops.dev.simple.services.activities.fragments;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.SignIn;
import com.ops.dev.simple.services.adapters.ToastAdapter;

public class Profile extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	// Vars
	Boolean isLoggedIn = false;
	ImageView settings;
	RelativeLayout main;

	static int lSettings = R.layout.__modal_settings;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	ToastAdapter toastAdapter;

	private OnFragmentInteractionListener mListener;

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

		settings = rootView.findViewById(R.id.settings);
		main = rootView.findViewById(R.id.main);

		toastAdapter = new ToastAdapter(getActivity());

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lSettings);
			}
		});

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
				toastAdapter.makeToast(theme.toString(), R.drawable.__info);
			}
		});
		alertDialog = builder.create();
		alertDialog.show();
	}
}