package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>{

    private Context mContext;
    private List<CategoriesModel> mData;
    private GlideAdapter glideAdapter;
    private ToastAdapter toastAdapter;
    int count = 0, limit = 3;

    public CategoriesListAdapter(Context mContext, List<CategoriesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__card_categories_list, parent, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);
        toastAdapter = new ToastAdapter(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoriesModel category = mData.get(position);

        if (!category.getIcon().equals(""))
            glideAdapter.setImageCircle(holder.icon, category.getIcon());
        else
            glideAdapter.setImageCircle(holder.icon, R.drawable._fav);
        holder.name.setText(category.getName());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !category.getChecked()) {
                    if (count >= limit) {
                        buttonView.setChecked(false);
                        toastAdapter.makeToast(R.drawable.__warning, "Solo puedes seleccionar 3 categorías");
                        return;
                    }
                    category.setChecked(true);
                    count ++;
                } else if (!isChecked && category.getChecked()) {
                    category.setChecked(false);
                    count --;
                }
            }
        });
        holder.checkBox.setChecked(category.getChecked());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.radioButton);
        }
    }
}