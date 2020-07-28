package com.ops.dev.simple.services.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.MembershipsModel;

import java.util.List;

public class MembershipsListAdapter extends RecyclerView.Adapter<MembershipsListAdapter.ViewHolder>{

    private Context mContext;
    private List<MembershipsModel> mData;
    private ToastAdapter toastAdapter;
    private GlideAdapter glideAdapter;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    int count = 0, limit = 1;

    public MembershipsListAdapter(Context mContext, List<MembershipsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_memberships_list, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        toastAdapter = new ToastAdapter(mContext);
        glideAdapter = new GlideAdapter(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MembershipsModel membership = mData.get(position);

        holder.name.setText(membership.getName());

        switch (membership.getPriority()) {
            case 3:
                glideAdapter.setImageDefault(holder.icon, R.drawable._basic);
                holder.main.setBackgroundResource(R.color.basic);
                holder.priority.setText("NORMAL");
                break;
            case 2:
                glideAdapter.setImageDefault(holder.icon, R.drawable._plus);
                holder.main.setBackgroundResource(R.color.plus);
                holder.priority.setText("ALTA");
                break;
            case 1:
                glideAdapter.setImageDefault(holder.icon, R.drawable._vip);
                holder.main.setBackgroundResource(R.color.vip);
                holder.priority.setText("LA MÁS ALTA");
                break;
        }

        if (membership.getPrice().equals(membership.getPriceOff())) {
            holder.priceOffText.setVisibility(View.VISIBLE);
            holder.priceOff.setVisibility(View.VISIBLE);
            holder.priceOffText.setText("Precio:");
            holder.priceOff.setText(membership.getPrice());
        } else {
            holder.priceText.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.priceText.setText("Regular:");
            holder.price.setText(membership.getPrice());

            holder.priceOffText.setVisibility(View.VISIBLE);
            holder.priceOff.setVisibility(View.VISIBLE);
            holder.priceOffText.setText("Oferta:");
            holder.priceOff.setText(membership.getPriceOff());
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !membership.getChecked()) {
                    if (count >= limit) {
                        buttonView.setChecked(false);
                        toastAdapter.makeToast(R.drawable.__warning, "Ya tienes una membresía seleccionada...");
                        return;
                    }
                    membership.setChecked(true);
                    count ++;
                } else if (!isChecked && membership.getChecked()) {
                    membership.setChecked(false);
                    count --;
                }
            }
        });
        holder.checkBox.setChecked(membership.getChecked());

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModal(membership);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout main;
        ImageView icon;
        TextView name, price, priceOff, priceText, priceOffText, priority, more;
        MaterialCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            main = itemView.findViewById(R.id.main);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            priceOff = itemView.findViewById(R.id.priceOff);
            priceText = itemView.findViewById(R.id.priceText);
            priceOffText = itemView.findViewById(R.id.priceOffText);
            priority = itemView.findViewById(R.id.priority);
            more = itemView.findViewById(R.id.more);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    private void showModal(MembershipsModel membership) {
        int icon = R.drawable._fav;
        String msj = "";
        switch (membership.getPriority()) {
            case 3:
                icon = R.drawable._basic;
                msj = " " +
                        membership.getDesc() + "<br/><br/>" +
                        "Límite de productos: " + membership.getProducts() + "<br/><br/>" +
                        "Límite de fotografías por producto: " + membership.getPictures();
                break;
            case 2:
                icon = R.drawable._plus;
                msj = " " +
                        membership.getDesc() + "<br/><br/>" +
                        "<b>Aumenta </b>" + "Límite de productos: " + membership.getProducts() + "<br/><br/>" +
                        "<b>Aumenta </b>" + "Límite de fotografías por producto: " + membership.getPictures() + "<br/><br/>" +
                        "<b>Nuevo </b>" +  "Entradas al módulo de noticias: " + membership.getEntries();
                break;
            case 1:
                icon = R.drawable._vip;
                msj = " " +
                        membership.getDesc() + "<br/><br/>" +
                        "<b>Aumenta </b>" + "Límite de productos: " + membership.getProducts() + "<br/><br/>" +
                        "<b>Aumenta </b>" + "Límite de fotografías por producto: " + membership.getPictures() + "<br/><br/>" +
                        "<b>Aumenta </b>" + "Entradas al módulo de noticias: " + membership.getEntries();
                break;
        }
        builder = new AlertDialog.Builder(mContext);
        builder.setIcon(icon);
        builder.setTitle(membership.getName());
        builder.setMessage(Html.fromHtml(msj));
        builder.setPositiveButton("Cerrar", null);
        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}