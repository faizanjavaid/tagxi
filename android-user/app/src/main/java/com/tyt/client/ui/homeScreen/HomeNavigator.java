package com.tyt.client.ui.homeScreen;

import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.ProfileModel;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 10/11/17.
 */

public interface HomeNavigator extends BaseView {

    BaseActivity getBaseAct();

    void onClickProfile();

    void onClickHome();

    void onClickHistory();

    void closeWaitingDialog(boolean b);

    void openTripFragment(ProfileModel model);

    void openFeedbackFrag(ProfileModel model);

    void openWaitingDialog(String id);

    void openFromStart();

    void openHomeFrag();

    void onclickdaily();

    void onclickrental();

    void onclickoutstation();

    void onclickuparrow();

    void onclicksearchdes();

    void loadfavouritesdata(List<FavouriteLocations.FavLocData> favData);
}
