package com.tyt.driver.ui.historyDetail;


import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface HistoryDetailNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickBack();

    void openFromStart();

    void startshim();

    void stopshim();
}
