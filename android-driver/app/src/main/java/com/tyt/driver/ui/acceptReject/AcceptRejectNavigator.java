package com.tyt.driver.ui.acceptReject;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/7/17.
 */

public interface AcceptRejectNavigator extends BaseView {

    BaseActivity getBaseAct();

    void finishAct();
}
