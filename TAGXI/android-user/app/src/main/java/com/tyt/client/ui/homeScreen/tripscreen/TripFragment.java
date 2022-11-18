package com.tyt.client.ui.homeScreen.tripscreen;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tyt.client.R;
import com.tyt.client.databinding.FragmentTripBinding;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseFragment;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.feedback.FeedbackFrag;
import com.tyt.client.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
* A Fragment which is used to show the Trip Status After Successful booking.
* */


public class TripFragment extends BaseFragment<FragmentTripBinding, TripFragViewModel> implements TripNavigator, OnMapReadyCallback, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "TripFragment";

    boolean ispickDrop;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    View layoutMarkerPickup, layoutMArkerDrop;
    Marker Pickup, Drop;
    @Inject
    SharedPrefence sharedPrefence;

    BaseActivity context;

    public static ProfileModel onTripReq;

    @Inject
    TripFragViewModel tripFragViewModel;

    FragmentTripBinding fragmentTripBinding;
    GoogleMap mgoogleMap;
    private FloatingActionButton sosPhoneZero, sosPhoneOne, sosPhoneTwo;
    private FloatingActionMenu sosMenu;
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
        Log.d("xxxTripFrTAG", "onCreate: ");
        this.context = getbaseAct();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("xxxTripFrTAG", "onResume: ");
        sharedPrefence.saveBoolean(SharedPrefence.dont_refresh,false);
        ((HomeAct) getActivity()).ShowHideBar(false);

        fragmentTripBinding.tripSwipeRefLay.setOnRefreshListener(() -> {
            ((HomeAct) getBaseActivity()).callProfileAPI();
        });

    }

    public void stop_refresh(){
        fragmentTripBinding.tripSwipeRefLay.setRefreshing(false);
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


    /**
     * {@link BroadcastReceiver} for receiving current status of the trip. They are
     * Bill generated, Trip Started, Driver arrived
     **/
    private BroadcastReceiver receiverTripStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


        }
    };

    /**
     * Initialize {@link FusedLocationProviderClient} to get location data
     **/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("xxxTripFrTAG", "onViewCreated: ");
        fragmentTripBinding = getViewDataBinding();
        tripFragViewModel.setNavigator(this);
        init_pages();

        ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.trip_map)).getMapAsync(this);


        tripFragViewModel.setValues(onTripReq,fragmentTripBinding);
        sosPhoneZero=fragmentTripBinding.sosPhoneZero;
        sosPhoneOne=fragmentTripBinding.sosPhoneOne;
        sosPhoneTwo=fragmentTripBinding.sosPhoneTwo;
        sosMenu=fragmentTripBinding.sosMenu;
        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(getActivity());

        getActivity().setTitle(getbaseAct().getTranslatedString(R.string.app_name));

        bottomSheetBehavior = BottomSheetBehavior.from(fragmentTripBinding.menu.bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        fragmentTripBinding.bottomAppBar.setVisibility(View.GONE);
        fragmentTripBinding.statusTxt.setText(tripFragViewModel.tripStatus.get());
        Log.d("xxxTripTAG", "onViewCreated: "+tripFragViewModel.tripStatus.get());
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
                            fragmentTripBinding.fabCallChat.setVisibility(View.GONE);
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


        if (tripFragViewModel.sos_phone_zero.get() != null)
            sosPhoneZero.setLabelText(String.valueOf(tripFragViewModel.sos_phone_zero.get()));
        else sosPhoneZero.setVisibility(View.GONE);
        if (tripFragViewModel.sos_phone_one.get() != null)
            sosPhoneOne.setLabelText(String.valueOf(tripFragViewModel.sos_phone_one.get()));
        else sosPhoneOne.setVisibility(View.GONE);
        if (tripFragViewModel.sos_phone_two.get() != null)
            sosPhoneTwo.setLabelText(String.valueOf(tripFragViewModel.sos_phone_two.get()));
        else sosPhoneTwo.setVisibility(View.GONE);

        fragmentTripBinding.notifyAdmin.setLabelText(tripFragViewModel.sos_notify_admin.get());

        fragmentTripBinding.notifyAdmin.setOnClickListener(this);

        sosPhoneZero.setOnClickListener(this);
        sosPhoneOne.setOnClickListener(this);
        sosPhoneTwo.setOnClickListener(this);
        fragmentTripBinding.bottomAppBar.setNavigationOnClickListener(this);
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

    }

    /**
     * Returns reference of {@link BaseActivity}
     **/
    @Override
    public BaseActivity getbaseAct() {
        return getBaseActivity() != null ? getBaseActivity() : (BaseActivity) getActivity();
    }

    @Override
    public void openHomePage() {
        startActivity(new Intent(getActivity(), HomeAct.class));
    }

    @Override
    public void openFeedback(String s) {

        BaseResponse response = CommonUtils.getSingleObject(s, BaseResponse.class);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, FeedbackFrag.newInstance(response), FeedbackFrag.TAG)
                .commitAllowingStateLoss();
    }

    Dialog carInfoDialog;

    @Override
    public void openCarInfoDialog() {
        if (carInfoDialog != null)
            if (!carInfoDialog.isShowing()) {
                carInfoDialog.show();
                return;
            }

        carInfoDialog = new Dialog(getActivity());
        carInfoDialog.setContentView(R.layout.dialog_car_info);
        if (carInfoDialog.getWindow() != null) {
            carInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView carMake = carInfoDialog.findViewById(R.id.car_make);
        TextView carModel = carInfoDialog.findViewById(R.id.car_model);
        TextView carColor = carInfoDialog.findViewById(R.id.car_clr);
        TextView carNumber = carInfoDialog.findViewById(R.id.car_num);
        TextView ok = carInfoDialog.findViewById(R.id.ok_txt);
        carMake.setText(tripFragViewModel.car_make.get());
        carModel.setText(tripFragViewModel.car_model.get());
        carColor.setText(tripFragViewModel.car_color.get());
        carNumber.setText(tripFragViewModel.car_number.get());

        ok.setOnClickListener(v -> carInfoDialog.dismiss());

        carInfoDialog.show();
        carInfoDialog.setCanceledOnTouchOutside(false);
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

        TextView tTitle = cancelReasonDialog.findViewById(R.id.choose_reason);
        TextView tConfirm = cancelReasonDialog.findViewById(R.id.txt_confirm);
        editText = cancelReasonDialog.findViewById(R.id.cancel_reason_edit);

        tTitle.setText(getBaseActivity().getTranslatedString(R.string.txt_choose_reason));
        tConfirm.setText(getBaseActivity().getTranslatedString(R.string.txt_confirm));
        editText.setHint(getBaseActivity().getTranslatedString(R.string.txt_raise_reason));

        editText = cancelReasonDialog.findViewById(R.id.cancel_reason_edit);
        RelativeLayout relativeLayout = cancelReasonDialog.findViewById(R.id.confirm);
        RecyclerView recyclerView = cancelReasonDialog.findViewById(R.id.recycle);
        cancelReasonAdapter = new CancelReasonAdapter(getActivity(), reasonModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cancelReasonAdapter);

        relativeLayout.setOnClickListener(v -> {
            if (!editText.getText().toString().isEmpty())
                tripFragViewModel.reasonText.set(editText.getText().toString());

            tripFragViewModel.cancelApi();
        });

        cancelReasonDialog.show();

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
    public void onClickShare(String s) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");

        String firstText = "I've booked an TYT. Track this ride :" + "\n";
        String secondText = Constants.URL.BaseURL + "track/request/" + s + "\n";
        String thridText = "Vehicle Number:" + " " + tripFragViewModel.car_number.get() + "\n";
        String driverPhone = "Driver Number:" + " " + tripFragViewModel.userPhone.get();
        String shareBody = firstText + secondText + thridText + driverPhone;
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }


    @Override
    public String getItemPosition() {
        return cancelReasonAdapter != null ? cancelReasonAdapter.getSelectPosition() : "";
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
    public void onTaskDone(Object... values) {

    }


    /**
     * This callback is called when {@link GoogleMap} loading was complete
     **/
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (getContext() != null && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setPadding(0, 250, 0, 490);
        this.mgoogleMap = googleMap;

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.uber_map));
        googleMap.setTrafficEnabled(true);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            requireActivity(), R.raw.style_json));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        mgoogleMap.setOnMapLoadedCallback(() -> {
            tripFragViewModel.isMapRendered.set(true);
            tripFragViewModel.buildGoogleApiClient(mgoogleMap);
            if (tripFragViewModel.dropLatLng.get() != null && tripFragViewModel.dropLocation.get() != null && !tripFragViewModel.dropLocation.get().isEmpty()) {
                tripFragViewModel.drawtriproute();
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sos_phone_zero) {
            new Handler().postDelayed(() -> sosPhoneZero.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            sosMenu.close(true);
            onSosClick(v, sosPhoneZero.getLabelText());

        } else if (v.getId() == R.id.sos_phone_one) {
            new Handler().postDelayed(() -> sosPhoneOne.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            sosMenu.close(true);
            onSosClick(v, sosPhoneOne.getLabelText());


        } else if (v.getId() == R.id.sos_phone_two) {
            new Handler().postDelayed(() -> sosPhoneTwo.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom)), 300);
            sosMenu.close(true);
            onSosClick(v, sosPhoneTwo.getLabelText());

        } else if (v.getId() == R.id.notify_Admin) {
            new Handler().postDelayed(() -> {
                sosMenu.close(true);
                UpdateSOSAdminFireBase();
            },300);
        } else {
            onClickShare();
        }
    }

    private void UpdateSOSAdminFireBase() {
        showMessage("Please Wait...");

        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("SOS").child(Objects.requireNonNull(tripFragViewModel.ReqId.get()));

        HashMap<String,Object> admin_sos_map = new HashMap<>();
        admin_sos_map.put("req_id",tripFragViewModel.ReqId.get());
        admin_sos_map.put("is_user",1);
        admin_sos_map.put("is_driver", 0);
        admin_sos_map.put("serv_loc_id",tripFragViewModel.Serv_Loc_ID.get());
        admin_sos_map.put("updated_at",System.currentTimeMillis());

        mydb.setValue(admin_sos_map).addOnSuccessListener(task -> {
            showMessage("Notified to Admin Successfully...");
        }).addOnFailureListener(e -> showMessage(e.getMessage()));

    }

    /**
     * SOS Call phone {@link Intent} to make phone call to user when call sos button is clicked
     **/
    public void onSosClick(View view, String phone_number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone_number));
        view.getContext().startActivity(callIntent);
    }

    public void onClickShare() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String firstText = "I've booked an TYT. Track this ride :" + "\n";
        String secondText = Constants.URL.BaseURL + "track/request/" + tripFragViewModel.ReqId.get() + "\n";
        String thridText = "Vehicle Number:" + " " + tripFragViewModel.car_number.get() + "\n";
        String driverPhone = "Driver Number:" + " " + tripFragViewModel.userPhone.get();
        String shareBody = firstText + secondText + thridText + driverPhone;
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }

}