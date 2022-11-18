package com.tyt.client.ui.homeScreen.makeTrip;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.arsy.maps_library.MapRipple;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.LayoutDateTimePickBinding;
import com.tyt.client.databinding.MakeTripLayBinding;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.OffersResponseData;
import com.tyt.client.retro.responsemodel.Type;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.mapFrag.adapter.CarsTypesAdapter;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class MakeTripFrag extends BaseFragment<MakeTripLayBinding, MakeTripViewModel> implements MakeTripNavigator, OnMapReadyCallback, View.OnClickListener, LocationListener {

    public static final String TAG = "MakeTripFrag";
    public static final String NODRIVERS = "no_drivers";
    public static final String ALLDRIVERSBUSY = "drivers_busy";
    public static final String TRIPACCEPTED = "trip_accepted";
    public static final String TRIPSEARCHING = "trip_searching";

    @Inject
    MakeTripViewModel makeTripViewModel;
    public MakeTripLayBinding makeTripLayBinding;
    public LayoutDateTimePickBinding layoutDateTimePickBinding;

    private GoogleMap googleMap;
    MapRipple mapRipple;

    static LatLng pLatLng, dLatLng = null;

    static String pAddress, dAddress = null;

    CarsTypesAdapter carsTypesAdapter;
    EtaModel getEtaModel;

    @Inject
    SharedPrefence sharedPrefence;

    static HashMap<String, String> driverData;
    static HashMap<String, Marker> driverPin;

    BottomSheetBehavior behavior;
    BottomSheetBehavior bookingsheet;
    BottomSheetBehavior progress_sheet;

    ShimmerFrameLayout MakeShimmerLay;

    public static String opentype, rentrip_id = "";

    public MakeTripFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MakeTripFrag newInstance(LatLng pickLatLng, LatLng dropLatLng, String pickAddress, String dropAddress, HashMap<String, String> driverDatas, HashMap<String, Marker> driverPins, String TYPE) {
        Log.d("xxxMakeTripFrag", "newInstance(1) called with: pickLatLng = [" + pickLatLng + "], dropLatLng = [" + dropLatLng + "], pickAddress = [" + pickAddress + "], dropAddress = [" + dropAddress + "], driverDatas = [" + driverDatas + "], driverPins = [" + driverPins + "]");
        pLatLng = pickLatLng;
        dLatLng = dropLatLng;
        pAddress = pickAddress;
        dAddress = dropAddress;
        driverData = driverDatas;
        driverPin = driverPins;
        opentype = TYPE;
        return new MakeTripFrag();
    }

    public static MakeTripFrag newInstance(LatLng pickLatLng, String pickAddress, HashMap<String, String> driverDatas, HashMap<String, Marker> driverPins, String TYPE) {
        Log.d("xxxMakeTripFrag", "MakeTripFrag(2) called with: pickLatLng = [" + pickLatLng + "], pickAddress = [" + pickAddress + "], driverDatas = [" + driverDatas + "], driverPins = [" + driverPins + "]");
        pLatLng = pickLatLng;
        pAddress = pickAddress;
        driverData = driverDatas;
        driverPin = driverPins;
        dLatLng = null;
        dAddress = null;
        opentype = TYPE;
        return new MakeTripFrag();
    }

    public static MakeTripFrag newRentInstance(LatLng pickLatLng, String pickAddress, HashMap<String, String> driverDatas, HashMap<String, Marker> driverPins, String TYPE, String RentID) {
        Log.d("xxxMakeTripFrag", "MakeTripFrag(2) called with: pickLatLng = [" + pickLatLng + "], pickAddress = [" + pickAddress + "], driverDatas = [" + driverDatas + "], driverPins = [" + driverPins + "]");
        pLatLng = pickLatLng;
        pAddress = pickAddress;
        driverData = driverDatas;
        driverPin = driverPins;
        dLatLng = null;
        dAddress = null;
        opentype = TYPE;
        rentrip_id = RentID;
        return new MakeTripFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
        super.onDestroyView();
        stopShim();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeTripLayBinding = getViewDataBinding();
        makeTripViewModel.setNavigator(this);

        makeTripViewModel.rental_request.set(!rentrip_id.equals(""));

        if (dAddress != null) {
            sharedPrefence.savevalue(SharedPrefence.Book_Pick_Address, pAddress);
            sharedPrefence.savevalue(SharedPrefence.Book_Drop_Address, dAddress);
            sharedPrefence.savelatlng(SharedPrefence.Book_Pick_Lat, pLatLng.latitude);
            sharedPrefence.savelatlng(SharedPrefence.Book_Pick_lng, pLatLng.longitude);
            sharedPrefence.savelatlng(SharedPrefence.Book_Drop_Lat, dLatLng.latitude);
            sharedPrefence.savelatlng(SharedPrefence.Book_Drop_lng, dLatLng.longitude);
        } else {
            sharedPrefence.savevalue(SharedPrefence.Book_Pick_Address, pAddress);
            sharedPrefence.savelatlng(SharedPrefence.Book_Pick_Lat, pLatLng.latitude);
            sharedPrefence.savelatlng(SharedPrefence.Book_Pick_lng, pLatLng.longitude);
            sharedPrefence.savelatlng(SharedPrefence.Book_Drop_Lat, 0.0);
            sharedPrefence.savelatlng(SharedPrefence.Book_Drop_lng, 0.0);
        }

        if (makeTripViewModel.REQID.get() == null)
            makeTripViewModel.REQID.set(sharedPrefence.GetLatlng(SharedPrefence.REQUEST_ID));

        View pickerview = getLayoutInflater().inflate(R.layout.layout_date_time_pick, null);

        layoutDateTimePickBinding = LayoutDateTimePickBinding.inflate(getLayoutInflater(), (ViewGroup) pickerview, false);

        layoutDateTimePickBinding.setViewModel(makeTripViewModel);

        MakeShimmerLay = view.findViewById(R.id.shim_book);

        makeTripViewModel.myself_contact.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no));

        makeTripViewModel.myself_contact_name.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_name));

        makeTripViewModel.myself_trip.set(sharedPrefence.Getvalue(SharedPrefence.Myself_Contact_no).equalsIgnoreCase("Myself"));

        if (makeTripViewModel.myself_contact_name.get() != null && !makeTripViewModel.myself_contact_name.get().isEmpty()) {
            setMyselfContactDetails(Objects.requireNonNull(makeTripViewModel.myself_contact_name.get()));
        } else if (Objects.requireNonNull(makeTripViewModel.myself_contact_name.get()).equalsIgnoreCase("Myself")) {
            makeTripLayBinding.bookingSheet.tvMyselfBook.setText("Myself");
            makeTripLayBinding.bookingSheet.imgMyselfBook.setImageResource(R.drawable.ic_myself);
        }

        startShim();

        if (pLatLng != null)
            makeTripViewModel.pickupLatLng.set(pLatLng);
        if (pAddress != null)
            makeTripViewModel.pickupAddress.set(pAddress);

        if (dAddress != null || dLatLng != null) {
            makeTripViewModel.dropLatLng.set(dLatLng);
            if (dAddress != null)
                makeTripViewModel.dropAddress.set(dAddress);
            makeTripViewModel.isPromoShown.set(true);
        }

        makeTripViewModel.showdropbookui.set(dLatLng != null && dAddress != null);

        setOffersSheets();

        set_Booking_Sheet();

        set_Searching_Sheet();

        makeTripViewModel.driverData = driverData;
        makeTripViewModel.driverPin = driverPin;

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(requireActivity()));
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (!Places.isInitialized()) {
            Places.initialize(getAttachedcontext(), Constants.PlaceApi_key);
        }

        PlacesClient placesClient = Places.createClient(getAttachedcontext());

        makeTripLayBinding.searchSheet.cancelButtonBook.setOnClickListener(this);
        makeTripLayBinding.searchSheet.tryAgainSearchbook.setOnClickListener(this);

        // makeTripViewModel.EtaApi();

    }

    private void setMyselfContactDetails(String cname) {
        if (cname.equalsIgnoreCase("Myself")) {
            //default one will be applied no changes
            makeTripLayBinding.bookingSheet.tvMyselfBook.setText("Myself");
            makeTripLayBinding.bookingSheet.imgMyselfBook.setImageResource(R.drawable.ic_myself);
        } else {
            String name2 = "";
            if (cname.length() <= 4) {
                name2 = cname;
            } else name2 = cname.substring(0, 4);

            makeTripLayBinding.bookingSheet.tvMyselfBook.setText(name2);

            String startLetter = String.valueOf(cname.charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL;

            int randomcolor = generator.getRandomColor();

            TextDrawable drawletterimg = TextDrawable.builder()
                    .buildRoundRect(startLetter, randomcolor, 10);

            makeTripLayBinding.bookingSheet.imgMyselfBook.setImageDrawable(drawletterimg);
        }

    }

    private void startShim() {
        MakeShimmerLay.startShimmer();
    }

    private void stopShim() {
        MakeShimmerLay.startShimmer();
    }

    Marker pickMarker, dropMarker;

    private void PlaceMarker() {
        if (pickMarker != null)
            pickMarker.remove();
        pickMarker = googleMap.addMarker(new MarkerOptions()
                .position(pLatLng)
                .title("Pickup Location")
                .anchor(0.5f, 0.5f)
                .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.pick_marker)));

        if (dropMarker != null)
            dropMarker.remove();

        if (dLatLng != null)
            dropMarker = googleMap.addMarker(new MarkerOptions()
                    .position(dLatLng)
                    .title("Drop Location")
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.getBitmapDescriptor(getActivity(), R.drawable.drop_marker)));

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
        googleMap.setPadding(0, 0, 0, half_height);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onStart() {
        super.onStart();
        makeTripViewModel.isScreenAvailable = true;

        LocalBroadcastManager.getInstance(getAttachedcontext()).registerReceiver(tripStatus, new IntentFilter(Constants.BroadcastsActions.CANCEL_RECEIVER));
    }

    // Receieve the broadcast if the driver cancel the trip
    final BroadcastReceiver tripStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            close_search_sheet();

            //move back to rental tab by Removing current fragment
            if (makeTripViewModel.rental_request.get()) {
                removeFrag();
            }
        }
    };

    @Override
    public void removeFrag() {
        rentrip_id = "";
        makeTripViewModel.rental_request.set(false);
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commitNow();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRippleAnimation();
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
            @Override
            public void onResult(@NotNull LocationSettingsResult result) {
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
                            status.startResolutionForResult(requireActivity(), Constants.REQUEST_CODE_ENABLING_GOOGLE_LOCATION);
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
    public Context getBaseAct() {

        Context context = getContext();

        try {
            if (isAdded()) {
                context = requireActivity();
            } else {
                //context = requireActivity().getApplicationContext();
                context = MyApp.getmContext();
            }
        } catch (IllegalStateException e) {
            showMessage("Try Again..");
        }
        return context;
    }

    @Override
    public Context getAttachedcontext() {
        return getContext();
    }

    @Override
    public void onClickBackImg() {
        if (requireActivity().getSupportFragmentManager().findFragmentByTag(MakeTripFrag.TAG) != null) {
            ((HomeAct) requireActivity()).ShowHideBar(true);
            Fragment frag = Objects.requireNonNull(requireActivity()).getSupportFragmentManager().findFragmentByTag(MakeTripFrag.TAG);
            if (frag != null)
                Objects.requireNonNull(requireActivity()).getSupportFragmentManager().beginTransaction().remove(frag).commit();
        } else {
            Log.e("elkfjefjke", "lejfioe");
        }
    }

    @Override
    public void loadTypes(List<Type> typeList) {

    }

    String clickedID, clickedTypeID;

    @Override
    public void clickedTypes(Type type) {
        if (type != null) {
            clickedID = type.getId();
            clickedTypeID = type.getTypeID();
        }
    }

    @Override
    public void onClickNext(HashMap<String, String> driverData, HashMap<String, Marker> driverPin) {
       /* if (getEtaModel != null) {
            if (makeTripViewModel.dropLatLng.get() != null && makeTripViewModel.dropAddress != null)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.fragment_frame, BookingFrag.newInstance(getEtaModel, makeTripViewModel.pickupAddress.get(), makeTripViewModel.dropAddress.get(), makeTripViewModel.pickupLatLng.get(), makeTripViewModel.dropLatLng.get(), driverData, driverPin, makeTripViewModel.clickedTYpeID), BookingFrag.TAG)
                        .commit();
            else
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .disallowAddToBackStack()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.fragment_frame, BookingFrag.newInstance(getEtaModel, makeTripViewModel.pickupAddress.get(), makeTripViewModel.pickupLatLng.get(), driverData, driverPin, makeTripViewModel.clickedTYpeID), BookingFrag.TAG)
                        .commit();
        }*/
    }

    @Override
    public void loadEta(EtaModel data) {
        if (data != null)
            Log.d("xxxMakeTrip", "loadEta(): getDriverArivalEstimation = [" + data.getDriverArivalEstimation() != null ? data.getDriverArivalEstimation() : "xxx" + "]" + "getName:[" + data.getName() != null ? data.getName() : "xxxx" + "] getDistance:[" + data.getDistance() + "]");
        getEtaModel = data;
        makeTripViewModel.onClickTypes.set(true);
        makeTripViewModel.carModel.set(data.getName());
        makeTripViewModel.distance.set(data.getDistance() + " " + data.getUnitInWordsWithoutLang());

        if (!data.getDriverArivalEstimation().equalsIgnoreCase("--")) {
            makeTripViewModel.showTYpes();
            makeTripViewModel.ETA.set("ETA " + data.getDriverArivalEstimation());
        } else
            makeTripViewModel.ETA.set("NA");

    }

    @Override
    public void setNoDrivers() {
        makeTripViewModel.ETA.set("NA");
        Toast.makeText(getAttachedcontext(), "No driver for this type", Toast.LENGTH_SHORT).show();
        makeTripViewModel.showTYpes();
    }

    Dialog dialog_promo;

    @Override
    public void onClickPromAlert() {
        if (dialog_promo != null)
            if (dialog_promo.isShowing()) {
                dialog_promo.dismiss();
                dialog_promo = null;
            }

        dialog_promo = new Dialog(getActivity());
        dialog_promo.setContentView(R.layout.promo_lay);

        if (dialog_promo.getWindow() != null) {
            dialog_promo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final EditText promoValue = dialog_promo.findViewById(R.id.promo_edit);
        CardView submit = dialog_promo.findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(promoValue.getText().toString())) {
                dialog_promo.dismiss();
                if (!clickedID.isEmpty() && !clickedTypeID.isEmpty()) {
                    makeTripViewModel.promoValue.set(promoValue.getText().toString());
                }
            } else
                Toast.makeText(getActivity(), "Fields Should not empty", Toast.LENGTH_SHORT).show();

        });

        dialog_promo.setCanceledOnTouchOutside(true);
        dialog_promo.show();
    }

    @Override
    public void loadNewEta(List<EtaModel> etaModel) {

        if (etaModel.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getAttachedcontext(), LinearLayoutManager.VERTICAL, false);
            carsTypesAdapter = new CarsTypesAdapter(getAttachedcontext(), etaModel, this);
            makeTripLayBinding.bookingSheet.typesRecycle.setLayoutManager(linearLayoutManager);
            makeTripLayBinding.bookingSheet.typesRecycle.setAdapter(carsTypesAdapter);

            int i = 0;
            for (EtaModel model : etaModel) {
                if (model.getIsDefault() != null && carsTypesAdapter != null) {
                    if (model.getIsDefault()) {
                        carsTypesAdapter.defaultSelection(i);
                        break;
                    }
                }
                i++;
            }

            //later fix
            /*new Handler().postDelayed(() -> {
                rotateanim = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.anticlock);
                makeTripLayBinding.mapBookLay.backimgbook.startAnimation(rotateanim);
            }, 2000);*/

        } else {
            //show specific layout
        }

    }

    @Override
    public void onClickEtaItem(EtaModel etaModel) {

        makeTripViewModel.bookcartypename.set(etaModel.getTypeName());

        Log.e("paymentTYpe---", "type---" + etaModel.getPaymentType().contains("cash"));
        if (etaModel.getPaymentType().contains("card")) {
            makeTripViewModel.cardAvail.set(true);
        }
        if (etaModel.getPaymentType().contains("cash")) {
            makeTripViewModel.cashAvail.set(true);
        }
        if (etaModel.getPaymentType().contains("wallet")) {
            makeTripViewModel.walletAvail.set(true);
        }
        makeTripViewModel.ClickedETATypeID.set(etaModel.getTypeId());
        makeTripViewModel.ClickedZoneTypeID.set(etaModel.getZoneTypeId());
        makeTripViewModel.showTYpes();
    }

    private final SimpleDateFormat mtimeFormatter = new SimpleDateFormat("kk:mm", Locale.US);
    private final SimpleDateFormat mdateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    BottomSheetDialog DateandTimePickDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClickSchedule() {

        DateandTimePickDialog = new BottomSheetDialog(getBaseAct());
        DateandTimePickDialog.setContentView(R.layout.layout_date_time_pick);

        Button Confirm = DateandTimePickDialog.findViewById(R.id.bt_datetimepic_confirm);
        TextView seletedtime = DateandTimePickDialog.findViewById(R.id.tv_selected_timebl);
        TextView Reset = DateandTimePickDialog.findViewById(R.id.tv_reset_bl);
        SingleDateAndTimePicker datetimepicker = DateandTimePickDialog.findViewById(R.id.single_day_picker);

        if (seletedtime != null) {
            seletedtime.setText("Today");
        }

        if (Reset != null) {
            Reset.setOnClickListener(view -> {
                getViewModel().onCLickReset(view);
                DateandTimePickDialog.dismiss();
            });

        }

        if (datetimepicker != null)
            datetimepicker.addOnDateChangedListener((displayed, date) -> {
                Log.e("selectedDatee----", "dateSelected---" + date.toString());

                long different = 0;

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

                            makeTripViewModel.dateChoose.set(sdfDate.format(date));
                            Log.e("dataFomat-----", "format--" + makeTripViewModel.dateChoose.get());
                            makeTripViewModel.isDateChoosen.set(true);
                            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
                            String displayValue = timeFormatter.format(date);
                            String displayDate = dateFormatter.format(date);
                            makeTripViewModel.splittedTime.set(displayValue);
                            makeTripViewModel.splittedDate.set(displayDate);

                            makeTripViewModel.splitdatetime.set(displayDate + ", " + displayValue);

                            seletedtime.setText(makeTripViewModel.splitdatetime.get());

                            Log.e("timeValue---", "time---" + displayDate);

                            makeTripViewModel.isDriversAvailable.set(true);

                        } else {
                            Toast.makeText(getAttachedcontext(), "Time should be 60 mins greater", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        makeTripViewModel.dateChoose.set(sdfDate.format(date));
                        Log.e("dataFomat-----", "format--" + makeTripViewModel.dateChoose.get());
                        makeTripViewModel.isDateChoosen.set(true);
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM ");
                        String displayValue = timeFormatter.format(date);
                        String displayDate = dateFormatter.format(date);
                        makeTripViewModel.splittedTime.set(displayValue);
                        makeTripViewModel.splittedDate.set(displayDate);

                        makeTripViewModel.splitdatetime.set(displayDate + ", " + displayValue);

                        if (seletedtime != null) {
                            seletedtime.setText(makeTripViewModel.splitdatetime.get());
                        }
                        makeTripViewModel.isDriversAvailable.set(true);
                        Log.e("timeValue---", "time---" + displayDate);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            });

        if (Confirm != null)
            Confirm.setOnClickListener(view -> {
                DateandTimePickDialog.dismiss();
            });

        DateandTimePickDialog.show();
    }

    @Override
    public void onCLickPayment() {
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    @Override
    public void onCLickPromo() {
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        if (Objects.requireNonNull(makeTripViewModel.clickedPromoCode.get()).isEmpty())
            makeTripViewModel.loadOffersApi();
        else makeTripViewModel.promoAppliedApi(makeTripViewModel.clickedPromoCode.get());
    }


    @Override
    public void openHomePage() {
        startActivity(new Intent(getAttachedcontext(), HomeAct.class));
    }

    @Override
    public void loadOffersData(List<OffersResponseData> offersResponseData) {
        OffersAdapter offersAdapter = new OffersAdapter(getAttachedcontext(), offersResponseData, this);
        makeTripLayBinding.promoSheet.offersRecycle.setLayoutManager(new LinearLayoutManager(getAttachedcontext()));
        makeTripLayBinding.promoSheet.offersRecycle.setAdapter(offersAdapter);
    }

    @Override
    public void clickedOferItem(OffersResponseData offersResponseData) {
        makeTripViewModel.clickedPromoCode.set(offersResponseData.getCode());
        makeTripViewModel.promoAppliedApi(offersResponseData.getCode());
        closesheet();
    }

    @Override
    public void removeclickedpromo(OffersResponseData offersResponseData) {
        makeTripViewModel.clickedPromoCode.set("");
        makeTripViewModel.removepromocode.set(true);
        makeTripViewModel.EtaApi();
        closesheet();
    }

    @Override
    public void closesheet() {
        if (behavior == null)
            behavior = BottomSheetBehavior.from(makeTripLayBinding.promoSheet.sheetBehaviour);

        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void opensearchsheet() {
        open_search_sheet();
        startRippleAnimation();
        setSearchState(TRIPSEARCHING);
    }

    @Override
    public void closesearchsheet() {
        close_search_sheet();
    }

    @Override
    public BaseActivity actbase() {
        return getBaseActivity();
    }

    @Override
    public void onClickCloseBottom() {
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setHideable(false);
    }

    /**
     * callback to get notified that {@link GoogleMap} is loaded
     **/
    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap = googleMap;

        makeTripViewModel.mGoogleMap = googleMap;

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.uber_map));
        googleMap.setTrafficEnabled(true);

        googleMap.setOnMapLoadedCallback(() -> {
            //PlaceMarker();
            makeTripViewModel.addLastKnownMarkers();
            makeTripViewModel.EtaApi();
            if (dLatLng != null && dAddress != null && !dAddress.isEmpty()) {
                makeTripViewModel.drawroute();
            } else {
                PlaceMarker();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("xxxMakeTrip", "Onresume Method Called");

        if (opentype.equalsIgnoreCase("EXISTS")) {
            open_search_sheet();
        }

        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, true);
        ((HomeAct) requireActivity()).ShowHideBar(false);

        if (progress_sheet != null) {
            if (progress_sheet.getState() == BottomSheetBehavior.STATE_COLLAPSED || progress_sheet.getState() == BottomSheetBehavior.STATE_EXPANDED || progress_sheet.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                open_search_sheet();
            }
        } else {
            close_search_sheet();
        }

    }

    @Override
    public void closeWaitingDialog(boolean b) {
        Log.e("xxxMakeTrip", "close waiting dialog method called");
        sharedPrefence.saveBoolean(SharedPrefence.Request_in_Progress, false);

        stopRippleAnimation();

        if (b) {
            close_search_sheet();
            if (isAdded()) {
                startActivity(new Intent(requireActivity(), HomeAct.class));
               /* if (requireActivity().getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG) != null)
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(new BookingFrag()).commit();*/
            } else showMessage("Try Again Later");

        } else {
            //Trip Cancelled By User or Driver
            if (makeTripViewModel.isCancelledClick.get()) {
                close_search_sheet();
            } else if (makeTripViewModel.nodriverfound.get()) {
                //open_search_sheet();
                setSearchState(NODRIVERS);
            } else if (makeTripViewModel.alldriversbusy.get()) {
                //open_search_sheet();
                setSearchState(ALLDRIVERSBUSY);
            }
        }

    }

    @Override
    public void openWaitingDialog() {

        Log.e("xxxMakeTrip", "Open waiting dialog method called");
        sharedPrefence.saveBoolean(SharedPrefence.Request_in_Progress, true);
        open_search_sheet();

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cancel_button_book) {
            //move back to rental tab by Removing current fragment
            if (makeTripViewModel.rental_request.get()) {
                removeFrag();
            }
            cancel_trip_api();
        } else if (view.getId() == R.id.try_again__searchbook) {
            close_search_sheet();
            if (makeTripViewModel.rental_request.get()) {
                removeFrag();
            }
        }

    }

    private void cancel_trip_api() {

        Log.e("xxxCancel Trip ID", makeTripViewModel.REQID.get());

        if (opentype.equalsIgnoreCase("EXISTS")) {

            String rental_trip_id = sharedPrefence.Getvalue(SharedPrefence.Rental_Request_ID);

            if (rental_trip_id.equals("")) {
                showMessage("Null REQ ID");
            }else {
                makeTripViewModel.cancelApi(rental_trip_id);
            }
        } else {
            open_search_sheet();
            makeTripViewModel.cancelApi(rentrip_id);
        }
    }

    private void open_search_sheet() {

        if (behavior == null) {
            behavior = BottomSheetBehavior.from(makeTripLayBinding.promoSheet.sheetBehaviour);
        } else if (bookingsheet == null) {
            bookingsheet = BottomSheetBehavior.from(makeTripLayBinding.bookingSheet.bookingMainLay);
        } else if (progress_sheet == null) {
            progress_sheet = BottomSheetBehavior.from(makeTripLayBinding.searchSheet.searchDriveSheet);
        }

        makeTripLayBinding.bookingSheet.bookCoOrdlay.setVisibility(View.GONE);
        makeTripLayBinding.promoSheet.promoCoOrdLay.setVisibility(View.GONE);

        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bookingsheet.setHideable(true);
        bookingsheet.setPeekHeight(850);
        bookingsheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        progress_sheet.setHideable(false);
        progress_sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void close_search_sheet() {

        if (behavior == null) {
            behavior = BottomSheetBehavior.from(makeTripLayBinding.promoSheet.sheetBehaviour);
        } else if (bookingsheet == null) {
            bookingsheet = BottomSheetBehavior.from(makeTripLayBinding.bookingSheet.bookingMainLay);
        } else if (progress_sheet == null) {
            progress_sheet = BottomSheetBehavior.from(makeTripLayBinding.searchSheet.searchDriveSheet);
        }

        makeTripLayBinding.bookingSheet.bookCoOrdlay.setVisibility(View.VISIBLE);

        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bookingsheet.setHideable(false);
        bookingsheet.setPeekHeight(850);
        bookingsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

        progress_sheet.setHideable(true);
        progress_sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        progress_sheet.setHideable(false);

    }

    @Override
    public void onDestroy() {
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh, false);
        super.onDestroy();
    }

    @Override
    public EtaModel getSelectedCar() {
        return carsTypesAdapter.getSelectedCar();
    }

    @Override
    public void refreshAdapter(List<EtaModel> etaModels) {
        Log.e("xxxMapFrag", "MakeTripRefresh : " + new Gson().toJson(etaModels));
        carsTypesAdapter.refreshData(etaModels);
        carsTypesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCLickInfoButon(EtaModel etaModelValue) {
        getEtaModel = etaModelValue;
        showInfoDialog(getEtaModel);
    }

    BottomSheetDialog InfoDialog;

    private void showInfoDialog(EtaModel getEtaModel) {

        InfoDialog = new BottomSheetDialog(getBaseAct());

        InfoDialog.setContentView(R.layout.layout_car_desc);

        TextView Carname = InfoDialog.findViewById(R.id.info_carnam);
        TextView Cardesc = InfoDialog.findViewById(R.id.info_cardesc);
        //TextView tot_seats = InfoDialog.findViewById(R.id.info_carseats);
        TextView Sprtd_Vehic = InfoDialog.findViewById(R.id.info_sprtd_vhcls);
        TextView tot_pay = InfoDialog.findViewById(R.id.info_totalpay);

        ImageView carImage = InfoDialog.findViewById(R.id.info_carimg);

        RelativeLayout donebt = InfoDialog.findViewById(R.id.info_done_bt);

        if (!getEtaModel.getHasDiscount()) {
            if (tot_pay != null)
                tot_pay.setText("Total Payable : " + getEtaModel.getCurrency() + CommonUtils.doubleDecimalFromat(getEtaModel.getTotal()));
        } else {
            tot_pay.setText("Total Payable : " + getEtaModel.getCurrency() + CommonUtils.doubleDecimalFromat(getEtaModel.getDiscountAmount()));
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
            donebt.setOnClickListener(view -> InfoDialog.dismiss());

        if (Carname != null)
            Carname.setText(getEtaModel.getTypeName());
        //Cardesc.setText();

        InfoDialog.show();

    }

    @Override
    public void setCarTypeName(String typeName) {
        makeTripViewModel.bookcartypename.set("Book " + typeName);
    }


    @Override
    public void updateCarETA(String typeId, String eta) {
        if (carsTypesAdapter != null) {
            carsTypesAdapter.updateEta(typeId, eta);
        }
    }

    @Override
    public void onLocationChanged(@NonNull @NotNull Location location) {

        mapRipple.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

        pLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        makeTripViewModel.pickupLatLng.set(pLatLng);

    }

    private void set_Booking_Sheet() {
        bookingsheet = BottomSheetBehavior.from(makeTripLayBinding.bookingSheet.bookingMainLay);
        bookingsheet.setHideable(false);
        bookingsheet.setPeekHeight((int) (MyApp.getmContext().getResources().getDisplayMetrics().heightPixels * 0.50));

        if (!makeTripViewModel.mIsLoading.get()) {
            bookingsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else bookingsheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        bookingsheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottomSheet.requestLayout();
                        bookingsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
                        bottomSheet.requestLayout();
                        bookingsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setOffersSheets() {

        behavior = BottomSheetBehavior.from(makeTripLayBinding.promoSheet.sheetBehaviour);
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        // bookingsheet.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
                        bottomSheet.requestLayout();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void set_Searching_Sheet() {

        progress_sheet = BottomSheetBehavior.from(makeTripLayBinding.searchSheet.searchDriveSheet);
        progress_sheet.setHideable(true);

        progress_sheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        progress_sheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        // bookingsheet.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottomSheet.requestLayout();
                        makeTripLayBinding.searchSheet.pickdropdetailcard.setVisibility(View.GONE);

                        getBaseActivity().runOnUiThread(() -> {
                            progress_sheet.setPeekHeight(getsearchdynamicheight());
                        });

                    }
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        makeTripLayBinding.searchSheet.pickdropdetailcard.setVisibility(View.VISIBLE);
                        bottomSheet.requestLayout();
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
                        makeTripLayBinding.searchSheet.pickdropdetailcard.setVisibility(View.VISIBLE);
                        bottomSheet.requestLayout();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private int getsearchdynamicheight() {
        int fullheight = makeTripLayBinding.searchSheet.searchDriveSheet.getHeight();
        int cardheight = makeTripLayBinding.searchSheet.pickdropdetailcard.getHeight();
        return fullheight - cardheight;
    }

    public void setSearchState(String state) {

        switch (state) {
            case NODRIVERS: {
                makeTripLayBinding.searchSheet.animSearchbook.setBackgroundResource(R.drawable.bk_nodriverfound);
                makeTripViewModel.SearchTitle.set("No Drivers Found Around You...!");
                makeTripLayBinding.searchSheet.tryAgainSearchbook.setVisibility(View.VISIBLE);
                makeTripLayBinding.searchSheet.cancelButtonBook.setVisibility(View.GONE);
            }
            break;
            case ALLDRIVERSBUSY: {
                makeTripLayBinding.searchSheet.animSearchbook.setBackgroundResource(R.drawable.bk_driver_busy);
                makeTripViewModel.SearchTitle.set("All Drivers are busy right Now...!");
                makeTripLayBinding.searchSheet.tryAgainSearchbook.setVisibility(View.VISIBLE);
                makeTripLayBinding.searchSheet.cancelButtonBook.setVisibility(View.GONE);
            }
            break;
            case TRIPACCEPTED: {
                makeTripViewModel.SearchTitle.set("Booking Accepted...Your Taxi is On the Way...");
                makeTripLayBinding.searchSheet.animSearchbook.setBackgroundResource(R.drawable.bk_accepted);
                makeTripLayBinding.searchSheet.tryAgainSearchbook.setVisibility(View.GONE);
                makeTripLayBinding.searchSheet.cancelButtonBook.setVisibility(View.GONE);
            }
            break;
            case TRIPSEARCHING: {
                makeTripLayBinding.searchSheet.animSearchbook.setBackgroundResource(R.drawable.bk_searching);
                makeTripViewModel.SearchTitle.set("Waiting For a near driver around you to accept your booking");
                makeTripLayBinding.searchSheet.tryAgainSearchbook.setVisibility(View.GONE);
                makeTripLayBinding.searchSheet.cancelButtonBook.setVisibility(View.VISIBLE);
            }
            break;
        }

    }

    public void startRippleAnimation() {

        googleMap.clear();

        Context context = this.getContext();

        if (context != null && googleMap != null && pLatLng != null) {
            mapRipple = new MapRipple(googleMap, pLatLng, context);

            mapRipple.withNumberOfRipples(3);
            mapRipple.withFillColor(Color.parseColor("#FFA3D2E4"));
            mapRipple.withStrokeColor(Color.BLACK);
            mapRipple.withStrokewidth(0);
            mapRipple.withDistance(10000);
            mapRipple.withRippleDuration(10000);
            mapRipple.withTransparency(0.5f);

            if (ActivityCompat.checkSelfPermission(this.getBaseAct(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getBaseAct(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (googleMap != null)
                googleMap.setMyLocationEnabled(true);
            if (googleMap != null)
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (googleMap != null)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(pLatLng));
            if (googleMap != null)
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }

        if (!mapRipple.isAnimationRunning()) {
            mapRipple.startRippleMapAnimation();
        }
    }

    public void stopRippleAnimation() {

        if (ActivityCompat.checkSelfPermission(getBaseAct(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getBaseAct(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(false);

        googleMap.clear();

        if (mapRipple != null) {
            if (mapRipple.isAnimationRunning()) {
                mapRipple.stopRippleMapAnimation();
            }
        }

        makeTripViewModel.drawroute();

    }

    public void trip_cancel_alert() {
        AlertDialog trip_request = new AlertDialog.Builder(getBaseActivity())
                .setTitle("TRIP END Confirmation")
                .setCancelable(false)
                .setMessage("Trip Request In Progress Are you sure to Cancel ?")
                .setPositiveButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    cancel_trip_api();
                })
                .setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

        if (!requireActivity().isFinishing() && !trip_request.isShowing()) {
            trip_request.show();
        }
    }


}



