package com.tyt.driver.ui.carDetails.carmake;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.CarMakeModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class CarMakeViewModel extends BaseNetwork<BaseResponse, CarMakeNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    List<CarMakeModel> carMakeModels = new ArrayList<>();
    public ObservableField<String> carMakeChoosen = new ObservableField<>();


    public CarMakeViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
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
            carMakeModels.clear();
            String carModel = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            carMakeModels.addAll(CommonUtils.stringToArray(carModel, CarMakeModel[].class));
            getmNavigator().loadCarMake(carMakeModels);
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


    public void carMakeApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getCarMake();
        } else {
            setIsLoading(false);
            getmNavigator().showNetworkMessage();
        }
    }

    public void onClickContinue(View v) {
        getmNavigator().onClickContiue();
    }
}
