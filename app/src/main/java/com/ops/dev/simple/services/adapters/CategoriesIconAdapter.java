package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.Business;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.List;

public class CategoriesIconAdapter extends RecyclerView.Adapter<CategoriesIconAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesModel> mData;
    private GlideAdapter glideAdapter;

    public CategoriesIconAdapter(Context mContext, List<CategoriesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__card_categories_icon, parent, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriesModel category = new CategoriesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getIcon()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), Business.class);
                intent.putExtra("category", category);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel category = mData.get(position);

        if (!category.getIcon().equals(""))
            glideAdapter.setImageCircle(holder.icon, category.getIcon());
        else
            glideAdapter.setImageCircle(holder.icon, R.drawable._fav);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
        }
    }
}