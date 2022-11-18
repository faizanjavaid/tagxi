package com.tyt.driver.ui.login;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tyt.driver.BR;
import com.tyt.driver.BuildConfig;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ActLoginBinding;
import com.tyt.driver.databinding.ActivitySignupBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.country.Countrylistdialog;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.ui.otpscreen.OTPActivity;
import com.tyt.driver.ui.signup.SignupActivity;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * Created by naveen on 8/24/17.
 */

public class LoginAct extends BaseActivity<ActLoginBinding, LoginViewModel> implements LoginNavigator, HasAndroidInjector {

    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    LoginViewModel loginViewModel;
    ActLoginBinding actLoginBinding;
    EditText edt_text;
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    HintRequest hintRequest;

    private static DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("call_FB_OTP");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actLoginBinding = getViewDataBinding();
        loginViewModel.setNavigator(this);

        loginViewModel.countryCode.set(Constants.defaultCountryCode);

        hintRequest();

       /* if (BuildConfig.DEBUG) {
            actLoginBinding.txtCc.setText("+91");
            actLoginBinding.etPhone.setText("9942843430");
        }*/
    }



    private BroadcastReceiver countryChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(Constants.INTENT_COUNTRY_CHOOSE))
                    loginViewModel.countryCode.set(intent.getStringExtra(Constants.countryCode));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if (sharedPrefence.Getvalue(SharedPrefence.DEFAULT_COUNTRY) != null)
//            Constants.COUNTRY_CODE = sharedPrefence.Getvalue(SharedPrefence.DEFAULT_COUNTRY);
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String jsonStr = String.valueOf(snapshot.getValue());
                sharedPrefence.savevalue(SharedPrefence.showFBOTP, jsonStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showMessage(error.getMessage());
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

    @Override
    public void setCountryCode(String countryCode) {
    }

    /**
     * Opens {@link OTPActivity} when called
     *
     * @param b boolean parameter
     * @param s String parameter
     **/
    @Override
    public void openOtpPage(boolean b, String s) {
        startActivity(new Intent(this, OTPActivity.class).putExtra(Constants.isLogin, b).putExtra(Constants.PhonewithCountry, s));
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void onClickSignup() {
        finish();
        startActivity(new Intent(this, SignupActivity.class));
    }

    @Override
    public void onClickBack() {
        closeAct();
    }

    @Override
    public void openHomeAct() {
        finish();
        startActivity(new Intent(this, HomeAct.class));
    }

    @Override
    public boolean getcheckedvalue() {
        loginViewModel.termscheck.set(actLoginBinding.termsCheck.isChecked());
        return actLoginBinding.termsCheck.isChecked();
    }

    @Override
    public void opentcfrag() {
        int primary = Color.parseColor("#FF6E1D");
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(primary);
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(Constants.URL.privacy_policy));
    }

    @Override
    public void onClickCountryChoose() {
        Countrylistdialog newInstance = Countrylistdialog.newInstance();
        newInstance.show(this.getSupportFragmentManager());
    }

    @Override
    public LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    /**
     * Opens {@link OTPActivity} when called
     *
     * @param isLogin boolean parameter
     **/
    @Override
    public void openOtpActivity(boolean isLogin) {
        startActivity(new Intent(this, OTPActivity.class)
                .putExtra(Constants.country, loginViewModel.countryCode.get())
                .putExtra(Constants.phone,loginViewModel.phone.get())
                .putExtra(Constants.isLogin,isLogin)
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeAct();
    }

    private void closeAct() {
        //startActivity(new Intent(this, LoginOrSignAct.class));
        //finish();
        super.onBackPressed();
    }

    /**
     * Called to hide soft keyboard
     **/
    @Override
    public void HideKeyBoard() {
        hideKeyboard();
    }


    String CountryCode, countryShort;

    /**
     * Get currently selected country code
     **/
    @Override
    public String getCountryCode() {
        return CountryCode != null ? (CountryCode.replaceAll(" ", "")) : "";
    }

    /**
     * Returns short country code
     **/
    @Override
    public String getCountryShortName() {
        return countryShort;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private void hintRequest() {
        try {
            hintRequest = new HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .build();

            GoogleApiClient apiClient = new GoogleApiClient.Builder(this).addApi(Auth.CREDENTIALS_API)
                    .build();

            PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                    apiClient, hintRequest);

            startIntentSenderForResult(intent.getIntentSender(),
                    Constants.RESOLVE_HINT, null, 0, 0, 0);
            //startIntentSenderForResult(intent.getIntentSender(),Constants.RESOLVE_HINT,null,0,0,0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public View getBaseView(){
        return actLoginBinding.bglog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    if (credential != null) {
                        String picked_phone_no = credential.getId();
                        String no = picked_phone_no.substring(3);
                        loginViewModel.phone.set(no);
                        if (loginViewModel.phone != null) {
                            loginViewModel.onClickLogin(getBaseView());
                        }
                    }
                }
            }
        }

    }
}