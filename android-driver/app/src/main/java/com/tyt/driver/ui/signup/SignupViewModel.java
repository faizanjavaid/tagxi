package com.tyt.driver.ui.signup;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.User;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class SignupViewModel extends BaseNetwork<User, SignupNavigator> {

    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;

    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> fName = new ObservableField<>("");
    public ObservableField<String> lName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> confPassword = new ObservableField<>("");
    public ObservableField<String> countryCode = new ObservableField<>("");


    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    @Inject
    public SignupViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                           @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                           SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
    }

    public void onClickLogin(View view) {
        getmNavigator().onClickOpenLogin();
    }

    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, User response) {
        setIsLoading(false);

       /* if (response.accessToken != null) {
            if (!TextUtils.isEmpty(response.accessToken)) {
                Log.e("AccessToken==", "token");
                sharedPrefence.savevalue(SharedPrefence.AccessToken, response.accessToken);
                getmNavigator().opencarDetailsAct();
            }
        }*/
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

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }

    public void openFrag(View v){
        getmNavigator().opentcFrag();
    }

    public void onFirstNameChanged(Editable e) {
        fName.set(e.toString());
    }

    public void onLatnameChanged(Editable e) {
        lName.set(e.toString());
    }

    public void onPassswordChanged(Editable e) {
        password.set(e.toString());
    }

    public void onConfirmPassswordChanged(Editable e) {
        confPassword.set(e.toString());
    }

    public void onEmailChnaged(Editable e) {
        email.set(e.toString());
    }

    public void onPhoneChnaged(Editable e) {
        phone.set(e.toString());
    }

    public void onClickSignup(View v) {
        getmNavigator().onGetPhoneNum();
        if (Validation()) {
            sharedPrefence.savevalue(SharedPrefence.FName, fName.get());
            sharedPrefence.savevalue(SharedPrefence.LName, lName.get());
            sharedPrefence.savevalue(SharedPrefence.Email, email.get());
            sharedPrefence.savevalue(SharedPrefence.Password, password.get());
            sharedPrefence.savevalue(SharedPrefence.ConfirmPassword, confPassword.get());
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, phone.get());
            sharedPrefence.savevalue(SharedPrefence.CountryCode, countryCode.get());
            getmNavigator().opencarDetailsAct();
            //RegisterUserAPI();
        }


       /* if (getmNavigator().isNetworkConnected()) {
            Map.clear();
            Map.put(Constants.NetworkParameters.name, fName.get());
            Map.put(Constants.NetworkParameters.email, email.get());
            Map.put(Constants.NetworkParameters.password, password.get());
            Map.put(Constants.NetworkParameters.login_by, "android");
            Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
            Map.put(Constants.NetworkParameters.UUID, fName.get());
        } else getmNavigator().showNetworkMessage();*/
    }

    private void RegisterUserAPI() {
        Map.put(Constants.NetworkParameters.country, sharedPrefence.Getvalue(SharedPrefence.CountryCode));
        Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
        Map.put(Constants.NetworkParameters.email, sharedPrefence.Getvalue(SharedPrefence.Email));
        Map.put(Constants.NetworkParameters.name, sharedPrefence.Getvalue(SharedPrefence.FName));
        Map.put(Constants.NetworkParameters.password, sharedPrefence.Getvalue(SharedPrefence.Password));
        Map.put(Constants.NetworkParameters.confpwd, sharedPrefence.Getvalue(SharedPrefence.ConfirmPassword));
        Map.put(Constants.NetworkParameters.phoneNumber, sharedPrefence.Getvalue(SharedPrefence.PhoneNumber));
        Map.put(Constants.NetworkParameters.login_by, "android");
        Map.put(Constants.NetworkParameters.TermsCond, "1");
        Map.put(Constants.NetworkParameters.companyKey, sharedPrefence.Getvalue(SharedPrefence.keyValue));
    }

    private void ValidateMobile() {
        if (getmNavigator().isNetworkConnected()) {
            Log.e("clicked", "click");
            setIsLoading(true);
            Map.clear();
            Map.put(Constants.NetworkParameters.phoneNumber, phone.get());
            validateMobile(Map);
        } else getmNavigator().showNetworkMessage();
    }

    private boolean Validation() {
        boolean fieldValidation = false;
        if (TextUtils.isEmpty(fName.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_name_required));
       /* else if (TextUtils.isEmpty(lName.get()))
            getmNavigator().showMessage("LastName Required");*/
        else if (TextUtils.isEmpty(email.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_mail_required));
        else if (!CommonUtils.isEmailValid(email.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_mail_invalid));
/*        else if (TextUtils.isEmpty(password.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pass_required));
        else if (TextUtils.isEmpty(confPassword.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pass_required));
        else if (!(Objects.equals(password.get(), confPassword.get()))) {
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pass_mismatch));}*/
         else if (TextUtils.isEmpty(phone.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_phone_required));
        else fieldValidation = true;

        return fieldValidation;
    }

    public void onClickBackImg(View v) {
        getmNavigator().onClickBack();
    }

    public void onClickCountryChoose(View v) {
        getmNavigator().openCountryDialog();
    }

}
