package com.tyt.driver.ui.homeScreen.inAppChat;

import com.google.gson.JsonObject;
import com.tyt.driver.retro.base.BaseResponse;
import com.tyt.driver.ui.base.BaseActivity;
import com.tyt.driver.ui.base.BaseView;

public interface AppChatNavigator extends BaseView {
     BaseActivity getBaseAct();
     void onSmsChat();
     void onSmsChatShow(boolean isSuccess, String successMsg, BaseResponse response);
     void onReceivedChatShow(JsonObject asJsonObject);
}
