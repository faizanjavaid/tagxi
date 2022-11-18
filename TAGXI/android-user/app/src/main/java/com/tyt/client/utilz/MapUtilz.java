package com.tyt.client.utilz;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tyt.client.R;
import com.tyt.client.retro.responsemodel.Route;
import com.tyt.client.retro.responsemodel.Step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
* This class contains all related utilities required by the google map.
* */

public class MapUtilz {

    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if (iStream != null)
                iStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return data;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + R.string.Map_key;
    }

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

}
