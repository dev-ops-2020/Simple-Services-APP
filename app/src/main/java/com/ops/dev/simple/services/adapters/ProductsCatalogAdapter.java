package com.ops.dev.simple.services.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.fragments.Catalog;
import com.ops.dev.simple.services.models.ProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProductsCatalogAdapter extends RecyclerView.Adapter<ProductsCatalogAdapter.ViewHolder>{

    private Context mContext;
    private List<ProductsModel> mData;
    private ToastAdapter toastAdapter;
    private GlideAdapter glideAdapter;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private StorageReference storageReference;
    private RequestQueue queue;

    public ProductsCatalogAdapter(Context mContext, List<ProductsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
        //notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_products_catalog, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        toastAdapter = new ToastAdapter(mContext);
        glideAdapter = new GlideAdapter(mContext);
        queue = Volley.newRequestQueue(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ProductsModel product = mData.get(position);

        if (product.getAvailable())
            holder.available.setChecked(true);
        glideAdapter.setImageDefault(holder.picture, product.getPicture());
        if (product.getType().equals("Product"))
            holder.type.setText("Producto");
        else if (product.getType().equals("Service"))
            holder.type.setText("Servicio");
        holder.price.setText(product.getPrice());
        holder.name.setText(product.getName());
        holder.desc.setText(product.getDesc());

        String uri = " $ " + product.getPrice() + " ";
        holder.price.setText(uri);
        holder.available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(product.getId(), !product.getAvailable());
                Catalog f = (Catalog) Catalog.catalog;
                Objects.requireNonNull(f.getFragmentManager()).findFragmentByTag("catalog");
                f.refreshFragment();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastAdapter t =  new ToastAdapter(mContext);
                t.makeToast(R.drawable.__info, "Editar " + product.getName());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModal(view.getRootView().getContext(), product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox available;
        ImageView picture, edit, delete;
        TextView type, price, name, desc;

        public ViewHolder(View itemView) {
            super(itemView);

            available = itemView.findViewById(R.id.available);
            picture = itemView.findViewById(R.id.picture);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
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

    private void showModal(Context context, final ProductsModel product) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("¿Desea eliminar " + product.getName() + "?");
        builder.setMessage("Si los clientes lo tienen en su carito podría no aparecerles 😕");
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Eliminar", null);
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
                        deleteProduct(product.getId());
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void deleteProduct(String productId) {
        String url = Network.Products+productId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String __message = response.getString("message");
                    if (__message.equals("Product deleted")) {
                        JSONObject jsonObject = response.getJSONObject("product");
                        JSONArray jsonArrayPictures = jsonObject.getJSONArray("pictures");
                        for (int i = 0; i < jsonArrayPictures.length(); i++) {
                            JSONObject jsonObjectPicture = jsonArrayPictures.getJSONObject(i);
                            String image = jsonObjectPicture.getString("picture");
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
                            storageReference.delete();
                        }
                        Catalog f = (Catalog) Catalog.catalog;
                        Objects.requireNonNull(f.getFragmentManager()).findFragmentByTag("catalog");
                        f.refreshFragment();
                    } else {
                        toastAdapter.makeToast(R.drawable.__info, "Error al borrar el producto/servicio");
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