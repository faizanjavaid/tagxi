package com.tyt.driver.ui.carDetails.carNumber;

import android.text.Editable;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.CarTypeModel;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class VehNumberViewModel extends BaseNetwork<BaseResponse, VehNumNavigator> {
    private static final String TAG = "MakeTripViewModel";

    SharedPrefence sharedPrefence;
    public ObservableField<String> carNumber = new ObservableField<>("");

    List<CarTypeModel> carTypeModels = new ArrayList<>();


    public VehNumberViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
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
        if (!carNumber.get().isEmpty()) {
            sharedPrefence.savevalue(Constants.selectedCarNumber, carNumber.get());
            getmNavigator().onClickContiue();

        } else getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_enter_car_number));
    }

    public void onClickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void onNumberChanged(Editable e) {
        carNumber.set(e.toString());
    }
}
