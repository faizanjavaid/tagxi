package com.tyt.client.ui.splash;

import androidx.databinding.ObservableBoolean;

import com.google.gson.Gson;
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

public class SplashViewModel extends BaseNetwork<BaseResponse, SplashNavigator> {
    SharedPrefence sharedPrefence;
    Gson gson;
    public ObservableBoolean isLoaad = new ObservableBoolean(false);

    @Inject
    public SplashViewModel(@Named(Constants.ourApp) GitHubService gitHubService
            , SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
    }

    /**
     * @param taskId
     * @param response holds the response getting the language.
     */
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        isLoaad.set(false);
    }

    /**
     * @param taskId
     * @param e      holds the exception messages from Api.
     */
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        isLoaad.set(false);
        getmNavigator().showMessage(e.getMessage());
        getmNavigator().startRequestingPermissions();
    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

}
