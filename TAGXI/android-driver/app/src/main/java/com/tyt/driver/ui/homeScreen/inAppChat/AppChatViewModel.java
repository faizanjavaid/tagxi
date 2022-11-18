package com.tyt.driver.ui.homeScreen.inAppChat;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.base.BaseNetwork;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.ui.homeScreen.inAppChat.chathistory.ChatHistory;
import com.tyt.driver.ui.homeScreen.inAppChat.chathistory.Datum;
import com.tyt.driver.ui.homeScreen.inAppChat.chatmodel.Chat;
import com.tyt.driver.utilz.Constants;
import com.tyt.driver.utilz.SharedPrefence;
import com.tyt.driver.utilz.SocketHelper;
import com.tyt.driver.utilz.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class AppChatViewModel extends BaseNetwork<BaseResponse, AppChatNavigator> implements SocketHelper.SocketListener{
    HashMap<String, String> hashMap = new HashMap<>();
    private static final String TAG = "AppChatViewModel";

    @Inject
    public AppChatViewModel(GitHubService gitHubService, SharedPrefence sharedPrefence, Gson gson) {
        super(gitHubService, sharedPrefence, gson);
        this.sharedPrefence = sharedPrefence;
        this.gson = gson;
        SocketHelper.init(sharedPrefence, this, TAG, true);
    }

    @Override
    public HashMap<String, String> getMap() {
        return null;
    }

    public void sendSmsClick(View view) {
        getmNavigator().onSmsChat();
    }

    @Override
    public void onSuccessfulApi(long taskId, BaseResponse response) {
        Log.d("--AppChatViewModelTAG", "onSuccessfulApi() called with: taskId = [" + taskId + "], response = [" + response.success + "]"+"["+response.message+"]"+" data ="+new Gson().toJson(response.data));
        if (response.success){
            getmNavigator().onSmsChatShow(response.success,response.message,response);

        }else {
           getmNavigator().showSnackBar("Internal Server Error");
        }
    }

    @Override
    public void onFailureApi(long taskId, CustomException e) {
        Log.d("--AppChatViewModelTAG", "onFailureApi() called with: taskId = [" + taskId + "], e = [" + e + "]");
    }

    public void SmsApiCall(String req_id,String msg,String time) {
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            hashMap.clear();
            hashMap.put(Constants.NetworkParameters.request_id, req_id);
            hashMap.put(Constants.NetworkParameters.message, msg);
            smsSendApi(hashMap);
        } else
            getmNavigator().showNetworkMessage();
    }

    public void SmsHistoryApiCall(){
        if (getmNavigator().isNetworkConnected()) {
            setIsLoading(true);
            smsHistoryApi(sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID));
        } else
            getmNavigator().showNetworkMessage();
    }


    @Override
    public void ReceivedChatStatus(String strJson) {
        Log.d("xxxxTAG", "ReceivedChatStatus() called with: toString = [" + strJson + "]");
        JsonElement json = JsonParser.parseString(strJson);
        if (json.isJsonObject()) {
           getmNavigator().onReceivedChatShow(json.getAsJsonObject());
        }
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
        Log.d("xxxAppCatModel", "onCreateRequest: ");
    }

    @Override
    public void RequestHandler(String toString) {

    }

    @Override
    public void updateTripDistance(double v) {

    }

    @Override
    public void ApprovalStatus(String toString) {

    }

}
