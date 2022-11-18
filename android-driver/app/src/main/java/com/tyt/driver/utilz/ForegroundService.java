package com.tyt.driver.utilz;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tyt.driver.R;
import com.tyt.driver.ui.homeScreen.HomeAct;


public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Intent oreonotificationIntent = new Intent(this, HomeAct.class);
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent oreopendingIntent = PendingIntent.getActivity(this,
                    0, oreonotificationIntent, 0);

            Notification oreonotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Running BackGround")
                    .setContentText(getString(R.string.app_name))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                    .setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground)
                    .setContentIntent(oreopendingIntent)
                    .build();

            startForeground(1, oreonotification);
        }
    }

    private void setLog() {
        Log.e("Loggg", "log");
    }

    /**
     * @param intent  Service triggering Data
     * @param flags
     * @param startId
     * @return the Service restarted.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, HomeAct.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Running BackGround")
                    .setContentText(getString(R.string.app_name))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                    .setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

            setLog();
        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Running BackGround")
                    .setContentText(getString(R.string.app_name))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                    .setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();

            startForeground(1, notification);

            setLog();
        }



        //do heavy work on a background thread


        //stopSelf();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}