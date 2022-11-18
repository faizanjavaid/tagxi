package com.tyt.driver.ui.login;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

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

    /*BaseView baseView;*/

    //    public ObservableField<String> email = new ObservableField<>("");
//    public ObservableField<String> password = new ObservableField<>("+");
    public ObservableField<String> countryCode = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableBoolean termscheck= new ObservableBoolean(true);
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

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
                if (validation()) {
                    Map.clear();
                    Map.put(Constants.NetworkParameters.phoneNumber,phone.get());
                    Map.put(Constants.NetworkParameters.country,countryCode.get());
                    MobileLogin(Map);
                }
            } else {
                getmNavigator().showNetworkMessage();
            }
        } else {
            getmNavigator().showMessage(translationModel.txt_accept_tc);
        }
    }

    private boolean validation() {
        boolean valid = false;
       /* if (!CommonUtils.isEmailValid(email.get()) && TextUtils.isEmpty(email.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_mail_invalid));
        else if (TextUtils.isEmpty(password.get()))
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_enter_password));*/
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

       /* if (response.accessToken != null) {
            if (!TextUtils.isEmpty(response.accessToken)) {
                sharedPrefence.savevalue(SharedPrefence.AccessToken, response.accessToken);
                getmNavigator().openHomeAct();
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
        if (e.getMessage().equalsIgnoreCase("mobile_does_not_exists")||e.getCode()==200){
            getmNavigator().openOtpActivity(false);
        }
       /* if (e.getCode() == 702) {
            getmNavigator().openOtpActivity(true);
        } else if (e.getCode() == Constants.ErrorCode.COMPANY_CREDENTIALS_NOT_VALID ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_DATE_EXPIRED ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_ACTIVE ||
                e.getCode() == Constants.ErrorCode.COMPANY_KEY_NOT_VALID) {
            if (getmNavigator() != null)
                getmNavigator().refreshCompanyKey();
        }*/
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    /**
     * Country code changed listener
     *
     * @param e {@link Editable}
     **/
   /* public void onPassswordChanged(Editable e) {
        password.set(e.toString());
    }*/

    /**
     * Text changed listener for EmailorPhone field
     *
     * @parame {@link Editable}
     **/
    /*public void onEmailChanged(Editable e) {
        email.set(e.toString());
    }*/

    public void onClickSignup(View v) {
        getmNavigator().onClickSignup();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void onClickCountryChoose(View view) {
        //Show the Instane Of Choose Country Dialog
        getmNavigator().onClickCountryChoose();
    }

    public void onPhoneChanged(Editable e) {
        phone.set(e.toString());
    }

    public void openFrag(View view) {
        getmNavigator().opentcfrag();
    }

}
