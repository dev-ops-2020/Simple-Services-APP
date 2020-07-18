package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.Business;
import com.ops.dev.simple.services.models.CategoriesModel;
import com.ops.dev.simple.services.models.CategoriesModelListIcon;
import com.ops.dev.simple.services.models.SchedulesModel;
import com.ops.dev.simple.services.models.TagsModel;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder>{

    private Context mContext;
    private List<TagsModel> mData;

    public TagsAdapter(Context mContext, List<TagsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_tags, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagsModel tag = new TagsModel(
                        mData.get(vh.getAdapterPosition()).getName()
                );
                ToastAdapter t = new ToastAdapter(mContext);
                t.makeToast(R.drawable.__info, tag.getName());
                //Intent intent = new Intent(mContext.getApplicationContext(), Business.class);
                //intent.putExtra("tag", tag);
                //mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagsModel tagsModel = mData.get(position);
        TextView name = holder.name;

        name.setText(tagsModel.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}