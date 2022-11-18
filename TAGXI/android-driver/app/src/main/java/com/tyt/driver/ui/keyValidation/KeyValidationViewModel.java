package com.tyt.driver.ui.keyValidation;

import android.text.Editable;
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

public class KeyValidationViewModel extends BaseNetwork<BaseResponse, KeyValidationNavigator> {

    @Inject
    HashMap<String, String> Map;
    SharedPrefence sharedPrefence;

    /*BaseView baseView;*/

    public ObservableField<String> key = new ObservableField<>("");
    public ObservableField<String> submit = new ObservableField<>("Submit");
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    public ObservableField<String> slogan = new ObservableField<>("We Dont'Make Apps..\n    We Make Dreams into reality");

    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> countryCode = new ObservableField<>("");

    public ObservableBoolean showcntdgloader = new ObservableBoolean(false);
    public ObservableBoolean valid_demo_key = new ObservableBoolean(false);

    @Inject
    public KeyValidationViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                  @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                                  SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
    }

    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        if (response.success) {
            if (response.message.equalsIgnoreCase("demo_requested_successfully")) {
                showcntdgloader.set(false);
                getmNavigator().showMessage("We will Contact Soon ASAP");
                getmNavigator().closecontactpage();
            } else {
                setIsLoading(false);
                sharedPrefence.savevalue(SharedPrefence.keyValue, key.get());
                getmNavigator().openPages();
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
        return new HashMap<>();
    }

    public void onKeyChanged(Editable e) {
        key.set(e.toString());
    }

    public void onClickKeySubmit(View v) {
        if (getmNavigator().isNetworkConnected()) {
            Map.clear();
            if (!key.get().isEmpty()) {
                setIsLoading(true);
                Map.put(Constants.NetworkParameters.companyKey, key.get());
                CommonUtils.HideKeyboard(v.getContext(),v);
                CompanyKey(Map);
            } else getmNavigator().showMessage("Please enter key");

        } else getmNavigator().showNetworkMessage();
    }

    public void senddatatocontact(HashMap<String, String> clientdatamap) {
        if (getmNavigator().isNetworkConnected()) {
            showcntdgloader.set(true);
            ContactUS(clientdatamap);
        } else getmNavigator().showNetworkMessage();
    }

    public void onclickContactUs(View v) {
        getmNavigator().opencontactdialog();
    }

    public void onClickCountryChoose(View v) {
        getmNavigator().onClickCountryChoose();
    }

    public void onCntPhoneChanged(Editable e) {
        phone.set(e.toString());
    }

    public void onclickVisitUs(View v) {
        getmNavigator().openwebsite();
    }

}
