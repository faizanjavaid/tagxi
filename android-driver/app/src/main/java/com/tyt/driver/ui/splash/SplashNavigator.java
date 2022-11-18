package com.tyt.driver.ui.splash;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/11/17.
 */

public interface SplashNavigator extends BaseView{
    BaseActivity getAttachedContext();
    void startRequestingPermissions();
}
