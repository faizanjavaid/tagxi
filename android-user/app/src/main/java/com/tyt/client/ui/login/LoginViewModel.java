package com.tyt.client.ui.login;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.R;
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
 * Created by root on 10/7/17.
 */

public class LoginViewModel extends BaseNetwork<BaseResponse, LoginNavigator> {

    @Inject
    HashMap<String, String> Map;
    SharedPrefence sharedPrefence;

    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    public ObservableBoolean termscheck = new ObservableBoolean(true);

    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> countryCode = new ObservableField<>("");

    @Inject
    public LoginViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                          @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                          SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
    }

    public void onClickLogin(View view) {
        if (getmNavigator().getcheckedvalue()) {
            if (getmNavigator().isNetworkConnected()) {
                if (phone.get() != null && countryCode.get() != null) {
                    if (CommonUtils.IsEmpty(phone.get().trim().replace("-", "").replace("_", "").replace(" ", "")))
                        getmNavigator().showSnackBar(getmNavigator().getBaseView(), getmNavigator().getBaseAct().getTranslatedString(R.string.txt_phone_required));
                    else if (CommonUtils.IsEmpty(countryCode.get().trim().replace(" ", "").replace("+", ""))) {
                        getmNavigator().showSnackBar(getmNavigator().getBaseView(), getmNavigator().getBaseAct().getTranslatedString(R.string.txt_cc_invalid));
                    } else {
                        if (validation()) {
                            Map.clear();
                            Map.put(Constants.NetworkParameters.mobile, phone.get());
                            Map.put(Constants.NetworkParameters.country,countryCode.get());
                            MobileLogin(Map);
                        } else getmNavigator().showNetworkMessage();
                    }
                }
            }
        } else getmNavigator().showMessage(translationModel.txt_accept_tc);
    }

    private boolean validation() {
        boolean valid = false;
        if (CommonUtils.IsEmpty(phone.get()) || TextUtils.isEmpty(phone.get())) {
            getmNavigator().showMessage("Phone Invalid");
        } else {
            valid = true;
        }
        return valid;
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
        if (response.success) {
            if (response.message.equalsIgnoreCase("mobile_exists")){
                getmNavigator().openOtpActivity(true);
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
        if (e.getMessage() != null && (e.getMessage().equalsIgnoreCase("mobile_does_not_exists") || e.getCode() == 200)) {
            getmNavigator().openOtpActivity(false);
        }
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {

        return null;
    }

    public void onClickSignup(View v) {
        getmNavigator().onClickSignup();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void openFrag(View v) {
        getmNavigator().opentcfrag();
    }

    public void onClickCountryChoose(View v) {
        getmNavigator().onClickCountryChoose();
    }

    public void onPhoneChanged(Editable e) {
        phone.set(e.toString());
       /* if (Objects.requireNonNull(phone.get()).length() == 10) {
            onClickLogin(getmNavigator().getBaseView());
        }*/
    }

}
