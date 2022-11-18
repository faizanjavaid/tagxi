package com.tyt.driver.ui.carDetails.servicelocation;

import com.tyt.driver.retro.responsemodel.ServiceLocationModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface ServiceLocationNavigator extends BaseView {

    BaseActivity getBaseAct();


    void onClickContiue();

    void loadServiceLocation(List<ServiceLocationModel> serviceLocationModels);

    void onClickSelectArea(String name, String id);
}
