package com.tyt.driver.ui.homeScreen.mapscrn;

import android.content.Context;


import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface MapScrnNavigator extends BaseView {

    BaseActivity getBaseAct();

    Context getAttachedcontext();

    void onClickCurrLocation();

    void onclickEarnings();

    void openSideMenu();
}
