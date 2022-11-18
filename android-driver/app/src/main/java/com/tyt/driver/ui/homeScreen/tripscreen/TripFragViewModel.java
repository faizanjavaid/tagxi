package com.tyt.driver.ui.homeScreen.tripscreen;

import android.Manifest;
import android.animation.Animator;
import android.animation.TimeAnimator;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableFloat;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.retro.responsemodel.CancelReasonModel;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.SOS;
import com.tyt.driver.retro.responsemodel.Step;
import com.tyt.driver.retro.responsemodel.TranslationModel;

import com.tyt.driver.R;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubMapService;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.responsemodel.Car;
import com.tyt.driver.retro.responsemodel.Driver;
import com.tyt.driver.retro.responsemodel.Request;
import com.tyt.driver.retro.responsemodel.Route;
import com.tyt.driver.retro.responsemodel.tripRequest.TripRequestData;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.Maputilz;
import com.tyt.driver.utilz.RootMap;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.google.android.gms.maps.model.JointType.ROUND;

/**
 * Created by root on 12/21/17.
 */

public class TripFragViewModel extends BaseNetwork<BaseResponse, TripNavigator> implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, SocketHelper.SocketListener, LocationListener {
    private static final String TAG = "TripFragViewModel";
    HashMap<String, String> hashMap = new HashMap<>();
    SharedPrefence sharedPrefence;
    Request request;
    GitHubMapService gitHubMapService;
    Driver driver;
    private GoogleApiClient mGoogleApiClient;
    Marker marker;
    //  Socket mSocket;
    private PolylineOptions options = new PolylineOptions();
    GoogleMap mGoogleMap;
    public PolylineOptions lineOptionsDest1, lineOptionDesDark;
    Polyline polyLineDest1, polyLineDestDark;
    public Route routeDest1;
    List<Car> cars;
    boolean isPickupDrop;
    public ObservableField<String> userName, lastName, profileurl, car_number, car_model, car_color, tripOTP, reasonText,
            StatusofTrip, Distance, paymenttype, pickupLocation, dropLocation, waitingtime, userPhone, pickupLocationTittle, dropLocationTittle, sos_phone_one, sos_phone_two, sos_phone_zero, sos_notify_admin, sos_text;
    public ObservableField<String> ReqID, user_ID, tripStatus, Serv_Loc_ID;
    public ObservableBoolean isTrpStatusShown, isTripArrived, isTripStared, isShare, isPromodone, dropcheck, isExpCollpClicked, isDropLocationFound;
    public Gson gson, gsoncustom;
    public List<LatLng> pointsDest1;
    public ObservableFloat userRating;
    public ObservableBoolean isMapRendered = new ObservableBoolean(false);
    public ObservableBoolean contactsListed = new ObservableBoolean(false);
    public ObservableBoolean isArrowClicked = new ObservableBoolean(false);
    private int graceTime;
    private GoogleMap googleMap;
    ObservableField<LatLng> driverLatLng, pickupLatLng, dropLatLng;

    LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 1000;

    public List<CancelReasonModel> reasonModels = new ArrayList<>();

    private long lastUpdatedTime;

    private String distanceTravelled = "0.00";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("drivers");

    public ObservableField<Integer> ride_otp = new ObservableField<>(0);


    public TripFragViewModel(GitHubService gitHubService,
                             SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.hashMap = hashMap;
        this.gitHubMapService = gitHubMapService;
        //  this.mSocket = socket;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;


        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }

        userName = new ObservableField<>();
        isDropLocationFound = new ObservableBoolean(false);
        car_number = new ObservableField<>();
        car_model = new ObservableField<>();
        lastName = new ObservableField<>();
        Distance = new ObservableField<>();
        ReqID = new ObservableField<>();
        user_ID = new ObservableField<>();
        userPhone = new ObservableField<>();
        sos_phone_zero = new ObservableField<>();
        sos_phone_one = new ObservableField<>();
        sos_phone_two = new ObservableField<>();
        Serv_Loc_ID=new ObservableField<>();
        sos_notify_admin = new ObservableField<>();
        sos_text = new ObservableField<>();
        tripStatus = new ObservableField<>("");
        isPromodone = new ObservableBoolean(true);

        isTripArrived = new ObservableBoolean(false);
        isTripStared = new ObservableBoolean(false);

        pickupLatLng = new ObservableField<>();
        dropLatLng = new ObservableField<>();
        pickupLocation = new ObservableField<>("");
        dropLocation = new ObservableField<>("");
        pickupLocationTittle = new ObservableField<>(translationModel.txt_pickup_tittle);
        dropLocationTittle = new ObservableField<>(translationModel.txt_dropup_tittle);

        isTrpStatusShown = new ObservableBoolean(false);
        dropcheck = new ObservableBoolean(false);
        paymenttype = new ObservableField<>();
        profileurl = new ObservableField<>();
        userRating = new ObservableFloat(0);
        car_color = new ObservableField<>("");
        StatusofTrip = new ObservableField<>();
        tripOTP = new ObservableField<>("");
        reasonText = new ObservableField<>("");
        waitingtime = new ObservableField<>("0:0");

        driverLatLng = new ObservableField<>();
        isShare = new ObservableBoolean();
        isMapRendered.set(false);
        isExpCollpClicked = new ObservableBoolean(false);

        sos_notify_admin.set(translationModel.txt_notify_admin);

        SocketHelper.init(sharedPrefence, this, TAG, true);
        lastUpdatedTime = System.currentTimeMillis();
        createLocationRequest();

    }

    @BindingAdapter("app:tripUserImg")
    public static void loadtripUserImg(ImageView imageView, String carimgurl) {
        if (carimgurl != null) {
            Glide.with(imageView.getContext()).load(Uri.parse(carimgurl)).into(imageView);
        }
        Log.d("Car Img", "loadCarImg: " + carimgurl);
    }

    public void setValues(ProfileModel onTripRequest) {
//        tripStatus.set(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_way_to_pick));
        Log.d("xxxTripViewModelTAG", "setValues: " + translationModel.toString());
        Log.d("xxxTripViewModelTAG", "onTripRequest :" + new Gson().toJson(onTripRequest));

        TripRequestData data = onTripRequest.getOnTripRequest().getData();
        if (data != null) {
            tripStatus.set("");
            tripStatus.set(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_way_to_pick));
            ReqID.set(data.getId());
            Serv_Loc_ID.set(data.getService_location_id());
            user_ID.set("" + data.getUserDetail().getUserDetailData().getId());
            userPhone.set(data.getUserDetail().getUserDetailData().getMobile());
            sharedPrefence.savevalue(SharedPrefence.USER_ID, user_ID.get());
            sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, ReqID.get());
            userName.set(data.getUserDetail().getUserDetailData().getName());
            if (data.getUserDetail().getUserDetailData().getRating() != null)
                userRating.set(Float.parseFloat(data.getUserDetail().getUserDetailData().getRating()));

            if (data.getUserDetail().getUserDetailData().getProfilePicture() != null)
                profileurl.set(data.getUserDetail().getUserDetailData().getProfilePicture());

            isTripArrived.set(data.getIsDriverArrived() == 1);
            isTripStared.set(data.getIsTripStart() == 1);

//            if (!isTripArrived.get()){
            if (data.getPickAddress() != null && data.getDropAddress() != null) {
                Log.d("xxxTripViewModelTAG", "setValues: getPickAddress =" + data.getPickAddress());
                Log.d("xxxTripViewModelTAG", "setValues: getDropAddress =" + data.getDropAddress());
            }
            if (data.getPickAddress() != null) {
                pickupLocation.set(data.getPickAddress());
            }

            if (data.getDropAddress() != null) {
                dropLocation.set(data.getDropAddress());
                isDropLocationFound.set(true);
            } else {
                isDropLocationFound.set(false);
            }

//            }

            if (data.getPickLat() != null && data.getPickLng() != null)
                pickupLatLng.set(new LatLng(data.getPickLat(), data.getPickLng()));

            if (data.getDropLat() != null && data.getDropLng() != null)
                dropLatLng.set(new LatLng(data.getDropLat(), data.getDropLng()));

            if (isTripStared.get()) {
//            Log.d(TAG, "setValues: true"+getmNavigator().getBaseAct().getTranslatedString(R.string.txt_way_to_drop));
                Log.d(TAG, "setValues: true" + translationModel.txt_way_to_drop_user);
                contactsListed.set(false);
                isArrowClicked.set(false);
//            tripStatus.set(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_way_to_drop));
                tripStatus.set(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_way_to_drop_user));
                sharedPrefence.savevalue(SharedPrefence.TRIP_START, "1");
            } else {
                Log.d(TAG, "setValues:else " + translationModel.txt_way_to_pick);
//            tripStatus.set(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_reached_user));
                tripStatus.set(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_way_to_pick));
                contactsListed.set(true);
                sharedPrefence.savevalue(SharedPrefence.TRIP_START, "0");
            }

            if (data.getUserDetail().getUserDetailData() != null
                    && data.getUserDetail().getUserDetailData() != null
                    && data.getUserDetail().getUserDetailData().getSosPhone() != null
            ) {
                List<SOS.SOSData> sosDataList = data.getUserDetail().getUserDetailData().getSosPhone().getSosDataList();
                for (int i = 0; i < sosDataList.size(); i++) {
                    if (sosDataList.get(i).getNumber() != null) {
                        switch (i) {
                            case 0:
                                sos_phone_zero.set(sosDataList.get(i).getNumber());
                                break;
                            case 1:
                                sos_phone_one.set(sosDataList.get(i).getNumber());
                                break;
                            case 2:
                                sos_phone_two.set(sosDataList.get(i).getNumber());
                                break;
                        }
                    }

                }
            } else {
                sos_phone_zero.set("Add Phone Number");
            }

        }
    }


    /**
     * Callback for successful API calls
     *
     * @param taskId   Id of the API task
     * @param response {@link BaseResponse} data model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        Log.d(TAG, "onSuccessfulApi: " + response.message);
        if (response.message.equalsIgnoreCase("driver_arrived")) {
            Log.d(TAG, "onSuccessfulApi: driver_arrived =" + translationModel.txt_reached_user);
            isTripArrived.set(true);
            sharedPrefence.savevalue(SharedPrefence.TRIP_ARRIVED, "1");
            sharedPrefence.savevalue(SharedPrefence.TRIP_START, "0");
            SocketHelper.updateTripStartandArrive(3);
            tripStatus.set(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_reached_user));
            boundLatLang();
        } else if (response.message.equalsIgnoreCase("driver_trip_started")) {
            Log.d(TAG, "onSuccessfulApi: driver_trip_started =" + translationModel.txt_way_to_drop);
            isTripArrived.set(true);
            isTripStared.set(true);
            SocketHelper.updateTripStartandArrive(4);
            sharedPrefence.savevalue(SharedPrefence.TRIP_START, "1");


            getmNavigator().closerideotpdialog();

//            tripStatus.set(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_way_to_drop));
            tripStatus.set(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_way_to_drop));
            isArrowClicked.set(false);
            contactsListed.set(false);
            boundLatLang();
        } else if (response.message.equalsIgnoreCase("request_ended")) {
            Log.d("xxxTripModel", "onSuccessfulApi: request_ended =open bill");
            SocketHelper.updateTripStartandArrive(2);
            sharedPrefence.savevalue(SharedPrefence.TRIP_START, "0");
            sharedPrefence.savevalue(SharedPrefence.TRIP_ARRIVED, "0");
            sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, "");

            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("is_available", true);
            ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);
            Log.i("xxxTripModel", "onSuccessfulApi: EndTripData ===>>" + gson.toJson(response.data));
//            EndTripData data = gson.fromJson(gson.toJson(response.data), EndTripData.class);
            EndTripData data = CommonUtils.getSingleObject(new Gson().toJson(response.data), EndTripData.class);
            if (data != null) {
                if (data.getRequestBill() != null) {
                    if (data.getRequestBill().getData() != null)
                        Log.e("xxxTripModel", "getBaseDistance :" + data.getRequestBill().getData().getBaseDistance());

                }
                if (data.getDropAddress() != null)
                    Log.e("xxxTripModel", "getDropAddress : " + data.getDropAddress());
                if (data.getPickAddress() != null)
                    Log.e("xxxTripModel", "getPickAddress : " + data.getPickAddress());
                if (data.getDropLat() != null)
                    Log.e("xxxTripModel", "getDropLat : " + data.getDropLat());
                if (data.getDropLng() != null)
                    Log.e("xxxTripModel", "getDropLng : " + data.getDropLng());

            }
            getmNavigator().openBillFeedBack(data);
            //getmNavigator().openHomePage();
        } else if (response.message.equalsIgnoreCase("cancellation_reasons_listed")) {
            Log.d(TAG, "onSuccessfulApi: cancellation_reasons_listed");
            reasonModels.clear();
            String reasonResp = CommonUtils.arrayToString((ArrayList<Object>) response.data);
            reasonModels.addAll(CommonUtils.stringToArray(reasonResp, CancelReasonModel[].class));
            getmNavigator().cancelConfiremAlert(reasonModels);
        } else {
            Log.d(TAG, "onSuccessfulApi: else other");
            SocketHelper.updateTripStartandArrive(1);
            sharedPrefence.savevalue(SharedPrefence.TRIP_START, "0");
            sharedPrefence.savevalue(SharedPrefence.TRIP_ARRIVED, "0");
            sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, "");

            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("is_available", true);
            ref.child(sharedPrefence.Getvalue(SharedPrefence.ID)).updateChildren(statusMap);

            getmNavigator().openHomePage();
        }
    }

    /**
     * Callback for failed API calls
     *
     * @param taskId Id of the API task
     * @param e      Exception msg
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e);

        getmNavigator().resetSlider();

        assert e.getMessage() != null;
        if (e.getMessage().equalsIgnoreCase("Trip already cancelled")) {
            Log.d("xxxTripviewModel", "onFailureApi: ");
        }
    }

    /**
     * Returns {@link HashMap} with query parameters for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        hashMap.put(Constants.NetworkParameters.client_id, sharedPrefence.getCompanyID());
        hashMap.put(Constants.NetworkParameters.client_token, sharedPrefence.getCompanyToken());
        return hashMap;
    }


    /**
     * This method calls cancel API after confirming the cancellation process
     **/
    public void cancelApi() {
    }

    /**
     * Call phone {@link Intent} to make phone call to diver when call button is clicked
     **/
    public void OnclickCall(View view) {
        getmNavigator().onOpenChatAndCall(userPhone.get());

//        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//        callIntent.setData(Uri.parse("tel:" + userPhone.get()));
//        view.getContext().startActivity(callIntent);
    }

    /**
     * Opens compose message screen with driver's phone number on it via {@link Intent} when sms button is clicked
     **/
    public void OnclickSms(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + userPhone.get()));
        view.getContext().startActivity(sendIntent);
    }


    private long lastUpdatedDriverLocation = 0;

    /**
     * Draws route between pickup & drop locations using Google Directions API
     *
     * @param pick Pickup {@link LatLng}
     * @param drop Drop {@link LatLng}
     **/
    private void DrawPathCurrentToHero(final boolean clearMap, LatLng pick, LatLng drop) {

        /*gitHubMapService.GetDrawpath(pick.latitude + "," + pick.longitude, drop.latitude + "," + drop.longitude
                , false, Constants.PlaceApi_key).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {
                    routeDest1 = new Route();
                    CommonUtils.parseRoute(response.body(), routeDest1);

                    final ArrayList<Step> step = routeDest1.getListStep();
                    System.out.println("step size=====> " + step.size());
                    pointsDest1 = new ArrayList<LatLng>();
                    lineOptionsDest1 = new PolylineOptions();
                    lineOptionDesDark = new PolylineOptions();

                    lineOptionsDest1.geodesic(true);
                    lineOptionDesDark.geodesic(true);

                    for (int i = 0; i < step.size(); i++) {
                        List<LatLng> path = step.get(i).getListPoints();
                        System.out.println("step =====> " + i + " and "
                                + path.size());
                        pointsDest1.addAll(path);
                    }
                    if (polyLineDest1 != null)
                        polyLineDest1.remove();
                    lineOptionsDest1.addAll(pointsDest1);
                    lineOptionsDest1.width(8);
                    lineOptionsDest1.startCap(new SquareCap());
                    lineOptionsDest1.endCap(new SquareCap());
                    lineOptionsDest1.jointType(ROUND);


                    try {
                        if (lineOptionsDest1 != null && mGoogleMap != null) {
                            if (clearMap) {
                                mGoogleMap.clear();
                                marker = null;
                                if (driver.latitude != 0 && driver.longitude != 0) {
                                    if (marker == null)
                                        marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(driver.latitude, driver.longitude)).title("Driver Point"));
                                    else {
                                        marker.setPosition(new LatLng(driver.latitude, driver.longitude));
                                        marker.setAnchor(0.5f, 0.5f);
                                    }
                                }
                            }
                            polyLineDest1 = mGoogleMap.addPolyline(lineOptionsDest1);
                            polyLineDestDark = mGoogleMap.addPolyline(lineOptionsDest1);
                            boundLatLang();
                            //animatePolyLine();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });*/


    }

    Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

            addMarker(pointsDest1.get(pointsDest1.size() - 1));
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            List<LatLng> blackLatLng = polyLineDest1.getPoints();
            List<LatLng> greyLatLng = polyLineDestDark.getPoints();

            greyLatLng.clear();
            greyLatLng.addAll(blackLatLng);
            blackLatLng.clear();

            polyLineDest1.setPoints(blackLatLng);
            polyLineDestDark.setPoints(greyLatLng);

            polyLineDest1.setZIndex(2);

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
        MarkerOptions options = new MarkerOptions();
        options.position(destination);
    }
    /* not used */

    /**
     * Initiates {@link GoogleApiClient}
     **/
    public void buildGoogleApiClient(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (getmNavigator().getBaseAct() != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getmNavigator().getBaseAct())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API).build();

            mGoogleApiClient.connect();
        }
        if (Objects.requireNonNull(pickupLatLng.get()).latitude != 0.0 && Objects.requireNonNull(pickupLatLng.get()).longitude != 0.0) {
            boundLatLang();
        }
    }

    Marker markerPickup, markerDrop, markerDriver;

    /**
     * Adjusts {@link GoogleMap} zoom to fit pickup and drop location
     **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void boundLatLang() {
        if (mGoogleMap == null)
            return;
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        if (getmNavigator().getBaseAct() != null)
            markerPickup = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Objects.requireNonNull(pickupLatLng.get()).latitude, Objects.requireNonNull(pickupLatLng.get()).longitude)).title(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_pick_point))
                    .icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.pick_marker, Constants.Pick_Marker)));

        if (dropLatLng.get() != null)
            if (isTripArrived.get() && Objects.requireNonNull(dropLatLng.get()).latitude != 0.0 && Objects.requireNonNull(dropLatLng.get()).longitude != 0.0 && getmNavigator().getBaseAct() != null) {
                markerDrop = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Objects.requireNonNull(dropLatLng.get()).latitude, Objects.requireNonNull(dropLatLng.get()).longitude)).title(getmNavigator().getBaseAct().getLocalizedString(translationModel.txt_drop_point)).icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.drop_marker, Constants.Drop_Marker)));
                bld.include(new LatLng(Objects.requireNonNull(dropLatLng.get()).latitude, Objects.requireNonNull(dropLatLng.get()).longitude));
            }

        bld.include(new LatLng(Objects.requireNonNull(pickupLatLng.get()).latitude, Objects.requireNonNull(pickupLatLng.get()).longitude));

        if (driverLatLng.get() != null)
            if (Objects.requireNonNull(driverLatLng.get()).latitude != 0.0 && Objects.requireNonNull(driverLatLng.get()).longitude != 0.0)
                bld.include(new LatLng(Objects.requireNonNull(driverLatLng.get()).latitude, Objects.requireNonNull(driverLatLng.get()).longitude));

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                bld.build(), 20));
    }

    /**
     * {@link Socket} connection successful callback
     **/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /* if (driver.latitude != 0 && driver.longitude != 0) {
            if (marker == null)
                marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(driver.latitude, driver.longitude)).title("Driver Point"));
            else {
                marker.setPosition(new LatLng(driver.latitude, driver.longitude));
                marker.setAnchor(0.5f, 0.5f);
            }
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public JSONArray locationArray = new JSONArray();
    JSONObject object = new JSONObject();
    JSONObject objectParent = new JSONObject();

    // Send the location to the socket for tracing the driver.
    public void sendSocketLocationMessage(LatLng latLng) {

        lastUpdatedTime = System.currentTimeMillis();

        try {
            object.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
            objectParent.put(Constants.NetworkParameters.LAT, latLng.latitude);
            objectParent.put(Constants.NetworkParameters.LNG, latLng.longitude);
            object.put(Constants.NetworkParameters.LAT, latLng.latitude);
            object.put(Constants.NetworkParameters.LNG, latLng.longitude);
            object.put(Constants.NetworkParameters.BEARING, Float.valueOf(sharedPrefence.Getvalue(SharedPrefence.BEARING)));
            object.put(Constants.NetworkParameters.TRIP_START, isTripStared.get() ? "1" : "0");
            object.put(Constants.NetworkParameters.TRIP_ARRIVED, isTripArrived.get() ? "1" : "0");
            object.put(Constants.NetworkParameters.USER_ID, user_ID.get());
            object.put(Constants.NetworkParameters.request_id, sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID));
//            if (isTripStared.get())
//                object.put(Constants.NetworkParameters.WAITING_TIME, Minutes + "");
//            else object.put(Constants.NetworkParameters.BEFORE_WAITING_TIME, Minutes + "");

            //  if (System.currentTimeMillis() - lastUpdatedTime > 3000) {
            if (SocketHelper.isSocketConnected()) {
                object.put(Constants.NetworkParameters.WAITING_TIME, 0 + "");
                if (isTripStared.get())
                    locationArray.put(objectParent);
                object.put(Constants.NetworkParameters.LAT_LNG_ARRAY, locationArray);
                Log.d("keysTrip", "Size of B4--Array:" + locationArray.length());
//                if (MyApp.isInsideTrip())
                SocketHelper.setTripLocation(object.toString(), "1");
                locationArray = new JSONArray();

                lastUpdatedTime = System.currentTimeMillis();
            } else if (isTripStared.get()) {
                locationArray.put(objectParent);
            }
            //   }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Send the trip location to the socket for tracing the driver.
    public void sendTripLocation(String stringJson, String lat, String lng, String bearing) {
        Log.d("xxxTripmodel", "sendTripLocation() called with: stringJson = [" + stringJson + "], lat = [" + lat + "], lng = [" + lng + "], bearing = [" + bearing + "]");

        lastUpdatedTime = System.currentTimeMillis();

        try {
            object.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
            objectParent.put(Constants.NetworkParameters.LAT, lat);
            objectParent.put(Constants.NetworkParameters.LNG, lng);
            object.put(Constants.NetworkParameters.LAT, lat);
            object.put(Constants.NetworkParameters.LNG, lat);
            object.put(Constants.NetworkParameters.BEARING, Float.valueOf(sharedPrefence.Getvalue(SharedPrefence.BEARING)));
            object.put(Constants.NetworkParameters.TRIP_START, isTripStared.get() ? "1" : "0");
            object.put(Constants.NetworkParameters.TRIP_ARRIVED, isTripArrived.get() ? "1" : "0");
            object.put(Constants.NetworkParameters.USER_ID, user_ID.get());
            object.put(Constants.NetworkParameters.request_id, sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID));
//            if (isTripStared.get())
//                object.put(Constants.NetworkParameters.WAITING_TIME, Minutes + "");
//            else object.put(Constants.NetworkParameters.BEFORE_WAITING_TIME, Minutes + "");

            //  if (System.currentTimeMillis() - lastUpdatedTime > 3000) {
            if (SocketHelper.isSocketConnected()) {
                object.put(Constants.NetworkParameters.WAITING_TIME, 0 + "");
                if (isTripStared.get())
                    locationArray.put(objectParent);
                object.put(Constants.NetworkParameters.LAT_LNG_ARRAY, locationArray);

//                if (MyApp.isInsideTrip())
                SocketHelper.setTripLocation(object.toString(), "1");
                locationArray = new JSONArray();

                lastUpdatedTime = System.currentTimeMillis();
            } else if (isTripStared.get()) {
                locationArray.put(objectParent);
            }
            //   }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xxxTripmodel", "sendTripLocation: " + e.getMessage());
        }

    }

    /**
     * Reverse geo-coding: Gets {@link android.location.Location} from given address string
     *
     * @param place Address that needs to be converted into {@link android.location.Location}
     **/
    private void getLocationFromAddress(final String place) {
        LatLng loc = null;
        Geocoder gCoder = new Geocoder(getmNavigator().getBaseAct());
        final List<Address> list;
        try {
            list = gCoder.getFromLocationName(place, 1);
            if (list != null && list.size() > 0) {
                loc = new LatLng(list.get(0).getLatitude(), list.get(0)
                        .getLongitude());
                // updateLocation(place, loc.latitude, loc.longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setIsLoading(true);
            gitHubMapService.GetLatLngFromAddress(place, false, Constants.PlaceApi_key).enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getAsJsonArray("results") != null && response.body().getAsJsonArray("results").size() != 0) {
                            Double lat = response.body().getAsJsonArray("results").get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsDouble();
                            Double lng = response.body().getAsJsonArray("results").get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsDouble();
                            // updateLocation(place, lat, lng);
                        }

                    } else {
                        Log.d(TAG, "GetAddressFromLatlng" + response.toString());
                        getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_try_again));
                    }
                    setIsLoading(false);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    setIsLoading(false);
                    getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_try_again));
                    Log.d(TAG, "GetAddressFromLatlng" + t.toString());
                }
            });
        }

    }

    /**
     * Callback for {@link GoogleMap} loading complete
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("xxxTripFragModel", "onMapReady: ");
        this.googleMap = googleMap;

        //getCurrLocation();
    }

    private void getCurrLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MyApp.getmContext());

        if (ActivityCompat.checkSelfPermission(MyApp.getmContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApp.getmContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    if (markerDriver != null)
                        markerDriver.remove();

                    markerDriver = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_my_location))
                            .anchor(0.5f, 0.5f)
                            .icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker, Constants.Map_Marker)));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 12));

                }
            }
        });
    }


    /**
     * Returns true/false based on the internet connection
     **/
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
        Log.d("xxxTripModel", "onCreateRequest: ");
    }

    @Override
    public void RequestHandler(String toString) {
        Log.e("requestCancel----", "__" + toString);
        sharedPrefence.savevalue(SharedPrefence.TRIP_START, "0");
        sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, "");
        getmNavigator().openHomePage();
    }

    @Override
    public void updateTripDistance(double v) {
        Log.e("xxxDistance----", "distance---" + v);
        distanceTravelled = "" + v;
        Distance.set(CommonUtils.doubleDecimalFromat(v));
    }

    @Override
    public void ApprovalStatus(String toString) {

    }

    @Override
    public void ReceivedChatStatus(String toString) {
        Log.d("--TripFrViewModTAG", "ReceivedChatStatus: " + toString);
    }

    Location locationNew, locationOld;

    @Override
    public void onLocationChanged(Location location) {
        Log.e("locationhangess--", "changes---" + location.getLatitude());

        locationNew = location;

        if (locationOld == null)
            locationOld = locationNew;

        float bearing = locationOld.bearingTo(locationNew);

        sharedPrefence.savevalue(SharedPrefence.BEARING, "" + bearing);

        if (markerDriver != null)
            markerDriver.remove();

        driverLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));

        //dropLocation.set(getmNavigator().getaddressfromlatlng(new LatLng(location.getLatitude(), location.getLongitude())));
//        if (isTripStared.get())
//            dropLocation.set(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
//        else
//            pickupLocation.set(getCompleteAddressString(location.getLatitude(), location.getLongitude()));

        animationMarker(new LatLng(location.getLatitude(), location.getLongitude()));

        if (System.currentTimeMillis() - lastUpdatedTime > 5000) {
            sendSocketLocationMessage(driverLatLng.get());
        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(MyApp.getmContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("vkejvk", "My Current loction address" + strReturnedAddress.toString());
            } else {
                Log.e("vkejvk", "My Current loction address");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("vkejvk", "My Current loction address" + "Canont get Address!");
        }
        return strAdd;
    }

    public void animationMarker(LatLng latLng) {

        if (markerDriver != null)
            markerDriver.remove();
        if (latLng != null && mGoogleMap != null) {
            Log.d("xxxTripModel", "animationMarker() called with: latLng = [" + latLng + "]" + "|| Bear =" + sharedPrefence.Getvalue(SharedPrefence.BEARING));
            markerDriver = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                    .rotation(TextUtils.isEmpty(sharedPrefence.Getvalue(SharedPrefence.BEARING)) ? 0f :
                            Float.parseFloat(sharedPrefence.Getvalue(SharedPrefence.BEARING))).title("Driver Point").icon(CommonUtils.getBitmapDescriptor(getmNavigator().getBaseAct(), R.drawable.ic_carmarker, Constants.Map_Marker)));

            // markerDriver.setFlat(true);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));


        }
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(MyApp.getmContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        if (ActivityCompat.checkSelfPermission(MyApp.getmContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApp.getmContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(MyApp.getmContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(final LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }


    /**
     * Click listener for expand/collapse arrow
     **/
    public void onExpCollpClick(View v) {
        if (!isExpCollpClicked.get())
            isExpCollpClicked.set(true);
        else if (isExpCollpClicked.get())
            isExpCollpClicked.set(false);
    }

    public void ArrivedApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, ReqID.get());
            DriverArrivedApi(hashMap);
        } else
            getmNavigator().showNetworkMessage();
    }

    public void StartEndAPI() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            if (isTripStared.get()) {
                getmNavigator().openEndtripConfiremAlert();
            } else {
                hashMap.clear();
                hashMap.put(Constants.NetworkParameters.request_id, ReqID.get());
                hashMap.put(Constants.NetworkParameters.Pick_lat, "" + Objects.requireNonNull(pickupLatLng.get()).latitude);
                hashMap.put(Constants.NetworkParameters.Pick_lng, "" + Objects.requireNonNull(pickupLatLng.get()).longitude);
                hashMap.put(Constants.NetworkParameters.Pick_Address, pickupLocation.get());
                hashMap.put(Constants.NetworkParameters.RideOTP, Objects.requireNonNull(ride_otp.get()).toString());
                DriverStartedApi(hashMap);
            }
        } else {
            setIsLoading(true);
            getmNavigator().showNetworkMessage();
        }
    }

    public void endTripApi() {
        if (getmNavigator().isNetworkConnected()) {
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, ReqID.get());

            hashMap.put(Constants.NetworkParameters.Drop_lat, "" + Objects.requireNonNull(dropLatLng.get()).latitude);
            hashMap.put(Constants.NetworkParameters.Drop_lng, "" + Objects.requireNonNull(dropLatLng.get()).longitude);

            hashMap.put(Constants.NetworkParameters.Drop_Address, dropLocation.get());
            hashMap.put(Constants.NetworkParameters.Distance, Distance.get());
            hashMap.put(Constants.NetworkParameters.BeforeArrival, "0");
            hashMap.put(Constants.NetworkParameters.AfterArrival, "0");
            DriverEndApi(hashMap);
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.e("End Trip Map", "" + "Key is: " + key + " && Value is: " + value + "\\n");
            }
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickCancel(View v) {
        getmNavigator().onCancel(v);
        if (getmNavigator().isNetworkConnected()) {
            v.setClickable(false);
            v.setEnabled(false);
            DriverCancelReasonApi(isTripArrived.get() ? "after" : "before");
        } else getmNavigator().showNetworkMessage();
    }

    public void cancel() {
        if (getmNavigator().isNetworkConnected()) {
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, ReqID.get());

            if (getmNavigator().getItemPosition().isEmpty() || getmNavigator().getItemPosition() == null || getmNavigator().getItemPosition().equalsIgnoreCase("")) {
                getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_choose_reason));
                return;
            }

            if (getmNavigator().getItemPosition().equalsIgnoreCase("0")) {
                if (!CommonUtils.IsEmpty(reasonText.get())) {
                    hashMap.put(Constants.NetworkParameters.cancel_other_reason, reasonText.get());
                    setIsLoading(true);
                    DriverCancelApi(hashMap);
                } else
                    getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_pls_enter_reason));
            } else {
                hashMap.put(Constants.NetworkParameters.reason, getmNavigator().getItemPosition());
                setIsLoading(true);
                DriverCancelApi(hashMap);
            }
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickArrow(View v) {
        isArrowClicked.set(!isArrowClicked.get());
    }

    public void onClickDirection(View v) {
        if (isTripStared.get()) {
            if (dropLatLng.get() != null && Objects.requireNonNull(dropLatLng.get()).latitude != 0.0 && Objects.requireNonNull(dropLatLng.get()).longitude != 0.0)
                getmNavigator().openGoogleMap(Objects.requireNonNull(dropLatLng.get()).latitude, Objects.requireNonNull(dropLatLng.get()).longitude);
        } else {
            if (pickupLatLng.get() != null && Objects.requireNonNull(pickupLatLng.get()).latitude != 0.0 && Objects.requireNonNull(pickupLatLng.get()).longitude != 0.0)
                getmNavigator().openGoogleMap(Objects.requireNonNull(pickupLatLng.get()).latitude, Objects.requireNonNull(pickupLatLng.get()).longitude);
        }
    }

    public PolylineOptions triplineOptionsDest1, triplineOptionDesDark;
    Polyline trippolyLineDest1, trippolyLineDestDark;
    public Route triprouteDest1;
    public List<LatLng> trippointsDest1;

    public void drawtriproute() {
        Maputilz.RouteMapClient gitHubService = RootMap.getClient().create(Maputilz.RouteMapClient.class);
        gitHubService.GetDrawpath(pickupLatLng.get().latitude + "," + pickupLatLng.get().longitude, dropLatLng.get().latitude + "," + dropLatLng.get().longitude, Constants.PlaceApi_key)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.body() != null) {
                            triprouteDest1 = new Route();
                            Maputilz.parseRoute(response.body(), triprouteDest1);

                            final ArrayList<Step> step = triprouteDest1.getListStep();
                            trippointsDest1 = new ArrayList<LatLng>();
                            triplineOptionsDest1 = new PolylineOptions();
                            triplineOptionDesDark = new PolylineOptions();
                            triplineOptionsDest1.geodesic(true);
                            triplineOptionDesDark.geodesic(true);


                            for (int i = 0; i < step.size(); i++) {
                                List<LatLng> path = step.get(i).getListPoints();
                                System.out.println("step =====> " + i + " and "
                                        + path.size());
                                trippointsDest1.addAll(path);
                            }
                            if (trippolyLineDest1 != null)
                                trippolyLineDest1.remove();
                            triplineOptionsDest1.addAll(trippointsDest1);
                            triplineOptionsDest1.width(8);
                            triplineOptionsDest1.startCap(new SquareCap());
                            triplineOptionsDest1.endCap(new SquareCap());
                            triplineOptionsDest1.jointType(ROUND);
                            triplineOptionsDest1.color(Color.RED);


                            try {
                                if (mGoogleMap != null) {
                                    mGoogleMap.clear();
                                    boundLatLang();
                                    trippolyLineDest1 = mGoogleMap.addPolyline(triplineOptionsDest1);
                                    trippolyLineDestDark = mGoogleMap.addPolyline(triplineOptionsDest1);

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


    public void notifyadminclick(View view) {
        getmNavigator().showMessage("Notify Admin Clicked");
    }

}
