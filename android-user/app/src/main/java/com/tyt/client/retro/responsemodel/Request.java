package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Request implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("request_id")
    @Expose
    public String request_id;
    @SerializedName("distance")
    @Expose
    public double distance;
    @SerializedName("time")
    @Expose
    public Float time;
    @SerializedName("trip_start_time")
    @Expose
    public String tripStartTime;
    @SerializedName("is_driver_started")
    @Expose
    public Integer isDriverStarted;
    @SerializedName("is_driver_arrived")
    @Expose
    public Integer isDriverArrived;
    @SerializedName("is_trip_start")
    @Expose
    public Integer isTripStart;
    @SerializedName("is_completed")
    @Expose
    public Integer isCompleted;
    @SerializedName("later")
    @Expose
    public Integer later;
    @SerializedName("is_cancelled")
    @Expose
    public Integer isCancelled;
    @SerializedName("payment_opt")
    @Expose
    public Integer paymentOpt;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("pick_latitude")
    @Expose
    public Float pickLatitude;
    @SerializedName("pick_longitude")
    @Expose
    public Float pickLongitude;
    @SerializedName("drop_latitude")
    @Expose
    public Float dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    public Float dropLongitude;
    @SerializedName("pick_location")
    @Expose
    public String pickLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
    @SerializedName("driver")
    @Expose
    public Driver driver;
    @SerializedName("bill")
    @Expose
    public Bill bill;
    @SerializedName("type_icon")
    @Expose
    public String typeIcon;
    @SerializedName("type_name ")
    @Expose
    public String typename;
    @SerializedName("enable_dispute_button")
    @Expose
    public boolean enable_dispute_button;
    @Expose
    public int promo_used;
    @SerializedName("corporate")
    @Expose
    public int isCorporate = 0;
    @SerializedName("is_share")
    public int is_share;
    @SerializedName("request_otp")
    public String request_otp;
    @SerializedName("waiting_grace_time")
    public int waiting_grace_time;
    @SerializedName("duration")
    public String txt_duration;

}