package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.BusinessDetail;
import com.ops.dev.simple.services.models.BusinessesModel;

import java.util.List;

public class BusinessesAdapter extends RecyclerView.Adapter<BusinessesAdapter.ViewHolder>{

    private Context mContext;
    private List<BusinessesModel> mData;
    private GlideAdapter glideAdapter;
    private ToastAdapter toastAdapter;

    public BusinessesAdapter(Context mContext, List<BusinessesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_businesses, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);
        toastAdapter = new ToastAdapter(mContext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessesModel business = new BusinessesModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        // Owner info
                        mData.get(vh.getAdapterPosition()).getOwner(),
                        mData.get(vh.getAdapterPosition()).getEmail(),
                        // Business info
                        mData.get(vh.getAdapterPosition()).getType(),
                        mData.get(vh.getAdapterPosition()).getLogo(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getDesc(),
                        mData.get(vh.getAdapterPosition()).getSlogan(),
                        mData.get(vh.getAdapterPosition()).getPhone(),
                        mData.get(vh.getAdapterPosition()).getAddress(),
                        mData.get(vh.getAdapterPosition()).getLat(),
                        mData.get(vh.getAdapterPosition()).getLng(),
                        mData.get(vh.getAdapterPosition()).getDist(),
                        mData.get(vh.getAdapterPosition()).getFb(),
                        mData.get(vh.getAdapterPosition()).getIg(),
                        mData.get(vh.getAdapterPosition()).getWa(),
                        mData.get(vh.getAdapterPosition()).getDelivery(),
                        mData.get(vh.getAdapterPosition()).getSchedule(),
                        mData.get(vh.getAdapterPosition()).getCategories(),
                        mData.get(vh.getAdapterPosition()).getPicture(),
                        mData.get(vh.getAdapterPosition()).getPictures(),
                        mData.get(vh.getAdapterPosition()).getMembershipId()
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BusinessesModel business = mData.get(position);

        if (business.getDelivery()) {
            holder.delivery.setVisibility(View.VISIBLE);
        }

        holder.delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).getType().equals("Products"))
                    toastAdapter.makeToast(R.drawable.__info, "Este negocio tiene delivery para sus productos");
                else if (mData.get(position).getType().equals("Services"))
                    toastAdapter.makeToast(R.drawable.__info, "Este negocio brinda servicios a domicilio");
            }
        });

        if (business.getType().equals("Products")) {
            holder.type.setText("PRODUCTOS");
        } else {
            holder.type.setText("SERVICIOS");
        }

        final double km = Math.round(business.getDist() * 100.0) / 100.0;
        holder.dist.setText(km + " Kms");

        holder.dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (km < 3)
                    toastAdapter.makeToast(R.drawable.__info, "Este negocio parece estar muy cerca");
                else
                    toastAdapter.makeToast(R.drawable.__info, "Podríamos crear una ruta hacia este negocio, ¿te parece? 😋");
            }
        });

        holder.score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastAdapter.makeToast(R.drawable._fav, "Pronto podrás calificar estos negocios");
            }
        });

        glideAdapter.setImageCircle(holder.logo, business.getLogo());
        glideAdapter.setImageDefault(holder.picture, business.getPicture());
        holder.name.setText(business.getName());
        holder.slogan.setText(business.getSlogan());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo, picture, delivery, score;
        TextView type, dist, name, slogan;

        public ViewHolder(View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.logo);
            picture = itemView.findViewById(R.id.picture);
            delivery = itemView.findViewById(R.id.delivery);
            score = itemView.findViewById(R.id.score);
            type = itemView.findViewById(R.id.type);
            dist = itemView.findViewById(R.id.dist);
            name = itemView.findViewById(R.id.name);
            slogan = itemView.findViewById(R.id.slogan);
        }
    }
}