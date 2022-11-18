package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddmoneyData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("amount_added")
    @Expose
    private Double amountAdded;
    @SerializedName("amount_balance")
    @Expose
    private Double amountBalance;
    @SerializedName("amount_spent")
    @Expose
    private Double amountSpent;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("currency_symbol")
    @Expose
    private String CurrencySymbol;


    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmountAdded() {
        return amountAdded;
    }


    public Double getAmountBalance() {
        return amountBalance;
    }


    public Double getAmountSpent() {
        return amountSpent;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }
}
