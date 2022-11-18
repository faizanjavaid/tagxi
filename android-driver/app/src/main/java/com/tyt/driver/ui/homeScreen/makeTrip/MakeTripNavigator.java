package com.tyt.driver.ui.homeScreen.makeTrip;

import android.content.Context;

import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.retro.responsemodel.Type;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 11/13/17.
 */

public interface MakeTripNavigator extends BaseView {

    BaseActivity getBaseAct();

    Context getAttachedcontext();

    void onClickBackImg();

    void loadTypes(List<Type> typeList);

    void clickedTypes(Type type);

    void onClickNext();

    void loadEta(EtaModel data);
}
