package com.tyt.driver.ui.homeScreen.userHistory;

import com.tyt.driver.retro.responsemodel.HistoryCard;
import com.tyt.driver.retro.responsemodel.HistoryModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface HistoryNavigator extends BaseView {
    BaseActivity getBaseAct();

    void loadHistory(List<HistoryCard> historyModels);

    void setPageValue(Integer num,Integer tot);

    void onClickItem(String id);

    void openFromStart();

    void startshim();

    void stopshim();


}
