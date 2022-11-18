package com.tyt.driver.ui.otpscreen;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/9/17.
 */

public interface OTPNavigator extends BaseView {

    String getOpt();

    void FinishAct();

    void openHomeActivity();

    void startTimer(int resendtimer);

    void resendOtp();

    BaseActivity getBaseAct();

    void onClicOpenLogin();

    void openHomeAct();

    void openCarDetailsAct();

    void openSignUpActivity();
}
