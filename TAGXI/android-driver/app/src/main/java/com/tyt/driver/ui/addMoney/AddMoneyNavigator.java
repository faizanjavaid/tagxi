package com.tyt.driver.ui.addMoney;


import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface AddMoneyNavigator extends BaseView {

    boolean isNetworkConnected();

    void intiatePayment(String clientToken);

    void onClickBack();

    BaseActivity getBaseAct();

}
