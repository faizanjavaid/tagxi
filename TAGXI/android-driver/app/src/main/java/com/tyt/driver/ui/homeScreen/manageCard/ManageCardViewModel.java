package com.tyt.driver.ui.homeScreen.manageCard;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;

import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.CardListModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class ManageCardViewModel extends BaseNetwork<BaseResponse, ManageCardNavigator> {
    private static final String TAG = "AddCardViewModel";

    HashMap<String, String> map = new HashMap<>();

    List<CardListModel> cardListModels = new ArrayList<>();
    public ObservableBoolean noItemFound = new ObservableBoolean(false);

    public ManageCardViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {

            if (response.message.equalsIgnoreCase("card_listed_succesfully")) {
                cardListModels.clear();
                String cardString = CommonUtils.arrayToString((ArrayList<Object>) response.data);
                cardListModels.addAll(CommonUtils.stringToArray(cardString, CardListModel[].class));
                getmNavigator().loadManageCardList(cardListModels);
            } else if (response.message.equalsIgnoreCase("card_added_succesfully")) {
                manageCardApi();
            } else if (response.message.equalsIgnoreCase("card_deleted_succesfully"))
                manageCardApi();
            else if (response.message.equalsIgnoreCase("card_made_default_succesfully"))
                manageCardApi();
            else {
                String clientTokenString = CommonUtils.ObjectToString(response.data);
                BaseResponse.ClientToken clientToken = (BaseResponse.ClientToken) CommonUtils.StringToObject(clientTokenString, BaseResponse.ClientToken.class);
                Log.e("token==", "___" + clientToken.getClientToken());
                getmNavigator().intiatePayment(clientToken.getClientToken());
            }

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


    public void manageCardApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            ListCards();
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickAddCard(View v) {
        //getmNavigator().openAddcardFrag();
        setIsLoading(true);
        if (getmNavigator().isNetworkConnected()) {
            paymentClientToken();
        } else getmNavigator().showNetworkMessage();
    }

    public void AddCardApi(PaymentMethodNonce paymentMethodNonce) {
        if (getmNavigator().isNetworkConnected()) {
            map.clear();
            map.put(Constants.NetworkParameters.PayNonce, paymentMethodNonce.getNonce());
            map.put(Constants.NetworkParameters.LastNumber, paymentMethodNonce.getDescription().replace("ending in", ""));
            map.put(Constants.NetworkParameters.UserRole, paymentMethodNonce.getTypeLabel());
            AddCardDetails(map);
        } else getmNavigator().showNetworkMessage();

    }

    public void DeletCard(String id) {
        if (getmNavigator().isNetworkConnected()) {
            DeletCardApi(id);
        } else getmNavigator().showNetworkMessage();
    }

    public void onClickBackImg(View v) {
        getmNavigator().clickBack();
    }

    public void DefaultCardAPi(String id) {
        if (getmNavigator().isNetworkConnected()) {
            map.clear();
            map.put(Constants.NetworkParameters.cardId, id);
            DefaultCard(map);
        } else getmNavigator().showNetworkMessage();
    }
}
