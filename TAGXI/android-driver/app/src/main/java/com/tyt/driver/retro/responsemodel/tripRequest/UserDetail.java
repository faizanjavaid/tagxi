package com.tyt.driver.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class UserDetail {
    @SerializedName("data")
    @Expose
    UserDetailData userDetailData;

    public UserDetailData getUserDetailData() {
        return userDetailData;
    }

    public void setUserDetailData(UserDetailData userDetailData) {
        this.userDetailData = userDetailData;
    }
}
