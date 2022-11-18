package com.tyt.client.retro;


import com.google.gson.JsonObject;
import com.tyt.client.R;


import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Query;


/**
 * Created by Mahi in 2021.
 */


public interface GitHubMapService {

    @GET("maps/api/geocode/json?")
    Call<JsonObject> GetAddressFromLatLng(@Query("latlng") String name, @Query("sensor") boolean a, @Query("key") String key);

    @GET("maps/api/place/autocomplete/json?radius=500")
    Call<JsonObject> GetPlaceApi(@Query("language") String language, @Query("location") String locat, @Query("input") String input, @Query("sensor") boolean a, @Query("key") String key);

    @GET("maps/api/place/autocomplete/json?radius=500")
    Call<JsonObject> GetPlaceApi(@Query("location") String locat, @Query("input") String input, @Query("sensor") boolean a, @Query("key") String key);

    @GET("maps/api/geocode/json?")
    Call<JsonObject> GetLatLngFromAddress(@Query("address") String name, @Query("sensor") boolean a, @Query("key") String key);

    @GET("maps/api/directions/json?{address}&key={key}")
    Call<JsonObject> GetDrawpath(@Query("address") String latlong, @Query("sensor") boolean a, @Query("key") String key);

}