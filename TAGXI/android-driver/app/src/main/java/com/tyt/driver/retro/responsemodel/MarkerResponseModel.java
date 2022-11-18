package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MarkerResponseModel implements Serializable {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("is_driver_arrived")
    @Expose
    private Integer isDriverArrived;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getIsDriverArrived() {
        return isDriverArrived;
    }

    public void setIsDriverArrived(Integer isDriverArrived) {
        this.isDriverArrived = isDriverArrived;
    }

}
