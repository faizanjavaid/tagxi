package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletHistoryModel {

    @SerializedName("data")
    @Expose
    private List<WalletHistoryData> data = null;

    public List<WalletHistoryData> getData() {
        return data;
    }

    public void setData(List<WalletHistoryData> data) {
        this.data = data;
    }

    public static class WalletHistoryData {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("card_id")
        @Expose
        private String cardId;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("conversion")
        @Expose
        private String conversion;
        @SerializedName("merchant")
        @Expose
        private String merchant;
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("is_credit")
        @Expose
        private Integer isCredit;
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

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getConversion() {
            return conversion;
        }

        public void setConversion(String conversion) {
            this.conversion = conversion;
        }

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String merchant) {
            this.merchant = merchant;
        }

        public String getRemarks() {
            return remarks;
        }

        public Integer getIsCredit() {
            return isCredit;
        }

        public void setIsCredit(Integer isCredit) {
            this.isCredit = isCredit;
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

    }
}
