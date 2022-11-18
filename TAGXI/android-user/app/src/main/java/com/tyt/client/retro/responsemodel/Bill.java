package com.tyt.client.retro.responsemodel;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    @SerializedName("show_bill")
    @Expose
    public int show_bill;
    @SerializedName("base_price")
    @Expose
    public Double base_price;
    @SerializedName("base_distance")
    @Expose
    public Double base_distance;
    @SerializedName("price_per_distance")
    @Expose
    public Double price_per_distance;
    @SerializedName("price_per_time")
    @Expose
    public Double price_per_time;
    @SerializedName("distance_price")
    @Expose
    public Double distance_price;
    @SerializedName("time_price")
    @Expose
    public Double time_price;
    @SerializedName("waiting_price")
    @Expose
    public Double waiting_price;
    @SerializedName("service_tax")
    @Expose
    public Double service_tax;
    @SerializedName("service_tax_percentage")
    @Expose
    public Double service_tax_percentage;
    @SerializedName("promo_amount")
    @Expose
    public Double promo_amount;
    @SerializedName("referral_amount")
    @Expose
    public Double referral_amount;
    @SerializedName("service_fee")
    @Expose
    public Double service_fee;
    @SerializedName("driver_amount")
    @Expose
    public Double driver_amount;
    @SerializedName("total")
    @Expose
    public Double total;
    @Expose
    public String currency;
    @SerializedName("totalAdditionalCharge")
    public Double totalAdditionalCharge;
    @SerializedName("AdditionalCharge")
    public List<AdditionalCharge> additionalCharge;
    @Expose
    public String unit_in_words;
    @Expose
    public Double wallet_amount;
    @Expose
    public Double ride_fare;
    @Expose
    public Double cancellation_fee;
    @Expose
    public Double drop_out_of_zone_fee;


    @SerializedName("custom_select_driver_fee")
    public double custom_select_driver_fee;


    public static class AdditionalCharge implements Serializable {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
        @SerializedName("amount")
        public String amount;
    }

    protected Bill(Parcel in) {
        show_bill = in.readInt();
        base_price = in.readDouble();
        totalAdditionalCharge = in.readDouble();
        base_distance = in.readDouble();
        price_per_distance = in.readDouble();
        price_per_time = in.readDouble();
        distance_price = in.readDouble();
        time_price = in.readDouble();
        waiting_price = in.readDouble();
        service_tax = in.readDouble();
        service_tax_percentage = in.readDouble();
        promo_amount = in.readDouble();
        referral_amount = in.readDouble();
        service_fee = in.readDouble();
        driver_amount = in.readDouble();
        total = in.readDouble();
        wallet_amount = in.readDouble();
        ride_fare = in.readDouble();
        cancellation_fee = in.readDouble();
        drop_out_of_zone_fee = in.readDouble();
        custom_select_driver_fee = in.readDouble();
    }
}