package com.tyt.client.ui.otpscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class OTPReceiver extends BroadcastReceiver {
    private static smsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (mListener!=null){
                        mListener.messageReceived(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    Log.e("OTP Receiver", "TimeOut For OTP" );
                    break;
            }

        }

    }

    public static void bindListener(smsListener listener) {
        mListener = listener;
    }

}
