package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Driver implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("latitude")
    @Expose
    public Float latitude;
    @SerializedName("longitude")
    @Expose
    public Float longitude;
    @SerializedName("car_model")
    @Expose
    public String carmodel;
    @SerializedName("car_number")
    @Expose
    public String carnumber;
    @SerializedName("firstname")
    @Expose
    public String firstname;
    @SerializedName("lastname")
    @Expose
    public String lastname;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("review")
    @Expose
    public float review;
    @SerializedName("car_colour")
    @Expose
    public String car_colour;


}