package com.ops.dev.simple.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static ConnectivityManager manager;
    private static final String IP = "https://simple-services.herokuapp.com/api/";
    public static final String GET_CATEGORIES = IP + "categories/";
    public static final String ADD_ESTABLISHMENT = IP + "establishment/";
    public static final String GET_ESTABLISHMENT = IP + "establishment/";
    public static final String GET_ESTABLISHMENT_BY_CAT = IP + "categories/";
    public  static final  String GET_PROMOTIONS = IP  + "promotions/";
    public  static final  String GET_PROMOTIONS_BY_CAT = IP  + "categories/promotions/";
    public  static final  String GET_VOUCHERS = IP  + "vouchers/";
    public  static final  String GET_MY_VOUCHERS = IP  + "user/voucher/";
    public  static final  String GET_VOUCHERS_BY_CAT = IP  + "categories/vouchers/";
    public  static final  String GET_COMMENTS = IP  + "comments/";
    public  static final  String GET_TIME_SERVICES = IP  + "servicestimes/";
    public  static final  String ADD_EVENT = IP  + "newEvent/";
    public  static final  String GET_EVENT_LIST = IP  + "eventsList/";
    public  static final  String GET_FAVORITE_ESTABLISHMENTS = IP  + "favorites/";
    public  static final  String ADD_TOW_TRUCK_SERVICE = IP  + "towTruckRequest/";
    public  static final  String GET_TOW_TRUCKS = IP  + "towTrucks/";
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