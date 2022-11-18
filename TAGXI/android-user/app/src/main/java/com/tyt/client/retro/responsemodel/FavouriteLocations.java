package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouriteLocations {

    @SerializedName("data")
    @Expose
    public List<FavLocData> favLocData;

    public List<FavLocData> getFavLocData() {
        return favLocData;
    }

    public void setFavLocData(List<FavLocData> favLocData) {
        this.favLocData = favLocData;
    }

    public static class FavLocData{

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("pick_lat")
        @Expose
        private float pick_lat;

        @SerializedName("pick_lng")
        @Expose
        private float pick_lng;

        @SerializedName("drop_lat")
        @Expose
        private float drop_lat;

        @SerializedName("drop_lng")
        @Expose
        private float drop_lng;

        @SerializedName("pick_address")
        @Expose
        private String pick_address;

        @SerializedName("drop_address")
        @Expose
        private String drop_address;

        @SerializedName("address_name")
        @Expose
        private String address_name;

        @SerializedName("landmark")
        @Expose
        private String landmark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getPick_lat() {
            return pick_lat;
        }

        public void setPick_lat(float pick_lat) {
            this.pick_lat = pick_lat;
        }

        public float getPick_lng() {
            return pick_lng;
        }

        public void setPick_lng(float pick_lng) {
            this.pick_lng = pick_lng;
        }

        public float getDrop_lat() {
            return drop_lat;
        }

        public void setDrop_lat(float drop_lat) {
            this.drop_lat = drop_lat;
        }

        public float getDrop_lng() {
            return drop_lng;
        }

        public void setDrop_lng(float drop_lng) {
            this.drop_lng = drop_lng;
        }

        public String getPick_address() {
            return pick_address;
        }

        public void setPick_address(String pick_address) {
            this.pick_address = pick_address;
        }

        public String getDrop_address() {
            return drop_address;
        }

        public void setDrop_address(String drop_address) {
            this.drop_address = drop_address;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }
    }
}
