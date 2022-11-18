package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtaModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("zone_type_id")
    @Expose
    private String zoneTypeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("short_description")
    @Expose
    private String short_description;

    @SerializedName("supported_vehicles")
    @Expose
    private String supported_vehicles;

    @SerializedName("is_default")
    @Expose
    private Boolean isDefault;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("has_discount")
    @Expose
    private Boolean hasDiscount;
    @SerializedName("discount_amount")
    @Expose
    private Double discountAmount;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("base_distance")
    @Expose
    private Double baseDistance;
    @SerializedName("base_price")
    @Expose
    private Double basePrice;
    @SerializedName("price_per_distance")
    @Expose
    private Double pricePerDistance;
    @SerializedName("price_per_time")
    @Expose
    private Integer pricePerTime;
    @SerializedName("distance_price")
    @Expose
    private Double distancePrice;
    @SerializedName("time_price")
    @Expose
    private Double timePrice;
    @SerializedName("ride_fare")
    @Expose
    private Double rideFare;
    @SerializedName("tax_amount")
    @Expose
    private Double taxAmount;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("approximate_value")
    @Expose
    private Integer approximateValue;
    @SerializedName("min_amount")
    @Expose
    private Double minAmount;
    @SerializedName("max_amount")
    @Expose
    private Double maxAmount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currency_name")
    @Expose
    private String currencyName;
    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("unit")
    @Expose
    private Object unit;
    @SerializedName("unit_in_words_without_lang")
    @Expose
    private String unitInWordsWithoutLang;
    @SerializedName("unit_in_words")
    @Expose
    private String unitInWords;
    @SerializedName("driver_arival_estimation")
    @Expose
    private String driverArivalEstimation;

    @SerializedName("fare_amount")
    private String fare_amount;

    public String getFare_amount() {
        return fare_amount;
    }

    public void setFare_amount(String fare_amount) {
        this.fare_amount = fare_amount;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getZoneTypeId() {
        return zoneTypeId;
    }

    public void setZoneTypeId(String zoneTypeId) {
        this.zoneTypeId = zoneTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Boolean getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }


    public Double getDistance() {
        return distance;
    }


    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Double getBaseDistance() {
        return baseDistance;
    }


    public Double getBasePrice() {
        return basePrice;
    }


    public Double getPricePerDistance() {
        return pricePerDistance;
    }


    public Integer getPricePerTime() {
        return pricePerTime;
    }

    public void setPricePerTime(Integer pricePerTime) {
        this.pricePerTime = pricePerTime;
    }

    public Double getDistancePrice() {
        return distancePrice;
    }


    public Double getTimePrice() {
        return timePrice;
    }


    public Double getRideFare() {
        return rideFare;
    }


    public Double getTaxAmount() {
        return taxAmount;
    }


    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }


    public Integer getApproximateValue() {
        return approximateValue;
    }

    public void setApproximateValue(Integer approximateValue) {
        this.approximateValue = approximateValue;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public String getUnitInWordsWithoutLang() {
        return unitInWordsWithoutLang;
    }

    public void setUnitInWordsWithoutLang(String unitInWordsWithoutLang) {
        this.unitInWordsWithoutLang = unitInWordsWithoutLang;
    }

    public String getUnitInWords() {
        return unitInWords;
    }

    public void setUnitInWords(String unitInWords) {
        this.unitInWords = unitInWords;
    }

    public String getDriverArivalEstimation() {
        return driverArivalEstimation;
    }

    public void setDriverArivalEstimation(String driverArivalEstimation) {
        this.driverArivalEstimation = driverArivalEstimation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupported_vehicles() {
        return supported_vehicles;
    }

    public void setSupported_vehicles(String supported_vehicles) {
        this.supported_vehicles = supported_vehicles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
