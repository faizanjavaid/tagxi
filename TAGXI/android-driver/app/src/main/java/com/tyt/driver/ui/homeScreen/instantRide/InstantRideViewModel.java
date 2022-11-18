package com.tyt.driver.ui.homeScreen.instantRide;

import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.retro.responsemodel.TranslationModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class InstantRideViewModel extends BaseNetwork<BaseResponse, InstantRideNavigator> {
    private static final String TAG = "InstantRideViewModel";

    public ObservableBoolean isETA_Picked = new ObservableBoolean(false);
    public ObservableBoolean EnableBookButon = new ObservableBoolean(false);
    public ObservableBoolean PickupClicked = new ObservableBoolean(false);
    public ObservableBoolean DropClicked = new ObservableBoolean(false);
    public ObservableBoolean SwitchToMapMode = new ObservableBoolean(false);

    public List<EtaModel> insrlist = new ArrayList<>();

    public ObservableField<String> insr_name = new ObservableField<>("");
    public ObservableField<String> insr_phone = new ObservableField<>("");

    public ObservableField<String> PickAddress = new ObservableField<>("Pick Address");
    public ObservableField<String> DropAddress = new ObservableField<>("Drop Address");
    public ObservableField<LatLng> PickLatLng = new ObservableField<>();
    public ObservableField<LatLng> DropLatLng = new ObservableField<>();

    public ObservableField<String> Conformloc = new ObservableField<>("Confirm Location");

    public ObservableField<String> MainTitle = new ObservableField<>("Instant Ride");

    public GoogleMap mGoogleMap;

    int pickupClickedCount = 0;
    int dropClickedCount = 0;

    @Inject
    public TranslationModel translationModel;

    public InstantRideViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }

    @Override
    public HashMap<String, String> getMap() {
        return new HashMap<>();
    }

    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {

        if (response.success) {
            if (response.message.equalsIgnoreCase("success")) {
                setIsLoading(false);
                insrlist.clear();

                ArrayList<EtaModel> TempetaList = new ArrayList<>();
                String strJson = new Gson().toJson(response.data);

                JsonElement json = JsonParser.parseString(strJson);

                if (json.isJsonArray()) {
                    JsonArray jsonArray = json.getAsJsonArray();
                    for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                        JsonElement data = jsonArray.getAsJsonArray().get(i);
                        if (data.isJsonObject()) {
                            EtaModel etaModel = new Gson().fromJson(data, EtaModel.class);
                            TempetaList.add(etaModel);
                        } else if (data.isJsonArray()) {
                            Log.d(TAG, "onSuccessfulApi: isJsonArray ==" + data.getAsJsonArray());
                        }
                    }
                } else if (json.isJsonObject()) {
                    EtaModel etaModel = new Gson().fromJson(json, EtaModel.class);
                    TempetaList.add(etaModel);
                }

                String etares = CommonUtils.arrayToString(TempetaList);

                Log.e("xxxINSR", etares);

                insrlist.addAll(CommonUtils.stringToArray(etares, EtaModel[].class));

                getmNavigator().loadetatypes(insrlist);

            } else if (response.message.equalsIgnoreCase("created_instant_ride_successfully")) {

                getmNavigator().showMessage("Booking Confirmed... Trip will start soon");

                getmNavigator().movetoinstanttrip();

            }
        }
    }


    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
    }

    public void startInsRide(HashMap<String, Object> insridemap) {
        if (getmNavigator().isNetworkConnected()) {
            MakeInstantRide(insridemap);
        } else getmNavigator().showNetworkMessage();
    }

    public void getCarTypes(HashMap<String, String> etatypesmap) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            GetETAApi(etatypesmap);
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        if (PickupClicked.get() || DropClicked.get()) {
            SwitchToMapMode.set(false);
            PickupClicked.set(false);
            DropClicked.set(false);
            MainTitle.set("Instant Ride");
        } else {
            getmNavigator().removeinsfrag();
        }
    }

    public void onPhoneChanged(Editable e) {
        insr_phone.set(e.toString());
    }

    public void onNameChanged(Editable e) {
        insr_name.set(e.toString());
    }

    public void onClickPickup(View v) {
        pickupClickedCount+=1;
        dropClickedCount = 0;

        if (pickupClickedCount == 2) {
            getmNavigator().openplacesearchAPI();
        }else {
            SwitchToMapMode.set(true);
            PickupClicked.set(true);
            Conformloc.set("Confirm PickUp Location");
            MainTitle.set("Choose Your PickUp");
            DropClicked.set(false);
        }
    }

    public void onClickDrop(View v) {
        dropClickedCount+=1;
        pickupClickedCount=0;

        if (dropClickedCount == 2) {
           getmNavigator().openplacesearchAPI();
        }else {
            SwitchToMapMode.set(true);
            DropClicked.set(true);
            Conformloc.set("Confirm Drop Location");
            MainTitle.set("Choose Your Destination");
            PickupClicked.set(false);
        }

    }

    public void validatePickDrop(View v) {
        if (Objects.requireNonNull(PickAddress.get()).isEmpty()) {
            SwitchToMapMode.set(true);
            PickupClicked.set(true);
            Conformloc.set("Confirm PickUp Location");
            MainTitle.set("Choose Your PickUp");
            PickAddress.set("Pick Address");
            DropClicked.set(false);
        } else if (Objects.requireNonNull(DropAddress.get()).isEmpty()) {
            SwitchToMapMode.set(true);
            DropClicked.set(true);
            Conformloc.set("Confirm Drop Location");
            MainTitle.set("Choose Your Destination");
            DropAddress.set("Drop Address");
            PickupClicked.set(false);
        } else {
            //close Map Screen
            MainTitle.set("Instant Ride");
            SwitchToMapMode.set(false);

            if (DropLatLng.get() == null) {
                getmNavigator().showMessage("Choose Drop");
            }else if (PickLatLng.get() != null && DropLatLng.get() != null) {
                getmNavigator().calletaAPI();
            }else getmNavigator().showMessage("Choose Adddress Properly..!");
        }
    }

    public void onclickCurrLoc(View v) {
        getmNavigator().currloc();
    }

}
