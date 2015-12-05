package edu.stonybrook.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by praveenkumaralam on 11/13/15.
 */
public class DeviceLocationInfo {
    //Location mLocation;
    Context mContext;
    public DeviceLocationInfo(Context context){
        mContext = context;
    }
    public Location getLocation(){
        LocationManager locMan = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            boolean isGpsEnabled = false;
            boolean isNetworkLocEnabled = false;
            Location location = null;
            isGpsEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkLocEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGpsEnabled && !isNetworkLocEnabled){
                Log.i("Praveen", "No permission is allowed to access data");
                return null;
            }
            if(isNetworkLocEnabled){
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60*1000, 10,new customLocationListener());
                location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){
                    Log.i("Praveen","Location information from Network");
                    Log.i("Praveen","longitude "+Double.toString(location.getLongitude()));
                    Log.i("Praveen"," Lattitude "+Double.toString(location.getLatitude()));
                    return  location;
                }
            }
            if(isGpsEnabled){
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,60*1000, 10,new customLocationListener());
                location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    Log.i("Praveen","longitude "+Double.toString(location.getLongitude()));
                    Log.i("Praveen"," Lattitude "+Double.toString(location.getLatitude()));
                    return location;
                }
            }

        }else{
            Log.i("Praveen"," Access is not granted");
        }
        return null;
    }
    private class customLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
