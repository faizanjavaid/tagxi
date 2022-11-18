package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 1/4/18.
 */

public class history {

    @SerializedName("request_id")
    @Expose
    public String requestId;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
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
    @SerializedName("trip_start_time")
    @Expose
    public String tripStartTime;
    @SerializedName("is_completed")
    @Expose
    public Integer isCompleted;
    @SerializedName("is_cancelled")
    @Expose
    public Integer isCancelled;

    @SerializedName("cancel_method")
    @Expose
    public Integer cancel_method;



    @SerializedName("later")
    @Expose
    public Integer later;
    @SerializedName("driver_id")
    @Expose
    public Integer driverId;
    @SerializedName("car_model")
    @Expose
    public String carModel;
    @SerializedName("car_number")
    @Expose
    public String carNumber;
    @SerializedName("driver_type")
    @Expose
    public Integer driverType;
    @SerializedName("driver_profile_pic")
    @Expose
    public String driverProfilePic;
    @SerializedName("type_icon")
    @Expose
    public String typeIcon;
    @SerializedName("type_name")
    @Expose
    public String typename;
    @SerializedName("total")
    @Expose
    public Float total;
    @SerializedName("currency")
    @Expose
    public String currency;
//    @Expose
//    public String currency;
@SerializedName("is_share")
public int is_share=0;
}
