package com.tyt.driver.ui.carDetails.carTypes;

import com.tyt.driver.retro.responsemodel.CarTypeModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface CarTypeNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickContiue();

    void loadTypes(List<CarTypeModel> carTypeModels);

    void onClickSelectedType(String name, String id);
}
