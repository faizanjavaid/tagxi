package com.tyt.client.retro.responsemodel;


import com.tyt.client.retro.responsemodel.tripRequest.RequestBill;

public class HistoryCard {
    private String id;
    private String requestNumber;
    private String pickAddress;
    private String dropAddress;

    private Integer isLater;
    private String tripStartTime;

    private Integer isCancelled;
    private String updated_at;

    private Integer isCompleted;
    private RequestBill requestBill;
    private HistoryModel.DriverDetail driverDetail;
    private Object arrivedAt;
    private String completedAt;

    public HistoryCard(String id, String requestNumber, String pickAddress, String dropAddress, Integer isLater, String tripStartTime, Integer isCancelled, String updated_at, Integer isCompleted, RequestBill requestBill, HistoryModel.DriverDetail driverDetail, Object arrivedAt, String completedAt) {
        this.id = id;
        this.requestNumber = requestNumber;
        this.pickAddress = pickAddress;
        this.dropAddress = dropAddress;
        this.isLater = isLater;
        this.tripStartTime = tripStartTime;
        this.isCancelled = isCancelled;
        this.updated_at = updated_at;
        this.isCompleted = isCompleted;
        this.requestBill = requestBill;
        this.driverDetail = driverDetail;
        this.arrivedAt = arrivedAt;
        this.completedAt = completedAt;
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

    public Integer getIsLater() {
        return isLater;
    }

    public void setIsLater(Integer isLater) {
        this.isLater = isLater;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Integer getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Integer isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public RequestBill getRequestBill() {
        return requestBill;
    }

    public void setRequestBill(RequestBill requestBill) {
        this.requestBill = requestBill;
    }

    public HistoryModel.DriverDetail getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(HistoryModel.DriverDetail driverDetail) {
        this.driverDetail = driverDetail;
    }

    public Object getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(Object arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}


