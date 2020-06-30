package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.activities.ProductDetail;
import com.ops.dev.simple.services.models.ProductsModel;

import java.util.List;

public class ProductsAdapterMini extends RecyclerView.Adapter<ProductsAdapterMini.ViewHolder>{

    private Context mContext;
    private List<ProductsModel> mData;
    private GlideAdapter glideAdapter;

    public ProductsAdapterMini(Context mContext, List<ProductsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_products_mini, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsModel product = new ProductsModel(
                        mData.get(vh.getAdapterPosition()).getId(),
                        mData.get(vh.getAdapterPosition()).getName(),
                        mData.get(vh.getAdapterPosition()).getDescription(),
                        mData.get(vh.getAdapterPosition()).getAvailable(),
                        mData.get(vh.getAdapterPosition()).getPicture(),
                        mData.get(vh.getAdapterPosition()).getPictures(),
                        mData.get(vh.getAdapterPosition()).getPrice(),
                        mData.get(vh.getAdapterPosition()).getPrices(),
                        mData.get(vh.getAdapterPosition()).getStatus(),
                        mData.get(vh.getAdapterPosition()).getCategories(),
                        mData.get(vh.getAdapterPosition()).getIdBusiness()
                );
                Intent intent = new Intent(mContext.getApplicationContext(), ProductDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product", product);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductsModel product = mData.get(position);
        ImageView picture = holder.picture;
        TextView name = holder.name;
        TextView price = holder.price;

        Glide
                .with(mContext)
                .load(product.getPicture())
                .into(picture);
        name.setText(product.getName());

        String uri = " $ " + product.getPrice() + " ";
        price.setText(uri);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView name, price;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }
    }
}