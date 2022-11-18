package com.tyt.driver.ui.carDetails.carColor;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface CarColorNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickContiue();

    void onClickSelectedColor(String s);
}
