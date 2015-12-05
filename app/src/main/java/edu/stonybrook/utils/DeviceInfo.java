package edu.stonybrook.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by praveenkumaralam on 10/10/15.
 */
public class DeviceInfo {
    private String deviceID;
    private String deviceOperator;
    private String deviceIp;
    private double longitude;
    private double latitude;

    public DeviceInfo(Context context) {
        //we want fresh copy of information for every invocation of this object.
        // IP address of the phone can change while the tests are in progress
        deviceID = null;
        deviceOperator = null;
        deviceIp = null;
        ;
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = conMan.getAllNetworks();
        for (Network network : networks) {
            NetworkInfo networkInfo = conMan.getNetworkInfo(network);
            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                Log.i("Praveen", "Netowk type " + networkInfo.getTypeName());
                if (networkInfo.getTypeName().equals("WIFI")) {
                    deviceOperator = "WIFI";
                } else {
                    TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                    deviceOperator = manager.getNetworkOperator().replaceAll("\\s", "");
                }
            }
        }

        try {
            for (Enumeration<NetworkInterface>
                 en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress>
                     enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String someString = inetAddress.getHostAddress().toString();
                        if (!inetAddress.isLinkLocalAddress()) {
                            deviceIp = someString;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
    public String getDeviceID(){
        return deviceID;
    }
    public String getDeviceOperator(){
        return deviceOperator;
    }
    public String getDeviceIp(){
        return deviceIp;
    }

}
