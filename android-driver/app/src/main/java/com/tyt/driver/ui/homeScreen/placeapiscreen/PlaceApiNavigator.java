package com.tyt.driver.ui.homeScreen.placeapiscreen;

import com.tyt.driver.retro.responsemodel.Favplace;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/30/17.
 */

public interface PlaceApiNavigator extends BaseView {
    void addList(List<Favplace> favplace);
    void showProgress(boolean show);
    void showclearButton(boolean show);
    void FinishAct();


    void clearText();
}
