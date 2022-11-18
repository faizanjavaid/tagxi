package com.tyt.driver.utilz.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class SyncJobRestartService extends JobIntentService {

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
      //  SyncUtils.CreateSyncAccount(context);
//        enqueueWork(context, SensorService.class, JOB_ID, work);
//       context.sta(new Intent(context,SensorService.class));
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // your code
    }

}