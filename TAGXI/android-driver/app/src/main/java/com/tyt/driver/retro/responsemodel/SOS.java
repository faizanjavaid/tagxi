package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SOS {
    @SerializedName("data")
    @Expose
    List<SOSData> sosDataList;

    public List<SOSData> getSosDataList() {
        return sosDataList;
    }

    public void setSosDataList(List<SOSData> sosDataList) {
        this.sosDataList = sosDataList;
    }

    public static class SOSData {
        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("number")
        @Expose
        String number="please add";

        @SerializedName("status")
        @Expose
        boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
