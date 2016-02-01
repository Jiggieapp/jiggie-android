package com.jiggie.android.model;

import org.json.JSONObject;

/**
 * Created by rangg on 11/11/2015.
 */
public class Venue extends IdModel {
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_LONG = "long";
    public static final String FIELD_LAT = "lat";

    private String address;
    private double latitude;
    private double longitude;

    public Venue(JSONObject json) {
        super(json);
        this.address = json.optString(FIELD_ADDRESS);
        this.latitude = json.optDouble(FIELD_LAT);
        this.longitude = json.optDouble(FIELD_LONG);
    }

    public String getAddress() { return this.address; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }
}
