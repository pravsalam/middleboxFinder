package edu.stonybrook.middleboxes;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by praveenkumaralam on 9/22/15.
 */
public class HTMLTests {
    Observable<String> performHTTP404(final String server, final String port){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    String url="http://";
                    url+=server+":"+port;
                    Log.i("Praveen","Url= "+url);
                    URL urlObj = new URL(url);
                    HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
                    httpCon.setRequestMethod("GET");
                    httpCon.setRequestProperty("Accept","*/*");
                    httpCon.setRequestProperty("Test-Type","Http404");
                    httpCon.setConnectTimeout(1500);

                    Integer responseCode = httpCon.getResponseCode();
                    Log.i("INFO",responseCode.toString());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpCon.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Log.i("INFO", response.toString());
                    in.close();
                    httpCon.disconnect();
                    if(response.toString().equals("HTTP_404"))
                        subscriber.onNext("Pass");
                    else
                        subscriber.onNext("Fail");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
