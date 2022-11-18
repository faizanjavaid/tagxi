package com.tyt.client.retro.responsemodel;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("latitude")
    @Expose
    public Float latitude;
    @SerializedName("longitude")
    @Expose
    public Float longitude;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("bearing")
    @Expose
    public Float bearing;
    public Boolean status; //true updated //false new
    public Marker marker;
    public MarkerOptions markerOptions;

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
       /* result = prime * result + ((bearing == null) ? 0 : bearing.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
        result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());*/
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Car other = (Car) obj;
        /*if (bearing == null) {
            if (other.bearing != null)
                return false;
        } else if (!bearing.equals(other.bearing))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (bearing == null) {
            if (other.bearing != null)
                return false;
        } else if (!bearing.equals(other.bearing))
            return false;*/
        if (id == null) {
            return other.id == null;
        } else return id == other.id;
    }
}
