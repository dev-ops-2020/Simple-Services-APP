package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ops.dev.simple.services.R;

public class ToastAdapter {

    private Context context;

    public ToastAdapter(Context context) {
        this.context = context;
    }

    public void makeToast(String mMessage, int mIcon) {
        View view = View.inflate(context, R.layout.___toast_message, null);

        TextView message = view.findViewById(R.id.message);
        ImageView icon = view.findViewById(R.id.icon);

        message.setText(mMessage);
        icon.setImageResource(mIcon);

        Toast toast = new Toast(context.getApplicationContext());
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        //toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
