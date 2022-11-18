package com.tyt.driver.ui.loginOrSign;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.language.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class LoginOrSignViewModel extends BaseNetwork<BaseResponse, LoginOrSignNavigator> {
    private static final String TAG = "LoginOrSignViewModel";
    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;
    public ObservableBoolean stopTimer = new ObservableBoolean(false);
    public ObservableField<Integer> totalTime = new ObservableField<>(10000);
    public ObservableField<Integer> intervalTime = new ObservableField<>(2000);
    /*BaseView baseView;*/
    public MutableLiveData<String > langset=new MutableLiveData<>("English");
    public ObservableField<String> EmailorPhone = new ObservableField<>("");
    public ObservableField<String> Countrycode = new ObservableField<>("+");
    public ObservableField<String> continueText = new ObservableField<>("Continue");
    public ObservableField<String> helperText = new ObservableField<>("Continue");
    public ObservableField<String> tittleText = new ObservableField<>("Your ride, on demand");
    public ObservableField<String> contentText = new ObservableField<>("Fastest way to book taxi without the hassle of waiting & haggling for price");
    public ObservableField<String> Tamil = new ObservableField<>("Tamil");
    public ObservableField<String> English = new ObservableField<>("English");
    public ObservableField<String> Arabic = new ObservableField<>("Arabic");

    public MutableLiveData<Integer> Selected = new MutableLiveData<>(1);
    public ObservableField<Integer> Select = new ObservableField<>(1);
    public MutableLiveData<MaterialTextView> materialTextView = new MutableLiveData<>();
    public MutableLiveData<Boolean> materialSelected = new MutableLiveData<>(true);

    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;


    @Inject
    public LoginOrSignViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                                SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;

    }


    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            if (response.data != null) {
                if (sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).equalsIgnoreCase("") || sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).isEmpty() || response.isUpdate_sheet()) {
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: equalsIgnoreCase =" + sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).equalsIgnoreCase(""));
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: isEmpty =" + sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).isEmpty());
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: isUpdate_sheet =" + response.isUpdate_sheet());
                    String getTranslations = CommonUtils.ObjectToString(response.data);
                    BaseResponse.DataObject dataObject = (BaseResponse.DataObject) CommonUtils.StringToObject(getTranslations, BaseResponse.DataObject.class);
                    response.saveLanguageTranslations(sharedPrefence, gson, dataObject);
                }
            }
           onGetLanguagesList();
        }
    }

    /**
     * Callback for failed API calls
     *
     * @param taskId ID of the API task
     * @param e      {@link CustomException}
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }


    public void onCLickLogin(View v) {

        getmNavigator().onClickLogin();
    }

    public void onClickSignup(View v) {
        getmNavigator().onClickSignup();
    }

    public void getLanguagees() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getTranslations();
        } else
            getmNavigator().showNetworkMessage();
    }

    public void onGetLanguagesList() {
        final List<String> items = new ArrayList<>();
        if (sharedPrefence.Getvalue(SharedPrefence.LANGUANGES) != null) {
            for (String key : gson.fromJson(sharedPrefence.Getvalue(SharedPrefence.LANGUANGES), String[].class))
                switch (key) {
                    case "en":
                        items.add("English");
                        break;
                    case "ar":
                        items.add("عربى");
                        break;
                    case "ta":
                        items.add("தமிழ்");
                        break;
                    case "es":
                        items.add("español");
                        break;
                    case "ja":
                        items.add("日本語");
                        break;
                    case "ko":
                        items.add("한국어");
                        break;
                    case "pt":
                        items.add("português");
                        break;
                    case "zh":
                        items.add("中国人");
                        break;
                    case "hi":
                        items.add("हिंदी");
                        break;
                }
        }

        Log.e("xxxLang"," total items in profile"+items);

        getmNavigator().languageslist(items);

    }

}
