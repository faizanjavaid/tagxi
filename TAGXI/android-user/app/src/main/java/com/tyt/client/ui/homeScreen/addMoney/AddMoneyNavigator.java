package com.tyt.client.ui.homeScreen.addMoney;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */


public interface AddMoneyNavigator extends BaseView {
    BaseActivity getBaseAct();

    boolean isNetworkConnected();

    void intiatePayment(String clientToken);

    void onClickBack();
}
