package com.tyt.driver.ui.homeScreen;

import com.tyt.driver.retro.responsemodel.ProfileModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 10/11/17.
 */

public interface HomeNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickProfile();

    void onClickHome();

    void onClickHistory();

    void onClickOnOff();

    void StartUpdate();

    void openAcceptRejectAct(String req);

    void openMetaAcceptReject(ProfileModel metaRequest);

    void closeAcceptReject();

    void openTripFragment(ProfileModel onTripRequest);

    void openFeedbackFrag(ProfileModel model);

    void openDocumentPage();

    void openApprovalPage(String declinedReason);

    void openFromStart();

    void openHomeAct();

    void openMapFrag(String currentDate, String currencySymbol, double totalEarnigs);
}
