package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeeklyModel {
    @SerializedName("week_days")
    @Expose
    private WeekDays weekDays;
    @SerializedName("current_date")
    @Expose
    private String currentDate;
    @SerializedName("current_week_number")
    @Expose
    private Integer currentWeekNumber;
    @SerializedName("start_of_week")
    @Expose
    private String startOfWeek;
    @SerializedName("end_of_week")
    @Expose
    private String endOfWeek;
    @SerializedName("disable_next_week")
    @Expose
    private Boolean disableNextWeek;
    @SerializedName("disable_previous_week")
    @Expose
    private Boolean disablePreviousWeek;
    @SerializedName("total_trips_count")
    @Expose
    private double totalTripsCount;
    @SerializedName("total_trip_kms")
    @Expose
    private Double totalTripKms;
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

    public WeekDays getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(WeekDays weekDays) {
        this.weekDays = weekDays;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getCurrentWeekNumber() {
        return currentWeekNumber;
    }

    public void setCurrentWeekNumber(Integer currentWeekNumber) {
        this.currentWeekNumber = currentWeekNumber;
    }

    public String getStartOfWeek() {
        return startOfWeek;
    }

    public void setStartOfWeek(String startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public String getEndOfWeek() {
        return endOfWeek;
    }

    public void setEndOfWeek(String endOfWeek) {
        this.endOfWeek = endOfWeek;
    }

    public Boolean getDisableNextWeek() {
        return disableNextWeek;
    }

    public void setDisableNextWeek(Boolean disableNextWeek) {
        this.disableNextWeek = disableNextWeek;
    }

    public Boolean getDisablePreviousWeek() {
        return disablePreviousWeek;
    }

    public void setDisablePreviousWeek(Boolean disablePreviousWeek) {
        this.disablePreviousWeek = disablePreviousWeek;
    }

    public double getTotalTripsCount() {
        return totalTripsCount;
    }

    public void setTotalTripsCount(Integer totalTripsCount) {
        this.totalTripsCount = totalTripsCount;
    }

    public Double getTotalTripKms() {
        return totalTripKms;
    }

    public void setTotalTripKms(Double totalTripKms) {
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

    public void setTotalWalletTripAmount(Integer totalWalletTripAmount) {
        this.totalWalletTripAmount = totalWalletTripAmount;
    }

    public double getTotalCashTripCount() {
        return totalCashTripCount;
    }

    public void setTotalCashTripCount(Integer totalCashTripCount) {
        this.totalCashTripCount = totalCashTripCount;
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
