package com.tyt.driver.utilz;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tyt.driver.retro.responsemodel.Route;
import com.tyt.driver.retro.responsemodel.Step;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Maputilz {

    public static Route parseRoute(JsonObject response, Route routeBean) {

        Step stepBean;
        //   JSONObject jObject = new JSONObject(response);
        JsonArray jArray = response.getAsJsonArray("routes");
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject innerjObject = jArray.get(i).getAsJsonObject();
            if (innerjObject != null) {
                JsonArray innerJarry = innerjObject.getAsJsonArray("legs");
                for (int j = 0; j < innerJarry.size(); j++) {
                    JsonObject jObjectLegs = innerJarry.get(j).getAsJsonObject();
                    routeBean.setDistanceText(jObjectLegs.getAsJsonObject("distance").get("text").getAsString());
                    routeBean.setDistanceValue(jObjectLegs.getAsJsonObject("distance").get("value").getAsInt());
                    routeBean.setDurationText(jObjectLegs.getAsJsonObject("distance").get("text").getAsString());
                    routeBean.setDurationValue(jObjectLegs.getAsJsonObject("duration").get("value").getAsInt());
                    routeBean.setStartAddress(jObjectLegs.get("start_address").getAsString());
                    if (jObjectLegs.has("end_address"))
                        routeBean.setEndAddress(jObjectLegs.get("end_address").getAsString());
                    routeBean.setStartLat(jObjectLegs.getAsJsonObject("start_location").get("lat").getAsDouble());
                    routeBean.setStartLon(jObjectLegs.getAsJsonObject("start_location").get("lng").getAsDouble());
                    routeBean.setEndLat(jObjectLegs.getAsJsonObject("end_location").get("lat").getAsDouble());
                    routeBean.setEndLon(jObjectLegs.getAsJsonObject("end_location").get("lng").getAsDouble());
                    JsonArray jstepArray = jObjectLegs
                            .getAsJsonArray("steps");
                    if (jstepArray != null) {
                        for (int k = 0; k < jstepArray.size(); k++) {
                            stepBean = new Step();
                            JsonObject jStepObject = jstepArray
                                    .get(k).getAsJsonObject();
                            if (jStepObject != null) {

                                stepBean.setHtml_instructions(jStepObject
                                        .get("html_instructions").getAsString());
                                stepBean.setStrPoint(jStepObject
                                        .getAsJsonObject("polyline")
                                        .get("points").getAsString());
                                stepBean.setStartLat(jStepObject
                                        .getAsJsonObject("start_location")
                                        .get("lat").getAsDouble());
                                stepBean.setStartLon(jStepObject
                                        .getAsJsonObject("start_location")
                                        .get("lng").getAsDouble());
                                stepBean.setEndLat(jStepObject
                                        .getAsJsonObject("end_location")
                                        .get("lat").getAsDouble());
                                stepBean.setEndLong(jStepObject
                                        .getAsJsonObject("end_location")
                                        .get("lng").getAsDouble());

                                stepBean.setListPoints(new PolyLineUtils()
                                        .decodePoly(stepBean.getStrPoint()));
                                routeBean.getListStep().add(stepBean);
                            }
                        }
                    }
                }

            }

        }
        return routeBean;
    }

    public interface RouteMapClient {
        @GET("/maps/api/directions/json")
        Call<JsonObject> GetDrawpath(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
        @GET("/maps/api/directions/json")
        void GetDrawpath(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key, Callback<JsonObject> callback);
    }


}


