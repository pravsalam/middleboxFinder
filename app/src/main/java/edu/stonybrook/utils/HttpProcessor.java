package edu.stonybrook.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by praveenkumaralam on 10/11/15.
 */
public class HttpProcessor {
    HttpURLConnection httpCon;
    public HttpProcessor(String url) throws IOException {
        Log.i("INFO","HTTP Processor constructor");
        try {
            URL urlObj = new URL(url);
            httpCon = (HttpURLConnection)urlObj.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Accept", "*/*");
            httpCon.setConnectTimeout(1500);
        }catch(MalformedURLException e){
            throw e;
        }catch(ProtocolException e){
            throw e;
        }catch(IOException e){
            throw e;
        }
    }
    public void setHttpProperty(String pname, String value){
        httpCon.setRequestProperty(pname, value);
    }
    public String processConnection(boolean readErrorStream) throws IOException {
        Log.i("INFO","Process Connection ");
        try{
            Integer responseCode = httpCon.getResponseCode();
            Log.i("INFO", " in HttpProcessor class"+responseCode.toString());
            InputStreamReader streamReader;

            if(readErrorStream) streamReader = new InputStreamReader(httpCon.getErrorStream());
            else streamReader = new InputStreamReader(httpCon.getInputStream());

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                Log.i("Praveen","response = "+inputLine.toString());
                response.append(inputLine);
            }
            in.close();
            httpCon.disconnect();
            Log.i("Praveen"," process conn "+response.toString());
            return response.toString();
        }catch(IOException e){
            throw e;
        }
    }
}
