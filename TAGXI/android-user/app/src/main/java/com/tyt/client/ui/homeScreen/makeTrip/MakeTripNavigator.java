package com.tyt.client.ui.homeScreen.makeTrip;

import android.content.Context;

import com.google.android.gms.maps.model.Marker;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.OffersResponseData;
import com.tyt.client.retro.responsemodel.Type;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface MakeTripNavigator extends BaseView {

    Context getBaseAct();

    Context getAttachedcontext();

    void onClickBackImg();

    void loadTypes(List<Type> typeList);

    void clickedTypes(Type type);

    void onClickNext(HashMap<String, String> driverData, HashMap<String, Marker> driverPin);

    void loadEta(EtaModel data);

    void setNoDrivers();

    void onClickPromAlert();

    void loadNewEta(List<EtaModel> etaModel);

    void onClickEtaItem(EtaModel etaModel);

    void onClickSchedule();

    void onCLickPayment();

    void onCLickPromo();

    void closeWaitingDialog(/*boolean goHome*/boolean b);

    void openWaitingDialog();

    void openHomePage();

    void loadOffersData(List<OffersResponseData> offersResponseData);

    void clickedOferItem(OffersResponseData offersResponseData);

    void onClickCloseBottom();

    void updateCarETA(String typeId, String eta);

    EtaModel getSelectedCar();

    void refreshAdapter(List<EtaModel> etaModels);

    void onCLickInfoButon(EtaModel etaModel);

    void setCarTypeName(String typeName);

    void removeclickedpromo(OffersResponseData offersResponseData);

    void closesheet();

    void opensearchsheet();

    void closesearchsheet();

    BaseActivity actbase();

    void removeFrag();
}
