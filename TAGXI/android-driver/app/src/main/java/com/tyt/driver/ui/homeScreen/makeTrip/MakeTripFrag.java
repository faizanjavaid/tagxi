package com.tyt.driver.ui.homeScreen.makeTrip;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.MakeTripLayBinding;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.homeScreen.bookingConfirm.BookingFrag;
import com.tyt.driver.ui.homeScreen.mapscrn.adapter.CarsTypesAdapter;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;

import java.util.List;

import javax.inject.Inject;

public class MakeTripFrag extends BaseFragment<MakeTripLayBinding, MakeTripViewModel> implements MakeTripNavigator, OnMapReadyCallback {

    public static final String TAG = "MakeTripFrag";
    @Inject
    public MakeTripViewModel makeTripViewModel;
    public MakeTripLayBinding makeTripLayBinding;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker pickup, drop;

    static LatLng pLatLng, dLatLng;

    static String pAddress, dAddress;

    CarsTypesAdapter carsTypesAdapter;
    LinearLayoutManager linearLayoutManager;
    EtaModel getEtaModel;

    public MakeTripFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MakeTripFrag newInstance(LatLng pickLatLng, LatLng dropLatLng, String pickAddress, String dropAddress) {
        pLatLng = pickLatLng;
        dLatLng = dropLatLng;
        pAddress = pickAddress;
        dAddress = dropAddress;
        return new MakeTripFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeTripLayBinding = getViewDataBinding();
        makeTripViewModel.setNavigator(this);

        makeTripViewModel.pickupLatLng.set(pLatLng);
        makeTripViewModel.dropLatLng.set(dLatLng);
        makeTripViewModel.pickupAddress.set(pAddress);
        makeTripViewModel.dropAddress.set(dAddress);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getAttachedcontext(), Constants.PlaceApi_key);
        }
        PlacesClient placesClient = Places.createClient(getAttachedcontext());

        makeTripViewModel.getTypes();
    }

    Marker pickMarker, dropMarker;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void PlaceMarker() {

        if (pickMarker != null)
            pickMarker.remove();
        pickMarker = googleMap.addMarker(new MarkerOptions()
                .position(pLatLng)
                .title("Pickup Location")
                .anchor(0.5f, 0.5f)
                .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.pick_marker,Constants.Pick_Marker)));

        if (dropMarker != null)
            dropMarker.remove();
        dropMarker = googleMap.addMarker(new MarkerOptions()
                .position(dLatLng)
                .title("Pickup Location")
                .anchor(0.5f, 0.5f)
                .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.drop_marker,Constants.Drop_Marker)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickMarker.getPosition());
        builder.include(dropMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int width = MyApp.getmContext().getResources().getDisplayMetrics().widthPixels;
        int height = MyApp.getmContext().getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.40);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);

    }


    @Override
    public void onStart() {
        super.onStart();
        makeTripViewModel.isScreenAvailable = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        makeTripViewModel.isScreenAvailable = true;
    }


    @Override
    public MakeTripViewModel getViewModel() {
        return makeTripViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.make_trip_lay;
    }


    /**
     * asks the user enable high accuracy gps mode if in battery saver mode
     **/
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), Constants.REQUEST_CODE_ENABLING_GOOGLE_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
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

    @Override
    public void onClickBackImg() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag(MakeTripFrag.TAG) != null) {
            ((HomeAct) getActivity()).ShowHideBar(true);
            Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag(MakeTripFrag.TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
    }

    @Override
    public void loadTypes(List<Type> typeList) {
        if (typeList.size() > 0) {
            makeTripViewModel.noItem.set(false);
            linearLayoutManager = new LinearLayoutManager(getAttachedcontext(), LinearLayoutManager.HORIZONTAL, false);
            carsTypesAdapter = new CarsTypesAdapter(getAttachedcontext(), typeList, this);
            makeTripLayBinding.typesRecycle.setLayoutManager(linearLayoutManager);
            makeTripLayBinding.typesRecycle.setAdapter(carsTypesAdapter);
        } else
            makeTripViewModel.noItem.set(true);

    }

    @Override
    public void clickedTypes(Type type) {
        if (type != null) {
            makeTripViewModel.EtaApi(type.getId());
        }
    }

    @Override
    public void onClickNext() {
        if (getEtaModel != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_frame, BookingFrag.newInstance(getEtaModel, makeTripViewModel.pickupAddress.get(), makeTripViewModel.dropAddress.get(), makeTripViewModel.pickupLatLng.get(), makeTripViewModel.dropLatLng.get()), BookingFrag.TAG)
                    .commit();
    }

    @Override
    public void loadEta(EtaModel data) {
        getEtaModel = data;
        makeTripViewModel.onClickTypes.set(true);
        makeTripViewModel.carModel.set(data.getName());
        makeTripViewModel.distance.set(data.getDistance() + " " + data.getUnitInWordsWithoutLang());
        makeTripViewModel.price.set(CommonUtils.doubleDecimalFromat(data.getTotal()));
        makeTripViewModel.ETA.set("ETA " + data.getDriverArivalEstimation());
    }


    /**
     * callback to get notified that {@link GoogleMap} is loaded
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("xxxMakeTripFrag", "onMapReady: ");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // makeTripViewModel.buildGoogleApiClient(googleMap);
        this.googleMap = googleMap;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                PlaceMarker();
            }
        });

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.uber_map));
        googleMap.setTrafficEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
