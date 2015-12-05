package edu.stonybrook.middleboxes;

import android.content.Context;
import android.view.View;

import java.io.IOException;
import java.net.Socket;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by praveenkumaralam on 10/11/15.
 */
public class TCPTests {
    Context mContext;
    public TCPTests(View view){
        mContext = view.getContext();
    }
    public Observable performTCPResetTest(final String serverIp, final String port){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Socket s = new Socket(serverIp, Integer.parseInt(port));
                    subscriber.onNext("Fail");
                }catch(IOException e){
                    subscriber.onNext("Pass");
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
