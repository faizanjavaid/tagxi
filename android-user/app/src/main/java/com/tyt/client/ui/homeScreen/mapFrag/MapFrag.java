package com.tyt.client.ui.homeScreen.mapFrag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.FragmentMapScrnBinding;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.MyselfContact;
import com.tyt.client.retro.responsemodel.RentalPackage;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.FavouitesLocAdapter;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.client.ui.homeScreen.mapFrag.adapter.RentalPackagesAdapter;
import com.tyt.client.ui.homeScreen.mapFrag.adapter.RentalVehicAdapter;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.FirebaseHelper;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.TouchableWrapper;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.tyt.client.utilz.Constants.REQUEST_CODE_AUTOCOMPLETE;

/**
 * Fragment to show the Map in the entry point After Successful Login.
 */

public class MapFrag extends BaseFragment<FragmentMapScrnBinding, MapFragViewModel> implements MapFragNavigator, View.OnClickListener, TouchableWrapper.UpdateMapAfterUserInterection, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks {
    public static final String TAG = "MapScrn";

    public static final String HOMETAG = "Home";
    public static final String PICKDROPTAG = "PickDrop";

    public static final int STATE_HIDDEN = BottomSheetBehavior.STATE_HIDDEN;

    @Inject
    MapFragViewModel mapScrnViewModel;
    FragmentMapScrnBinding fragmentMapScrnBinding;

    @Inject
    SharedPrefence sharedPrefence;

    //Google Maps
    public GoogleMap googleMap;
    PlacesClient placesClient;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static String coming_type;

    public static boolean mMapIsTouched = false;
    public View mOriginalContentView;

    private BottomSheetBehavior mapbottomsheet;
    private BottomSheetBehavior PickDropbottomsheet;
    Animation bounceanim;
    Animation topanim;

    String CurrentAddress;

    List<MyselfContact> myselfContacts = new ArrayList<>();
    List<EtaModel> rentalPackageList = new ArrayList<>();

    RentalVehicAdapter rentalVehicAdapter;

    // TODO: Rename and change types and number of parameters
    public static MapFrag newInstance(String key) {
        Log.d("xxxMapFrg", "newInstance: ");
        coming_type = key;
        return new MapFrag();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("xxxMapFrag", "onCreateView Called in MapFrag");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e("xxxMapFrag", "onViewCreated Called in MapFrag");
        super.onViewCreated(view, savedInstanceState);

        fragmentMapScrnBinding = getViewDataBinding();
        mapScrnViewModel.setNavigator(this);

        if (coming_type.equalsIgnoreCase("Profile")) {
            callProfileAPIFromMap();
        }

        saveAndSendMyselfVal();

        fragmentMapScrnBinding.mapsheet.tabs.dailyFragView.favouiteplaceRecycler.setOnTouchListener((viewr, motionEvent) -> {

            int action = motionEvent.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    viewr.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    viewr.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            viewr.onTouchEvent(motionEvent);
            return true;
        });

        bounceanim = AnimationUtils.loadAnimation(MyApp.getmContext(), R.anim.bounce);
        fragmentMapScrnBinding.mapHomeLay.homemapcard.startAnimation(bounceanim);

        sharedPrefence.saveBoolean(SharedPrefence.In_Home, true);
        sharedPrefence.saveBoolean(SharedPrefence.In_Book, false);
        sharedPrefence.saveBoolean(SharedPrefence.In_PickDrop, false);

        setMAPBottomSheetBehaviour();

        set_PickDrop_Sheet();

        fragmentMapScrnBinding.mapHomeLay.moreIcon.setOnClickListener(this);
        fragmentMapScrnBinding.mapHomeLay.favIcon.setOnClickListener(this);
        fragmentMapScrnBinding.mapHomeLay.llhomebar.setOnClickListener(this);
        fragmentMapScrnBinding.mapHomeLay.pickup.setOnClickListener(this);

        fragmentMapScrnBinding.mapPickDropLay.cancelIcDrop.setOnClickListener(this);
        fragmentMapScrnBinding.mapPickDropLay.cancelIcPick.setOnClickListener(this);
        fragmentMapScrnBinding.mapPickDropLay.myselfLay.setOnClickListener(this);
        fragmentMapScrnBinding.confirmLocBt.setOnClickListener(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapview);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getAttachedcontext(), Constants.PlaceApi_key);
        }
        placesClient = Places.createClient(getAttachedcontext());

    }

    @SuppressLint("SetTextI18n")
    private void saveAndSendMyselfVal() {
        mapScrnViewModel.myself_contact.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no));

        mapScrnViewModel.myself_contact_name.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_name));

        mapScrnViewModel.myself_trip.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no).equalsIgnoreCase("Myself"));

        if (mapScrnViewModel.myself_contact_name.get() != null && !Objects.requireNonNull(mapScrnViewModel.myself_contact_name.get()).isEmpty()) {
            setMyselfContactDetails(Objects.requireNonNull(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_name)));
        } else if (Objects.requireNonNull(mapScrnViewModel.myself_contact_name.get()).equalsIgnoreCase("Myself")) {
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.tvMyselfBook.setText("Myself");
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.imgMyselfBook.setImageResource(R.drawable.ic_myself);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMyselfContactDetails(String cname) {
        if (cname.equalsIgnoreCase("Myself")) {
            //default one will be applied no changes
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.tvMyselfBook.setText("Myself");
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.imgMyselfBook.setImageResource(R.drawable.ic_myself);
        } else {
            String name2;
            if (cname.length() <= 4) {
                name2 = cname;
            } else name2 = cname.substring(0, 4);

            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.tvMyselfBook.setText(name2);

            String startLetter = String.valueOf(cname.charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL;

            int randomcolor = generator.getRandomColor();

            TextDrawable drawletterimg = TextDrawable.builder()
                    .buildRoundRect(startLetter, randomcolor, 10);

            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.imgMyselfBook.setImageDrawable(drawletterimg);
        }

    }

    private void callProfileAPIFromMap() {
        ((HomeAct) requireActivity()).callProfileAPI();
    }

    @Override
    public void onStart() {
        Log.e("xxxMapFrag", "onStart Called in MapFrag");
        super.onStart();
        mapScrnViewModel.isScreenAvailable = true;
    }

    @Override
    public void onStop() {
        Log.e("xxxMapFrag", "onStop Called in MapFrag");
        super.onStop();
        mapScrnViewModel.isScreenAvailable = true;
    }

    @Override
    public MapFragViewModel getViewModel() {
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
     * asks the user enable high accuracy gps mode if in battery saver mode
     **/

/*    private void displayLocationSettingsRequest(Context context) {
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
    }*/

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
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.e("xxxMapFrag", "onMapReady method Called in MapFrag");

        if (checkPlayServices()) {
            assert getContext() != null;
            if (getContext() != null)
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            CurrLocations();
        } else {
            getBaseActivity().finish();/// Later change..have to look fragment deattach...
        }

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(requireActivity()), R.raw.uber_map));
        googleMap.setTrafficEnabled(true);

        this.googleMap = googleMap;
        mapScrnViewModel.mGoogleMap = googleMap;

        this.googleMap.setOnCameraIdleListener(this);

    }

    String Address;

    private void CurrLocations() {
        if (CommonUtils.isGpscheck(Objects.requireNonNull(requireActivity()))) {
            if (ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getAttachedcontext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LocationGet(location);
                } else {
                    if (isAdded()) {
                        _getLocation();
                    } else {
                        new Handler().postDelayed(this::_getLocation, 1500);
                    }

                    mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + 0.0);
                    mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + 0.0);

                    Log.e("LocationNUll--", "Null");
                }
            });
        } else CommonUtils.ShowGpsDialog(getActivity());
    }

    private void _getLocation() {

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(MyApp.getmContext())
                .addConnectionCallbacks(this)
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

        if (isAdded()) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Objects.requireNonNull(requireActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationServices.getFusedLocationProviderClient(MyApp.getmContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    /**
     * h
     * Constants.REQUEST_CODE_ENABLING_GOOGLE_LOCATION
     * request of Enabling GPS using google api
     * used delay to get location request
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("xxxMapFrag", "onActivityResult method Called in MapFrag");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
              /*  if (mapbottomsheet == null)
                    mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);

                mapbottomsheet.setHideable(true);
                mapbottomsheet.setState(STATE_HIDDEN);
                mapbottomsheet.setHideable(false);*/

                if (mapScrnViewModel.pickupClicked.get()) {
                    mapScrnViewModel.pickupAddress.set(place.getAddress());
                    mapScrnViewModel.newpickupAddress.set(getNewPickAddress(Objects.requireNonNull(place.getAddress())));

                    mapScrnViewModel.pickupLatLng.set(place.getLatLng());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude), 14.0f));

                } else if (mapScrnViewModel.dropClicked.get()) {
                    mapScrnViewModel.dropAddress.set(place.getAddress());
                    //if (place.getAddress().length() >= 40)
                    mapScrnViewModel.newdropAddress.set(getNewDropAddress(Objects.requireNonNull(place.getAddress())));
                    //else mapScrnViewModel.newdropAddress.set(place.getAddress());
                    mapScrnViewModel.dropLatLng.set(place.getLatLng());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude), 14.0f));
                }
            } else
                Log.e("ddd==", "error");
        } else if (requestCode == Constants.REQUEST_SELECT_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();

                Cursor cursor = getBaseAct().managedQuery(contactUri, null, null, null, null);

                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                createContact(number, name);

            }
        }
    }

    private void createContact(String number, String name) {

        if (myselfContacts.size() < 3) {
            MyselfContact contactobj = new MyselfContact(name, number, R.drawable.ic_choose_contact);
            myselfContacts.add(contactobj);
            MyselfDialog.dismiss();
            showMyselfDialog("");
        } else {
            showMessage("Maximum Contacts Reached");
        }

    }

    /**
     * checks if GooglePlayServices available or not
     **/
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
        Log.e("xxxMapFrag", "onResume Called in MapFrag");
        super.onResume();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
        ((HomeAct) requireActivity()).ShowHideBar(true);
    }

    @Override
    public void onDestroy() {
        Log.e("xxxMapFrag", "onDestroy Called in MapFrag");
        super.onDestroy();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
    }

    @Override
    public void onDestroyView() {
        Log.e("xxxMapFrag", "onDestroyView Called in MapFrag");
        super.onDestroyView();
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
    }

    @Override
    public void onClickPickup() {
        openPlacesSearchApi();
        if (mapbottomsheet == null)
            mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);

        if (PickDropbottomsheet == null) {
            PickDropbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.pickdropsheet.pickdroplay);
        }
        fragmentMapScrnBinding.mapsheet.mapSheetCoOrdLay.setVisibility(View.GONE);
        PickDropbottomsheet.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onClickDrop() {
        openPlacesSearchApi();
        if (mapbottomsheet == null)
            mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);

        if (PickDropbottomsheet == null) {
            PickDropbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.pickdropsheet.pickdroplay);
        }
        fragmentMapScrnBinding.mapsheet.mapSheetCoOrdLay.setVisibility(View.GONE);
        PickDropbottomsheet.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onClickCurrLocation() {
        CurrLocations();
    }

    @Override
    public void onClickConfirm(HashMap<String, String> driverDatas, HashMap<String, Marker> driverPins, String openmethod) {
        if (mapScrnViewModel.pickupLatLng.get() != null) {
            //if (mapScrnViewModel.dropLatLng.get() != null) {
            Log.e("DropLatlng", "dLatlNg==" + mapScrnViewModel.dropLatLng.get());

            if (mapScrnViewModel.dropAddress.get() != null && mapScrnViewModel.dropLatLng.get() != null) {
                Log.w("xxxMapFrag", "onClickConfirm: pickupAddress =" + mapScrnViewModel.pickupAddress.get() + " || dropAddress = " + mapScrnViewModel.dropAddress.get());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fragment_frame, MakeTripFrag.newInstance(mapScrnViewModel.pickupLatLng.get(), mapScrnViewModel.dropLatLng.get(), mapScrnViewModel.pickupAddress.get(), mapScrnViewModel.dropAddress.get(), driverDatas, driverPins, openmethod), MakeTripFrag.TAG)
                        .commit();
                ((HomeAct) getAttachedcontext()).ShowHideBar(false);
            } else if (mapScrnViewModel.dropAddress.get() == null) {

                if (!mapScrnViewModel.rental_clicked.get()) {
                    showMessage("Choose Your Drop Address");
                } else {
                    //Open Booking Screen For Rental Trip
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.fragment_frame, MakeTripFrag.newRentInstance(mapScrnViewModel.pickupLatLng.get(), mapScrnViewModel.pickupAddress.get(), driverDatas, driverPins, openmethod, mapScrnViewModel.Rent_REQID.get()), MakeTripFrag.TAG)
                            .commit();
                    showMessage("Please Wait...");
                }
            } else if (mapScrnViewModel.pickupAddress.get() == null)
                showMessage("Choose Your Pickup Point");
            else
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.fragment_frame, MakeTripFrag.newInstance(mapScrnViewModel.pickupLatLng.get(), mapScrnViewModel.pickupAddress.get(), driverDatas, driverPins, openmethod), MakeTripFrag.TAG)
                        .commit();
        }
    }

    @Override
    public void moveToMapPosition() {
        if (mapScrnViewModel.pickupClicked.get()) {
            if (mapScrnViewModel.pickupLatLng.get() != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).latitude, Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).longitude), 14.0f));

                FirebaseHelper.queryDrivers(mapScrnViewModel.pickupLatLng.get());
            }
        } else if (mapScrnViewModel.dropClicked.get()) {
            if (mapScrnViewModel.dropLatLng.get() != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Objects.requireNonNull(mapScrnViewModel.dropLatLng.get()).latitude, Objects.requireNonNull(mapScrnViewModel.dropLatLng.get()).longitude), 14.0f));

            }
        }
    }

    private final SimpleDateFormat mtimeFormatter = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
    private final SimpleDateFormat mdateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    SimpleDateFormat sdfDate11 = new SimpleDateFormat("dd MMM", Locale.US);

    @Override
    public void onClickLoadCalendar() {
        new SingleDateAndTimePickerDialog.Builder(getAttachedcontext())
                .curved()
                .bottomSheet()
                .mustBeOnFuture()
                .backgroundColor(Color.WHITE)
                .mainColor(requireActivity().getResources().getColor(R.color.colorPrimary))
                .title("Choose date")
                .titleTextColor(requireActivity().getResources().getColor(R.color.clr_black))
                .listener(date -> {
                    Log.e("selectedDatee----", "dateSelected---" + date.toString());
                    long different;

                    String currentdate = mdateFormatter.format(new Date());
                    String selecteddate = mdateFormatter.format(date);
                    try {
                        if (Objects.requireNonNull(mdateFormatter.parse(currentdate)).compareTo(mdateFormatter.parse(selecteddate)) == 0) {
                            Calendar now = Calendar.getInstance();
                            now.add(Calendar.MINUTE, 15);

                            String After30Time = mtimeFormatter.format(now.getTime());
                            String Selectedtime = mtimeFormatter.format(date);

                            different = Objects.requireNonNull(mtimeFormatter.parse(After30Time)).getTime() - Objects.requireNonNull(mtimeFormatter.parse(Selectedtime)).getTime();

                            if (different < 0) {
                                String month_name = sdfDate11.format(date);
                                mapScrnViewModel.shortDate.set(month_name);
                                Log.e("dateee----", "_________" + month_name);
                            } else {
                                Toast.makeText(getAttachedcontext(), "Time should be 60 mins greater", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                })
                .display();
    }

    private void openPlacesSearchApi() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getAttachedcontext());
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    @Override
    public void onCameraIdle() {
        if (mMapIsTouched) {
            if (googleMap != null) {
                SetMarker(googleMap.getCameraPosition().target);
            }
        }
    }

    private void SetMarker(LatLng latLng) {
        mMapIsTouched = true;
        Log.e("xxxgetPosition==", "pos--" + googleMap.getCameraPosition().target.latitude);

        if (googleMap != null)
            googleMap.clear();

        double lat, lng;

        lat = Double.parseDouble(mapScrnViewModel.sharedPrefence.Getvalue(SharedPrefence.CURRLAT));
        lng = Double.parseDouble(mapScrnViewModel.sharedPrefence.Getvalue(SharedPrefence.CURRLNG));

        if (lat != 0.0 && lng != 0.0) {
            getBaseAct().runOnUiThread(() -> googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lng))
                    .radius(10000)
                    .strokeColor(MyApp.getmContext().getResources().getColor(R.color.blue_map_stroke))
                    .fillColor(MyApp.getmContext().getResources().getColor(R.color.blue_map_fill))
                    .strokeWidth(2)));
        }

        /*getBaseAct().runOnUiThread(() -> {*/
        getBaseAct().runOnUiThread(() -> {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Address = addresses.get(0).getAddressLine(0);

                if (Address.equals(CurrentAddress)) {
                    fragmentMapScrnBinding.currLoc.setVisibility(View.INVISIBLE);
                } else {
                    fragmentMapScrnBinding.currLoc.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("xxxMapFrag", "SetMarker: " + e.getLocalizedMessage());
            }
        });

        if (Address != null) {
            if (mapScrnViewModel.pickupClicked.get()) {
                mapScrnViewModel.pickupAddress.set(Address);
                //if (Address.length() >= 40)
                mapScrnViewModel.newpickupAddress.set(getNewPickAddress((Objects.requireNonNull(Address))));
                //else mapScrnViewModel.newpickupAddress.set(Address);
                Log.d("xxxMapfrag", "SetMarker: pickupAddress = " + Address);
                mapScrnViewModel.pickupLatLng.set(new LatLng(latLng.latitude, latLng.longitude));

                FirebaseHelper.queryDrivers(mapScrnViewModel.pickupLatLng.get());

           /* if (pickup != null)
                pickup.remove();

            pickup = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .title("Pickup Location")
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.pick_marker)));*/

            } else if (mapScrnViewModel.dropClicked.get()) {
                mapScrnViewModel.dropLatLng.set(new LatLng(latLng.latitude, latLng.longitude));
                mapScrnViewModel.dropAddress.set(Address);
                //if (Address.length() >= 40)
                mapScrnViewModel.newdropAddress.set(getNewDropAddress(Objects.requireNonNull((Objects.requireNonNull(Address)))));
                //else mapScrnViewModel.newdropAddress.set(Address);
                Log.d("xxxMapfrag", "SetMarker: dropAddress = " + Address);

                mapScrnViewModel.dropShown.set(!Objects.requireNonNull(mapScrnViewModel.dropAddress.get()).isEmpty());

          /*  if (drop != null)
                drop.remove();

            drop = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .title("Drop Location")
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.drop_marker)));*/
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void LocationGet(Location location) {
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 11));

            mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLAT, "" + location.getLatitude());
            mapScrnViewModel.sharedPrefence.savevalue(SharedPrefence.CURRLNG, "" + location.getLongitude());

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


            if (getBaseAct() != null) {
                getBaseAct().runOnUiThread(() -> {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses;
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
                fragmentMapScrnBinding.currLoc.setVisibility(View.INVISIBLE);

                if (mapScrnViewModel.pickupClicked.get()) {
                    mapScrnViewModel.pickupAddress.set(Address);
                    //if (Address.length() >= 40)
                    mapScrnViewModel.newpickupAddress.set(getNewPickAddress((Objects.requireNonNull(Address))));
                    //else mapScrnViewModel.newpickupAddress.set(Address);
                    Log.d("xxxMapFrag", "LocationGet: pickupAddress ==> " + Address);
                    mapScrnViewModel.pickupLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));

                    FirebaseHelper.queryDrivers(mapScrnViewModel.pickupLatLng.get());

                    callETA_API();

                         /*   if (pickup != null)
                                pickup.remove();

                            pickup = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("Pickup Location")
                                    .anchor(0.5f, 0.5f)
                                    .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.pick_marker)));*/

                } else if (mapScrnViewModel.dropClicked.get()) {
                    mapScrnViewModel.dropLatLng.set(new LatLng(location.getLatitude(), location.getLongitude()));
                    mapScrnViewModel.dropAddress.set(Address);
                    //if (Address.length() >= 40)
                    mapScrnViewModel.newdropAddress.set(getNewDropAddress(Objects.requireNonNull((Objects.requireNonNull(Address)))));
                    //else mapScrnViewModel.newdropAddress.set(Address);
                    Log.e("xxxMapFrag", "LocationGet: dropAddress ==> " + Address);
                    if (!Objects.requireNonNull(mapScrnViewModel.dropAddress.get()).isEmpty())
                        mapScrnViewModel.dropShown.set(true);

                /*  if (drop != null)
                        drop.remove();
                        drop = googleMap.addMarker(new MarkerOptions()
                           .position(new LatLng(location.getLatitude(), location.getLongitude()))
                           .title("Drop Location")
                           .anchor(0.5f, 0.5f)
                           .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.drop_marker)));*/

                }

            }
        }
    }

    private void callETA_API() {
        mapScrnViewModel.ETA_API();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.more_icon) {
            LoadProfileFrag();
        }

        if (id == R.id.fav_icon) {
            showAddFavDialog();
        }

        if (id == R.id.cancel_ic_drop) {
            clear_drop_et();
        }

        if (id == R.id.cancel_ic_pick) {
            clear_pickup_et();
        }

        if (id == R.id.myself_lay) {
            showMyselfDialog("");
        }

        if (id == R.id.confirm_loc_bt) {
            callETA_API();
            //do nothing
            mapScrnViewModel.pickupClicked.set(true);
            setPageViews(HOMETAG);
            if (sharedPrefence.GetBoolean(SharedPrefence.In_PickDrop))
                fragmentMapScrnBinding.mapsheet.mapSheetCoOrdLay.setVisibility(View.VISIBLE);
        }

        if (id == R.id.pickup) {
            setPageViews(PICKDROPTAG);
            mapScrnViewModel.show_dropui.set(false);
            mapScrnViewModel.pickdroptext.set(mapScrnViewModel.PickUp.get());
        }
    }


    BottomSheetDialog MyselfDialog;

    @SuppressLint("SetTextI18n")
    public void showMyselfDialog(String openType) {
        MyselfDialog = new BottomSheetDialog(getBaseAct());
        MyselfDialog.setContentView(R.layout.layout_myself);

        Button Skip = MyselfDialog.findViewById(R.id.bt_skip_myslf);
        Button Continue = MyselfDialog.findViewById(R.id.bt_continue_myslf);

        TextView Mtitle = MyselfDialog.findViewById(R.id.My_Tile);
        TextView Mdesc = MyselfDialog.findViewById(R.id.Mydesc);
        TextView MChsecnt = MyselfDialog.findViewById(R.id.Mychsecnt);

        RadioGroup radioGroupMyself = MyselfDialog.findViewById(R.id.rad_grp_myself);

        RadioButton cnt1 = MyselfDialog.findViewById(R.id.rdbt_contact1);
        RadioButton cnt2 = MyselfDialog.findViewById(R.id.rdbt_contact2);
        RadioButton cnt3 = MyselfDialog.findViewById(R.id.rdbt_contact3);

        RadioButton def_myself = MyselfDialog.findViewById(R.id.rdbt_myself);

        assert Skip != null;
        Skip.setText(mapScrnViewModel.skip.get());
        assert Continue != null;
        Continue.setText(mapScrnViewModel.mcontinue.get());
        assert Mtitle != null;
        Mtitle.setText(mapScrnViewModel.myself_title.get());
        assert Mdesc != null;
        Mdesc.setText(mapScrnViewModel.myself_desc.get());
        assert MChsecnt != null;
        MChsecnt.setText(mapScrnViewModel.choosecnt.get());

        if (def_myself != null)
            mapScrnViewModel.msg.set("Myself");
        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, "Myself");
        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, "Myself");

        getcheckedvalue(cnt1, cnt2, cnt3, def_myself);

        if (myselfContacts.size() == 0) {
            assert cnt1 != null;
            cnt1.setVisibility(View.GONE);
            assert cnt2 != null;
            cnt2.setVisibility(View.GONE);
            assert cnt3 != null;
            cnt3.setVisibility(View.GONE);
        } else if (myselfContacts.size() == 1) {
            assert cnt1 != null;
            cnt1.setText(myselfContacts.get(0).getName() + "  " + myselfContacts.get(0).getNumber());
            cnt1.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(0).getName()), null, null, null);
            assert cnt2 != null;
            cnt2.setVisibility(View.GONE);
            assert cnt3 != null;
            cnt3.setVisibility(View.GONE);
        } else if (myselfContacts.size() == 2) {
            assert cnt1 != null;
            cnt1.setText(myselfContacts.get(0).getName() + "  " + myselfContacts.get(0).getNumber());
            assert cnt2 != null;
            cnt2.setText(myselfContacts.get(1).getName() + "  " + myselfContacts.get(1).getNumber());
            cnt1.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(0).getName()), null, null, null);
            cnt2.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(1).getName()), null, null, null);
            assert cnt3 != null;
            cnt3.setVisibility(View.GONE);
        } else if (myselfContacts.size() == 3) {
            assert cnt1 != null;
            cnt1.setText(myselfContacts.get(0).getName() + "  " + myselfContacts.get(0).getNumber());
            assert cnt2 != null;
            cnt2.setText(myselfContacts.get(1).getName() + "  " + myselfContacts.get(1).getNumber());
            assert cnt3 != null;
            cnt3.setText(myselfContacts.get(2).getName() + "  " + myselfContacts.get(2).getNumber());
            cnt1.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(0).getName()), null, null, null);
            cnt2.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(1).getName()), null, null, null);
            cnt3.setCompoundDrawablesWithIntrinsicBounds(createRoundImg(myselfContacts.get(2).getName()), null, null, null);
        }

        assert radioGroupMyself != null;
        radioGroupMyself.setOnCheckedChangeListener((radioGroup, i) -> {

            int rdbtid = radioGroup.getCheckedRadioButtonId();

            if (rdbtid == R.id.rdbt_myself) {
                mapScrnViewModel.lastpicked.set(3);
                mapScrnViewModel.msg.set("Myself");
                mapScrnViewModel.name.set("Myself");
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, "Myself");
            } else if (rdbtid == R.id.rdbt_contact1) {
                mapScrnViewModel.lastpicked.set(0);
                mapScrnViewModel.msg.set(myselfContacts.get(0).getNumber());
                mapScrnViewModel.name.set(myselfContacts.get(0).getName());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(0).getNumber());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(0).getName());
            } else if (rdbtid == R.id.rdbt_contact2) {
                mapScrnViewModel.lastpicked.set(1);
                mapScrnViewModel.msg.set(myselfContacts.get(1).getNumber());
                mapScrnViewModel.name.set(myselfContacts.get(1).getName());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(1).getNumber());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(1).getName());
            } else if (rdbtid == R.id.rdbt_contact3) {
                mapScrnViewModel.lastpicked.set(2);
                mapScrnViewModel.msg.set(myselfContacts.get(2).getNumber());
                mapScrnViewModel.name.set(myselfContacts.get(2).getName());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(2).getNumber());
                sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(2).getName());
            } else if (rdbtid == -1) {
                mapScrnViewModel.lastpicked.set(4);
            }

        });

        RelativeLayout choose_contact = MyselfDialog.findViewById(R.id.lay_chse_cnat);

        if (choose_contact != null)
            choose_contact.setOnClickListener(view -> makecontactintent());

        Skip.setOnClickListener(view -> DismissAndMove(openType, 3));

        Continue.setOnClickListener(view -> {

            getcheckedvalue(cnt1, cnt2, cnt3, def_myself);

            Log.e("xxxMapFrag", sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no));
            Log.e("xxxMapFrag", sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_name));

            if (sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no) != null) {
                if (sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no).equalsIgnoreCase("Myself") && mapScrnViewModel.lastpicked.get() == 3) {
                    //mapScrnViewModel.lastpicked.set(3);
                    showMessage("Yourself ride on");
                    ShowMainContent("Myself");
                    DismissAndMove(openType, 3);
                } else {
                    if (mapScrnViewModel.lastpicked.get() == 0) {
                        mapScrnViewModel.lastpicked.set(0);
                        ShowMainContent(myselfContacts.get(0).getName());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(0).getNumber());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(0).getName());
                        showMessage("details will be shared with " + myselfContacts.get(0).getName());
                        DismissAndMove(openType, 0);
                    } else if (mapScrnViewModel.lastpicked.get() == 1) {
                        mapScrnViewModel.lastpicked.set(1);
                        ShowMainContent(myselfContacts.get(1).getName());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(1).getNumber());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(1).getName());
                        showMessage("details will be shared with " + myselfContacts.get(1).getName());
                        DismissAndMove(openType, 1);
                    } else if (mapScrnViewModel.lastpicked.get() == 2) {
                        mapScrnViewModel.lastpicked.set(2);
                        ShowMainContent(myselfContacts.get(2).getName());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_no, myselfContacts.get(2).getNumber());
                        sharedPrefence.savevalue(SharedPrefence.Myself_Contact_name, myselfContacts.get(2).getName());
                        showMessage("details will be shared with " + myselfContacts.get(2).getName());
                        DismissAndMove(openType, 2);
                    } else if (mapScrnViewModel.lastpicked.get() == 4) {
                        showMessage("Select Your Choice");
                    }
                }
            } else showMessage("Choose Contact Properly");
        });

        MyselfDialog.show();
    }

    private void getcheckedvalue(RadioButton cnt1, RadioButton cnt2, RadioButton cnt3, RadioButton def_myself) {
        switch (mapScrnViewModel.lastpicked.get()) {
            case 0: {
                cnt1.setChecked(true);
            }
            break;
            case 1: {
                cnt2.setChecked(true);
            }
            break;
            case 2: {
                cnt3.setChecked(true);
            }
            break;
            case 3: {
                def_myself.setChecked(true);
            }
            break;
            default: {
                //no checked values
            }
            break;
        }
    }

    private void DismissAndMove(String type, int pickval) {
        mapScrnViewModel.lastpicked.set(pickval);
        if (type.equalsIgnoreCase("DROP")) {
            MyselfDialog.dismiss();
            onClickConfirm(mapScrnViewModel.driverDatas, mapScrnViewModel.driverPins, "NORMAL");
        } else if (type.equalsIgnoreCase("RENT")) {
            mapScrnViewModel.show_rental_book_btn.set(false);

            MyselfDialog.dismiss();
            saveAndSendMyselfVal();
            mapScrnViewModel.mIsLoading.set(true);

            mapScrnViewModel.createReqAPI();

            //new Handler().postDelayed(() -> mapScrnViewModel.createReqAPI(), 500);

        } else MyselfDialog.dismiss();
    }

    @SuppressLint("SetTextI18n")
    private void ShowMainContent(String name) {
        if (name.equalsIgnoreCase("Myself")) {
            fragmentMapScrnBinding.mapPickDropLay.tvMyself.setText("Myself");
            fragmentMapScrnBinding.mapPickDropLay.imgMyself.setImageResource(R.drawable.ic_myself);
        } else {
            String name2;
            if (name.length() <= 4) {
                name2 = name;
            } else name2 = name.substring(0, 4);

            fragmentMapScrnBinding.mapPickDropLay.tvMyself.setText(name2);

            fragmentMapScrnBinding.mapPickDropLay.imgMyself.setImageDrawable(createRoundImg(name2));
        }
    }

    public Drawable createRoundImg(String Name) {
        String startLetter = String.valueOf(Name.charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int randomcolor = generator.getRandomColor();

        return TextDrawable.builder()
                .buildRoundRect(startLetter, randomcolor, 10);
    }

    private void makecontactintent() {
        Intent contactintent = new Intent(Intent.ACTION_PICK);
        contactintent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contactintent, Constants.REQUEST_SELECT_CONTACT);
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
    }

    private void clear_drop_et() {
        fragmentMapScrnBinding.mapPickDropLay.droplocEt.setText("");
        fragmentMapScrnBinding.mapPickDropLay.droplocEt.setHint("Choose Destination");
    }

    private void clear_pickup_et() {
        fragmentMapScrnBinding.mapPickDropLay.picklocEt.setText("");
        fragmentMapScrnBinding.mapPickDropLay.picklocEt.setHint("Choose Pickup");
    }

    BottomSheetDialog AddFavDialog;

    private void showAddFavDialog() {

        AddFavDialog = new BottomSheetDialog(getBaseAct());
        AddFavDialog.setContentView(R.layout.layout_add_favourite);

        TextView fav_address = AddFavDialog.findViewById(R.id.tv_fav_address);
        RadioButton fav_home = AddFavDialog.findViewById(R.id.rdbt_fav_home);
        RadioButton fav_work = AddFavDialog.findViewById(R.id.rdbt_fav_work);
        RadioButton fav_others = AddFavDialog.findViewById(R.id.rdbt_fav_others);
        EditText fav_others_et = AddFavDialog.findViewById(R.id.et_fav_others);
        Button fav_cancel = AddFavDialog.findViewById(R.id.bt_fav_cancel);
        Button fav_save = AddFavDialog.findViewById(R.id.bt_fav_save);
        RadioGroup fav_rd_gropu = AddFavDialog.findViewById(R.id.rdgroup_fav);

        LinearLayout showcontent = AddFavDialog.findViewById(R.id.fav_content_lay);
        LinearLayout loader = AddFavDialog.findViewById(R.id.fav_loader);

        TextView status_fav = AddFavDialog.findViewById(R.id.tv_fav_save_status);

        assert status_fav != null;
        status_fav.setText(mapScrnViewModel.fav_save.get());
        assert fav_cancel != null;
        fav_cancel.setText(mapScrnViewModel.cancel.get());
        assert fav_save != null;
        fav_save.setText(mapScrnViewModel.save.get());

        Objects.requireNonNull(fav_rd_gropu).setOnCheckedChangeListener((radioGroup, i) -> {

            int radiobtid = fav_rd_gropu.getCheckedRadioButtonId();

            if (radiobtid == Objects.requireNonNull(fav_home).getId()) {

                mapScrnViewModel.addressName.set("Home");

                if (fav_work != null)
                    fav_work.setChecked(false);
                if (fav_others != null)
                    fav_others.setChecked(false);
                if (fav_others_et != null)
                    fav_others_et.setVisibility(View.GONE);

            } else if (radiobtid == Objects.requireNonNull(fav_work).getId()) {

                mapScrnViewModel.addressName.set("Work");

                //fav_work.setChecked(true);
                fav_home.setChecked(false);
                if (fav_others != null)
                    fav_others.setChecked(false);
                if (fav_others_et != null)
                    fav_others_et.setVisibility(View.GONE);
            } else {

                if (fav_others_et != null)
                    mapScrnViewModel.addressName.set(fav_others_et.getText().toString());
                assert fav_others_et != null;
                fav_others_et.setVisibility(View.VISIBLE);

                //fav_others.setChecked(true);
                fav_work.setChecked(false);
                fav_home.setChecked(false);

            }

        });

        if (fav_others_et != null)
            fav_others_et.setVisibility(View.GONE);

        if (fav_address != null) {
            if (mapScrnViewModel.pickupClicked.get()) {
                if (mapScrnViewModel.pickupAddress.get() != null) {
                    fav_address.setText(mapScrnViewModel.pickupAddress.get());
                }
            } else if (mapScrnViewModel.dropClicked.get()) {
                if (mapScrnViewModel.dropAddress.get() != null) {
                    fav_address.setText(mapScrnViewModel.dropAddress.get());
                }
            }
        }

        fav_cancel.setOnClickListener(view -> AddFavDialog.dismiss());

        fav_save.setOnClickListener(view -> {

            String address_name = "";
            if (fav_others != null)
                if (fav_others.isChecked()) {
                    assert fav_others_et != null;
                    mapScrnViewModel.addressName.set(fav_others_et.getText().toString());
                    if (Objects.equals(mapScrnViewModel.addressName.get(), "")) {
                        showMessage("Enter Location Name");
                        return;
                    }
                    fav_others_et.setVisibility(View.VISIBLE);
                    address_name = fav_others_et.getText().toString();
                    mapScrnViewModel.addressName.set(address_name);
                } else if (fav_home != null)
                    if (fav_home.isChecked()) {
                        address_name = "Home";
                        mapScrnViewModel.addressName.set(address_name);
                    } else if (fav_work != null)
                        if (fav_work.isChecked()) {
                            address_name = "Work";
                            mapScrnViewModel.addressName.set(address_name);
                        }

            assert showcontent != null;
            showcontent.setVisibility(View.GONE);
            assert loader != null;
            loader.setVisibility(View.VISIBLE);
            status_fav.setText(mapScrnViewModel.fav_saving.get());

            HashMap<String, Object> favmap = new HashMap<>();

            if (!address_name.equals(""))
                favmap.put(Constants.NetworkParameters.Address_Name, mapScrnViewModel.addressName.get());

            favmap.put(Constants.NetworkParameters.pAddress, mapScrnViewModel.pickupAddress.get());

            favmap.put(Constants.NetworkParameters.pLat, Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).latitude);

            favmap.put(Constants.NetworkParameters.pLng, Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).longitude);

            mapScrnViewModel.calladdfavapi(favmap);

            //Call Add Favourites API
        });

        AddFavDialog.show();

    }

    @Override
    public void closeFavDialog() {

        if (AddFavDialog != null && AddFavDialog.isShowing()) {
            AddFavDialog.dismiss();
        }

    }

    @Override
    public void clearpicket() {
        clear_pickup_et();
    }

    @Override
    public void cleardropet() {
        clear_drop_et();
    }

    @Override
    public void onClickPickFavItem(FavouriteLocations.FavLocData favLocData) {
        //showMessage(favLocData.getPick_address() + " is Clicked");
        String favpickaddress = favLocData.getPick_address();
        double favdroplat = favLocData.getPick_lat();
        double favdroplng = favLocData.getPick_lng();
        LatLng droplatlng = new LatLng(favdroplat, favdroplng);

        mapScrnViewModel.dropLatLng.set(droplatlng);
        mapScrnViewModel.pickupLatLng.set(droplatlng);
        if (mapScrnViewModel.pickupClicked.get()) {

            mapScrnViewModel.pickupAddress.set(favpickaddress);
            mapScrnViewModel.newpickupAddress.set(favpickaddress);
            setPageViews(HOMETAG);
            if (mapScrnViewModel.pickupLatLng.get() != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).latitude, Objects.requireNonNull(mapScrnViewModel.pickupLatLng.get()).longitude), 14.0f));

                FirebaseHelper.queryDrivers(mapScrnViewModel.pickupLatLng.get());
            }
        } else if (mapScrnViewModel.dropClicked.get()) {
            mapScrnViewModel.dropClicked.set(true);
            mapScrnViewModel.dropAddress.set(favpickaddress);
            mapScrnViewModel.newdropAddress.set(favpickaddress);
            //move to Booking Screen
            if (mapScrnViewModel.dropAddress.get() != null && mapScrnViewModel.dropLatLng.get() != null)
                onClickConfirm(mapScrnViewModel.driverDatas, mapScrnViewModel.driverPins, "NORMAL");
            else showMessage("choose drop address properly");
        }
    }

    @Override
    public void onClickDropFavItem(FavouriteLocations.FavLocData favLocData) {
        String favdropaddress = favLocData.getPick_address();
        double favdroplat = favLocData.getPick_lat();
        double favdroplng = favLocData.getPick_lng();
        LatLng droplatlng = new LatLng(favdroplat, favdroplng);
        mapScrnViewModel.dropLatLng.set(droplatlng);
        mapScrnViewModel.dropAddress.set(favdropaddress);
        mapScrnViewModel.newdropAddress.set(favdropaddress);
        //move to Booking Screen
        if (mapScrnViewModel.dropAddress.get() != null && mapScrnViewModel.dropLatLng.get() != null) {
            onClickConfirm(mapScrnViewModel.driverDatas, mapScrnViewModel.driverPins, "NORMAL");
        } else showMessage("choose drop address properly");
    }

    @Override
    public void clikedcurrloc() {
        if (PickDropbottomsheet != null)
            PickDropbottomsheet.setState(STATE_HIDDEN);
        onClickCurrLocation();
    }

    @Override
    public void clickedlocateonmap() {
        if (PickDropbottomsheet != null)
            PickDropbottomsheet.setState(STATE_HIDDEN);
    }

    @Override
    public void clickedenterdeslater() {
        // Move To Booking Page
        cleardropaddress();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, MakeTripFrag.newInstance(mapScrnViewModel.pickupLatLng.get(), mapScrnViewModel.pickupAddress.get(), getViewModel().driverDatas, getViewModel().driverPins, "NORMAL"), MakeTripFrag.TAG)
                .commit();
    }

    @Override
    public void showMyselfDialogv(String type) {
        showMyselfDialog(type);
    }

    @Override
    public void callprofileapi() {
        callProfileAPIFromMap();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onclickrentalitem(RentalPackage rentalPackage) {
        mapScrnViewModel.Rental_Book_Btn.set(false);
        if (rentalPackage != null) {
            mapScrnViewModel.selected_Rent_Package.set(rentalPackage.getPackage_name());
            mapScrnViewModel.selected_Rent_PackID.set(rentalPackage.getId());
            //showMessage(rentalPackage.getName()+" : Package Is Clicked");
            mapScrnViewModel.is_Rental_item_Picked.set(true);

            if (rentalPackage.getTypesWithPrice().getData() != null)
                rentalPackageList = rentalPackage.getTypesWithPrice().getData();

            mapScrnViewModel.FireBase_etaModel = rentalPackage.getTypesWithPrice().getData();
            mapScrnViewModel.calculateETADuration(false);
            if (rentalPackageList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getAttachedcontext(), LinearLayoutManager.VERTICAL, false);
                rentalVehicAdapter = new RentalVehicAdapter(getAttachedcontext(), rentalPackageList, this);
                fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.rentalVehicRecycler.setLayoutManager(linearLayoutManager);
                fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.rentalVehicRecycler.setAdapter(rentalVehicAdapter);

                int i = 0;
                for (EtaModel model : rentalPackageList) {
                    if (model.getIsDefault() != null && rentalVehicAdapter != null) {
                        if (model.getIsDefault()) {
                            rentalVehicAdapter.defaultSelection(i);
                            break;
                        }
                    }
                    i++;
                }

                assert rentalVehicAdapter != null;
                rentalVehicAdapter.notifyDataSetChanged();
            } else {
                showMessage("No Vehicles Found");
            }
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void loadrentalpacks(List<RentalPackage> rentalPackageslist) {

        Log.d("Rental Packages while loading.", new Gson().toJson(rentalPackageslist));

        if (rentalPackageslist.size() > 0) {
            RentalPackagesAdapter rentalPackagesAdapter = new RentalPackagesAdapter(getContext(), rentalPackageslist, this);
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.rentalPackRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            fragmentMapScrnBinding.mapsheet.tabs.rentalFragView.rentalPackRecycler.setAdapter(rentalPackagesAdapter);
        } else showMessage("No Packages Available in your location");
    }

    @Override
    public void onClickEtaItemR(EtaModel rentalPackage) {
        mapScrnViewModel.RClickedZoneTypeID.set(rentalPackage.getZoneTypeId());
        mapScrnViewModel.selectedETATypeID.set(rentalPackage.getTypeId());
        mapScrnViewModel.Rental_Book_Btn.set(true);
        mapScrnViewModel.rbookcartypename.set(rentalPackage.getName());
    }

    @Override
    public void onClickInfoButtonR(EtaModel rentalPackage) {
        showInfoDialog(rentalPackage);
    }

    @Override
    public void refreshAdapter(List<EtaModel> etaModels) {
        Log.e("xxxMapFrag", "Refreshed Data in Map : " + new Gson().toJson(etaModels));
        if (etaModels.size() > 0) {
            rentalVehicAdapter.refreshData(etaModels);
        }
    }

    @Override
    public EtaModel getSelectedCar() {
        return rentalVehicAdapter.getSelectedCar();
    }

    @Override
    public void updateCarETA(String typeId, String eta) {
        if (rentalVehicAdapter != null) {
            rentalVehicAdapter.updateEta(typeId, eta);
        }
    }

    @Override
    public void openwaitingDialogAct(String id) {
        ((HomeAct) getBaseAct()).openWaitingDialog(id);
    }

    Dialog noservdialog;

    @Override
    public void open_no_serv_dialog() {
        noservdialog = new Dialog(getBaseAct(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        noservdialog.setContentView(R.layout.noserv_dialog_lay);

        if (noservdialog.getWindow() != null) {
            noservdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        noservdialog.setCancelable(false);
        noservdialog.setCanceledOnTouchOutside(false);

        TextView loc_detail = noservdialog.findViewById(R.id.location_detail_tv);
        TextView desc = noservdialog.findViewById(R.id.noserv_desc_tv);
        TextView change_loc = noservdialog.findViewById(R.id.nosev_chg_loc_btn);
        ImageView cancel = noservdialog.findViewById(R.id.noserv_cancel_ic);

        loc_detail.setText(mapScrnViewModel.pickupAddress.get());
        desc.setText(mapScrnViewModel.noserv_desc.get());
        change_loc.setText(mapScrnViewModel.noserv_chnge_loc_btn.get());

        change_loc.setOnClickListener(v -> {
            //dismiss and open pickdrop page
            noservdialog.dismiss();
            setPageViews(PICKDROPTAG);
        });

        cancel.setOnClickListener(v -> {
            //dismiss the dialog
            setPageViews(PICKDROPTAG);
            noservdialog.dismiss();
        });

        if (!getBaseActivity().isFinishing() && !noservdialog.isShowing())
            noservdialog.show();

    }

    @Override
    public void openFavDialog() {
        showAddFavDialog();
    }

    private void LoadProfileFrag() {
        getBaseAct().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                .commit();
    }

    private void setMAPBottomSheetBehaviour() {
        // LinearLayout ll = getBaseAct().findViewById(R.id.map_bottom_sheet);
        mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);
        mapbottomsheet.setHideable(false);
        mapbottomsheet.setPeekHeight(500);
        Log.e("xxxMapFrag", "MapSheet called in setmapbehaviour");
        mapbottomsheet.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        setCollapsed();

        mapbottomsheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {

                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //mapbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    }//
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        // homeViewModel.sheet_expanded.set(true);
                        //mapbottomsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
                       /* bottomSheet.requestLayout();
                        bottomSheet.invalidate();*/
                        //mapbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        // fragmentMapScrnBinding.currLoc.setVisibility(View.GONE);
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {

                    }
                    break;
                }
            }


            @Override
            public void onSlide(@NonNull @NotNull View view, float v) {

                ((LinearLayout) view).bringChildToFront(view);

             /*   switch (mapbottomsheet.getState()) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        setMapPaddingBotttom(v);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleMap.getCameraPosition().target));
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                }*/
               /* fragmentMapScrnBinding.currLoc.animate().y(v<= 0?
                                view.getY()+mapbottomsheet.getPeekHeight()-fragmentMapScrnBinding.currLoc.getHeight():
                                view.getHeight()-fragmentMapScrnBinding.currLoc.getHeight()).setDuration(0).start();*/

                //fragmentMapScrnBinding.currLoc.animate().scaleX(1-v).scaleY(1-v).setDuration(0).start();

            }
        });
    }

    private void setCollapsed() {
        mapbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onclickdaily() {
        mapbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onclickrental() {
        mapbottomsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onclickoutstation() {

    }

    @Override
    public void onclickuparrow() {
        setCollapsed();
    }

    @Override
    public void onclicksearchdes() {

    }

    @Override
    public void BackClickPickDrop() {
        fragmentMapScrnBinding.mapsheet.mapSheetCoOrdLay.setVisibility(View.VISIBLE);
        setPageViews(HOMETAG);
        //moveToMapPosition();
        mapScrnViewModel.pickupClicked.set(true);
        mapScrnViewModel.dropClicked.set(false);
        mapScrnViewModel.show_dropui.set(false);
    }

    @Override
    public void openDropScrn() {
        cleardropaddress();
        setPageViews(PICKDROPTAG);
        mapScrnViewModel.pickupClicked.set(false);
        mapScrnViewModel.dropClicked.set(true);

        mapScrnViewModel.show_dropui.set(true);
        mapScrnViewModel.pickdroptext.set(mapScrnViewModel.Drop.get());
    }

    private void cleardropaddress() {
        mapScrnViewModel.dropLatLng.set(null);
        mapScrnViewModel.dropAddress.set(null);
        mapScrnViewModel.newdropAddress.set(null);
    }

    @Override
    public void openhomemap() {
        setPageViews(HOMETAG);
        mapScrnViewModel.show_dropui.set(true);
    }

    public void backpressed() {
        if (sharedPrefence.GetBoolean(SharedPrefence.In_PickDrop)) {
            setPageViews(HOMETAG);
            mapScrnViewModel.pickupClicked.set(true);
            mapScrnViewModel.dropClicked.set(false);
            BackClickPickDrop();
        }
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public void loadfavdata(List<FavouriteLocations.FavLocData> favResData) {

        if (favResData.size() > 0) {
            fragmentMapScrnBinding.pickdropsheet.pickdropFavText.setVisibility(View.VISIBLE);
            fragmentMapScrnBinding.pickdropsheet.favouiteplaceRecycler.setVisibility(View.VISIBLE);
            FavouitesLocAdapter favouitesLocAdapter = new FavouitesLocAdapter(getBaseAct(), favResData, this, "DROPFAV");
            fragmentMapScrnBinding.mapsheet.tabs.dailyFragView.favouiteplaceRecycler.setLayoutManager(new LinearLayoutManager(getAttachedcontext()));
            fragmentMapScrnBinding.mapsheet.tabs.dailyFragView.favouiteplaceRecycler.setAdapter(favouitesLocAdapter);
            favouitesLocAdapter.notifyDataSetChanged();

            FavouitesLocAdapter favouitesLocAdapter2 = new FavouitesLocAdapter(getBaseAct(), favResData, this, "PICKFAV");
            fragmentMapScrnBinding.pickdropsheet.favouiteplaceRecycler.setLayoutManager(new LinearLayoutManager(getAttachedcontext()));
            fragmentMapScrnBinding.pickdropsheet.favouiteplaceRecycler.setAdapter(favouitesLocAdapter2);
            favouitesLocAdapter2.notifyDataSetChanged();
        } else {
            fragmentMapScrnBinding.pickdropsheet.pickdropFavText.setVisibility(View.GONE);
            fragmentMapScrnBinding.pickdropsheet.favouiteplaceRecycler.setVisibility(View.GONE);
        }
        Log.e("xxxMapFrag", "Loadfavdata method called and list :" + new Gson().toJson(favResData));

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        setPageViews(PICKDROPTAG);
        mapScrnViewModel.pickupClicked.set(true);
        mapScrnViewModel.show_dropui.set(false);
        mapScrnViewModel.pickdroptext.set(mapScrnViewModel.PickUp.get());
        mapScrnViewModel.pickdroptext.set(mapScrnViewModel.PickUp.get());
    }

    public void setPageViews(String PageName) {

        switch (PageName) {
            case HOMETAG: {

                if (mapbottomsheet == null)
                    mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);

                if (PickDropbottomsheet == null) {
                    PickDropbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.pickdropsheet.pickdroplay);
                }

                fragmentMapScrnBinding.currLoc.setVisibility(View.VISIBLE);

                fragmentMapScrnBinding.mapsheet.mapSheetCoOrdLay.setVisibility(View.VISIBLE);

                moveToMapPosition();
                mapScrnViewModel.pickupClicked.set(true);

                Log.e("xxxMapFrag", "set Home Page Called");

                bounceanim = AnimationUtils.loadAnimation(MyApp.getmContext(), R.anim.bounce);
                fragmentMapScrnBinding.mapHomeLay.llhomebar.startAnimation(bounceanim);

                mapScrnViewModel.in_home.set(true);
                mapScrnViewModel.in_booking.set(false);
                mapScrnViewModel.in_pickDrop.set(false);

                sharedPrefence.saveBoolean(SharedPrefence.In_Home, true);
                sharedPrefence.saveBoolean(SharedPrefence.In_Book, false);
                sharedPrefence.saveBoolean(SharedPrefence.In_PickDrop, false);

                if (mapbottomsheet != null) {
                    Log.e("xxxMapFrag", "MapSheet called in PageView");
                    mapbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mapbottomsheet.setHideable(false);
                }

                if (PickDropbottomsheet != null) {
                    PickDropbottomsheet.setState(STATE_HIDDEN);
                }
            }
            break;
            case PICKDROPTAG: {

                if (mapbottomsheet == null)
                    mapbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.mapsheet.mapBottomSheet);

                if (PickDropbottomsheet == null) {
                    PickDropbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.pickdropsheet.pickdroplay);
                }

                Log.e("xxxMapFrag", "Pick Drop Page Called");

                mapbottomsheet.setHideable(true);
                mapbottomsheet.setState(STATE_HIDDEN);
                mapbottomsheet.setHideable(false);

                PickDropbottomsheet.setHideable(true);

                topanim = AnimationUtils.loadAnimation(MyApp.getmContext(), R.anim.top);
                fragmentMapScrnBinding.mapPickDropLay.totalPickdropLay.startAnimation(topanim);

                mapScrnViewModel.in_pickDrop.set(true);
                mapScrnViewModel.in_home.set(false);
                mapScrnViewModel.in_booking.set(false);

                sharedPrefence.saveBoolean(SharedPrefence.In_PickDrop, true);
                sharedPrefence.saveBoolean(SharedPrefence.In_Book, false);
                sharedPrefence.saveBoolean(SharedPrefence.In_Home, false);

                PickDropbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            break;
        }
    }

    private void set_PickDrop_Sheet() {

        PickDropbottomsheet = BottomSheetBehavior.from(fragmentMapScrnBinding.pickdropsheet.pickdroplay);

        PickDropbottomsheet.setHideable(true);

        PickDropbottomsheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        PickDropbottomsheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {

                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        bottomSheet.requestLayout();

                     /*   new Thread(() -> {
                            PickDropbottomsheet.setPeekHeight(getDynamicPickSheetHeight());
                        }).start();*/

                        getBaseAct().runOnUiThread(() -> PickDropbottomsheet.setPeekHeight(getDynamicPickSheetHeight()));

                    }
                    break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        PickDropbottomsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheet.requestLayout();
                        new Thread(() -> PickDropbottomsheet.setPeekHeight(getDynamicPickSheetHeight())).start();
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {

                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {

                    }
                    case BottomSheetBehavior.STATE_SETTLING: {

                    }
                    break;
                }
                getBaseAct().runOnUiThread(() -> bottomSheet.post(() -> {
                    //workaround for the bottomsheet  bug
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();
                }));
            }

            @Override
            public void onSlide(@NonNull @NotNull View view, float v) {

                //View optionsview = fragmentMapScrnBinding.pickdropsheet.pickdropOptionsLay;

             /*   optionsview.animate().y(v <= 0 ?
                        view.getY() + PickDropbottomsheet.getPeekHeight() - optionsview.getHeight():
                        view.getHeight() - optionsview.getHeight()).setDuration(0).start();*/
            }
        });
    }

    public int getDynamicPickSheetHeight() {
        int pickdroplayheight = fragmentMapScrnBinding.mapPickDropLay.totalPickdropLay.getHeight();

        int layoutheight = fragmentMapScrnBinding.mapCoOrdLay.getHeight();

        return layoutheight - pickdroplayheight;
    }

    public String getNewPickAddress(String pickaddress) {
        if (pickaddress.length() >= 40) {
            return pickaddress.substring(0, 40) + "...";
        } else return pickaddress;
    }

    public String getNewDropAddress(String dropaddress) {
        if (dropaddress.length() >= 40) {
            return dropaddress.substring(0, 40) + "...";
        } else return dropaddress;
    }

    @Override
    public void onAttach(Context context) {
        Log.e("xxxMapFrag", "onAttach Called in MapFrag");
        super.onAttach(context);
    }

    BottomSheetDialog RentalInfoDialog;

    @SuppressLint("SetTextI18n")
    private void showInfoDialog(EtaModel getEtaModel) {

        RentalInfoDialog = new BottomSheetDialog(getBaseAct());

        RentalInfoDialog.setContentView(R.layout.layout_car_desc);

        TextView Carname = RentalInfoDialog.findViewById(R.id.info_carnam);
        TextView Cardesc = RentalInfoDialog.findViewById(R.id.info_cardesc);
        //TextView tot_seats = InfoDialog.findViewById(R.id.info_carseats);
        TextView Sprtd_Vehic = RentalInfoDialog.findViewById(R.id.info_sprtd_vhcls);
        TextView tot_pay = RentalInfoDialog.findViewById(R.id.info_totalpay);

        ImageView carImage = RentalInfoDialog.findViewById(R.id.info_carimg);

        RelativeLayout donebt = RentalInfoDialog.findViewById(R.id.info_done_bt);

        if (tot_pay != null) {
            tot_pay.setText("Total Payable : " + getEtaModel.getCurrency() + getEtaModel.getFare_amount());
        }

        if (Sprtd_Vehic != null)
            Sprtd_Vehic.setText(getEtaModel.getSupported_vehicles());

        if (Cardesc != null)
            Cardesc.setText(getEtaModel.getDescription());

        if (carImage != null) {
            if (getContext() != null)
                Glide
                        .with(getContext())
                        .load(getEtaModel.getIcon())
                        .into(carImage);
        }

        if (donebt != null)
            donebt.setOnClickListener(view -> RentalInfoDialog.dismiss());

        if (Carname != null)
            Carname.setText(getEtaModel.getName());

        RentalInfoDialog.show();

    }

    public void open_booking_page(String id) {
        //Previous trip Request In Progress call booking Page

        if (!mapScrnViewModel.rental_clicked.get()) {

            /**
             * Normal Trip Request usually handled by Booking Page
             * If user Close app during Waiting Process of Trip Request This Block will be called
             * */

            mapScrnViewModel.pickupAddress.set(sharedPrefence.Getvalue(SharedPrefence.Book_Pick_Address));
            mapScrnViewModel.dropAddress.set(sharedPrefence.Getvalue(SharedPrefence.Book_Drop_Address));

            if (!sharedPrefence.GetLatlng(SharedPrefence.Book_Pick_Lat).equals("") &&
                    !sharedPrefence.GetLatlng(SharedPrefence.Book_Pick_lng).equals("") &&
                    !sharedPrefence.GetLatlng(SharedPrefence.Book_Drop_Lat).equals("") &&
                    !sharedPrefence.GetLatlng(SharedPrefence.Book_Drop_lng).equals("")
            ) {
                double plat = Double.parseDouble(sharedPrefence.GetLatlng(SharedPrefence.Book_Pick_Lat));
                double plng = Double.parseDouble(sharedPrefence.GetLatlng(SharedPrefence.Book_Pick_lng));
                double dlat = Double.parseDouble(sharedPrefence.GetLatlng(SharedPrefence.Book_Drop_Lat));
                double dlng = Double.parseDouble(sharedPrefence.GetLatlng(SharedPrefence.Book_Drop_lng));

                mapScrnViewModel.pickupLatLng.set(getlatlng_from_String(plat, plng));
                mapScrnViewModel.dropLatLng.set(getlatlng_from_String(dlat, dlng));
            }

            if (!getBaseActivity().isFinishing()) {
                onClickConfirm(getViewModel().driverDatas, getViewModel().driverPins, "EXISTS");
            }

        } else {
            /**
             * Rental Trip Request
             * */

            /*if (mapScrnViewModel.Rent_REQID.get() != null)
                ((HomeAct) getBaseAct()).openWaitingDialog(mapScrnViewModel.Rent_REQID.get());*/

            if (!getBaseActivity().isFinishing()) {
                onClickConfirm(getViewModel().driverDatas, getViewModel().driverPins, "EXISTS");
            }

        }

    }


    public LatLng getlatlng_from_String(Double lat, Double lng) {
        return new LatLng(lat, lng);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapScrnViewModel.show_rental_book_btn.set(true);
    }
}
