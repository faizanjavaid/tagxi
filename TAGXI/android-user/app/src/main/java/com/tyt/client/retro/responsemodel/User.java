package com.tyt.client.retro.responsemodel;

import com.tyt.client.retro.base.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends BaseResponse  {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("firstname")
    @Expose
    public String firstname;
    @SerializedName("lastname")
    @Expose
    public String lastname;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("login_by")
    @Expose
    public String loginBy;
    @SerializedName("login_method")
    @Expose
    public String loginMethod;
    @SerializedName("profile_pic")
    @Expose
    public String profilepic;

    @SerializedName("overall_rating")
    @Expose
    public float ratings;

    @SerializedName("is_presented")
    @Expose
    public Boolean Ispresented;



}