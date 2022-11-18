package com.tyt.client.ui.homeScreen.getRefferal;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface GetRefferalNavigator extends BaseView {
    BaseActivity getBaseAct();

    boolean isNetworkConnected();

    void onClickBack();

    void onClickinvite();

    void onClickCopy();
}
