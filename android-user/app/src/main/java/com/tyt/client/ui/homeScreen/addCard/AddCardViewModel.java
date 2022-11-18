package com.tyt.client.ui.homeScreen.addCard;

import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.client.retro.GitHubService;
import com.tyt.client.retro.base.BaseNetwork;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.utilz.CommonUtils;
import com.tyt.client.utilz.SharedPrefence;
import com.tyt.client.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by Mahi in 2021.
 */

public class AddCardViewModel extends BaseNetwork<BaseResponse, AddCardNavigator> {
    private static final String TAG = "AddCardViewModel";

    ObservableField<String> cardName = new ObservableField<>();
    ObservableField<String> cardNumber = new ObservableField<>();
    ObservableField<String> cardMonth = new ObservableField<>();
    ObservableField<String> cardYear = new ObservableField<>();
    ObservableField<String> cardCvv = new ObservableField<>();

    public AddCardViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            String clientTokenString = CommonUtils.ObjectToString(response.data);
            BaseResponse.ClientToken clientToken = (BaseResponse.ClientToken) CommonUtils.StringToObject(clientTokenString, BaseResponse.ClientToken.class);

            Log.e("token==", "___" + clientToken.getClientToken());

            getmNavigator().intiatePayment(clientToken.getClientToken());
        }
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


    public void AddCard() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
        } else getmNavigator().showNetworkMessage();
    }

    public void onCardNumberChanged(Editable e) {
        cardNumber.set(e.toString());
    }

    public void onCardNameChanged(Editable e) {
        cardName.set(e.toString());
    }

    public void onMonthChangedd(Editable e) {
        cardMonth.set(e.toString());
    }

    public void onCardYearChanged(Editable e) {
        cardYear.set(e.toString());
    }

    public void onCardCvvChanged(Editable e) {
        cardCvv.set(e.toString());
    }

    public void onClickCardAdd(View v) {
        /*if (getmNavigator().isNetworkConnected()) {
            paymentClientToken();
        } else getmNavigator().showNetworkMessage();*/
    }

}
