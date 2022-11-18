package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayEarningsModel {
    @SerializedName("current_date")
    @Expose
    private String currentDate;
    @SerializedName("total_trips_count")
    @Expose
    private int totalTripsCount;
    @SerializedName("total_trip_kms")
    @Expose
    private Double totalTripKms;
    @SerializedName("total_earnings")
    @Expose
    private Double totalEarnings;
    @SerializedName("total_cash_trip_amount")
    @Expose
    private Double totalCashTripAmount;
    @SerializedName("total_wallet_trip_amount")
    @Expose
    private Double totalWalletTripAmount;
    @SerializedName("total_cash_trip_count")
    @Expose
    private int totalCashTripCount;
    @SerializedName("total_wallet_trip_count")
    @Expose
    private int totalWalletTripCount;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    @SerializedName("total_hours_worked")
    @Expose
    private String totalHoursWorked;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getTotalTripsCount() {
        return totalTripsCount;
    }



    public Double getTotalTripKms() {
        return totalTripKms;
    }

    public void setTotalTripKms(Double totalTripKms) {
        this.totalTripKms = totalTripKms;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Double getTotalCashTripAmount() {
        return totalCashTripAmount;
    }

    public void setTotalCashTripAmount(Double totalCashTripAmount) {
        this.totalCashTripAmount = totalCashTripAmount;
    }

    public Double getTotalWalletTripAmount() {
        return totalWalletTripAmount;
    }

    public void setTotalWalletTripAmount(Double totalWalletTripAmount) {
        this.totalWalletTripAmount = totalWalletTripAmount;
    }

    public int getTotalCashTripCount() {
        return totalCashTripCount;
    }


    public int getTotalWalletTripCount() {
        return totalWalletTripCount;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getTotalHoursWorked() {
        return totalHoursWorked;
    }
}
