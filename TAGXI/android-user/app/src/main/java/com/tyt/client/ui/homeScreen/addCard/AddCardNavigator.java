package com.tyt.client.ui.homeScreen.addCard;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface AddCardNavigator extends BaseView {
    BaseActivity getBaseAct();

    boolean isNetworkConnected();

    void intiatePayment(String clientToken);
}
