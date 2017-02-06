package com.falcon.exp1;

import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.falcon.exp1.service.ServiceForAll;
import com.falcon.exp1.service.ServiceForRoomEventsListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Falcon on 11/15/2016.
 */

public class RoomFragment extends Fragment{

    View myView;
    String tempTemperature, tempHumidity, tempbrightness, tempgasData;
    TextView roomtemperatureValue, roomHumidityValue, roomBrightnessValue, roomGasDataValue;
    ServiceForAll serviceForAll;
    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.room_layout, container, false);


        final Intent intent = new Intent();
        intent.setAction("com.falcon.exp1.service.ServiceForAll");
        final Intent eintent = new Intent(createExplicitFromImplicitIntent(getActivity(),intent));
        getActivity().bindService(eintent,conn, Service.BIND_AUTO_CREATE);


        handler.postDelayed(updateTimer, 100);
        handler.postDelayed(updateRoomInfo, 100);

        return myView;

    }

    @Override
    public void onDestroy() {
        handler.removeMessages(0);
        super.onDestroy();
    }

    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    ServiceForRoomEventsListener receiveListener = new ServiceForRoomEventsListener() { //监听service回传的资料
        @Override
        public void getRoomInfo(String temperature, String humidity, String brightness, String gasData) {
            tempTemperature = temperature;
            tempHumidity = humidity;
            tempbrightness = brightness;
            tempgasData = gasData;
        }
    };



    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceForAll = ((ServiceForAll.LocalBinder) service).getService();
            serviceForAll.setListener(receiveListener);
            serviceForAll.getRoomJson();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            serviceForAll=null;
        }

    };

    private Runnable updateRoomInfo = new Runnable() {
        @Override
        public void run() {
            roomtemperatureValue = (TextView) myView.findViewById(R.id.roomTemperatureValueTv);
            roomHumidityValue = (TextView) myView.findViewById(R.id.roomHumidityValueTv);
            roomBrightnessValue = (TextView) myView.findViewById(R.id.roomBrightnessValueTv);
            roomGasDataValue = (TextView) myView.findViewById(R.id.roomGasDataValueTv);
            handler.postDelayed(updateRoomInfo, 100);
            roomtemperatureValue.setText(tempTemperature + "°C");
            roomHumidityValue.setText(tempHumidity + "%RH");
            roomBrightnessValue.setText(tempbrightness + "cd/m^2");
            int gasConvert = Integer.parseInt(tempgasData);

            if(gasConvert < 400) {
                roomGasDataValue.setText(tempgasData);
            }else{
                roomGasDataValue.setText("Danger! Gas ppm is "+ gasConvert);
            }

        }
    };


    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView dateTime = (TextView) myView.findViewById(R.id.datetimeTextView);
            handler.postDelayed(updateTimer, 100);
            SimpleDateFormat nowdate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            nowdate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            final String sdate = nowdate.format(new java.util.Date());
            dateTime.setText(sdate);
        }
    };
}
