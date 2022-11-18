package com.tyt.driver.utilz.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class MyJobIntentService  extends JobIntentService {
    static final int JOB_ID = 1000; //Unique job ID.

    //Convenience method for enqueuing work in to this service.

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, JobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // This describes what will happen when service is triggered
    }
}
