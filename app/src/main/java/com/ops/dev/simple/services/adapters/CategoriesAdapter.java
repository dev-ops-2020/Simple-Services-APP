package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.Categories;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.List;
import java.util.Random;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesModel> mData;

    public CategoriesAdapter(Context mContext, List<CategoriesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_categories, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);
        final LinearLayout container = view.findViewById(R.id.container);

        int[] colors = {R.color.gold, R.color.teal, R.color.purple, R.color.gold_alt, R.color.teal_alt, R.color.purple_alt};
        int randomColor = new Random().nextInt(colors.length);
        container.setBackgroundResource(colors[randomColor]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriesModel category = new CategoriesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getDescription(),
                        mData.get(vh.getAdapterPosition()).getIcon()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), Categories.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tittle","" + mData.get(vh.getAdapterPosition()).getName().toUpperCase());
                intent.putExtra("category", category);
                mContext.startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel item = mData.get(position);
        ImageView image = holder.icon;
        TextView title = holder.txtName;

        image.setImageResource(item.getIcon());
        title.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}