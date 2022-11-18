package com.tyt.client.retro.responsemodel.tripRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBill {

    @SerializedName("data")
    @Expose
    private ReqBillData data;

    public ReqBillData getData() {
        return data;
    }

    public void setData(ReqBillData data) {
        this.data = data;
    }

    public static class RequestBillData {


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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Double getBasePrice() {
            return basePrice;
        }


        public Double getBaseDistance() {
            return baseDistance;
        }


        public Double getPricePerDistance() {
            return pricePerDistance;
        }


        public Double getDistancePrice() {
            return distancePrice;
        }


        public Double getPricePerTime() {
            return pricePerTime;
        }


        public Double getTimePrice() {
            return timePrice;
        }


        public Double getWaitingCharge() {
            return waitingCharge;
        }


        public Double getCancellationFee() {
            return cancellationFee;
        }


        public Double getServiceTax() {
            return serviceTax;
        }


        public Double getServiceTaxPercentage() {
            return serviceTaxPercentage;
        }


        public Double getPromoDiscount() {
            return promoDiscount;
        }


        public Double getAdminCommision() {
            return adminCommision;
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

}
