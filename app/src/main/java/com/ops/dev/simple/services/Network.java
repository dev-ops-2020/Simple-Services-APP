package com.ops.dev.simple.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static ConnectivityManager manager;
    private static final String IP = "https://simple-services.herokuapp.com/api/";
    public static final String SignUp = IP + "signup/";
    public static final String SignIn = IP + "signin/";
    public static final String ListCategories = IP + "categories/";
    public static final String Users = IP + "users/";
    public static final String ListBusinessByCategory = IP + "businesses/category/";
    public static final String ListCommentsByBusiness = IP + "comments/business/";

    public static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    public static final int DURATION_SHORT = 1500;
    public static final int DURATION_LONG = 2500;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
/*
    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
*/