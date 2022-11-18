package com.tyt.driver.ui.signup;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface SignupNavigator extends BaseView {

    void openOtpActivity(String s, String removeFirstZeros);

    BaseActivity getBaseAct();

    void onClickOpenLogin();

    void onClickBack();

    void openCountryDialog();

    void opentcFrag();

    void opencarDetailsAct();
    void onGetPhoneNum();
}
