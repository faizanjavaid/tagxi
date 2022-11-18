package com.tyt.driver.ui.homeScreen.addCard;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface AddCardNavigator extends BaseView {
    BaseActivity getBaseAct();
    boolean isNetworkConnected();

    void intiatePayment(String clientToken);
}
