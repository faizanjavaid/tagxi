package com.tyt.driver.ui.addMoney;

import android.view.View;

import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.AddmoneyData;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

/**
 * Created by root on 11/13/17.
 */

public class AddMoneyViewModel extends BaseNetwork<BaseResponse, AddMoneyNavigator> {
    private static final String TAG = "AddCardViewModel";

    public ObservableField<String> cardIDD = new ObservableField<>();
    public ObservableField<String> currency = new ObservableField<>();
    public ObservableField<String> walletBal = new ObservableField<>();
    public ObservableField<String> moneyValue = new ObservableField<>("");

    public ObservableField<String> amnt_50 = new ObservableField<>();
    public ObservableField<String> amnt_40 = new ObservableField<>();
    public ObservableField<String> amnt_30 = new ObservableField<>();
    public ObservableField<String> amnt_20 = new ObservableField<>();
    public ObservableField<String> amnt_10 = new ObservableField<>();
    HashMap<String, String> map = new HashMap<>();

    public AddMoneyViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
    }


    /**
     * called when API call is successful
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.message.equalsIgnoreCase("money_added_successfully")) {
            AddmoneyData add = (AddmoneyData) CommonUtils.getSingleObject(new Gson().toJson(response.data), AddmoneyData.class);
            walletBal.set(add.getCurrencySymbol() + CommonUtils.doubleDecimalFromat(add.getAmountBalance()));
            getmNavigator().showMessage(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_money_add_success));
        }
    }

    /**
     * called when API call fails
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
    }

    /**
     * adds driver_id, driver_token to {@link HashMap} used for API calls
     **/
    @Override
    public HashMap<String, String> getMap() {
        return null;
    }


    public void onCLickBack(View v) {
        getmNavigator().onClickBack();
    }

    public void onClick10(View v) {
        moneyValue.set("10");
    }

    public void onClick20(View v) {
        moneyValue.set("20");
    }

    public void onClick30(View v) {
        moneyValue.set("30");
    }

    public void onClick40(View v) {
        moneyValue.set("40");
    }

    public void onClick50(View v) {
        moneyValue.set("50");
    }


    public void onClickSubmit(View v) {
        if (getmNavigator().isNetworkConnected()) {
            map.clear();
            map.put(Constants.NetworkParameters.cardId, cardIDD.get());
            map.put(Constants.NetworkParameters.Amount, moneyValue.get());
            AddMoneyApi(map);
        } else getmNavigator().showNetworkMessage();
    }

}
