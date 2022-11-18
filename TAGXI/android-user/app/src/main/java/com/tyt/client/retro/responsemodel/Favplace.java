package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.client.retro.base.BaseResponse;

public class Favplace extends BaseResponse {

    @SerializedName("placeId")
    @Expose
    public String placeId;
    @SerializedName("id")
    @Expose
    public Integer Favid;
    @SerializedName("nickName")
    @Expose
    public String nickName;
    @SerializedName("latitude")
    @Expose
    public Float latitude;
    @SerializedName("longitude")
    @Expose
    public Float longitude;

    public boolean IsFavTit;
    public boolean IsPlaceLayout;
    public String PlaceApiOGaddress;


}