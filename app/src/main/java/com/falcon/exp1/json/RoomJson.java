package com.falcon.exp1.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pawl on 2016/11/26.
 */

public class RoomJson {

    @SerializedName("temperature")
    private String roomTemperatureJson;


    public String getroomTemperatureJson() {
        return roomTemperatureJson;
    }
}
