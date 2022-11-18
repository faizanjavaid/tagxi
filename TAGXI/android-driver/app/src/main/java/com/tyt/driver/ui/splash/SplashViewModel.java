package com.tyt.driver.ui.splash;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.exception.CustomException;
import com.tyt.driver.utilz.SharedPrefence;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/11/17.
 */

public class SplashViewModel extends BaseNetwork<BaseResponse, SplashNavigator> {
    SharedPrefence sharedPrefence;
    Gson gson;
    public ObservableBoolean isLoaad = new ObservableBoolean(false);

    /*public ObservableField<String> alertmessage = new ObservableField<>("");
    public ObservableField<String> alerttitle = new ObservableField<>("");
    public ObservableField<String> alertok = new ObservableField<>("");
    public ObservableField<String> alertcancel = new ObservableField<>("");*/

    @Inject
    public SplashViewModel(@Named(Constants.ourApp) GitHubService gitHubService
            , SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
    }

    /**
     * @param taskId
     * @param response holds the response getting the language.
     */
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        // setIsLoading(false);
        isLoaad.set(false);
        if (response.success) {
            if (response.data != null) {
                if (sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).equalsIgnoreCase("") || sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).isEmpty() || response.isUpdate_sheet()) {
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: equalsIgnoreCase ="+sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).equalsIgnoreCase(""));
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: isEmpty ="+sharedPrefence.Getvalue(sharedPrefence.Getvalue(SharedPrefence.LANGUANGE)).isEmpty());
                    Log.d("xxxSplashScreenModel", "onSuccessfulApi: isUpdate_sheet ="+response.isUpdate_sheet());
                    String getTranslations = CommonUtils.ObjectToString(response.data);
                    BaseResponse.DataObject dataObject = (BaseResponse.DataObject) CommonUtils.StringToObject(getTranslations, BaseResponse.DataObject.class);
                    response.saveLanguageTranslations(sharedPrefence, gson, dataObject);
                }
            }
            getmNavigator().startRequestingPermissions();
        }
    }

    /**
     * @param taskId
     * @param e      holds the exception messages from Api.
     */
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        //setIsLoading(false);
        isLoaad.set(false);
        getmNavigator().showMessage(e.getMessage());
        getmNavigator().startRequestingPermissions();
    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    /**
     * Api call for getting the languages.
     */
   /* public void getLanguagees() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            isLoaad.set(true);
            getTranslations();
        } else
            getmNavigator().showNetworkMessage();
    }*/
}
