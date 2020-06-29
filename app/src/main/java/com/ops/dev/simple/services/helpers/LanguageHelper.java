package com.ops.dev.simple.services.helpers;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class LanguageHelper {

    public static void setAppLocale(Activity activity, String language) {
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        activity.getApplicationContext().createConfigurationContext(configuration);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getAppLocale(Activity activity) {
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
    }
}
