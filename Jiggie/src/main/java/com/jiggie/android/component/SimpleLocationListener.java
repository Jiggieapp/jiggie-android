package com.jiggie.android.component;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by rangg on 18/11/2015.
 */
public abstract class SimpleLocationListener implements LocationListener {
    @Override
    public abstract void onLocationChanged(Location location);
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { }
}
