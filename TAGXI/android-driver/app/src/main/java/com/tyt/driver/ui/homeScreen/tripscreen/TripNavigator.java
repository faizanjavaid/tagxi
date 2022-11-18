package com.tyt.driver.ui.homeScreen.tripscreen;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.tyt.driver.retro.responsemodel.CancelReasonModel;
import com.tyt.driver.retro.responsemodel.EndTripData;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

import java.util.List;

/**
 * Created by root on 12/21/17.
 */

public interface TripNavigator extends BaseView {

    BaseActivity getBaseAct();

    void openHomePage();

    void openBillFeedBack(EndTripData response);

    void cancelConfiremAlert(List<CancelReasonModel> reasonModels);

    void openEndtripConfiremAlert();

    void resetSlider();

    String getItemPosition();

    void selectedReason(boolean id);

    void openGoogleMap(double latitude, double longitude);
    void onOpenChatAndCall(String ph);
    void onCancel(View view);

    void getCurrLoc();

    String getaddressfromlatlng(LatLng latLng);

    void closerideotpdialog();

}
