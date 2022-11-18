package com.tyt.client.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqBillData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("base_price")
    @Expose
    private Double basePrice;
    @SerializedName("base_distance")
    @Expose
    private Double baseDistance;
    @SerializedName("price_per_distance")
    @Expose
    private Double pricePerDistance;
    @SerializedName("distance_price")
    @Expose
    private Double distancePrice;
    @SerializedName("price_per_time")
    @Expose
    private Double pricePerTime;
    @SerializedName("time_price")
    @Expose
    private Double timePrice;
    @SerializedName("waiting_charge")
    @Expose
    private Double waitingCharge;
    @SerializedName("cancellation_fee")
    @Expose
    private Double cancellationFee;
    @SerializedName("service_tax")
    @Expose
    private Double serviceTax;
    @SerializedName("service_tax_percentage")
    @Expose
    private Double serviceTaxPercentage;
    @SerializedName("promo_discount")
    @Expose
    private Double promoDiscount;
    @SerializedName("admin_commision")
    @Expose
    private Double adminCommision;
    @SerializedName("driver_commision")
    @Expose
    private Double driverCommision;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("requested_currency_code")
    @Expose
    private String requestedCurrencyCode;
    @SerializedName("admin_commision_with_tax")
    @Expose
    private Double adminCommisionWithTax;

    @SerializedName("airport_surge_fee")
    @Expose
    private Double airport_fee;

    @SerializedName("requested_currency_symbol")
    @Expose
    private String requested_currency_symbol;

    public Double getAirport_fee() {
        return airport_fee;
    }

    public void setAirport_fee(Double airport_fee) {
        this.airport_fee = airport_fee;
    }

    public String getRequested_currency_symbol() {
        return requested_currency_symbol;
    }

    public void setRequested_currency_symbol(String requested_currency_symbol) {
        this.requested_currency_symbol = requested_currency_symbol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getBaseDistance() {
        return baseDistance;
    }

    public void setBaseDistance(Double baseDistance) {
        this.baseDistance = baseDistance;
    }

    public Double getPricePerDistance() {
        return pricePerDistance;
    }

    public void setPricePerDistance(Double pricePerDistance) {
        this.pricePerDistance = pricePerDistance;
    }

    public Double getDistancePrice() {
        return distancePrice;
    }

    public void setDistancePrice(Double distancePrice) {
        this.distancePrice = distancePrice;
    }

    public Double getPricePerTime() {
        return pricePerTime;
    }

    public void setPricePerTime(Double pricePerTime) {
        this.pricePerTime = pricePerTime;
    }

    public Double getTimePrice() {
        return timePrice;
    }

    public void setTimePrice(Double timePrice) {
        this.timePrice = timePrice;
    }

    public Double getWaitingCharge() {
        return waitingCharge;
    }

    public void setWaitingCharge(Double waitingCharge) {
        this.waitingCharge = waitingCharge;
    }

    public Double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(Double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public Double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public Double getServiceTaxPercentage() {
        return serviceTaxPercentage;
    }

    public void setServiceTaxPercentage(Double serviceTaxPercentage) {
        this.serviceTaxPercentage = serviceTaxPercentage;
    }

    public Double getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(Double promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public Double getAdminCommision() {
        return adminCommision;
    }

    public void setAdminCommision(Double adminCommision) {
        this.adminCommision = adminCommision;
    }

    public Double getDriverCommision() {
        return driverCommision;
    }

    public void setDriverCommision(Double driverCommision) {
        this.driverCommision = driverCommision;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRequestedCurrencyCode() {
        return requestedCurrencyCode;
    }

    public void setRequestedCurrencyCode(String requestedCurrencyCode) {
        this.requestedCurrencyCode = requestedCurrencyCode;
    }

    public Double getAdminCommisionWithTax() {
        return adminCommisionWithTax;
    }

    public void setAdminCommisionWithTax(Double adminCommisionWithTax) {
        this.adminCommisionWithTax = adminCommisionWithTax;
    }


}
