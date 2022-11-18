package com.tyt.client.ui.otpscreen;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.exception.CustomException;
import com.tyt.client.utilz.SharedPrefence;

/**
 * Created by Mahi in 2021.
 */

public class OTPViewModel extends BaseNetwork<BaseResponse, OTPNavigator> {

    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;
    public View view;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String mVerificationId = "";
    PhoneAuthProvider.ForceResendingToken mToken;

    public ObservableBoolean shownOTP = new ObservableBoolean(false);

    public ObservableBoolean isLogin = new ObservableBoolean(true);
    public ObservableField<String> countrycode = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>("");

    @Inject
    public OTPViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                        @Named("HashMapData") HashMap<String, String> stringHashMap,
                        SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;

        Map = stringHashMap;

        System.out.println("+++" + Map.get(Constants.NetworkParameters.phonenumber));
    }

    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }

    public void onclickverfiy(View view) {
        setIsLoading(true);
        Log.e("otp---", "otp----" + getmNavigator().getOpt());

        if (getmNavigator().getOpt().length() == 6) {
            PhoneAuthCredential credential=null;
            if (mVerificationId != null) {
                credential= PhoneAuthProvider.getCredential(mVerificationId, getmNavigator().getOpt());
            }
            if (credential != null) {
                signInWithPhoneAuthCredential(credential);
            } else {
                getmNavigator().showMessage("We Couldn't Got Your Credentials");
            }
        } else {
            getmNavigator().showMessage("Please enter OTP");
        }

    }

    /**
     * Login with {@link PhoneAuthCredential}
     *
     * @param credential {@link PhoneAuthCredential}
     **/
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                        Log.e("OTP", "signInWithPhoneAuthCredential: Success");
                        if (isLogin.get()){
                            callLoginApi();
                        }else{
                            CallRegisterApi();
                        }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("signInSucess", "signInWithCredential:success");
                        //
                    } else {
                        // Sign in failed, display a message and update the UI
                        setIsLoading(false);
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            getmNavigator().showMessage(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }


    /**
     * Click listener to resend OTP
     **/
    public void onClickResend(View view) {
        if (sharedPrefence.Getvalue(SharedPrefence.showFBOTP).equalsIgnoreCase("true")) {
            getmNavigator().resendOtp();
        } else getmNavigator().showMessage("Use the otp above");
    }

    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.accessToken != null) {
            if (!TextUtils.isEmpty(response.accessToken)) {
                Log.e("AccessToken==", "token");
                sharedPrefence.savevalue(SharedPrefence.AccessToken, response.accessToken);
                getmNavigator().openHomeAct();
            }
        }
    }

    public void callLoginApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            Map.clear();
            Map.put(Constants.NetworkParameters.mobile,phone.get());
            Map.put(Constants.NetworkParameters.login_by,"android");
            Map.put(Constants.NetworkParameters.device_token,sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
            Map.put(Constants.NetworkParameters.country,countrycode.get());
            CallLoginApi(Map);
        }else getmNavigator().showNetworkMessage();
    }

    public void CallRegisterApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getmNavigator().openSignUpActivity();
        } else getmNavigator().showNetworkMessage();
    }

    /**
     * Callback for failed API calls
     *
     * @param taskId ID of the API task
     * @param e      {@link CustomException}
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);
    }

    public void onClickLogin(View v) {
        getmNavigator().onClicOpenLogin();
    }

}
