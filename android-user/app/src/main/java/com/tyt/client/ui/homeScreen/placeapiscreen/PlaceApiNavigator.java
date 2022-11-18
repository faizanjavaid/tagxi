package com.tyt.client.ui.homeScreen.placeapiscreen;

import com.tyt.client.retro.responsemodel.Favplace;
import com.tyt.client.ui.base.BaseView;

import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface PlaceApiNavigator extends BaseView {
    void addList(List<Favplace> favplace);

    void showProgress(boolean show);

    void showclearButton(boolean show);

    void FinishAct();

    void clearText();
}
