package com.tyt.client.utilz.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;


import com.tyt.client.app.MyApp;

import java.util.Locale;

/**
 * Created by Mahi in 2021.
 */

public class LanguageUtil {
    public static void changeLanguageType(Context context, Locale localelanguage) {
        Log.i("=======", "context = " + context);
        Resources resources = MyApp.getmContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(localelanguage);
        } else {
            config.locale = localelanguage;
            resources.updateConfiguration(config, dm);
        }
    }

    public static Locale getLanguageType(Context context) {
        Log.i("=======", "context = " + context);
        Resources resources = MyApp.getmContext().getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }
}
