package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.client.retro.responsemodel.tripRequest.OnTripRequest;

public class ProfileModel {
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

    @SerializedName("map_key")
    @Expose
    private String MapKey;

    @SerializedName("show_rental_ride")
    @Expose
    private Boolean show_rental_ride;

    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("no_of_ratings")
    @Expose
    private Integer noOfRatings;
    @SerializedName("onTripRequest")
    @Expose
    private OnTripRequest onTripRequest;
    @SerializedName("metaRequest")
    @Expose
    private MetaRequest metaRequest;

    @SerializedName("favouriteLocations")
    @Expose
    private FavouriteLocations favouriteLoactions;

    @SerializedName("sos")
    @Expose
    private SOS sosPhone;

    public SOS getSosPhone() {
        return sosPhone;
    }

    public void setSosPhone(SOS sosPhone) {
        this.sosPhone = sosPhone;
    }

    public FavouriteLocations getFavouriteLoactions() {
        return favouriteLoactions;
    }

    public void setFavouriteLoactions(FavouriteLocations favouriteLoactions) {
        this.favouriteLoactions = favouriteLoactions;
    }

//    public Integer getId() {
//        return id;
//    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

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

    public float getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(Integer noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public OnTripRequest getOnTripRequest() {
        return onTripRequest;
    }

    public void setOnTripRequest(OnTripRequest onTripRequest) {
        this.onTripRequest = onTripRequest;
    }

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public void setMetaRequest(MetaRequest metaRequest) {
        this.metaRequest = metaRequest;
    }

    public Integer getId() {
        return id;
    }

    public String getMapKey() {
        return MapKey;
    }

    public Boolean getShow_rental_ride() {
        return show_rental_ride;
    }

    public void setShow_rental_ride(Boolean show_rental_ride) {
        this.show_rental_ride = show_rental_ride;
    }
}