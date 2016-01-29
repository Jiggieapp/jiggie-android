package com.android.jiggie.component;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;

import com.android.jiggie.App;

/**
 * Created by rangg on 18/11/2015.
 */
public class LocationHandler extends SimpleLocationListener {
    private static LocationHandler instance;
    public static LocationHandler getInstance() { return instance != null ? instance : (instance = new LocationHandler()); }

    private Handler handler;
    private Runnable runnable;

    private LocationHandler() {
        this.handler = new Handler(App.getInstance().getMainLooper());
    }

    @SuppressWarnings("ResourceType")
    public Location getCurrentLocation() {
        final LocationManager lm = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            final Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            final String provider = lm.getBestProvider(criteria, true);
            location = lm.getLastKnownLocation(provider);
        }

        if (location == null)
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return location;
    }

    @SuppressWarnings("ResourceType")
    public void requestLocationUpdate() {
        final LocationManager lm = (LocationManager)App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        final boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(isGPSEnabled ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        if (this.runnable != null)
            this.handler.removeCallbacks(this.runnable);

        this.handler.postDelayed(this.runnable = new Runnable() {
            @Override
            public void run() { lm.removeUpdates(LocationHandler.this); }
        }, 30000);
    }

    @Override
    @SuppressWarnings("ResourceType")
    public void onLocationChanged(Location location) {
        // we got accurate enough location with 10 meters radius.
        if (location.getAccuracy() == 10) {
            final LocationManager lm = (LocationManager)App.getInstance().getSystemService(Context.LOCATION_SERVICE);
            lm.removeUpdates(this);
        }
    }

    @SuppressWarnings("ResourceType")
    public void addLocationUpdateListener(LocationListener locationListener) {
        final LocationManager lm = (LocationManager)App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        final boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(isGPSEnabled ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    }

    @SuppressWarnings("ResourceType")
    public void removeLocationUpdateListner(LocationListener locationListener) {
        final LocationManager lm = (LocationManager)App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(locationListener);
    }
}
