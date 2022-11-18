package com.tyt.client.ui.homeScreen;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;
import com.tyt.client.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SocketHelper;
import com.tyt.client.utilz.exception.CustomException;
import com.tyt.client.utilz.SharedPrefence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mahi in 2021.
 */

public class HomeViewModel extends BaseNetwork<BaseResponse, HomeNavigator> implements SocketHelper.SocketListener {

    Gson gson;
    SharedPrefence sharedPrefence;
    @Inject
    HashMap<String, String> hashMap;

    public String TAG = "HomeViewModel";

    public ObservableBoolean homeClicked = new ObservableBoolean(true);
    public ObservableBoolean profileClicked = new ObservableBoolean(false);
    public ObservableBoolean historyClicked = new ObservableBoolean(false);
    public ObservableBoolean hideBar = new ObservableBoolean(false);
    public ObservableBoolean cancelClicked = new ObservableBoolean(false);

    public ObservableBoolean sheet_expanded = new ObservableBoolean(false);

    public ObservableBoolean daily_clicked = new ObservableBoolean(true);
    public ObservableBoolean rental_clicked = new ObservableBoolean(false);
    public ObservableBoolean outside_clicked = new ObservableBoolean(false);

    ArrayList<FavouriteLocations.FavLocData> FAVData = new ArrayList<>();
    List<FavouriteLocations.FavLocData> FAVDataList = new ArrayList<>();

    int homecall = 0;

    @Inject
    public HomeViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                         SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
        SocketHelper.init(sharedPrefence, this, TAG, false);
    }


    /**
     * Callback method for successful API calls
     *
     * @param response {@link BaseResponse} model
     * @param taskId   Id of the API task
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        Log.d("--HomeModel-TAG", "onSuccessfulApi() called with: taskId = [" + taskId + "], response = [" + new Gson().toJson(response.data) + "]" + " response.message " + response.message);
        setIsLoading(false);
        if (response.success) {

            if (cancelClicked.get()) {
                getmNavigator().closeWaitingDialog(false);
            } else {
                ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);
                TripFragment tripFrag = (TripFragment) getmNavigator().getBaseAct().getSupportFragmentManager().findFragmentByTag(TripFragment.TAG);
                if (tripFrag != null) {
                    tripFrag.stop_refresh();
                }

                if (model != null) {
                    sharedPrefence.saveBoolean(SharedPrefence.Show_Rental, model.getShow_rental_ride());
                }

                if (model != null) {
                    sharedPrefence.savevalue(SharedPrefence.ID, "" + model.getId());
                    sharedPrefence.savevalue(SharedPrefence.Name, model.getName());
                    sharedPrefence.savevalue(SharedPrefence.Email, model.getEmail());
                    sharedPrefence.savevalue(SharedPrefence.PhoneNumber, model.getMobile());
                    sharedPrefence.savevalue(SharedPrefence.Profile, model.getProfilePicture());

                    SocketHelper.init(sharedPrefence, this, TAG, false);

                    if (model.getMetaRequest() != null) {
                        getmNavigator().openWaitingDialog(model.getMetaRequest().getData().getId());
                    }
                    if (model.getOnTripRequest() != null) {
                        if (model.getOnTripRequest().getData().getIsCompleted() == 1 && model.getOnTripRequest().getData().getRequestBill() != null) {
                            getmNavigator().openFeedbackFrag(model);
                        } else {
                            if (model.getOnTripRequest() != null) {
                                getmNavigator().openTripFragment(model);
                            }
                        }
                    } else {

                        getmNavigator().openHomeFrag();

                        new Handler().postDelayed(() -> {
                            if (model.getFavouriteLoactions() != null) {

                                FAVDataList.clear();
                                FAVData.clear();

                                String favdata = new Gson().toJson(model.getFavouriteLoactions().getFavLocData());

                                JsonElement json = JsonParser.parseString(favdata);

                                if (json.isJsonArray()) {

                                    JsonArray jsonArray = json.getAsJsonArray();
                                    for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                                        JsonElement data = jsonArray.getAsJsonArray().get(i);
                                        if (data.isJsonObject()) {
                                            FavouriteLocations.FavLocData favouriteLocations = new Gson().fromJson(data, FavouriteLocations.FavLocData.class);
                                            FAVData.add(favouriteLocations);
                                        } else if (data.isJsonArray()) {

                                        }
                                    }

                                    String favString = CommonUtils.arrayToString(FAVData);

                                    FAVDataList.addAll(CommonUtils.stringToArray(favString, FavouriteLocations.FavLocData[].class));

                                    getmNavigator().loadfavouritesdata(FAVDataList);

                                } else if (json.isJsonObject()) {
                                }
                            }
                        }, 2000);

                    }
                }
            }

        }

    }

    /**
     * Callback method for failed API tasks
     *
     * @param taskId Id of the API task
     * @param e      {@link CustomException} msg
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        cancelClicked.set(false);
        getmNavigator().showMessage(e.getMessage());

        if (e.getCode() == 401) {
            sharedPrefence.savevalue(SharedPrefence.AccessToken, "");
            sharedPrefence.savevalue(SharedPrefence.Name, "");
            sharedPrefence.savevalue(SharedPrefence.Email, "");
            sharedPrefence.savevalue(SharedPrefence.Password, "");
            sharedPrefence.savevalue(SharedPrefence.CountryCode, "");
            sharedPrefence.savevalue(SharedPrefence.ConfirmPassword, "");
            sharedPrefence.savevalue(SharedPrefence.PhoneNumber, "");
            sharedPrefence.savevalue(SharedPrefence.Profile, "");
            sharedPrefence.savevalue(SharedPrefence.ID, "");
            getmNavigator().openFromStart();
        }
    }

    /**
     * {@link HashMap} with query parameters used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return hashMap;
    }


    public void onClickProfile(View v) {
        getmNavigator().onClickProfile();
        profileClicked.set(true);
        homeClicked.set(false);
        historyClicked.set(false);
    }

    public void onClickHistory(View v) {
        getmNavigator().onClickHistory();
    }

    public void onClickHome(View v) {
        getmNavigator().onClickHome();
    }


    public void porfileApi() {
        if (getmNavigator().isNetworkConnected()) {
            GetUserProfile();
        } else
            getmNavigator().showNetworkMessage();
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
    public void onLoadDrivers() {

    }

    @Override
    public void TripStatus(String s) {
        Log.e("tripStatus---", "homePage");

        BaseResponse response = CommonUtils.getSingleObject(s, BaseResponse.class);

        if (response != null) {
            Log.e("sucessMessage---", ",message---" + response.successMessage);
            if (response.successMessage.equalsIgnoreCase("trip_accepted")) {
                getmNavigator().closeWaitingDialog(true);
            } else if (response.successMessage.equalsIgnoreCase("no_driver_found")) {
                getmNavigator().getBaseAct().runOnUiThread(() -> {
                    getmNavigator().closeWaitingDialog(false);
                });
            } else if (response.successMessage.equalsIgnoreCase("all drivers busy")) {
                getmNavigator().getBaseAct().runOnUiThread(() -> {
                    getmNavigator().closeWaitingDialog(false);
                });
            }
        }
    }

    @Override
    public void ApprovalStatus(String toString) {

    }

    @Override
    public void ReceivedChatStatus(String toString) {

    }

    public void cancelApi(String ID) {
        if (getmNavigator().isNetworkConnected()) {
            cancelClicked.set(true);
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, ID);
            RequestCancelApi(hashMap);
        } else {
            getmNavigator().showNetworkMessage();
        }
    }


    public void onClikDaily(View view) {
        daily_clicked.set(true);
        rental_clicked.set(false);
        outside_clicked.set(false);
        getmNavigator().onclickdaily();
    }

    public void onClikRental(View view) {
        rental_clicked.set(true);
        daily_clicked.set(false);
        outside_clicked.set(false);
        getmNavigator().onclickrental();
    }

    public void onClikOutstation(View view) {
        outside_clicked.set(true);
        daily_clicked.set(false);
        rental_clicked.set(false);
        getmNavigator().onclickoutstation();
    }

    public void onClickUpArrow(View view) {
        getmNavigator().onclickuparrow();
    }

    public void onClickSearchDes(View view) {
        getmNavigator().onclicksearchdes();
    }
}
