package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.driver.retro.responsemodel.tripRequest.OnTripRequest;

public class ProfileModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("map_key")
    @Expose
    private String mapKey;

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

    @SerializedName("current_date")
    @Expose
    private String CurrentDate;

    @SerializedName("total_earnings")
    @Expose
    private double TotalEarnigs;

    @SerializedName("currency_symbol")
    @Expose
    private String CurrencySymbol;

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
    private Integer carMake;
    @SerializedName("car_model")
    @Expose
    private Integer carModel;
    @SerializedName("car_make_name")
    @Expose
    private String carMakeName;
    @SerializedName("car_model_name")
    @Expose
    private String carModelName;
    @SerializedName("car_color")
    @Expose
    private String carColor;
    @SerializedName("car_number")
    @Expose
    private String carNumber;

    @SerializedName("onTripRequest")
    @Expose
    private OnTripRequest onTripRequest;


    @SerializedName("metaRequest")
    @Expose
    private MetaRequest metaRequest;

    @SerializedName("show_instant_ride")
    @Expose
    private Boolean show_instant_ride;

    @SerializedName("rating")
    @Expose
    private float rating;

    public Boolean getShow_instant_ride() {
        return show_instant_ride;
    }

    public void setShow_instant_ride(Boolean show_instant_ride) {
        this.show_instant_ride = show_instant_ride;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Integer getCarMake() {
        return carMake;
    }

    public void setCarMake(Integer carMake) {
        this.carMake = carMake;
    }

    public Integer getCarModel() {
        return carModel;
    }

    public void setCarModel(Integer carModel) {
        this.carModel = carModel;
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

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public OnTripRequest getOnTripRequest() {
        return onTripRequest;
    }

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public String getMapKey() {
        return mapKey;
    }

    public String getDeclinedReason() {
        return declinedReason;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }

    public double getTotalEarnigs() {
        return TotalEarnigs;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

}
