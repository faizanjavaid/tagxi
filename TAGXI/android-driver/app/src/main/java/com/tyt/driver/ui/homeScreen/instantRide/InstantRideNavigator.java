package com.tyt.driver.ui.homeScreen.instantRide;

import android.content.Context;

import com.tyt.driver.retro.responsemodel.EtaModel;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

public interface InstantRideNavigator extends BaseView {

    void removeinsfrag();

    void loadetatypes(List<EtaModel> insrlist);

    void onclick_insr_eta_item(EtaModel etaModel);

    void movetoinstanttrip();

    BaseActivity getBaseAct();

    Context getAttachedcontext();

    void openplacesearchAPI();

    void currloc();

    void calletaAPI();

}
