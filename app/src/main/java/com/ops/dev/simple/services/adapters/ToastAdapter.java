package com.ops.dev.simple.services.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ops.dev.simple.services.R;

public class ToastAdapter {
    private Activity mActivity;

    public ToastAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void makeToast(String mMessage, int mIcon) {
        LayoutInflater inflater = (LayoutInflater) this.mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.___toast_message, (ViewGroup) this.mActivity.findViewById(R.id.container));

        TextView message = layout.findViewById(R.id.message);
        ImageView icon = layout.findViewById(R.id.icon);

        message.setText(mMessage);
        icon.setImageResource(mIcon);

        Toast toast = new Toast(mActivity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
