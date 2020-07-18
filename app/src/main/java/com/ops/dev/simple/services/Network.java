package com.ops.dev.simple.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

public class Network {

    private static ConnectivityManager manager;
    private static final String IP = "https://simple-services.herokuapp.com/api/";
    public static final String SignUp = IP + "signup/";
    public static final String SignIn = IP + "signin/";
    public static final String LogIn = IP + "login/";
    public static final String Terms = IP + "terms/";
    public static final String ListCategories = IP + "categories/";
    public static final String Users = IP + "users/";
    public static final String Business = IP + "businesses/";
    public static final String Products = IP + "products/";
    public static final String ProductsAvailable = IP + "products/available/";
    public static final String ProductsUnavailable = IP + "products/unavailable/";
    public static final String ProductsChangeState = IP + "products/change_state/";
    public static final String ListBusinessByCategory = IP + "businesses/category/";
    public static final String ListCommentsByBusiness = IP + "comments/business/";
    public static final String ListProductsByBusiness = IP + "products/business/";
    public static final String Cart = IP + "cart/";

    public static final String DESIGNER_URL = "https://www.instagram.com/the_mid_lemon/";
    public static final String NO_LOGO = "https://firebasestorage.googleapis.com/v0/b/simple-services-25f81.appspot.com/o/images%2Fbusinesses%2F_____No_Logo.png?alt=media";

    public static final int REQUEST_CODE_IMAGE = 1020;
    public static final int REQUEST_CODE_PICTURE_1 = 1021;
    public static final int REQUEST_CODE_PICTURE_2 = 1022;
    public static final int REQUEST_CODE_PICTURE_3 = 1023;
    public static final int REQUEST_CODE_LOCATION = 1030;
    /*
    public static final int DURATION_NONE = 500;
    public static final int DURATION_SHORT = 1500;
    public static final int DURATION_NORMAL = 3000;
    public static final int DURATION_LONG = 6000;
    */
    public static final int DURATION_NONE = 0;
    public static final int DURATION_SHORT = 500;
    public static final int DURATION_NORMAL = 1000;
    public static final int DURATION_LONG = 2000;

    public static final float ZOOM_LEVEL_MIN = 10.0f;
    public static final float ZOOM_LEVEL = 16.0f;
    public static final float ZOOM_LEVEL_MAX = 22.0f;

    public static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

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