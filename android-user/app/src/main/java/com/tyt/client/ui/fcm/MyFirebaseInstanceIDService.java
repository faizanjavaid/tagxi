package com.tyt.client.ui.fcm;

import android.util.Log;

import com.tyt.client.utilz.SharedPrefence;
import com.google.firebase.messaging.FirebaseMessagingService;


import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    @Inject
    SharedPrefence sharedPrefence;

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);

        Log.d(TAG, "Refreshed token: " + s);
        //sharedPrefence.savevalue(SharedPrefence.FCMTOKEN,s);
        Log.e("FCM ", "Token in FID :" + s);

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     **/

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //  if(token!=null)
        //  sharedPrefence.savevalue(SharedPrefence.FCMTOKEN,token);
    }
}