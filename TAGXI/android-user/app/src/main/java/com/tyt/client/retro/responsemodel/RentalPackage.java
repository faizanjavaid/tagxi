package com.tyt.client.retro.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RentalPackage {
    @SerializedName("id")
    public String id;

    @SerializedName("package_name")
    public String package_name;

    @SerializedName("typesWithPrice")
    public RentalTypedata typesWithPrice;

    public static class RentalTypedata{

        @SerializedName("data")
        public List<EtaModel> data;

        public List<EtaModel> getData() {
            return data;
        }

        public void setData(List<EtaModel> data) {
            this.data = data;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public RentalTypedata getTypesWithPrice() {
        return typesWithPrice;
    }

    public void setTypesWithPrice(RentalTypedata typesWithPrice) {
        this.typesWithPrice = typesWithPrice;
    }
}
