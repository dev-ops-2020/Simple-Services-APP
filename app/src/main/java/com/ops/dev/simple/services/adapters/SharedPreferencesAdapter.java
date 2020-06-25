package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ops.dev.simple.services.R;

import java.util.Objects;

public class SharedPreferencesAdapter {

    private Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public SharedPreferencesAdapter(Context context) {
        this.context = context;
    }

    public void savePreferences(String id, String alias, String password, String idDevice, String token, Boolean isFirstTime) {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", 0);
        editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("alias", alias);
        editor.putString("password", password);
        editor.putString("idDevice", idDevice);
        editor.putString("token", token);
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.apply();
    }

    public String getId() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id"))
            return sharedPreferences.getString("id", "");
        return null;
    }

    public String getAlias() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("alias"))
            return sharedPreferences.getString("alias", "");
        return null;
    }

    public String getPassword() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("password"))
            return sharedPreferences.getString("password", "");
        return null;
    }

    public String getIdDevice() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("idDevice"))
            return sharedPreferences.getString("idDevice", "");
        return null;
    }

    public String getToken() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("token"))
            return sharedPreferences.getString("token", "");
        return null;
    }

    public void deletePreferences() {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
