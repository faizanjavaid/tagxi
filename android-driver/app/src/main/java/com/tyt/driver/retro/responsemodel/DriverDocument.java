package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverDocument {
    @SerializedName("data")
    @Expose
    private DriverDocumentData data;

    public DriverDocumentData getData() {
        return data;
    }

    public void setData(DriverDocumentData data) {
        this.data = data;
    }

    public static class DriverDocumentData {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("document_id")
        @Expose
        private Integer documentId;
        @SerializedName("document_name")
        @Expose
        private String documentName;
        @SerializedName("document")
        @Expose
        private String document;
        @SerializedName("identify_number")
        @Expose
        private String identifyNumber;
        @SerializedName("expiry_date")
        @Expose
        private String expiryDate;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("document_status")
        @Expose
        private Integer documentStatus;
        @SerializedName("document_status_string")
        @Expose
        private String documentStatusString;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Integer documentId) {
            this.documentId = documentId;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public String getIdentifyNumber() {
            return identifyNumber;
        }

        public void setIdentifyNumber(String identifyNumber) {
            this.identifyNumber = identifyNumber;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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
    }
}
