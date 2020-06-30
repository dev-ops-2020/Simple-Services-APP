package com.ops.dev.simple.services.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.MainMenu;
import com.ops.dev.simple.services.models.ProductsCartModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductsCartAdapter extends RecyclerView.Adapter<ProductsCartAdapter.ViewHolder>{

    private Context mContext;
    private List<ProductsCartModel> mData;
    private String cartId, businessId;
    private ToastAdapter toastAdapter;
    private GlideAdapter glideAdapter;
    private RequestQueue queue;

    public ProductsCartAdapter(Context mContext, List<ProductsCartModel> mData, String cartId, String businessId) {
        this.mContext = mContext;
        this.mData = mData;
        this.cartId = cartId;
        this.businessId = businessId;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_products_cart, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        toastAdapter = new ToastAdapter(mContext);
        glideAdapter = new GlideAdapter(mContext);
        queue = Volley.newRequestQueue(mContext);

        getTotal();

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductsCartModel product = mData.get(position);
        ImageView picture = holder.picture;
        TextView name = holder.name;
        TextView description = holder.description;
        TextView price = holder.price;
        TextView subtotal = holder.subtotal;
        TextView qty = holder.qty;
        final ImageView subtract = holder.subtract;
        final ImageView add = holder.add;

        glideAdapter.setImage(picture, product.getPicture());
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice());
        subtotal.setText(String.valueOf(getSubTotal(position)));
        qty.setText(String.valueOf(product.getQuantity()));

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtract(position);
                holder.subtotal.setText(String.valueOf(getSubTotal(position)));
                holder.qty.setText(String.valueOf(mData.get(position).getQuantity()));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(position);
                holder.subtotal.setText(String.valueOf(getSubTotal(position)));
                holder.qty.setText(String.valueOf(mData.get(position).getQuantity()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture, subtract, add;
        TextView name, description, price, subtotal, qty;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            subtotal = itemView.findViewById(R.id.subtotal);
            qty = itemView.findViewById(R.id.qty);
            subtract = itemView.findViewById(R.id.subtract);
            add = itemView.findViewById(R.id.add);
        }
    }

    public void subtract (final int pos) {
        int temp = Integer.parseInt(String.valueOf(mData.get(pos).getQuantity()));
        int res = temp - 1;
        if (res < 1)
            res = 1;
        mData.get(pos).setQuantity(res);
        updateCart(cartId, businessId, mData.get(pos).getId(), mData.get(pos).getQuantity());
    }

    public void add (final int pos) {
        int temp = Integer.parseInt(String.valueOf(mData.get(pos).getQuantity()));
        int res = temp + 1;
        if (res > 20)
            res = 20;
        mData.get(pos).setQuantity(res);
        updateCart(cartId, businessId, mData.get(pos).getId(), mData.get(pos).getQuantity());
    }

    private void updateCart(final String cartId, final String businessId, final String productId, int qty) {
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
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public double getSubTotal(int pos) {
        return Double.parseDouble(mData.get(pos).getPrice()) * mData.get(pos).getQuantity();
    }

    public double getTotal() {
        double totalPrice = 0;
        for (int i = 0; i < mData.size(); i++) {
            totalPrice += Double.parseDouble(mData.get(i).getPrice()) * mData.get(i).getQuantity();
        }
        return totalPrice;
    }
}