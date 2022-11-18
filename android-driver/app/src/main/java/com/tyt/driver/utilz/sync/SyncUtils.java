package com.tyt.driver.utilz.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.sync.common.accounts.GenericAccountService;
import com.tyt.driver.utilz.sync.provider.FeedContract;

/**
 * Static helper methods for working with the sync framework.
 */
public class SyncUtils {
    private static final long SYNC_FREQUENCY = 2;  // 1 hour (in seconds)
    private static final String CONTENT_AUTHORITY = FeedContract.CONTENT_AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    // Value below must match the account type specified in res/xml/syncadapter.xml
    public static final String ACCOUNT_TYPE = "com.captaincare.Drivers.Sync.account";

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void CreateSyncAccount(Context context) {
        Log.d("xxxSyncUtils", "CreateSyncAccount: ");
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = GenericAccountService.GetAccount(ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
//            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, true);
//            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, new Bundle(), 60*1000);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            Bundle bundle = new Bundle();
            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, bundle, SYNC_FREQUENCY);
            newAccount = true;
        } else {
            ContentResolver.setIsSyncable(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, 1);

        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount /*|| !setupComplete*/) {
            TriggerRefresh();
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).apply();
        }
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void TriggerRefresh() {
//        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
//        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.setSyncAutomatically(
                GenericAccountService.GetAccount(ACCOUNT_TYPE), // Sync account
                FeedContract.CONTENT_AUTHORITY,                 // Content authority
                true);                                             // Extras
    }

    public static void syncCancel() {
        int syncOnOff = 0;
        ContentResolver.setIsSyncable(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, syncOnOff);
    }

    public static void syncCancelOld() {
        ContentResolver.cancelSync(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY);
        ContentResolver.removePeriodicSync(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, new Bundle());
        int syncOnOff = 0;
        ContentResolver.setIsSyncable(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, syncOnOff);
    }

    public static long lastUpdated = 0;

    public static void syncSendData(String data) {
        Log.d("xxxSynUtils", "syncSendData: ");
        if ((System.currentTimeMillis() - lastUpdated) < 10000) {
            lastUpdated = System.currentTimeMillis();
            return;
        }

        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(data))
            bundle.putString(Constants.IntentExtras.LOCATION_DATA, data);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY)) {
            ContentResolver.setIsSyncable(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, true);
        }

        ContentResolver.requestSync(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY, bundle);
        lastUpdated = System.currentTimeMillis();

    }


    public static boolean checkIfSyncEnabled(Context context) {
        if (ContentResolver.getIsSyncable(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY) <= 0) {

            // sync is not enable
            return false;
        } else return ContentResolver.getSyncAutomatically(GenericAccountService.GetAccount(ACCOUNT_TYPE), FeedContract.CONTENT_AUTHORITY);
    }
}
