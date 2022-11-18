package com.tyt.driver.utilz.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tyt.driver.app.MyApp;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            MyApp.getmContext().startForegroundService(new Intent(MyApp.getmContext(), SensorService.class));
        else
            MyApp.getmContext().startService(new Intent(MyApp.getmContext(), SensorService.class));
    }
}
