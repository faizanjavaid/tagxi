package com.tyt.driver.ui.carDetails.carmodel;

import com.tyt.driver.retro.responsemodel.CarModelData;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface CarModelNavigator extends BaseView {

    BaseActivity getBaseAct();

    void loadCarMake(List<CarModelData> carMakeModels);

    void onClickContiue();

    void onClickSelectedModel(String name, int id);

    void onCLickBack();
}
