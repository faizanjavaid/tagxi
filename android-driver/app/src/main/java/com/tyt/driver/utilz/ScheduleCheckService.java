package com.tyt.driver.utilz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.tyt.driver.ui.homeScreen.HomeAct;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleCheckService extends Service {

    private Timer timer;
    final  int REFRESH=0;
    Context context;
    private PendingIntent pendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context=this;
        //==============================================

        TimerTask refresher;
        // Initialization code in onCreate or similar:
        timer = new Timer();    
        refresher = new TimerTask() {
            public void run() {
              handler.sendEmptyMessage(0);
            };
        };
        // first event immediately,  following after 1 seconds each
        timer.scheduleAtFixedRate(refresher, 0,1000); 
        //=======================================================

    }

    final  Handler handler = new Handler() {


        public void handleMessage(Message msg) {
              switch (msg.what) {
              case REFRESH: 
                   //your code here 
          PendingIntentmethod();

                  break;
              default:
                  break;
              }
          }
        };


         void PendingIntentmethod()
         {
         Intent myIntent = new Intent(context, HomeAct.class);
         pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
         AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();


         }




}