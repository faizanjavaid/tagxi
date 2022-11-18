package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeclinedStatusModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
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
    private Boolean active;
    @SerializedName("approve")
    @Expose
    private Boolean approve;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("uploaded_document")
    @Expose
    private Boolean uploadedDocument;
    @SerializedName("declined_reason")
    @Expose
    private String declinedReason;
    @SerializedName("service_location_id")
    @Expose
    private String serviceLocationId;
    @SerializedName("vehicle_type_id")
    @Expose
    private String vehicleTypeId;
    @SerializedName("vehicle_type_name")
    @Expose
    private String vehicleTypeName;
    @SerializedName("car_make")
    @Expose
    private Object carMake;
    @SerializedName("car_model")
    @Expose
    private Object carModel;
    @SerializedName("car_make_name")
    @Expose
    private Object carMakeName;
    @SerializedName("car_model_name")
    @Expose
    private Object carModelName;
    @SerializedName("car_color")
    @Expose
    private Object carColor;
    @SerializedName("driver_lat")
    @Expose
    private Object driverLat;
    @SerializedName("driver_lng")
    @Expose
    private Object driverLng;
    @SerializedName("car_number")
    @Expose
    private Object carNumber;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("no_of_ratings")
    @Expose
    private Integer noOfRatings;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("refferal_code")
    @Expose
    private String refferalCode;
    @SerializedName("map_key")
    @Expose
    private String mapKey;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;
    @SerializedName("total_earnings")
    @Expose
    private Integer totalEarnings;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getApprove() {
        return approve;
    }

    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getUploadedDocument() {
        return uploadedDocument;
    }

    public void setUploadedDocument(Boolean uploadedDocument) {
        this.uploadedDocument = uploadedDocument;
    }

    public String getDeclinedReason() {
        return declinedReason;
    }

    public void setDeclinedReason(String declinedReason) {
        this.declinedReason = declinedReason;
    }

    public String getServiceLocationId() {
        return serviceLocationId;
    }

    public void setServiceLocationId(String serviceLocationId) {
        this.serviceLocationId = serviceLocationId;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public Object getCarMake() {
        return carMake;
    }

    public void setCarMake(Object carMake) {
        this.carMake = carMake;
    }

    public Object getCarModel() {
        return carModel;
    }

    public void setCarModel(Object carModel) {
        this.carModel = carModel;
    }

    public Object getCarMakeName() {
        return carMakeName;
    }

    public void setCarMakeName(Object carMakeName) {
        this.carMakeName = carMakeName;
    }

    public Object getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(Object carModelName) {
        this.carModelName = carModelName;
    }

    public Object getCarColor() {
        return carColor;
    }

    public void setCarColor(Object carColor) {
        this.carColor = carColor;
    }

    public Object getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(Object driverLat) {
        this.driverLat = driverLat;
    }

    public Object getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(Object driverLng) {
        this.driverLng = driverLng;
    }

    public Object getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Object carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getRating() {
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getRefferalCode() {
        return refferalCode;
    }

    public void setRefferalCode(String refferalCode) {
        this.refferalCode = refferalCode;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Integer totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
}