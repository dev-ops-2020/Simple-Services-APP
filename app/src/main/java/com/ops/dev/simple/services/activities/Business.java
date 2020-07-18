package com.ops.dev.simple.services.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.ops.dev.simple.services.adapters.BusinessesAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesModel;
import com.ops.dev.simple.services.models.CategoriesModelListIcon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Business extends AppCompatActivity {

    //Vars
    RecyclerView rvBusinesses;
    List<BusinessesModel> listBusinesses;
	BusinessesAdapter businessesAdapter;

	ImageView filter;
	String filterName;

    Context context;
    RequestQueue queue;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

    String categoryId, categoryName;
    int categoryIcon;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	private static int lFilter = R.layout.__modal_filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		View layout = findViewById(android.R.id.content);
		context = Business.this;
		queue = Volley.newRequestQueue(context);
		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);

		try {
			CategoriesModel category = (CategoriesModel) getIntent().getSerializableExtra("category");
			categoryId = category.getId();
			categoryName = category.getName();
			categoryIcon = category.getIcon();
		} catch (Exception ex) {
			CategoriesModelListIcon categoryI = (CategoriesModelListIcon) getIntent().getSerializableExtra("category");
			categoryId = categoryI.getId();
			categoryName = categoryI.getName();
			categoryIcon = categoryI.getIcon();
		}

		final TextView tittle = findViewById(R.id.tittle);
		final ImageView icon = findViewById(R.id.icon);

		tittle.setText(categoryName);
		glideAdapter.setImageCircle(icon, categoryIcon);

		rvBusinesses = findViewById(R.id.rvBusinesses);
		listBusinesses = new ArrayList<>();

		if (preferencesAdapter.keyExists("filter"))
			filterName = preferencesAdapter.getFilter();
		else
			filterName = "def";

		getBusinessesByCategory(categoryId, filterName, preferencesAdapter.getLat(), preferencesAdapter.getLng(), 10000);

		filter = findViewById(R.id.filter);
		filter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showAlertDialog(lFilter);
			}
		});
	}

	private void showAlertDialog(final int layout) {
		final View layoutView = getLayoutInflater().inflate(layout, null);
		builder = new AlertDialog.Builder(this);
		builder.setView(layoutView);
		//Vars
		final RadioButton rdbDef = layoutView.findViewById(R.id.def);
		final RadioButton rdbNear = layoutView.findViewById(R.id.near);
		final RadioButton rdbScore = layoutView.findViewById(R.id.score);

		if (filterName.equals("def"))
			rdbDef.setChecked(true);
		else if(filterName.equals("near"))
			rdbNear.setChecked(true);
		else if(filterName.equals("score"))
			rdbScore.setChecked(true);

		rdbDef.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (rdbDef.isChecked()) {
					rdbScore.setChecked(false);
					rdbNear.setChecked(false);
				}
			}
		});
		rdbNear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (rdbNear.isChecked()) {
					rdbDef.setChecked(false);
					rdbScore.setChecked(false);
				}
			}
		});
		rdbScore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (rdbScore.isChecked()) {
					rdbDef.setChecked(false);
					rdbNear.setChecked(false);
				}
			}
		});

		builder.setPositiveButton("Ok", null);
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				btnOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						clearBusinessList();
						if (rdbDef.isChecked()) {
							preferencesAdapter.setFilter("def");
							alertDialog.dismiss();
						} else if (rdbScore.isChecked()) {
							preferencesAdapter.setFilter("score");
							alertDialog.dismiss();
						} else if (rdbNear.isChecked()) {
							preferencesAdapter.setFilter("near");
							alertDialog.dismiss();
						}
						filterName = preferencesAdapter.getFilter();
						getBusinessesByCategory(categoryId, filterName, preferencesAdapter.getLat(), preferencesAdapter.getLng(), 10000);
					}
				});
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	private void clearBusinessList() {
		listBusinesses.clear();
		LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		rvBusinesses.setLayoutManager(layoutManager);
		businessesAdapter = new BusinessesAdapter(context, listBusinesses);
		rvBusinesses.setAdapter(businessesAdapter);
	}

	private void getBusinessesByCategory(final String categoryId, final String filter, final Float lat, final Float lng, final int rad) {
		String url = Network.ListBusinessByCategory+categoryId+"/"+filter+"/"+lat+"/"+lng+"/"+rad;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String __message = response.getString("message");
					if (__message.equals("Ok")) {
						if (response.getJSONArray("businesses").length() > 0) {
							JSONArray jsonArray = response.getJSONArray("businesses");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								BusinessesModel business = new BusinessesModel();
								business.setId(jsonObject.getString("_id"));
								// Owner info
								business.setOwner(jsonObject.getString("owner"));
								business.setEmail(jsonObject.getString("email"));
								// Business info
								business.setType(jsonObject.getString("type"));
								business.setLogo(jsonObject.getString("logo"));
								business.setName(jsonObject.getString("name"));
								business.setDesc(jsonObject.getString("desc"));
								business.setSlogan(jsonObject.getString("slogan"));
								business.setPhone(jsonObject.getString("phone"));
								business.setAddress(jsonObject.getString("address"));
								business.setLat(jsonObject.getDouble("lat"));
								business.setLng(jsonObject.getDouble("lng"));
								JSONObject jsonObjectDist = jsonObject.getJSONObject("dist");
								business.setDist(jsonObjectDist.getDouble("calculated"));
								business.setFb(jsonObject.getString("fb"));
								business.setIg(jsonObject.getString("ig"));
								business.setWa(jsonObject.getString("wa"));
								business.setDelivery(jsonObject.getBoolean("delivery"));
								business.setSchedule(jsonObject.getString("schedule"));
								business.setCategories(jsonObject.getString("categories"));
								JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
								JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(0);
								business.setPicture(jsonObjectPicture.getString("picture"));
								business.setPictures(jsonObject.getString("pictures"));
								listBusinesses.add(business);
							}
							LinearLayoutManager layoutManager = new LinearLayoutManager(context);
							layoutManager.setOrientation(RecyclerView.VERTICAL);
							rvBusinesses.setLayoutManager(layoutManager);
							businessesAdapter = new BusinessesAdapter(context, listBusinesses);
							rvBusinesses.setAdapter(businessesAdapter);
						}
					} else {
						toastAdapter.makeToast(R.drawable.__info, "No se encontraron negocios en la categoría " + categoryName);
						finish();
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
}