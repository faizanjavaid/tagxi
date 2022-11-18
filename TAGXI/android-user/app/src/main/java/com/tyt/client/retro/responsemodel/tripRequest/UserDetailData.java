package com.tyt.client.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailData {
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
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("no_of_ratings")
    @Expose
    private Integer noOfRatings;

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

    public double getRating() {
        return rating;
    }

    public Integer getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(Integer noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}