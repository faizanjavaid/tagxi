package com.tyt.client.ui.homeScreen.makeTrip.RouteUtils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RouteMapClient {
    @GET("/maps/api/directions/json")
    Call<JsonObject> GetDrawpath(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
    @GET("/maps/api/directions/json")
    void GetDrawpath(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key, Callback<JsonObject> callback);
}
