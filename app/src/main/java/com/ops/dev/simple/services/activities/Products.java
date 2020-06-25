package com.ops.dev.simple.services.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.ops.dev.simple.services.adapters.BusinessesAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.ProductsAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.ProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {

    //Vars
    RecyclerView rvProducts;
    List<ProductsModel> listProducts;
	ProductsAdapter productsAdapter;
    Context context ;
	RequestQueue queue;
    String businessId, businessName, businessIcon;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		View layout = findViewById(android.R.id.content);
		context = Products.this;

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		queue = Volley.newRequestQueue(context);

		BusinessesModel business = (BusinessesModel) getIntent().getSerializableExtra("business");
		businessId = business.getId();
		businessName = business.getName();
		businessIcon = business.getLogo();

		final TextView tittle = findViewById(R.id.tittle);
		final ImageView icon = findViewById(R.id.icon);

		tittle.setText(businessName);
		glideAdapter.setImage(icon, businessIcon);

		rvProducts = findViewById(R.id.rvProducts);
		listProducts = new ArrayList<>();
		getProductByBusiness(businessId);
    }

    private void getProductByBusiness(final String businessId) {
    	String url = Network.ListProductsByBusiness+businessId;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String __message = response.getString("message");
					if (__message.equals("Ok")) {
						if (response.getJSONArray("products").length() > 0) {
							JSONArray jsonArray = response.getJSONArray("products");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								ProductsModel product = new ProductsModel();
								product.setId(jsonObject.getString("_id"));
								product.setName(jsonObject.getString("name"));
								product.setDescription(jsonObject.getString("description"));
								product.setAvailable(jsonObject.getString("available"));
								JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
								for (int j = 0 ; j <jsonArrayPictures.length(); j++ ) {
									JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(j);
									product.setPicture(jsonObjectPicture.getString("picture"));
								}
								product.setPictures(jsonObject.getString("pictures"));
								JSONArray jsonArrayPrices = jsonObject.getJSONArray("prices");
								for (int j = 0 ; j <jsonArrayPrices.length(); j++ ) {
									JSONObject jsonObjectPrice = jsonArrayPrices.getJSONObject(j);
									product.setPrice(jsonObjectPrice.getString("price"));
								}
								product.setPrices(jsonObject.getString("prices"));
								product.setStatus(jsonObject.getString("status"));
								product.setCategories(jsonObject.getString("categories"));
								product.setIdBusiness(jsonObject.getString("idBusiness"));
								listProducts.add(product);
							}
							RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
							rvProducts.setLayoutManager(layoutManager);
							productsAdapter = new ProductsAdapter(context, listProducts);
							rvProducts.setAdapter(productsAdapter);
						}
					} else {
						toastAdapter.makeToast(R.drawable.__info, "No se encontraron productos para el negocio " + businessName);
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