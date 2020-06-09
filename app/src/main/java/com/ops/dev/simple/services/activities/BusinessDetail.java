package com.ops.dev.simple.services.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesIconAdapter;
import com.ops.dev.simple.services.adapters.CommentsAdapter;
import com.ops.dev.simple.services.adapters.ViewAnimationAdapter;
import com.ops.dev.simple.services.adapters.ViewPagerAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.CommentsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusinessDetail extends AppCompatActivity implements OnMapReadyCallback {

    //Vars
    RecyclerView rvComments;
    List<CommentsModel> listComments;
    CommentsAdapter commentsAdapter;
    RecyclerView rvCategories;
    List<CategoriesIconModel> listCategories;
    CategoriesIconAdapter categoriesIconAdapter;
    Context context;
    RequestQueue queue;
    String businessId, businessName;
    double latitude, longitude;
    float zoomLevel;
    int icon;
    GoogleMap map;

    ViewPager viewPager;

    String catName;
    JSONArray picturesArray, networksArray, categoriesArray;

    ImageView btnContact, btnSchedule, btnProducts, btnCoupons;

    FloatingActionButton options, fb, ig, wa;
    String businessFb, businessIg, businessWa;
    boolean isRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_bussiness_detail);
        View layout = findViewById(android.R.id.content);
        context = BusinessDetail.this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.businessMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        zoomLevel = 16.0f;
        viewPager = findViewById(R.id.pictures);

        final BusinessesModel business = (BusinessesModel) getIntent().getSerializableExtra("business");
        businessId = business.getId();
        businessName = business.getName();

        final TextView tittle = findViewById(R.id.tittle);
        final ImageView logo = findViewById(R.id.logo);

        tittle.setText(businessName);
        Glide
                .with(context)
                .load(business.getLogo())
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(logo);

        final TextView description = findViewById(R.id.description);
        description.setText(business.getDescription());

        latitude = Double.parseDouble(business.getLatitude());
        longitude = Double.parseDouble(business.getLongitude());

        try {
            picturesArray = new JSONArray(business.getPictures());
            networksArray = new JSONArray(business.getNetworks());
            categoriesArray = new JSONArray(business.getCategories());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rvCategories = findViewById(R.id.rvCategories);
        listCategories = new ArrayList<>();

        rvComments = findViewById(R.id.rvComments);
        listComments = new ArrayList<>();

        queue = Volley.newRequestQueue(BusinessDetail.this);
        getPictures();
        getNetworks();
        getIconCategories();
        getCommentsByEstablishment();


        // FloatingActionButtons
        options = findViewById(R.id.options);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimationAdapter.rotate(v, !isRotate);
            }
        });

        fb = findViewById(R.id.fb);
        ig = findViewById(R.id.ig);
        wa = findViewById(R.id.wa);

        ViewAnimationAdapter.init(fb);
        ViewAnimationAdapter.init(ig);
        ViewAnimationAdapter.init(wa);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimationAdapter.rotate(v, !isRotate);
                if(isRotate){
                    ViewAnimationAdapter.show(fb);
                    ViewAnimationAdapter.show(ig);
                    ViewAnimationAdapter.show(wa);
                }else{
                    ViewAnimationAdapter.hide(fb);
                    ViewAnimationAdapter.hide(ig);
                    ViewAnimationAdapter.hide(wa);
                }

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
            for(int i = 0; i < picturesArray.length() ; i++) {
                JSONObject jsonObject = picturesArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("picture"));
            }
            if (viewPager != null) {
                viewPager.setAdapter(new ViewPagerAdapter(BusinessDetail.this, arrayList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNetworks() {
        try {
            for(int i = 0; i < networksArray.length() ; i++) {
                JSONObject jsonObject = networksArray.getJSONObject(i);
                try {
                    businessFb = jsonObject.getString("fb");
                    businessIg = jsonObject.getString("ig");
                    businessWa = jsonObject.getString("wa");
                }catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getIconCategories() {
        try {
            for(int i = 0; i < categoriesArray.length() ; i++) {
                JSONObject jsonObject = categoriesArray.getJSONObject(i);
                CategoriesIconModel category = new CategoriesIconModel();
                category.setName(jsonObject.getString("category"));
                catName = jsonObject.getString("category");
                switch (catName) {
                    case "Comestibles":
                        icon = BusinessDetail.this.getResources().getIdentifier("cat_comestibles" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Bebidas":
                        icon = BusinessDetail.this.getResources().getIdentifier("cat_bebidas" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Medicamentos":
                        icon = BusinessDetail.this.getResources().getIdentifier("cat_medicamentos" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Canasata Básica":
                        icon = BusinessDetail.this.getResources().getIdentifier("cat_canasta" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Veterinaria":
                        icon = BusinessDetail.this.getResources().getIdentifier("cat_veterinaria" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    default:
                        icon = BusinessDetail.this.getResources().getIdentifier("_fav" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                }
                category.setIcon(icon);
                listCategories.add(category);
            }
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(RecyclerView.HORIZONTAL);
            rvCategories.setLayoutManager(llm);
            rvCategories.setHasFixedSize(true);
            categoriesIconAdapter = new CategoriesIconAdapter(context, listCategories);
            rvCategories.setAdapter(categoriesIconAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCommentsByEstablishment() {
        CommentsModel comment1 = new CommentsModel();
        comment1.setId("5d815fa8a51bd02b1dc69sdg");
        comment1.setComment("Los mejores precios");
        comment1.setDate("20/02/2020");
        comment1.setIdUser("");
        comment1.setNameUser("Juana María");
        comment1.setPictureUser("https://scontent.fsal1-1.fna.fbcdn.net/v/t1.0-9/101084485_110487594025906_642627768630116352_n.jpg?_nc_cat=111&_nc_sid=09cbfe&_nc_ohc=FXFV4E-EiU0AX-bs2u2&_nc_ht=scontent.fsal1-1.fna&oh=f0184477086f579c4f2c4b875826998c&oe=5F013606");
        comment1.setIdBusiness("");
        listComments.add(comment1);

        CommentsModel comment2 = new CommentsModel();
        comment2.setId("5d815fa8a51bd02b1dc69sdg");
        comment2.setComment("Encuentras de todo");
        comment2.setDate("17/04/2020");
        comment2.setIdUser("");
        comment2.setNameUser("Carlos Alberto");
        comment2.setPictureUser("https://scontent.fsal1-1.fna.fbcdn.net/v/t1.0-9/101084485_110487594025906_642627768630116352_n.jpg?_nc_cat=111&_nc_sid=09cbfe&_nc_ohc=FXFV4E-EiU0AX-bs2u2&_nc_ht=scontent.fsal1-1.fna&oh=f0184477086f579c4f2c4b875826998c&oe=5F013606");
        comment2.setIdBusiness("");
        listComments.add(comment2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvComments.setLayoutManager(layoutManager);
        rvComments.setHasFixedSize(true);
        commentsAdapter = new CommentsAdapter(context, listComments);
        rvComments.setAdapter(commentsAdapter);
    }

    public void fbIntent() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(businessFb)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(businessFb)));
        }
    }

    public void igIntent() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(businessIg)));
        } catch (ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")));
        }
    }

    public void waIntent() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(businessWa));
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
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