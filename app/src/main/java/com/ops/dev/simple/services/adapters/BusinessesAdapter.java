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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.BusinessDetail;
import com.ops.dev.simple.services.models.BusinessesModel;

import java.util.List;

public class BusinessesAdapter extends RecyclerView.Adapter<BusinessesAdapter.ViewHolder>{

    private Context mContext;
    private List<BusinessesModel> mData;

    public BusinessesAdapter(Context mContext, List<BusinessesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_businesses, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessesModel business = new BusinessesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getDescription(),
                        mData.get(vh.getAdapterPosition()).getSlogan(),
                        mData.get(vh.getAdapterPosition()).getOwner(),
                        mData.get(vh.getAdapterPosition()).getScore(),
                        mData.get(vh.getAdapterPosition()).getStatus(),
                        mData.get(vh.getAdapterPosition()).getLogo(),
                        mData.get(vh.getAdapterPosition()).getPicture(),
                        mData.get(vh.getAdapterPosition()).getPictures(),
                        mData.get(vh.getAdapterPosition()).getPhones(),
                        mData.get(vh.getAdapterPosition()).getSchedule(),
                        mData.get(vh.getAdapterPosition()).getNetworks(),
                        mData.get(vh.getAdapterPosition()).getCategories(),
                        mData.get(vh.getAdapterPosition()).getLatitude(),
                        mData.get(vh.getAdapterPosition()).getLongitude(),
                        mData.get(vh.getAdapterPosition()).getIdMembership()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), BusinessDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("business", business);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusinessesModel business = mData.get(position);
        ImageView logo = holder.logo;
        ImageView picture = holder.picture;
        TextView name = holder.name;
        TextView slogan = holder.slogan;
        TextView score = holder.score;

        Glide
                .with(mContext)
                .load(business.getPicture())
                .into(picture);

        Glide
                .with(mContext)
                .load(business.getLogo())
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(logo);
        name.setText(business.getName());
        slogan.setText(business.getSlogan());
        score.setText(business.getScore());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo, picture;
        TextView name, slogan, score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.logo);
            picture = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
            slogan = itemView.findViewById(R.id.slogan);
            score = itemView.findViewById(R.id.score);
        }
    }
}