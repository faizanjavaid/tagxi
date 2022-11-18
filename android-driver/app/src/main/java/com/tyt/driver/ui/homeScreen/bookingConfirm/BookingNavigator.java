package com.tyt.driver.ui.homeScreen.bookingConfirm;

import android.content.Context;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface BookingNavigator extends BaseView {

    BaseActivity getBaseAct();

    Context getAttachedcontext();


    void onClickBack();

    void openWaitingDialog();

    void onClickChoose();
}
