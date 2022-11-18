package com.tyt.driver.ui.homeScreen.vehicleInfo;


import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface VehicleInfoNavigator extends BaseView {
    boolean isNetworkConnected();

    void onClickBack();

    void onClickEdit();
}
