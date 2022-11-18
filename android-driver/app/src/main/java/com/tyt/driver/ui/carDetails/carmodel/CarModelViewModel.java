package com.tyt.driver.ui.carDetails.carmodel;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.CarModelData;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class CarModelViewModel extends BaseNetwork<BaseResponse, CarModelNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    public ObservableField<String> carMakeId = new ObservableField<>();

    List<CarModelData> carModelDataList = new ArrayList<>();


    public CarModelViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
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

        Log.e("Reponse---", "reponsee--" + response);
        if (response.success) {
            carModelDataList.clear();
            String carModel = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            carModelDataList.addAll(CommonUtils.stringToArray(carModel, CarModelData[].class));
            getmNavigator().loadCarMake(carModelDataList);
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


    public void carModelApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getCarModel(sharedPrefence.Getvalue(Constants.selectedMakeID));
        } else {
            setIsLoading(false);
            getmNavigator().showNetworkMessage();
        }
    }

    public void onClickBack(View v) {
        getmNavigator().onCLickBack();
    }

    public void onClickContinue(View v) {
        getmNavigator().onClickContiue();
    }
}
