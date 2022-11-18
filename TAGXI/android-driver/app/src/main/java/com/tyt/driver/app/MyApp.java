package com.tyt.driver.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDex;

import android.util.Log;

import dagger.android.HasAndroidInjector;

import com.tyt.driver.di.component.DaggerAppComponent;
import com.tyt.driver.ui.acceptReject.AccepRejectAct;
import com.tyt.driver.ui.homeScreen.HomeAct;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;

/**
 * Created by root on 9/22/17.
 */

public class MyApp extends Application implements HasAndroidInjector, Application.ActivityLifecycleCallbacks {

    @Inject
    DispatchingAndroidInjector<Object> activityDispatchingAndroidInjector;
    private static Context mContext;

    Activity topDriverAct;
    static boolean isDriverEnabled = false;
    static boolean isNodDriveActDestroyed = true;
    private static boolean isDrawerActivityDestroyed = true;
    private static boolean isInsideTrip = true;
    private static boolean isDrawerActivityVisible = false;
    private static boolean isAcceptRejectVisible = false;


    @Override
    public void onCreate() {
        super.onCreate();
        //initiatingCrashlytics();
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

    /**
     * initialize crashlytics Fabric
     **/
    private void initiatingCrashlytics() {
        //Fabric.with(this, new Crashlytics());
        // TODO: Move this to where you establish a user session
        //logUser();
    }

    /**
     * login to crashlytics
     **/
   /* private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("TapnGoDriver");
    }*/

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof AccepRejectAct)
            isAcceptRejectVisible = true;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof AccepRejectAct)
            isAcceptRejectVisible = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof AccepRejectAct)
            isAcceptRejectVisible = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("ActPaused", "Pause");

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof HomeAct)
            isDrawerActivityVisible = false;

        if (activity instanceof AccepRejectAct)
            isAcceptRejectVisible = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof HomeAct) {
            isDrawerActivityDestroyed = true;
            // SyncUtils.CreateSyncAccount(this);
        }
        if (activity instanceof AccepRejectAct)
            isAcceptRejectVisible = false;
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

    public static boolean isIsDrawerActivityDestroyed() {
        return isDrawerActivityDestroyed;
    }


    public static boolean isInsideTrip() {
        return isInsideTrip;
    }


    public static void setInsideTrip(boolean isInsideTrip) {
        MyApp.isInsideTrip = isInsideTrip;
    }

    // Check the DrawerActivity is in Resumed state or not.
    public static boolean isIsDrawerActivityVisible() {
        return isDrawerActivityVisible;
    }

    public static boolean isIsAcceptRejectVisible() {
        return isAcceptRejectVisible;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return activityDispatchingAndroidInjector;
    }
}
