package com.tyt.client.ui.keyValidation;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.app.MyApp;
import com.tyt.client.databinding.ContactUsLayBinding;
import com.tyt.client.databinding.KeyValidationBinding;
import com.tyt.client.ui.EnableLocationAct;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.country.Countrylistdialog;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.ui.loginOrSign.LoginOrSignAct;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * Created by Mahi in 2021.
 *
 * It is used to validate the demo key when the user wants to have a trial version of TYT.
 */

public class KeyValidationAct extends BaseActivity<KeyValidationBinding, KeyValidationViewModel> implements KeyValidationNavigator, HasAndroidInjector {
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    KeyValidationViewModel keyValidationViewModel;
    KeyValidationBinding keyValidationBinding;
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    ColorStateList colorDefaultStateList, colorSelectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyValidationBinding = getViewDataBinding();
        keyValidationViewModel.setNavigator(this);

        keyValidationViewModel.countryCode.set(Constants.defaultCountryCode);

        colorSelectedText = ContextCompat.getColorStateList(MyApp.getmContext(), R.color.clr_FF0000);
        colorDefaultStateList = ContextCompat.getColorStateList(MyApp.getmContext(), R.color.colorPrimaryDark);

    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                countryChoose, new IntentFilter(Constants.INTENT_COUNTRY_CHOOSE));
        super.onResume();

        keyValidationBinding.etDemoKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyValidationViewModel.key.set(charSequence.toString());

                if (charSequence.length() <= 5) {
                    //enter full demo key
                    keyValidationViewModel.valid_demo_key.set(false);
                }else {
                    //demo key is valid
                    keyValidationViewModel.valid_demo_key.set(true);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
              /*  try {

                } catch (NumberFormatException e) {
                    showMessage(e.getMessage());
                }*/
            }
        });
    }


    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void openPages() {
        if (!CommonUtils.isGpscheck(this)){
            startActivity(new Intent(KeyValidationAct.this, EnableLocationAct.class));
            finish();
        } else if(!(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGE).isEmpty() || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES).isEmpty())){
            startActivity(new Intent(KeyValidationAct.this, LoginAct.class));
            finish();
        } else if (CommonUtils.IsEmpty(sharedPrefence.Getvalue(SharedPrefence.AccessToken))){
            startActivity(new Intent(KeyValidationAct.this, LoginOrSignAct.class));
            finish();
        }
    }


    Dialog contact_dg;

    @Override
    public void opencontactdialog() {
        contact_dg = new Dialog(this, R.style.FullViewDialog);

        ContactUsLayBinding dialogbinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.contact_us_lay, null, false);
        dialogbinding.setViewModel(keyValidationViewModel);

        contact_dg.setContentView(dialogbinding.getRoot());

        Window window = contact_dg.getWindow();

        if (window != null) {
            contact_dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        contact_dg.setCancelable(false);
        contact_dg.setCanceledOnTouchOutside(false);

        if (keyValidationViewModel.countryCode.get() != null) {
            dialogbinding.countrychooseDg.setText(keyValidationViewModel.countryCode.get());
        } else {
            dialogbinding.countrychooseDg.setText("+91");
        }

        dialogbinding.countrychooseDg.setOnClickListener(view -> {
            onClickCountryChoose();
        });

        dialogbinding.cancelCnt.setOnClickListener(view -> {
            contact_dg.dismiss();
        });

        contact_dg.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                contact_dg.dismiss();
            }
            return true;
        });

        //Implement Live Text Char Updates
        dialogbinding.messageEtCnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                dialogbinding.compCharCountCntUs.setText("Char Count : " + charSequence.length());

                if (charSequence.length() < 15 || charSequence.length() >= 30) {
                    dialogbinding.compCharCountCntUs.setTextColor(colorSelectedText);
                } else {
                    dialogbinding.compCharCountCntUs.setTextColor(colorDefaultStateList);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        dialogbinding.messageEtCnt.setOnKeyListener((view, i, keyEvent) -> {

            if (i == KeyEvent.KEYCODE_DEL) {
                dialogbinding.messageEtCnt.setText("");
            }
            return false;

        });


        dialogbinding.sendMsgTvCnt.setOnClickListener(view -> {
            //validate fields and after call API

            String fname = "";
            String fphone = "";
            String femail = "";
            String fmessage = "";

            fname = dialogbinding.nameEtCnt.getText().toString();

            fphone = dialogbinding.phoneEtCnt.getText().toString();

            femail = dialogbinding.emailEtCnt.getText().toString();

            fmessage = dialogbinding.messageEtCnt.getText().toString();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if (fname.isEmpty()) {
                showMessage("Your name please");
            } else if (fphone.isEmpty()) {
                showMessage("Phone no is empty");
            } else if (fphone.length() <= 9) {
                showMessage("Enter Full phone no");
            } else if (femail.isEmpty()) {
                showMessage("Email is empty");
            } else if (!validate(femail) && emailPattern.matches(femail)) {
                showMessage(femail);
                showMessage("Email is not valid");
            } else {
                //call API
                showMessage("Ready to send");

                HashMap<String, String> datamap = new HashMap<>();

                datamap.put(Constants.NetworkParameters.name, fname);
                datamap.put(Constants.NetworkParameters.mobile, fphone);
                datamap.put(Constants.NetworkParameters.email, femail);
                if (!fmessage.isEmpty()) {
                    datamap.put(Constants.NetworkParameters.message, fmessage);
                }
                datamap.put("app_type","TYT - User");
                datamap.put(Constants.NetworkParameters.country, keyValidationViewModel.countryCode.get());

                keyValidationViewModel.senddatatocontact(datamap);

            }

        });

        contact_dg.show();
    }

    @Override
    public void openwebsite() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL.web_enquiry_url)));
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    String CountryCode, countryShort;

    @Override
    public void onClickCountryChoose() {
        Countrylistdialog newInstance = Countrylistdialog.newInstance();
        newInstance.show(this.getSupportFragmentManager());
    }

    @Override
    public String getCountryCode() {
        return CountryCode != null ? (CountryCode.replaceAll(" ", "")) : "";
    }

    @Override
    public String getCountryShortName() {
        return countryShort;
    }

    @Override
    public void setCountryCode(String flat) {

    }

    @Override
    public void closecontactpage() {
        new Handler().postDelayed(() -> {
            if (contact_dg.isShowing() && contact_dg != null) {
                contact_dg.dismiss();
            }
        }, 700);
    }

    @Override
    public KeyValidationViewModel getViewModel() {
        return keyValidationViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.key_validation;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private final BroadcastReceiver countryChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(Constants.INTENT_COUNTRY_CHOOSE))
                    keyValidationViewModel.countryCode.set(intent.getStringExtra(Constants.countryCode));
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countryChoose);
    }
}