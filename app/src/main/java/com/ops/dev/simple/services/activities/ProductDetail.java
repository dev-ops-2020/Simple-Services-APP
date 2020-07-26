package com.ops.dev.simple.services.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.PicturesPagerAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.TagsAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.ProductsModel;
import com.ops.dev.simple.services.models.TagsModel;
import com.ops.dev.simple.services.widget.CustomNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetail extends AppCompatActivity {

    //Vars
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

    JSONArray picturesArray, tagsArray;

    RecyclerView rvTags;
    List<TagsModel> listTags;
    TagsAdapter tagsAdapter;

    PreferencesAdapter preferencesAdapter;
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
        preferencesAdapter = new PreferencesAdapter(context);
        queue = Volley.newRequestQueue(context);

        viewPager = findViewById(R.id.pictures);
        picker = findViewById(R.id.picker);
        add_to_cart = findViewById(R.id.add_to_cart);

        final ProductsModel product = (ProductsModel) getIntent().getSerializableExtra("product");
        productId = product.getId();
        productName = product.getName();
        productPrice = product.getPrice();
        productDescription = product.getDesc();
        productBusinessId = product.getBusinessId();

        final TextView tittle = findViewById(R.id.tittle);
        final TextView description = findViewById(R.id.description);
        final ImageView logo = findViewById(R.id.logo);
        price = findViewById(R.id.price);

        tittle.setText(productName);
        description.setText(productDescription);
        price.setText(productPrice);

        try {
            picturesArray = new JSONArray(product.getPictures());
            tagsArray = new JSONArray(product.getTags());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getPictures();

        rvTags = findViewById(R.id.rvTags);
        listTags = new ArrayList<>();

        getTags();

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
                updateCart(preferencesAdapter.getCartId(), preferencesAdapter.getId() ,product.getBusinessId(), productId, value);
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

    private void getTags() {
        try {
            for (int i = 0; i < tagsArray.length(); i++) {
                JSONObject jsonObject = tagsArray.getJSONObject(i);
                TagsModel tag = new TagsModel();
                tag.setName(jsonObject.getString("tag"));
                listTags.add(tag);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvTags.setLayoutManager(layoutManager);
            rvTags.setHasFixedSize(true);
            tagsAdapter = new TagsAdapter(context, listTags);
            rvTags.setAdapter(tagsAdapter);
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
                        preferencesAdapter.setCartId(response.getString("cart"));
                        preferencesAdapter.setBusinessId(businessId);
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

    private void updateCart(String cartId, String userId, String businessId, String productId, int qty) {
        String url = Network.Cart+cartId+"/"+userId+"/"+businessId;
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
                        case "Wrong user":
                            builder = new AlertDialog.Builder(context);
                            builder.setTitle("Esto no debería haber pasado");
                            builder.setMessage("Hemos confundido un poco las cosas y este es el carrito de alguien más 😕");
                            builder.setPositiveButton("Crear carrito nuevo (solo para mí 😎)", null);
                            alertDialog = builder.create();
                            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            deleteCart(preferencesAdapter.getCartId());
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            break;
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
                                            alertDialog.dismiss();
                                        }
                                    });
                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            deleteCart(preferencesAdapter.getCartId());
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
                                            .setAction(R.string.go_to_cart, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(context, MainMenuUser.class);
                                                    intent.putExtra("screen", R.id.cart);
                                                    intent.putExtra("number", 3);
                                                    startActivity(intent);
                                                }
                                            })
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
                                            .setAction(R.string.go_to_cart, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(context, MainMenuUser.class);
                                                    intent.putExtra("screen", R.id.cart);
                                                    intent.putExtra("number", 3);
                                                    startActivity(intent);
                                                }
                                            })
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
                            preferencesAdapter.deleteCartId();
                            preferencesAdapter.deleteBusinessId();
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    createCart(preferencesAdapter.getId(), productBusinessId);
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
}