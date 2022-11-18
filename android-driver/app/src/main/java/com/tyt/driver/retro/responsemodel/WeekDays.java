package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekDays {
    @SerializedName("sun")
    @Expose
    private Double sun;
    @SerializedName("mon")
    @Expose
    private Double mon;
    @SerializedName("tues")
    @Expose
    private Double tues;
    @SerializedName("wed")
    @Expose
    private Double wed;
    @SerializedName("thurs")
    @Expose
    private Double thurs;
    @SerializedName("fri")
    @Expose
    private Double fri;

    public Double getSun() {
        return sun;
    }

    public void setSun(Double sun) {
        this.sun = sun;
    }

    public Double getMon() {
        return mon;
    }

    public void setMon(Double mon) {
        this.mon = mon;
    }

    public Double getTues() {
        return tues;
    }

    public void setTues(Double tues) {
        this.tues = tues;
    }

    public Double getWed() {
        return wed;
    }

    public void setWed(Double wed) {
        this.wed = wed;
    }

    public Double getThurs() {
        return thurs;
    }

    public void setThurs(Double thurs) {
        this.thurs = thurs;
    }

    public Double getFri() {
        return fri;
    }

    public void setFri(Double fri) {
        this.fri = fri;
    }

    public Double getSat() {
        return sat;
    }

    public void setSat(Double sat) {
        this.sat = sat;
    }

    @SerializedName("sat")
    @Expose
    private Double sat;



}
