package com.tyt.driver.ui.otpscreen;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import com.tyt.driver.R;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

/**
 * Created by root on 10/9/17.
 */

public class OTPViewModel extends BaseNetwork<BaseResponse, OTPNavigator> {

    @Inject
    HashMap<String, String> Map;
    public ObservableBoolean shownOTP = new ObservableBoolean(false);

    public ObservableBoolean islogin = new ObservableBoolean(false);
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> countrycode = new ObservableField<>("");

    SharedPrefence sharedPrefence;
    public View view;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String mVerificationId = "";
    PhoneAuthProvider.ForceResendingToken mToken;
    ObservableField<String> UUIDValue = new ObservableField<>();

    public ObservableBoolean resendClicked = new ObservableBoolean(false);
    public ObservableField<String> termsCondtClicked = new ObservableField<>();


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
      /*  if (sharedPrefence.Getvalue(SharedPrefence.showFBOTP).equalsIgnoreCase("true")) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, getmNavigator().getOpt());
            signInWithPhoneAuthCredential(credential);
        } else
            CallRegisterApi();*/
    }

    /**
     * Login with {@link PhoneAuthCredential}
     *
     * @param credential {@link PhoneAuthCredential}
     **/
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInSucess", "signInWithCredential:success");

                            if (islogin.get()) {
                                callLoginApi();
                            } else {
                                callRegisterFlow();
                            }

                            //
                        } else {
                            // Sign in failed, display a message and update the UI
                            setIsLoading(false);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                getmNavigator().showMessage(task.getException().getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    /**
     * Calls login API
     **/
    public void initializeSucessNavigation() {

    }

    /**
     * Click listener to resend OTP
     **/
    public void onClickResend(View view) {
        if (sharedPrefence.Getvalue(SharedPrefence.showFBOTP).equalsIgnoreCase("true")) {
            getmNavigator().resendOtp();
        } else
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_use_above_otp));
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

    private void CallRegisterApi() {
        setIsLoading(true);
        getmNavigator().openCarDetailsAct();
       /* Map.clear();
        Map.put(Constants.NetworkParameters.country, sharedPrefence.Getvalue(SharedPrefence.CountryCode));
        Map.put(Constants.NetworkParameters.UUId, sharedPrefence.Getvalue(Constants.UUID));
        Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
        Map.put(Constants.NetworkParameters.email, sharedPrefence.Getvalue(SharedPrefence.Email));
        Map.put(Constants.NetworkParameters.name, sharedPrefence.Getvalue(SharedPrefence.FName));
        Map.put(Constants.NetworkParameters.password, sharedPrefence.Getvalue(SharedPrefence.Password));
        Map.put(Constants.NetworkParameters.confpwd, sharedPrefence.Getvalue(SharedPrefence.ConfirmPassword));
        Map.put(Constants.NetworkParameters.phoneNumber, sharedPrefence.Getvalue(SharedPrefence.PhoneNumber));
        Map.put(Constants.NetworkParameters.login_by, "android");
        Map.put(Constants.NetworkParameters.TermsCond, "1");
        UserRegisterApi(Map);*/
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
      /*  if (e.getCode() == Constants.ErrorCode.COMPANY_CREDENTIALS_NOT_VALID ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_DATE_EXPIRED ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_ACTIVE ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_VALID) {
            getmNavigator().refreshCompanyKey();
        }*/

    }

    public void onClickLogin(View v) {
        getmNavigator().onClicOpenLogin();
    }

    public void callLoginApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            Map.clear();
            Map.put(Constants.NetworkParameters.phoneNumber, phone.get());
            Map.put(Constants.NetworkParameters.login_by, "android");
            Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
            Map.put(Constants.NetworkParameters.country,countrycode.get());
            SendLoginApi(Map);
        } else getmNavigator().showNetworkMessage();
    }

    public void callRegisterFlow() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getmNavigator().openSignUpActivity();
        } else getmNavigator().showNetworkMessage();

    }
}
