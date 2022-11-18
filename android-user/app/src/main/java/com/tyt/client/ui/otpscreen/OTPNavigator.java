package com.tyt.client.ui.otpscreen;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface OTPNavigator extends BaseView{

    String getOpt();
    void openSinupuActivity();
    void FinishAct();

    void openHomeActivity();

    void startTimer(int resendtimer);

    void resendOtp();
    BaseActivity getBaseAct();

    void onClicOpenLogin();

    void openHomeAct();

    void openSignUpActivity();
}
