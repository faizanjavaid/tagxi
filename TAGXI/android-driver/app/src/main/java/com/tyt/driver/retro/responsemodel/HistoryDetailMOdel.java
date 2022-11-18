package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.driver.retro.responsemodel.tripRequest.UserDetail;

public class HistoryDetailMOdel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_number")
    @Expose
    private String requestNumber;
    @SerializedName("is_later")
    @Expose
    private Integer isLater;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("trip_start_time")
    @Expose
    private String tripStartTime;
    @SerializedName("arrived_at")
    @Expose
    private String arrivedAt;
    @SerializedName("accepted_at")
    @Expose
    private String acceptedAt;
    @SerializedName("completed_at")
    @Expose
    private String completedAt;
    @SerializedName("is_driver_started")
    @Expose
    private Integer isDriverStarted;
    @SerializedName("is_driver_arrived")
    @Expose
    private Integer isDriverArrived;
    @SerializedName("is_trip_start")
    @Expose
    private Integer isTripStart;
    @SerializedName("total_distance")
    @Expose
    private Double totalDistance;
    @SerializedName("total_time")
    @Expose
    private Integer totalTime;
    @SerializedName("is_completed")
    @Expose
    private Integer isCompleted;
    @SerializedName("is_cancelled")
    @Expose
    private Integer isCancelled;
    @SerializedName("cancel_method")
    @Expose
    private String cancelMethod;
    @SerializedName("payment_opt")
    @Expose
    private String paymentOpt;
    @SerializedName("is_paid")
    @Expose
    private Integer isPaid;
    @SerializedName("user_rated")
    @Expose
    private Integer userRated;
    @SerializedName("driver_rated")
    @Expose
    private Integer driverRated;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("zone_type_id")
    @Expose
    private String zoneTypeId;
    @SerializedName("vehicle_type_name")
    @Expose
    private String vehicleTypeName;
    @SerializedName("vehicle_type_image")
    @Expose
    private String VehicleTypeImage;
    @SerializedName("pick_lat")
    @Expose
    private Double pickLat;
    @SerializedName("pick_lng")
    @Expose
    private Double pickLng;
    @SerializedName("drop_lat")
    @Expose
    private Double dropLat;
    @SerializedName("drop_lng")
    @Expose
    private Double dropLng;
    @SerializedName("pick_address")
    @Expose
    private String pickAddress;
    @SerializedName("drop_address")
    @Expose
    private String dropAddress;

    @SerializedName("car_make_name")
    @Expose
    private String car_make_name;

    @SerializedName("car_model_name")
    @Expose
    private String car_model_name;

    @SerializedName("requested_currency_code")
    @Expose
    private String requestedCurrencyCode;
    @SerializedName("requested_currency_symbol")
    @Expose
    private Object requestedCurrencySymbol;
    @SerializedName("driverDetail")
    @Expose
    private UserDetail driverDetail;

    @SerializedName("userDetail")
    @Expose
    private UserDetail userDetail;


    @SerializedName("requestBill")
    @Expose
    private RequestBill requestBill;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Integer getIsLater() {
        return isLater;
    }

    public void setIsLater(Integer isLater) {
        this.isLater = isLater;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public String getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(String arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getIsDriverStarted() {
        return isDriverStarted;
    }

    public void setIsDriverStarted(Integer isDriverStarted) {
        this.isDriverStarted = isDriverStarted;
    }

    public Integer getIsDriverArrived() {
        return isDriverArrived;
    }

    public void setIsDriverArrived(Integer isDriverArrived) {
        this.isDriverArrived = isDriverArrived;
    }

    public Integer getIsTripStart() {
        return isTripStart;
    }

    public void setIsTripStart(Integer isTripStart) {
        this.isTripStart = isTripStart;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getTotalTime() {

        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Integer getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Integer isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getCancelMethod() {
        return cancelMethod;
    }

    public void setCancelMethod(String cancelMethod) {
        this.cancelMethod = cancelMethod;
    }

    public String getPaymentOpt() {
        return paymentOpt;
    }

    public void setPaymentOpt(String paymentOpt) {
        this.paymentOpt = paymentOpt;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public Integer getUserRated() {
        return userRated;
    }

    public void setUserRated(Integer userRated) {
        this.userRated = userRated;
    }

    public Integer getDriverRated() {
        return driverRated;
    }

    public void setDriverRated(Integer driverRated) {
        this.driverRated = driverRated;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getZoneTypeId() {
        return zoneTypeId;
    }

    public void setZoneTypeId(String zoneTypeId) {
        this.zoneTypeId = zoneTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public Double getPickLat() {
        return pickLat;
    }

    public void setPickLat(Double pickLat) {
        this.pickLat = pickLat;
    }

    public Double getPickLng() {
        return pickLng;
    }

    public void setPickLng(Double pickLng) {
        this.pickLng = pickLng;
    }

    public Double getDropLat() {
        return dropLat;
    }

    public void setDropLat(Double dropLat) {
        this.dropLat = dropLat;
    }

    public Double getDropLng() {
        return dropLng;
    }

    public void setDropLng(Double dropLng) {
        this.dropLng = dropLng;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getRequestedCurrencyCode() {
        return requestedCurrencyCode;
    }

    public void setRequestedCurrencyCode(String requestedCurrencyCode) {
        this.requestedCurrencyCode = requestedCurrencyCode;
    }

    public Object getRequestedCurrencySymbol() {
        return requestedCurrencySymbol;
    }

    public void setRequestedCurrencySymbol(Object requestedCurrencySymbol) {
        this.requestedCurrencySymbol = requestedCurrencySymbol;
    }


    public RequestBill getRequestBill() {
        return requestBill;
    }

    public void setRequestBill(RequestBill requestBill) {
        this.requestBill = requestBill;
    }
    public String getVehicleTypeImage() {
        return VehicleTypeImage;
    }

    public void setVehicleTypeImage(String vehicleTypeImage) {
        VehicleTypeImage = vehicleTypeImage;
    }

    public UserDetail getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(UserDetail driverDetail) {
        this.driverDetail = driverDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getCar_make_name() {
        return car_make_name;
    }

    public void setCar_make_name(String car_make_name) {
        this.car_make_name = car_make_name;
    }

    public String getCar_model_name() {
        return car_model_name;
    }

    public void setCar_model_name(String car_model_name) {
        this.car_model_name = car_model_name;
    }
}
