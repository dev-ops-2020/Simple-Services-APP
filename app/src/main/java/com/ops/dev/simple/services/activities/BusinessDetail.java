package com.ops.dev.simple.services.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesIconAdapter;
import com.ops.dev.simple.services.adapters.CommentsAdapter;
import com.ops.dev.simple.services.adapters.FabAnimationAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.PicturesPagerAdapter;
import com.ops.dev.simple.services.adapters.ProductsAdapterMini;
import com.ops.dev.simple.services.adapters.SchedulesAdapter;
import com.ops.dev.simple.services.adapters.SharedPreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.CommentsModel;
import com.ops.dev.simple.services.models.ProductsModel;
import com.ops.dev.simple.services.models.SchedulesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BusinessDetail extends AppCompatActivity implements OnMapReadyCallback {

    //Vars
    RecyclerView rvCategories;
    List<CategoriesIconModel> listCategories;
    CategoriesIconAdapter categoriesIconAdapter;

    RecyclerView rvSchedules;
    List<SchedulesModel> listSchedules;
    SchedulesAdapter schedulesAdapter;

    RecyclerView rvProducts;
    List<ProductsModel> listProducts;
    ProductsAdapterMini productsAdapter;

    RecyclerView rvComments;
    List<CommentsModel> listComments;
    CommentsAdapter commentsAdapter;

    JSONArray picturesArray, categoriesArray, scheduleArray;
    ArrayList<String> days;

    Context context;
    RequestQueue queue;
    ToastAdapter toastAdapter;
    GlideAdapter glideAdapter;

    String businessId, businessName;
    double latitude, longitude;
    float zoomLevel;
    GoogleMap map;

    ViewPager viewPager;

    String catId;

    FloatingActionButton options, phone, fb, ig, wa;
    String businessPhone, businessFb, businessIg, businessWa;
    boolean isRotate;

    SharedPreferencesAdapter sharedPreferencesAdapter;
    String __message;

    String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_bussiness_detail);
        View layout = findViewById(android.R.id.content);
        context = BusinessDetail.this;

        toastAdapter = new ToastAdapter(context);
        glideAdapter = new GlideAdapter(context);
        sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
        queue = Volley.newRequestQueue(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.businessMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        zoomLevel = 16.0f;

        viewPager = findViewById(R.id.pictures);

        final BusinessesModel business = (BusinessesModel) getIntent().getSerializableExtra("business");
        businessId = business.getId();
        businessName = business.getName();
        businessPhone = business.getPhone();
        businessFb = business.getFb();
        businessIg = business.getIg();
        businessWa = business.getWa();

        final TextView tittle = findViewById(R.id.tittle);
        final ImageView logo = findViewById(R.id.logo);
        final TextView description = findViewById(R.id.description);

        tittle.setText(businessName);
        glideAdapter.setImage(logo, business.getLogo());
        description.setText(business.getDescription());

        latitude = Double.parseDouble(business.getLatitude());
        longitude = Double.parseDouble(business.getLongitude());

        try {
            picturesArray = new JSONArray(business.getPictures());
            categoriesArray = new JSONArray(business.getCategories());
            scheduleArray = new JSONArray(business.getSchedule());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rvCategories = findViewById(R.id.rvCategories);
        listCategories = new ArrayList<>();

        rvSchedules = findViewById(R.id.rvSchedules);
        listSchedules = new ArrayList<>();

        rvProducts = findViewById(R.id.rvProducts);
        listProducts = new ArrayList<>();

        rvComments = findViewById(R.id.rvComments);
        listComments = new ArrayList<>();

        getPictures();
        getIconCategories();
        getSchedules();
        getProductsByBusiness(businessId);
        getCommentsByEstablishment(businessId);

        if (!sharedPreferencesAdapter.keyExists("cartId"))
            createCart(sharedPreferencesAdapter.getUserId(), businessId);

        findViewById(R.id.morePromotions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Products.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("business", business);
                startActivity(intent);
            }
        });

        // FloatingActionButtons
        options = findViewById(R.id.options);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = FabAnimationAdapter.rotate(v, !isRotate);
                if(isRotate) {
                    FabAnimationAdapter.show(phone);
                    FabAnimationAdapter.show(fb);
                    FabAnimationAdapter.show(ig);
                    FabAnimationAdapter.show(wa);
                } else {
                    FabAnimationAdapter.hide(phone);
                    FabAnimationAdapter.hide(fb);
                    FabAnimationAdapter.hide(ig);
                    FabAnimationAdapter.hide(wa);
                }
            }
        });

        phone = findViewById(R.id.phone);
        fb = findViewById(R.id.fb);
        ig = findViewById(R.id.ig);
        wa = findViewById(R.id.wa);

        FabAnimationAdapter.init(phone);
        FabAnimationAdapter.init(fb);
        FabAnimationAdapter.init(ig);
        FabAnimationAdapter.init(wa);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneIntent();
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbIntent();
            }
        });
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                igIntent();
            }
        });
        wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waIntent();
            }
        });
    }

    private void getPictures() {
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < picturesArray.length(); i++) {
                JSONObject jsonObject = picturesArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("picture"));
            }
            if (viewPager != null) {
                viewPager.setAdapter(new PicturesPagerAdapter(context, arrayList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getIconCategories() {
        try {
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject jsonObject = categoriesArray.getJSONObject(i);
                final CategoriesIconModel category = new CategoriesIconModel();
                catId = jsonObject.getString("category");
                String url = Network.ListCategories+catId;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("category");
                        category.setId(jsonObject.getString("_id"));
                        category.setName(jsonObject.getString("name"));
                        int icon = context.getResources().getIdentifier(jsonObject.getString("icon"), "drawable", context.getPackageName());
                        category.setIcon(icon);
                        listCategories.add(category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    rvCategories.setLayoutManager(layoutManager);
                    rvCategories.setHasFixedSize(true);
                    categoriesIconAdapter = new CategoriesIconAdapter(context, listCategories);
                    rvCategories.setAdapter(categoriesIconAdapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            queue.add(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSchedules() {
        /*
        Locale locale = context.getResources().getConfiguration().locale;

        forceLocale(context, "en");

        //LanguageHelper.setAppLocale(this, "en");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.forLanguageTag(locale.getLanguage()));
        Date d = new Date();
        currentDay = sdf.format(d);
        toastAdapter.makeToast(R.drawable._fav, locale.getLanguage() + " " + currentDay);
        days = new ArrayList<>();
        for (int i = 0; i < days.size(); i ++) {
            toastAdapter.makeToast(R.drawable._fav, days.get(i));
        }
        if (days.contains(currentDay)) {

        }
        */
        try {
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject jsonObject = scheduleArray.getJSONObject(i);
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
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvSchedules.setLayoutManager(layoutManager);
            rvSchedules.setHasFixedSize(true);
            schedulesAdapter = new SchedulesAdapter(context, listSchedules);
            rvSchedules.setAdapter(schedulesAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProductsByBusiness(String businessId) {
        String url = Network.ListProductsByBusiness+businessId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String __message = response.getString("message");
                    if (__message.equals("Ok")) {
                        if (response.getJSONArray("products").length() > 0) {
                            JSONArray jsonArray = response.getJSONArray("products");
                            for (int i = 0; i < 5; i++) {
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
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            rvProducts.setLayoutManager(layoutManager);
                            rvProducts.setHasFixedSize(true);
                            productsAdapter = new ProductsAdapterMini(context, listProducts);
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

    private void getCommentsByEstablishment(String businessId) {
        String url = Network.ListCommentsByBusiness+businessId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("comments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CommentsModel comment = new CommentsModel();
                        comment.setId(jsonObject.getString("_id"));
                        comment.setComment(jsonObject.getString("comment"));
                        comment.setDate(jsonObject.getString("date"));
                        comment.setIdUser(jsonObject.getString("idUser"));
                        comment.setNameUser(jsonObject.getString("nameUser"));
                        comment.setPictureUser(jsonObject.getString("pictureUser"));
                        comment.setIdBusiness(jsonObject.getString("idBusiness"));
                        listComments.add(comment);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    rvComments.setLayoutManager(layoutManager);
                    rvComments.setHasFixedSize(true);
                    commentsAdapter = new CommentsAdapter(context, listComments);
                    rvComments.setAdapter(commentsAdapter);
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

    private void createCart(String userId, final String businessId) {
        String url = Network.Cart;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userId", userId);
            jsonParams.put("businessId", businessId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    __message = response.getString("message");
                    if (__message.equals("Ok")) {
                        sharedPreferencesAdapter.setCartId(response.getString("cart"));
                        sharedPreferencesAdapter.setBusinessId(businessId);
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

    public void phoneIntent() {
        toastAdapter.makeToast(R.drawable._phone, "llamando a " + businessPhone);
        Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ businessPhone));
        startActivity(dial);
    }

    public void fbIntent() {
        if (businessFb == null)
            toastAdapter.makeToast(R.drawable.__warning, "Este negocio no ha vinculado aún su cuenta de Facebook");
        else {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(businessFb)));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://fb.com")));
            }
        }
    }

    public void igIntent() {
        if (businessIg == null)
            toastAdapter.makeToast(R.drawable.__warning, "Este negocio no ha vinculado aún su cuenta de Instagram");
        else {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(businessIg)));
            } catch (ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")));
            }
        }
    }

    public void waIntent() {
        if (businessWa == null)
            toastAdapter.makeToast(R.drawable.__warning, "Este negocio no ha vinculado aún su cuenta de WhatsApp");
        else {
            try {
                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                sendMsg.setPackage("com.whatsapp");
                sendMsg.setData(Uri.parse(businessWa));
                startActivity(sendMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng loc = new LatLng(latitude, longitude);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(loc).title(businessName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}