package com.tyt.driver.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnTripRequest {

    @SerializedName("data")
    @Expose
    private TripRequestData data;

    public TripRequestData getData() {
        return data;
    }

    public void setData(TripRequestData data) {
        this.data = data;
    }

}
