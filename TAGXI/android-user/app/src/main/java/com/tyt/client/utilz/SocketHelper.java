package com.tyt.client.utilz;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tyt.client.app.MyApp;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

/**
 * This class contains all the MQTT intiation , callbacks and Socket listeners and methods
* */

public class SocketHelper {
    static Socket mSocket;
    static SharedPrefence sharedPrefence;
    static String TAG = "SocketHelper";
    static SocketListener socketDataReceiver;
    static String pendingTypeData, typesDriversData = null;

    private static String MQTT_URL = "tcp://34.239.45.247:1883";
    static MqttAndroidClient mqttAndroidClient;

    static Long lastSocketConnected, isDriversLastListed;
    static boolean isInsideTrip = false;
    static String userID = "";

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

        userID = sharedPrefence.Getvalue(SharedPrefence.ID);

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
                    Log.v(TAG, "Connection complete");
                    subscribeToTopic(Constants.MqttEvents.TripStatus + "_" + userID);
                    subscribeToTopic(Constants.MqttEvents.received_chat_status + "_" + userID);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.v(TAG, "Connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    Log.d("xxxSocketHelperTAG", "messageArrived() topic = [" + topic + userID + "], message = [" + message.toString() + "]");
                    if (topic.equalsIgnoreCase(Constants.MqttEvents.TripStatus + "_" + userID))
                        socketDataReceiver.TripStatus(message.toString());
                    else if (topic.equalsIgnoreCase(Constants.MqttEvents.received_chat_status + "_" + userID))
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

    private static void unSubscribe() {
        try {
            mqttAndroidClient.unsubscribe(Constants.MqttEvents.TripStatus + "_" + userID);
        } catch (MqttException e) {
            e.printStackTrace();
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
        mSocket.off("get_near_drivers", NearDrivers);

    }

    /**
     * Update the available drivers and Formated the driver for the Logged user.
     */
    private static Emitter.Listener NearDrivers = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (socketDataReceiver != null)
                socketDataReceiver.onLoadDrivers();
        }
    };

    private static Emitter.Listener TripStatus = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("TripStatus---", "status");
            if (socketDataReceiver != null)
                socketDataReceiver.TripStatus(args[0].toString());
        }
    };


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

    /**
     * Through the error of socket connections.
     */
    private static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "onConnectError");
            if (socketDataReceiver != null)
                socketDataReceiver.OnConnectError();
        }
    };


    public static void GetDriversList(LatLng latLng) {
        if (mSocket != null) {
            Log.i("SocketTriggering", "get_rider_info Data--------Socket=" + mSocket.connected());
            if (!mSocket.connected()) {
                SetSocketListener();
            } else if (mSocket.connected()) {
                JSONObject object = new JSONObject();
                try {
                    object.put(Constants.NetworkParameters.id, sharedPrefence.Getvalue(SharedPrefence.ID));
                    object.put(Constants.NetworkParameters.lat, latLng.latitude);
                    object.put(Constants.NetworkParameters.lng, latLng.longitude);
                    //object.put("socket_id", mSocket.id() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("NearDrivers", object.toString());

                Log.i("SocketTriggering", "NearDrivers=" + object.toString() + " Connected=" + mSocket.connected());

            }
        }
    }

    /**
     * This is a interface of Initiating the socket.
     */
    public interface SocketListener {

        boolean isNetworkConnected();

        void OnConnect();

        void OnDisconnect();

        void OnConnectError();

        void onLoadDrivers();

        void TripStatus(String s);

        void ApprovalStatus(String toString);

        void ReceivedChatStatus(String toString);
    }
}
