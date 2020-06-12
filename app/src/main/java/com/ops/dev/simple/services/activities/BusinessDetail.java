package com.ops.dev.simple.services.activities;

import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
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
import com.ops.dev.simple.services.adapters.FabAnimationAdapter;
import com.ops.dev.simple.services.adapters.PhonesAdapter;
import com.ops.dev.simple.services.adapters.SchedulesAdapter;
import com.ops.dev.simple.services.adapters.ViewPagerAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.CommentsModel;
import com.ops.dev.simple.services.models.PhonesModel;
import com.ops.dev.simple.services.models.SchedulesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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
    GoogleMap map;

    ViewPager viewPager;

    String catName;
    int catIcon;

    JSONArray picturesArray, categoriesArray, networksArray;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    ImageView btnContact, btnSchedule, btnProducts, btnCoupons;

    static int lPhones = R.layout.__modal_business_phones;
    static int lSchedule = R.layout.__modal_business_schedule;

    JSONArray phonesArray, scheduleArray;

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
            categoriesArray = new JSONArray(business.getCategories());
            networksArray = new JSONArray(business.getNetworks());
            phonesArray = new JSONArray(business.getPhones());
            scheduleArray = new JSONArray(business.getSchedule());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rvCategories = findViewById(R.id.rvCategories);
        listCategories = new ArrayList<>();

        rvComments = findViewById(R.id.rvComments);
        listComments = new ArrayList<>();

        queue = Volley.newRequestQueue(BusinessDetail.this);
        getPictures();
        getIconCategories();
        getCommentsByEstablishment();
        getNetworks();

        // ImageView (Buttons)
        btnContact = findViewById(R.id.btnContact);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnProducts = findViewById(R.id.btnProducts);
        btnCoupons = findViewById(R.id.btnCoupons);

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModal(lPhones);
            }
        });
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModal(lSchedule);
            }
        });
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Intent to Products/Services
            }
        });
        btnCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Intent to Coupons
            }
        });

        // FloatingActionButtons
        options = findViewById(R.id.options);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = FabAnimationAdapter.rotate(v, !isRotate);
                if(isRotate) {
                    FabAnimationAdapter.show(fb);
                    FabAnimationAdapter.show(ig);
                    FabAnimationAdapter.show(wa);
                } else {
                    FabAnimationAdapter.hide(fb);
                    FabAnimationAdapter.hide(ig);
                    FabAnimationAdapter.hide(wa);
                }
            }
        });

        fb = findViewById(R.id.fb);
        ig = findViewById(R.id.ig);
        wa = findViewById(R.id.wa);

        FabAnimationAdapter.init(fb);
        FabAnimationAdapter.init(ig);
        FabAnimationAdapter.init(wa);

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
                viewPager.setAdapter(new ViewPagerAdapter(BusinessDetail.this, arrayList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getIconCategories() {
        try {
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject jsonObject = categoriesArray.getJSONObject(i);
                CategoriesIconModel category = new CategoriesIconModel();
                catName = jsonObject.getString("category");
                //catIcon = context.getResources().getIdentifier(jsonObject.getString("icon"), "drawable", context.getPackageName());
                category.setName(catName);
                //category.setIcon(catIcon);

                switch (catName) {
                    case "Comestibles":
                        catIcon = BusinessDetail.this.getResources().getIdentifier("cat_comestibles" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Bebidas":
                        catIcon = BusinessDetail.this.getResources().getIdentifier("cat_bebidas" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Medicamentos":
                        catIcon = BusinessDetail.this.getResources().getIdentifier("cat_medicamentos" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Canasata Básica":
                        catIcon = BusinessDetail.this.getResources().getIdentifier("cat_canasta" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    case "Veterinaria":
                        catIcon = BusinessDetail.this.getResources().getIdentifier("cat_veterinaria" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                    default:
                        catIcon = BusinessDetail.this.getResources().getIdentifier("_fav" ,"drawable", BusinessDetail.this.getPackageName());
                        break;
                }
                category.setIcon(catIcon);
                listCategories.add(category);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvCategories.setLayoutManager(layoutManager);
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

    private void getNetworks() {
        try {
            for (int i = 0; i < networksArray.length(); i++) {
                JSONObject jsonObject = networksArray.getJSONObject(i);
                if (jsonObject.has("fb"))
                    businessFb = jsonObject.getString("fb");
                else if (jsonObject.has("wa"))
                    businessWa = jsonObject.getString("wa");
                else if (jsonObject.has("ig"))
                    businessIg = jsonObject.getString("ig");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    private void getNetworkss() {
        listNetworks = new ArrayList<>();
        NetworksModel network = new NetworksModel();
        try {
            for (int i = 0; i < networksArray.length(); i++) {
                JSONObject jsonObject = networksArray.getJSONObject(i);
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()) {
                    String key = (String) keys.next();
                    network.setNet(key);
                    network.setUrl(jsonObject.get(key).toString());
                }
                listNetworks.add(network);
            }
            for (int i = 0; i < listNetworks.size(); i++) {
                if (listNetworks.get(i).getNet().equals(network.getNet()))
                    businessWa = listNetworks.get(i).getUrl();
                else
                    wa.hide();
                if (listNetworks.get(i).getNet().equals("fb"))
                    businessWa = listNetworks.get(i).getUrl();
                else
                    fb.hide();
                if (listNetworks.get(i).getNet().equals("ig"))
                    businessWa = listNetworks.get(i).getUrl();
                else
                    ig.hide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        try {
            JSONObject jsonObject0 = networksArray.getJSONObject(0);
            if (jsonObject0.has("wa"))
                businessWa = jsonObject0.getString("wa");
            else
                FabAnimationAdapter.hide(wa);
            JSONObject jsonObject1 = networksArray.getJSONObject(1);
            if (jsonObject1.has("fb"))
                businessFb = jsonObject1.getString("fb");
            else
                FabAnimationAdapter.hide(fb);
            JSONObject jsonObject2 = networksArray.getJSONObject(2);
            if (jsonObject2.has("ig"))
                businessIg = jsonObject2.getString("ig");
            else
                FabAnimationAdapter.hide(ig);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public void fbIntent() {
        if (businessFb == null)
            Toast.makeText(context, "Este negocio no ha vinculado aún su cuenta de Facebook", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Este negocio no ha vinculado aún su cuenta de Instagram", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Este negocio no ha vinculado aún su cuenta de WhastsApp", Toast.LENGTH_SHORT).show();
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

    private void showModal(final int layout) {
        final View layoutView = getLayoutInflater().inflate(layout, null);
        builder = new AlertDialog.Builder(this);
        builder.setView(layoutView);

        //Vars
        final ImageView icon = layoutView.findViewById(R.id.icon);
        final TextView tittle = layoutView.findViewById(R.id.tittle);
        if (layout == lPhones) {
            icon.setImageResource(R.drawable._phone);
            tittle.setText("Contactos");

            final RecyclerView rvPhones = layoutView.findViewById(R.id.rvPhones);
            final List<PhonesModel> listPhones = new ArrayList<>();
            final PhonesAdapter phonesAdapter;

            try {
                for (int i = 0; i < phonesArray.length(); i++) {
                    JSONObject jsonObject = phonesArray.getJSONObject(i);
                    Iterator<String> keys = jsonObject.keys();
                    PhonesModel phone = new PhonesModel();
                    while(keys.hasNext()) {
                        String key = (String) keys.next();
                        phone.setName(key);
                        phone.setNumber(jsonObject.get(key).toString());
                    }
                    listPhones.add(phone);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                rvPhones.setLayoutManager(layoutManager);
                rvPhones.setHasFixedSize(true);
                phonesAdapter = new PhonesAdapter(context, listPhones);
                rvPhones.setAdapter(phonesAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (layout == lSchedule) {
            icon.setImageResource(R.drawable._schedule);
            tittle.setText("Horarios");

            final RecyclerView rvSchedules = layoutView.findViewById(R.id.rvSchedules);
            final List<SchedulesModel> listSchedules = new ArrayList<>();
            final SchedulesAdapter schedulesAdapter;

            try {
                for (int i = 0; i < scheduleArray.length(); i++) {
                    JSONObject jsonObject = scheduleArray.getJSONObject(i);
                    Iterator<String> keys = jsonObject.keys();
                    SchedulesModel schedule = new SchedulesModel();
                    while(keys.hasNext()) {
                        String key = (String) keys.next();
                        schedule.setDay(key);
                        schedule.setTime(jsonObject.get(key).toString());
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
        alertDialog = builder.create();
        alertDialog.show();
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