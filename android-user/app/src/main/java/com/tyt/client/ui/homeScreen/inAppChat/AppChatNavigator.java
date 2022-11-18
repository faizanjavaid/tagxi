package com.tyt.client.ui.homeScreen.inAppChat;

import com.google.gson.JsonObject;
import com.tyt.client.retro.base.BaseResponse;
import com.tyt.client.ui.base.BaseActivity;
import com.tyt.client.ui.base.BaseView;


public interface AppChatNavigator extends BaseView {
    BaseActivity getBaseAct();

    void onSmsChat();

    void onSmsChatShow(boolean isSuccess, String successMsg, BaseResponse response);

    void onReceivedChatShow(JsonObject asJsonObject);
}
