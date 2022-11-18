package com.tyt.driver.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tyt.driver.R;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.Driver;
import com.tyt.driver.retro.responsemodel.Request;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.NetworkUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.language.LanguageUtil;
import com.tyt.driver.utilz.language.MyContextWrapper;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


/**
 * Created by amitshekhar on 07/07/17.
 */

public abstract class BaseActivity<T extends ViewDataBinding, V> extends AppCompatActivity implements BaseFragment.Callback, BaseView {

    public static final int REQUEST_ENABEL_INTERNET = 199;
    public static final int REQUEST_ENABEL_LOCATION = 200;
    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;

    private T mViewDataBinding;
    private V mViewModel;
    public String check;
    public AppBarLayout mappBarlayout;
    public Toolbar mToolbar;
    private Resources resources;

    @Inject
    SharedPrefence sharedPrefence;

    public Dialog dialog_internet;

    public HashMap<String, String> Bindabledata = new HashMap<>();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
      /*  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build());*/


        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(Constants.EXTRA_Data)) {
            Bindabledata = (HashMap<String, String>) getIntent().getSerializableExtra(Constants.EXTRA_Data);
        }
        resources = getResources();
        performDependencyInjection();
        performDataBinding();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());

        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.d("xxxBaseAct", "attachBaseContext: call to => LanguageUtil.getLanguageType(this)");
        Locale languageType = LanguageUtil.getLanguageType(this);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    /**
     * ask for run time permissions in Android 6 & above
     **/
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("xxxBaseAct", "requestPermissionsSafely: permissions =" + permissions);
            requestPermissions(permissions, requestCode);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkGranted(String[] permissions) {
        Log.d("xxxBaseAct", "checkGranted() called with: permissions = [" + permissions + "]");
        for (String per : permissions) {

            if (checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkGranted(int[] permissions) {
        Log.d("xxxBaseAct", "checkGranted: ");
        for (int per : permissions) {

            if (per != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }


    /**
     * to reflect changes of Language in All screens if Arabic RTL
     */
    public void changeDiretionLanguage(Context context, Window window) {
        configureLanguage();
        if (sharedPrefence.Getvalue(SharedPrefence.LANGUANGE).equalsIgnoreCase("ar")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                window.getDecorView().setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                window.getDecorView().setTextDirection(View.TEXT_DIRECTION_LTR);
            }
        }


    }

    /**
     * configuring app language
     **/
    private void configureLanguage() {
        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)))
            sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "en");
        String localLanguageType = sharedPrefence.Getvalue(SharedPrefence.LANGUANGE);
        if ("es".equals(localLanguageType)) {
            Locale locale = Locale.getDefault();
            Locale.setDefault(new Locale("es"));
            LanguageUtil.changeLanguageType(this, locale);
        } else if (!TextUtils.isEmpty(localLanguageType)) {
            Locale locale = Locale.getDefault();
            Locale.setDefault(new Locale(localLanguageType));
            LanguageUtil.changeLanguageType(this, locale);
        } else {
            LanguageUtil.changeLanguageType(this, Locale.ENGLISH);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bcastReceiver != null) {
            IntentFilter intentFilter = new IntentFilter(Constants.BroadcastsActions.B_REQUEST);
            LocalBroadcastManager.getInstance(this).registerReceiver(bcastReceiver, intentFilter);
        }
        IntentFilter intentFilter = new IntentFilter(Constants.BroadcastsActions.LATER_NO_DRIVER);
        LocalBroadcastManager.getInstance(this).registerReceiver(laterNoDriver, intentFilter);

        changeDiretionLanguage(this, getWindow());
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(laterNoDriver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Mapscrn123", "Mapscrn123");

            removeWaitProgressDialog();
            if (intent.hasExtra(Constants.EXTRA_Data)) {
                String json = intent.getExtras().getString(Constants.EXTRA_Data);

                BaseResponse baseResponse = CommonUtils.getSingleObject(json, BaseResponse.class);
                /*if (baseResponse != null && baseResponse.getRequest() != null) {
                    if (baseResponse.getRequest().later != null && baseResponse.getRequest().later == 1) {
                        //   Toast.makeText(BaseActivity.this, getTranslatedString(R.string.text_accepted), Toast.LENGTH_SHORT).show();
                    }
                }*/
            }

        }
    };

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void NeedRideFragment(final Type type) {

    }

    @Override
    public void setResultToDropAddress(String Address) {

    }

    @Override
    public void NeedTripFragment(Request request, Driver driver) {

    }

    public void NeedFeedbackFragment(Request request, boolean isCorporate) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentInternetFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentInternetFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReceiver, intentInternetFilter);
    }

    @Override
    public void NeedHomeFragment() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetReceiver);
    }

    @Override
    public void changeTripnFeedback() {

    }

    public BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(ConnectivityManager.CONNECTIVITY_ACTION)) {
                checkInternetEnabled();
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * this hides soft keyboard when called
     **/
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * shows toast with translation text from string resource
     **/
    public void showMessage(int resId) {
        Toast.makeText(this, getTranslatedString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * shows toast with exception msg
     **/
    public void showMessage(CustomException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * returns true if internet connected, false if not connected
     **/
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    /**
     * shows toast with given string
     **/
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * shows circular progressbar
     **/
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    /**
     * hides circular progressbar if already showing
     **/
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * @return SharedPrefereance
     */
    public abstract SharedPrefence getSharedPrefence();

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    /**
     * shows snackbar
     **/
    @Override
    public void showSnackBar(String message) {
        if (getViewModel() != null) {
            Snackbar snackbar = Snackbar.make((View) getViewModel(), message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    /**
     * shows snackbar
     **/
    @Override
    public void showSnackBar(@NonNull View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    /**
     * shows network error msg
     **/
    @Override
    public void showNetworkMessage() {
        Toast.makeText(this, getTranslatedString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show();
    }

    /**
     * shows network enable dialog
     **/
    private void enableInternetDialog() {
        if (dialog_internet != null) {

            if (dialog_internet.isShowing()) {
                dialog_internet.dismiss();
            }

            dialog_internet = new Dialog(this);
            dialog_internet.setContentView(R.layout.dialog_enable_internet);
            if (dialog_internet.getWindow() != null) {
                dialog_internet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            TextView tInternetTitle = dialog_internet.findViewById(R.id.txt_internet_title);
            TextView tInternetContent = dialog_internet.findViewById(R.id.txt_internet_content);
            TextView tInternetExit = dialog_internet.findViewById(R.id.btn_exit_internetialog);
            TextView tInternetSettings = dialog_internet.findViewById(R.id.btn_settings_internetdialog);

            tInternetTitle.setText("Confirm");
            tInternetContent.setText("A connection to the internet is required to continue");
            tInternetExit.setText("Exit");
            tInternetSettings.setText("Settings");

            dialog_internet.findViewById(R.id.btn_exit_internetialog).setOnClickListener(view -> finish());
            dialog_internet.findViewById(R.id.btn_settings_internetdialog).setOnClickListener(view -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), REQUEST_ENABEL_INTERNET));
            dialog_internet.setCanceledOnTouchOutside(false);

            if (!dialog_internet.isShowing() && !((Activity) this).isFinishing()) {
                dialog_internet.show();
            }
        }
    }

    /**
     * checks if internet enabled if not shows the enable internet dialog
     **/
    public boolean checkInternetEnabled() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = true;
        result = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if (!result)
            enableInternetDialog();
        else {
            if (dialog_internet != null)
                if (dialog_internet.isShowing()) {
                    dialog_internet.setCancelable(true);
                    dialog_internet.dismiss();
                }
        }

        return result;
    }

    /**
     * returns translated string for the given resource id
     **/
    public String getTranslatedString(int id) {
        if (resources.getResourceEntryName(id).equalsIgnoreCase("app_name") || resources.getResourceEntryName(id).equalsIgnoreCase("app_title"))
            return super.getString(id);
        else
            return getLocalizedString(resources.getResourceEntryName(id));
    }

    /**
     * returns string from translation sheet for given key
     **/
    public String getLocalizedString(String resId) {
        String result = resId;
        try {
            JSONObject jsonObject;
            if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
                jsonObject = new JSONObject(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)));
                result = jsonObject.optString(resId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (TextUtils.isEmpty(result))
                result = resId;
        }
        return result;

    }

    /**
     * receives broadcast if the same user logged in another device and logs out the user
     **/
    public BroadcastReceiver bcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String intentDAta = intent.getStringExtra(Constants.BroadcastsActions.B_REQUEST);
                if (!CommonUtils.IsEmpty(intentDAta)) {
                    Log.d("keys", "showingMessage:" + intentDAta);
                    BaseResponse model = CommonUtils.getSingleObject(intentDAta, BaseResponse.class);
                   /* if (model != null)
                        if (model.successMessage != null) {
                            if (model.successMessage.equalsIgnoreCase("another_user_loggedin"))
                                Toast.makeText(getBaseContext(), getTranslatedString(R.string.text_already_login), Toast.LENGTH_LONG).show();
                            sharedPrefence.savevalue(SharedPrefence.USERDETAILS, null);
                            sharedPrefence.savevalue(SharedPrefence.SOSLIST, null);
                            sharedPrefence.savevalue(SharedPrefence.LATITUDE, null);
                            sharedPrefence.savevalue(SharedPrefence.LONGITUDE, null);
                            finish();
                        }*/
                }
            }
        }
    };


//    @Override
//    public Resources getResources() {
//        return new CustomResources(sharedPrefence, getAssets(), super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
//    }

    /**
     * generates HASH string for user registration & social media login
     **/
    public void generateKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key new ", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    /**
     * retrieves & saves Firebase Cloud Messaging device token for sending push notifications
     **/
    public void getGCMDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().intern();
                    sharedPrefence.savevalue(SharedPrefence.FCMTOKEN, "" + token);
                    Log.e("xxxRefreshTokenOLD", sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
                });




       /* if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN))) {
            String token = FirebaseInstanceId.getInstance().getToken();
            if (!CommonUtils.IsEmpty(token)) {
                sharedPrefence.savevalue(SharedPrefence.FCMTOKEN, token);
                Log.e("RefreshToken", token);
            }
        } else {
            Log.e("RefreshTokenOLD", sharedPrefence.Getvalue(SharedPrefence.FCMTOKEN));
        }*/
    }


    /**
     * receives broadcast when no driver found and shows no driver screen
     **/
    public BroadcastReceiver laterNoDriver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  if (MyApp.isIsNodDriveActDestroyed())

        }
    };

    /**
     * removes waiting for driver screen
     **/
    public void removeWaitProgressDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    /**
     * removes confirm ride booking screen
     **/
    public void removeRideConfirmationFragment() {

    }

    /**
     * opens an activity to refresh company key
     **/
    @Override
    public void refreshCompanyKey() {
        finish();
    }

    public void HideNshowToolbar(boolean hide) {
        if (hide) {
            mappBarlayout.animate().translationY(-100).alpha(0.0f);
            mToolbar.setVisibility(View.GONE);

        } else {
            mappBarlayout.animate().translationY(0).alpha(1.0f).setDuration(100);
            mToolbar.setVisibility(View.VISIBLE);

        }
    }
}

/*
*
*
* 2021-05-27 20:29:07.455 6437-6437/com.tyt.driver E/LocationUpdated===: updates==11.0725443
        2021-05-27 20:29:07.456 6437-6437/com.tyt.driver E/lat&lng--: 11.0725443____77.3333494
        2021-05-27 20:29:07.457 6437-6437/com.tyt.driver E/lastUpdate: ______1622127547457_____0
        2021-05-27 20:29:07.457 6437-6437/com.tyt.driver E/fjokj: 11.0725443___77.3333494___-20.868782___38
        2021-05-27 20:29:07.457 6437-6437/com.tyt.driver E/ID--: IDVALUE--38
        2021-05-27 20:29:07.460 6437-6437/com.tyt.driver E/shareStatus--: statussss---7563beee-2bb3-4559-bc47-31468d1531ef
        2021-05-27 20:29:07.460 6437-6437/com.tyt.driver E/Updatingg--: ffr
        2021-05-27 20:29:12.558 6437-6437/com.tyt.driver E/LocationUpdated===: updates==11.0724791
        2021-05-27 20:29:12.560 6437-6437/com.tyt.driver E/lat&lng--: 11.0724791____77.333351
        2021-05-27 20:29:12.560 6437-6437/com.tyt.driver E/lastUpdate: ______1622127552560_____0
        2021-05-27 20:29:12.560 6437-6437/com.tyt.driver E/fjokj: 11.0725443___77.3333494___0.0___38
        2021-05-27 20:29:12.560 6437-6437/com.tyt.driver E/ID--: IDVALUE--38
        2021-05-27 20:29:12.562 6437-6437/com.tyt.driver E/shareStatus--: statussss---7563beee-2bb3-4559-bc47-31468d1531ef
        2021-05-27 20:29:12.562 6437-6437/com.tyt.driver E/Updatingg--: ffr
        2021-05-27 20:29:14.298 6437-6437/com.tyt.driver E/xxxrequestData---: data---{"success":true,"success_message":"request_created","result":{"data":{"id":"aebf4bda-ada1-41b6-896a-3df3098eab31","request_number":"REQ_000230","is_later":null,"user_id":141,"trip_start_time":null,"arrived_at":null,"accepted_at":null,"completed_at":null,"is_driver_started":null,"is_driver_arrived":null,"updated_at":"27th May 08:29 PM","is_trip_start":null,"total_distance":"0.00","total_time":null,"is_completed":null,"is_cancelled":null,"cancel_method":null,"payment_opt":1,"is_paid":null,"user_rated":null,"driver_rated":null,"unit":"KM","zone_type_id":"0a17c9a2-c8ba-4cc0-9b55-cc160f474299","vehicle_type_name":"Suv","vehicle_type_image":"https:\/\/admin.tagyourtaxi.com\/storage\/uploads\/types\/images\/swHmGCaQsK7DgIkr24ptoYbrMpr3wdA9Cr0geuWc.jpg","car_make_name":"-","car_model_name":"-","pick_lat":11.07259358,"pick_lng":77.33322602,"drop_lat":null,"drop_lng":null,"pick_address":"Karuppa Gaundan Palayam, Tiruppur, 641605, Tamil Nadu, India","drop_address":null,"requested_currency_code":"INR","requested_currency_symbol":null,"user_cancellation_fee":0,"userDetail":{"data":{"id":141,"name":"nisath","last_name":null,"username":null,"email":"nisat.mobi@gmail.com","mobile":"6384300737","profile_picture":"https:\/\/admin.tagyourtaxi.com\/assets\/images\/default-profile-picture.jpeg","active":1,"email_confirmed":0,"mobile_confirmed":1,"last_known_ip":"1.38.196.147","last_login_at":"2021-05-25 11:27:38","rating":2.85,"no_of_ratings":2,"refferal_code":"Qr7MR6","currency_code":"INR","currency_symbol":"\u20b9","map_key":"AIzaSyBeVRs1icwooRpk7ErjCEQCwu0OQowVt9I","referral_comission_string":"Refer a friend and earn\u20b930","sos":{"data":[{"id":"b07b76fc-b706-4c10-b95e-3c78d689f317","name":"Emergency","number":"911","status":false}]}}}}}}
        2021-05-27 20:29:14.638 6437-6437/com.tyt.driver E/ExtMediaPlayer-JNI: env->IsInstanceOf fails
        2021-05-27 20:29:14.638 6437-6437/com.tyt.driver E/MediaPlayer-JNI: JNIMediaPlayerFactory: bIsQCMediaPlayerPresent 0
        2021-05-27 20:29:14.638 6437-6437/com.tyt.driver E/ExtMediaPlayer-JNI: env->IsInstanceOf fails
        2021-05-27 20:29:14.638 6437-6437/com.tyt.driver E/MediaPlayer-JNI: JNIMediaPlayerFactory: bIsQCMediaPlayerPresent 0
        2021-05-27 20:29:16.450 6437-6437/com.tyt.driver E/MediaPlayer: start called in state 1, mPlayer(0x0)
        2021-05-27 20:29:16.450 6437-6437/com.tyt.driver E/MediaPlayer: error (-38, 0)
        2021-05-27 20:29:16.450 6437-6437/com.tyt.driver E/ActPaused: Pause
        2021-05-27 20:29:16.454 6437-6437/com.tyt.driver E/MediaPlayer: prepareAsync called in state 0, mPlayer(0x0)
        2021-05-27 20:29:16.455 6437-6437/com.tyt.driver E/AndroidRuntime: FATAL EXCEPTION: main
        Process: com.tyt.driver, PID: 6437
        java.lang.IllegalStateException
        at android.media.MediaPlayer.prepareAsync(Native Method)
        at com.tyt.driver.ui.acceptReject.AccepRejectAct.onResume(AccepRejectAct.java:229)
        at android.app.Instrumentation.callActivityOnResume(Instrumentation.java:1270)
        at android.app.Activity.performResume(Activity.java:6958)
        at android.app.ActivityThread.performNewIntents(ActivityThread.java:2898)
        at android.app.ActivityThread.handleNewIntent(ActivityThread.java:2905)
        at android.app.ActivityThread.-wrap15(ActivityThread.java)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1622)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:165)
        at android.app.ActivityThread.main(ActivityThread.java:6375)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:912)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:802)
*
* */
