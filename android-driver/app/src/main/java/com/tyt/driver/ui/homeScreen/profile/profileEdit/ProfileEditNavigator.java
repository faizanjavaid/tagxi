package com.tyt.driver.ui.homeScreen.profile.profileEdit;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface ProfileEditNavigator extends BaseView {
    BaseActivity getBaseAct();

    void chooseCountryCode();

    void openImageChooseAlert();

    void openHomeAct();

    void clickBack();
}
