package com.tyt.client.ui.signup;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface SignupNavigator extends BaseView {

    void openOtpActivity(String s, String removeFirstZeros);

    BaseActivity getBaseAct();

    void onClickOpenLogin();

    void onClickBack();

    void openCountryDialog();

    void openHomAct();

    void onGetPhoneNum();

}
