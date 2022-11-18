package com.tyt.driver.retro.base;

import android.util.Log;

import com.google.gson.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tyt.driver.retro.responsemodel.HistoryMeta;
import com.tyt.driver.retro.responsemodel.WalletHistoryModel;
import com.tyt.driver.retro.responsemodel.tripRequest.RequestResult;
import com.tyt.driver.retro.responsemodel.TranslationModel;

import com.tyt.driver.utilz.SharedPrefence;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 9/27/17.
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

    @SerializedName("enable_submit_button")
    @Expose
    public boolean enable_submit_button;


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


    @SerializedName("result")
    @Expose
    private RequestResult result;


    @SerializedName("message_keyword")
    @Expose
    public String message_keyword;

    public Object data;

    @SerializedName("default_card_id")
    @Expose
    public String DefaultCardID;
    @SerializedName("wallet_balance")
    @Expose
    public Double walletBalance;
    @SerializedName("currency_code")
    @Expose
    public String currencyCode;
    @SerializedName("currency_symbol")
    @Expose
    public String currencySymbol;
    @SerializedName("wallet_history")
    @Expose
    public WalletHistoryModel walletHistory;

    @SerializedName("errors")
    Map<String, List<String>> errors;

    @SerializedName("meta")
    @Expose
    public HistoryMeta historyMeta;


    public HistoryMeta getHistoryMeta() {
        return historyMeta;
    }

    public void setHistoryMeta(HistoryMeta historyMeta) {
        this.historyMeta = historyMeta;
    }

    public boolean isUpdate_sheet() {
        return update_sheet;
    }

    public void setUpdate_sheet(boolean update_sheet) {
        this.update_sheet = update_sheet;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public RequestResult getResult() {
        return result;
    }


    public class DataObject {
        @Expose
        public TranslationModel en;
        @Expose
        public TranslationModel ta;
        @Expose
        public TranslationModel es;
        @Expose
        public TranslationModel ar;
        @Expose
        public TranslationModel ja;
        @Expose
        public TranslationModel ko;
        @Expose
        public TranslationModel pt;
        @Expose
        public TranslationModel zh;
    }

    public class ClientToken {

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
