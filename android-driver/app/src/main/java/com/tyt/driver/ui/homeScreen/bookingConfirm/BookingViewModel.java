package com.tyt.driver.ui.homeScreen.bookingConfirm;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 11/13/17.
 */

public class BookingViewModel extends BaseNetwork<BaseResponse, BookingNavigator> {
    private static final String TAG = "BookingViewModel";
    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<LatLng> pickupLatLng = new ObservableField<>();
    public ObservableField<LatLng> dropLatLng = new ObservableField<>();
    public ObservableField<String> carModel = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> tax = new ObservableField<>();
    public ObservableField<String> farePrice = new ObservableField<>();
    public ObservableField<String> payType = new ObservableField<>("Choose");

    public ObservableBoolean cardAvail = new ObservableBoolean(false);
    public ObservableBoolean cashAvail = new ObservableBoolean(false);
    public ObservableBoolean walletAvail = new ObservableBoolean(false);

    HashMap<String, String> map = new HashMap<>();
    public EtaModel etaDataModel;

    public BookingViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            getmNavigator().openWaitingDialog();
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

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    public void onClickBackImg(View v) {
        getmNavigator().onClickBack();
    }

    public void onClickBookNow(View v) {
        if (getmNavigator().isNetworkConnected()) {
            getmNavigator().openWaitingDialog();
           /* setIsLoading(true);
            map.clear();
            map.put(Constants.NetworkParameters.pLat, "" + pickupLatLng.get().latitude);
            map.put(Constants.NetworkParameters.pLng, "" + pickupLatLng.get().longitude);
            map.put(Constants.NetworkParameters.dLat, "" + dropLatLng.get().latitude);
            map.put(Constants.NetworkParameters.dLng, "" + dropLatLng.get().longitude);
            map.put(Constants.NetworkParameters.vType, etaDataModel.getZoneTypeId());
            map.put(Constants.NetworkParameters.rType, "1");
            map.put(Constants.NetworkParameters.PayOpt, "1");
            map.put(Constants.NetworkParameters.pAddress, pickupAddress.get());
            map.put(Constants.NetworkParameters.dAddress, dropAddress.get());
            CreateReqApicall(map);*/
        } else getmNavigator().showNetworkMessage();

    }


    public void onClickChoose(View v) {
        getmNavigator().onClickChoose();
    }

}
