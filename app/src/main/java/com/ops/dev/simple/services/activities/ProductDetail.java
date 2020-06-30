package com.ops.dev.simple.services.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesIconAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.PicturesPagerAdapter;
import com.ops.dev.simple.services.adapters.SharedPreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.CategoriesIconModel;
import com.ops.dev.simple.services.models.ProductsModel;
import com.ops.dev.simple.services.widget.CustomNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProductDetail extends AppCompatActivity {

    //Vars
    RecyclerView rvCategories;
    List<CategoriesIconModel> listCategories;
    CategoriesIconAdapter categoriesIconAdapter;
    Context context;
    RequestQueue queue;
    ToastAdapter toastAdapter;
    GlideAdapter glideAdapter;

    TextView price;
    ViewPager viewPager;
    CustomNumberPicker picker;
    Button add_to_cart;

    String productId, productName, productDescription, productPrice, productBusinessId;

    String catId, catName;

    JSONArray picturesArray, categoriesArray;

    SharedPreferencesAdapter sharedPreferencesAdapter;
    String __message;

    View layout;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);
        layout = findViewById(android.R.id.content);
        context = ProductDetail.this;

        toastAdapter = new ToastAdapter(context);
        glideAdapter = new GlideAdapter(context);
        sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
        queue = Volley.newRequestQueue(context);

        viewPager = findViewById(R.id.pictures);
        picker = findViewById(R.id.picker);
        add_to_cart = findViewById(R.id.add_to_cart);

        final ProductsModel product = (ProductsModel) getIntent().getSerializableExtra("product");
        productId = product.getId();
        productName = product.getName();
        productPrice = product.getPrice();
        productDescription = product.getDescription();
        productBusinessId = product.getIdBusiness();

        final TextView tittle = findViewById(R.id.tittle);
        final TextView description = findViewById(R.id.description);
        final ImageView logo = findViewById(R.id.logo);
        price = findViewById(R.id.price);

        tittle.setText(productName);
        description.setText(productDescription);
        price.setText(productPrice);

        try {
            picturesArray = new JSONArray(product.getPictures());
            categoriesArray = new JSONArray(product.getCategories());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rvCategories = findViewById(R.id.rvCategories);
        listCategories = new ArrayList<>();

        getPictures();
        getIconCategories();

        picker.setOnValueChangedListener(new CustomNumberPicker.OnValueChangedListener() {
            @Override
            public void onValueChanged(int newValue) {
                updatePrice();
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                updateCart(sharedPreferencesAdapter.getCartId(), product.getIdBusiness(), productId, value);
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

    private void updatePrice() {
        double _price = Double.parseDouble(productPrice) * picker.getValue();
        String res = String.format(Locale.US,"%.2f", _price);
        price.setText(res);
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

    private void updateCart(String cartId, String businessId, String productId, int qty) {
        String url = Network.Cart+cartId+"/"+businessId;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("productId", productId);
            jsonParams.put("qty", qty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    __message = response.getString("message");
                    int value = picker.getValue();
                    final String message = context.getResources().getQuantityString(R.plurals.items_added_to_cart, value, value);
                    final int actionTextColor = ContextCompat.getColor(context, R.color.colorAccent);
                    Handler h = new Handler();
                    switch (__message) {
                        case "Wrong business":
                            builder = new AlertDialog.Builder(context);
                            builder.setTitle("¡Vaya! Nos hemos topado con una barricada...");
                            builder.setMessage("Ya tienes un carrito de otro negocio ☹ ¿Deseas cancelarlo y crear uno nuevo para el negocio actual 😁?");
                            builder.setNegativeButton("Continuar con el carrito anterior", null);
                            builder.setPositiveButton("Crear carrito nuevo", null);
                            alertDialog = builder.create();
                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                    Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Handler h = new Handler();
                                            h.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    Intent intent = new Intent(context, MainMenu.class);
                                                    intent.putExtra("screen", R.id.cart);
                                                    intent.putExtra("number", 3);
                                                    startActivity(intent);
                                                }
                                            }, 1000);
                                            alertDialog.dismiss();
                                        }
                                    });
                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            deleteCart(sharedPreferencesAdapter.getCartId());
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            break;
                        case "Product updated":
                            toastAdapter.makeToast(R.drawable.__ok, productName + " actualizado");
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.go_to_cart, snackBarClickListener())
                                            .setActionTextColor(actionTextColor)
                                            .show();
                                }
                            }, 2000);
                            break;
                        case "Product added":
                            toastAdapter.makeToast(R.drawable.__ok, productName + " agregado");
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.go_to_cart, snackBarClickListener())
                                            .setActionTextColor(actionTextColor)
                                            .show();
                                }
                            }, 2000);
                            break;
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

    private void deleteCart(final String cartId) {
        String url = Network.Cart+cartId;
        JSONObject jsonParams = new JSONObject();try {
            jsonParams.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    __message = response.getString("message");
                    switch (__message) {
                        case "Error":
                            toastAdapter.makeToast(R.drawable.__error, "Errol al borrar el carrito");
                            break;
                        case "Ok":
                            toastAdapter.makeToast(R.drawable.__warning, "Carrito eliminado, creando nuevo carrito... Por favor agrega tu producto nuevamente");
                            sharedPreferencesAdapter.deleteCartId();
                            sharedPreferencesAdapter.deleteBusinessId();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    createCart(sharedPreferencesAdapter.getUserId(), productBusinessId);
                                }
                            }, 1000);
                            break;
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

    private View.OnClickListener snackBarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent intent = new Intent(context, MainMenu.class);
                        intent.putExtra("screen", R.id.cart);
                        intent.putExtra("number", 3);
                        startActivity(intent);
                    }
                }, 1000);
            }
        };
    }
}