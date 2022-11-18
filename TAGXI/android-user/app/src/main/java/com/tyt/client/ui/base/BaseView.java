package com.tyt.client.ui.base;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;

import com.tyt.client.utilz.exception.CustomException;

/**
 * Created by root on 9/28/17.
 */

public interface BaseView {
    void showMessage(String message);

    void showMessage(int resId);

    void showMessage(CustomException e);

    void showSnackBar(String message);

    void showSnackBar(@NonNull View view, String message);

    boolean isNetworkConnected();

    void showNetworkMessage();

    void refreshCompanyKey();
    default void driverCurrentLatLong(double lat,double lan){
        Log.d("xxxBaseView", "driverCurrentLatLong() called with: lat = [" + lat + "], lan = [" + lan + "]");
    }
}
