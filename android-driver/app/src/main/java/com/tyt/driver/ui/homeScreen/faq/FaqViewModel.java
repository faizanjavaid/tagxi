package com.tyt.driver.ui.homeScreen.faq;

import android.view.View;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.Faqmodel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class FaqViewModel extends BaseNetwork<BaseResponse, FaqNavigator> {
    private static final String TAG = "FaqViewModel";
    List<Faqmodel> faqmodels = new ArrayList<>();

    public FaqViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            String faqdata = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            faqmodels.addAll(CommonUtils.stringToArray(faqdata, Faqmodel[].class));
            getmNavigator().loadFaq(faqmodels);
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void faqApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            FAQLIST(sharedPrefence.Getvalue(SharedPrefence.CURRLAT), sharedPrefence.Getvalue(SharedPrefence.CURRLNG));
        } else
            getmNavigator().showNetworkMessage();
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBAck();
    }
}
