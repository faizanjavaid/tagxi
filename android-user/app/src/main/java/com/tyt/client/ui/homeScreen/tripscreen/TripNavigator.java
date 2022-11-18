package com.tyt.client.ui.homeScreen.tripscreen;

import com.tyt.client.retro.responsemodel.CancelReasonModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface TripNavigator extends BaseView {

    BaseActivity getbaseAct();

    void openHomePage();

    void openFeedback(String s);

    void openCarInfoDialog();

    void cancelConfiremAlert(List<CancelReasonModel> reasonModels);

    void selectedReason(boolean id);

    void onClickShare(String s);

    String getItemPosition();

    void onOpenChatAndCall(String ph);

    void onTaskDone(Object... values);


}
