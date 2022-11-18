/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tyt.driver.utilz;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.ui.acceptReject.AccepRejectAct;
import com.tyt.driver.utilz.sync.SyncRestartReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 * <p>
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 * <p>
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification assocaited with that service is removed.
 */
public class LocationUpdatesService extends Service implements SocketHelper.SocketListener {

    public static final String PACKAGE_NAME = "com.hive.driver.utilz.Location";
    public static final String TAG = "LocationUpdatesService";
    public static final String EXTRA_LOCATION = PACKAGE_NAME;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";
    private final IBinder mBinder = new LocalBinder();
    SharedPrefence sharedPrefence;


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long INTERVAL = 1000 * 5;
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_INTERVAL = 1000 * 3;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;


    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * Socket
     */
    // Socket mSocket = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor shardPrefEditor;
    public String userID, isTripStarted;
    public long lastUpdated = System.currentTimeMillis();
    GitHubService apiService;
    Gson gson = new Gson();
    public String id, lat, lng, bearing;
    // Schedule the task such that it will be executed every second
    ScheduledFuture<?> scheduledFuture;
    public Context applicationContext;

    void initializeSharedPreferences(Context application) {
        sharedPrefence = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        shardPrefEditor = sharedPreferences.edit();
        applicationContext = application;
    }

    //Initialize the OKHttp clients for Api call.
    void initializeAPI(Context application) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder();
        okhttpbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
                return chain.proceed(request);
            }
        });
        okhttpbuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpbuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpbuilder.addInterceptor(logging);
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpbuilder.build())
                .baseUrl(Constants.URL.BaseURL);
        apiService = builder.build().create(GitHubService.class);
    }

    /**
     * The current location.
     */
    private Location locationNew, locationOld;

    public LocationUpdatesService() {
    }

    @Override
    public void onCreate() {
        Log.e("Triggered", "trig");
        startServiceOreoCondition();
        if (sharedPreferences == null)
            initializeSharedPreferences(getApplication());

        if (apiService == null)
            initializeAPI(getApplication());
        /*if (!CommonUtils.checkLocationorGPSEnabled()) {
            MyFirebaseMessagingService.displayNotificationConnectivity(MyApp.getmContext(), MyApp.getmContext().getString(R.string.text_location_notification_hint));
        }*/
        // CommonUtils.setwifilock(getApplication());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
               /* if (!CommonUtils.checkLocationorGPSEnabled()) {
                    MyFirebaseMessagingService.displayNotificationConnectivity(MyApp.getmContext(), MyApp.getmContext().getString(R.string.text_location_notification_hint));
                }
                MyFirebaseMessagingService.cancelConnectivityNotification(MyApp.getmContext());
                if (sharedPreferences.getv(SharedPrefence.IS_OFFLINE, false))
                    Log.e("lkgnf", "kjrg");
                else*/

                //if (MyApp.isHomeActDestroyed)

                if (sharedPreferences.getBoolean(SharedPrefence.isOnline, false))
                    onNewLocation(locationResult.getLastLocation());
                else stopSelf();
            }
        };
       /* if (sharedPreferences.getBoolean(SharedPrefence.IS_OFFLINE, false))
            offSocket();*/
        createLocationRequest();
        getLastLocation();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
    }

    //Restart the Service when its not connected.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "keys----Service started");
        Log.i("keys---", "here -LocationUpdateService!");
        if (intent == null) {
            removeLocationUpdates();
            stopSelf();
            return START_NOT_STICKY;
        }
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration) {
            Log.i(TAG, "Starting foreground service");

//            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "-keys----- on Destroy");
//        super.onDestroy();
        mServiceHandler.removeCallbacksAndMessages(null);
//        offSocket();
        Intent broadcastIntent = new Intent(this, SyncRestartReceiver.class);
        broadcastIntent.setAction("restartservice");
        sendBroadcast(broadcastIntent);
        //CommonUtils.setwifilock(getApplication());
        if (scheduledFuture != null)
            if (!scheduledFuture.isCancelled())
                scheduledFuture.cancel(true);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
//        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       /* if (sharedPreferences.getBoolean(SharedPrefence.IS_OFFLINE, false))
            offSocket();
        else if (MyApp.isIsDrawerActivityDestroyed())
            initiateSocket();*/
    }

    //Stop the Service when it not used.
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            stopSelf();
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }


    //GEt the Last known Locaions or CurrentLocations
    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                locationNew = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    double lastBearing = 0, currentBearing = 0;

    /**
     * @param location is a location instance for updating the Locations frequently
     */
    private void onNewLocation(Location location) {

        Log.e("LocationUpdated===", "updates==" + location.getLatitude());
        locationNew = location;
        if (locationOld == null) {
            locationOld = locationNew;
        }

        Log.i(TAG, "New location: " + location);
        // if (!location.isFromMockProvider()) {

        if (!TextUtils.isEmpty(sharedPreferences.getString(SharedPrefence.ID, ""))) {

            currentBearing = locationOld.bearingTo(locationNew);
            Log.d("keys-Data", "Old=" + lastBearing + "New=" + ((currentBearing == lastBearing) ? ((currentBearing + 1) + "") : (currentBearing + "")));
            if (lastBearing == 0)
                lastBearing = currentBearing;
            Log.d("keys-Data", "Old=" + lastBearing + "New=" + ((currentBearing == lastBearing) ? ((currentBearing + 1) + "") : (currentBearing + "")));
            sendSocketLocationMessage(location.getLatitude() + "",
                    location.getLongitude() + "",
                    sharedPreferences.getString(SharedPrefence.ID, ""),
                    (currentBearing == lastBearing) ? ((currentBearing + 1) + "") : (currentBearing + ""));
            lastBearing = currentBearing;
        }

        Log.e("lastUpdate", "______" + lastUpdated + "_____" + (System.currentTimeMillis() - lastUpdated));
        //if (System.currentTimeMillis() - lastUpdated > 10000) {

        Log.e("fjokj", "" + lat + "___" + lng + "___" + bearing + "___" + sharedPrefence.Getvalue(SharedPrefence.ID));

        if (apiService != null
                && !CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.ID))
                && !CommonUtils.IsEmpty(lat) && !CommonUtils.IsEmpty(lng) && !CommonUtils.IsEmpty(bearing)) {
            JSONObject map = new JSONObject();
            try {

                Log.e("ID--", "IDVALUE--" + sharedPrefence.Getvalue(SharedPrefence.ID));

                map.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
                map.put(Constants.NetworkParameters.LAT, lat);
                map.put(Constants.NetworkParameters.LNG, lng);
                map.put(Constants.NetworkParameters.BEARING, bearing);
                map.put(Constants.NetworkParameters.isActive, sharedPrefence.GetBoolean(SharedPrefence.isOnline));

                if (!sharedPrefence.Getvalue(SharedPrefence.VehTypeID).isEmpty())
                    map.put(Constants.NetworkParameters.VehId, sharedPrefence.Getvalue(SharedPrefence.VehTypeID));

                sharedPrefence.savevalue(SharedPrefence.BEARING, bearing);

//                    if (!sharedPreferences.getString(SharedPrefence.OrderId, "").equalsIgnoreCase(""))
//                        map.put(Constants.NetworkParameters.OrderId, sharedPreferences.getString(SharedPrefence.OrderId, ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (CommonUtils.isNetworkConnected(this)) {
                // SocketHelper.init(sharedPrefence, this, TAG);

//                if (MyApp.isInsideTrip())
                    SocketHelper.setDriverLocation(map.toString());
                lastUpdated = System.currentTimeMillis();
            } else
                Toast.makeText(applicationContext, "NetWork Error", Toast.LENGTH_SHORT).show();
        }

        //  }

        id = sharedPreferences.getString(SharedPrefence.ID, "");
        lat = location.getLatitude() + "";
        lng = location.getLongitude() + "";
        bearing = locationOld.bearingTo(locationNew) + "";
        locationOld = locationNew;

//        if (!sharedPreferences.getString(SharedPrefence.REQUEST_ID, "").equalsIgnoreCase("-11")) {
//            Intent intent = new Intent(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
//            intent.putExtra(EXTRA_LOCATION, location);
//            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//        }

    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requestLocationUpdates();
        initializeSharedPreferences(getApplication());
        if (apiService == null)
            initializeAPI(getApplication());
      /*  if (mSocket == null) {
            try {
                mSocket = IO.socket(Constants.URL.SOCKET_URL);
                initiateSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }*/
        //setupPeriodicUpdate();
    }

    /**
     * @param lat     latitude of lastKnown locations
     * @param lng     longitude of lastKnown locations
     * @param bearing Bearing values
     * @param id      Driver Id
     * @param token   Driver Unique token.
     *                <p>
     *                Update the locations to socket.
     */
    public void sendLocation(String lat, String lng, String bearing, String id, String token) {
       /* if (sharedPreferences.getBoolean(SharedPrefence.IS_OFFLINE, false))
            return;
        Log.e(TAG, "Keys______________-UpdateStarted---Drawer = " + MyApp.isIsDrawerActivityDestroyed() + "  bearing = " + bearing);
        if (!MyApp.isIsDrawerActivityDestroyed()) {

            Intent intent = new Intent(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
            intent.setAction(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
            intent.putExtra(Constants.IntentExtras.LOCATION_ID, id);
            intent.putExtra(Constants.IntentExtras.LOCATION_LAT, lat);
            intent.putExtra(Constants.IntentExtras.LOCATION_LNG, lng);
            intent.putExtra(Constants.IntentExtras.LOCATION_BEARING, bearing);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            }
            //  offSocket();
        } else {

            *//*if (!mSocket.connected())
                initiateSocket();*//*
            if (!CommonUtils.checkLocationorGPSEnabled(this)) {
                MyFirebaseMessagingService.displayNotificationConnectivity(this, getString(R.string.text_location_notification_hint));
                return;
            }
            Log.e("Keys______________", " lastUpdated=" + lastUpdated + " (System.currentTimeMillis() - lastUpdated)" + (System.currentTimeMillis() - lastUpdated) + " ===>" + ((System.currentTimeMillis() - lastUpdated) >= Constants.MinRequestTime));
            if (lastUpdated == 0 || ((System.currentTimeMillis() - lastUpdated) >= 4000)) {
                if (apiService != null
                        && !CommonUtils.IsEmpty(id) && !CommonUtils.IsEmpty(token)
                        && !CommonUtils.IsEmpty(lat) && !CommonUtils.IsEmpty(lng)
                        && !CommonUtils.IsEmpty(lat) && !CommonUtils.IsEmpty(bearing)) {
                    JSONObject map = new JSONObject();
                    try {
                        map.put(Constants.NetworkParameters.id, id);
                        map.put(Constants.NetworkParameters.token, token);
                        map.put(Constants.NetworkParameters.LAT, lat);
                        map.put(Constants.NetworkParameters.LNG, lng);
                        map.put(Constants.NetworkParameters.BEARING, bearing);
                        map.put(Constants.NetworkParameters.RANDOM, (Math.random() * 49 + 1) + "");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!CommonUtils.isNetworkConnected(this))
                        MyFirebaseMessagingService.displayNotificationConnectivity(MyApp.getmContext(), MyApp.getmContext().getString(R.string.text_enable_internet));
                    else {
                        SocketHelper.setDriverLocation(map.toString());
                    }
                }
                SyncUtils.syncSendData("");
                lastUpdated = System.currentTimeMillis();

            }
        }*/
    }


    //Send the Trip location when app is in background.
    public void sendSocketLocationMessage(String lat, String lng, String id, String bearing) {
        JSONObject object = new JSONObject();
        JSONObject objectParent = new JSONObject();
        JSONArray locationArray = new JSONArray();
        try {
            objectParent.put(Constants.NetworkParameters.LAT, lat);
            objectParent.put(Constants.NetworkParameters.LNG, lng);
            object.put(Constants.NetworkParameters.id, id);
            object.put(Constants.NetworkParameters.LAT, lat);
            object.put(Constants.NetworkParameters.LNG, lng);
            object.put(Constants.NetworkParameters.BEARING, bearing);
            locationArray.put(objectParent);
            object.put(Constants.NetworkParameters.LAT_LNG_ARRAY, locationArray);

            userID = sharedPreferences.getString(SharedPrefence.USER_ID, "-1");
            isTripStarted = sharedPreferences.getString(SharedPrefence.TRIP_START, "-1");
            String isTripArrived = sharedPreferences.getString(SharedPrefence.TRIP_ARRIVED, "-1");

            if (!userID.equalsIgnoreCase("-1"))
                object.put(Constants.NetworkParameters.USER_ID, userID);
            if (!isTripStarted.equalsIgnoreCase("-1"))
                object.put(Constants.NetworkParameters.TRIP_START, "" + isTripStarted);

            if (!isTripArrived.equalsIgnoreCase("-1"))
                object.put(Constants.NetworkParameters.TRIP_ARRIVED, "" + isTripArrived);

//            if (isShareRide != Constants.NO_ID)
//                object.put(Constants.NetworkParameters.IS_SHARE, "" + isShareRide);
            object.put(Constants.NetworkParameters.request_id, sharedPreferences.getString(SharedPrefence.REQUEST_ID, ""));
            sendTripLocation(bearing, lat, lng, object + "");
            locationArray = new JSONArray();
            lastUpdated = System.currentTimeMillis();

//            if (sharedPreferences != null) {
//                sharedPreferences.edit().putString(SharedPrefence.Lat, lat);
//                sharedPreferences.edit().putString(SharedPrefence.Lng, lat);
//                sharedPreferences.edit().putString(SharedPrefence.BEARING, bearing);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "sendSocketLocationMessage: Error==>"+e.getMessage());
        }
    }

    //Send the Trip location when app is in background.
    public void sendTripLocation(String bearing, String lat, String lng, String jsonObject) {
        Log.d(TAG, "Keys______________-" + jsonObject);

        Log.e("lat&lng--", "" + lat + "____" + lng);
        sharedPrefence.savevalue(SharedPrefence.lat, lat);
        sharedPrefence.savevalue(SharedPrefence.lng, lng);
        sharedPrefence.savevalue(SharedPrefence.BEARING, bearing);

        if (!MyApp.isIsDrawerActivityDestroyed()) {
            Intent intent = new Intent(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
            intent.setAction(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
            intent.putExtra(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE, jsonObject);
//            intent.putExtra(Constants.IntentExtras.TRIP_BEARING, bearing);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }

    }

    /*  */

    /**
     *
     *//*
    @Override
    public void DriverRequest(Object typesString) {
       *//* if (typesString != null && typesString.toString().length() > 0) {
            RequestModel model = CommonUtils.getSingleObject(typesString + "", RequestModel.class);
            if (model != null)
                if (model.request != null) {
                    if (sharedPreferences == null)
                        initializeSharedPreferences(MyApp.getmContext());
                    if (apiService == null)
                        initializeAPI(getApplication());
                    if (!MyApp.isIsAcceptRejectActivityVisible() && !MyApp.getIsInsideTrip())
                        if (sharedPreferences.getInt(SharedPrefence.LAST_REQUEST_ID, Constants.NO_REQUEST) != model.request.id)
                            openAcceptReject(model, typesString + "");
                }

        }*//*

    }*/
    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(MyApp.getmContext());
    }

    @Override
    public void OnConnect() {

    }

    @Override
    public void OnDisconnect() {

    }

    @Override
    public void OnConnectError() {

    }

    @Override
    public void onCreateRequest(String s) {
        Log.d("xxxLocationUpdatServ", "onCreateRequest: ");
        if (!MyApp.isIsAcceptRejectVisible()) {
            Log.e("xxxinsideLocaService", "insideLocaService");
            Intent acceptRejectIntent = new Intent(this, AccepRejectAct.class);
            acceptRejectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            acceptRejectIntent.putExtra(Constants.IntentExtras.ACCEPT_REJECT_DATA, s);
            startActivity(acceptRejectIntent);
        }

    }
    @Override
    public void ReceivedChatStatus(String toString) {
        Log.d("--LocationUpdate", "ReceivedChatStatus: "+toString);
    }
    @Override
    public void RequestHandler(String toString) {

    }

    @Override
    public void updateTripDistance(double v) {

    }

    @Override
    public void ApprovalStatus(String toString) {

    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public static boolean serviceIsRunningInForeground(Context context, Class className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (className.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    //Restart the Service.
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        applicationContext = getApplicationContext();
        Intent restartServiceIntent = new Intent(applicationContext, this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(applicationContext, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmService.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);
            alarmService.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);
        } else
            alarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);

        Log.d("keys---", "keys----Removed----" + TAG);
        super.onTaskRemoved(rootIntent);
    }


    //Start the foreground notifications.
    private void startServiceOreoCondition() {
        if (Build.VERSION.SDK_INT >= 26) {
            Log.d("xxxLocationUpdateServ", "startServiceOreoCondition:->736 ");
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,
                    Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_SERVICE).setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground).setPriority(PRIORITY_MIN).
                            setContentTitle("TYT running in background").build();
            startForeground(101, notification);
        } else {
            Log.d("xxxLocationUpdateServ", "startServiceOreoCondition:->745 ");
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, Constants.CHANNEL_ID);
            notificationBuilder.setContentTitle("HiveDBoy")
                    .setContentText("TYT running in background")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new_icon_foreground))
                    .setSmallIcon(R.mipmap.ic_launcher_new_icon_foreground).setPriority(PRIORITY_MIN);
            startForeground(101, notificationBuilder.build());
        }
    }

}
