package com.tyt.driver.ui.homeScreen.instantRide;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.FragmentInstantRideBinding;
import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseFragment;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;


public class InstantRideFrag extends BaseFragment<FragmentInstantRideBinding, InstantRideViewModel> implements InstantRideNavigator, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    public static final String TAG = "InstantRideFrag";
    @Inject
    InstantRideViewModel instantRideViewModel;
    @Inject
    SharedPrefence sharedPrefence;
    FragmentInstantRideBinding fragmentInstantRideBinding;
    List<EtaModel> etalocallist = new ArrayList<>();

    InsEtaAdapter insEtaAdapter;

    BottomSheetBehavior typesheet;

    FusedLocationProviderClient fusedLocationProviderClient;
    public GoogleMap googleMap;

    String Address;

    public static boolean mMapIsTouched = true;
    private String CurrentAddress;

    PlacesClient placesClient;


    public static InstantRideFrag newInstance() {

        return new InstantRideFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentInstantRideBinding = getViewDataBinding();

        fragmentInstantRideBinding.setViewModel(instantRideViewModel);
        instantRideViewModel.setNavigator(this);

        if (!Places.isInitialized()) {
            Places.initialize(getAttachedcontext(), Constants.PlaceApi_key);
        }
        placesClient = Places.createClient(getAttachedcontext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.insr_map);

        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        instantRideViewModel.MainTitle.set("Instant Ride");

        setTypeSheet();

        CurrLocations();

        /*if (instantRideViewModel.PickLatLng!=null&&instantRideViewModel.DropLatLng!=null)
        CallETAApi();*/

        fragmentInstantRideBinding.pickupLocTv.setOnClickListener(this);

        fragmentInstantRideBinding.dropLocTv.setOnClickListener(this);

        fragmentInstantRideBinding.insrStartrideBt.setOnClickListener(this);

    }


    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {


        if (checkPlayServices()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getAttachedcontext(), R.raw.uber_map));
                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivityRaw", "Can't find style.", e);
            }


            CurrLocations();

        } else {
            getBaseActivity().finish();
        }

        this.googleMap = googleMap;
        instantRideViewModel.mGoogleMap = googleMap;

        googleMap.setTrafficEnabled(true);
        this.googleMap.setOnCameraIdleListener(this);


    }


    @Override
    public void onCameraIdle() {
        if (mMapIsTouched) {
            if (googleMap != null) {
                SetMarker(googleMap.getCameraPosition().target);
            }
        }
    }

    private void SetMarker(LatLng target) {
        mMapIsTouched = true;

        if (googleMap != null)
            googleMap.clear();

        double lat = 0.0, lng = 0.0;

        lat = Double.parseDouble(instantRideViewModel.sharedPrefence.Getvalue(SharedPrefence.CURRLAT));
        lng = Double.parseDouble(instantRideViewModel.sharedPrefence.Getvalue(SharedPrefence.CURRLNG));

        if (lat != 0.0 && lng != 0.0) {
            double finalLat = lat;
            double finalLng = lng;
            getBaseAct().runOnUiThread(() -> {
                googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(finalLat, finalLng))
                        .radius(10000)
                        .strokeColor(MyApp.getmContext().getResources().getColor(R.color.blue_map_stroke))
                        .fillColor(MyApp.getmContext().getResources().getColor(R.color.blue_map_fill))
                        .strokeWidth(2));
            });
        }

        getBaseAct().runOnUiThread(() -> {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1);
                Address = addresses.get(0).getAddressLine(0);

                if (Address.equals(CurrentAddress)) {
                    fragmentInstantRideBinding.currlocfab.setVisibility(View.INVISIBLE);
                }else {
                    fragmentInstantRideBinding.currlocfab.setVisibility(View.VISIBLE);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("xxxMapFrag", "SetMarker: " + e.getLocalizedMessage());
            }
        });

        if (Address != null) {

            if (instantRideViewModel.PickupClicked.get()) {
                setPickUpAddress(Address, null, target);
            } else if (instantRideViewModel.DropClicked.get()) {
                setDropAddress(Address, null, target);
            }
        }
    }

    private void CurrLocations() {
        if (CommonUtils.isGpscheck(getActivity())) {
            if (ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LocationGet(location);
                } else {
                    _getLocation();
                    instantRideViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + 0.0);
                    instantRideViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + 0.0);

                    Log.e("LocationNUll--", "Null");
                }
            });
        } else {
            CommonUtils.ShowGpsDialog(getActivity());
        }
    }

    private void _getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
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

        LocationServices.getFusedLocationProviderClient(getAttachedcontext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void LocationGet(Location location) {
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 11));

            instantRideViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + location.getLatitude());
            instantRideViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + location.getLongitude());


            //Draw Circle For 10KM Radius In Map
            if (googleMap != null) {

                if (getActivity() != null) {
                    //new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                    googleMap.clear();
                    googleMap.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()))
                            .radius(10000)
                            .strokeColor(requireActivity().getResources().getColor(R.color.blue_map_stroke))
                            .fillColor(requireActivity().getResources().getColor(R.color.blue_map_fill))
                            .strokeWidth(2));
                    //}), 500);
                }

            }

            //Getting The Address of the current location in Map
            if (getBaseAct() != null) {
                getBaseAct().runOnUiThread(() -> {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Address = addresses.get(0).getAddressLine(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("xxxMapFrag", "LocationGet: Error ==> " + e.getMessage());
                    }
                });
            }

            if (Address != null) {
                CurrentAddress = Address;
                fragmentInstantRideBinding.currlocfab.setVisibility(View.INVISIBLE);
                if (instantRideViewModel.PickupClicked.get()) {
                    setPickUpAddress(Address, location, null);
                } else if (instantRideViewModel.DropClicked.get()) {
                    setDropAddress(Address, location, null);
                } else setPickUpAddress(Address, location, null);
            }

        }
    }

    public boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getBaseActivity());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(getBaseActivity(), resultCode,
                        Constants.PLAY_SERVICES_REQUEST).show();
            } else {
                showMessage(getBaseAct().getTranslatedString(R.string.DeviceNotSupport));
            }
            return false;
        }
        return true;
    }

    @Override
    public InstantRideViewModel getViewModel() {
        return instantRideViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_instant_ride;
    }

    @Override
    public void removeinsfrag() {
        String date = sharedPrefence.Getvalue(SharedPrefence.CurrDate);
        String cursymbl = sharedPrefence.Getvalue(SharedPrefence.CurrenSymbol);
        String totalearn = sharedPrefence.Getvalue(SharedPrefence.TotalEarn);
        ((HomeAct) requireActivity()).openHomeFrag(date, cursymbl, Double.parseDouble(totalearn));
    }

    @Override
    public void loadetatypes(List<EtaModel> insrlist) {
        etalocallist.clear();

        etalocallist = insrlist;

        if (insrlist.size() > 0) {
            instantRideViewModel.EnableBookButon.set(true);

            insEtaAdapter = new InsEtaAdapter(getContext(), etalocallist, this);
            LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

            fragmentInstantRideBinding.cartypesSheet.insrEtaRecycler.setLayoutManager(manager);
            fragmentInstantRideBinding.cartypesSheet.insrEtaRecycler.setAdapter(insEtaAdapter);

            insEtaAdapter.notifyDataSetChanged();

        } else {

            instantRideViewModel.EnableBookButon.set(false);
            showMessage("No Vehicles Available to ride");

        }
    }

    @Override
    public void onclick_insr_eta_item(EtaModel etaModel) {

        if (etaModel != null) {
            instantRideViewModel.isETA_Picked.set(true);
            if (typesheet == null) {
                typesheet = BottomSheetBehavior.from(fragmentInstantRideBinding.cartypesSheet.irEtaMainLay);
                typesheet.setHideable(true);
            }
            typesheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else instantRideViewModel.isETA_Picked.set(false);

    }

    @Override
    public void movetoinstanttrip() {
        new Handler().postDelayed(() -> ((HomeAct) requireActivity()).ProfileRefreshAPI(), 2500);
    }

    @Override
    public BaseActivity getBaseAct() {
        return ((BaseActivity) getActivity());
    }

    private void setTypeSheet() {
        typesheet = BottomSheetBehavior.from(fragmentInstantRideBinding.cartypesSheet.irEtaMainLay);
        typesheet.setHideable(true);

        typesheet.setState(BottomSheetBehavior.STATE_HIDDEN);


        typesheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
                    /*    bottomSheet.requestLayout();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
                    }
                    case BottomSheetBehavior.STATE_SETTLING:
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.pickup_loc_tv) {
            //open Map Frag to choose Pickup Location
            instantRideViewModel.onClickPickup(getView());
        } else if (view.getId() == R.id.drop_loc_tv) {
            //open Map Frag to choose Drop Location
            instantRideViewModel.onClickDrop(getView());
        } else if (view.getId() == R.id.insr_startride_bt) {

            if (instantRideViewModel.PickLatLng.get() == null || instantRideViewModel.PickAddress == null) {
                showMessage("Choose Your Pickup Point");
            } else if (instantRideViewModel.DropLatLng.get() == null || instantRideViewModel.DropAddress == null) {
                showMessage("Choose Your Drop Point");
            } else if (instantRideViewModel.insr_name.get() == null || Objects.requireNonNull(instantRideViewModel.insr_name.get()).isEmpty()) {
                showMessage("Enter Valid Name");
            } else if (instantRideViewModel.insr_phone.get() == null || Objects.requireNonNull(instantRideViewModel.insr_phone.get()).isEmpty()) {
                showMessage("Enter Valid Phone No");
            }else {
                if (instantRideViewModel.EnableBookButon.get()) {
                    if (instantRideViewModel.isETA_Picked.get()) {
                        instantRideViewModel.mIsLoading.set(true);
                        MakeInstantTrip();
                        instantRideViewModel.EnableBookButon.set(false);
                    } else {
                        if (typesheet == null) {
                            typesheet = BottomSheetBehavior.from(fragmentInstantRideBinding.cartypesSheet.irEtaMainLay);
                            typesheet.setHideable(true);
                        }
                        typesheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                } else showMessage("Try Again Later");
            }

        }
    }

    private void CallETAApi() {
        HashMap<String, String> etamap = new HashMap<>();
        etamap.put(Constants.NetworkParameters.pLat, String.valueOf(Objects.requireNonNull(instantRideViewModel.PickLatLng.get()).latitude));
        etamap.put(Constants.NetworkParameters.pLng, String.valueOf(Objects.requireNonNull(instantRideViewModel.PickLatLng.get()).longitude));

        if (instantRideViewModel.DropLatLng.get()!=null){
            etamap.put(Constants.NetworkParameters.dLat, String.valueOf(Objects.requireNonNull(instantRideViewModel.DropLatLng.get()).latitude));
            etamap.put(Constants.NetworkParameters.dLng, String.valueOf(Objects.requireNonNull(instantRideViewModel.DropLatLng.get()).longitude));
        }

        etamap.put(Constants.NetworkParameters.rType, "1");

        instantRideViewModel.getCarTypes(etamap);

    }

    private void MakeInstantTrip() {
        HashMap<String, Object> insrmap = new HashMap<>();

        insrmap.put(Constants.NetworkParameters.pLat, String.valueOf(Objects.requireNonNull(instantRideViewModel.PickLatLng.get()).latitude));
        insrmap.put(Constants.NetworkParameters.pLng, String.valueOf(Objects.requireNonNull(instantRideViewModel.PickLatLng.get()).longitude));

        insrmap.put(Constants.NetworkParameters.dLat, String.valueOf(Objects.requireNonNull(instantRideViewModel.DropLatLng.get()).latitude));
        insrmap.put(Constants.NetworkParameters.dLng, String.valueOf(Objects.requireNonNull(instantRideViewModel.DropLatLng.get()).longitude));

        insrmap.put(Constants.NetworkParameters.pAddress, instantRideViewModel.PickAddress.get());
        insrmap.put(Constants.NetworkParameters.dAddress, instantRideViewModel.DropAddress.get());

        insrmap.put(Constants.NetworkParameters.rType, "1");

        insrmap.put(Constants.NetworkParameters.pick_IRCname, instantRideViewModel.insr_name.get());

        insrmap.put(Constants.NetworkParameters.pick_IRCmobile, instantRideViewModel.insr_name.get());

        instantRideViewModel.startInsRide(insrmap);
    }

    public void handlebackpress() {

        instantRideViewModel.onCLickBack(getView());

    }

    @Override
    public Context getAttachedcontext() {
        return getContext();
    }

    @Override
    public void openplacesearchAPI() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getAttachedcontext());
        startActivityForResult(intent, Constants.REQUEST_CODE_AUTOCOMPLETE);

    }

    @Override
    public void currloc() {
        CurrLocations();
    }

    @Override
    public void calletaAPI() {
        CallETAApi();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = Autocomplete.getPlaceFromIntent(data);

                    sharedPrefence.saveBoolean(SharedPrefence.onGoogleSearch, true);

                    if (instantRideViewModel.PickupClicked.get()) {

                        setPickUpAddress(place.getAddress(), null, place.getLatLng());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude), 14.0f));

                    } else if (instantRideViewModel.DropClicked.get()) {

                        setDropAddress(place.getAddress(), null, place.getLatLng());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude), 14.0f));

                    }
                }
            } else showMessage("something went wrong");

        }

    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public String getNewShortAddress(String pickdropaddress) {
        if (pickdropaddress.length() >= 40) {
            return pickdropaddress.substring(0, 40) + "...";
        } else return pickdropaddress;
    }

    public void setPickUpAddress(String Address, Location location, LatLng target) {
        instantRideViewModel.PickAddress.set(getNewShortAddress(Address));

        if (location == null) {
            instantRideViewModel.PickLatLng.set(target);
        } else if (target == null) {
            instantRideViewModel.PickLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));
        }

       /* if (instantRideViewModel.PickLatLng.get() != null && instantRideViewModel.DropLatLng.get() != null) {
            CallETAApi();
        }*/

    }

    public void setDropAddress(String Address, Location location, LatLng latlng) {
        instantRideViewModel.DropAddress.set(getNewShortAddress(Address));

        if (location == null) {
            instantRideViewModel.DropLatLng.set(latlng);
        } else if (latlng == null) {
            instantRideViewModel.DropLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));
        }

      /*  if (instantRideViewModel.PickLatLng.get() != null && instantRideViewModel.DropLatLng.get() != null) {
            CallETAApi();
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAct) requireActivity()).ShowOnloneOffline(false);
        ((HomeAct) requireActivity()).ShowHideBar(false);
        sharedPrefence.saveBoolean(SharedPrefence.onGoogleSearch,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPrefence.saveBoolean(SharedPrefence.onGoogleSearch,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPrefence.saveBoolean(SharedPrefence.onGoogleSearch,false);
    }

}