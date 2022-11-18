package com.tyt.client.ui.splash;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.SplashscreenBinding;
import com.tyt.client.retro.responsemodel.TranslationModel;
import com.tyt.client.ui.EnableLocationAct;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.keyValidation.KeyValidationAct;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.language.LanguageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Mahi in 2021.
 */

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
    String[] permission_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGCMDeviceToken();
        binding = getViewDataBinding();

        emptyViewModel.setNavigator(this);

        permission_array = Constants.Array_permissions;

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

        if (!CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE))) {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
        }
        startRequestingPermissions();

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

        if ((requestCode == Constants.REQUEST_PERMISSION) && checkGranted(grantResults)) {
            RedirectPage();
        } else {
            showDisclosureDialog(permission_array);
        }

    }

    /**
     * Runnable to move on launch screens.
     */
    Runnable runnable = this::initiateNaviagation;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Alert for location permissions.
     */
    private void showDisclosureDialog(final String[] permission_array) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.RoundedDialogNarrow);
        ad.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.layout_location_popup, null);
        ad.setView(view);
        final AlertDialog locdialog = ad.create();

        if (!locdialog.isShowing() && !this.isFinishing()) {
            locdialog.show();
        }

        TextView textView = locdialog.findViewById(R.id.txt_bg_location);
        Button btnContinue = locdialog.findViewById(R.id.btn_bg_continue);
        Button btnExit = locdialog.findViewById(R.id.btn_bg_exit);

        String text = getLocalizedString(getString(R.string.txt_bg_location_msg));
        String Continue = getLocalizedString(getString(R.string.Txt_Continue));
        String Exit = getLocalizedString(getString(R.string.Txt_Exit));

        if (textView != null) {
            textView.setText(text);
        }
        if (btnContinue != null) {
            btnContinue.setText(Continue);
        }
        if (btnExit != null) {
            btnExit.setText(Exit);
        }

        if (btnContinue != null) {
            btnContinue.setOnClickListener(view1 -> {
                locdialog.dismiss();
                requestPermissionsSafely(permission_array, Constants.REQUEST_PERMISSION);
            });
        }

        if (btnExit != null) {
            btnExit.setOnClickListener(view12 -> finish());
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Constants.Array_permissions)) {
            requestPermissionsSafely(Constants.Array_permissions, Constants.REQUEST_PERMISSION);
        } else
            RedirectPage();
    }

    /**
     * set the app launches.
     */
    private void initiateNaviagation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGranted(Constants.Array_permissions)) {
            requestPermissionsSafely(permission_array, Constants.REQUEST_PERMISSION);
        } else {
            RedirectPage();
        }
    }

    private void RedirectPage() {
        Log.e("key--", "key--" + sharedPrefence.Getvalue(SharedPrefence.keyValue));

        /*if (sharedPrefence.Getvalue(SharedPrefence.keyValue).isEmpty()) {
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
}