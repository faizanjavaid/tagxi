package com.tyt.driver.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ActivitySignupBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.carDetails.cardetailsAct.CarDetailsAct;
import com.tyt.driver.ui.country.Countrylistdialog;
import com.tyt.driver.ui.login.LoginAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.ui.otpscreen.OTPActivity;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import static com.tyt.driver.utilz.Constants.NetworkParameters.country;

/**
 * Created by naveen on 8/24/17.
 */

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> implements SignupNavigator, HasAndroidInjector {

    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    SignupViewModel loginViewModel;
    ActivitySignupBinding activitySignupBinding;
    EditText edt_text;

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
        LocalBroadcastManager.getInstance(this).registerReceiver(
                countryChoose, new IntentFilter(Constants.INTENT_COUNTRY_CHOOSE));


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
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countryChoose);
    }

    private BroadcastReceiver countryChoose = new BroadcastReceiver() {
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
     * @param s
     * @param removeFirstZeros*/


    @Override
    public void openOtpActivity(String s, String removeFirstZeros) {
        startActivity(new Intent(this, OTPActivity.class)
                .putExtra(Constants.country, s)
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
    public void opentcFrag() {
        int primary = Color.parseColor("#FF6E1D");
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(primary);
        CustomTabsIntent intent =builder.build();
        intent.launchUrl(this, Uri.parse(Constants.URL.privacy_policy));
    }

    @Override
    public void opencarDetailsAct() {
        startActivity(new Intent(this, CarDetailsAct.class).putExtra("from", "otp"));
        finish();
    }

    @Override
    public void onGetPhoneNum() {
        String phone= Objects.requireNonNull(activitySignupBinding.TIEPhonefield.getText()).toString();
        if (phone!=null&&!phone.isEmpty()){
            loginViewModel.phone.set(phone);
            // sharedPrefence.savevalue(SharedPrefence.PhoneNumber, CommonUtils.removeFirstZeros(loginViewModel.phone.get()));
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