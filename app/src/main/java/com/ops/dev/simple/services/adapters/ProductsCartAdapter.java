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
    private int lastPosition = 0;

    public ProductsCartAdapter(Context mContext, List<ProductsCartModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
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

        glideAdapter.setImage(holder.picture, product.getPicture());
        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(product.getPrice());
        holder.qty.setText(product.getPrice());
        //holder.qty.setText(product.getQuantity());
/*
        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt((String) holder.qty.getText());
                int res = temp - 1;
                holder.qty.setText(res);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt((String) holder.qty.getText());
                int res = temp + 1;
                holder.qty.setText(res);
            }
        });
 */
    }
/*
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.some_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition=position;
        }
        position++;
    }
*/

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture, subtract, add;
        TextView name, description, price;
        TextView qty;

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
}