package com.tyt.driver.utilz;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.tyt.driver.app.MyApp;
import com.tyt.driver.ui.base.BaseActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketHelper {
    static Socket mSocket;
    static SharedPrefence sharedPrefence;
    static String TAG = "SocketHelper";
    static SocketListener socketDataReceiver;
    static String pendingTypeData, typesDriversData = null;

    static Long lastSocketConnected, isDriversLastListed;
    static boolean isInsideTrip = false;

    static MqttAndroidClient mqttAndroidClient;
    private static final String MQTT_URL = "tcp://34.239.45.247:1883";
//    private static String M_USERNAME = "";
//    private static String M_PASSWORD = "";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference ref = database.getReference("drivers");
    private static DatabaseReference locRef = database.getReference("driver_locations");
    private static DatabaseReference requestRef = database.getReference("requests");
    private static Long lastLocationUpdate;

    // static BaseActivity baseActivity;

    public static void init(SharedPrefence prefence, SocketListener socketDataReceivers, String tag, boolean isInTrip) {
        socketDataReceiver = socketDataReceivers;
        isInsideTrip = isInTrip;
        sharedPrefence = prefence;
//        TAG = tag;
        SetSocketListener();
    }

    /**
     * Initiate the socket Events.
     */
    public static void SetSocketListener() {
//        if (mSocket != null)
//            return;
//        IO.Options opts = new IO.Options();
//        opts.forceNew = true;
//        opts.reconnection = true;
//        opts.transports = new String[]{WebSocket.NAME};
//        try {
//            if (mSocket == null)
//                mSocket = IO.socket(Constants.URL.SOCKET_URL, opts);
//            if (!(mSocket.connected())) {
//                Log.v("SocketTriggering", "xxxxxxxxxxxxxxxxxxxxx" + (mSocket != null ? ("Is connected" + mSocket.connected()) : "mSocket is Null"));
//                mSocket.on(Socket.EVENT_CONNECT, onConnect);
//                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//                mSocket.on("create_request", CreateReq);
//                mSocket.on("request_handler", RequestHandler);
//                mSocket.on("approval_status", approvalStatus);
//                mSocket.connect();
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        final String driverID = sharedPrefence.Getvalue(SharedPrefence.ID);

        try {
            if (mqttAndroidClient != null) {
                if (mqttAndroidClient.isConnected()) {
                    mqttAndroidClient.disconnect();
                }
            }

            mqttAndroidClient = new MqttAndroidClient(MyApp.getmContext(), MQTT_URL, MqttClient.generateClientId());
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.v(TAG, "Connection complete"+"server="+serverURI+" clientId="+MqttClient.generateClientId());
                    subscribeToTopic(Constants.MqttEvents.create_request + "_" + driverID);
                    subscribeToTopic(Constants.MqttEvents.request_handler + "_" + driverID);
                    subscribeToTopic(Constants.MqttEvents.approval_status + "_" + driverID);
                    subscribeToTopic(Constants.MqttEvents.received_chat_status + "_" + driverID);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.v(TAG, "Connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    Log.d("xxxSocketHelper", topic + ": " + message.toString());
                    if (topic.equalsIgnoreCase(Constants.MqttEvents.create_request + "_" + driverID))
                        socketDataReceiver.onCreateRequest(message.toString());
                    else if (topic.equalsIgnoreCase(Constants.MqttEvents.request_handler + "_" + driverID))
                        socketDataReceiver.RequestHandler(message.toString());
                    else if (topic.equalsIgnoreCase(Constants.MqttEvents.approval_status + "_" + driverID))
                        socketDataReceiver.ApprovalStatus(message.toString());
                    else if (topic.equalsIgnoreCase(Constants.MqttEvents.received_chat_status + "_" + driverID))
                        socketDataReceiver.ReceivedChatStatus(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    try {
                        Log.v(TAG, "MQTT msg sent");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(false);
//            mqttConnectOptions.setUserName(M_USERNAME);
//            mqttConnectOptions.setPassword(M_PASSWORD.toCharArray());

            try {
                Log.v(TAG, "Connecting...");
                IMqttToken token = mqttAndroidClient.connect(mqttConnectOptions);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.v(TAG, "Mqtt connection success");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.v(TAG, "Mqtt Connection failure: " + exception.toString());
                    }
                });
            } catch (Exception e) {
                Log.v(TAG, "e1: " + e.getMessage());
            }
        } catch (Exception e) {
            Log.v(TAG, "e1: " + e.getMessage());
        }
    }

    private static void subscribeToTopic(final String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.v(TAG, "Subscribed to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.v(TAG, "Failed to subscribe to topic " + topic);
                }
            });
        } catch (Exception e) {
            Log.v(TAG, "Subscribe error: " + e.getLocalizedMessage());
        }
    }

    public static void DisconnectSocket() {
        if (mSocket == null)
            return;
        /**********Trunning Off Socket********/
        JSONObject object = new JSONObject();
        try {
            // object.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
            object.put("socket_id", mSocket.id() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("disconnect", object.toString());
        /**********************************/
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

    }

    private static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "connected");
            JSONObject object = new JSONObject();
            try {
                object.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
                //object.put("socket_id", mSocket.id() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (lastSocketConnected == null) {
                mSocket.emit(Constants.NetworkParameters.start_connect, object.toString());
                lastSocketConnected = System.currentTimeMillis();
            } else if ((System.currentTimeMillis() - lastSocketConnected) > 2000) {
                mSocket.emit(Constants.NetworkParameters.start_connect, object.toString());
                lastSocketConnected = System.currentTimeMillis();
            }
            Log.i("SocketTriggering", "start_connect = " + object.toString() + " Connected=" + mSocket.connected());
        }
    };

    /**
     * Disconnect the socket.
     */
    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "onDisconnect");
            if (socketDataReceiver != null)
                socketDataReceiver.OnDisconnect();
        }
    };

    private static Emitter.Listener CreateReq = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "CreateRequest Trigress");
            if (socketDataReceiver != null)
                socketDataReceiver.onCreateRequest(args[0].toString());
        }
    };

    private static Emitter.Listener RequestHandler = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "CreateRequest Cancel");
            if (socketDataReceiver != null)
                socketDataReceiver.RequestHandler(args[0].toString());
        }
    };

    private static Emitter.Listener approvalStatus = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "Approval Status");
            if (socketDataReceiver != null)
                socketDataReceiver.ApprovalStatus(args[0].toString());
        }
    };

    /**
     * Through the error of socket connections.
     */
    private static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "onConnectError");
            if (socketDataReceiver != null)
                socketDataReceiver.OnConnectError();

            Exception e = (Exception) args[0];
            Log.e(TAG, "Transport error " + e);
        }
    };

    public static void setDriverLocation(String driverLocationStr) {
//        if (mSocket != null) {
//            if (!mSocket.connected()) {
//                if (!sharedPrefence.GetBoolean(SharedPrefence.isOnline))
//                    SetSocketListener();
//            } else {
//                if (lastLocationUpdate == null) {
//                    mSocket.emit(Constants.NetworkParameters.SET_LOCATION, driverLocationStr);
//                    Log.i("SocketTriggering", Constants.NetworkParameters.SET_LOCATION + "--------------" + driverLocationStr);
//                    lastLocationUpdate = System.currentTimeMillis();
//                } else if ((System.currentTimeMillis() - lastLocationUpdate) > 4000) {
//                    mSocket.emit(Constants.NetworkParameters.SET_LOCATION, driverLocationStr);
//                    Log.i("SocketTriggering", Constants.NetworkParameters.SET_LOCATION + "--------------" + driverLocationStr);
//                    lastLocationUpdate = System.currentTimeMillis();
//                }
//            }
//        }

        try {
            JSONObject jsonObject = new JSONObject(driverLocationStr);
            double dLat = Double.parseDouble(jsonObject.getString("lat"));
            int IDVALUE = Integer.parseInt(jsonObject.getString("id"));
            double dLng = Double.parseDouble(jsonObject.getString("lng"));
            float bearing = Float.parseFloat(jsonObject.getString("bearing"));
            String status = jsonObject.getString(Constants.NetworkParameters.isActive);
            String vehId = jsonObject.getString(Constants.NetworkParameters.VehId);

            int ActiveStatus = status.equalsIgnoreCase("true") ? 1 : 0;

            long timeInMillis = new Date().getTime();
            GeoHash geoHash = new GeoHash(new GeoLocation(dLat, dLng));
            HashMap<String, Object> geoData = new HashMap<>();
            geoData.put("id", IDVALUE);
            geoData.put("g", geoHash.getGeoHashString());
            geoData.put("l", Arrays.asList(dLat, dLng));
            geoData.put("bearing", bearing);
            geoData.put("updated_at", timeInMillis);
            geoData.put("is_available", true);
            geoData.put("is_active", ActiveStatus);

            Log.e("shareStatus--", "statussss---" + vehId);

            if (!vehId.isEmpty())
                geoData.put("vehicle_type", vehId);

            Log.e("Updatingg--", "ffr");

            ref.child("" + IDVALUE).updateChildren(geoData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateTripStartandArrive(int from) {
        Map<String, Object> statusMap = new HashMap<>();
        if (from == 1)
            statusMap.put("is_cancelled", true);
        else if (from == 2)
            statusMap.put("is_completed", true);
        else if (from == 3)
            statusMap.put("trip_arrived", "1");
        else
            statusMap.put("trip_start", "1");
        requestRef.child(sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID)).updateChildren(statusMap);
    }

    public static void setTripLocation(String driverLocationStr, String s) {


        try {
            final JSONObject jTripLocation = new JSONObject(driverLocationStr);
            final JSONArray locArray = jTripLocation.getJSONArray("lat_lng_array");

            final String requestId = jTripLocation.getString("request_id");
            DatabaseReference tripRef = requestRef.child(requestId);
            tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (Objects.requireNonNull(ds.getKey()).equalsIgnoreCase("lat_lng_array")) {
                                String jsonStr = new Gson().toJson(ds.getValue());
                                jsonObject.put("lat_lng_array", new JSONArray(jsonStr));
                            } else {
                                jsonObject.put(ds.getKey(), ds.getValue());
                            }
                        }

                     //   Log.e("xxxListener", "list--" + jsonObject);

                        JSONArray distArray;
                        if (jsonObject.has("lat_lng_array")) {
                            JSONArray remoteLatLngArray = jsonObject.getJSONArray("lat_lng_array");

                            /* adding local data to firebase if exists */
                            String request_ID = jTripLocation.getString(Constants.NetworkParameters.request_id);
                            String key = request_ID + "_" + SharedPrefence.isOnline;
                            String offlineStr = sharedPrefence.Getvalue(key);
                            Log.d("xxxSocketHelp", "onDataChange: offlineStr == "+offlineStr);
                            if (!offlineStr.equals("")) {
                                JSONArray jOfflineArr = new JSONArray(offlineStr);
                                if (jOfflineArr.length() > 0) {
                                    for (int i = 0; i < jOfflineArr.length(); i++) {
                                        JSONObject jOfflineObj = jOfflineArr.getJSONObject(i);
                                        if (!ifDataExists(remoteLatLngArray, "lat", jOfflineObj.getString("lat")) &&
                                                !ifDataExists(remoteLatLngArray, "lng", jOfflineObj.getString("lng"))) {
                                            remoteLatLngArray.put(jOfflineObj);
                                            Log.i("xxxSocketTriggering", "Adding offline object=="+jOfflineObj);
                                        }
                                    }
                                }
                            }
                            /* adding local data to firebase if exists */

                            if (locArray.length() > 0) {
                                JSONObject jFirst = locArray.getJSONObject(locArray.length() - 1);
                                if (!ifDataExists(remoteLatLngArray, "lat", jFirst.getString("lat")) &&
                                        !ifDataExists(remoteLatLngArray, "lng", jFirst.getString("lng"))) {
                                    remoteLatLngArray.put(jFirst);
                                }
                            }
                            jTripLocation.put(Constants.NetworkParameters.LAT_LNG_ARRAY, remoteLatLngArray);
                            distArray = remoteLatLngArray;
                        } else {
                            jTripLocation.put(Constants.NetworkParameters.LAT_LNG_ARRAY, locArray);
                            distArray = locArray;
                        }
                        jTripLocation.put("success", true);
                        jTripLocation.put("show_cancellation_reason", true);
                        jTripLocation.put("distancee", getDistanceFrom(distArray));
                        Log.d("xxxSocketTriggering", "onDataChange: Distance "+getDistanceFrom(distArray));
                        Log.d("xxxSocketTriggering", "onDataChange: distArray "+distArray);

                        if (!sharedPrefence.Getvalue(SharedPrefence.REQUEST_ID).isEmpty()) {
                            jTripLocation.put("is_cancelled", false);
                            jTripLocation.put("is_completed", false);
                        }
                        updateStringToFirebase(requestId, jTripLocation.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("xxSocketTriggering", "E1: " + e.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SocketTriggering", "E2: " + e.toString());
        }
    }

    public static void updateStringToFirebase(String requestId, final String str) {
       // Log.e("xxxupdateString----", "string---" + str);

        HashMap<String, Object> result = new Gson().fromJson(str, HashMap.class);
        requestRef.child(requestId).updateChildren(result, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference r_ref) {
                if (error == null) {
                    try {
                        JSONObject jTrip = new JSONObject(str);
                        String request_ID = jTrip.getString(Constants.NetworkParameters.request_id);
                        String key = request_ID + "_" + SharedPrefence.isOnline;
                        if (sharedPrefence.containsKey(key)) {
                            sharedPrefence.deleteKey(key);
                            Log.i("SocketTriggering", "Deleting offline data");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("SocketTriggering", e.toString());
                    }

                    /* Set driver status as not available */
                    Map<String, Object> additionalData = new HashMap<>();
                    additionalData.put("is_available", false);

                    ref.child(sharedPrefence.ID).updateChildren(additionalData);
                }
            }
        });
       // Log.i("SocketTriggering", "EnteredTrip" + "--------------" + str);
    }

    private static boolean ifDataExists(JSONArray jsonArray, String key, String data) {
        return jsonArray.toString().contains("\"" + key + "\":\"" + data + "\"");
    }

    private static double getDistanceFrom(JSONArray locArray) {
        double distance = 0;
        try {
            List<LatLng> list = new ArrayList<>();
            for (int i = 0; i < locArray.length(); i++) {
                JSONObject jObj = locArray.getJSONObject(i);
                double lat = jObj.getDouble("lat");
                double lng = jObj.getDouble("lng");
                LatLng latLng = new LatLng(lat, lng);
                list.add(latLng);
            }
            distance = SphericalUtil.computeLength(list);

            socketDataReceiver.updateTripDistance(distance / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xxxSockethelp", "getDistanceFrom: Error==>"+e.getMessage());
        }
        return distance / 1000;
    }

    public static boolean isSocketConnected() {
        return mqttAndroidClient.isConnected();
    }

    /**
     * This is a interface of Initiating the socket.
     */
    public interface SocketListener {

        boolean isNetworkConnected();

        void OnConnect();

        void OnDisconnect();

        void OnConnectError();

        void onCreateRequest(String s);

        void RequestHandler(String toString);

        void updateTripDistance(double v);

        void ApprovalStatus(String toString);
        void ReceivedChatStatus(String toString);

    }

}
