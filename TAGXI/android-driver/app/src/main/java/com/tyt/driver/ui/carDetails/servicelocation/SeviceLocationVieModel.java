package com.tyt.driver.ui.carDetails.servicelocation;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.ServiceLocationModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class SeviceLocationVieModel extends BaseNetwork<BaseResponse, ServiceLocationNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    public ObservableField<String> carMakeId = new ObservableField<>();

    List<ServiceLocationModel> serviceLocationModels = new ArrayList<>();


    public SeviceLocationVieModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            serviceLocationModels.clear();
            String carModel = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            serviceLocationModels.addAll(CommonUtils.stringToArray(carModel, ServiceLocationModel[].class));
            getmNavigator().loadServiceLocation(serviceLocationModels);
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);
    }

    public void onClickContinue(View v) {
        getmNavigator().onClickContiue();
    }

    public void serviceLocApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            serviceLocApiCall(sharedPrefence.Getvalue(SharedPrefence.keyValue));
        } else {
            setIsLoading(false);
            getmNavigator().showNetworkMessage();
        }
    }
}
