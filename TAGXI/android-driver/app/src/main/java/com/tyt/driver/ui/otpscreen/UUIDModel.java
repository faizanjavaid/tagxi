package com.tyt.driver.ui.otpscreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UUIDModel {

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
