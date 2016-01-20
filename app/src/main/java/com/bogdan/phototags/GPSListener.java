package com.bogdan.phototags;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.bogdan.phototags.activities.MainActivity;

/**
 * Created by bogdan on 18.01.2016.
 */
public class GPSListener implements LocationListener {

    public static void getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new GPSListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            MainActivity.latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude() + "";
            MainActivity.longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude() + "";
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        MainActivity.latitude = location.getLatitude() + "";
        MainActivity.longitude = location.getLongitude() + "";

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
