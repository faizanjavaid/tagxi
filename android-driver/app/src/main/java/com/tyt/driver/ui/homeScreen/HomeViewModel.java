package com.tyt.driver.ui.homeScreen;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.DeclinedStatusModel;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.tripRequest.OnTripRequest;
import com.tyt.driver.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/11/17.
 */

public class HomeViewModel extends BaseNetwork<BaseResponse, HomeNavigator> implements SocketHelper.SocketListener {

    private static final String TAG = "HomeViewModel";
    Gson gson;
    SharedPrefence sharedPrefence;
    @Inject
    HashMap<String, String> hashMap;

    public ObservableBoolean homeClicked = new ObservableBoolean(true);
    public ObservableBoolean profileClicked = new ObservableBoolean(false);
    public ObservableBoolean historyClicked = new ObservableBoolean(false);
    public ObservableBoolean hideBar = new ObservableBoolean(false);
    public ObservableBoolean isOnline = new ObservableBoolean(false);


    public long lastUpdatedTime = System.currentTimeMillis();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("drivers");

    HashMap<String, String> map = new HashMap<>();

    @Inject
    public HomeViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                         SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
        lastUpdatedTime = System.currentTimeMillis();
    }


    /**
     * Callback method for successful API calls
     *
     * @param response {@link BaseResponse} model
     * @param taskId   Id of the API task
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        Log.d("xxxHomeModel-TAG", "onSuccessfulApi() called with: response = [" + new Gson().toJson(response.data) + "]" + " response.message " + response.message);
        setIsLoading(false);
        if (response.success) {

            if (response.message != null && response.message.equalsIgnoreCase("offline-success")) {
                ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);
                if (model != null)
                    sharedPrefence.savevalue(SharedPrefence.VehTypeID, model.getVehicleTypeId());
                sharedPrefence.saveBoolean(SharedPrefence.isOnline, false);
                isOnline.set(false);

                Map<String, Object> statusMap = new HashMap<>();
                statusMap.put("is_active", "0");
                ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);

            } else if (response.message != null && response.message.equalsIgnoreCase("online-success")) {

                ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);
                sharedPrefence.saveBoolean(SharedPrefence.isOnline, true);
                isOnline.set(true);
                if (model != null) {
                    if (model.getVehicleTypeId() != null)
                        sharedPrefence.savevalue(SharedPrefence.VehTypeID, model.getVehicleTypeId());

                    sharedPrefence.savevalue(SharedPrefence.ID, "" + model.getId());
                }
                SocketHelper.init(sharedPrefence, this, TAG, false);

                Map<String, Object> statusMap = new HashMap<>();
                statusMap.put("is_active", "1");
                ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);

            } else {
                Log.d("xxxHomeModel", "onSuccessfulApi: Else ");
                ProfileModel model = CommonUtils.getSingleObject(new Gson().toJson(response.data), ProfileModel.class);

                TripFragment tripFrag = (TripFragment) getmNavigator().getBaseAct().getSupportFragmentManager().findFragmentByTag(TripFragment.TAG);
                if (tripFrag != null) {
                    tripFrag.stop_refresh();
                }

                if (model != null) {
                    sharedPrefence.savevalue(SharedPrefence.ID, "" + model.getId());
                    if (model.getName() != null)
                        sharedPrefence.savevalue(SharedPrefence.Name, model.getName());
                    if (model.getEmail() != null)
                        sharedPrefence.savevalue(SharedPrefence.Email, model.getEmail());
                    if (model.getMobile() != null)
                        sharedPrefence.savevalue(SharedPrefence.PhoneNumber, model.getMobile());
                    if (model.getVehicleTypeId() != null)
                        sharedPrefence.savevalue(SharedPrefence.VehTypeID, model.getVehicleTypeId());

//                if (model.getMapKey() != null)
//                    Constants.PlaceApi_key = model.getMapKey();

                    if (model.getDeclinedReason() != null) {
                        getmNavigator().openApprovalPage(model.getDeclinedReason());
                    } else {
                        if (!model.getUploadedDocument()) {
                            getmNavigator().openDocumentPage();
                        } else if (!model.getApprove()) {
                            getmNavigator().openApprovalPage("");
                        }
                    }

                    if (model.getProfilePicture() != null)
                        sharedPrefence.savevalue(SharedPrefence.Profile, model.getProfilePicture());
                    Log.d("xxxHomeModel", "onSuccessfulApi:Else--> isOnline =" + model.getActive());
                    sharedPrefence.saveBoolean(SharedPrefence.isOnline, model.getActive());

                    isOnline.set(model.getActive());
                    SocketHelper.init(sharedPrefence, this, TAG, false);

                    Map<String, Object> statusMap = new HashMap<>();
                    statusMap.put("is_active", model.getActive() ? "1" : "0");
                    statusMap.put("demo_key", sharedPrefence.Getvalue(SharedPrefence.keyValue));
                    ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);

                    if (model.getMetaRequest() != null) {
                        Log.e(TAG, "MetaReq From OnsuccessApi Called");
                        getmNavigator().openMetaAcceptReject(model);
                        return;
                    }

                    sharedPrefence.saveBoolean(SharedPrefence.SHOW_INSTANTRIDE, model.getShow_instant_ride());

                    if (model.getOnTripRequest() != null) {
                        statusMap.put("is_available", false);
                        ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);
                        if (model.getOnTripRequest().getData().getIsCompleted() == 1 && model.getOnTripRequest().getData().getRequestBill() != null) {
                            getmNavigator().openFeedbackFrag(model);
                        } else {
                            OnTripRequest onTripRequest = model.getOnTripRequest();
                            Log.e("xxxtripDrop---", "drop---" + onTripRequest.getData().getDropAddress());
                            getmNavigator().openTripFragment(model);
                        }
                    } else {
                        statusMap.put("is_available", true);
                        ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);

                        sharedPrefence.savevalue(SharedPrefence.CurrDate, model.getCurrentDate());
                        sharedPrefence.savevalue(SharedPrefence.CurrenSymbol, model.getCurrencySymbol());
                        sharedPrefence.savevalue(SharedPrefence.TotalEarn, String.valueOf(model.getTotalEarnigs()));

                        getmNavigator().openMapFrag(model.getCurrentDate(), model.getCurrencySymbol(), model.getTotalEarnigs());
                    }
                    updateDriverToFirebaseDB(model);
                }
            }
        }
    }

    private void updateDriverToFirebaseDB(ProfileModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", model.getName());
        hashMap.put("vehicle_type_name", model.getVehicleTypeName());
        hashMap.put("vehicle_number", model.getCarNumber());
        hashMap.put("mobile", model.getMobile());
        ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(hashMap);
    }


    /**
     * Callback method for failed API tasks
     *
     * @param taskId Id of the API task
     * @param e      {@link CustomException} msg
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        getmNavigator().showMessage(e.getMessage());

        Log.e("code---", "code--" + e.getCode());

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
            sharedPrefence.savevalue(Constants.selectedMakeID, "");
            sharedPrefence.savevalue(Constants.selectedModelID, "");
            sharedPrefence.savevalue(Constants.selectedModelName, "");
            sharedPrefence.savevalue(Constants.selectedMakeName, "");
            sharedPrefence.savevalue(Constants.selectedCarNumber, "");
            sharedPrefence.savevalue(Constants.selectedCarYear, "");
            sharedPrefence.savevalue(Constants.selectedColor, "");
            getmNavigator().openFromStart();
        }
        setIsLoading(false);
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

    public void onClickOnOff() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            GetOnlineOffline();
        } else {
            setIsLoading(false);
            getmNavigator().showNetworkMessage();
        }

    }

    // Driver location update.
    public void sendLocation(String id, String lat, String lng, String bearing) {

       /* if (!mSocket.connected())
            initiateSocket();*/

        JSONObject object = new JSONObject();
        try {
            object.put(Constants.NetworkParameters.id, sharedPrefence.ID);
            object.put(Constants.NetworkParameters.LAT, lat);
            object.put(Constants.NetworkParameters.LNG, lng);
            object.put(Constants.NetworkParameters.BEARING, bearing);
            object.put(Constants.NetworkParameters.VehId, sharedPrefence.Getvalue(SharedPrefence.VehTypeID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((System.currentTimeMillis() - lastUpdatedTime) > 10000) {
            if (CommonUtils.checkLocationorGPSEnabled()) {
                if (MyApp.isInsideTrip())
                    SocketHelper.setDriverLocation(object.toString());
            }

            lastUpdatedTime = System.currentTimeMillis();
        }
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
        Log.d("xxxHomeModel", "onCreateRequest: ");
        getmNavigator().openAcceptRejectAct(s);
    }

    @Override
    public void RequestHandler(String toString) {
        Log.e("requestHandler---", "close");
        getmNavigator().closeAcceptReject();
    }

    @Override
    public void updateTripDistance(double v) {
    }

    @Override
    public void ApprovalStatus(String toString) {
        Log.e("approvalString--", "string---" + toString);
        BaseResponse response = CommonUtils.getSingleObject(toString, BaseResponse.class);
        Log.e("sucessMsg--", "msg---" + response.successMessage);

        if (response.successMessage.equalsIgnoreCase("driver_account_declined")) {
            DeclinedStatusModel declinedStatusModel = CommonUtils.getSingleObject(new Gson().toJson(response.data), DeclinedStatusModel.class);
            assert declinedStatusModel != null;
            getmNavigator().openApprovalPage(declinedStatusModel.getDeclinedReason());
        } else getmNavigator().openHomeAct();

    }

    @Override
    public void ReceivedChatStatus(String toString) {
        Log.d(TAG, "ReceivedChatStatus() called with: toString = [" + toString + "]");
    }

    public void onClickGo(View v) {
        onClickOnOff();
    }

}

