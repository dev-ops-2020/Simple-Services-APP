package com.ops.dev.simple.services.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.CategoriesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Businesses extends AppCompatActivity {

    //Vars
    RecyclerView rvBusinesses;
    List<BusinessesModel> listBusinesses;
	BusinessesAdapter businessesAdapter;
    Context context ;
	RequestQueue queue;
    String categoryId, categoryName;
    int categoryIcon;
	ToastAdapter toastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businesses);
		View layout = findViewById(android.R.id.content);
		context = Businesses.this;

		try {
			CategoriesModel category = (CategoriesModel) getIntent().getSerializableExtra("category");
			categoryId = category.getId();
			categoryName = category.getName();
			categoryIcon = category.getIcon();
		} catch (Exception ex) {
			CategoriesIconModel categoryI = (CategoriesIconModel) getIntent().getSerializableExtra("category");
			categoryId = categoryI.getId();
			categoryName = categoryI.getName();
			categoryIcon = categoryI.getIcon();
		}

		final TextView tittle = findViewById(R.id.tittle);
		final ImageView icon = findViewById(R.id.icon);

		tittle.setText(categoryName);
		icon.setImageResource(categoryIcon);

		rvBusinesses = findViewById(R.id.rvBusinesses);
		listBusinesses = new ArrayList<>();

		queue = Volley.newRequestQueue(context);
		toastAdapter = new ToastAdapter(context);
		getBusinessesByCategory(categoryId);
    }

    private void getBusinessesByCategory(final String categoryId) {
    	String url = Network.ListBusinessByCategory+categoryId;
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
								business.setName(jsonObject.getString("name"));
								business.setDescription(jsonObject.getString("description"));
								business.setSlogan(jsonObject.getString("slogan"));
								business.setOwner(jsonObject.getString("owner"));
								business.setScore(jsonObject.getString("score"));
								business.setStatus(jsonObject.getString("status"));
								business.setLogo(jsonObject.getString("logo"));
								JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
								for (int j = 0 ; j <jsonArrayPictures.length(); j++ ) {
									JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(j);
									business.setPicture(jsonObjectPicture.getString("picture"));
								}
								business.setPictures(jsonObject.getString("pictures"));
								business.setPhones(jsonObject.getString("phones"));
								business.setSchedule(jsonObject.getString("schedule"));
								business.setNetworks(jsonObject.getString("networks"));
								business.setCategories(jsonObject.getString("categories"));
								business.setLatitude(jsonObject.getString("latitude"));
								business.setLongitude(jsonObject.getString("longitude"));
								business.setIdMembership(jsonObject.getString("idMembership"));
								listBusinesses.add(business);
							}
							LinearLayoutManager layoutManager = new LinearLayoutManager(context);
							layoutManager.setOrientation(RecyclerView.VERTICAL);
							rvBusinesses.setLayoutManager(layoutManager);
							businessesAdapter = new BusinessesAdapter(context, listBusinesses);
							rvBusinesses.setAdapter(businessesAdapter);
						}
					} else {
						toastAdapter.makeToast("No se encontraron negocios en la categoría " + categoryName, R.drawable.__info);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				toastAdapter.makeToast(error.toString(), R.drawable.__error);
			}
		});
		queue.add(request);
	}
}