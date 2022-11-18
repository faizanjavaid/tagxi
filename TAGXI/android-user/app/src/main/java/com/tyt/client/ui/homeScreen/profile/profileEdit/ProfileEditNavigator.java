package com.tyt.client.ui.homeScreen.profile.profileEdit;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface ProfileEditNavigator extends BaseView {
    BaseActivity getBaseAct();

    void chooseCountryCode();

    void openImageChooseAlert();

    void openHomeAct();

    void clickBack();
}
