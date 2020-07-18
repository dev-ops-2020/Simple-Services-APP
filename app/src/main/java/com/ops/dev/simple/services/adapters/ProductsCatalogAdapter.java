package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.MainMenuBusiness;
import com.ops.dev.simple.services.activities.MainMenuUser;
import com.ops.dev.simple.services.activities.fragments.Catalog;
import com.ops.dev.simple.services.models.ProductsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ProductsCatalogAdapter extends RecyclerView.Adapter<ProductsCatalogAdapter.ViewHolder>{

    private Context mContext;
    private List<ProductsModel> mData;
    private GlideAdapter glideAdapter;
    private RequestQueue queue;

    public ProductsCatalogAdapter(Context mContext, List<ProductsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_products_catalog, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);
        queue = Volley.newRequestQueue(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductsModel product = mData.get(position);
        final MaterialCheckBox available = holder.available;
        ImageView picture = holder.picture;
        TextView type = holder.type;
        TextView price = holder.price;
        TextView name = holder.name;
        TextView desc = holder.desc;

        if (product.getAvailable())
            available.setChecked(true);
        glideAdapter.setImageDefault(picture, product.getPicture());
        if (product.getType().equals("Product"))
            type.setText("Producto");
        else if (product.getType().equals("Service"))
            type.setText("Servicio");
        price.setText(product.getPrice());
        name.setText(product.getName());
        desc.setText(product.getDesc());

        String uri = " $ " + product.getPrice() + " ";
        price.setText(uri);
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(product.getId(), !product.getAvailable());
                Catalog f = (Catalog) Catalog.catalog;
                Objects.requireNonNull(f.getFragmentManager()).findFragmentByTag("catalog");
                f.refreshFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox available;
        ImageView picture;
        TextView type, price, name, desc;

        public ViewHolder(View itemView) {
            super(itemView);

            available = itemView.findViewById(R.id.available);
            picture = itemView.findViewById(R.id.picture);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    private void changeState(String productId, boolean available) {
        String url = Network.ProductsChangeState+productId;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("available", available);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}