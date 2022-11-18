package com.tyt.client.utilz;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * This clss contains All intiations and logics required to observe from Firebase database
 */

public class FirebaseHelper {

    static SharedPrefence sharedPrefence;
    static FirebaseObserver firebaseObserver;
    static String TAG = "FirebaseHelperLog";

    static boolean isInsideTrip = false;

    static DatabaseReference ref;
    static DatabaseReference requestRef;
    static GeoFire geoRef;
    static GeoQuery geoQuery;
    static HashMap<String, ValueEventListener> listeners = new HashMap<>();
    static GeoQueryEventListener queryEventListener;

    public static void init(SharedPrefence prefence, FirebaseObserver observer, boolean isInTrip) {
        sharedPrefence = prefence;
        firebaseObserver = observer;
        isInsideTrip = isInTrip;

        setupReferences();
    }

    public static void addObservers(FirebaseObserver observer) {
        firebaseObserver = observer;
    }

    private static void setupReferences() {
        ref = FirebaseDatabase.getInstance().getReference("drivers");
        requestRef = FirebaseDatabase.getInstance().getReference("requests");
        geoRef = new GeoFire(ref);
    }

    public static void queryDrivers(LatLng latLng) {
        if (queryEventListener != null && geoQuery != null) {
            geoQuery.removeGeoQueryEventListener(queryEventListener);
            Log.v("fatal_log", "removing query listeners");
        } else {
            Log.v("fatal_log", "failed to remove query listeners");
        }
        geoQuery = geoRef.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 10);
        queryEventListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                Log.v("fatal_log", "onKeyEntered: " + key);
                ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                jsonObject.put(Objects.requireNonNull(ds.getKey()), ds.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        firebaseObserver.driverEnteredFence(key, location, jsonObject.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onKeyExited(final String key) {
                Log.v("fatal_log", "onKeyExited " + key);
                ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                jsonObject.put(Objects.requireNonNull(ds.getKey()), ds.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("oject---", "object----" + key + "_________" + jsonObject.toString());
                        firebaseObserver.driverExitedFence(key, jsonObject.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onKeyMoved(final String key, final GeoLocation location) {
                Log.v("fatal_log", "onKeyMoved " + key);
                ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                jsonObject.put(ds.getKey(), ds.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        firebaseObserver.driverMovesInFence(key, location, jsonObject.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onGeoQueryReady() {
                Log.v("fatal_log", "onGeoQueryReady");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.v("fatal_log", "Error: " + error);
            }
        };

        geoQuery.addGeoQueryEventListener(queryEventListener);
    }

    public static void addObserverFor(final String key) {
        DatabaseReference driverRef = ref.child(key);
        ValueEventListener eventListener = driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JSONObject jsonObject = new JSONObject();
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        jsonObject.put(Objects.requireNonNull(ds.getKey()), ds.getValue());
                    }

                    if (jsonObject.has("is_active")) {
                        int isActive = jsonObject.getInt("is_active");
                        if (isActive != 1) {
                            firebaseObserver.driverWentOffline(key);
                        } else {
                            firebaseObserver.driverDataUpdated(key, jsonObject.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listeners.put(key, eventListener);
    }

    public static void removeObserverFor(String key) {
        DatabaseReference driverRef = ref.child(key);
        if (listeners.containsKey(key)) {
            driverRef.removeEventListener(listeners.get(key));
        }
    }

    public static void addTripObserverFor(String requestId) {
        Log.v("trip_status", "requestId: " + requestId);
        DatabaseReference tripRef = requestRef.child(requestId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String jsonStr = new Gson().toJson(snapshot.getValue());
                Log.e("xxxtripReceieved--", "recc---" + new Gson().toJson(jsonStr));
                firebaseObserver.tripStatusReceived(jsonStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("xxxfirebasehelp", "onCancelled: " + error.getMessage());
            }
        });
    }

    public interface FirebaseObserver {

        void driverEnteredFence(String key, GeoLocation location, String response);

        void driverExitedFence(String key, String response);

        void driverMovesInFence(String key, GeoLocation location, String response);

        void driverWentOffline(String key);

        void driverDataUpdated(String key, String response);

        void tripStatusReceived(String response);

    }

}
