package com.tyt.driver.ui.carDetails.carYear;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface VehYearNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickConfirm();

    void openHomeAct();

    void openRefferalAct();

    void openProfile();

    void onCLickBack();
}
