package com.tyt.driver.ui.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tyt.driver.R;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Resources resources;
    @Inject
    SharedPrefence sharedPrefence;

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);

        if (sharedPrefence != null)
            sharedPrefence.savevalue(SharedPrefence.FCMTOKEN, s);
        Log.e("FCM ", "Token in FMS :" + s);
    }

    public static void cancelConnectivityNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(R.string.connectivity_notification_channel_id);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("xxxFirebaseMesgServ", "From: " + remoteMessage.getFrom());
        Log.d("xxxFirebaseMesgServ", "DATA: " + remoteMessage.getData());
        Log.d("xxxFirebaseMesgServ", "Id: " + remoteMessage.getMessageId());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if (CommonUtils.IsEmpty(remoteMessage.getData().get("message")))
                return;
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().get("message"));
                Log.d("xxxFirebaseMesgServ", "onMessageReceived:->84 " + json);
                // BaseResponse model = CommonUtils.getSingleObject(json.toString(), BaseResponse.class);
                if (isAppInBackground()) {
                    sendNotification("New TripRequested Arrived", 1);
                }
//                if (remoteMessage.getData().get("title") != null && remoteMessage.getData().get("title").equalsIgnoreCase("general notification")) {
//                 sendNotification(remoteMessage.getData().get("message"), 1);
//
//                    //sendNotification(new Gson().toJson(model), 1);
//                } else
//                {
//                    Log.d("xxxFirebaseMesgServ", "onMessageReceived: Else");
//                    handleNow();
//                }

              /* if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("bill_generated") ||
                        model.successMessage.equalsIgnoreCase("driver arrived") ||
                        model.successMessage.equalsIgnoreCase("Trip Started") ||
                        model.successMessage.equalsIgnoreCase("pay_by_cash") ||
                        model.successMessage.equalsIgnoreCase("pay_balance_amount")) {


//                 Intent intent = new Intent(Constants.PushWaitingorAcceptByDriver);
//                    intent.putExtra(Constants.EXTRA_Data, json.toString());
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


                    if (model.successMessage.equalsIgnoreCase("bill_generated")) {
                        Intent intent = new Intent(Constants.PushTripCompleted);
                        intent.putExtra(Constants.EXTRA_Data, json.toString());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        sendNotification(getTranslatedString(R.string.Txt_TripCompleted), json.getInt("msg_id"));
                    } else if (model.successMessage.equalsIgnoreCase("Trip Started")) {
                        Intent intent = new Intent(Constants.PushTripCompleted);
                        intent.putExtra(Constants.EXTRA_Data, json.toString());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        sendNotification(getTranslatedString(R.string.Txt_TripStarted), 23496);
                    } else if (model.successMessage.equalsIgnoreCase("driver arrived")) {
                        Intent intent = new Intent(Constants.PushTripCompleted);
                        intent.putExtra(Constants.EXTRA_Data, json.toString());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        sendNotification(getTranslatedString(R.string.txt_arrived), 2342);
                    } else if (model.successMessage.equalsIgnoreCase("pay_by_cash"))
                        sendNotification(json.getString("message"), json.getInt("msg_id"));
                    else if (model.successMessage.equalsIgnoreCase("pay_balance_amount"))
                        sendNotification(json.getString("message"), json.getInt("msg_id"));


                } else if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("Accepted")) {
                    Intent intent = new Intent(Constants.PushWaitingorAcceptByDriver);
                    intent.putExtra(Constants.EXTRA_Data, json.toString());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    sendNotification(getTranslatedString(R.string.Txt_DriverAccepted), json.getInt("msg_id"));

                    // its for leave from top driver page when trip accepted.
                    Intent intent11 = new Intent(Constants.pushRejected);
                    intent11.putExtra("message", "Ride Has been scheduled");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent11);
                    //   startActivity(new Intent(this, HomeAct.class).putExtra("Accept", "Accepted"));
                } else if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("driver_rejected")) {
//                    TopDriverAct.show(this);
                    //this is for stay the top driver page when driver reject the request.
                    Intent intent = new Intent(Constants.pushRejected);
                    intent.putExtra("message", "Request Rejected");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                } else if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("Trip cancelled")) {
                    Intent intent = new Intent(Constants.PushTripCancelled);
                    intent.putExtra(Constants.EXTRA_Data, model.successMessage);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    sendNotification(getTranslatedString(R.string.Txt_TripCanceled), json.getInt("msg_id"));
                } else if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("no_driver_found")) {
                    Intent intent = new Intent(Constants.PushWaitingorAcceptByDriver);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    sendNotification(getTranslatedString(R.string.Txt_NoDriverFound), json.getInt("msg_id"));

                    Intent intent11 = new Intent(Constants.pushRejected);
                    intent11.putExtra("message", "No Driver Found");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent11);

                } else if (model.successMessage.equalsIgnoreCase("another_user_loggedin")) {
                    sendNotification(model.message, remoteMessage.getData().get("message"));

                } else if (!json.isNull("success_message") && model.successMessage.equalsIgnoreCase("Driver_started")) {
                    Intent intent = new Intent(Constants.PushWaitingorAcceptByDriver);
                    intent.putExtra(Constants.EXTRA_Data, json.toString());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    sendNotification(getTranslatedString(R.string.Driver_started), json.getInt("msg_id"));
                } else if (model.successMessage.equalsIgnoreCase("requested_another_driver")) {
                    Log.e("anotherDriver==", "driver==" + "anotherriver");
                    Log.e("anotherDriver121==", "driver==" + model.request.request_id + "vd" + model.request.id);

                    Intent intent11 = new Intent(Constants.driverChanged);
                    intent11.putExtra("req_id", model.request.request_id);
                    intent11.putExtra("id", model.request.id);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent11);
                } else if (model.successMessage.equalsIgnoreCase("ride_later_cancelled_because_of_no_driver_found"))
                    sendNoDriverNotification(getTranslatedString(R.string.Txt_NoDriverFound), model.request.id);
                else if (model.successMessage.equalsIgnoreCase("user_declined"))
                    sendNotification(getTranslatedString(R.string.txt_user_declained), json.getInt("msg_id"));
                else if (model.successMessage.equalsIgnoreCase("user_actived"))
                    sendNotification(getTranslatedString(R.string.txt_user_approved), json.getInt("msg_id"));
                else if (model.successMessage.equalsIgnoreCase("user_cancellation_owes_waring"))
                    sendNotification(remoteMessage.getData().get("title"), json.getInt("msg_id")); */

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Sendpath(JSONArray images) {

    }

    private void handleNow() {
        Log.d("xxxFirebaseMsgServ", "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, int mesgid) {
        Log.d("xxxFirebaseMesgServ", "sendNotification:->206 " + messageBody);
        Intent intent = new Intent(this, HomeAct.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(this, defaultSoundUri);
//        r.play();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.mipmap.app_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setSound(defaultSoundUri)
                        //.setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, getString(R.string.default_notification_channel_id), importance);
            notificationManager.createNotificationChannel(mChannel);

        }
        notificationManager.notify(mesgid, notificationBuilder.build());
        wakupScreen();
    }

    public Ringtone ringtone() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNoDriverNotification(String messageBody, int messageForIntente) {

        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(Constants.BroadcastsActions.LATER_NO_DRIVER);
        intentBroadcast.putExtra("req_id", messageForIntente);
        intentBroadcast.putExtra("from", "push");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
        Intent intent = new Intent(this, HomeAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        //.setSmallIcon(R.drawable.ic_small_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, getString(R.string.default_notification_channel_id), importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(R.string.default_notification_channel_id, notificationBuilder.build());
        wakupScreen();
    }


    /**
     * {@link android.os.PowerManager.WakeLock} is used to bring device from sleep mode
     **/
    private void wakupScreen() {
        Log.d("xxxFirebaseMesgServ", "wakupScreen: 1");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, Constants.WAKE_LOCK_TAG);
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.WAKE_LOCK_TAG2);
            wl_cpu.acquire(10000);
        }

        PackageManager pm2 = this.getPackageManager();
        Intent intent2 = pm2.getLaunchIntentForPackage("com.tyt.driver");//for e.g.(com.android.sms)//but this app must be in your device
        if (intent2 != null) startActivity(intent2);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String messageForIntente) {
        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(Constants.BroadcastsActions.B_REQUEST);
        intentBroadcast.putExtra(Constants.BroadcastsActions.B_REQUEST, messageForIntente);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
        Intent intent = new Intent(this, HomeAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        //.setSmallIcon(R.drawable.ic_launcher_small)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, getString(R.string.default_notification_channel_id), importance);
            notificationManager.createNotificationChannel(mChannel);


        }
        notificationManager.notify(R.string.default_notification_channel_id, notificationBuilder.build());
        wakupScreen();
    }

    public static void wakupScreen(Context context) {
        Log.d("xxxFirebaseMesgServ", "wakupScreen:2 ");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }

    }

    /**
     * Returns translated string for given resource id
     **/
    public String getTranslatedString(int id) {
        if (resources == null)
            resources = getResources();
        if (resources.getResourceEntryName(id).equalsIgnoreCase("app_name") || resources.getResourceEntryName(id).equalsIgnoreCase("app_title"))
            return super.getString(id);
        else
            return getLocalizedString(resources.getResourceEntryName(id));
    }

    /**
     * Uses resource id to get translation from Translation sheet response
     **/
    public String getLocalizedString(String resId) {
        if (sharedPrefence == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPrefence = new SharedPrefence(preferences, preferences.edit());
        }
        String result = resId;
        try {
            JSONObject jsonObject;
            if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
                jsonObject = new JSONObject(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
                result = jsonObject.optString(resId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (TextUtils.isEmpty(result))
                result = resId;
        }
        return result;
    }


    public static void displayNotificationConnectivity(Context context, String messageBody) {
        Log.d("xxxFirebaseMesgServ", "displayNotificationConnectivity->406: " + messageBody);
        Intent intent = new Intent(context, HomeAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = context.getString(R.string.connectivity_notification_channel_id);
        Uri defaultSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground)//ic_launcher_small)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                        .setContentText(messageBody)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, context.getString(R.string.connectivity_notification_channel_id), importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(R.string.connectivity_notification_channel_id, notificationBuilder.build());
        wakupScreen(context);
    }

    private Boolean isAppInBackground() {
        ActivityManager.RunningAppProcessInfo myprocess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myprocess);
        return myprocess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
    }

}