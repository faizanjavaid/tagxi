package com.tyt.client.ui.homeScreen.userHistory;

import com.tyt.client.retro.responsemodel.HistoryCard;
import com.tyt.client.retro.responsemodel.HistoryModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface HistoryNavigator extends BaseView {
    BaseActivity getBaseAct();

    void loadHistory(List<HistoryCard> historyModels);

    void onClickItem(String id);

    void openFromStart();

    void startshim();

    void stopshim();

    void setPageValue(Integer num, Integer tot);

    void onCLickBack();
}
