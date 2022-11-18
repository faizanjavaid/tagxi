package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Documentdata {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("doc_type")
    @Expose
    private String docType;
    @SerializedName("has_identify_number")
    @Expose
    private Boolean hasIdentifyNumber;
    @SerializedName("has_expiry_date")
    @Expose
    private Boolean hasExpiryDate;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("identify_number_locale_key")
    @Expose
    private Object identifyNumberLocaleKey;
    @SerializedName("is_uploaded")
    @Expose
    private Boolean isUploaded;
    @SerializedName("document_status")
    @Expose
    private Integer documentStatus;
    @SerializedName("document_status_string")
    @Expose
    private String documentStatusString;
    @SerializedName("driver_document")
    @Expose
    private DriverDocument driverDocument;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Boolean getHasIdentifyNumber() {
        return hasIdentifyNumber;
    }

    public void setHasIdentifyNumber(Boolean hasIdentifyNumber) {
        this.hasIdentifyNumber = hasIdentifyNumber;
    }

    public Boolean getHasExpiryDate() {
        return hasExpiryDate;
    }

    public void setHasExpiryDate(Boolean hasExpiryDate) {
        this.hasExpiryDate = hasExpiryDate;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Object getIdentifyNumberLocaleKey() {
        return identifyNumberLocaleKey;
    }

    public void setIdentifyNumberLocaleKey(Object identifyNumberLocaleKey) {
        this.identifyNumberLocaleKey = identifyNumberLocaleKey;
    }

    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public Integer getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Integer documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentStatusString() {
        return documentStatusString;
    }

    public void setDocumentStatusString(String documentStatusString) {
        this.documentStatusString = documentStatusString;
    }

    public DriverDocument getDriverDocument() {
        return driverDocument;
    }

    public void setDriverDocument(DriverDocument driverDocument) {
        this.driverDocument = driverDocument;
    }

}
