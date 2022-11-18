package com.tyt.driver.utilz.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tyt.driver.app.MyApp;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.LocationUpdatesService;


public class SyncRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "keys-----Service Stops! Oooooooooooooppppssssss!!!!");
        if (!SyncUtils.checkIfSyncEnabled(context)) {
            SyncUtils.CreateSyncAccount(context);
            SyncUtils.TriggerRefresh();
        }
        SyncUtils.syncSendData(" ");
        if (!CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                MyApp.getmContext().startForegroundService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
            else
                MyApp.getmContext().startService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
        }
    }
}