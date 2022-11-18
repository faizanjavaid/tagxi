package com.tyt.client.ui.homeScreen.profile;

import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

/**
 * Created by Mahi in 2021.
 */

public interface ProfileNavigator extends BaseView {
    BaseActivity getBaseAct();

    void openEditProfile();

    void opneChangePasswordAlert();

    void onClickManageCard();

    void onClickHistory();

    void logoutApi();

    void openFromStart();

    void onClickWallet();

    void onClickRefferal();

    void onClickSOS();

    void onClickFAQ();

    void refreshScreen();

    void onCLickBack();

    void oncheckupdate();

    void onabout();

    void onfav();

    void profileComplaintDialog();
}
