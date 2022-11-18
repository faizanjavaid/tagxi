package com.tyt.client.ui.signup;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubCountryCode;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mahi in 2021.
 */

public class SignupViewModel extends BaseNetwork<BaseResponse, SignupNavigator> {

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
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);

        if (response.accessToken != null) {
            if (!TextUtils.isEmpty(response.accessToken)) {
                Log.e("AccessToken==", "token");
                sharedPrefence.savevalue(SharedPrefence.AccessToken, response.accessToken);
                getmNavigator().openHomAct();
            }
        }
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
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, CommonUtils.removeFirstZeros(phone.get()));
            sharedPrefence.savevalue(SharedPrefence.CountryCode, countryCode.get());

            Map.put(Constants.NetworkParameters.country, sharedPrefence.Getvalue(SharedPrefence.CountryCode));
            Map.put(Constants.NetworkParameters.device_token, sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
            Map.put(Constants.NetworkParameters.email, sharedPrefence.Getvalue(SharedPrefence.Email));
            Map.put(Constants.NetworkParameters.name, sharedPrefence.Getvalue(SharedPrefence.FName));
            Map.put(Constants.NetworkParameters.mobile, sharedPrefence.Getvalue(SharedPrefence.PhoneNumber));
            Map.put(Constants.NetworkParameters.login_by, "android");
            Map.put(Constants.NetworkParameters.TermsCond, "1");
            Map.put(Constants.NetworkParameters.companyKey, sharedPrefence.Getvalue(SharedPrefence.keyValue));
            UserRegisterApi(Map);
        }
    }

    private void ValidateMobile() {
        if (getmNavigator().isNetworkConnected()) {
            Log.e("clicked", "click");
            setIsLoading(true);
            Map.clear();
            Map.put(Constants.NetworkParameters.mobile, phone.get());
            validateMobile(Map);
        } else getmNavigator().showNetworkMessage();
    }

    private boolean Validation() {
        boolean fieldValidation = false;
        if (TextUtils.isEmpty(fName.get()))
            getmNavigator().showMessage("Name Required");
        else if (TextUtils.isEmpty(email.get()))
            getmNavigator().showMessage("Email Required");
        else if (TextUtils.isEmpty(phone.get()))
            getmNavigator().showMessage("Phone Required");
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
