package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.ProductsCartModel;

import java.util.List;

public class ProductsCartAdapter extends RecyclerView.Adapter<ProductsCartAdapter.ViewHolder>{

    private Context mContext;
    private List<ProductsCartModel> mData;
    private GlideAdapter glideAdapter;

    public ProductsCartAdapter(Context mContext, List<ProductsCartModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_products_cart, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        glideAdapter = new GlideAdapter(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductsCartModel product = mData.get(position);
        ImageView picture = holder.picture;
        TextView name = holder.name;
        TextView description = holder.description;
        TextView price = holder.price;
        TextView qty = holder.qty;
        ImageView subtract = holder.subtract;
        ImageView add = holder.add;

        glideAdapter.setImage(picture, product.getPicture());
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice());
        qty.setText(String.valueOf(product.getQuantity()));

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt((String) holder.qty.getText());
                int res = temp - 1;
                holder.qty.setText(String.valueOf(res));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt((String) holder.qty.getText());
                int res = temp + 1;
                holder.qty.setText(String.valueOf(res));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture, subtract, add;
        TextView name, description, price, qty;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            subtract = itemView.findViewById(R.id.subtract);
            add = itemView.findViewById(R.id.add);
        }
    }

    public double getTotal() {
        double totalPrice = 0;
        for (int i = 0; i < mData.size(); i++) {
            totalPrice += Double.parseDouble(mData.get(i).getPrice());
        }
        return totalPrice;
    }
}