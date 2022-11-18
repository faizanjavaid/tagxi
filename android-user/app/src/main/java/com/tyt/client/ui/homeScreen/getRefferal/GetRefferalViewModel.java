package com.tyt.client.ui.homeScreen.getRefferal;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.retro.responsemodel.RefferralModel;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by Mahi in 2021.
 */

public class GetRefferalViewModel extends BaseNetwork<BaseResponse, GetRefferalNavigator> {
    private static final String TAG = "GetRefferalViewModel";

    public ObservableField<String> referalCode = new ObservableField<>();

    public GetRefferalViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        String referalData = CommonUtils.ObjectToString(response.data);
        RefferralModel referalDataModel = (RefferralModel) CommonUtils.StringToObject(referalData, RefferralModel.class);
        referalCode.set(referalDataModel.getRefferalCode());
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * adds client_id, client_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void getRefferalAPi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            getRefferal();
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void onClickInvite(View v) {
        getmNavigator().onClickinvite();
    }

    public void onClickCopyImage(View v) {
        getmNavigator().onClickCopy();
    }
}

