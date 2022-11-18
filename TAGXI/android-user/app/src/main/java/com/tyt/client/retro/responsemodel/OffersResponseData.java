package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OffersResponseData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_location_id")
    @Expose
    private String serviceLocationId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("minimum_trip_amount")
    @Expose
    private Integer minimumTripAmount;
    @SerializedName("maximum_discount_amount")
    @Expose
    private Integer maximumDiscountAmount;
    @SerializedName("discount_percent")
    @Expose
    private Integer discountPercent;
    @SerializedName("total_uses")
    @Expose
    private Integer totalUses;
    @SerializedName("uses_per_user")
    @Expose
    private Integer usesPerUser;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("is_applied")
    @Expose
    private boolean isApplied;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceLocationId() {
        return serviceLocationId;
    }

    public void setServiceLocationId(String serviceLocationId) {
        this.serviceLocationId = serviceLocationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getMinimumTripAmount() {
        return minimumTripAmount;
    }

    public void setMinimumTripAmount(Integer minimumTripAmount) {
        this.minimumTripAmount = minimumTripAmount;
    }

    public Integer getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(Integer maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getTotalUses() {
        return totalUses;
    }

    public void setTotalUses(Integer totalUses) {
        this.totalUses = totalUses;
    }

    public Integer getUsesPerUser() {
        return usesPerUser;
    }

    public void setUsesPerUser(Integer usesPerUser) {
        this.usesPerUser = usesPerUser;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public boolean getIsApplied() {
        return isApplied;
    }
}
