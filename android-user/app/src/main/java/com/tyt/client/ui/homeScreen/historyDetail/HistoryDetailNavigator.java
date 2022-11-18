package com.tyt.client.ui.homeScreen.historyDetail;

import android.view.View;

import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 *
 */

public interface HistoryDetailNavigator extends BaseView {
    BaseActivity getBaseAct();

    void onClickBack();

    void loadCancelAlert(List<CancelReasonModel> reasonModels);

    void selectedReason(boolean equalsIgnoreCase);

    String getItemPosition();

    View getdriverimg();

    View getCarImg();

    void startshim();

    void stopshim();
}
