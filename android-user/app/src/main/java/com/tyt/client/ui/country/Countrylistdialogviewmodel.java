package com.tyt.client.ui.country;


import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * A viewmodel which holds the business logic events of country choose dialog
 */

public class Countrylistdialogviewmodel extends BaseNetwork<BaseResponse, Countrylistnavigator> {

    HashMap<String, String> hashMap = new HashMap<>();

    public Countrylistdialogviewmodel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    @Override
    public HashMap<String, String> getMap() {
        return hashMap;
    }

    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        Log.e("response==", "response==" + new Gson().toJson(response.data));
        getmNavigator().countryResponse(response.data);
    }

    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
    }

    public void getCountryListApi() {
        setIsLoading(true);
        getCountryList();
    }

    public void cancelClicked(View v) {
        getmNavigator().dismissDialg();
    }
}