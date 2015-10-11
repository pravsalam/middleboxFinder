package edu.stonybrook.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by praveenkumaralam on 10/10/15.
 */
public class DeviceInfo {
    private String deviceID;
    private String deviceOperator;
    private String deviceIp;
    public DeviceInfo(View view){
        //we want fresh copy of information for every invocation of this object.
        // IP address of the phone can change while the tests are in progress
        deviceID = null;
        deviceOperator = null;
        deviceIp = null;
        Context context = view.getContext();
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        deviceOperator =manager.getNetworkOperator().replaceAll("\\s","");
        try {
            for (Enumeration<NetworkInterface>
                 en =  NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress>
                     enumIpAddr = intf.getInetAddresses();enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        String someString = inetAddress.getHostAddress().toString();
                        if(!inetAddress.isLinkLocalAddress())
                        {
                            deviceIp = someString;
                        }
                    }
                }
            }
        }catch (SocketException e){
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
