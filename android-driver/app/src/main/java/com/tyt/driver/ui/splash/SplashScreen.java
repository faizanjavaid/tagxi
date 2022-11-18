package com.tyt.driver.ui.splash;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.tyt.driver.BR;
import com.tyt.driver.R;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.databinding.SplashscreenBinding;
import com.tyt.driver.retro.responsemodel.TranslationModel;
import com.tyt.driver.ui.EnableLocationAct;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.homeScreen.HomeAct;
import com.tyt.driver.ui.keyValidation.KeyValidationAct;
import com.tyt.driver.ui.login.LoginAct;
import com.tyt.driver.ui.loginOrSign.LoginOrSignAct;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.language.LanguageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

//import org.jsoup.Jsoup;

/**
 * Created by naveen on 8/24/17.
 */

@RequiresApi(api = Build.VERSION_CODES.Q)
public class SplashScreen extends BaseActivity<SplashscreenBinding, SplashViewModel> implements SplashNavigator {
    private static final String TAG = "SplashScreen";
    @Inject
    SplashViewModel emptyViewModel;
    @Inject
    SharedPrefence sharedPrefence;
    String language = "";
    SplashscreenBinding binding;
    @Inject
    Gson gson;
    TranslationModel translationModel;

    String alert_permission, alert_message, alert_ok, alert_cancel;
    String[] permission_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getGCMDeviceToken();
        binding = getViewDataBinding();

        emptyViewModel.setNavigator(this);
        permission_array = Constants.Array_permissions;


      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            showMessage("android 10 or 11");
        } else {
            permission_array = Constants.Array_permissions;
            showMessage("android old");
        }*/

        configureLanguage();

        Resources resources = MyApp.getmContext().getResources();
        Configuration config = resources.getConfiguration();
        Log.d(TAG, "onCreate: " + config.locale.toString());
        if (config.locale.toString().contains("_")) {
            String[] phoneLang = config.locale.toString().split("_");
            Log.e("Phonelang===", phoneLang[0]);
            language = phoneLang[0];
        } else {
            language = config.locale.toString();
        }

        // emptyViewModel.getLanguagees();

        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }
        startRequestingPermissions();

       /* getvaluefromsheets();
        if (translationModel == null) {
            getvaluefromsheets();
        }*/

        //getKeyHash();
        //Glide.with(this).asGif().load(R.drawable.splash_loader).into(binding.splashLoaderGfView);

    }

    public void getvaluefromsheets() {
        alert_permission = translationModel.txt_alert_title_permission;
        alert_message = translationModel.txt_alert_msg_location;
        alert_cancel = translationModel.txt_exit;
        alert_ok = translationModel.txt_ok;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //emptyViewModel.getLanguagees();
    }

    /**
     * Set the app default language.
     */
    private void configureLanguage() {
        if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)))
            sharedPrefence.savevalue(SharedPrefence.LANGUANGE, "en");
        LanguageUtil.changeLanguageType(this, Locale.ENGLISH);
    }

    @Override
    public SplashViewModel getViewModel() {
        return emptyViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.splashscreen;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //showMessage("onRequestPermissionsResult method called");

        if ((requestCode == Constants.REQUEST_PERMISSION)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //Android 10 or 11
                if (grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
                    requestPermissionsSafely(Constants.backgroundlocpermission, Constants.REQUEST_BGLOC_PERMISSION);
                    //RedirectPage();
                } else showDisclosureDialog(permission_array, Constants.REQUEST_PERMISSION);

            } else {
                //Old Android Versions
                if (!checkGranted(grantResults)) {
                    showDisclosureDialog(permission_array, Constants.REQUEST_PERMISSION);
                } else RedirectPage();
            }

        } else if (requestCode == Constants.REQUEST_BGLOC_PERMISSION) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                //recalling all permissions in android
                requestPermissionsSafely(Constants.all10permission, Constants.REQUEST_ALL10_PERMISSION);
            } else {
                showDisclosureDialog(Constants.backgroundlocpermission, Constants.REQUEST_BGLOC_PERMISSION);
            }
        } else if (requestCode == Constants.REQUEST_ALL10_PERMISSION) {
            if (grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED && grantResults[2] == PERMISSION_GRANTED) {
                //all permissions granted for android 10 or higher
                RedirectPage();
            } else if (grantResults[0] != PERMISSION_GRANTED || grantResults[1] != PERMISSION_GRANTED) {
                requestPermissionsSafely(Constants.Array_permissions, Constants.REQUEST_PERMISSION);
            } else {
                requestPermissionsSafely(Constants.backgroundlocpermission, Constants.REQUEST_BGLOC_PERMISSION);
            }
        }
    }

    private boolean autoStartEnable() {
        Log.d("xxxSplacshScreen", "autoStartEnable: ");
        String manufacturer = android.os.Build.MANUFACTURER;
        try {
            Intent intent = new Intent();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // initiateNaviagation();
    /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permission_array = Constants.Array_permissions_10;
        } else {
            permission_array = Constants.Array_permissions;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(permission_array)) {
            showDisclosureDialog(permission_array);
        } else {
            initiateNaviagation();
        }*/
    /**
     * Runnable to move on launch screens.
     */
    Runnable runnable = this::initiateNaviagation;

    private void showDisclosureDialog(final String[] permission_array, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getAttachedContext(), R.style.RoundedDialogNarrow);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.layout_location_popup, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (!dialog.isShowing() && !this.isFinishing()) {
            dialog.show();
        }

        TextView textView = dialog.findViewById(R.id.txt_bg_location);
        Button btnContinue = dialog.findViewById(R.id.btn_bg_continue);
        Button btnExit = dialog.findViewById(R.id.btn_bg_exit);
        String str11Content = getTranslatedString(R.string.txt_bg_location);
        if (str11Content.equalsIgnoreCase("txt_bg_location"))
            str11Content = getString(R.string.txt_bg_location);
        String strContent = getTranslatedString(R.string.txt_bg_location_msg);
        if (strContent.equalsIgnoreCase("txt_bg_location_msg"))
            strContent = getString(R.string.txt_bg_location_msg);
        String strContinue = getTranslatedString(R.string.Txt_Continue);
        if (strContinue.equalsIgnoreCase("Txt_Continue"))
            strContinue = getString(R.string.Txt_Continue);
        String strExit = getTranslatedString(R.string.Txt_Exit);
        if (strExit.equalsIgnoreCase("Txt_Exit"))
            strExit = getString(R.string.Txt_Exit);

        if (textView != null)
            if (Build.VERSION.SDK_INT >= 30) {
                textView.setText(str11Content);
            } else textView.setText(strContent);
        if (btnContinue != null)
            btnContinue.setText(strContinue);
        if (btnExit != null)
            btnExit.setText(strExit);

        if (btnContinue != null)
            btnContinue.setOnClickListener(v -> {
                dialog.dismiss();
                requestPermissionsSafely(permission_array, type);
            });

        if (btnExit != null)
            btnExit.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public BaseActivity getAttachedContext() {
        return this;
    }

    /**
     * set the delaying for 4 seconds, after 4 seconds the runnable methods called..
     */
    @Override
    public void startRequestingPermissions() {
        new Handler().postDelayed(runnable, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // showMessage("onActivityResult method called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkGranted(permission_array)) {
                //user didn't give all permissions
                showDisclosureDialog(permission_array, Constants.REQUEST_PERMISSION);
                //requestPermissionsSafely(Constants.Array_permissions, Constants.REQUEST_PERMISSION);
            } else {
                RedirectPage();
            }
        }

    }

    /**
     * set the app launches.
     */
    private void initiateNaviagation() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permission_array = Constants.Array_permissions_10;
            showMessage("android 10 or 11");
        } else {
            permission_array = Constants.Array_permissions;
            showMessage("android old");
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !checkGranted(permission_array)) {
            showDisclosureDialog(permission_array, Constants.REQUEST_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && !checkGranted(permission_array)) {
            //showDisclosureDialog(permission_array);
            requestPermissionsSafely(permission_array, Constants.REQUEST_PERMISSION);
        } else RedirectPage();
    }

    private void RedirectPage() {
       /* if (sharedPrefence.Getvalue(SharedPrefence.keyValue).isEmpty()) {
            startActivity(new Intent(this, KeyValidationAct.class));
        } else {*/
            if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.AccessToken)) && CommonUtils.isGpscheck(this)) {
                startActivity(new Intent(SplashScreen.this, HomeAct.class));
            } else {
                if (!CommonUtils.isGpscheck(this))
                    startActivity(new Intent(SplashScreen.this, EnableLocationAct.class));
                else if (!(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGE).isEmpty() || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES).isEmpty()))
                    startActivity(new Intent(SplashScreen.this, LoginAct.class));
                else
                    startActivity(new Intent(SplashScreen.this, LoginOrSignAct.class));
            }
        //}
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Alert for location permissions.
     */
    private void alertCalling() {
        AlertDialog ad = new AlertDialog.Builder(getAttachedContext())
                .setTitle(translationModel.txt_alert_title_permission)
                .setCancelable(false)
                .setMessage(translationModel.txt_alert_msg_location)
                .setPositiveButton(translationModel.txt_ok, (dialog, which) -> {
                    dialog.dismiss();
                    requestPermissionsSafely(Constants.Array_permissions, Constants.REQUEST_PERMISSION);
                })
                .setNegativeButton(translationModel.txt_cancel, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        if (!isFinishing())
            ad.show();
    }

    /**
     * Get HaskKey for facebook login.
     */
   /* private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("KeyHash==", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            Log.e("KeyHash==", "printHashKey()", e);
        }
    }*/

    /*public void playGif(){
        Animation fadeout = new AlphaAnimation(1.f, 1.f);
        fadeout.setDuration(2500); // You can modify the duration here
        fadeout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                binding.splashLoaderGfView.setBackgroundResource(R.drawable.splash_loader);//your gif file
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        binding.splashLoaderGfView.startAnimation(fadeout);
    }*/
}