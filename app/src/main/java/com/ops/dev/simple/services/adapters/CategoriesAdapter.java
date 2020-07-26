package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.Business;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesModel> mData;
    private ToastAdapter toastAdapter;
    private GlideAdapter glideAdapter;

    public CategoriesAdapter(Context mContext, List<CategoriesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_categories, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        toastAdapter = new ToastAdapter(mContext);
        glideAdapter = new GlideAdapter(mContext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesModel category = new CategoriesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getIcon()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), Business.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category", category);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel category = mData.get(position);
        ImageView icon = holder.icon;
        TextView name = holder.name;

        if (category.getIcon() != 0)
            glideAdapter.setImageCircle(icon, category.getIcon());
        else
            glideAdapter.setImageCircle(icon, R.drawable._fav);
        name.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }
    }
}