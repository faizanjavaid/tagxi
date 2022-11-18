package com.tyt.driver.ui.wallet;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.WalletHistoryModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public class WalletFragViewModel extends BaseNetwork<BaseResponse, WalletNavigator> {
    private static final String TAG = "AddCardViewModel";

    public ObservableField<String> feedBack = new ObservableField<>("");

    public ObservableField<String> WalletBalance = new ObservableField<>();
    public ObservableField<String> walletCurrency = new ObservableField<>();

    public ObservableField<String> DefaultCardId = new ObservableField<>();

    public ObservableFloat userReview = new ObservableFloat();
    HashMap<String, String> hashMap = new HashMap<>();

    WalletHistoryModel walletHistoryModel;
    List<WalletHistoryModel.WalletHistoryData> walletHistoryData = new ArrayList<>();

    public WalletFragViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.message.equalsIgnoreCase("rated_successfully")) {
            getmNavigator().openHomePage();
        } else if (response.message.equalsIgnoreCase("wallet_history_listed")) {
            walletHistoryData.clear();
            walletHistoryModel = response.walletHistory;
            walletHistoryData.addAll(walletHistoryModel.getData());
            if (response.DefaultCardID != null)
                DefaultCardId.set(response.DefaultCardID);
            walletCurrency.set(response.currencySymbol);
            WalletBalance.set(walletCurrency.get() + CommonUtils.doubleDecimalFromat(response.walletBalance));

            if (walletHistoryData.size() > 0) {
                getmNavigator().loadWalletHistoryData(walletHistoryData, response.currencySymbol);
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
     * adds driver_id, driver_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void walletHistoryApi() {
        if (getmNavigator().isNetworkConnected()) {
            WalletHIstory();
        } else getmNavigator().showNetworkMessage();
    }

    public void onCLickBack(View v) {
        getmNavigator().onCLickBack();
    }

    public void onCLickAddMoney(View v) {
        if (!TextUtils.isEmpty(DefaultCardId.get()))
            getmNavigator().onClickAddMoney();
        else getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_add_one_card));
    }

    public void addCard(View v) {
        getmNavigator().onCLickAddCard();
    }
}
