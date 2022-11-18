package com.tyt.driver.ui.homeScreen.earnings;


import com.tyt.driver.retro.responsemodel.WeeklyModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface EarningsNavigator extends BaseView {

    boolean isNetworkConnected();

    void onClickBack();

    void onClickToDate();

    void onClickStartDate();

    BaseActivity getBaseAct();

    void loadChartData(WeeklyModel response);

}
