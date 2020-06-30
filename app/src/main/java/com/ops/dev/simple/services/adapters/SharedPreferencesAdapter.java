package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesAdapter {

    private Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public SharedPreferencesAdapter(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
    }

    public Boolean keyExists(String key) {
        if (sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    public Boolean getIsLoggedIn() {
        if (sharedPreferences.contains("isLoggedIn"))
            return sharedPreferences.getBoolean("isLoggedIn", false);
        return false;
    }

    public void setIsLoggedIn(Boolean status) {
        editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", status);
        editor.apply();
    }

    public String getUserId() {
        if (sharedPreferences.contains("userId"))
            return sharedPreferences.getString("userId", "");
        return null;
    }

    public void setUserId(String userId) {
        editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public String getAlias() {
        if (sharedPreferences.contains("alias"))
            return sharedPreferences.getString("alias", "");
        return null;
    }

    public void setAlias(String alias) {
        editor = sharedPreferences.edit();
        editor.putString("alias", alias);
        editor.apply();
    }

    public String getPassword() {
        if (sharedPreferences.contains("password"))
            return sharedPreferences.getString("password", "");
        return null;
    }

    public void setPassword(String password) {
        editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public String getIdDevice() {
        if (sharedPreferences.contains("idDevice"))
            return sharedPreferences.getString("idDevice", "");
        return null;
    }

    public void setIdDevice(String idDevice) {
        editor = sharedPreferences.edit();
        editor.putString("idDevice", idDevice);
        editor.apply();
    }

    public String getToken() {
        if (sharedPreferences.contains("token"))
            return sharedPreferences.getString("token", "");
        return null;
    }

    public void setToken(String token) {
        editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getCartId() {
        if (sharedPreferences.contains("cartId"))
            return sharedPreferences.getString("cartId", "");
        return null;
    }

    public void setCartId(String cartId) {
        editor = sharedPreferences.edit();
        editor.putString("cartId", cartId);
        editor.apply();
    }

    public void deleteCartId() {
        editor = sharedPreferences.edit();
        editor.remove("cartId");
        editor.apply();
    }

    public String getBusinessId() {
        if (sharedPreferences.contains("businessId"))
            return sharedPreferences.getString("businessId", "");
        return null;
    }

    public void setBusinessId(String businessId) {
        editor = sharedPreferences.edit();
        editor.putString("businessId", businessId);
        editor.apply();
    }

    public void deleteBusinessId() {
        editor = sharedPreferences.edit();
        editor.remove("businessId");
        editor.apply();
    }

    public boolean getIsFirstTime() {
        if (sharedPreferences.contains("isFirstTime"))
            return sharedPreferences.getBoolean("isFirstTime", true);
        return false;
    }

    public void setIsFirstTime(Boolean isFirstTime) {
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.apply();
    }
/*
    public void deletePreferences() {
        sharedPreferences.edit().clear().apply();
    }
*/
}
