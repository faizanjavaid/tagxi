package com.tyt.client.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_number")
    @Expose
    private String requestNumber;
    @SerializedName("is_later")
    @Expose
    private Object isLater;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("trip_start_time")
    @Expose
    private Object tripStartTime;
    @SerializedName("arrived_at")
    @Expose
    private Object arrivedAt;
    @SerializedName("accepted_at")
    @Expose
    private Object acceptedAt;
    @SerializedName("completed_at")
    @Expose
    private Object completedAt;
    @SerializedName("is_driver_started")
    @Expose
    private Object isDriverStarted;
    @SerializedName("is_driver_arrived")
    @Expose
    private Object isDriverArrived;
    @SerializedName("is_trip_start")
    @Expose
    private Object isTripStart;
    @SerializedName("total_distance")
    @Expose
    private Object totalDistance;
    @SerializedName("total_time")
    @Expose
    private Double totalTime;

    @SerializedName("is_completed")
    @Expose
    private Object isCompleted;
    @SerializedName("is_cancelled")
    @Expose
    private Object isCancelled;
    @SerializedName("cancel_method")
    @Expose
    private Object cancelMethod;
    @SerializedName("payment_opt")
    @Expose
    private String paymentOpt;
    @SerializedName("is_paid")
    @Expose
    private Object isPaid;
    @SerializedName("user_rated")
    @Expose
    private Object userRated;
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
    @SerializedName("requested_currency_code")
    @Expose
    private String requestedCurrencyCode;

    @SerializedName("vehicle_type_image")
    @Expose
    private String VehicleTypeImage;

    @SerializedName("car_make_name")
    @Expose
    private String car_make_name;

    @SerializedName("car_model_name")
    @Expose
    private String car_model_name;
    @SerializedName("driverDetail")
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

    public Object getIsLater() {
        return isLater;
    }

    public void setIsLater(Object isLater) {
        this.isLater = isLater;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Object tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Object getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(Object arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public Object getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Object acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public Object getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Object completedAt) {
        this.completedAt = completedAt;
    }

    public Object getIsDriverStarted() {
        return isDriverStarted;
    }

    public void setIsDriverStarted(Object isDriverStarted) {
        this.isDriverStarted = isDriverStarted;
    }

    public Object getIsDriverArrived() {
        return isDriverArrived;
    }

    public void setIsDriverArrived(Object isDriverArrived) {
        this.isDriverArrived = isDriverArrived;
    }

    public Object getIsTripStart() {
        return isTripStart;
    }

    public void setIsTripStart(Object isTripStart) {
        this.isTripStart = isTripStart;
    }

    public Object getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Object totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public Object getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Object isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Object getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Object isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Object getCancelMethod() {
        return cancelMethod;
    }

    public void setCancelMethod(Object cancelMethod) {
        this.cancelMethod = cancelMethod;
    }

    public String getPaymentOpt() {
        return paymentOpt;
    }

    public void setPaymentOpt(String paymentOpt) {
        this.paymentOpt = paymentOpt;
    }

    public Object getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Object isPaid) {
        this.isPaid = isPaid;
    }

    public Object getUserRated() {
        return userRated;
    }

    public void setUserRated(Object userRated) {
        this.userRated = userRated;
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

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public RequestBill getRequestBill() {
        return requestBill;
    }

    public String getVehicleTypeImage() {
        return VehicleTypeImage;
    }

    public void setVehicleTypeImage(String vehicleTypeImage) {
        VehicleTypeImage = vehicleTypeImage;
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

    public void setRequestBill(RequestBill requestBill) {
        this.requestBill = requestBill;
    }

    public Integer getDriverRated() {
        return driverRated;
    }


    public void setDriverRated(Integer driverRated) {
        this.driverRated = driverRated;
    }
}
