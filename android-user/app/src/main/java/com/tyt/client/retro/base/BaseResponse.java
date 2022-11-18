package com.tyt.client.retro.base;

import android.util.Log;

import com.google.gson.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.client.retro.responsemodel.HistoryMeta;
import com.tyt.client.retro.responsemodel.TranslationModel;

import com.tyt.client.retro.responsemodel.WalletHistoryModel;
import com.tyt.client.retro.responsemodel.tripRequest.RequestResult;
import com.tyt.client.utilz.SharedPrefence;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahi in 2021.
 */

/**
 * contains commonly used API response parameters
 **/

public class BaseResponse implements Serializable {

    @SerializedName("success")
    @Expose
    public boolean success;

    @SerializedName("update_sheet")
    @Expose
    public boolean update_sheet;

    @SerializedName("status_code")
    @Expose
    public int statusCode;

    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("expires_in")
    @Expose
    public Integer expiresIn;
    @SerializedName("access_token")
    @Expose
    public String accessToken;

    @SerializedName("success_message")
    @Expose
    public String successMessage;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("error_message")
    @Expose
    public String errorMessage;

    @SerializedName("error_code")
    @Expose
    public Integer errorCode;


    @SerializedName("wallet_balance")
    @Expose
    public Double walletBalance;
    @SerializedName("currency_code")
    @Expose
    public String currencyCode;


    @SerializedName("default_card_id")
    @Expose
    public String DefaultCardID;

    @SerializedName("currency_symbol")
    @Expose
    public String currencySymbol;
    @SerializedName("wallet_history")
    @Expose
    public WalletHistoryModel walletHistory;

    @SerializedName("message_keyword")
    @Expose
    public String message_keyword;

    @Expose
    public Object data;

    @SerializedName("errors")
    Map<String, List<String>> errors;

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    @SerializedName("result")
    @Expose
    public RequestResult result;

    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("show_cancellation_reason")
    @Expose
    private Boolean showCancellationReason;
    @SerializedName("trip_start")
    @Expose
    private String tripStart;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("waiting_time")
    @Expose
    private String waitingTime;
    @SerializedName("bearing")
    @Expose
    private Double bearing;

    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("distancee")
    @Expose
    private double distancee;


    @SerializedName("meta")
    @Expose
    public HistoryMeta historyMeta;


    public HistoryMeta getHistoryMeta() {
        return historyMeta;
    }

    public void setHistoryMeta(HistoryMeta historyMeta) {
        this.historyMeta = historyMeta;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getShowCancellationReason() {
        return showCancellationReason;
    }

    public void setShowCancellationReason(Boolean showCancellationReason) {
        this.showCancellationReason = showCancellationReason;
    }

    public Boolean isUpdate_sheet() {
        return update_sheet;
    }

    public String getTripStart() {
        return tripStart;
    }

    public void setTripStart(String tripStart) {
        this.tripStart = tripStart;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getDistancee() {
        return distancee;
    }

    public void setDistancee(Integer distancee) {
        this.distancee = distancee;
    }


    public static class DataObject {
        @Expose
        public TranslationModel en;
        @Expose
        public TranslationModel ar;
        @Expose
        public TranslationModel ta;
        @Expose
        public TranslationModel es;
        @Expose
        public TranslationModel ja;
        @Expose
        public TranslationModel ko;
        @Expose
        public TranslationModel pt;
        @Expose
        public TranslationModel zh;
        @Expose
        public TranslationModel hi;
    }

    public static class ClientToken {

        @SerializedName("client_token")
        @Expose
        private String clientToken;

        public String getClientToken() {
            return clientToken;
        }

        public void setClientToken(String clientToken) {
            this.clientToken = clientToken;
        }

    }

    public void saveLanguageTranslations(SharedPrefence sharedPrefence, Gson gson, DataObject dataObject) {
        JSONObject map = null;
        List<String> languages = new ArrayList<>();
        try {
            map = new JSONObject(gson.toJson(dataObject));
            Iterator<String> iterator = map.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                sharedPrefence.savevalue(key, map.get(key).toString());
                languages.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Languages==", "Lang==" + new Gson().toJson(languages));
        sharedPrefence.savevalue(SharedPrefence.LANGUANGES, gson.toJson(languages) + "");
    }
}
