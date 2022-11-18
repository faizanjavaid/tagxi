package com.tyt.driver.ui.homeScreen.profile;

import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

/**
 * Created by root on 11/13/17.
 */

public interface ProfileNavigator extends BaseView {
    BaseActivity getBaseAct();

    void openEditProfile();

    void opneChangePasswordAlert();

    void dismissDialog();

    void onClickManageCard();

    void logoutApi();

    void openFromStart();

    void onClickWallet();

    void openRefferalFrag();

    void onClickEarnings();

    void openDocumentpage();

    void onClickSOS();

    void onClickFAQ();

    void openVehicleInfoPage();

    void refreshScreen();

    void oncheckupdate();

    void onabout();

    void profileComplaintDialog();

    void onClickHistory();

}
