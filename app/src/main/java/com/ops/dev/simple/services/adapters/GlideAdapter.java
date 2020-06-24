package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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
                .centerCrop()
                .override(300, 300)
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(mImageView);
    }

    public void setImage(ImageView mImageView, int mImage) {
        Glide
                .with(context)
                .load(mImage)
                .centerCrop()
                .override(300, 300)
                .transform(new RoundedCorners(R.dimen.med_margin))
                .into(mImageView);
    }
}
