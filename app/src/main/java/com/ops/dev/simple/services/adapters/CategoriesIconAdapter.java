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
import com.ops.dev.simple.services.activities.Businesses;
import com.ops.dev.simple.services.models.CategoriesIconModel;

import java.util.List;

public class CategoriesIconAdapter extends RecyclerView.Adapter<CategoriesIconAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesIconModel> mData;

    public CategoriesIconAdapter(Context mContext, List<CategoriesIconModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__card_categories_icon, parent, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriesIconModel category = new CategoriesIconModel(
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getIcon()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), Businesses.class);
                intent.putExtra("category", category);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesIconModel item = mData.get(position);
        ImageView image = holder.icon;

        image.setImageResource(item.getIcon());
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