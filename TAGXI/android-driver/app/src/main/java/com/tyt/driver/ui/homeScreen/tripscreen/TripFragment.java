package com.tyt.driver.ui.homeScreen.tripscreen;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ncorti.slidetoact.SlideToActView;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.FragmentTripBinding;
import com.tyt.driver.retro.responsemodel.CancelReasonModel;
import com.tyt.driver.retro.responsemodel.Driver;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.retro.responsemodel.Request;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.feedback.FeedbackFrag;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.LocationUpdatesService;
import com.tyt.driver.utilz.OtpEditText;
import com.tyt.driver.utilz.SharedPrefence;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class TripFragment extends BaseFragment<FragmentTripBinding, TripFragViewModel> implements TripNavigator, OnMapReadyCallback, View.OnClickListener, LocationSource.OnLocationChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "TripFragment";

    // TODO: Rename and change types of parameters
    private Request mParam1;
    private Driver mParam2;
    boolean ispickDrop;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    View layoutMarkerPickup, layoutMArkerDrop;
    Marker Pickup, Drop;
    @Inject
    SharedPrefence sharedPrefence;
    public int counter;
    BaseActivity context;

    public static ProfileModel onTripReq;

//    ResizeAnimation resizeAnimation;

    @Inject
    TripFragViewModel tripFragViewModel;

    FragmentTripBinding fragmentTripBinding;
    GoogleMap mgoogleMap;
    private BottomSheetBehavior bottomSheetBehavior;

    public TripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripFragment newInstance(ProfileModel onTripRequest) {
        onTripReq = onTripRequest;
        return new TripFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getBaseAct();
        startJobLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
       /* fragmentTripBinding.tripSwipeRefLay.setOnRefreshListener(() -> {
            ((HomeAct) getBaseActivity()).ProfileRefreshAPI();
        });*/
        ((HomeAct) getActivity()).ShowHideBar(false);
        ((HomeAct) getActivity()).ShowOnloneOffline(false);
        MyApp.setInsideTrip(true);
    }

    public void stop_refresh(){
        //fragmentTripBinding.tripSwipeRefLay.setRefreshing(false);
    }

    /**
     * Initialize {@link FusedLocationProviderClient} to get location data
     **/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTripBinding = getViewDataBinding();
        tripFragViewModel.setNavigator(this);
        init_pages();

        ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_map)).getMapAsync(this);

        tripFragViewModel.setValues(onTripReq);

        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(getActivity());

        getActivity().setTitle(getBaseAct().getTranslatedString(R.string.app_name));

        bottomSheetBehavior = BottomSheetBehavior.from(fragmentTripBinding.menu.bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        fragmentTripBinding.bottomAppBar.setVisibility(View.GONE);
        if (tripFragViewModel.isTripStared.get()) {
            fragmentTripBinding.fabCallChat.setAlpha(0.0f);
            fragmentTripBinding.menu.icCallChat.setAlpha(0.0f);
            fragmentTripBinding.fabCallChat.setVisibility(View.INVISIBLE);
            fragmentTripBinding.menu.icCallChat.setVisibility(View.INVISIBLE);
        } else {
            fragmentTripBinding.fabCallChat.setVisibility(View.GONE);
            fragmentTripBinding.menu.icCallChat.setVisibility(View.VISIBLE);
        }

        fragmentTripBinding.bottomAppBar.setHideOnScroll(true);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        fragmentTripBinding.bottomAppBar.setHideOnScroll(true);
                        fragmentTripBinding.bottomAppBar.setHovered(true);
                        fragmentTripBinding.bottomAppBar.setVisibility(View.INVISIBLE);
                        if (tripFragViewModel.isTripStared.get()) {
                            fragmentTripBinding.fabCallChat.setAlpha(0.0f);
                            fragmentTripBinding.menu.icCallChat.setAlpha(0.0f);
                            fragmentTripBinding.fabCallChat.setVisibility(View.INVISIBLE);
                            fragmentTripBinding.menu.icCallChat.setVisibility(View.INVISIBLE);
                        } else {
                            fragmentTripBinding.fabCallChat.setVisibility(View.GONE);
                            fragmentTripBinding.menu.icCallChat.setVisibility(View.VISIBLE);
                        }

                        Log.d("BSB", "collapsed");
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {

                        Log.d("BSB", "settling");
                        fragmentTripBinding.bottomAppBar.setHovered(true);
                        fragmentTripBinding.bottomAppBar.setAlpha(0.3f);
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.d("BSB", "expanded");
                        fragmentTripBinding.bottomAppBar.setAlpha(1.0f);
                        fragmentTripBinding.bottomAppBar.setHideOnScroll(true);
                        fragmentTripBinding.bottomAppBar.setHovered(true);
                        fragmentTripBinding.bottomAppBar.setVisibility(View.VISIBLE);

                        if (tripFragViewModel.isTripStared.get()) {
                            fragmentTripBinding.fabCallChat.setAlpha(0.0f);
                            fragmentTripBinding.menu.icCallChat.setAlpha(0.0f);
                            fragmentTripBinding.fabCallChat.setClickable(false);
                            fragmentTripBinding.fabCallChat.setEnabled(false);
                            fragmentTripBinding.fabCallChat.setVisibility(View.INVISIBLE);
                            fragmentTripBinding.menu.icCallChat.setVisibility(View.INVISIBLE);
                        } else {
                            fragmentTripBinding.fabCallChat.setClickable(true);
                            fragmentTripBinding.fabCallChat.setEnabled(true);
                            fragmentTripBinding.menu.icCallChat.setVisibility(View.GONE);
                            fragmentTripBinding.fabCallChat.setVisibility(View.VISIBLE);
                        }

                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {

                        Log.d("BSB", "hidden");
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        Log.d("BSB", "dragging");
                        fragmentTripBinding.bottomAppBar.setHovered(true);
                        fragmentTripBinding.bottomAppBar.setAlpha(0.1f);
                        break;
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        Log.d("BSB", "STATE_HALF_EXPANDED");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        fragmentTripBinding.menu.slideMenu.setOnSlideCompleteListener(slideToActView -> {
            Log.d("xxTAG", "onSlideComplete: slideMenu");

            tripFragViewModel.ArrivedApi();
        });

        fragmentTripBinding.menu.startMenu.setOnSlideCompleteListener(slideToActView -> {
            Log.d("xxTAG", "onSlideComplete: startMenu");

            showRideotp_dialog();

            if (tripFragViewModel.isTripArrived.get()) {
                fragmentTripBinding.fabCallChat.setAlpha(0.0f);
                fragmentTripBinding.menu.icCallChat.setAlpha(0.0f);
                fragmentTripBinding.fabCallChat.setVisibility(View.INVISIBLE);
                //fabAnimation(fragmentTripBinding.menu.icCallChat);
                fragmentTripBinding.menu.icCallChat.setVisibility(View.INVISIBLE);
            }
        });

        fragmentTripBinding.menu.endMenu.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                Log.d("xxTAG", "onSlideComplete: endMenu");

               // if (tripFragViewModel.dropLocation.get() == null || tripFragViewModel.dropLocation.get().isEmpty() || tripFragViewModel.dropLatLng.get() == null) {
                    getBaseAct().runOnUiThread(() -> {
                        tripFragViewModel.mIsLoading.set(true);
                        GetCurrentLoaction();
                    });
                //}

                new Handler().postDelayed(() -> {
                    openEndtripConfiremAlert();
                }, 3000);

                if (tripFragViewModel.isTripArrived.get()) {
                    fragmentTripBinding.fabCallChat.setAlpha(0.0f);
                    fragmentTripBinding.menu.icCallChat.setAlpha(0.0f);
                    fragmentTripBinding.fabCallChat.setVisibility(View.INVISIBLE);
                    //fabAnimation(fragmentTripBinding.menu.icCallChat);
                    fragmentTripBinding.menu.icCallChat.setVisibility(View.INVISIBLE);
                }
            }
        });


        if (tripFragViewModel.sos_phone_zero.get() != null)
            fragmentTripBinding.sosPhoneZero.setLabelText(String.valueOf(tripFragViewModel.sos_phone_zero.get()));
        else fragmentTripBinding.sosPhoneZero.setVisibility(View.GONE);
        if (tripFragViewModel.sos_phone_one.get() != null)
            fragmentTripBinding.sosPhoneOne.setLabelText(String.valueOf(tripFragViewModel.sos_phone_one.get()));
        else fragmentTripBinding.sosPhoneOne.setVisibility(View.GONE);
        if (tripFragViewModel.sos_phone_two.get() != null)
            fragmentTripBinding.sosPhoneTwo.setLabelText(String.valueOf(tripFragViewModel.sos_phone_two.get()));
        else fragmentTripBinding.sosPhoneTwo.setVisibility(View.GONE);

        fragmentTripBinding.notifyAdmin.setLabelText(tripFragViewModel.sos_notify_admin.get());

        fragmentTripBinding.notifyAdmin.setOnClickListener(this);
        fragmentTripBinding.sosPhoneZero.setOnClickListener(this);
        fragmentTripBinding.sosPhoneOne.setOnClickListener(this);
        fragmentTripBinding.sosPhoneTwo.setOnClickListener(this);


    }

    Dialog rideotdialog;

    private void showRideotp_dialog() {


        //OTP=otpview.toString();
        rideotdialog = new Dialog(getBaseAct(), R.style.RIDEOTP_Theme_Dialog);

        rideotdialog.setContentView(R.layout.ride_start_with_otp_lay);

        Window window = rideotdialog.getWindow();
        if (window != null) {
            rideotdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //window.setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        }

        rideotdialog.setCancelable(false);

        rideotdialog.setCanceledOnTouchOutside(false);

        //add textview from ride otp dialog for translation in sheet

        Button submitbt = rideotdialog.findViewById(R.id.ride_otp_start_card);

        OtpEditText otpview = rideotdialog.findViewById(R.id.ride_et_otp);

        ImageView closearw = rideotdialog.findViewById(R.id.ride_cancelarrw);


        otpview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable otp) {
                try {
                    getViewModel().ride_otp.set(Integer.parseInt(otp.toString()));
                } catch (NumberFormatException e) {
                    showMessage(e.getMessage());
                    otpview.setText("");
                }
            }
        });

        closearw.setOnClickListener(view -> {
            rideotdialog.dismiss();
            resetSlider();
        });

        submitbt.setOnClickListener(view -> {
            Integer OTP = 0;

            OTP = tripFragViewModel.ride_otp.get();
            // showMessage(OTP.toString());

            if (OTP != null) {
                if (validaterideotp(OTP.toString())) {
                    //move to start Trip API
                    showMessage("Done");
                    getViewModel().ride_otp.set(OTP);
                    tripFragViewModel.StartEndAPI();
                } else {
                    showMessage("Enter full OTP");
                }
            }
        });

        rideotdialog.show();

    }

    public boolean validaterideotp(String otp) {
        boolean validotp;

        validotp = otp.length() >= 4;

        return validotp;
    }

    private void GetCurrentLoaction() {

        if (CommonUtils.isGpscheck(getActivity())) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LocationGet(location);
                } else {
                    _getLocation();
                    tripFragViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + 0.0);
                    tripFragViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + 0.0);

                    Log.e("LocationNUll--", "Null");
                }
            });
        } else CommonUtils.ShowGpsDialog(getActivity());
    }

    private void _getLocation() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                /*.addOnConnectionFailedListener(this)*/
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        // Get the location manager
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        LocationGet(location);
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void LocationGet(Location location) {
        //tripFragViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + location.getLatitude());
        //tripFragViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + location.getLongitude());
        getBaseAct().runOnUiThread(() -> {
            tripFragViewModel.dropLocation.set(getAddressfromlatlng(new LatLng(location.getLatitude(), location.getLongitude())));
        });
        tripFragViewModel.dropLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public String getAddressfromlatlng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        String addrs = "";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            addrs = addresses.get(0).getAddressLine(0);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xxxMapFrag", "LocationGet: Error ==> " + e.getMessage());
        }
        return addrs;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {
                String json = intent.getExtras().getString(Constants.EXTRA_Data);
                if (json.equalsIgnoreCase("Trip cancelled")) {

                }
            }
        }
    };

    public void startJobLocationUpdate() {
        LocalBroadcastManager.getInstance(getBaseActivity()).registerReceiver(
                locationBroadReceiver, new IntentFilter(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE));
        if (!CommonUtils.isMyServiceRunning(getBaseActivity(), LocationUpdatesService.class)) {
            ((HomeAct) getActivity()).startLocationUpdate();
        }
    }

    BroadcastReceiver locationBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("xxxkeys", "On New Locaiton Received");
            String json = intent.getStringExtra(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);


            Log.e("xxxTripFrag", "LAT:" + intent.getStringExtra(Constants.IntentExtras.TRIP_LAT) + "|| LAN:" + intent.getStringExtra(Constants.IntentExtras.TRIP_LNG) + "||BEARER:" +
                    intent.getStringExtra(Constants.IntentExtras.TRIP_BEARING));

            tripFragViewModel.sendTripLocation(json, sharedPrefence.Getvalue(SharedPrefence.lat), sharedPrefence.Getvalue(SharedPrefence.lng), sharedPrefence.Getvalue(SharedPrefence.BEARING));

//            if (tripFragViewModel != null && intent.getStringExtra(Constants.IntentExtras.TRIP_LAT) != null
//                    && intent.getStringExtra(Constants.IntentExtras.TRIP_LNG) != null
//                    && intent.getStringExtra(Constants.IntentExtras.TRIP_BEARING) != null) {
//                Log.e("tripLOcation---", "insideMethod--");
//
//            }
        }
    };


    /**
     * {@link BroadcastReceiver} for receiving current status of the trip. They are
     * Bill generated, Trip Started, Driver arrived
     **/
    private BroadcastReceiver receiverTripStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


        }
    };

    private void fabAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1);
        anim.setFillBefore(true);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setDuration(300);
        anim.setInterpolator(new BounceInterpolator());
        view.startAnimation(anim);
    }


    @Override
    public TripFragViewModel getViewModel() {
        return tripFragViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_trip;
    }

    /**
     * Called to animate {@link ImageView} near trip status text
     *
     * @param v View that needs to be animated
     **/
    private void blink_animation(View v) {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        v.startAnimation(animation);
    }

    public void init_pages() {
        load_Headerpage1();
    }

    public void load_Headerpage1() {

//        fragmentTripBinding.lytHeaderV1.animate().alpha(1.0f);
//        fragmentTripBinding.lytHeaderV2.animate().alpha(0.0f);
//        resizeAnimation = new ResizeAnimation(fragmentTripBinding.spsRight, 00);
//        resizeAnimation.setDuration(20);
//        fragmentTripBinding.spsRight.startAnimation(resizeAnimation);
//        resizeAnimation = new ResizeAnimation(fragmentTripBinding.spsLeft, 80);
//        resizeAnimation.setDuration(20);
//        fragmentTripBinding.spsLeft.startAnimation(resizeAnimation);
    }

    public void load_Headerpage2() {
//        fragmentTripBinding.lytHeaderV2.animate().alpha(1.0f);
//        fragmentTripBinding.lytHeaderV1.animate().alpha(0.0f);
//        resizeAnimation = new ResizeAnimation(fragmentTripBinding.spsLeft, 00);
//        resizeAnimation.setDuration(20);
//        fragmentTripBinding.spsLeft.startAnimation(resizeAnimation);
//        resizeAnimation = new ResizeAnimation(fragmentTripBinding.spsRight, 80);
//        resizeAnimation.setDuration(20);
//        fragmentTripBinding.spsRight.startAnimation(resizeAnimplation);

    }


    /**
     * Returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return getBaseActivity() != null ? getBaseActivity() : (BaseActivity) getActivity();
    }

    @Override
    public void openHomePage() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }


    @Override
    public void openEndtripConfiremAlert() {
        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setTitle(getBaseAct().getTranslatedString(R.string.txt_trip_end_title))
                .setCancelable(false)
                .setMessage(getBaseAct().getTranslatedString(R.string.txt_sure_start_trip))
                .setPositiveButton(getBaseAct().getTranslatedString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tripFragViewModel.endTripApi();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getBaseAct().getTranslatedString(R.string.txt_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tripFragViewModel.setIsLoading(false);
                        fragmentTripBinding.menu.endMenu.resetSlider();
                    }
                })
                .create();
        if (!ad.isShowing())
            ad.show();
    }

    @Override
    public void resetSlider() {
        fragmentTripBinding.tripCancel.setEnabled(true);
        fragmentTripBinding.tripCancel.setClickable(true);
        if (tripFragViewModel.isTripStared.get())
            fragmentTripBinding.menu.endMenu.resetSlider();
        else if (tripFragViewModel.isTripArrived.get())
            fragmentTripBinding.menu.startMenu.resetSlider();
        else fragmentTripBinding.menu.slideMenu.resetSlider();
    }

    @Override
    public String getItemPosition() {
        return cancelReasonAdapter != null && cancelReasonAdapter.getSelectPosition() != null ? cancelReasonAdapter.getSelectPosition() : "";
    }


    /**
     * This callback is called when {@link GoogleMap} loading was complete
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("xxxTripFrag", "onMapReady: ");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setPadding(0, 250, 0, 490);
        this.mgoogleMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e("xxxMapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("xxxMapsActivityRaw", "Can't find style.", e);
        }

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.uber_map));
        googleMap.setTrafficEnabled(true);

        mgoogleMap.setOnMapLoadedCallback(() -> {
//                changeMapStyle();
            tripFragViewModel.isMapRendered.set(true);
            tripFragViewModel.buildGoogleApiClient(mgoogleMap);

            if (tripFragViewModel.dropLatLng.get() != null && tripFragViewModel.dropLocation.get() != null && !tripFragViewModel.dropLocation.get().isEmpty()) {
                tripFragViewModel.drawtriproute();
            }

        });

    }


    @Override
    public void openGoogleMap(double latitude, double longitude) {
        if (!isGoogleMapsInstalled(getActivity()))
            showMessage(getBaseAct().getTranslatedString(R.string.txt_maps_not_available));
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latitude + "," + longitude));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // tripFragViewModel.DisconnectSocket();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverTripStatus);

        tripFragViewModel.isMapRendered.set(false);
    }

    /**
     * Callback to receive result from previous screen
     *
     * @param requestCode Code of the request
     * @param resultCode  Code of the result
     * @param data        {@link Intent} with data from previous screen
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    Dialog cancelReasonDialog;
    EditText editText;
    CancelReasonAdapter cancelReasonAdapter;

    @Override
    public void cancelConfiremAlert(List<CancelReasonModel> reasonModels) {

        reasonModels.add(new CancelReasonModel(0, "Others"));

        if (cancelReasonDialog != null)
            if (!cancelReasonDialog.isShowing()) {
                cancelReasonDialog.show();
                return;
            }

        cancelReasonDialog = new Dialog(getActivity());
        cancelReasonDialog.setContentView(R.layout.cancel_reason_dialog);
      /*  if (cancelReasonDialog.getWindow() != null) {
            cancelReasonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/

        TextView tTitle = cancelReasonDialog.findViewById(R.id.choose_reason);
        TextView tConfirm = cancelReasonDialog.findViewById(R.id.txt_confirm);
        editText = cancelReasonDialog.findViewById(R.id.cancel_reason_edit);

        tTitle.setText(getBaseAct().getTranslatedString(R.string.txt_choose_reason));
        tConfirm.setText(getBaseAct().getTranslatedString(R.string.txt_confirm));
        editText.setHint(getBaseAct().getTranslatedString(R.string.txt_raise_reason));

        RelativeLayout relativeLayout = cancelReasonDialog.findViewById(R.id.confirm);
        RecyclerView recyclerView = cancelReasonDialog.findViewById(R.id.recycle);
        cancelReasonAdapter = new CancelReasonAdapter(getActivity(), reasonModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cancelReasonAdapter);

        relativeLayout.setOnClickListener(v -> {
            tripFragViewModel.reasonText.set(editText.getText().toString());
            tripFragViewModel.cancel();
        });

        cancelReasonDialog.show();
    }

    public void onCancel(View v) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            v.setClickable(true);
            v.setEnabled(true);
        }, 3000);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void getCurrLoc() {
        GetCurrentLoaction();
    }

    @Override
    public String getaddressfromlatlng(LatLng latLng) {
        return getAddressfromlatlng(latLng);
    }

    @Override
    public void closerideotpdialog() {
        showMessage("OTP Verified..!");
        new Handler().postDelayed(() -> {
            if (rideotdialog != null && rideotdialog.isShowing()) {
                rideotdialog.dismiss();
            }
        }, 100);
    }

    @Override
    public void selectedReason(boolean clickedStatus) {
        if (clickedStatus)
            editText.setVisibility(View.VISIBLE);
        else {
            if (!editText.getText().toString().isEmpty())
                editText.setText("");
            editText.setVisibility(View.GONE);
        }
    }

    @Override
    public void openBillFeedBack(EndTripData response) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, FeedbackFrag.newInstance(response), FeedbackFrag.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void onOpenChatAndCall(String ph) {

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, InAppChatFragment.newInstance(ph), InAppChatFragment.TAG)
                .commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sos_phone_zero) {
            new Handler().postDelayed(() -> fragmentTripBinding.sosPhoneZero.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            fragmentTripBinding.sosMenu.close(true);
            onSosClick(v, fragmentTripBinding.sosPhoneZero.getLabelText());

        } else if (v.getId() == R.id.sos_phone_one) {
            new Handler().postDelayed(() -> fragmentTripBinding.sosPhoneOne.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            fragmentTripBinding.sosMenu.close(true);
            onSosClick(v, fragmentTripBinding.sosPhoneOne.getLabelText());

        } else if (v.getId() == R.id.sos_phone_two) {
            new Handler().postDelayed(() -> fragmentTripBinding.sosPhoneTwo.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            fragmentTripBinding.sosMenu.close(true);
            onSosClick(v, fragmentTripBinding.sosPhoneTwo.getLabelText());
        } else if (v.getId() == R.id.notify_Admin) {
            new Handler().postDelayed(() -> {
                fragmentTripBinding.sosMenu.close(true);
                UpdateSOSAdminFireBase();
            },300);
        }
    }

    private void UpdateSOSAdminFireBase() {
        showMessage("Please Wait...");

        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("SOS").child(Objects.requireNonNull(tripFragViewModel.ReqID.get()));

        HashMap<String,Object> admin_sos_map = new HashMap<>();
        admin_sos_map.put("req_id",tripFragViewModel.ReqID.get());
        admin_sos_map.put("is_user",0);
        admin_sos_map.put("is_driver", 1);
        admin_sos_map.put("serv_loc_id",tripFragViewModel.Serv_Loc_ID.get());
        admin_sos_map.put("updated_at",System.currentTimeMillis());

        mydb.setValue(admin_sos_map).addOnSuccessListener(task -> {
            showMessage("Notified to Admin Successfully...");
        }).addOnFailureListener(e -> {
            showMessage(e.getMessage());
        });

    }

    /**
     * SOS Call phone {@link Intent} to make phone call to user when call sos button is clicked
     **/
    public void onSosClick(View view, String phone_number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone_number));
        view.getContext().startActivity(callIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
       /* Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);

        if (address!=null)
            tripFragViewModel.dropLocation.set(address);*/
    }
}