package com.tyt.client.ui.homeScreen.feedback;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface FeedbackNavigator extends BaseView {
    BaseActivity getBaseAct();

    boolean isNetworkConnected();

    void openHomePage();
}
