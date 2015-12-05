package edu.stonybrook.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by praveenkumaralam on 10/10/15.
 */
public class UrlBuilder {
    String serverIP;
    String serverPort;
    String serverUrl;
    public UrlBuilder(String sIP,String sPort,Context context){
        serverIP = sIP;
        serverPort = sPort;
        DeviceInfo phoneInfo = new DeviceInfo(context);
        serverUrl ="http://"+serverIP;
        serverUrl +=":"+serverPort;
        serverUrl +="/?"+"unique_id="+phoneInfo.getDeviceID();
        serverUrl +="&network_operator="+phoneInfo.getDeviceOperator();
        serverUrl +="&local_ip="+phoneInfo.getDeviceIp();
    }
    public String getServerUrl(){
        return serverUrl;
    }
}
