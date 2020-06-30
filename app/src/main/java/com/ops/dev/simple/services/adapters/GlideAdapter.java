package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ops.dev.simple.services.R;

public class GlideAdapter {

    private Context context;

    public GlideAdapter(Context context) {
        this.context = context;
    }

    public void setImage(ImageView mImageView, String mImage) {
        Glide
                .with(context)
                .load(mImage)
                .override(300, 300)
                .centerCrop()
                .placeholder(R.drawable.__loading)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.centerInsideTransform().error(R.drawable.__error))
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(mImageView);
    }

    public void setImage(ImageView mImageView, int mImage) {
        Glide
                .with(context)
                .load(mImage)
                .override(300, 300)
                .centerCrop()
                .placeholder(R.drawable.__loading)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.centerInsideTransform().error(R.drawable.__error))
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(mImageView);
    }
}