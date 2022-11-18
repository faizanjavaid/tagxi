package com.tyt.client.ui.homeScreen.mapFrag;

import android.content.Context;


import com.google.android.gms.maps.model.Marker;
import com.tyt.client.retro.responsemodel.EtaModel;
import com.tyt.client.retro.responsemodel.FavouriteLocations;
import com.tyt.client.retro.responsemodel.RentalPackage;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahi in 2021.
 */

public interface MapFragNavigator extends BaseView {

    BaseActivity getBaseAct();

    Context getAttachedcontext();

    void onClickPickup();

    void onClickDrop();

    void onClickCurrLocation();

    void onClickConfirm(HashMap<String, String> driverDatas, HashMap<String, Marker> driverPins, String otype);

    void moveToMapPosition();

    void onClickLoadCalendar();

    void onclickdaily();

    void onclickrental();

    void onclickoutstation();

    void onclickuparrow();

    void onclicksearchdes();

    void BackClickPickDrop();

    void openDropScrn();

    void openhomemap();

    void closeFavDialog();

    void clearpicket();

    void cleardropet();

    void onClickPickFavItem(FavouriteLocations.FavLocData favLocData);

    void onClickDropFavItem(FavouriteLocations.FavLocData favLocData);

    void clikedcurrloc();

    void clickedlocateonmap();

    void clickedenterdeslater();

    void showMyselfDialogv(String opentype);

    void callprofileapi();

    void onclickrentalitem(RentalPackage rentalPackage);

    void loadrentalpacks(List<RentalPackage> rentalPackageslist);

    void onClickEtaItemR(EtaModel rentalPackage);

    void onClickInfoButtonR(EtaModel rentalPackage);

    void refreshAdapter(List<EtaModel> etaModels);

    EtaModel getSelectedCar();

    void updateCarETA(String typeId, String eta);

    void openwaitingDialogAct(String id);

    void open_no_serv_dialog();

    void openFavDialog();

    void loadfavdata(List<FavouriteLocations.FavLocData> favResData);

}
