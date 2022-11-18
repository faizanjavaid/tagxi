package com.tyt.client.ui.loginOrSign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.client.BR;
import com.tyt.client.R;
import com.tyt.client.databinding.LoginSignBinding;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.TranslationModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.login.LoginAct;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.language.LanguageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Mahi in 2021.
 * <p>
 * It is a activity which is actually used to get the languages list in the first visit of a user.
 */

public class LoginOrSignAct extends BaseActivity<LoginSignBinding, LoginOrSignViewModel> implements LoginOrSignNavigator {
    private static final String TAG = "LoginorSigninAct";

    @Inject
    LoginOrSignViewModel loginOrSignViewModel;
    LoginSignBinding loginSignBinding;

    TranslationModel translationModel;
    @Inject
    SharedPrefence sharedPrefence;
    @Inject
    Gson gson;

    List<String> languagelist;

    BufferedReader bufferedReader;
    StringBuffer stringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginSignBinding = getViewDataBinding();
        loginOrSignViewModel.setNavigator(this);

        languagelist = new ArrayList<>();

        if (sharedPrefence.Getvalue(SharedPrefence.LANGUANGE) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGE).isEmpty() || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES) == null || sharedPrefence.Getvalue(SharedPrefence.LANGUANGES).isEmpty()) {

            /**
             *
             * Instead Of Getting the Translations From API we will use the Json File Presented in Our RAW Folder
             *
             * */
            //loginOrSignViewModel.getLanguagees();

            BaseResponse response = new BaseResponse();
            response.saveLanguageTranslations(sharedPrefence, gson, gettranslationFromJson(R.raw.translations));

            loginOrSignViewModel.onGetLanguagesList();

            showMessage("we are Getting Your Languages...Please Wait...");
        } else {
            translationModel = gson.fromJson(sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)), TranslationModel.class);
            loginOrSignViewModel.mIsLoading.set(false);
            loginOrSignViewModel.onGetLanguagesList();
        }

        if (translationModel != null) {
            if (translationModel.txt_continue != null)
                loginOrSignViewModel.continueText.set(translationModel.txt_continue);
            else
                loginOrSignViewModel.continueText.set("continue");

            if (translationModel.txt_choose_language != null)
                loginOrSignViewModel.helperText.set(translationModel.txt_choose_language);
            else
                loginOrSignViewModel.helperText.set("Select your language");

            if (translationModel.txt_ride_demand != null)
                loginOrSignViewModel.tittleText.set(translationModel.txt_ride_demand);
            else
                loginOrSignViewModel.tittleText.set("Your ride, on demand");

            if (translationModel.txt_fastest_way != null)
                loginOrSignViewModel.contentText.set(translationModel.txt_fastest_way);
            else
                loginOrSignViewModel.contentText.set("Fastest way to book taxi without the hassle of waiting & haggling for price");
        }

    }


    public BaseResponse.DataObject gettranslationFromJson(Integer rawId) {

        stringBuffer = new StringBuffer();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(rawId)));

            String temp;

            while ((temp = bufferedReader.readLine()) != null)
                stringBuffer.append(temp);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BaseResponse response = (BaseResponse) CommonUtils.StringToObject(stringBuffer.toString(),BaseResponse.class);
        String translationString = CommonUtils.ObjectToString(response.data);
        BaseResponse.DataObject dataObject = (BaseResponse.DataObject) CommonUtils.StringToObject(translationString,BaseResponse.DataObject.class);

        return dataObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public BaseActivity getBaseAct() {
        return this;
    }

    @Override
    public void onClickLogin() {
        startActivity(new Intent(this, LoginAct.class));
        finish();
        setSelectedLanguage();
    }

    private void setSelectedLanguage() {
        String loc = sharedPrefence.Getvalue(SharedPrefence.LANGUANGE);
        ;
        if (!TextUtils.isEmpty(loc)) {
            Locale locale = Locale.getDefault();
            Locale.setDefault(new Locale(loc));
            LanguageUtil.changeLanguageType(this, locale);
        } else {
            LanguageUtil.changeLanguageType(this, Locale.ENGLISH);
        }
    }

    @Override
    public void languageslist(List<String> languages) {
        languagelist = languages;
        setSpinner();
        Log.e("xxxLang", String.valueOf(languagelist));
    }

    @Override
    public LoginOrSignViewModel getViewModel() {
        return loginOrSignViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_sign;
    }

    @Override
    public SharedPrefence getSharedPrefence() {
        return sharedPrefence;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    ArrayAdapter<String> spinadapter;

    public void setSpinner() {
        spinadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languagelist);

        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loginSignBinding.languageSpinner.setAdapter(spinadapter);

        loginSignBinding.languageSpinner.setSelection(1);

        loginSignBinding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLanguage = (String) adapterView.getSelectedItem();

                displaylanguagedata(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void displaylanguagedata(String selectedLanguage) {

        //showMessage(selectedLanguage);
        String loc = "";
        if (selectedLanguage.equalsIgnoreCase("தமிழ்")) {
            loc = "ta";
        } else if (selectedLanguage.equalsIgnoreCase("عربى")) {
            loc = "ar";
        } else if (selectedLanguage.equalsIgnoreCase("English")) {
            loc = "en";
        } else if (selectedLanguage.equalsIgnoreCase("español")) {
            loc = "es";
        } else if (selectedLanguage.equalsIgnoreCase("日本語")) {
            loc = "ja";
        } else if (selectedLanguage.equalsIgnoreCase("한국어")) {
            loc = "ko";
        } else if (selectedLanguage.equalsIgnoreCase("português")) {
            loc = "pt";
        } else if (selectedLanguage.equalsIgnoreCase("中国人")) {
            loc = "zh";
        } else if (selectedLanguage.equalsIgnoreCase("हिंदी")) {
            loc = "hi";
        }

        sharedPrefence.savevalue(SharedPrefence.LANGUANGE, loc);

    }
}