package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarningsReportModel {
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("total_trips_count")
    @Expose
    private Integer totalTripsCount;
    @SerializedName("total_trip_kms")
    @Expose
    private double totalTripKms;
    @SerializedName("total_earnings")
    @Expose
    private double totalEarnings;
    @SerializedName("total_cash_trip_amount")
    @Expose
    private double totalCashTripAmount;
    @SerializedName("total_wallet_trip_amount")
    @Expose
    private double totalWalletTripAmount;
    @SerializedName("total_cash_trip_count")
    @Expose
    private double totalCashTripCount;
    @SerializedName("total_wallet_trip_count")
    @Expose
    private double totalWalletTripCount;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;
    @SerializedName("total_hours_worked")
    @Expose
    private String totalHoursWorked;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public double getTotalTripsCount() {
        return totalTripsCount;
    }

    public void setTotalTripsCount(Integer totalTripsCount) {
        this.totalTripsCount = totalTripsCount;
    }

    public double getTotalTripKms() {
        return totalTripKms;
    }

    public void setTotalTripKms(Integer totalTripKms) {
        this.totalTripKms = totalTripKms;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Integer totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public double getTotalCashTripAmount() {
        return totalCashTripAmount;
    }

    public void setTotalCashTripAmount(Integer totalCashTripAmount) {
        this.totalCashTripAmount = totalCashTripAmount;
    }

    public double getTotalWalletTripAmount() {
        return totalWalletTripAmount;
    }

    public double getTotalCashTripCount() {
        return totalCashTripCount;
    }

    public double getTotalWalletTripCount() {
        return totalWalletTripCount;
    }

    public void setTotalWalletTripCount(Integer totalWalletTripCount) {
        this.totalWalletTripCount = totalWalletTripCount;
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

    public void setTotalHoursWorked(String totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }
}
