package com.tyt.driver.ui.homeScreen.mapscrn;

import android.util.Log;
import android.view.View;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 11/13/17.
 */
@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageDrawable")})
public
class MapScrnViewModel extends BaseNetwork<BaseResponse, MapScrnNavigator> implements
        SocketHelper.SocketListener {
    private static final String TAG = "MapScrnViewModel";
    public boolean isScreenAvailable = true;

    public ObservableField<String> earnings = new ObservableField<>();
    public ObservableField<String> currency = new ObservableField<>();
    public ObservableField<String> currDate = new ObservableField<>();
    public ObservableBoolean hideEarnings;
    public ObservableBoolean show_instant_ride;

    //  static Socket mSocket;
    SharedPrefence sharedPrefence;


    public MapScrnViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        hideEarnings = new ObservableBoolean(sharedPrefence.GetBoolean(SharedPrefence.SHOW_EARNINGS));
        show_instant_ride = new ObservableBoolean(sharedPrefence.GetBoolean(SharedPrefence.SHOW_INSTANTRIDE));
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


    public void onClickCurrentLoc(View v) {
        getmNavigator().onClickCurrLocation();
    }


    @Override
    public boolean isNetworkConnected() {
        return getmNavigator().isNetworkConnected();
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
        Log.d("xxxMapScrnModel", "onCreateRequest: ");
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

    public void onClickShowEarnings(View v) {
        sharedPrefence.saveBoolean(SharedPrefence.SHOW_EARNINGS, true);
        hideEarnings.set(!sharedPrefence.GetBoolean(SharedPrefence.SHOW_EARNINGS));
    }

    public void onClickHideEarnings(View v) {
        sharedPrefence.saveBoolean(SharedPrefence.SHOW_EARNINGS, false);
        hideEarnings.set(!sharedPrefence.GetBoolean(SharedPrefence.SHOW_EARNINGS));
    }

    public void onClickViewEarnings(View v) {
        getmNavigator().onclickEarnings();
    }

    public void onClickSideMenu(View v) {
        getmNavigator().openSideMenu();
    }

}
