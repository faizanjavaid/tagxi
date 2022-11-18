package com.tyt.driver.ui.acceptReject;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.tyt.driver.R;
import com.tyt.driver.databinding.AcceptRejectBinding;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.retro.responsemodel.MetaRequest;
import com.tyt.driver.retro.responsemodel.tripRequest.ReqData;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by root on 10/7/17.
 */

public class AcceptRejectViewModel extends BaseNetwork<BaseResponse, AcceptRejectNavigator> {

    public final static String VMTAG = "xxxAcceptrejectModel";

    @Inject
    HashMap<String, String> Map;

    SharedPrefence sharedPrefence;

    /*BaseView baseView;*/
    GitHubService gitHubService;
    GitHubCountryCode gitHubCountryCode;

    public GoogleMap googleMap;

    public ObservableField<String> counterText = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> rating = new ObservableField<>();
    public ObservableField<String> reqId = new ObservableField<>();
    public ObservableField<String> pickupAddress = new ObservableField<>();
    public ObservableField<String> tripStartTime = new ObservableField<>();

    ObservableField<Double> pickLat = new ObservableField<>();
    ObservableField<Double> pickLng = new ObservableField<>();
    public ObservableBoolean stopTimer = new ObservableBoolean(false);
    public ObservableBoolean isAcceptClicked = new ObservableBoolean(false);

    HashMap<String, String> map = new HashMap<>();

    public ObservableField<Integer> later = new ObservableField();

    @Inject
    public AcceptRejectViewModel(@Named(Constants.ourApp) GitHubService gitHubService,
                                 @Named(Constants.countryMap) GitHubCountryCode gitHubCountryCode,
                                 SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.gitHubService = gitHubService;
        this.gitHubCountryCode = gitHubCountryCode;
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
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
        Log.e(VMTAG,"onSuccessAPI called");
        if (response.success) {
            getmNavigator().finishAct();
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
        Log.e(VMTAG,"onFailureAPI called");
        setIsLoading(false);
        getmNavigator().showMessage(e.getMessage());
        isAcceptClicked.set(false);
        //getmNavigator().finishAct();
    }

    /**
     * Returns {@link HashMap} with query parameters to call APIs
     **/
    @Override
    public HashMap<String, String> getMap() {
        return Map;
    }


    public void setDetails(ReqData req, Context context, AcceptRejectBinding mBinding) {

        Log.e("xxxAccRej"," page called via ReqData");
        userName.set(req.getUserDetail().getUserDetailData().getName());
        reqId.set(req.getId());
        pickupAddress.set(req.getPickAddress());

        pickLat.set(req.getPickLat());
        pickLng.set(req.getPickLng());

        later.set(req.getIsLater());
        tripStartTime.set(req.getTripStartTime());

        if (req.getUserDetail().getUserDetailData().getProfilePicture() != null) {
            Glide.with(context).load(req.getUserDetail().getUserDetailData().getProfilePicture()).error(R.drawable.profile_place_holder).into(mBinding.userImg);
        }

        // googleMap.addM
    }


    public void setDetails(MetaRequest.metaReqData req, Context context, AcceptRejectBinding mBinding) {

        Log.e("xxxAccRej"," page called via MetaRequest.metaReqData");
        userName.set(req.getUserDetail().getUserDetailData().getName());
        reqId.set(req.getId());
        pickupAddress.set(req.getPickAddress());

        pickLat.set(req.getPickLat());
        pickLng.set(req.getPickLng());

        if (req.getUserDetail().getUserDetailData().getProfilePicture() != null) {
            Glide.with(context).load(req.getUserDetail().getUserDetailData().getProfilePicture()).error(R.drawable.profile_place_holder).into(mBinding.userImg);
        }
        // googleMap.addM
    }


    public void onClickAccept(View v) {
        stopTimer.set(true);
        AcceptApi();
    }

    public void onClickReject(View v) {
        stopTimer.set(true);
        RejectApi();
    }

    public void RejectApi() {
        if (getmNavigator().isNetworkConnected()) {
            Log.d("", "RejectApi: ");
            setIsLoading(true);
            isAcceptClicked.set(false);
            map.put(Constants.NetworkParameters.request_id, reqId.get());
            map.put(Constants.NetworkParameters.is_accept, "0");
            RequestRepondApi(map);
        }
    }

    public void AcceptApi() {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            isAcceptClicked.set(true);
            map.put(Constants.NetworkParameters.request_id, reqId.get());
            map.put(Constants.NetworkParameters.is_accept, "1");
            RequestRepondApi(map);
        }
    }
}
