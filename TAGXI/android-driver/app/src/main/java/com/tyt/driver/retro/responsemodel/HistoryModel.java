package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryModel {
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
    private Object arrivedAt;
    @SerializedName("accepted_at")
    @Expose
    private Object acceptedAt;
    @SerializedName("completed_at")
    @Expose
    private String completedAt;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("is_driver_started")
    @Expose
    private Integer isDriverStarted;
    @SerializedName("is_driver_arrived")
    @Expose
    private Integer isDriverArrived;
    @SerializedName("is_trip_start")
    @Expose
    private Integer isTripStart;
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

    @SerializedName("driverDetail")
    @Expose
    private DriverDetail driverDetail;

    @SerializedName("requestBill")
    private RequestBill requestBill;

    public static class DriverDetail {

        @SerializedName("data")
        @Expose
        private Driverdata driverdata;

        public Driverdata getDriverdata() {
            return driverdata;
        }

        public void setDriverdata(Driverdata driverdata) {
            this.driverdata = driverdata;
        }
    }

    public static class Driverdata{

        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("mobile")
        @Expose
        public String mobile;

        @SerializedName("profile_picture")
        @Expose
        public String profile_picture;

        @SerializedName("active")
        @Expose
        public boolean active;

        @SerializedName("approve")
        @Expose
        public boolean approve;

        @SerializedName("available")
        @Expose
        public boolean available;

        @SerializedName("uploaded_document")
        @Expose
        public boolean uploaded_document;

        @SerializedName("declined_reason")
        @Expose
        public Object declined_reason;

        @SerializedName("service_location_id")
        @Expose
        public String service_location_id;

        @SerializedName("vehicle_type_id")
        @Expose
        public String vehicle_type_id;

        @SerializedName("vehicle_type_name")
        @Expose
        public String vehicle_type_name;

        @SerializedName("car_make")
        @Expose
        public int car_make;
        @SerializedName("car_model")
        @Expose
        public int car_model;
        @SerializedName("car_make_name")
        @Expose
        public String car_make_name;
        @SerializedName("car_model_name")
        @Expose
        public String car_model_name;
        @SerializedName("car_color")
        @Expose
        public String car_color;
        @SerializedName("driver_lat")
        @Expose
        public Object driver_lat;
        @SerializedName("driver_lng")
        @Expose
        public Object driver_lng;
        @SerializedName("car_number")
        @Expose
        public String car_number;
        @SerializedName("rating")
        @Expose
        public double rating;
        @SerializedName("no_of_ratings")
        @Expose
        public int no_of_ratings;
        @SerializedName("timezone")
        @Expose
        public String timezone;
        @SerializedName("refferal_code")
        @Expose
        public String refferal_code;
        @SerializedName("map_key")
        @Expose
        public String map_key;
        @SerializedName("company_key")
        @Expose
        public String company_key;
        @SerializedName("currency_symbol")
        @Expose
        public String currency_symbol;
        @SerializedName("total_earnings")
        @Expose
        public Object total_earnings;
        @SerializedName("current_date")
        @Expose
        public String current_date;


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

        public String getProfile_picture() {
            return profile_picture;
        }

        public void setProfile_picture(String profile_picture) {
            this.profile_picture = profile_picture;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isApprove() {
            return approve;
        }

        public void setApprove(boolean approve) {
            this.approve = approve;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public boolean isUploaded_document() {
            return uploaded_document;
        }

        public void setUploaded_document(boolean uploaded_document) {
            this.uploaded_document = uploaded_document;
        }

        public Object getDeclined_reason() {
            return declined_reason;
        }

        public void setDeclined_reason(Object declined_reason) {
            this.declined_reason = declined_reason;
        }

        public String getService_location_id() {
            return service_location_id;
        }

        public void setService_location_id(String service_location_id) {
            this.service_location_id = service_location_id;
        }

        public String getVehicle_type_id() {
            return vehicle_type_id;
        }

        public void setVehicle_type_id(String vehicle_type_id) {
            this.vehicle_type_id = vehicle_type_id;
        }

        public String getVehicle_type_name() {
            return vehicle_type_name;
        }

        public void setVehicle_type_name(String vehicle_type_name) {
            this.vehicle_type_name = vehicle_type_name;
        }

        public int getCar_make() {
            return car_make;
        }

        public void setCar_make(int car_make) {
            this.car_make = car_make;
        }

        public int getCar_model() {
            return car_model;
        }

        public void setCar_model(int car_model) {
            this.car_model = car_model;
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

        public String getCar_color() {
            return car_color;
        }

        public void setCar_color(String car_color) {
            this.car_color = car_color;
        }

        public Object getDriver_lat() {
            return driver_lat;
        }

        public void setDriver_lat(Object driver_lat) {
            this.driver_lat = driver_lat;
        }

        public Object getDriver_lng() {
            return driver_lng;
        }

        public void setDriver_lng(Object driver_lng) {
            this.driver_lng = driver_lng;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getNo_of_ratings() {
            return no_of_ratings;
        }

        public void setNo_of_ratings(int no_of_ratings) {
            this.no_of_ratings = no_of_ratings;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getRefferal_code() {
            return refferal_code;
        }

        public void setRefferal_code(String refferal_code) {
            this.refferal_code = refferal_code;
        }

        public String getMap_key() {
            return map_key;
        }

        public void setMap_key(String map_key) {
            this.map_key = map_key;
        }

        public String getCompany_key() {
            return company_key;
        }

        public void setCompany_key(String company_key) {
            this.company_key = company_key;
        }

        public String getCurrency_symbol() {
            return currency_symbol;
        }

        public void setCurrency_symbol(String currency_symbol) {
            this.currency_symbol = currency_symbol;
        }

        public Object getTotal_earnings() {
            return total_earnings;
        }

        public void setTotal_earnings(Object total_earnings) {
            this.total_earnings = total_earnings;
        }

        public String getCurrent_date() {
            return current_date;
        }

        public void setCurrent_date(String current_date) {
            this.current_date = current_date;
        }
    }

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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public DriverDetail getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(DriverDetail driverDetail) {
        this.driverDetail = driverDetail;
    }

    public RequestBill getRequestBill() {
        return requestBill;
    }

    public void setRequestBill(RequestBill requestBill) {
        this.requestBill = requestBill;
    }
}
