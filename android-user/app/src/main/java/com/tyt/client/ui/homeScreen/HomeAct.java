package com.tyt.client.ui.homeScreen;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.tingyik90.snackprogressbar.SnackProgressBar;
import com.tingyik90.snackprogressbar.SnackProgressBarManager;
import com.tyt.client.R;
import com.tyt.client.databinding.ActHomeBinding;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.homeScreen.addCard.AddCardFrag;
import com.tyt.client.ui.homeScreen.addMoney.AddMoneyFrag;
import com.tyt.client.ui.homeScreen.faq.FaqFrag;
import com.tyt.client.ui.homeScreen.feedback.FeedbackFrag;
import com.tyt.client.ui.homeScreen.getRefferal.GetRefferalFrag;
import com.tyt.client.ui.homeScreen.historyDetail.HistoryDetailFrag;
import com.tyt.client.ui.homeScreen.inAppChat.InAppChatFragment;
import com.tyt.client.ui.homeScreen.makeComplaint.MakeCompFrag;
import com.tyt.client.ui.homeScreen.makeTrip.MakeTripFrag;
import com.tyt.client.ui.homeScreen.manageCard.ManageCardFrag;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;
import com.tyt.client.ui.homeScreen.profile.ProfileFrag;
import com.tyt.client.ui.homeScreen.profile.profileEdit.ProfileEditFrag;
import com.tyt.client.ui.homeScreen.sos.SosFrag;
import com.tyt.client.ui.homeScreen.tripscreen.TripFragment;
import com.tyt.client.ui.homeScreen.userHistory.HistoryFrag;
import com.tyt.client.ui.homeScreen.wallet.WalletFrag;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.TouchableWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * It is a Main Activity Which holds all the fragments in this project.
 */

public class HomeAct extends BaseActivity<ActHomeBinding, HomeViewModel> implements HomeNavigator, HasAndroidInjector, TouchableWrapper.UpdateMapAfterUserInterection {

    private static final String TAG = "HomeAct";

    @Inject
    HomeViewModel homeViewModel;
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    Gson gson;
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    FragmentManager fragmentManager;
    private DrawerLayout mDrawer;
    boolean doubleBackToExitPressedOnce = false;
    private GoogleApiClient mGoogleApiClient;

    ActHomeBinding actHomeBinding;

    InstallStateUpdatedListener listener;
    AppUpdateManager appUpdateManager;
    SnackProgressBarManager snackProgressBarManager;

    public boolean isChanged;

    /**
     * Registers {@link BroadcastReceiver}s
     * Initializes {@link GoogleApiClient}
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actHomeBinding = getViewDataBinding();
        homeViewModel.setNavigator(this);

        homeViewModel.porfileApi();

        snackProgressBarManager = new SnackProgressBarManager(getBaseView(), this);

        snackProgressBarManager.setBackgroundColor(R.color.colorPrimary);

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
        isChanged = true;
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);

        if (f == null || f.getTag() == null) {

            MakeCompFrag makecompFrag = (MakeCompFrag) getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
            if (makecompFrag != null) {
                makecompFrag.clickBack();
            }

        }
        if (!(f instanceof MakeCompFrag)) {
            switch (Objects.requireNonNull(Objects.requireNonNull(f).getTag())) {
                case ManageCardFrag.TAG:
                case HistoryFrag.TAG:
                case WalletFrag.TAG:
                case GetRefferalFrag.TAG:
                case SosFrag.TAG:
                case FaqFrag.TAG:
                case ProfileEditFrag.TAG:
                    this.getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                            .commitAllowingStateLoss();

                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    isChanged = true;
                    break;
                case HistoryDetailFrag.TAG:
                    this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                            .commitAllowingStateLoss();
                    isChanged = true;
                    break;
                case AddCardFrag.TAG:
                case MakeTripFrag.TAG:
                    ShowHideBar(true);
                    if (sharedPrefence.GetBoolean(SharedPrefence.Request_in_Progress)) {
                        MakeTripFrag bookFrag = (MakeTripFrag) getSupportFragmentManager().findFragmentByTag(MakeTripFrag.TAG);
                        if (bookFrag != null)
                            bookFrag.trip_cancel_alert();
                        isChanged = true;
                    } else {
                        this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                        isChanged = true;
                    }
                    break;
                case InAppChatFragment.TAG:
                    this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    homeViewModel.GetUserProfile();
                    isChanged = true;
                    break;
                case AddMoneyFrag.TAG:
                    this.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(f).commit();
                    isChanged = true;
                    break;
                case ProfileFrag.TAG: {
                    this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_frame, MapFrag.newInstance("Profile"), MapFrag.TAG)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commitNow();
                    isChanged = true;

                }
                break;
                case MapFrag.TAG:
                    ((MapFrag) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(MapFrag.TAG))).backpressed();
                    isChanged = false;
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

            Snackbar backsnack = Snackbar.make((View) actHomeBinding.homwMainLay, getTranslatedString(R.string.text_double_tap_exit), Snackbar.LENGTH_LONG);
            backsnack.setAction("EXIT", view -> {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            });
            backsnack.setActionTextColor(Color.YELLOW);
            backsnack.show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }

    /**
     * Unregisters {@link BroadcastReceiver}s
     **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BackReceieved);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(listener);
        }
    }

    public void openHomeFrag() {
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_frame, MapFrag.newInstance("Activity"), MapFrag.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void onclickdaily() {

    }

    @Override
    public void onclickrental() {

    }

    @Override
    public void onclickoutstation() {

    }

    @Override
    public void onclickuparrow() {

    }

    @Override
    public void onclicksearchdes() {
        Log.e("xxxHome", "Search Icon Clicked");
    }


    @Override
    public void loadfavouritesdata(List<FavouriteLocations.FavLocData> favData) {
        Log.e("xxxHomeVModel", "Act method called and list :" + favData);
        ((MapFrag) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(MapFrag.TAG))).loadfavdata(favData);
    }

    /**
     * Check for app update
     * Other fragment transactions
     **/
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateCheck();

        if (!sharedPrefence.GetBoolean(SharedPrefence.dont_refresh)) {
            if (!homeViewModel.historyClicked.get() && !homeViewModel.profileClicked.get()) {
                homeViewModel.GetUserProfile();
                homeViewModel.homeClicked.set(true);
                homeViewModel.historyClicked.set(false);
                homeViewModel.profileClicked.set(false);
            } else {
                if (homeViewModel.historyClicked.get()) {
                    homeViewModel.historyClicked.set(true);
                }

                if (homeViewModel.profileClicked.get()) {
                    homeViewModel.profileClicked.set(true);
                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

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
    }

    /**
     * Call this to check if new version of the app is available for the app in store
     **/
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

    public void ShowHideBar(boolean isShow) {
        homeViewModel.hideBar.set(isShow);
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
        if (getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.fragment_frame, ProfileFrag.newInstance(), ProfileFrag.TAG)
                    .commit();

        new Handler().postDelayed(() -> {
            if (getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) != null) {
                Fragment frag = getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG);
                if (frag != null)
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
            }
        }, 200);
    }

    @Override
    public void onClickHome() {
        homeViewModel.profileClicked.set(false);
        homeViewModel.homeClicked.set(true);
        homeViewModel.historyClicked.set(false);

        if (getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) != null) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG);
            if (frag != null)
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }
        if (getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG) != null) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG);
            if (frag != null)
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }
        if (getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) != null) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG);
            if (frag != null)
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
        }

        homeViewModel.porfileApi();
    }

    @Override
    public void onClickHistory() {
        homeViewModel.profileClicked.set(false);
        homeViewModel.homeClicked.set(false);
        homeViewModel.historyClicked.set(true);

        if (getSupportFragmentManager().findFragmentByTag(HistoryFrag.TAG) == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_frame, HistoryFrag.newInstance(), HistoryFrag.TAG)
                    .commitNow();

        new Handler().postDelayed(() -> {
            if (getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG) != null) {
                Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileFrag.TAG);
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
            }
            if (getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG) != null) {
                Fragment frag = getSupportFragmentManager().findFragmentByTag(ProfileEditFrag.TAG);
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).remove(frag).commit();
            }
        }, 200);

    }

    @Override
    public void closeWaitingDialog(boolean movetonextpage) {
        if (waitdialog != null) {
            if (Objects.requireNonNull(waitdialog).isShowing())
                waitdialog.dismiss();

          /*  if (getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG) != null) {
                Intent intentBroadcasts = new Intent();
                intentBroadcasts.setAction(Constants.BroadcastsActions.CANCEL_RECEIVER);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentBroadcasts);
            }*/
        }

        if (movetonextpage) {
            startActivity(new Intent(this, HomeAct.class));
            /*if (this.getSupportFragmentManager().findFragmentByTag(BookingFrag.TAG) != null)
                this.getSupportFragmentManager().beginTransaction().remove(new BookingFrag()).commit();*/
        }

    }

    public void callProfileAPI() {
        homeViewModel.porfileApi();
    }

    @Override
    public void openTripFragment(ProfileModel model) {
        ShowHideBar(false);
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, TripFragment.newInstance(model), TripFragment.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void openFeedbackFrag(ProfileModel model) {
        ShowHideBar(false);
        this.getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_frame, FeedbackFrag.newInstance(model), FeedbackFrag.TAG)
                .commitAllowingStateLoss();
    }

    Dialog waitdialog;

    @Override
    public void openWaitingDialog(final String id) {
        new Handler().postDelayed(() -> {
            MapFrag mapFrag = (MapFrag) getSupportFragmentManager().findFragmentByTag(MapFrag.TAG);
            if (mapFrag != null)
                mapFrag.open_booking_page(id);
        }, 300);

    }

    @Override
    public void openFromStart() {
        startActivity(new Intent(this, LoginOrSignAct.class));
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

    public View getBaseView() {
        return actHomeBinding.homeBg;
    }

    BroadcastReceiver BackReceieved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            homeViewModel.hideBar.set(true);
        }
    };

    @Override
    public void onUpdateMapAfterUserInterection() {
        Log.e("xxxNewHome", "is in PickDrop : " + sharedPrefence.GetBoolean(SharedPrefence.In_PickDrop));

        if (!sharedPrefence.GetBoolean(SharedPrefence.In_PickDrop))
            ((MapFrag) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(MapFrag.TAG))).onUpdateMapAfterUserInterection();
    }
}

