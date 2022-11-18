package com.tyt.client.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.tyt.client.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;


/**
 * Created by Mahi in 2021.
 */

public class MyApp extends Application implements HasAndroidInjector, Application.ActivityLifecycleCallbacks {

    /*
    This Class contains the context and lifecycler methods to handle injection events in the Dependency Injection Using Dagger.
    * */

    @Inject
    DispatchingAndroidInjector<Object> activityDispatchingAndroidInjector;

    private  static  Context mContext;

    Activity topDriverAct;
    static boolean isDriverEnabled = false;
    static boolean isNodDriveActDestroyed = true;

    @Override
    public void onCreate() {
        super.onCreate();

        /*Creating the Dagger App Component Using The MyApp Class*/

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);

        mContext = this;
        registerActivityLifecycleCallbacks(this);

    }

    public static Context getmContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("ActPaused", "Pause");

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isIsNodDriveActDestroyed() {
        return isNodDriveActDestroyed;
    }

    public static void setIsNodDriveActDestroyed(boolean isNodDriveActDestroyed) {
        MyApp.isNodDriveActDestroyed = isNodDriveActDestroyed;
    }

    public static boolean clearMethd() {
        return isDriverEnabled;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return activityDispatchingAndroidInjector;
    }

}
