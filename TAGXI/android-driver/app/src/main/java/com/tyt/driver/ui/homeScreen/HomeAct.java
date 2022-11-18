package com.tyt.driver.ui.homeScreen;

import static com.tyt.driver.utilz.CommonUtils.checkLocationorGPSEnabled;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.ActHomeBinding;
import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.ui.acceptReject.AccepRejectAct;
import com.tyt.driver.ui.addMoney.AddMoneyFrag;
import com.tyt.driver.ui.approval.ApprovalAct;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.documentUpload.DocumentUploadAct;
import com.tyt.driver.ui.feedback.FeedbackFrag;
import com.tyt.driver.ui.historyDetail.HistoryDetailFrag;
import com.tyt.driver.ui.homeScreen.addCard.AddCardFrag;
import com.tyt.driver.ui.homeScreen.bookingConfirm.BookingFrag;
import com.tyt.driver.ui.homeScreen.earnings.EarningsFrag;
import com.tyt.driver.ui.homeScreen.faq.FaqFrag;
import com.tyt.driver.ui.homeScreen.getRefferal.GetRefferalFrag;
import com.tyt.driver.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.driver.ui.homeScreen.instantRide.InstantRideFrag;
import com.tyt.driver.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.driver.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.driver.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.driver.ui.homeScreen.mapscrn.MapScrn;
import com.tyt.driver.ui.homeScreen.profile.ProfileFrag;
import com.tyt.driver.ui.homeScreen.profile.profileEdit.ProfileEditFrag;
import com.tyt.driver.ui.homeScreen.sos.SosFrag;
import com.tyt.driver.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.driver.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.driver.ui.homeScreen.vehicleInfo.VehicleInfoFrag;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.ui.wallet.WalletFrag;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.ForegroundService;
import com.tyt.driver.utilz.LocationUpdatesService;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.sync.SensorService;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class HomeAct extends BaseActivity<ActHomeBinding, HomeViewModel>
        implements HomeNavigator,
        HasAndroidInjector {

    private static final String TAG = "HomeAct";
    @Inject
    HomeViewModel homeViewModel;
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    Gson gson;

    private SensorService mSensorService;
    Intent mServiceIntent;

    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    FragmentManager fragmentManager;
    private DrawerLayout mDrawer;
    boolean doubleBackToExitPressedOnce = false;
    private GoogleApiClient mGoogleApiClient;

    private NavigationView mNavigationView;

    ActHomeBinding actHomeBinding;

    InstallStateUpdatedListener listener;
    AppUpdateManager appUpdateManager;
    SnackProgressBarManager snackProgressBarManager;

    private LocationUpdatesService locationService = null;
    // Tracks the bound state of the service.
    private boolean mBound = false, mLocationBound = false;

    /**
     * Registers {@link BroadcastReceiver}s
     * Initializes {@link GoogleApiClient}
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actHomeBinding = getViewDataBinding();
        homeViewModel.setNavigator(this);

        snackProgressBarManager = new SnackProgressBarManager(getBaseView(), this);

        snackProgressBarManager.setBackgroundColor(R.color.colorPrimary);

        ProfileRefreshAPI();

        //homeViewModel.porfileApi();
//        actHomeBinding.switchCompat.setChecked(sharedPrefence.GetBoolean(SharedPrefence.isOnline));
//
//        actHomeBinding.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                homeViewModel.onClickOnOff();
//            }
//        });
    }


    public void ProfileRefreshAPI() {
        Log.e("xxxHomeAct","Profile API CAlled");
        homeViewModel.porfileApi();
    }


    @Override
    public HomeViewModel getViewModel() {
        return homeViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_home;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    /**
     * Phone back button click callback. Handles fragments navigation
     **/
    @Override
    public void onBackPressed() {
        boolean isChanged = true;
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_frame);

        if (f == null || f.getTag() == null) {
            MakeCompFrag makecompFrag = (MakeCompFrag) getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
            if (makecompFrag != null) {
                makecompFrag.clickBack();
            }
        }
        if (!(f instanceof MakeCompFrag)) {
            switch (Objects.requireNonNull(Objects.requireNonNull(f).getTag())) {
                case ProfileEditFrag.TAG:
                case ManageCardFrag.TAG:
                case VehicleInfoFrag.TAG:
                case WalletFrag.TAG:
                case GetRefferalFrag.TAG:
                case SosFrag.TAG:
                case FaqFrag.TAG:
                case AddCardFrag.TAG:
                case MakeTripFrag.TAG:
                case EarningsFrag.TAG:
                    ShowHideBar(true);
                    this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    isChanged = true;
                    break;
                //ShowOnloneOffline(true);
                case InAppChatFragment.TAG:
                    Log.d(TAG, "onBackPressed: ");
                    this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    homeViewModel.GetUserProfile();
                    isChanged = true;
                    break;
                case AddMoneyFrag.TAG:
                    if (this.getSupportFragmentManager().findFragmentByTag(WalletFrag.TAG) != null) {
                        Intent intentBroadcasts = new Intent();
                        intentBroadcasts.setAction(Constants.BroadcastsActions.BackPressedReceiever);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentBroadcasts);
                    }
                    this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    isChanged = true;
                    break;
                case BookingFrag.TAG:
                    this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    isChanged = true;
                    break;
                case HistoryDetailFrag.TAG:
                    ShowHideBar(true);
                    this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    isChanged = true;
                    break;
                case ProfileFrag.TAG:
                case HistoryFrag.TAG:
                    startActivity(new Intent(this, HomeAct.class));
                    break;
                case InstantRideFrag.TAG:
                    isChanged = true;
                    ((InstantRideFrag) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(InstantRideFrag.TAG))).handlebackpress();
                    break;
            }
        } else {
            MakeCompFrag makecompFrag = (MakeCompFrag) getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
            if (makecompFrag != null) {
                makecompFrag.clickBack();
            }
        }

        if (!isChanged) {

            if (doubleBackToExitPressedOnce) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }

            this.doubleBackToExitPressedOnce = true;

            Snackbar backsnack = Snackbar.make((View) actHomeBinding.homeBg, "Press again to exit.", Snackbar.LENGTH_LONG);
            backsnack.setAction("EXIT", view -> {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            });
            backsnack.setActionTextColor(Color.YELLOW);
            backsnack.show();
            /*
            Toast.makeText(this, getTranslatedString(R.string.text_double_tap_exit), Toast.LENGTH_SHORT).show();*/
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);


        }

    }

    /**
     * Unregisters {@link BroadcastReceiver}s
     **/
    @Override
    protected void onDestroy() {
        Log.e(TAG,"HomeAct onDestroy Called");
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(BackReceieved);
       /* if (appUpdater != null)
            appUpdater.stop();*/

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRemoveWaitingDialog);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"HomeAct onStop Called");
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(listener);
        }
    }

    public void openHomeFrag(String currentDate, String symbol, double currencySymbol) {
        Log.d("xxxHomeAct", "openHomeFrag: ");
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, MapScrn.newInstance(currentDate, symbol, currencySymbol), MapScrn.TAG)
                .commitAllowingStateLoss();
    }


    public void ShowHideBar(boolean isShow) {
        Log.d(TAG, "ShowHideBar: ");
        homeViewModel.hideBar.set(isShow);
    }

    public void ShowOnloneOffline(boolean b) {
        actHomeBinding.onlineOffline.setVisibility(b ? View.VISIBLE : View.GONE);
        actHomeBinding.onOff.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void onClickProfile() {
        if (this.getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) != null) {
                    Fragment frag = getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG);
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                }
            }
        }, 200);


    }

    @Override
    public void onClickHome() {

        homeViewModel.profileClicked.set(false);
        homeViewModel.homeClicked.set(true);
        homeViewModel.historyClicked.set(false);

        if (this.getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) != null) {
            Fragment frag = this.getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG);
            this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }
        if (this.getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG) != null) {
            Fragment frag = this.getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG);
            this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }
        if (this.getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) != null) {
            Fragment frag = this.getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG);
            this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }

        ShowHideBar(true);
        ShowOnloneOffline(true);

        homeViewModel.porfileApi();
    }

    @Override
    public void onClickHistory() {
        homeViewModel.profileClicked.set(false);
        homeViewModel.homeClicked.set(false);
        homeViewModel.historyClicked.set(true);

        if (this.getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) == null)
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                    .commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) != null) {
                    Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG);
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                }
                if (getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG) != null) {
                    Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG);
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                }
            }
        }, 200);


    }

    @Override
    public void onClickOnOff() {

    }

    @Override
    public void StartUpdate() {
        if (sharedPrefence.GetBoolean(SharedPrefence.isOnline)) {
            startService();
        } else stopService();
    }


    @Override
    public void openAcceptRejectAct(String req) {
        Log.e(TAG, "Normal openAcceptRejectAct method called");
        Log.e("xxxrequestData---", "data---" + req);
        Intent acceptRejectIntent = new Intent(this, AccepRejectAct.class);
        acceptRejectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acceptRejectIntent.putExtra(Constants.IntentExtras.ACCEPT_REJECT_DATA, req);
        startActivity(acceptRejectIntent);
    }

    @Override
    public void openTripFragment(ProfileModel onTripRequest) {
        ShowHideBar(false);
        ShowOnloneOffline(false);
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, TripFragment.newInstance(onTripRequest), TripFragment.TAG)
                .commitAllowingStateLoss();
    }


    @Override
    public void closeAcceptReject() {
        Log.e(TAG, "closeAcceptReject HomeAct method called");
        Intent intentBroadcasts = new Intent();
        intentBroadcasts.setAction(Constants.BroadcastsActions.CANCEL_RECEIVER);
        LocalBroadcastManager.getInstance(HomeAct.this).sendBroadcast(intentBroadcasts);
    }

    @Override
    public void openFeedbackFrag(ProfileModel model) {

        ShowHideBar(false);
        ShowOnloneOffline(false);
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, FeedbackFrag.newInstance(model), FeedbackFrag.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void openMetaAcceptReject(ProfileModel metaRequest) {
        Log.e(TAG, "openMetaAcceptReject method called");
        Intent acceptRejectIntent = new Intent(this, AccepRejectAct.class);
        acceptRejectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acceptRejectIntent.putExtra(Constants.IntentExtras.MetAcceptRejData, new Gson().toJson(metaRequest));
        startActivity(acceptRejectIntent);
    }

    @Override
    public void openDocumentPage() {
        startActivity(new Intent(this, DocumentUploadAct.class));
    }

    @Override
    public void openApprovalPage(String declinedReason) {
        startActivity(new Intent(this, ApprovalAct.class).putExtra("DeclinedReason", declinedReason));
    }

    @Override
    public void openFromStart() {
        startActivity(new Intent(this, LoginOrSignAct.class));
    }

    @Override
    public void openHomeAct() {
        startActivity(new Intent(this, HomeAct.class));
    }

    @Override
    public void openMapFrag(String currentDate, String currencySymbol, double totalEarnigs) {
        openHomeFrag(currentDate, currencySymbol, totalEarnigs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"HomeAct onStart Called");

        LocalBroadcastManager.getInstance(this).registerReceiver(BackReceieved, new IntentFilter(Constants.BroadcastsActions.BackPressedReceiever));

        appUpdateManager = AppUpdateManagerFactory.create(this);

        listen_inapp_update();

        appUpdateManager.registerListener(listener);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                PopSnackBar("Updates Are Ready To Install", "DOWNLOADED", 0, 0);
            }
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADING) {
                long bytes_downloaded = appUpdateInfo.bytesDownloaded();
                long total_bytes_downloaded = appUpdateInfo.totalBytesToDownload();
                PopSnackBar("Downloading...", "DOWNLOADING", bytes_downloaded, total_bytes_downloaded);
            }

            if (appUpdateInfo.installStatus() == InstallStatus.INSTALLING) {
                PopSnackBar("Installing In Progress", "INSTALLING", 0, 0);
            }
        });


        bindService(new Intent(this, LocationUpdatesService.class), mLocationServiceConnection,
                Context.BIND_AUTO_CREATE);
        IntentFilter intentLocationFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        intentLocationFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        IntentFilter intentInternetFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentInternetFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(locationReceiver, intentLocationFilter);
        registerReceiver(internetReceiver, intentInternetFilter);
        IntentFilter intentLocation = new IntentFilter(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
        intentLocation.addAction(Constants.BroadcastsActions.LOCATION_UPDATING_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationReceiver, intentLocation);

    }

    private final ServiceConnection mLocationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            locationService = binder.getService();
            mLocationBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            mLocationBound = false;
        }
    };

    //This receiever is for check the location is enabled or not
    public BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                checkLocationorGPSEnabled();
            }
        }
    };

    //This receiever is for check the internet is enabled or not
    public BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(ConnectivityManager.CONNECTIVITY_ACTION)) {
                checkInternetEnabled();
            }
        }
    };

    private final BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (homeViewModel != null && intent != null
                    && intent.getStringExtra(Constants.IntentExtras.LOCATION_ID) != null
                    && intent.getStringExtra(Constants.IntentExtras.LOCATION_LAT) != null
                    && intent.getStringExtra(Constants.IntentExtras.LOCATION_LNG) != null
                    && intent.getStringExtra(Constants.IntentExtras.LOCATION_BEARING) != null)
               /* if (locatoinReceiverListener != null && sharedPrefence.getIntvalue(SharedPrefence.REQUEST_ID) != Constants.NO_REQUEST)
                    locatoinReceiverListener.passLatlng(intent.getStringExtra(Constants.IntentExtras.LOCATION_LAT),
                            intent.getStringExtra(Constants.IntentExtras.LOCATION_LNG),
                            intent.getStringExtra(Constants.IntentExtras.LOCATION_BEARING), intent.getStringExtra(Constants.IntentExtras.LOCATION_ID));
                else*/
                homeViewModel.sendLocation(intent.getStringExtra(Constants.IntentExtras.LOCATION_ID),
                        intent.getStringExtra(Constants.IntentExtras.LOCATION_LAT),
                        intent.getStringExtra(Constants.IntentExtras.LOCATION_LNG),
                        intent.getStringExtra(Constants.IntentExtras.LOCATION_BEARING));

        }
    };


    public void startLocationUpdate() {
        if (locationService != null) {
            Intent locationIntent = new Intent(this, LocationUpdatesService.class);
            bindService(locationIntent, mLocationServiceConnection, Context.BIND_AUTO_CREATE);
        }
//        startService();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG,"HomeAct OnResume Called");

        checkLocationorGPSEnabled();

        MyApp.setInsideTrip(false);

        if (sharedPrefence.GetBoolean(SharedPrefence.isOnline)) {
            startService();
        } else stopService();

        if (mSensorService == null && mServiceIntent == null) {
            mSensorService = new SensorService(this);
            mServiceIntent = new Intent(this, mSensorService.getClass());
        }

        if (mSensorService != null && !CommonUtils.isMyServiceRunning(this, mSensorService.getClass())) {
            startService();
        }

        if (this.getSupportFragmentManager().findFragmentByTag(TripFragment.TAG) != null) {
            ShowHideBar(false);
            ShowOnloneOffline(false);
        } else {
            ShowHideBar(true);
            ShowOnloneOffline(true);
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                optimizeBattery();
//            }
//        }, 1000);
        appUpdateCheck();
        // homeViewModel.porfileApi();

   /*     if (sharedPrefence.GetBoolean(SharedPrefence.AcceptRejProc)) {
            //don't refresh
        }else {*/

        if (sharedPrefence.GetBoolean(SharedPrefence.onGoogleSearch)) {
            //don't refresh
        } else {
            if (homeViewModel.profileClicked.get() || homeViewModel.historyClicked.get()) {
                if (homeViewModel.profileClicked.get()) {
                    homeViewModel.profileClicked.set(true);
                }
                if (homeViewModel.historyClicked.get()) {
                    homeViewModel.historyClicked.set(true);
                }
            } else {
                homeViewModel.porfileApi();
                homeViewModel.homeClicked.set(true);
                homeViewModel.historyClicked.set(false);
                homeViewModel.profileClicked.set(false);
            }
        }
    }

    private void listen_inapp_update() {
        listener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
                long bytes_downloaded = state.bytesDownloaded();
                long total_bytes_downloaded = state.totalBytesToDownload();
                PopSnackBar("Downloading...", "DOWNLOADING", bytes_downloaded, total_bytes_downloaded);
            }

            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                PopSnackBar("Updates Are Ready To Install", "DOWNLOADED", 0, 0);
            }

            if (state.installStatus() == InstallStatus.INSTALLING) {
                PopSnackBar("Installing In Progress", "INSTALLING", 0, 0);
            }
        };
    }


    private void PopSnackBar(String message, String action, long bytes_downloaded, long total_bytes_downloaded) {
        SnackProgressBar snackbar = new SnackProgressBar(SnackProgressBar.TYPE_HORIZONTAL, message)
                .setIsIndeterminate(false)
                .setMessage(message);

        switch (action) {
            case "DOWNLOADED":
                snackbar.setAction("UPDATE", () -> {
                    appUpdateManager.completeUpdate();
                });
                snackProgressBarManager.show(snackbar, SnackProgressBarManager.LENGTH_INDEFINITE);
                break;
            case "DOWNLOADING":
                int current = Integer.parseInt(String.valueOf(bytes_downloaded));
                int total = Integer.parseInt(String.valueOf(total_bytes_downloaded));
                snackbar.setShowProgressPercentage(true);
                snackProgressBarManager.show(snackbar, SnackProgressBarManager.LENGTH_INDEFINITE);
                snackProgressBarManager.setProgress((current / total) * 100);
                break;
        }
    }

    private View getBaseView() {
        return actHomeBinding.homeBg;
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            this.startService(serviceIntent);
        }

    }

    private void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void optimizeBattery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                openOptimizingDialog();
            } else appUpdateCheck();
        } else appUpdateCheck();
    }

    public void startSocket() {
        //   drawerViewModel.initiateSocket();
        //       SyncUtils.CreateSyncAccount(this);
        mSensorService = new SensorService(this);
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!CommonUtils.isMyServiceRunning(this, mSensorService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private void openOptimizingDialog() {
        AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder1.setMessage(getTranslatedString(R.string.txt_disable_optimization));
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getTranslatedString(R.string.txt_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS), 0);
                    }
                });
        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * Call this to check if new version of the app is available for the app in store
     **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void appUpdateCheck() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            this,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                    .setAllowAssetPackDeletion(true)
                                    .build(),
                            Constants.UPDATE_REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.UPDATE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                //showMessage("someThing Went Wrong");
            }
            if (resultCode == RESULT_CANCELED) {
                showMessage("Update was Cancelled");
            }
            if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
                showMessage("Failed To Update");
            }
        }
    }

    BroadcastReceiver BackReceieved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            homeViewModel.hideBar.set(true);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"HomeAct onPause Called");
    }
}
