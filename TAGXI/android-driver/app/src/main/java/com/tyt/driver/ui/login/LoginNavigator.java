package com.tyt.driver.ui.login;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface LoginNavigator extends BaseView {

    void openOtpActivity(boolean isLogin);
    void HideKeyBoard();
    String getCountryCode();
    String getCountryShortName();
    void setCountryCode(String flat);

    void openOtpPage(boolean b, String s);
    BaseActivity getBaseAct();

    void onClickSignup();

    void onClickBack();

    void openHomeAct();

    boolean getcheckedvalue();

    void opentcfrag();

    void onClickCountryChoose();
}
