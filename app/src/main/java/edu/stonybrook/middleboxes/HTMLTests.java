package edu.stonybrook.middleboxes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import edu.stonybrook.utils.DeviceInfo;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by praveenkumaralam on 9/22/15.
 */
public class HTMLTests {
    Context mContext;
    public HTMLTests(View view){
        mContext  = view.getContext();
    }
    private HttpURLConnection httpConnBuilder(String url){
        try{
            URL urlObj = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlObj.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Accept", "*/*");
            httpCon.setConnectTimeout(1500);
            return httpCon;
        }catch(MalformedURLException e){
            return null;
        }catch(IOException e){
            return null;
        }
    }
    private String processConnection(HttpURLConnection httpCon) throws IOException{
        try{
            Integer responseCode = httpCon.getResponseCode();
            Log.i("INFO",responseCode.toString());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpCon.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            httpCon.disconnect();
            return response.toString();
        }catch(IOException e){
            throw e;
        }
    }
    public Observable<String> performHTTP404(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    HttpURLConnection httpCon = httpConnBuilder(url);
                    httpCon.setRequestProperty("Test-Type", "Http404");

                    String reply = processConnection(httpCon);
                    if(reply.equals("HTTP_404"))
                        subscriber.onNext("Pass");
                    else
                        subscriber.onNext("Fail");
                }catch(IOException e){
                    subscriber.onNext("Internal Error");
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
    Observable<String> performHTTPCustomHost(final String url){
        return Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpURLConnection httpCon = httpConnBuilder(url);
                httpCon.setRequestProperty("Host", "www.yahoo.com");
                httpCon.setRequestProperty("Test-Type","HeaderHostTest");
                try{
                    Integer responseCode = httpCon.getResponseCode();
                    Log.i("INFO",responseCode.toString());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpCon.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    httpCon.disconnect();
                    Log.i("INFO", response.toString());
                    //return response.toString();
                    if(response.toString().equals("MIDDLEBOX_SERVER"))
                    {
                        subscriber.onNext("Pass");
                    }
                    else{
                        subscriber.onNext("Fail");
                    }
                    subscriber.onCompleted();
                }catch(IOException e){
                    subscriber.onNext("Internal Error");
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
    Observable<String> performHTTPUserAgent(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpURLConnection httpCon = httpConnBuilder(url);
                String useragent =  mContext.getResources().getString(R.string.HTTP_TEST_USER_AGENT);
                httpCon.setRequestProperty("User-Agent",useragent);
                httpCon.setRequestProperty("Test-Type","HeaderTest");
                try{
                    int responseCode = httpCon.getResponseCode();
                    Integer code = responseCode;
                    Log.i("INFO",code.toString());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpCon.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    httpCon.disconnect();
                    Log.i("INFO",response.toString());
                    //return response.toString();
                    if(response.toString().equals("HTTP_HEADER_OK"))
                    {
                        subscriber.onNext("Pass");
                    }
                    else if( response.toString().equals("HTTP_HEADER_MANIPULATED")){
                        subscriber.onNext("Fail");
                    }
                    else{
                        subscriber.onNext("Internal Error");
                    }
                    subscriber.onCompleted();
                }catch(IOException e) {
                    subscriber.onNext("Internal Error");
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
