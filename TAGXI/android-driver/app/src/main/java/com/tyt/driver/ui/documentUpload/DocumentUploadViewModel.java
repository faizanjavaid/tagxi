package com.tyt.driver.ui.documentUpload;

import android.view.View;

import androidx.databinding.ObservableBoolean;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.Documentdata;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class DocumentUploadViewModel extends BaseNetwork<BaseResponse, DocumentUploadNavigator> {

    @Inject
    HashMap<String, String> Map;


    SharedPrefence sharedPrefence;
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    public List<Documentdata> documentdata = new ArrayList<>();

    public ObservableBoolean enableButon = new ObservableBoolean();


    @Inject
    public DocumentUploadViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
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
        documentdata.clear();
        String documentString = CommonUtils.arrayToString((ArrayList<Object>) response.data);
        documentdata.addAll(CommonUtils.stringToArray(documentString, Documentdata[].class));
        enableButon.set(response.enable_submit_button);

        getmNavigator().loadDocuments(documentdata);
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
        return Map;
    }


    public void documentgetApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getDocuments();
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickSubmit(View v) {
        getmNavigator().onCLickSubmit();
    }
}
