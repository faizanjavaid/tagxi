package com.tyt.driver.ui.feedback;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface FeedbackNavigator extends BaseView {

    boolean isNetworkConnected();

    void openHomePage();

    BaseActivity getBaseAct();

}
