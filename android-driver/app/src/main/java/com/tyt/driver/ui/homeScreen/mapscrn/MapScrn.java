package com.tyt.driver.ui.homeScreen.mapscrn;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.databinding.FragmentMapScrnBinding;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;

import com.tyt.driver.ui.homeScreen.earnings.EarningsFrag;
import com.tyt.driver.ui.homeScreen.instantRide.InstantRideFrag;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.Objects;

import javax.inject.Inject;

public class MapScrn extends BaseFragment<FragmentMapScrnBinding, MapScrnViewModel> implements MapScrnNavigator, OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = "MapScrn";
    @Inject
    MapScrnViewModel mapScrnViewModel;
    FragmentMapScrnBinding fragmentMapScrnBinding;

    public GoogleMap googleMap;

    //PlacesClient placesClient;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static boolean mMapIsTouched = false;
    public static String currency, currentDate;
    public static double earnings;


    // TODO: Rename and change types and number of parameters
    public static MapScrn newInstance(String date, String symbol, double earnigs) {
        currentDate = date;
        currency = symbol;
        earnings = earnigs;
        return new MapScrn();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMapScrnBinding = getViewDataBinding();
        mapScrnViewModel.setNavigator(this);
        ((HomeAct) requireActivity()).ShowOnloneOffline(true);
        mapScrnViewModel.earnings.set(currency + " " + CommonUtils.doubleDecimalFromat(earnings));
        mapScrnViewModel.currDate.set(currentDate);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fragmentMapScrnBinding.insRideMapBtn.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        mapScrnViewModel.isScreenAvailable = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mapScrnViewModel.isScreenAvailable = true;
    }

    @Override
    public MapScrnViewModel getViewModel() {
        return mapScrnViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_map_scrn;
    }

    /**
     * returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public Context getAttachedcontext() {
        return getContext();
    }

    /**
     * callback to get notified that {@link GoogleMap} is loaded
     **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("xxxMapScrnFrag", "onMapReady: ");
        this.googleMap = googleMap;
        if (checkPlayServices()) {

            if (ActivityCompat.checkSelfPermission(requireActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (getContext() != null)
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
            }
            CurrLocations();
        } else {
            getBaseActivity().finish();/// Later change..have to look fragment deattach...
        }

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getAttachedcontext(), R.raw.style_json));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        // mapScrnViewModel.buildGoogleApiClient(googleMap);

        this.googleMap = googleMap;
        googleMap.setTrafficEnabled(true);
    }

    //String Address;

    Marker driverMaekr;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void CurrLocations() {
        if (CommonUtils.isGpscheck(requireActivity())) {
            if (ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (googleMap != null) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {

                        mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + location.getLatitude());
                        mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + location.getLongitude());

                        if (driverMaekr != null)
                            driverMaekr.remove();

                        driverMaekr = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("MY Location")
                                .anchor(0.5f, 0.5f)
                                .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.ic_carmarker,Constants.Map_Marker)));

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), 14));
/*
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Address = addresses.get(0).getAddressLine(0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                });
            }
        } else CommonUtils.ShowGpsDialog(getActivity());
    }


    /**
     * checks if GooglePlayServices available or not
     **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getBaseActivity());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                Objects.requireNonNull(googleApiAvailability.getErrorDialog(getBaseActivity(), resultCode,
                        Constants.PLAY_SERVICES_REQUEST)).show();
            } else {

                showMessage(getBaseAct().getTranslatedString(R.string.DeviceNotSupport));


            }
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) requireActivity()).ShowHideBar(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClickCurrLocation() {
        CurrLocations();
    }

    @Override
    public void onclickEarnings() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, EarningsFrag.newInstance(), EarningsFrag.TAG)
                .commitAllowingStateLoss();

        ((HomeAct) requireActivity()).ShowOnloneOffline(false);
    }

    @Override
    public void openSideMenu() {
        LoadProfileFrag();
    }

    @Override
    public void onClick(View view) {
        ((HomeAct) requireActivity()).ShowOnloneOffline(false);
        ((HomeAct) requireActivity()).ShowHideBar(false);
        if (view.getId() == R.id.ins_ride_map_btn) {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, InstantRideFrag.newInstance(), InstantRideFrag.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitNow();
        }

       /* if(view.getId() == R.id.img_Side_Menu){
            LoadProfileFrag();
        }*/
    }

    private void LoadProfileFrag() {
        getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                .commit();
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOCATION_DIALOG) {
           *//* if (resultCode != RESULT_CANCELED) {
                if (resultCode == RESULT_OK) {
                    //
                }
            } else showMessage("Enable location to Move Further");*//*
        }
    }*/
}
