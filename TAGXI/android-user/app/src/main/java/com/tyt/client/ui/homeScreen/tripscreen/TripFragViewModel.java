package com.tyt.client.ui.homeScreen.tripscreen;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.FragmentTripBinding;
import com.tyt.client.retro.GitHubMapService;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.retro.responsemodel.Route;
import com.tyt.client.retro.responsemodel.Step;
import com.tyt.client.retro.responsemodel.TranslationModel;
import com.tyt.client.retro.responsemodel.tripRequest.TripRequestData;
import com.tyt.client.ui.base.BaseView;
import com.tyt.client.ui.homeScreen.makeTrip.RouteUtils.RootMap;
import com.tyt.client.ui.homeScreen.makeTrip.RouteUtils.RouteMapClient;
import com.tyt.client.ui.homeScreen.tripscreen.directionhelpers.FetchURL;
import com.tyt.client.ui.homeScreen.tripscreen.directionhelpers.TaskLoadedCallback;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.FirebaseHelper;
import com.tyt.client.utilz.MapUtilz;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.SocketHelper;
import com.tyt.client.utilz.exception.CustomException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.model.JointType.ROUND;

/**
 * Created by Mahi in 2021.
 */

public class TripFragViewModel extends BaseNetwork<BaseResponse, TripNavigator> implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, SocketHelper.SocketListener, FirebaseHelper.FirebaseObserver, TaskLoadedCallback {
    private static final String TAG = "TripFragViewModel";
    HashMap<String, String> hashMap = new HashMap<>();
    SharedPrefence sharedPrefence;
    GitHubMapService gitHubMapService;
    private GoogleApiClient mGoogleApiClient;
    Marker marker;
    //  Socket mSocket;
    private final PolylineOptions options = new PolylineOptions();
    GoogleMap mGoogleMap;
    private GoogleMap googleMap;
    public PolylineOptions lineOptionsDest1, lineOptionDesDark;
    Polyline polyLineDest1, polyLineDestDark;
    public Route routeDest1;

    public ObservableField<String> userName, lastName, profileurl, car_number, car_model, car_color, car_make, tripOTP,
            StatusofTrip, Distance, paymenttype, pickupLocation, dropLocation, waitingtime, userPhone, ReqId, tripStatus, reasonText, sos_phone_one, sos_phone_two, sos_phone_zero, sos_notify_admin, sos_text;
    public ObservableField<String> userRating, Serv_Loc_ID;
    public ObservableBoolean isTrpStatusShown, isDriverArrived, isTripStared, isShare, isPromodone, dropcheck, isExpCollpClicked, isArrowClicked, isDropLocationFound;
    public Gson gson, gsoncustom;
    public List<LatLng> pointsDest1;
    public ObservableBoolean isMapRendered = new ObservableBoolean(false);
    public ObservableBoolean contactsListed = new ObservableBoolean(false);
    public ObservableField<String> ride_otp = new ObservableField<>("Start Your Ride With : ****");
    private int graceTime;

    ObservableField<LatLng> driverLatLng, pickupLatLng, dropLatLng;
    public ObservableField<Long> lats = new ObservableField<Long>();
    public ObservableField<Long> lons = new ObservableField<Long>();
    public List<CancelReasonModel> reasonModels = new ArrayList<>();

    public TripFragViewModel(GitHubService gitHubService,
                             SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        //  this.mSocket = socket;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;

        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }

        userName = new ObservableField<>();
        car_number = new ObservableField<>();
        car_model = new ObservableField<>();
        lastName = new ObservableField<>();
        Distance = new ObservableField<>();
        ReqId = new ObservableField<>();
        lats = new ObservableField<>();
        lons = new ObservableField<>();
        sos_phone_zero = new ObservableField<>();
        sos_phone_one = new ObservableField<>();
        sos_phone_two = new ObservableField<>();
        sos_notify_admin = new ObservableField<>();
        Serv_Loc_ID = new ObservableField<>();
        sos_text = new ObservableField<>();
        isPromodone = new ObservableBoolean(true);
        isDropLocationFound = new ObservableBoolean(false);
        isDriverArrived = new ObservableBoolean(false);
        isTripStared = new ObservableBoolean(false);

        pickupLatLng = new ObservableField<>();
        dropLatLng = new ObservableField<>();
        pickupLocation = new ObservableField<>("");
        dropLocation = new ObservableField<>("");
        car_make = new ObservableField<>("");


        reasonText = new ObservableField<>("");
        tripStatus = new ObservableField<>("Driver Accepted");

        isTrpStatusShown = new ObservableBoolean(false);
        dropcheck = new ObservableBoolean(false);
        paymenttype = new ObservableField<>();
        profileurl = new ObservableField<>();
        userRating = new ObservableField<>("0");
        car_color = new ObservableField<>("");
        StatusofTrip = new ObservableField<>();
        tripOTP = new ObservableField<>("");
        userPhone = new ObservableField<>("");
        waitingtime = new ObservableField<>("0:0");

        driverLatLng = new ObservableField<>();
        isShare = new ObservableBoolean();
        isMapRendered.set(false);
        isExpCollpClicked = new ObservableBoolean(false);
        isArrowClicked = new ObservableBoolean(false);

        sos_notify_admin.set(translationModel.txt_notify_admin);

        SocketHelper.init(sharedPrefence, this, TAG, true);
        FirebaseHelper.init(sharedPrefence, this, true);

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

    @BindingAdapter("app:tripDriverImg")
    public static void loadtripDriverImg(ImageView imageView, String carimgurl) {
        if (carimgurl != null) {
            Glide.with(imageView.getContext()).load(Uri.parse(carimgurl)).into(imageView);
        }
    }


    public void setValues(ProfileModel onTripRequest, FragmentTripBinding fragmentTripBinding) {

        TripRequestData data = onTripRequest.getOnTripRequest().getData();

        Log.d("xxxTripViewModelTAG", "json: " + data.toString());
        ReqId.set(data.getId());
        Serv_Loc_ID.set(data.getService_location_id());
        sharedPrefence.savevalue(SharedPrefence.USER_ID, String.valueOf(data.getUserId()));
        sharedPrefence.savevalue(SharedPrefence.REQUEST_ID, data.getId());

        if (data.getRide_otp() != null && !data.getRide_otp().toString().isEmpty()) {
            ride_otp.set("Start Your Ride With OTP : " + data.getRide_otp().toString());
        }

        if (data.getDriverDetail() != null) {
            userPhone.set(data.getDriverDetail().getData().getMobile());
            userName.set(data.getDriverDetail().getData().getName());

            userRating.set(String.valueOf(data.getDriverDetail().getData().getRating()));

            if (data.getDriverDetail().getData().getProfilePicture() != null)
                profileurl.set(data.getDriverDetail().getData().getProfilePicture());

            car_make.set(data.getDriverDetail().getData().getCarMakeName() + "(" + data.getDriverDetail().getData().getVehicleTypeName() + ")");
            car_color.set(data.getDriverDetail().getData().getCarColor());
            car_model.set(data.getDriverDetail().getData().getCarModelName());
            car_number.set(data.getDriverDetail().getData().getCarNumber());
        }
        isDriverArrived.set(data.getIsDriverArrived() == 1);
        isTripStared.set(data.getIsTripStart() == 1);

//        if (!isDriverArrived.get()) {

        pickupLocation.set(data.getPickAddress());
        if (data.getDropAddress() != null) {
            dropLocation.set(data.getDropAddress());
            isDropLocationFound.set(true);
        } else {
            isDropLocationFound.set(false);
        }
        Log.d("xxxTripViewModelTAG", "setValues: getPickAddress =" + data.getPickAddress());
        Log.d("xxxTripViewModelTAG", "setValues: getDropAddress =" + data.getDropAddress());

//        }
        if (data.getPickLat() != null && data.getPickLng() != null)
            pickupLatLng.set(new LatLng(data.getPickLat(), data.getPickLng()));

        if (data.getDropLat() != null && data.getDropLng() != null)
            dropLatLng.set(new LatLng(data.getDropLat(), data.getDropLng()));

        if (isTripStared.get()) {
            contactsListed.set(false);
            isArrowClicked.set(false);
            tripStatus.set("Trip Started");
        } else if (isDriverArrived.get()) {
            tripStatus.set("Driver Arrived");
            contactsListed.set(true);
        } else {
            contactsListed.set(true);
        }
        Log.d(TAG, "setValues: " + onTripRequest.getMobile());
        if (onTripRequest.getSosPhone() != null) {
            for (int i = 0; i < onTripRequest.getSosPhone().getSosDataList().size(); i++) {
                if (onTripRequest.getSosPhone().getSosDataList().get(i).getNumber() != null) {
                    switch (i) {
                        case 0:
                            sos_phone_zero.set(onTripRequest.getSosPhone().getSosDataList().get(i).getNumber());
                            break;
                        case 1:
                            sos_phone_one.set(onTripRequest.getSosPhone().getSosDataList().get(i).getNumber());
                            break;
                        case 2:
                            sos_phone_two.set(onTripRequest.getSosPhone().getSosDataList().get(i).getNumber());
                            break;
                    }
                }
            }
        }
        FirebaseHelper.addTripObserverFor("" + data.getId());
    }


    @Override
    public void TripStatus(String s) {
        Log.e("TripStatus---", "string---" + s);
        long lat = sharedPrefence.getLong(SharedPrefence.DriverLat);
        long lon = sharedPrefence.getLong(SharedPrefence.DriverLong);
        Log.d("xxxtripModel", "TripStatus: ");
        lats.set(lat);
        lons.set(lon);

        BaseResponse response = CommonUtils.getSingleObject(s, BaseResponse.class);

        assert response != null;
        Log.e("xxsucessMessage---", ",message---" + response.successMessage);
        if (response.successMessage.equalsIgnoreCase("trip_accepted")) {

            tripStatus.set("Driver Accepted");
            isDriverArrived.set(false);
            isTripStared.set(false);

        } else if (response.successMessage.equalsIgnoreCase("driver_arrived")) {
            Log.d("xxTAG", "TripStatus: driver_arrived");

            isDriverArrived.set(true);
            tripStatus.set("Driver Arrived");

        } else if (response.successMessage.equalsIgnoreCase("driver_started_the_trip")) {
            Log.d("xxTAG", "TripStatus: driver_started_the_trip");

            isTripStared.set(true);
            tripStatus.set("Trip Started");

            contactsListed.set(false);
            isArrowClicked.set(false);

        } else if (response.successMessage.equalsIgnoreCase("driver_end_the_trip")) {
            Log.d("xxTAG", "TripStatus: driver_end_the_trip");

            isTripStared.set(true);

            tripStatus.set("Trip Ended");

            getmNavigator().openFeedback(s);

        } else getmNavigator().openHomePage();

    }

    @Override
    public void tripStatusReceived(String response) {
        Log.d("xxxTripget_cars", "tripStatusReceived: response==>" + response);
        BaseResponse baseResponse = null;
        JSONObject data = null;
        try {
            data = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("xxxTripget_cars", "Error =>" + e.getMessage());
        }

        if (data != null) {
            Log.d("xxxTripget_cars", "" + data.toString());
            baseResponse = gson.fromJson(data.toString(), BaseResponse.class);

            Distance.set(CommonUtils.doubleDecimalFromat(baseResponse.getDistancee()));

            if (markerPickup == null) {
                if (getmNavigator().getbaseAct() != null && mGoogleMap != null)
                    markerPickup = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Objects.requireNonNull(pickupLatLng.get()).latitude, Objects.requireNonNull(pickupLatLng.get()).longitude)).title("Pickup Point")
                            .icon(CommonUtils.getBitmapDescriptor(getmNavigator().getbaseAct(), R.drawable.pick_marker)));

            }

            if (markerDrop == null) {
                if (getmNavigator().getbaseAct() != null && mGoogleMap != null && dropLatLng.get() != null)
                    if (isDriverArrived.get() && Objects.requireNonNull(dropLatLng.get()).latitude != 0.0 && Objects.requireNonNull(dropLatLng.get()).longitude != 0.0 && getmNavigator().getbaseAct() != null) {
                        markerDrop = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Objects.requireNonNull(dropLatLng.get()).latitude, Objects.requireNonNull(dropLatLng.get()).longitude)).title("Drop Point").icon(CommonUtils.getBitmapDescriptor(getmNavigator().getbaseAct(), R.drawable.drop_marker)));
                    }
            }


            if (mGoogleMap != null) {
                if (marker != null) {
                    if (baseResponse.getLat() != null && baseResponse.getLng() != null)
                        marker.setPosition(new LatLng(baseResponse.getLat(), baseResponse.getLng()));
                    marker.setRotation((float) baseResponse.getBearing());
                    marker.setAnchor(0.5f, 0.5f);
                } else {
                    if (baseResponse.getLat() != null && baseResponse.getLng() != null)
                        marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(baseResponse.getLat(), baseResponse.getLng())).title("Driver Point").icon(CommonUtils.getBitmapDescriptor(MyApp.getmContext(), R.drawable.ic_carmarker)));
                }
                if (baseResponse.getLat() != null && baseResponse.getLng() != null) {
                    CameraPosition newCamPos = new CameraPosition(new LatLng(baseResponse.getLat(), baseResponse.getLng()),
                            15.5f,
                            mGoogleMap.getCameraPosition().tilt, //use old tilt
                            mGoogleMap.getCameraPosition().bearing);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 15, null);
                }
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
        Log.d(TAG, "onSuccessfulApi: message=" + response.message);
        if (response.success) {
            if (response.message.equalsIgnoreCase("cancellation_reasons_listed")) {
                reasonModels.clear();
                String reasonResp = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                reasonModels.addAll(CommonUtils.stringToArray(reasonResp, CancelReasonModel[].class));
                getmNavigator().cancelConfiremAlert(reasonModels);
            } else
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
        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Trip already cancelled")) {

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
    public void onClickCancel(View v) {
        if (getmNavigator().isNetworkConnected()) {
            userCancelReasonApi(isDriverArrived.get() ? "after" : "before");
        } else getmNavigator().showNetworkMessage();
    }

    public void cancelApi() {
        if (getmNavigator().isNetworkConnected()) {
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, ReqId.get());

            if (getmNavigator().getItemPosition().isEmpty() || getmNavigator().getItemPosition() == null) {
                getmNavigator().showMessage("Choose Reasons");
                return;
            }

            if (getmNavigator().getItemPosition().equalsIgnoreCase("0")) {
                if (!CommonUtils.IsEmpty(reasonText.get())) {
                    hashMap.put(Constants.NetworkParameters.cancel_other_reason, reasonText.get());
                    setIsLoading(true);
                    userCancelApi(hashMap);
                } else
                    getmNavigator().showMessage(getmNavigator().getbaseAct().getTranslatedString(R.string.txt_pls_enter_reason));
            } else {
                hashMap.put(Constants.NetworkParameters.reason, getmNavigator().getItemPosition());
                setIsLoading(true);
                userCancelApi(hashMap);
            }
        } else getmNavigator().showNetworkMessage();

    }

    /**
     * Call phone {@link Intent} to make phone call to diver when call button is clicked
     **/
    public void OnclickCall(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + userPhone.get()));
        view.getContext().startActivity(callIntent);
    }

    /**
     * Opens compose message screen with driver's phone number on it via {@link Intent} when sms button is clicked
     **/
    public void OnclickSms(View view) {
        if (!isTripStared.get()) {
            getmNavigator().onOpenChatAndCall(userPhone.get());
        } else {
            view.setAlpha(0.5f);
            getmNavigator().getbaseAct().runOnUiThread(() -> {
                Snackbar.make(view.getRootView().getRootView(), "Cant Do Chat with Driver", Snackbar.LENGTH_SHORT);
            });
        }

    }


    private long lastUpdatedDriverLocation = 0;

    /**
     * Draws route between pickup & drop locations using Google Directions API
     *
     * @param pick Pickup {@link LatLng}
     * @param drop Drop {@link LatLng}
     **/
    private void DrawPathCurrentToHero(final boolean clearMap, LatLng pick, LatLng drop) {

        /*gitHubMapService.GetDrawpath(getUrl(pick.latitude, pick.longitude, drop.latitude, drop.longitude), false, Constants.PlaceApi_key).enqueue(new Callback<JsonObject>() {
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
                    lineOptionsDest1.jointType(JointType.ROUND);


                    try {
                        if (lineOptionsDest1 != null && mGoogleMap != null) {
                            if (clearMap) {
                                mGoogleMap.clear();
                                marker = null;
//                                if (driverLatLng.get().latitude != 0 && driverLatLng.get().longitude != 0) {
//                                    if (marker == null)
//                                        marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(driverLatLng.get().latitude, driverLatLng.get().longitude)).title("Driver Point"));
//                                    else {
//                                        marker.setPosition(new LatLng(driverLatLng.get().latitude , driverLatLng.get().longitude));
//                                        marker.setAnchor(0.5f, 0.5f);
//                                    }
//                                }


                                if (lats.get() != null && lats.get() != 0 && lons.get() != null && lons.get() != 0) {
                                    if (marker == null)
                                        marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lats.get(), lons.get())).title("Driver Point"));
                                    else {
                                        marker.setPosition(new LatLng(lats.get(), lons.get()));
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

    private String getUrl(double latitude, double longitude, double latitude1, double longitude1) {
        // Origin of route
        String str_origin = "origin=" + latitude + "," + longitude;
        // Destination of route
        String str_dest = "destination=" + latitude1 + "," + longitude1;
        // Mode
        String mode = "mode=" + "driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;

        // Building the url to the web service
        String url = parameters;
        return url;
    }


    /*
     * url for mapping with two places*/
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = parameters + "&key=" + getmNavigator().getbaseAct().getResources().getString(R.string.google_api_key);
        return url;
    }

    Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

            addMarker(pointsDest1.get(pointsDest1.size() - 1));
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.d("xxxTripModel", "onAnimationEnd: ");
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

    /**
     * Initiates {@link GoogleApiClient}
     **/
    public void buildGoogleApiClient(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (getmNavigator().getbaseAct() != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getmNavigator().getbaseAct())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API).build();

            mGoogleApiClient.connect();
        }
        if (pickupLatLng.get() != null && Objects.requireNonNull(pickupLatLng.get()).latitude != 0.0 && pickupLatLng.get() != null && pickupLatLng.get().longitude != 0.0) {
            boundLatLang();
        }
    }

    public PolylineOptions triplineOptionsDest1, triplineOptionDesDark;
    Polyline trippolyLineDest1, trippolyLineDestDark;
    public Route triprouteDest1;
    public List<LatLng> trippointsDest1;

    public void drawtriproute() {
        RouteMapClient gitHubService = RootMap.getClient().create(RouteMapClient.class);
        gitHubService.GetDrawpath(pickupLatLng.get().latitude + "," + pickupLatLng.get().longitude, dropLatLng.get().latitude + "," + dropLatLng.get().longitude, Constants.PlaceApi_key)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.body() != null) {
                            triprouteDest1 = new Route();
                            MapUtilz.parseRoute(response.body(), triprouteDest1);

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
                            triplineOptionsDest1.color(Color.BLUE);


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

    Marker markerPickup, markerDrop, markerDriver;

    /**
     * Adjusts {@link GoogleMap} zoom to fit pickup and drop location
     **/
    private void boundLatLang() {
        if (mGoogleMap == null)
            return;
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        if (getmNavigator().getbaseAct() != null) {


            if (pickupLatLng.get() != null)
                markerPickup = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Objects.requireNonNull(pickupLatLng.get()).latitude, pickupLatLng.get().longitude)).title("Pickup Point")
                        .anchor(0.5f, 0.5f).icon(CommonUtils.getBitmapDescriptor(getmNavigator().getbaseAct(), R.drawable.pick_marker)));

            if (dropLatLng.get() != null)
                if (isDriverArrived.get() && Objects.requireNonNull(dropLatLng.get()).latitude != 0.0 && dropLatLng.get().longitude != 0.0 && getmNavigator().getbaseAct() != null) {
                    markerDrop = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(dropLatLng.get().latitude, dropLatLng.get().longitude)).title("Drop Point").anchor(0.5f, 0.5f).icon(CommonUtils.getBitmapDescriptor(getmNavigator().getbaseAct(), R.drawable.drop_marker)));
                    bld.include(new LatLng(Objects.requireNonNull(dropLatLng.get()).latitude, Objects.requireNonNull(dropLatLng.get()).longitude));
                }
            if (pickupLatLng.get() != null)
                bld.include(new LatLng(Objects.requireNonNull(pickupLatLng.get()).latitude, Objects.requireNonNull(pickupLatLng.get()).longitude));

            if (driverLatLng != null && driverLatLng.get() != null)
                if (Objects.requireNonNull(driverLatLng.get()).latitude != 0.0 && Objects.requireNonNull(driverLatLng.get()).longitude != 0.0)
                    bld.include(new LatLng(Objects.requireNonNull(driverLatLng.get()).latitude, Objects.requireNonNull(driverLatLng.get()).longitude));

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    bld.build(), 17));
        }
    }


    /**
     * {@link Socket} connection successful callback
     **/
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * Reverse geo-coding: Gets {@link android.location.Location} from given address string
     *
     * @param place Address that needs to be converted into {@link android.location.Location}
     **/
    private void getLocationFromAddress(final String place) {
        LatLng loc = null;
        Geocoder gCoder = new Geocoder(getmNavigator().getbaseAct());
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
                        getmNavigator().showMessage(getmNavigator().getbaseAct().getTranslatedString(R.string.txt_try_again));
                    }
                    setIsLoading(false);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    setIsLoading(false);
                    getmNavigator().showMessage(getmNavigator().getbaseAct().getTranslatedString(R.string.txt_try_again));
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
        this.googleMap = googleMap;
        this.mGoogleMap = googleMap;
        //getCurrLocation();
    }

    @Override
    public void onTaskDone1(Object... values) {
        if (googleMap != null) {
            if (polyLineDest1 != null)
                polyLineDest1.remove();
            polyLineDest1 = googleMap.addPolyline((PolylineOptions) values[0]);
        } else if (mGoogleMap != null) {
            if (polyLineDest1 != null)
                polyLineDest1.remove();
            polyLineDest1 = mGoogleMap.addPolyline((PolylineOptions) values[0]);
        }

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
    public void onLoadDrivers() {

    }

    @Override
    public void ApprovalStatus(String toString) {

    }

    @Override
    public void ReceivedChatStatus(String toString) {
        Log.d(TAG, "ReceivedChatStatus: ");
    }

    @Override
    public void driverEnteredFence(String key, GeoLocation location, String response) {

    }

    @Override
    public void driverExitedFence(String key, String response) {

    }

    @Override
    public void driverMovesInFence(String key, GeoLocation location, String response) {

    }

    @Override
    public void driverWentOffline(String key) {

    }

    @Override
    public void driverDataUpdated(String key, String response) {

    }

    public void onClickArrow(View v) {
        isArrowClicked.set(!isArrowClicked.get());
    }

    public void onClickCarInfo(View v) {
        getmNavigator().openCarInfoDialog();
    }

    public void onlickShare(View v) {
        getmNavigator().onClickShare(ReqId.get());
    }

}
