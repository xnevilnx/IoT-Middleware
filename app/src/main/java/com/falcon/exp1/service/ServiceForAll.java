package com.falcon.exp1.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Falcon on 11/25/2016.
 */

public class ServiceForAll extends Service{

    public static final String TAG = "ServiceForAll";
    private ServiceForRoomEventsListener mServiceForRoomEventsListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
        Toast.makeText(this, "service done", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public ServiceForAll getService() {
            return ServiceForAll.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();

    public void setListener(ServiceForRoomEventsListener mListener) {
        mServiceForRoomEventsListener = mListener;
    }  //监听是否有人要Service

    public void getRoomJson() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://140.127.74.190:8080/exp1/getSensorDataService.php?deviceID=device001")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "126e3429-9ad7-63f7-fc76-5da962d22bcd")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("Connection Failed", "onFailure");
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                String roomJsonData = response.body().string();
                Log.e("Connected Room Json", roomJsonData);

                try {
                    JSONObject Jobject = new JSONObject(roomJsonData);
                    JSONArray Jarray = Jobject.getJSONArray("room");
                    JSONObject object_Temperature_Humidity = Jarray.getJSONObject(0);
                    String tempTemperatureJson = object_Temperature_Humidity.getString("temperature");
                    String tempHumidityJson = object_Temperature_Humidity.getString("humidity");
                    JSONObject object_brightness = Jarray.getJSONObject(1);
                    String tempBrightnessJson = object_brightness.getString("brightness");
                    JSONObject object_Gasdata = Jarray.getJSONObject(2);
                    String tempGasDataJson = object_Gasdata.getString("gasData");
                    mServiceForRoomEventsListener.getRoomInfo(tempTemperatureJson, tempHumidityJson, tempBrightnessJson, tempGasDataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
