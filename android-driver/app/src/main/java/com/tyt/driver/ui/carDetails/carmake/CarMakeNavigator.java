package com.tyt.driver.ui.carDetails.carmake;

import com.tyt.driver.retro.responsemodel.CarMakeModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface CarMakeNavigator extends BaseView {

    BaseActivity getBaseAct();

    void loadCarMake(List<CarMakeModel> carMakeModels);

    void onClickSelectedMake(String name, int id);

    void onClickContiue();
}
