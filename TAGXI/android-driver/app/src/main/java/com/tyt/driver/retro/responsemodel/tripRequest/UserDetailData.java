package com.tyt.driver.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.driver.retro.responsemodel.SOS;

public  class UserDetailData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("active")
    @Expose
    private Integer active;

    @SerializedName("vehicle_type_name")
    @Expose
    private String vehicleTypeName;

    @SerializedName("email_confirmed")
    @Expose
    private Integer emailConfirmed;
    @SerializedName("mobile_confirmed")
    @Expose
    private Integer mobileConfirmed;
    @SerializedName("last_known_ip")
    @Expose
    private String lastKnownIp;
    @SerializedName("last_login_at")
    @Expose
    private String lastLoginAt;
    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("sos")
    @Expose
    private SOS SosPhone;

    @SerializedName("onTripRequest")
    @Expose
    private OnTripRequest onTripRequest;

    @SerializedName("car_make_name")
    @Expose
    private String carMakeName;

    @SerializedName("car_model_name")
    @Expose
    private String carModelName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Integer emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public Integer getMobileConfirmed() {
        return mobileConfirmed;
    }

    public void setMobileConfirmed(Integer mobileConfirmed) {
        this.mobileConfirmed = mobileConfirmed;
    }

    public String getLastKnownIp() {
        return lastKnownIp;
    }

    public void setLastKnownIp(String lastKnownIp) {
        this.lastKnownIp = lastKnownIp;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public OnTripRequest getOnTripRequest() {
        return onTripRequest;
    }

    public void setOnTripRequest(OnTripRequest onTripRequest) {
        this.onTripRequest = onTripRequest;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getCarMakeName() {
        return carMakeName;
    }

    public void setCarMakeName(String carMakeName) {
        this.carMakeName = carMakeName;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public SOS getSosPhone() {
        return SosPhone;
    }

    public void setSosPhone(SOS sosPhone) {
        SosPhone = sosPhone;
    }
}
