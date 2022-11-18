package com.tyt.client.ui.homeScreen.mapFrag;

import android.location.Location;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.CreateRequestData;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.RentalPackage;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.makeTrip.DriverData;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.FirebaseHelper;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.SocketHelper;
import com.tyt.client.utilz.exception.CustomException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Mahi in 2021.
 */
@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageDrawable")})
public class MapFragViewModel extends BaseNetwork<BaseResponse, MapFragNavigator> implements GoogleMap.OnCameraMoveStartedListener,
        SocketHelper.SocketListener, FirebaseHelper.FirebaseObserver {

    private static final String TAG = "MapScrnViewModel";

    public boolean isScreenAvailable = true;
    public GoogleMap mGoogleMap;

    //  static Socket mSocket;
    SharedPrefence sharedPrefence;
    public ObservableBoolean pickupClicked = new ObservableBoolean(true);
    public ObservableBoolean dropClicked = new ObservableBoolean(false);
    public ObservableBoolean dropShown = new ObservableBoolean(false);


    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> newpickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<String> newdropAddress = new ObservableField<>();
    public ObservableField<LatLng> pickupLatLng = new ObservableField<>();
    public ObservableField<LatLng> dropLatLng = new ObservableField<>();

    public ObservableField<String> fav_title = new ObservableField<>("Favourites");

    public ObservableField<String> shortDate = new ObservableField<>();
    public ObservableField<String> shortTime = new ObservableField<>();
    public ObservableField<String> noserv_desc = new ObservableField<>();
    public ObservableField<String> noserv_chnge_loc_btn = new ObservableField<>();

    public ObservableField<String> msg = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();

    public ObservableBoolean isDriversAvailable = new ObservableBoolean(false);

    public ObservableBoolean myself_trip = new ObservableBoolean(true);
    public ObservableField<String> myself_contact = new ObservableField<>("");
    public ObservableField<String> myself_contact_name = new ObservableField<>("");

    public ObservableBoolean RwalletClicked = new ObservableBoolean(false);
    public ObservableBoolean RCashClicked = new ObservableBoolean(false);
    public ObservableBoolean RCardClicked = new ObservableBoolean(false);
    public ObservableBoolean show_rental_book_btn = new ObservableBoolean(true);

    public ObservableField<String> RpaymentMethod = new ObservableField<>("Cash");
    public ObservableField<Integer> payType = new ObservableField<>(1);

    public ObservableField<String> RClickedZoneTypeID = new ObservableField<>("");
    public ObservableField<String> RenteredPromoCode = new ObservableField<>("");
    public ObservableField<String> RpromoStatus = new ObservableField<>("");
    public ObservableBoolean Rremovepromocode = new ObservableBoolean(false);

    int pickupClickedCount = 0;
    int dropClickedCount = 0;

    HashMap<String, String> map = new HashMap<>();

    List<EtaModel> FireBase_etaModel = new ArrayList<>();

    HashMap<String, Marker> driverPins = new HashMap<>();
    HashMap<String, String> driverDatas = new HashMap<>();

    public ObservableBoolean daily_clicked = new ObservableBoolean(true);
    public ObservableBoolean rental_clicked = new ObservableBoolean(false);
    public ObservableBoolean outside_clicked = new ObservableBoolean(false);
    public ObservableBoolean show_rental = new ObservableBoolean(false);

    public ObservableBoolean is_Rental_item_Picked = new ObservableBoolean(false);
    public ObservableBoolean Rental_Book_Btn = new ObservableBoolean(false);
    public ObservableField<String> selectedETATypeID = new ObservableField<>("");
    public ObservableField<String> selected_Rent_Package = new ObservableField<>("Select a package");
    public ObservableField<String> selected_Rent_PackID = new ObservableField<>("Select a package");

    public ObservableField<String> rpackage = new ObservableField<>(" package");

    public ObservableField<String> pickdroptext = new ObservableField<>("Choose Here");

    public ObservableField<String> addressName = new ObservableField<>("");

    public ObservableBoolean in_home = new ObservableBoolean(true);
    public ObservableBoolean in_pickDrop = new ObservableBoolean(false);
    public ObservableBoolean in_booking = new ObservableBoolean(false);

    public ObservableBoolean show_dropui = new ObservableBoolean(false);

    public ObservableBoolean show_myself_1 = new ObservableBoolean(true);
    public ObservableBoolean show_myself_2 = new ObservableBoolean(false);
    public ObservableBoolean show_myself_3 = new ObservableBoolean(false);
    public ObservableInt lastpicked = new ObservableInt(4);

    public ObservableField<String> save = new ObservableField<>("");
    public ObservableField<String> cancel = new ObservableField<>("");
    public ObservableField<String> fav_save = new ObservableField<>("");
    public ObservableField<String> fav_saving = new ObservableField<>("");
    public ObservableField<String> searchdes = new ObservableField<>("");

    public ObservableField<String> currloc = new ObservableField<>("");
    public ObservableField<String> enterlater = new ObservableField<>("");
    public ObservableField<String> locateinmap = new ObservableField<>("");

    public ObservableField<String> myself_title = new ObservableField<>("");
    public ObservableField<String> myself_desc = new ObservableField<>("");
    public ObservableField<String> choosecnt = new ObservableField<>("");
    public ObservableField<String> skip = new ObservableField<>("");
    public ObservableField<String> mcontinue = new ObservableField<>("");

    public ObservableField<String> PickUp = new ObservableField<>("");
    public ObservableField<String> Drop = new ObservableField<>("");
    public ObservableField<String> Myself = new ObservableField<>("");

    List<RentalPackage> rentalPackageList = new ArrayList<>();

    public ObservableField<String> rbookcartypename = new ObservableField<>("");
    public ObservableField<String> rbook = new ObservableField<>("Book ");

    public ObservableField<String> Rent_REQID = new ObservableField<>("");
    public ObservableBoolean RisCancelledClick = new ObservableBoolean(false);

    ArrayList<FavouriteLocations.FavLocData> FAVData = new ArrayList<>();
    List<FavouriteLocations.FavLocData> FAVDataList = new ArrayList<>();

    public MapFragViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);

        this.sharedPrefence = sharedPrefence;

        show_rental_book_btn.set(true);

        show_rental.set(sharedPrefence.GetBoolean(SharedPrefence.Show_Rental));

        if (translationModel != null) {
            PickUp.set(translationModel.txt_pick);
            Drop.set(translationModel.txt_dest);

            fav_title.set(translationModel.txt_favo);
            save.set(translationModel.txt_save);
            cancel.set(translationModel.txt_cancel);

            fav_save.set(translationModel.txt_saveasfav);
            fav_saving.set(translationModel.txt_savingfav);

            searchdes.set(translationModel.text_searchdes);

            locateinmap.set(translationModel.txt_locateonmap);
            enterlater.set(translationModel.txt_enterlater);
            currloc.set(translationModel.txt_currloc);

            myself_title.set(translationModel.txt_myself_title);
            myself_desc.set(translationModel.txt_myself_desc);
            choosecnt.set(translationModel.txt_chsecnt);
            skip.set(translationModel.txt_skip);
            mcontinue.set(translationModel.txt_continue);

            noserv_desc.set(translationModel.txt_no_serv_desc);
            noserv_chnge_loc_btn.set(translationModel.txt_no_serv_btn);

            Myself.set("Myself");
        }

        FirebaseHelper.init(sharedPrefence, this, false);
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

            if (response.message.equalsIgnoreCase("address added successfully")) {
                getmNavigator().showMessage("Location Added In Favourites");
                getmNavigator().closeFavDialog();
                getFavPlaceApi();
                //getmNavigator().callprofileapi();
            } else if (response.message.equalsIgnoreCase("address listed successfully")) {
                FAVData.clear();
                FAVDataList.clear();
                String favdata = new Gson().toJson(response.data);
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
                } else if (json.isJsonObject()) {
                    FavouriteLocations.FavLocData favouriteLocations = new Gson().fromJson(json, FavouriteLocations.FavLocData.class);
                    FAVData.add(favouriteLocations);
                }

                String favstring = CommonUtils.arrayToString(FAVData);

                FAVDataList.addAll(CommonUtils.stringToArray(favstring, FavouriteLocations.FavLocData[].class));

                getmNavigator().loadfavdata(FAVDataList);
            } else if (response.message.equalsIgnoreCase("created_request_successfully")) {
                if (response.data != null) {
                    String createRequestStr = CommonUtils.ObjectToString(response.data);
                    CreateRequestData model = (CreateRequestData) CommonUtils.StringToObject(createRequestStr, CreateRequestData.class);
                    Rent_REQID.set(model.getId());
                    sharedPrefence.savevalue(SharedPrefence.Rental_Request_ID,model.getId());
                    getmNavigator().openwaitingDialogAct(Rent_REQID.get());
                }
            } else if (response.message.equalsIgnoreCase("success")) {

                if (RisCancelledClick.get()) {
                    ((HomeAct) getmNavigator().getBaseAct()).closeWaitingDialog(false);
                }

                rentalPackageList.clear();
                ArrayList<RentalPackage> rentalPackageArrayList = new ArrayList<>();

                String strjson = new Gson().toJson(response.data);
                JsonElement json = JsonParser.parseString(strjson);

                if (json.isJsonArray()) {
                    JsonArray jsonArray = json.getAsJsonArray();
                    for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                        JsonElement data = jsonArray.getAsJsonArray().get(i);
                        if (data.isJsonObject()) {
                            RentalPackage rentalPackage = new Gson().fromJson(data, RentalPackage.class);
                            rentalPackageArrayList.add(rentalPackage);
                        } else if (data.isJsonArray()) {
                            Log.d(TAG, "onSuccessfulApi: isJsonArray ==" + data.getAsJsonArray());
                        }
                    }
                } else if (json.isJsonObject()) {
                    getmNavigator().showMessage("Json Object Called");
                    RentalPackage rentalPackage = new Gson().fromJson(json, RentalPackage.class);
                    rentalPackageArrayList.add(rentalPackage);
                }

                String renpackstring = CommonUtils.arrayToString(rentalPackageArrayList);

                rentalPackageList.addAll(CommonUtils.stringToArray(renpackstring, RentalPackage[].class));
                Log.e("xxxMapFrag", renpackstring);

                if (!rentalPackageList.isEmpty()) {
                    getmNavigator().loadrentalpacks(rentalPackageList);
                }

            }
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        show_rental_book_btn.set(true);
        getmNavigator().showMessage(e);
        if (e.getMessage() != null)
            if (e.getMessage().equalsIgnoreCase("service not available with this location")) {
                show_rental_book_btn.set(true);
                getmNavigator().open_no_serv_dialog();
            }
    }


    /**
     * Triggered when {@link GoogleMap} camera is starting to move
     *
     * @param i
     **/
    @Override
    public void onCameraMoveStarted(int i) {

    }

    public void onClickPickup(View v) {
        dropClickedCount = 0;
        pickupClicked.set(true);
        dropClicked.set(false);
        if (pickupClickedCount == 2) {
            getmNavigator().onClickPickup();
            pickupClickedCount = 0;
        } else {
            getmNavigator().moveToMapPosition();
        }
    }

    public void onClickDrop(View v) {
        pickupClickedCount = 0;
        pickupClicked.set(false);
        dropClicked.set(true);
        if (dropClickedCount == 2) {
            getmNavigator().onClickDrop();
            dropClickedCount = 0;
        } else {
            getmNavigator().moveToMapPosition();
        }
    }

    public void onClickCurrentLoc(View v) {
        getmNavigator().onClickCurrLocation();
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

        BaseResponse response = CommonUtils.getSingleObject(s, BaseResponse.class);

        if (response != null) {
            Log.e("sucessMessage---", ",message---" + response.successMessage);
            if (response.successMessage.equalsIgnoreCase("trip_accepted")) {
                show_rental_book_btn.set(true);
                ((HomeAct) getmNavigator().getBaseAct()).closeWaitingDialog(true);

            } else if (response.successMessage.equalsIgnoreCase("no_driver_found")) {
                show_rental_book_btn.set(true);
                getmNavigator().getBaseAct().runOnUiThread(() -> {
                    ((HomeAct) getmNavigator().getBaseAct()).closeWaitingDialog(false);
                });
            } else if (response.successMessage.equalsIgnoreCase("all drivers busy")) {
                show_rental_book_btn.set(true);
                getmNavigator().getBaseAct().runOnUiThread(() -> {
                    ((HomeAct) getmNavigator().getBaseAct()).closeWaitingDialog(false);
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

    public void onClickConfirm(View v) {
        getmNavigator().onClickConfirm(driverDatas, driverPins, "NORMAL");
    }

    @Override
    public void driverEnteredFence(String key, GeoLocation location, String response) {
        Log.e("DriverEntered--", "enetred---");
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("is_active")) {
                int isActive = jsonObject.getInt("is_active");
                Log.e("c", "Active-" + isActive);
                if (isActive == 1) {
                    long updatedAt = jsonObject.getLong("updated_at");
                    //JSONObject jsonObject1 = jsonObject.getJSONObject("l");
                    long currentTime = new Date().getTime();
                    long diff = (currentTime - updatedAt) / 1000;
                    if (diff < (5 * 60)) {
                        // Istypedata.set(true);
                        // isProgressShown.set(false);

                        double bearing = jsonObject.getDouble("bearing");
                        if (driverPins.containsKey(key)) {
                            Marker driverPin = driverPins.get(key);
                            //assert driverPin != null;
                            driverPin.remove();


                            Log.e("dkgjkj", "egkrgg");
//                            bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                            markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));

                            driverPins.put(key, marker);
                            driverDatas.put(key, response);
                        } else {

                            Log.e("dkgjkj", "egkrgg");

//                            bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                            markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));
                            driverPins.put(key, marker);
                            driverDatas.put(key, response);

                            FirebaseHelper.addObserverFor(key);
                        }

                        if (is_Rental_item_Picked.get()) {
                            showTYpes();
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void driverExitedFence(String key, String response) {
        try {
            Log.e("driverExit----", "exit---");
            if (driverPins.containsKey(key)) {
                Marker driverPin = driverPins.get(key);
                // assert driverPin != null;
                driverPin.remove();
                driverPins.remove(key);
                driverDatas.remove(key);

                FirebaseHelper.removeObserverFor(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void driverMovesInFence(String key, GeoLocation location, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("is_active")) {
                int isActive = jsonObject.getInt("is_active");
                Log.e("isActive---", "Active---" + isActive);
                if (isActive == 1) {
                    if (driverPins.containsKey(key)) {
                        Marker driverPin = driverPins.get(key);
                        double bearing = jsonObject.getDouble("bearing");
                        if (driverPin != null)
                            driverPin.remove();
//                        bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                        markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.latitude, location.longitude))
                                .title("MY Location")
                                .rotation((float) bearing)
                                .anchor(0.5f, 0.5f)
                                .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));
                        driverPins.put(key, marker);
                        driverDatas.put(key, response);

                        if (is_Rental_item_Picked.get()) {
                            showTYpes();
                        }


                    } else {
                        long updatedAt = jsonObject.getLong("updated_at");
                        long currentTime = new Date().getTime();
                        long diff = (currentTime - updatedAt) / 1000;
                        if (diff < (5 * 60)) {
//                            Istypedata.set(true);
//                            isProgressShown.set(false);
                            double bearing = jsonObject.getDouble("bearing");
//                            bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                            markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));

                            driverPins.put(key, marker);
                            driverDatas.put(key, response);

                            FirebaseHelper.addObserverFor(key);
                            if (is_Rental_item_Picked.get()) {
                                showTYpes();
                            }
                        }
                    }
                } else {
                    if (driverPins.containsKey(key)) {
                        Marker driverPin = driverPins.get(key);
                        if (driverPin != null)
                            driverPin.remove();
                        driverPins.remove(key);
                        driverDatas.remove(key);

                        FirebaseHelper.removeObserverFor(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void driverWentOffline(String key) {
        try {
            Log.e("driverOffline---", "offline----");
            if (driverPins.containsKey(key)) {
                Marker driverPin = driverPins.get(key);
                if (driverPin != null)
                    driverPin.remove();
                driverPins.remove(key);
                driverDatas.remove(key);

                FirebaseHelper.removeObserverFor(key);
            }

            if (is_Rental_item_Picked.get()) {
                int driverCount = 0;
                for (String keys : driverPins.keySet()) {
                    String driverDataStr = driverDatas.get(keys);
                    try {
                        assert driverDataStr != null;
                        JSONObject jsonObject = new JSONObject(driverDataStr);
                        if (jsonObject.has("vehicle_type")) {
                            String typeId = jsonObject.getString("vehicle_type");
                            if (getmNavigator().getSelectedCar() != null) {
                                if ((getmNavigator().getSelectedCar().getTypeId() + "").equals(typeId)) {
                                    driverCount++;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isDriversAvailable.set(driverCount > 0);
                if (!isDriversAvailable.get())
                    getmNavigator().updateCarETA(getmNavigator().getSelectedCar().getTypeId(), "NA");
            }

/*            int driverCount = 0;
            for (String keys : driverPins.keySet()) {
                String driverDataStr = driverDatas.get(keys);
                try {
                    assert driverDataStr != null;
                    JSONObject jsonObject = new JSONObject(driverDataStr);
                    if (jsonObject.has("vehicle_type")) {
                        String typeId = jsonObject.getString("vehicle_type");
                        if (getmNavigator().getSelectedCar() != null) {
                            if ((getmNavigator().getSelectedCar().getTypeId() + "").equals(typeId)) {
                                driverCount++;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            isDriversAvailable.set(driverCount > 0);
            if (!isDriversAvailable.get())
                getmNavigator().updateCarETA(getmNavigator().getSelectedCar().getTypeId(), "NA");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DriverData driverData1 = null;

    @Override
    public void driverDataUpdated(String key, String response) {
        /*Log.v("http_log", "driverDataUpdated:key= " + key + " : res= " + response);
        driverData1 = gson.fromJson(response, DriverData.class);

        Log.d("xxxxMakeTripModel", "driverEnteredFence:->757 " + driverData1.getL());*/
    }

    public void showTYpes() {

        for (String key : driverPins.keySet()) {
            Objects.requireNonNull(driverPins.get(key)).setVisible(false);
            FirebaseHelper.removeObserverFor(key);
        }

        for (String key : driverPins.keySet()) {
            String driverDetails = driverDatas.get(key);
            try {
                assert driverDetails != null;
                JSONObject jsonObject = new JSONObject(driverDetails);
                if (jsonObject.has("vehicle_type")) {
                    String typeId = jsonObject.getString("vehicle_type");
                    if (Objects.requireNonNull(selectedETATypeID.get()).equalsIgnoreCase(typeId)) {
                        Marker marker = driverPins.get(key);
                        assert marker != null;
                        marker.setVisible(true);
                        FirebaseHelper.addObserverFor(key);
                        isDriversAvailable.set(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        calculateETADuration(true);
    }

    public void calculateETADuration(boolean refreshAdapter) {
        if (FireBase_etaModel != null) {
            HashMap<String, List<Float>> distances = new HashMap<>();
            for (EtaModel etaModel : FireBase_etaModel) {
                List<Float> distanceList = new ArrayList<>();
                for (String driverId : driverPins.keySet()) {
                    String driverDataStr = driverDatas.get(driverId);
                    try {
                        assert driverDataStr != null;
                        JSONObject jsonObject = new JSONObject(driverDataStr);
                        if (jsonObject.has("vehicle_type")) {
                            String typeId = jsonObject.getString("vehicle_type");
                            if (typeId.equalsIgnoreCase(etaModel.getTypeId())) {
                                String[] arr = jsonObject.getString("l")
                                        .replace("[", "")
                                        .replace("]", "")
                                        .split(",");
                                LatLng carLatLng = new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));

                                Location pickLocation = new Location("pickup");
                                pickLocation.setLatitude(Objects.requireNonNull(pickupLatLng.get()).latitude);
                                pickLocation.setLongitude(Objects.requireNonNull(pickupLatLng.get()).longitude);

                                Location driverLocation = new Location("driver");
                                driverLocation.setLatitude(carLatLng.latitude);
                                driverLocation.setLongitude(carLatLng.longitude);

//                                sharedPrefence.savevalue(SharedPrefence.DriverLong, String.valueOf(carLatLng.longitude));
//                                sharedPrefence.savevalue(SharedPrefence.DriverLat, String.valueOf(carLatLng.latitude));

                                sharedPrefence.saveLong(SharedPrefence.DriverLat, Double.doubleToRawLongBits(carLatLng.latitude));
                                sharedPrefence.saveLong(SharedPrefence.DriverLong, Double.doubleToRawLongBits(carLatLng.longitude));


                                float distance = driverLocation.distanceTo(pickLocation);
                                distanceList.add(distance);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    distances.put(etaModel.getTypeId(), distanceList);
                }
            }
            Log.v("xxxhttp_log", "line->321" + distances.toString());
            for (EtaModel etaModel : FireBase_etaModel) {
                String typeId = etaModel.getTypeId();
                if (distances.get(typeId) != null) {
                    List<Float> distanceList = distances.get(typeId);
                    assert distanceList != null;
                    if (distanceList.size() > 0) {
                        float minDistance = Collections.min(Objects.requireNonNull(distances.get(typeId)));
                        String sMinutes = "0";
                        float kmDistance = minDistance / 1000;
                        if (kmDistance >= 0 && kmDistance < 3) {
                            sMinutes = "3";
                        } else if (kmDistance >= 3 && kmDistance < 7) {
                            sMinutes = "5";
                        } else if (kmDistance >= 7 && kmDistance < 10) {
                            sMinutes = "7";
                        } else {
                            int speed = 50;
                            float time = (minDistance / 1000) / (float) speed;
                            sMinutes = String.format(Locale.ENGLISH, "%.0f", (time * 60));
                        }
                        int iMinutes = Integer.parseInt(sMinutes);
                        if (iMinutes == 0)
                            iMinutes = 1;
                        if (iMinutes >= 60) {
                            int hours = iMinutes / 60;
                            int minutes = iMinutes % 60;
                            if (hours == 1) {
                                etaModel.setDriverArivalEstimation(hours + " hr " + minutes + " mins");
                            } else {
                                etaModel.setDriverArivalEstimation(hours + " hrs " + minutes + " mins");
                            }
                        } else if (iMinutes == 1 || iMinutes == 0) {
                            etaModel.setDriverArivalEstimation(iMinutes + " min");
                        } else {
                            etaModel.setDriverArivalEstimation(iMinutes + " mins");
                        }
                    } else {
                        etaModel.setDriverArivalEstimation("NA");
                    }
                } else {
                    etaModel.setDriverArivalEstimation("NA");
                }
            }
        }
        if (refreshAdapter) {
            getmNavigator().refreshAdapter(FireBase_etaModel);
        }
    }

    @Override
    public void tripStatusReceived(String response) {

    }

    public void getETAApi() {

        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            map.clear();
            map.put(Constants.NetworkParameters.pLat, "" + Objects.requireNonNull(pickupLatLng.get()).latitude);
            map.put(Constants.NetworkParameters.pLng, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);
            if (dropLatLng.get() != null) {
                map.put(Constants.NetworkParameters.dLat, "" + Objects.requireNonNull(dropLatLng.get()).latitude);
                map.put(Constants.NetworkParameters.dLng, "" + Objects.requireNonNull(dropLatLng.get()).longitude);
            }
            map.put(Constants.NetworkParameters.rType, "1");

            JSONArray jDrivers = new JSONArray();
//        try {
//            for (String key : driverDatas.keySet()) {
//                JSONObject jData = new JSONObject(driverDatas.get(key));
//                String d_id = jData.getString("id");
//                Marker marker = driverPins.get(key);
//
//                String DrivertypeId = jData.getString("vehicle_type");
//
//                if (DrivertypeId.equalsIgnoreCase(typeId)) {
//                    JSONObject jDriver = new JSONObject();
//                    jDriver.put("driver_id", d_id);
//                    jDriver.put("driver_lat", marker.getPosition().latitude);
//                    jDriver.put("driver_lng", marker.getPosition().longitude);
//                    jDrivers.put(jDriver);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            GetETAApi(map);
        }

    }

    public void onCLickClock(View v) {
        getmNavigator().onClickLoadCalendar();
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
        callRentalPackAPI();
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

    public void clickBackPickDrop(View v) {
        getmNavigator().BackClickPickDrop();
    }

    public void openPickDrop(View view) {
        getmNavigator().openDropScrn();
    }

    public void openHomeView(View view) {
        getmNavigator().openhomemap();
        onClickPickup(view);
    }

    public void calladdfavapi(HashMap<String, Object> favaddmap) {
        setIsLoading(true);
        AddFavLocation(favaddmap);
    }

    public void onAddressNameChanged(Editable e) {
        addressName.set(e.toString());
    }

    public void openPlaceSearchAPIPick(View v) {
        getmNavigator().onClickPickup();
        pickupClicked.set(true);
        dropClicked.set(false);
        show_dropui.set(false);
    }

    public void openPlaceSearchAPIDrop(View v) {
        getmNavigator().onClickDrop();
        pickupClicked.set(false);
        dropClicked.set(true);
        show_dropui.set(true);
    }

    public void clearpicket(View v) {
        getmNavigator().clearpicket();
    }

    public void cleardropet(View v) {
        getmNavigator().cleardropet();
    }

    public void OnclickCurrentLocPick(View v) {
        getmNavigator().clikedcurrloc();
    }

    public void OnclickLocateinMapPick(View v) {
        getmNavigator().clickedlocateonmap();
    }

    public void OnclickEnterdeslaterPick(View v) {
        getmNavigator().clickedenterdeslater();
    }

    public void showmyselfDialog(View v) {
        getmNavigator().showMyselfDialogv("DROP");
    }

    public void callRentalPackAPI() {
        HashMap<String, String> rmap = new HashMap<>();
        if (getmNavigator().isNetworkConnected()) {
            if (pickupLatLng.get() != null) {
                rmap.put(Constants.NetworkParameters.pLat, String.valueOf(Objects.requireNonNull(pickupLatLng.get()).latitude));
                rmap.put(Constants.NetworkParameters.pLng, String.valueOf(Objects.requireNonNull(pickupLatLng.get()).longitude));
                GetRentalPacks(rmap);
            } else {
                getmNavigator().showMessage("Try again later");
            }
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickRentalBook(View v) {

        getmNavigator().showMyselfDialogv("RENT");

        if (getmNavigator().isNetworkConnected()) {
            map.clear();

            map.put(Constants.NetworkParameters.Rent_pack_ID, selected_Rent_PackID.get());
            map.put(Constants.NetworkParameters.pLat, "" + Objects.requireNonNull(pickupLatLng.get()).latitude);
            map.put(Constants.NetworkParameters.pLng, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);

            if (Objects.requireNonNull(selectedETATypeID.get()).isEmpty()) {
                setIsLoading(false);
                getmNavigator().showMessage("Select Your Vehicle please");
                return;
            }

            if (myself_trip.get()) {
                map.put(Constants.NetworkParameters.Myself, "1");
            } else {
                map.put(Constants.NetworkParameters.Myself, "0");
                map.put(Constants.NetworkParameters.Myself_Contact_No, myself_contact.get());
            }

            //work with pay option
            map.put(Constants.NetworkParameters.PayOpt, "" + "1");

            map.put(Constants.NetworkParameters.vType, RClickedZoneTypeID.get());
            map.put(Constants.NetworkParameters.rType, "1");
            map.put(Constants.NetworkParameters.pAddress, pickupAddress.get());

        } else getmNavigator().showNetworkMessage();

    }

    public void createReqAPI() {
        CreateReqApicall(map);
    }

    public void onCLickRentPaymentType(View v) {
        //show payment type sheet
    }

    public void RonCLickPromoType(View v) {
        //show promo code sheet
    }

    public void rentalCancelApi() {
        if (getmNavigator().isNetworkConnected()) {
            RisCancelledClick.set(true);
            map.clear();
            map.put(Constants.NetworkParameters.request_id, Rent_REQID.get());
            RequestCancelApi(map);
        } else getmNavigator().showNetworkMessage();
    }

    public void tripCancelApi(String ID) {
        if (getmNavigator().isNetworkConnected()) {
            map.clear();
            map.put(Constants.NetworkParameters.request_id, ID);
            RequestCancelApi(map);
        } else {
            getmNavigator().showNetworkMessage();
        }
    }

    public void ETA_API() {
        HashMap<String, String> etamap = new HashMap<>();
        etamap.put(Constants.NetworkParameters.pLat, String.valueOf(Objects.requireNonNull(pickupLatLng.get()).latitude));
        etamap.put(Constants.NetworkParameters.pLng, String.valueOf(Objects.requireNonNull(pickupLatLng.get()).longitude));
        etamap.put(Constants.NetworkParameters.rType, "1");
        GetETAApi(etamap);
    }

    public void open_drop_favDialog(View view) {
        getmNavigator().openFavDialog();
    }

    public void getFavPlaceApi() {
        if (getmNavigator().isNetworkConnected()) {
            GetFavList();
        } else getmNavigator().showNetworkMessage();
    }

}
