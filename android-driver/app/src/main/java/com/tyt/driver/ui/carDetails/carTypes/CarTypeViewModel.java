package com.tyt.driver.ui.carDetails.carTypes;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.CarTypeModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class CarTypeViewModel extends BaseNetwork<BaseResponse, CarTypeNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    public ObservableField<String> carMakeId = new ObservableField<>();

    List<CarTypeModel> carTypeModels = new ArrayList<>();


    public CarTypeViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
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
            carTypeModels.clear();
            String carModel = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            carTypeModels.addAll(CommonUtils.stringToArray(carModel, CarTypeModel[].class));
            getmNavigator().loadTypes(carTypeModels);
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


    public void TypesApi(String areaId) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getServiceType(areaId);
        } else {
            setIsLoading(false);
            getmNavigator().showNetworkMessage();
        }
    }
}
