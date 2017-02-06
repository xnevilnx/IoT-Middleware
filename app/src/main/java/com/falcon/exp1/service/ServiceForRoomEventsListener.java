package com.falcon.exp1.service;

/**
 * Created by Pawl on 2016/11/26.
 */

public interface ServiceForRoomEventsListener {

    void getRoomInfo(String temperature, String humidity, String brightness, String gasData);

}
