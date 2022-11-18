package com.tyt.driver.ui.homeScreen.vehicleInfo;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.RefferralModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 11/13/17.
 */

public class VehicleInfoViewModel extends BaseNetwork<BaseResponse, VehicleInfoNavigator> {
    private static final String TAG = "GetRefferalViewModel";

    public ObservableField<String> CarMake = new ObservableField<>();
    public ObservableField<String> carModel = new ObservableField<>();
    public ObservableField<String> carColor = new ObservableField<>();
    public ObservableField<String> carNumber = new ObservableField<>();
    public ObservableField<String> carType = new ObservableField<>();

    public VehicleInfoViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);
            if (model != null) {
                if (model.getCarModelName() != null)
                    carModel.set(model.getCarModelName());

                if (model.getCarMakeName() != null)
                    CarMake.set(model.getCarMakeName());
                if (model.getCarColor() != null)
                   carColor.set(model.getCarColor());
                if (model.getCarNumber() != null)
                    carNumber.set(model.getCarNumber());
                if (model.getVehicleTypeName() != null)
                carType.set(model.getVehicleTypeName());
            }
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


    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }


    public void getProfileApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            GetUserProfile();
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickEdit(View v) {
        getmNavigator().onClickEdit();
    }
}

