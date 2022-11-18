package com.tyt.driver.retro.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelledDashboard {

    @SerializedName("total_cancellation")
    @Expose
    private String totalCancellation;
    @SerializedName("net")
    @Expose
    private String net;
    @SerializedName("total_paid")
    @Expose
    private String totalPaid;

    public String getTotalCancellation() {
        return totalCancellation;
    }

    public void setTotalCancellation(String totalCancellation) {
        this.totalCancellation = totalCancellation;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

}