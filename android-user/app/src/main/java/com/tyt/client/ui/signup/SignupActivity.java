package com.tyt.client.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.ActivitySignupBinding;
import com.tyt.client.ui.applyRefferal.ApplyRefferalAct;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.country.Countrylistdialog;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.ui.otpscreen.OTPActivity;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * Created by Mahi in 2021.
 * It is class which handles all signup events from getting data from user to send to the server.
 */

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> implements SignupNavigator, HasAndroidInjector {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    SignupViewModel loginViewModel;
    ActivitySignupBinding activitySignupBinding;
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference requestRef = database.getReference("call_FB_OTP");
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignupBinding = getViewDataBinding();
        loginViewModel.setNavigator(this);
        loginViewModel.countryCode.set(Constants.defaultCountryCode);
        getGCMDeviceToken();

        if (getIntent().hasExtra(Constants.country) && getIntent().hasExtra(Constants.phone)) {
            loginViewModel.countryCode.set(getIntent().getStringExtra(Constants.country));
            loginViewModel.phone.set(getIntent().getStringExtra(Constants.phone));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String jsonStr = String.valueOf(snapshot.getValue());
                Log.e("getFIrebaseString---", "string---" + jsonStr);
                sharedPrefence.savevalue(SharedPrefence.showFBOTP, jsonStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                countryChoose, new IntentFilter(Constants.INTENT_COUNTRY_CHOOSE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countryChoose);
    }

    private final BroadcastReceiver countryChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
                loginViewModel.countryCode.set(intent.getStringExtra(Constants.countryCode));
        }
    };

    /**
     * Retrieves FCM device token,
     * Get current country code
     *
     * @param removeFirstZeros
     * @params
     */


    @Override
    public void openOtpActivity(String country, String removeFirstZeros) {
        startActivity(new Intent(this, OTPActivity.class)
                .putExtra(Constants.country, country)
                .putExtra(Constants.phone, removeFirstZeros));
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void onClickOpenLogin() {
        startActivity(new Intent(this, LoginAct.class));
    }

    @Override
    public void onClickBack() {
        startActivity(new Intent(this, LoginOrSignAct.class));
        finish();
    }

    @Override
    public void openCountryDialog() {
        Countrylistdialog newInstance = Countrylistdialog.newInstance();
        newInstance.show(this.getSupportFragmentManager());
    }

    @Override
    public void openHomAct() {
        startActivity(new Intent(this, ApplyRefferalAct.class));
        finish();
    }

    @Override
    public void onGetPhoneNum() {
        String phone = Objects.requireNonNull(activitySignupBinding.TIEPhonefield.getText()).toString();
        if (!phone.isEmpty()) {
            loginViewModel.phone.set(phone);
        }
    }

    @Override
    public SignupViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeAct();
    }

    private void closeAct() {
        startActivity(new Intent(this, LoginOrSignAct.class));
        finish();
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }


    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }


}