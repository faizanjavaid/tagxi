package com.tyt.client.ui.loginOrSign;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubCountryCode;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.Constants;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mahi in 2021.
 */

public class LoginOrSignViewModel extends BaseNetwork<BaseResponse, LoginOrSignNavigator> {

    private static final String TAG ="LoginOrSignViewModel" ;
    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;

    public ObservableField<String> helperText = new ObservableField<>("Select Your Language");

    public ObservableField<String> tittleText = new ObservableField<>("Your ride, on demand");
    public ObservableField<String> contentText = new ObservableField<>("Fastest way to book taxi without the hassle of waiting & haggling for price");
    public ObservableField<String> continueText = new ObservableField<>("Continue");

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
