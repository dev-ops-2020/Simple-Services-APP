package com.ops.dev.simple.services.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.ops.dev.simple.services.R;

public class IntentAdapter {

    private Activity activity;
    private Context context;

    public IntentAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void goActivityAnimated(final Class _class, int time) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Activity)context).finish();
                Intent intent = new Intent(activity, _class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, time);
    }

    public void goActivityDefault(final Class _class) {
        Intent intent = new Intent(activity, _class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}