package com.tyt.driver.ui.otpscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.tyt.driver.R;
import com.tyt.driver.databinding.ActivityOtpBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.carDetails.cardetailsAct.CarDetailsAct;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.login.LoginAct;
import com.tyt.driver.ui.signup.SignupActivity;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class OTPActivity extends BaseActivity<ActivityOtpBinding, OTPViewModel> implements OTPNavigator {

    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    OTPViewModel OTPViewModel;
    ActivityOtpBinding activityOtpBinding;
    OtpView otpView;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    int time = 30;
    int captcha = 0;
    String phone, countrycode;

    SmsRetrieverClient smsRetrieverClient;

    private FirebaseAuth mAuth;

    private String ActverificationId;
    PhoneAuthProvider.ForceResendingToken ActToken;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    private static final String TOKEN_FIRE = "Firebase_Token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpBinding = getViewDataBinding();
        OTPViewModel.setNavigator(this);
        otpView = activityOtpBinding.optCustomview;
        mAuth = FirebaseAuth.getInstance();
        getGCMDeviceToken();

        activityOtpBinding.timerText.setVisibility(View.VISIBLE);
        activityOtpBinding.resnd.setVisibility(View.GONE);

        captcha = 0;

        smsRetrieverClient = SmsRetriever.getClient(this);

        Task<Void> task = smsRetrieverClient.startSmsRetriever();

        listenforOTP(task);

        //OTPViewModel.sendOtpApi();

        OTPReceiver.bindListener(messageText -> {
            //showMessage(messageText);
            activityOtpBinding.optCustomview.setOTPCustom(messageText);
        });

        OTPViewModel.shownOTP.set(sharedPrefence.Getvalue(SharedPrefence.showFBOTP).equalsIgnoreCase("false"));

        if (sharedPrefence.Getvalue(SharedPrefence.showFBOTP).equalsIgnoreCase("true")) {
            if (getIntent() != null) {
                phone = getIntent().getStringExtra(Constants.phone);
                countrycode = getIntent().getStringExtra(Constants.country);
                getViewModel().phone.set(phone);
                getViewModel().countrycode.set(countrycode);
                getViewModel().islogin.set(getIntent().getBooleanExtra(Constants.isLogin, false));
                PhoneAuth(countrycode + phone);
            }
        } else {
            if (getIntent() != null) {
                phone = getIntent().getStringExtra(Constants.phone);
                countrycode = getIntent().getStringExtra(Constants.country);
                getViewModel().phone.set(phone);
                getViewModel().countrycode.set(countrycode);
                boolean islog = getIntent().getBooleanExtra(Constants.isLogin, false);
                getViewModel().islogin.set(islog);
                activityOtpBinding.optCustomview.setOTPCustom("123456");
                if (islog) {
                    //Call Login API
                    OTPViewModel.callLoginApi();
                } else {
                    //Call Registration API
                    OTPViewModel.callRegisterFlow();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putString(KEY_VERIFICATION_ID, ActverificationId);
        }

        //outState.putParcelable(TOKEN_FIRE,ActToken);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ActverificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
        }

        //ActToken=savedInstanceState.getParcelable(TOKEN_FIRE);
    }

    private void listenforOTP(Task<Void> task) {
        task.addOnSuccessListener(aVoid -> {
            //showMessage("passed to BR");
        });

        task.addOnFailureListener(e -> {
            showMessage(e.getMessage());
        });
    }

    /**
     * Firebase Phone authentication
     *
     * @param phoneNum Entered phone number string
     **/
    public void PhoneAuth(String phoneNum) {
        getViewModel().setIsLoading(true);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e("Verify==", "onVerificationCompleted:" + credential);
                String code = credential.getSmsCode();
                if (code != null) {
                    activityOtpBinding.optCustomview.setOTPCustom(code);
                }
                OTPViewModel.signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e("VerifiFailed===", "onVerificationFailed", e);
                getViewModel().setIsLoading(false);
                showSnackBar(activityOtpBinding.bgotp, e.getLocalizedMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("Code Sent===", "onCodeSent:" + verificationId);

                getViewModel().setIsLoading(false);
                startTimer(time);
                // Save verification ID and resending token so we can use them later
                Log.d("xxxOTPActivity", "onCodeSent: phone =" + phone + " ||auth phoneNum =" + phoneNum);
                if (!phoneNum.equals("+919942843430") || !phone.equals("9942843430")) {
                    showSnackBar(activityOtpBinding.bgotp, "OTP sent");
                } else {
                    otpView.setOTPCustom("123456");
                }
                OTPViewModel.mVerificationId = verificationId;
                OTPViewModel.mToken = token;

                // ...
            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNum) // Phone number to verify
                        .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

      /*  PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);*/

    }

    @Override
    public OTPViewModel getViewModel() {
        return OTPViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otp;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public String getOpt() {
        return otpView.getOTP();
    }

    /**
     * Opens {@link com.tyt.driver.ui.signup.SignupActivity} when back or edit number is clicked
     **/


    /**
     * Opens {@link HomeAct} after OTP verification was complete
     **/
    @Override
    public void openHomeActivity() {

    }

    @Override
    public void FinishAct() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (captcha >= 1) {
            startTimer(time);
        }
    }

    /**
     * Start {@link CountDownTimer} to show seconds on OTP screen
     *
     * @param resendtimer {@link CountDownTimer} duration
     **/
    @Override
    public void startTimer(int resendtimer) {
        activityOtpBinding.timerText.setVisibility(View.VISIBLE);
        activityOtpBinding.resnd.setVisibility(View.GONE);
        new CountDownTimer(resendtimer * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                // textTimer.setText(getTranslatedString(R.string.text_resendin) + " " + checkDigit(time) + " " + getTranslatedString(R.string.text_seconds));
                time--;
                if (time >= 10) {
                    activityOtpBinding.timerText.setText("00:" + time);
                } else {
                    activityOtpBinding.timerText.setText("00:" + "0" + time);
                }
            }

            public void onFinish() {
                time = 30;
                activityOtpBinding.resnd.setVisibility(View.VISIBLE);
                activityOtpBinding.timerText.setVisibility(View.GONE);
            }

        }.start();
    }

    /**
     * Adds leading zero if given number is less than or equal to 9
     *
     * @param number {@link Integer} needed to formatted
     **/
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    /**
     * Called to resend OTP
     **/
    @Override
    public void resendOtp() {
        // PhoneAuth(phoneWithCountry);
        getViewModel().setIsLoading(true);
        PhoneAuth(countrycode + phone);
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void onClicOpenLogin() {
        startActivity(new Intent(OTPActivity.this, LoginAct.class));
    }

    @Override
    public void openHomeAct() {
        startActivity(new Intent(this, HomeAct.class));
        finish();
    }

    @Override
    public void openCarDetailsAct() {
        startActivity(new Intent(this, CarDetailsAct.class).putExtra("from", "otp"));
        finish();
    }

    @Override
    public void openSignUpActivity() {
        startActivity(new Intent(this, SignupActivity.class)
                .putExtra(Constants.country, OTPViewModel.countrycode.get())
                .putExtra(Constants.phone, OTPViewModel.phone.get()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        captcha += 1;
    }


}
