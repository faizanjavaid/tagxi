package com.tyt.driver.ui.homeScreen.makeTrip;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class MakeTripViewModel extends BaseNetwork<BaseResponse, MakeTripNavigator> implements SocketHelper.SocketListener {
    private static final String TAG = "MakeTripViewModel";
    private static Context context;
    public boolean isScreenAvailable = true;
    private GoogleApiClient mGoogleApiClient;

    SharedPrefence sharedPrefence;

    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<LatLng> pickupLatLng = new ObservableField<>();
    public ObservableField<LatLng> dropLatLng = new ObservableField<>();


    public ObservableField<String> carModel = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> ETA = new ObservableField<>();
    public ObservableBoolean onClickTypes = new ObservableBoolean(false);
    public ObservableBoolean ETAClicked = new ObservableBoolean(false);
    public ObservableBoolean noItem = new ObservableBoolean(false);

    HashMap<String, String> map = new HashMap<>();

    List<Type> typeList = new ArrayList<>();

    public MakeTripViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            if (ETAClicked.get()) {
                ETAClicked.set(false);
                EtaModel data = CommonUtils.getSingleObject(new Gson().toJson(response.data), EtaModel.class);
                Log.e("modelYime==", "time==" + data.getDistance());
                getmNavigator().loadEta(data);
            } else {
                SocketHelper.init(sharedPrefence, this, TAG, false);
//                if (pickupLatLng.get() != null)
//                    SocketHelper.GetDriversList(pickupLatLng.get());
                String dataArray = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                typeList.addAll(CommonUtils.stringToArray(dataArray, Type[].class));
                getmNavigator().loadTypes(typeList);
            }

        }

    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);
        ETAClicked.set(false);
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void OnConnect() {

    }

    @Override
    public void OnDisconnect() {

    }

    @Override
    public void OnConnectError() {

    }

    @Override
    public void onCreateRequest(String s) {
        Log.d("xxxMakeTripModel", "onCreateRequest: ");
    }

    @Override
    public void RequestHandler(String toString) {

    }

    @Override
    public void updateTripDistance(double v) {

    }

    @Override
    public void ApprovalStatus(String toString) {

    }

    @Override
    public void ReceivedChatStatus(String toString) {
        Log.d(TAG, "ReceivedChatStatus() called with: toString = [" + toString + "]");
    }

    public void getTypes() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getTypesApiii("" + pickupLatLng.get().latitude, "" + pickupLatLng.get().longitude);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickBackImg(View v) {
        getmNavigator().onClickBackImg();
    }

    public void EtaApi(String id) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            ETAClicked.set(true);
            map.clear();
            map.put(Constants.NetworkParameters.pLat, "" + pickupLatLng.get().latitude);
            map.put(Constants.NetworkParameters.pLng, "" + pickupLatLng.get().longitude);
            map.put(Constants.NetworkParameters.dLat, "" + dropLatLng.get().latitude);
            map.put(Constants.NetworkParameters.dLng, "" + dropLatLng.get().longitude);
            map.put(Constants.NetworkParameters.vType, id);
            map.put(Constants.NetworkParameters.rType, "1");
            GetETAApi(map);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickNext(View v) {
        getmNavigator().onClickNext();
    }
}
