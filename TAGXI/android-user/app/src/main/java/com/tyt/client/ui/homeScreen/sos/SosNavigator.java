package com.tyt.client.ui.homeScreen.sos;

import com.tyt.client.retro.responsemodel.SOSModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface SosNavigator extends BaseView {
    boolean isNetworkConnected();

    void onClickBAck();

    void loadSOS(List<SOSModel> sosModels);

    void onClickDelete(SOSModel sosModel);

    void onClickCall(SOSModel sosModel);

    public BaseActivity getAttachedContext();
}
