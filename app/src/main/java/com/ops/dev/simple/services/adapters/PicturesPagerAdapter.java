package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.ops.dev.simple.services.R;

import java.util.ArrayList;

public class PicturesPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private LayoutInflater mLayoutInflater;

    public PicturesPagerAdapter(Context mContext, ArrayList<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.___pager_item, container, false);
        final TextView counter = itemView.findViewById(R.id.counter);
        final ImageView picture = itemView.findViewById(R.id.picture);
        int count = position + 1;
        String uri = " " + count + "/" + mData.size() + " ";
        counter.setText(uri);
        Glide
                .with(mContext)
                .load(mData.get(position))
                .into(picture);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}