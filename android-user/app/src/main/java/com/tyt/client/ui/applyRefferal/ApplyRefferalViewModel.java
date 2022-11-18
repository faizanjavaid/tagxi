package com.tyt.client.ui.applyRefferal;

import android.text.Editable;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubCountryCode;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mahi in 2021.
 */

public class ApplyRefferalViewModel extends BaseNetwork<BaseResponse, ApplyRefferalNavigator> {

    @Inject
    HashMap<String, String> Map;


    SharedPrefence sharedPrefence;

    /*BaseView baseView;*/

    public ObservableField<String> RefferalCode = new ObservableField<>("");
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;


    @Inject
    public ApplyRefferalViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
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
        setIsLoading(false);
        if (response.success) {
            getmNavigator().openHomeAct();
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
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }

    public void onClickSkip(View v) {
        getmNavigator().openHomeAct();
    }

    public void onRefferalChanged(Editable e) {
        RefferalCode.set(e.toString());
    }

    public void onClickApply(View v) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            Map.clear();
            Map.put(Constants.NetworkParameters.RefferalCode, RefferalCode.get());
            RefferalCode(Map);
        } else getmNavigator().showNetworkMessage();
    }

}
