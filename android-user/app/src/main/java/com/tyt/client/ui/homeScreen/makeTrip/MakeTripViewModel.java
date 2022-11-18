package com.tyt.client.ui.homeScreen.makeTrip;

import static com.google.android.gms.maps.model.JointType.ROUND;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.CreateRequestData;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.OffersResponseData;
import com.tyt.client.retro.responsemodel.Route;
import com.tyt.client.retro.responsemodel.Step;
import com.tyt.client.retro.responsemodel.Type;
import com.tyt.client.ui.homeScreen.makeTrip.RouteUtils.RootMap;
import com.tyt.client.ui.homeScreen.makeTrip.RouteUtils.RouteMapClient;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.FirebaseHelper;
import com.tyt.client.utilz.MapUtilz;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.SocketHelper;
import com.tyt.client.utilz.exception.CustomException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahi in 2021.
 */

public class MakeTripViewModel extends BaseNetwork<BaseResponse, MakeTripNavigator> implements SocketHelper.SocketListener, FirebaseHelper.FirebaseObserver {
    private static final String TAG = "MakeTripViewModel";

    public boolean isScreenAvailable = true;
    private GoogleApiClient mGoogleApiClient;

    SharedPrefence sharedPrefence;

    public ObservableField<String> bookcartypename = new ObservableField<>("");
    public ObservableField<String> book = new ObservableField<>("Book ");

    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> dropAddress = new ObservableField<>();
    public ObservableField<LatLng> pickupLatLng = new ObservableField<>();
    public ObservableField<LatLng> dropLatLng = new ObservableField<>(null);

    public ObservableField<String> carModel = new ObservableField<>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> ETA = new ObservableField<>();
    public ObservableField<String> promoValue = new ObservableField<>("");
    public ObservableField<String> ClickedETATypeID = new ObservableField<>("");
    public ObservableField<String> dateChoose = new ObservableField<>("");

    public ObservableField<String> REQID = new ObservableField<>("");

    public ObservableBoolean isDateChoosen = new ObservableBoolean(false);
    public ObservableField<String> splittedTime = new ObservableField<>("");
    public ObservableField<String> splittedDate = new ObservableField<>("");

    public ObservableField<String> clickedPromoCode = new ObservableField<>("");

    public ObservableField<String> ClickedZoneTypeID = new ObservableField<>("");

    public ObservableField<String> enteredPromoCode = new ObservableField<>("");
    public ObservableField<String> promoStatus = new ObservableField<>("");
    public ObservableBoolean removepromocode = new ObservableBoolean(false);

    public ObservableBoolean onClickTypes = new ObservableBoolean(false);

    public ObservableBoolean rental_request = new ObservableBoolean(false);

    public ObservableField<Integer> payType = new ObservableField<>(1);
    public ObservableField<String> paymentMethod = new ObservableField<>("Cash");

    public ObservableBoolean ETAClicked = new ObservableBoolean(false);
    public ObservableBoolean noItem = new ObservableBoolean(false);
    public ObservableBoolean isPromoShown = new ObservableBoolean(false);
    public ObservableBoolean isCancelledClick = new ObservableBoolean(false);

    public ObservableBoolean cashAvail = new ObservableBoolean(false);
    public ObservableBoolean cardAvail = new ObservableBoolean(false);
    public ObservableBoolean walletAvail = new ObservableBoolean(false);

    public ObservableBoolean walletClicked = new ObservableBoolean(false);
    public ObservableBoolean CashClicked = new ObservableBoolean(false);
    public ObservableBoolean CardClicked = new ObservableBoolean(false);

    public ObservableField<String> myself_contact = new ObservableField<>("");
    public ObservableField<String> myself_contact_name = new ObservableField<>("");

    public ObservableField<String> Recmnd4u = new ObservableField<>("");

    public ObservableField<String> Myself = new ObservableField<>("");
    public ObservableField<String> Cashtrans = new ObservableField<>("");
    public ObservableField<String> Cardtrans = new ObservableField<>("");
    public ObservableField<String> Wallettrans = new ObservableField<>("");
    public ObservableField<String> ApplyPromo = new ObservableField<>("");
    public ObservableField<String> PromoAplied = new ObservableField<>("");

    public ObservableField<String> SearchTitle = new ObservableField<>("Waiting For a near driver around you to accept your booking");

    public ObservableBoolean myself_trip = new ObservableBoolean(true);

    public ObservableField<String> splitdatetime = new ObservableField<>("");

    List<EtaModel> etaModels = new ArrayList<>();
    List<OffersResponseData> offersResponseData = new ArrayList<>();

    public ObservableBoolean isDriversAvailable = new ObservableBoolean(false);

    public ObservableBoolean showdropbookui = new ObservableBoolean(true);


    public ObservableBoolean show_Paymethod_UI = new ObservableBoolean(true);

    public ObservableBoolean show_Promo_UI = new ObservableBoolean(true);

    public ObservableBoolean nodriverfound = new ObservableBoolean(true);
    public ObservableBoolean alldriversbusy = new ObservableBoolean(true);

    HashMap<String, String> map = new HashMap<>();

    HashMap<String, String> driverData;
    HashMap<String, Marker> driverPin;

    public GoogleMap mGoogleMap;

    String clickedTYpeID;

    List<Type> typeList = new ArrayList<>();

    MarkerOptions markeroption = new MarkerOptions();

    BitmapDescriptor bitmapDescriptorFactory;

    public MakeTripViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;

        Recmnd4u.set(translationModel.txt_recmnd_foru);
        Myself.set("Myself");

        Cashtrans.set(translationModel.txt_cash);
        Cardtrans.set(translationModel.txt_card);
        Wallettrans.set(translationModel.txt_wallet);
        ApplyPromo.set(translationModel.txt_apply_promo);
        PromoAplied.set(translationModel.txt_apply_promo);

        FirebaseHelper.addObservers(this);
    }

    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        Log.d(TAG, "onSuccessfulApi() called with: taskId = [" + taskId + "], response = [" + response + "]");
        if (response.message.equalsIgnoreCase("promo_listed")) {
            offersResponseData.clear();
            String offersData = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            offersResponseData.addAll(CommonUtils.stringToArray(offersData, OffersResponseData[].class));
            getmNavigator().loadOffersData(offersResponseData);
            if (!Objects.requireNonNull(enteredPromoCode.get()).isEmpty())
                clickedPromoCode.set(enteredPromoCode.get());
            enteredPromoCode.set("");
            if (!Objects.requireNonNull(clickedPromoCode.get()).isEmpty()) {
                removepromocode.set(false);
                EtaApi();
            }
            if (removepromocode.get()) {
                clickedPromoCode.set("");
                EtaApi();
            }

        } else if (response.message.equalsIgnoreCase("created_request_successfully")) {
            if (response.data != null) {
                String createRequestStr = CommonUtils.ObjectToString(response.data);
                CreateRequestData model = (CreateRequestData) CommonUtils.StringToObject(createRequestStr, CreateRequestData.class);
                if (model.getIsLater()) {
                    getmNavigator().openHomePage();
                    getmNavigator().showMessage("Trip registered Successfully");
                } else {
                    REQID.set(model.getId());
                    sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, model.getId());
                    getmNavigator().openWaitingDialog();
                }
            }
        } else {
            if (isCancelledClick.get()) {
                if (!sharedPrefence.Getvalue(SharedPrefence.Rental_Request_ID).equals("")) {
                    getmNavigator().removeFrag();
                }
                getmNavigator().closeWaitingDialog(false);
            } else {
                SocketHelper.init(sharedPrefence, this, TAG, false);
                etaModels.clear();

                ArrayList<EtaModel> etaList = new ArrayList<>();
                String strJson = new Gson().toJson(response.data);
                JsonElement json = JsonParser.parseString(strJson);
                if (json.isJsonArray()) {
                    JsonArray jsonArray = json.getAsJsonArray();
                    for (int i = 0; i < jsonArray.getAsJsonArray().size(); i++) {
                        JsonElement data = jsonArray.getAsJsonArray().get(i);
                        if (data.isJsonObject()) {
                            //Log.d("xxxTAG2", "onSmsChatShow: isJsonObject=="+data.getAsJsonObject());
                            EtaModel etaModel = new Gson().fromJson(data, EtaModel.class);
                            etaList.add(etaModel);
                        } else if (data.isJsonArray()) {
                            Log.d("xxxTAG1", "onSuccessfulApi: isJsonArray ==" + data.getAsJsonArray());
                        }
                    }
                } else if (json.isJsonObject()) {
                    Log.d("xxxxneweta res", "onSuccessfulApi: isJsonObject");
                }
//                ArrayList<Object> eta=new Gson().fromJson((JsonElement) response.data,new TypeToken<ArrayList<Object>>(){
//                }.getType());
                String etaString = CommonUtils.arrayToString(etaList);

                etaModels.addAll(CommonUtils.stringToArray(etaString, EtaModel[].class));
                getmNavigator().loadNewEta(etaModels);
                if (Objects.requireNonNull(clickedPromoCode.get()).isEmpty())
                    promoStatus.set(ApplyPromo.get());
                else promoStatus.set(PromoAplied.get());
                calculateETADuration(false);
            }
        }
    }


    private void calculateETADuration(boolean refreshAdapter) {
        if (etaModels != null) {
            HashMap<String, List<Float>> distances = new HashMap<>();
            for (EtaModel etaModel : etaModels) {
                List<Float> distanceList = new ArrayList<>();
                for (String driverId : driverPin.keySet()) {
                    String driverDataStr = driverData.get(driverId);
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
            for (EtaModel etaModel : etaModels) {
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
            getmNavigator().refreshAdapter(etaModels);
        }
    }

    public void cancelApi(String rentrip_id) {
        if (getmNavigator().isNetworkConnected()) {
            isCancelledClick.set(true);
            map.clear();

            if (!rentrip_id.equals("")) {
                map.put(Constants.NetworkParameters.request_id, rentrip_id);
            } else {
                map.put(Constants.NetworkParameters.request_id, REQID.get());
            }

            RequestCancelApi(map);

            sharedPrefence.removeValue(SharedPrefence.Rental_Request_ID);

        } else getmNavigator().showNetworkMessage();
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        if (e.getMessage() != null) {
            if (e.getMessage().equalsIgnoreCase("no drivers available")) {
                nodriverfound.set(true);
                alldriversbusy.set(false);
                getmNavigator().closeWaitingDialog(false);
            } else if (e.getMessage().equalsIgnoreCase("all drivers are busy")) {
                nodriverfound.set(false);
                alldriversbusy.set(true);
                getmNavigator().closeWaitingDialog(false);
            } else {
                getmNavigator().showMessage(e);
            }
        }
        ETAClicked.set(false);
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return new HashMap<>();
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
        Log.e("Drivers===", "loadDrivers");
    }

    @Override
    public void TripStatus(String s) {

        Log.e("xxxMakeTrip", "Trip Status method called");

        BaseResponse response = CommonUtils.getSingleObject(s, BaseResponse.class);

        if (response != null) {
            Log.e("sucessMessage---", ",message---" + response.successMessage);
            if (response.successMessage.equalsIgnoreCase("trip_accepted")) {
                getmNavigator().closeWaitingDialog(true);
            } else if (response.successMessage.equalsIgnoreCase("no_driver_found")) {
                getmNavigator().actbase().runOnUiThread(() -> {
                    nodriverfound.set(true);
                    alldriversbusy.set(false);
                    getmNavigator().closeWaitingDialog(false);
                });
            } else if (response.successMessage.equalsIgnoreCase("all drivers busy")) {
                getmNavigator().actbase().runOnUiThread(() -> {
                    nodriverfound.set(false);
                    alldriversbusy.set(true);
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

    public void addLastKnownMarkers() {
        if (driverPin != null)
            for (String key : driverPin.keySet()) {
                Marker marker = driverPin.get(key);
                bitmapDescriptorFactory = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getmNavigator().getBaseAct(), R.drawable.car_marker));
                assert marker != null;
                markeroption.position(marker.getPosition()).anchor(0.5f, 0.5f).rotation(marker.getRotation()).icon(bitmapDescriptorFactory);
                Marker nMarker = mGoogleMap.addMarker(markeroption);
                driverPin.put(key, nMarker);
                assert nMarker != null;
                nMarker.setVisible(false);
            }
    }

    @SuppressLint("ObsoleteSdkInt")
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (drawable != null)
                drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public void getTypes() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getTypesApiii("" + Objects.requireNonNull(pickupLatLng.get()).latitude, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickBackImg(View v) {
        getmNavigator().onClickBackImg();
    }

    public void EtaApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            map.clear();
            map.put(Constants.NetworkParameters.pLat, "" + Objects.requireNonNull(pickupLatLng.get()).latitude);
            map.put(Constants.NetworkParameters.pLng, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);

            if (dropLatLng.get() != null) {
                map.put(Constants.NetworkParameters.dLat, "" + Objects.requireNonNull(dropLatLng.get()).latitude);
                map.put(Constants.NetworkParameters.dLng, "" + Objects.requireNonNull(dropLatLng.get()).longitude);
            }

//            map.put(Constants.NetworkParameters.vType, id);
            map.put(Constants.NetworkParameters.rType, "1");

//            if (!promoValue.get().isEmpty())
//                map.put(Constants.NetworkParameters.Promocode, promoValue.get());

            if (!Objects.requireNonNull(clickedPromoCode.get()).isEmpty())
                map.put("promo_code", clickedPromoCode.get());
            for (Map.Entry j : map.entrySet()
            ) {
                Log.i("xxxMakeTripModel", "EtaApi:    key =" + j.getKey() + ":value =" + j.getValue());
            }
            GetETAApi(map);
        } else getmNavigator().showNetworkMessage();
    }

    public void showTYpes() {

        isDriversAvailable.set(!Objects.requireNonNull(dateChoose.get()).isEmpty());

        for (String key : driverPin.keySet()) {
            Objects.requireNonNull(driverPin.get(key)).setVisible(false);
            FirebaseHelper.removeObserverFor(key);
        }

        for (String key : driverPin.keySet()) {
            String driverDetails = driverData.get(key);
            try {
                assert driverDetails != null;
                JSONObject jsonObject = new JSONObject(driverDetails);
                if (jsonObject.has("vehicle_type")) {
                    String typeId = jsonObject.getString("vehicle_type");
                    if (Objects.requireNonNull(ClickedETATypeID.get()).equalsIgnoreCase(typeId)) {
                        Marker marker = driverPin.get(key);
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

    public void onClickNext(View v) {
        getmNavigator().onClickNext(driverData, driverPin);
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
                    JSONObject jsonObject1 = jsonObject.getJSONObject("l");
                    Log.d("xxxxMakeTrip", "driverEnteredFence->564: " + jsonObject1);
                    long currentTime = new Date().getTime();
                    long diff = (currentTime - updatedAt) / 1000;
                    if (diff < (5 * 60)) {
                        // Istypedata.set(true);
                        // isProgressShown.set(false);

                        double bearing = jsonObject.getDouble("bearing");
                        if (driverPin.containsKey(key)) {
                            Marker driverPinMarker = driverPin.get(key);
                            assert driverPinMarker != null;
                            driverPinMarker.remove();

                            Log.e("dkgjkj", "egkrgg");
//                            bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                            markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
//                            Marker marker = mGoogleMap.addMarker(markeroption);

                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));

                            driverPin.put(key, marker);
                            driverData.put(key, response);
                        } else {

                            Log.e("dkgjkj", "egkrgg");

//                            bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                            markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
//                            Marker marker = mGoogleMap.addMarker(markeroption);

                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));

                            driverPin.put(key, marker);
                            driverData.put(key, response);

                            FirebaseHelper.addObserverFor(key);
                        }

                        showTYpes();
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
            if (driverPin.containsKey(key)) {
                Marker driverPinMarker = driverPin.get(key);
                assert driverPinMarker != null;
                driverPinMarker.remove();
                driverPin.remove(key);
                driverData.remove(key);

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
                    if (driverPin.containsKey(key)) {
                        Marker driverPinMarker = driverPin.get(key);
                        double bearing = jsonObject.getDouble("bearing");

                        if (driverPinMarker != null)
                            driverPinMarker.remove();
//
//                        bitmapDescriptorFactory = BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker);
//                        markeroption.position(new LatLng(location.latitude, location.longitude)).anchor(0.5f, 0.5f).rotation((float) bearing).icon(bitmapDescriptorFactory);
//                        Marker marker = mGoogleMap.addMarker(markeroption);

                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.latitude, location.longitude))
                                .title("MY Location")
                                .rotation((float) bearing)
                                .anchor(0.5f, 0.5f)
                                .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));

                        driverPin.put(key, marker);
                        driverData.put(key, response);

                        showTYpes();

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
//                            Marker marker = mGoogleMap.addMarker(markeroption);

                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title("MY Location")
                                    .rotation((float) bearing)
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));


                            driverPin.put(key, marker);
                            driverData.put(key, response);

                            FirebaseHelper.addObserverFor(key);
                            showTYpes();
                        }
                    }
                } else {
                    if (driverPin.containsKey(key)) {
                        Marker driverPinMarker = driverPin.get(key);
                        if (driverPinMarker != null)
                            driverPinMarker.remove();
                        driverPin.remove(key);
                        driverData.remove(key);

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
            if (driverPin.containsKey(key)) {
                Marker driverPinMarker = driverPin.get(key);
                if (driverPinMarker != null)
                    driverPinMarker.remove();
                driverPin.remove(key);
                driverData.remove(key);

                FirebaseHelper.removeObserverFor(key);
            }
//            Disable Book NOw button when Drives of Selected Type are UNAVAILABLE
            int driverCount = 0;
            for (String keys : driverPin.keySet()) {
                String driverDataStr = driverData.get(keys);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DriverData driverData1 = null;

    @Override
    public void driverDataUpdated(String key, String response) {
        Log.v("http_log", "driverDataUpdated:key= " + key + " : res= " + response);
        driverData1 = gson.fromJson(response, DriverData.class);

        Log.d("xxxxMakeTripModel", "driverEnteredFence:->757 " + driverData1.getL());
    }

    @Override
    public void tripStatusReceived(String response) {

    }

    public void onClickPromo(View e) {
        getmNavigator().onClickPromAlert();
    }

    public void onCLickClock(View v) {
        getmNavigator().onClickSchedule();
    }

    public void onClickBookNow(View v) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            map.clear();
            map.put(Constants.NetworkParameters.pLat, "" + Objects.requireNonNull(pickupLatLng.get()).latitude);
            map.put(Constants.NetworkParameters.pLng, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);

            if (dropLatLng.get() != null) {
                map.put(Constants.NetworkParameters.dLat, "" + Objects.requireNonNull(dropLatLng.get()).latitude);
                map.put(Constants.NetworkParameters.dLng, "" + Objects.requireNonNull(dropLatLng.get()).longitude);
            }

            if (Objects.requireNonNull(ClickedETATypeID.get()).isEmpty()) {
                setIsLoading(false);
                return;
            }

            if (myself_trip.get()) {
                map.put(Constants.NetworkParameters.Myself, "1");
            } else {
                map.put(Constants.NetworkParameters.Myself, "0");
                map.put(Constants.NetworkParameters.Myself_Contact_No, myself_contact.get());
            }

            map.put(Constants.NetworkParameters.vType, ClickedZoneTypeID.get());
            map.put(Constants.NetworkParameters.rType, "1");

            map.put(Constants.NetworkParameters.PayOpt, "" + payType.get());

            map.put(Constants.NetworkParameters.pAddress, pickupAddress.get());

            if (dropAddress.get() != null)
                map.put(Constants.NetworkParameters.dAddress, dropAddress.get());

            if (!Objects.requireNonNull(dateChoose.get()).isEmpty()) {
                map.put(Constants.NetworkParameters.isLater, "1");
                map.put(Constants.NetworkParameters.tripStartTime, dateChoose.get());
            }

            getmNavigator().opensearchsheet();

            CreateReqApicall(map);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.e("Book Ride Map", "" + "Key is: " + key + " && Value is: " + value + "\\n");
            }
        } else getmNavigator().showNetworkMessage();

    }

    public void onCLickPaymentType(View v) {
        show_Paymethod_UI.set(true);
        show_Promo_UI.set(false);
        getmNavigator().onCLickPayment();
    }

    public void onCLickPromoType(View v) {
        show_Paymethod_UI.set(false);
        show_Promo_UI.set(true);
        getmNavigator().onCLickPromo();
    }

    public void onPromoChanged(Editable e) {
        enteredPromoCode.set(e.toString());
    }

    public void onCLickApplyPromo(View v) {
        if (!Objects.requireNonNull(enteredPromoCode.get()).isEmpty()) {
            promoAppliedApi(enteredPromoCode.get());
            getmNavigator().closesheet();
        } else
            getmNavigator().showMessage("Please enter promo code");
    }

    public void onCLickCloseBottom(View v) {
        getmNavigator().onClickCloseBottom();
    }

    public void loadOffersApi() {
        if (getmNavigator().isNetworkConnected()) {
            GetPromoCodes();
        } else getmNavigator().showNetworkMessage();
    }

    public void promoAppliedApi(String code) {
        if (getmNavigator().isNetworkConnected()) {
            GetPromoCodesQuery(code);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickCash(View v) {
        paymentMethod.set(Cashtrans.get());
        payType.set(1);
        getmNavigator().closesheet();
        CashClicked.set(true);
        CardClicked.set(false);
        walletClicked.set(false);
    }

    public void onClickCard(View v) {
        paymentMethod.set(Cashtrans.get());
        payType.set(0);
        getmNavigator().closesheet();
        CashClicked.set(false);
        CardClicked.set(true);
        walletClicked.set(false);
    }

    public void onClickWallet(View v) {
        paymentMethod.set(Wallettrans.get());
        payType.set(2);
        CashClicked.set(false);
        CardClicked.set(false);
        getmNavigator().closesheet();
        walletClicked.set(true);
    }

    public void onCLickReset(View v) {
        isDateChoosen.set(false);
        dateChoose.set("");
        isDriversAvailable.set(false);
        splittedDate.set("");
        splittedTime.set("");
    }

    public PolylineOptions black_options, grey_Options;
    Polyline black_polyline, grey_polyline;
    public Route routeDest1;
    public List<LatLng> pointsDest1;

    public void drawroute() {
        if (pickupLatLng.get() != null && dropLatLng.get() != null) {
            RouteMapClient gitHubService = RootMap.getClient().create(RouteMapClient.class);
            gitHubService.GetDrawpath(Objects.requireNonNull(pickupLatLng.get()).latitude + "," + Objects.requireNonNull(pickupLatLng.get()).longitude, Objects.requireNonNull(dropLatLng.get()).latitude + "," + Objects.requireNonNull(dropLatLng.get()).longitude, Constants.PlaceApi_key)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            if (response.body() != null) {
                                routeDest1 = new Route();
                                MapUtilz.parseRoute(response.body(), routeDest1);

                                final ArrayList<Step> step = routeDest1.getListStep();
                                pointsDest1 = new ArrayList<>();
                                black_options = new PolylineOptions();
                                grey_Options = new PolylineOptions();
                                black_options.geodesic(true);
                                grey_Options.geodesic(true);


                                for (int i = 0; i < step.size(); i++) {
                                    List<LatLng> path = step.get(i).getListPoints();
                                    System.out.println("step =====> " + i + " and "
                                            + path.size());
                                    pointsDest1.addAll(path);
                                }
                                if (black_polyline != null)
                                    black_polyline.remove();
                                if (grey_polyline != null)
                                    grey_polyline.remove();

                                black_options.addAll(pointsDest1);
                                black_options.width(10f);
                                black_options.startCap(new SquareCap());
                                black_options.endCap(new SquareCap());
                                black_options.jointType(ROUND);
                                black_options.color(Color.RED);

                                //Color.parseColor("#191a23")
                                grey_Options.addAll(pointsDest1);
                                grey_Options.width(10f);
                                grey_Options.startCap(new SquareCap());
                                grey_Options.endCap(new SquareCap());
                                grey_Options.jointType(ROUND);
                                grey_Options.color(Color.BLUE);

                                try {
                                    if (mGoogleMap != null) {
                                        mGoogleMap.clear();
                                        PlaceMarker();
                                        black_polyline = mGoogleMap.addPolyline(black_options);
                                        grey_polyline = mGoogleMap.addPolyline(grey_Options);
                                        animatePolyLine();
                                    }
                                } catch (Exception e) {
                                    // e.printStackTrace();
                                    Log.e(TAG, "onResponse: Error =" + e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            getmNavigator().showMessage("Network Error");
                        }
                    });
        }
    }

    private void animatePolyLine() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animator1 -> {

            List<LatLng> latLngList = black_polyline.getPoints();
            int initialPointSize = latLngList.size();
            int animatedValue = (int) animator1.getAnimatedValue();
            int newPoints = (animatedValue * pointsDest1.size()) / 100;

            if (initialPointSize < newPoints) {
                latLngList.addAll(pointsDest1.subList(initialPointSize, newPoints));
                black_polyline.setPoints(latLngList);
            }

        });
        animator.addListener(polyLineAnimationListener);
        animator.start();
    }

    Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            //addMarker(pointsDest1.get(pointsDest1.size() - 1));
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            List<LatLng> blackLatLng = black_polyline.getPoints();
            List<LatLng> greyLatLng = grey_polyline.getPoints();

            greyLatLng.clear();
            greyLatLng.addAll(blackLatLng);
            blackLatLng.clear();

            black_polyline.setPoints(blackLatLng);
            grey_polyline.setPoints(greyLatLng);

            black_polyline.setZIndex(2);

            animator.start();
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    };

    private void addMarker(LatLng destination) {

        MarkerOptions options = new MarkerOptions()
                .position(destination)
                .draggable(false)
                .flat(false)
                .icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.drop_marker));

        if (mGoogleMap != null)
            mGoogleMap.addMarker(options);

    }

    Marker pickMarker, dropMarker;

    private void PlaceMarker() {

        if (pickMarker != null)
            pickMarker.remove();
        pickMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(Objects.requireNonNull(pickupLatLng.get()))
                .title("Pickup Location")
                .anchor(0.5f, 0.5f)
                .icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.pick_marker)));

        if (dropMarker != null)
            dropMarker.remove();

        if (dropLatLng != null)
            dropMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(Objects.requireNonNull(dropLatLng.get()))
                    .title("Drop Location")
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.drop_marker)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        assert pickMarker != null;
        builder.include(pickMarker.getPosition());
        if (dropMarker != null)
            builder.include(dropMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int width = MyApp.getmContext().getResources().getDisplayMetrics().widthPixels;
        int height = MyApp.getmContext().getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);
        int half_height = (int) (height * 0.50);
        mGoogleMap.setPadding(0, 0, 0, half_height);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.animateCamera(cu);
    }


}

