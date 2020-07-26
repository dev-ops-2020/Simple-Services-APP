package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesAdapter {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PreferencesAdapter(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }

    public void deletePreferences() {
        preferences.edit().clear().apply();
    }

    public Boolean keyExists(String key) {
        return preferences.contains(key);
    }

    public Boolean getIsLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    public void setIsLoggedIn() {
        editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public Boolean getIsUser() {
        return preferences.getBoolean("isUser", false);
    }

    public void setIsUser() {
        editor = preferences.edit();
        editor.putBoolean("isUser", true);
        editor.apply();
    }

    public Boolean getIsBusiness() {
        return preferences.getBoolean("isBusiness", false);
    }

    public void setIsBusiness() {
        editor = preferences.edit();
        editor.putBoolean("isBusiness", true);
        editor.apply();
    }

    public String getId() {
        if (preferences.contains("id"))
            return preferences.getString("id", "");
        return null;
    }

    public void setId(String id) {
        editor = preferences.edit();
        editor.putString("id", id);
        editor.apply();
    }

    public String getAlias() {
        if (preferences.contains("alias"))
            return preferences.getString("alias", "");
        return null;
    }

    public void setAlias(String alias) {
        editor = preferences.edit();
        editor.putString("alias", alias);
        editor.apply();
    }

    public String getEmail() {
        if (preferences.contains("email"))
            return preferences.getString("email", "");
        return null;
    }

    public void setEmail(String email) {
        editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public String getPass() {
        if (preferences.contains("pass"))
            return preferences.getString("pass", "");
        return null;
    }

    public void setPass(String pass) {
        editor = preferences.edit();
        editor.putString("pass", pass);
        editor.apply();
    }

    public String getToken() {
        if (preferences.contains("token"))
            return preferences.getString("token", "");
        return null;
    }

    public void setToken(String token) {
        editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getDeviceId() {
        if (preferences.contains("deviceId"))
            return preferences.getString("deviceId", "");
        return null;
    }

    public void setDeviceId(String deviceId) {
        editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        editor.apply();
    }

    public Float getLat() {
        if (preferences.contains("lat"))
            return preferences.getFloat("lat", 0);
        return null;
    }

    public void setLat(Float lat) {
        editor = preferences.edit();
        editor.putFloat("lat", lat);
        editor.apply();
    }

    public Float getLng() {
        if (preferences.contains("lng"))
            return preferences.getFloat("lng", 0);
        return null;
    }

    public void setLng(Float lng) {
        editor = preferences.edit();
        editor.putFloat("lng", lng);
        editor.apply();
    }

    // Logic for user modules
    public String getCartId() {
        if (preferences.contains("cartId"))
            return preferences.getString("cartId", "");
        return null;
    }

    public void setCartId(String cartId) {
        editor = preferences.edit();
        editor.putString("cartId", cartId);
        editor.apply();
    }

    public void deleteCartId() {
        editor = preferences.edit();
        editor.remove("cartId");
        editor.apply();
    }

    public String getBusinessId() {
        if (preferences.contains("businessId"))
            return preferences.getString("businessId", "");
        return null;
    }

    public void setBusinessId(String businessId) {
        editor = preferences.edit();
        editor.putString("businessId", businessId);
        editor.apply();
    }

    public void deleteBusinessId() {
        editor = preferences.edit();
        editor.remove("businessId");
        editor.apply();
    }

    public String getFilter() {
        if (preferences.contains("filter"))
            return preferences.getString("filter", "");
        return null;
    }

    public void setFilter(String filter) {
        editor = preferences.edit();
        editor.putString("filter", filter);
        editor.apply();
    }

    // Logic for businesses modules

    public String getMembershipId() {
        if (preferences.contains("membershipId"))
            return preferences.getString("membershipId", "");
        return null;
    }

    public void setMembershipId(String membershipId) {
        editor = preferences.edit();
        editor.putString("membershipId", membershipId);
        editor.apply();
    }
/*
*/
}
