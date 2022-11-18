package com.tyt.driver.utilz.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.ui.fcm.MyFirebaseMessagingService;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.LocationUpdatesService;
import com.tyt.driver.utilz.NetworkUtils;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Define a sync adapter for the app.
 * <p>
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 * <p>
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    /**
     * URL to fetch & send content from during a sync.
     */
//    String SOCKET_URL = "http://192.168.1.27:3001/driver/home";
//    public static Socket mSocket = null;
    public static SharedPrefence sharedPreferences;


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d("keys-", TAG);
        sharedPreferences = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(context), PreferenceManager.getDefaultSharedPreferences(context).edit());
        if (!CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                context.startForegroundService(new Intent(context, LocationUpdatesService.class));
            } else {
                MyApp.getmContext().startService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
            }

        }
    }


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        sharedPreferences = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(context), PreferenceManager.getDefaultSharedPreferences(context).edit());
        if (!CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class)) {
            MyApp.getmContext().startService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
        }
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * .
     * <p>
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     * <p>
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d("xxxkeys", "keys----isMyServiceRunning() = " + CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class));
        if (!CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class)) {
            MyApp.getmContext().startService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
        }
        if (MyApp.isIsDrawerActivityVisible())
            return;
        if (sharedPreferences == null)
            sharedPreferences = new SharedPrefence(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()), PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()).edit());
        if (!sharedPreferences.GetBoolean(SharedPrefence.isOnline))
            return;
//        if (sharedPreferences.getIntvalue(SharedPrefence.REQUEST_ID) != Constants.NO_REQUEST)
//            return;
        if (!checkLocationorGPSEnabled()) {
            Log.d("xxxSyncAdapt", "onPerformSync: ");
            if (MyApp.getmContext() != null) {
                MyFirebaseMessagingService.displayNotificationConnectivity(MyApp.getmContext(), MyApp.getmContext().getString(R.string.text_location_notification_hint));
            }
            return;
        } else if (!SyncUtils.checkIfSyncEnabled(MyApp.getmContext())) {
            Log.d("xxxSyncAdapt", "onPerformSync: ");
            if (MyApp.getmContext() != null)
                MyFirebaseMessagingService.displayNotificationConnectivity(MyApp.getmContext(), MyApp.getmContext().getString(R.string.text_sync_disabled));
            return;
        } else {
            Log.d("xxxSyncAdapt", "onPerformSync: ");
            MyFirebaseMessagingService.cancelConnectivityNotification(MyApp.getmContext());
            if (!CommonUtils.isMyServiceRunning(MyApp.getmContext(), LocationUpdatesService.class)) {
                MyApp.getmContext().startService(new Intent(MyApp.getmContext(), LocationUpdatesService.class));
            }
        }
    }

    public boolean isNetworkConnected() {
        boolean result = NetworkUtils.isNetworkConnected(MyApp.getmContext());
        return result;
    }

    public boolean checkLocationorGPSEnabled() {
        LocationManager locationManager = (LocationManager) MyApp.getmContext().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !hasGPSDevice(MyApp.getmContext());
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    /**
     * to stop the running sync use flags to cancel the sync
     *
     * @link https://stackoverflow.com/questions/5705886/stop-android-syncadapter-sync
     */
    @Override
    public void onSyncCanceled(Thread thread) {
        super.onSyncCanceled(thread);
        Log.d(TAG, "cancelled");
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        Log.d(TAG, "cancelled");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "keys-----Finilized");
    }


}
