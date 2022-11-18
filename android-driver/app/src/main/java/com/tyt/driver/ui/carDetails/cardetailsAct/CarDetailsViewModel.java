package com.tyt.driver.ui.carDetails.cardetailsAct;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class CarDetailsViewModel extends BaseNetwork<BaseResponse, CarDetailsNavigator> {

    @Inject
    HashMap<String, String> Map;


    SharedPrefence sharedPrefence;

    /*BaseView baseView;*/

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("+");
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;


    @Inject
    public CarDetailsViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
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
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {

        return null;
    }


}
