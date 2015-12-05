package edu.stonybrook.middleboxes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import edu.stonybrook.utils.DeviceInfo;
import edu.stonybrook.utils.HttpProcessor;
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
    public Observable<String> performHTTP404(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    HttpProcessor httpProcessor = new HttpProcessor(url);
                    httpProcessor.setHttpProperty("Test-Type", "Http404");

                    String reply = httpProcessor.processConnection(true);
                    Log.i("Praveen","Reply  = "+reply);
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
                try{
                    HttpProcessor httpProcessor = new HttpProcessor(url);
                    httpProcessor.setHttpProperty("Host", "www.yahoo.com");
                    httpProcessor.setHttpProperty("Test-Type","HeaderHostTest");
                    String reply = httpProcessor.processConnection(false);
                    Log.i("Praveen","Reply  = "+reply);
                    if(reply.equals("MIDDLEBOX_SERVER"))
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
                String useragent =  mContext.getResources().getString(R.string.HTTP_TEST_USER_AGENT);

                try{
                    HttpProcessor httpProcessor = new HttpProcessor(url);
                    httpProcessor.setHttpProperty("User-Agent",useragent);
                    httpProcessor.setHttpProperty("Test-Type","HeaderTest");
                    String reply = httpProcessor.processConnection(false);
                    Log.i("Praveen","Reply  = "+reply);
                    if(reply.equals("HTTP_HEADER_OK"))
                    {
                        subscriber.onNext("Pass");
                    }
                    else if( reply.equals("HTTP_HEADER_MANIPULATED")){
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
