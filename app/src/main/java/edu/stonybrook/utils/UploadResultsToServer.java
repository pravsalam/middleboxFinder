package edu.stonybrook.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
/**
 * Created by praveenkumaralam on 10/26/15.
 */
public class UploadResultsToServer {
    public Observable<String> uploadResults(final HashMap<String,String> hashMap,
                                            final Context context,
                                            final String server,
                                            final String port,
                                            final Location location)
    {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("Is it called");
                String url = "http://"+server+":"+port;
                Log.i("Praveen"," url ="+url);
                try {
                    URL urlObj = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)urlObj.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setChunkedStreamingMode(0);
                    DeviceInfo deviceInfo = new DeviceInfo(context);
                    JSONObject jsonObject = new JSONObject();
                    String resultString="";
                    int size = hashMap.size();
                    Log.i("Praveen"," size = "+Integer.toString(size));
                    Log.i("Praveen"," is hashmap empty");
                    for(String key:hashMap.keySet()){
                        String testname = key;
                        Log.i("Praveen"," is this executing "+key);
                        String result = (String)hashMap.get(key);
                        boolean resultBool;
                        if(result.equals("Pass")){
                            resultBool = false;
                        }
                        else resultBool = true;
                        try {
                            jsonObject.put(key,resultBool);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonObject.put("Device ID",deviceInfo.getDeviceID());
                    jsonObject.put("Operator",deviceInfo.getDeviceOperator());
                    jsonObject.put("IP",deviceInfo.getDeviceIp());
                    if(location != null){
                        jsonObject.put("Longitude",location.getLongitude());
                        jsonObject.put("Latitude",location.getLatitude());
                    }
                    else{
                        jsonObject.put("Longitude",0);
                        jsonObject.put("Latitude",0);
                    }
                    resultString = jsonObject.toString();
                    //OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                    PrintStream printStream= new PrintStream(httpURLConnection.getOutputStream());
                    printStream.print(resultString);
                    printStream.close();
                    Log.i("Praveen", " " + resultString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
