package com.tyt.driver.ui.approval;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.DeclinedStatusModel;
import com.tyt.driver.utilz.CommonUtils;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class ApprovalViewModel extends BaseNetwork<BaseResponse, ApprovalNavigator> implements SocketHelper.SocketListener {

    private static final String TAG = "ApprovalViewModel";

    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;

    /*BaseView baseView;*/

    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<String> reasonss = new ObservableField<>("");
    public ObservableBoolean showdocsbutton = new ObservableBoolean(true);
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    @Inject
    public ApprovalViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                             @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                             SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;

        SocketHelper.init(sharedPrefence, this, TAG, false);
    }

    /**
     * Callback for successful API calls
     *
     * @param taskId   ID of the API task
     * @param response {@link BaseResponse} model
     **/
    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        setIsLoading(false);
        if (response.success) {
            getmNavigator().openHomeAct();
        }
    }

    /**
     * Callback for failed API calls
     *
     * @param taskId ID of the API task
     * @param e      {@link CustomException}
     **/
    @Override
    public void onFailureApi(long taskId, CustomException e) {
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }


    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void OnConnect() {

    }

    @Override
    public void OnDisconnect() {

    }

    @Override
    public void OnConnectError() {

    }

    @Override
    public void onCreateRequest(String s) {
        Log.d("xxxAprovalModel", "onCreateRequest: ");
    }

    @Override
    public void RequestHandler(String toString) {

    }

    @Override
    public void updateTripDistance(double v) {

    }

    @Override
    public void ApprovalStatus(String toString) {
        Log.e("approvalString--", "string---" + toString);
        BaseResponse response = CommonUtils.getSingleObject(toString, BaseResponse.class);
        Log.e("sucessMsg--", "msg---" + response.successMessage);

        if (response.successMessage.equalsIgnoreCase("driver_account_approved")) {
            getmNavigator().openHomeAct();
        } else {
            DeclinedStatusModel declinedStatusModel = CommonUtils.getSingleObject(new Gson().toJson(response.data), DeclinedStatusModel.class);
            if (declinedStatusModel.getDeclinedReason() != null)
                reasonss.set(declinedStatusModel.getDeclinedReason());
            else reasonss.set("Declined");
            title.set(getmNavigator().getBaseAct().getTranslatedString(R.string.txt_doc_declined));
            showdocsbutton.set(true);
        }

    }

    @Override
    public void ReceivedChatStatus(String toString) {

    }

    public void onClickMoveDocument(View v) {
        getmNavigator().openDocumentUpload();
    }

}
