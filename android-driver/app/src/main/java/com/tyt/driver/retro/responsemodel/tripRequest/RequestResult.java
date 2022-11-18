package com.tyt.driver.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestResult {

    @SerializedName("data")
    @Expose
    private ReqData data;

    public ReqData getData() {
        return data;
    }

    public void setData(ReqData data) {
        this.data = data;
    }

}
