package com.tyt.client.ui.splash;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface SplashNavigator extends BaseView {
    BaseActivity getAttachedContext();

    void startRequestingPermissions();
}
