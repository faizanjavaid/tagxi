package com.tyt.driver.utilz;

/**
 * Created by root on 11/16/17.
 */

public class SocketMessageModel {

    public String id, token, client_id, client_token,pickup_address;
    public Double pick_lat,pick_lng;

    public SocketMessageModel build() {
        SocketMessageModel message = new SocketMessageModel();
        message.id = id;
        message.token = token;
        message.client_id = client_id;
        message.client_token = client_token;
        message.pick_lat = pick_lat;
        message.pick_lng = pick_lng;
        message.pickup_address = pickup_address;
        return message;
    }
}
