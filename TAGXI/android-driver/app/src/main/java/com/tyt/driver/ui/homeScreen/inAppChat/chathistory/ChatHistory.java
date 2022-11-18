package com.tyt.driver.ui.homeScreen.inAppChat.chathistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatHistory {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    private List<Datum> data = null;

    private Datum[] data1 = null;

    private Datum data2 = null;



    public Datum getData2() {
        return data2;
    }

    public void setData2(Datum data2) {
        this.data2 = data2;
    }



    public Datum[] getData1() {
        return data1;
    }

    public void setData1(Datum[] data1) {
        this.data1 = data1;
    }


    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}