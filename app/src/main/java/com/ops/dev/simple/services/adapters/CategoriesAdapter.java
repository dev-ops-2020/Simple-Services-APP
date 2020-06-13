package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.BusinessDetail;
import com.ops.dev.simple.services.activities.Businesses;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.List;
import java.util.Random;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesModel> mData;
    ToastAdapter toastAdapter;

    public CategoriesAdapter(Context mContext, List<CategoriesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_categories, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);
        final RelativeLayout container = view.findViewById(R.id.container);

        toastAdapter = new ToastAdapter(mContext);

        //int[] colors = {R.color.yellow, R.color.light_blue, R.color.purple, R.color.yellow_alt, R.color.light_blue_alt, R.color.purple_alt};
        //int[] colors = {R.color.yellow_alt, R.color.light_blue, R.color.purple};
        //int randomColor = new Random().nextInt(colors.length);
        //container.setBackgroundResource(colors[randomColor]);

        container.setBackgroundResource(R.color.colorAccent);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesModel category = new CategoriesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getDescription(),
                        mData.get(vh.getAdapterPosition()).getIcon()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), Businesses.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category", category);
                mContext.startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            toastAdapter.makeToast( "" + mData.get(vh.getAdapterPosition()).getDescription(), mData.get(vh.getAdapterPosition()).getIcon());
            return true;
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
            icon.setImageResource(category.getIcon());
        else
            icon.setImageResource(R.drawable._fav);
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