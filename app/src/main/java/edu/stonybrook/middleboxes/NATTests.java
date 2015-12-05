package edu.stonybrook.middleboxes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.stonybrook.utils.DeviceInfo;
import edu.stonybrook.utils.HttpProcessor;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by praveenkumaralam on 10/11/15.
 */
public class NATTests {
    private View mView;
    private Context mContext;
    public NATTests(View view){
        mContext = view.getContext();
        mView = view;
    }
    public Observable<String> performNATExistTest(final String url){
        Log.i("INFO","NAT TEST url"+url);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    HttpProcessor httpProcessor = new HttpProcessor(url);
                    httpProcessor.setHttpProperty("Test-Type","NatTest");
                    String reply = httpProcessor.processConnection(false);
                    Log.i("INFO", " nat present reply" + reply);
                    JSONObject parentJsonObj = new JSONObject(reply);
                    JSONObject childJsonObj = (JSONObject)parentJsonObj.get("NatProxy");
                    if(((String)childJsonObj.get("NatPresent")).equals("Yes")){
                        subscriber.onNext("Fail");
                    }
                    else{
                        subscriber.onNext("Pass");
                    }
                }catch(IOException e){
                    subscriber.onNext("Internal Error");
                }catch(JSONException e){
                    subscriber.onNext("Internal Error");
                }
                finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
    public Observable<String> performIPFlippingTest(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    int numOfTries = 5;
                    boolean testFailed = false;
                    DeviceInfo phoneInfo = new DeviceInfo(mView.getContext());
                    String localIp = phoneInfo.getDeviceIp();
                    String publicIp ="";
                    while(numOfTries >0){
                        HttpProcessor httpProcessor = new HttpProcessor(url);
                        httpProcessor.setHttpProperty("Test-Type","NatTest");
                        String reply = httpProcessor.processConnection(false);
                        Log.i("INFO", " nat present reply" + reply);
                        JSONObject parentJsonObj = new JSONObject(reply);
                        JSONObject childJsonObj = (JSONObject)parentJsonObj.get("NatProxy");
                        if(childJsonObj.get("NatPresent").equals("yes")){
                            //Nat Proxy is present
                            String natIp = (String)childJsonObj.get("clientIP");
                            if(!publicIp.equals("")){
                                if(!publicIp.equals(natIp)){
                                    subscriber.onNext("Fail");
                                    testFailed = true;

                                }
                            }else {
                                publicIp =natIp;
                            }
                        }
                        numOfTries--;
                    }
                    if(!testFailed) subscriber.onNext("Pass");

                }catch(IOException e){
                    subscriber.onNext("Internal Error");
                }catch(JSONException e){
                    subscriber.onNext("Internal Error");
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
