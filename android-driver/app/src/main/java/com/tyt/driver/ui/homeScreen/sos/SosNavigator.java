package com.tyt.driver.ui.homeScreen.sos;

import com.tyt.driver.retro.responsemodel.SOSModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface SosNavigator extends BaseView {
    boolean isNetworkConnected();

    void onClickBAck();

    void loadSOS(List<SOSModel> sosModels);

    void onClickDelete(SOSModel sosModel);

    void onClickCall(SOSModel sosModel);

    public BaseActivity getAttachedContext();
}
