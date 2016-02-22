package com.jiggie.android.model;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 03/11/2015.
 */
public class IdModel implements Model {
    public static final String FIELD_ID = "_id";

    private String id;

    public IdModel() { }
    public IdModel(String id) { this.id = id; }
    public IdModel(JSONObject json) { this.id = json.optString(FIELD_ID); }
    protected IdModel(Parcel parcel) { this.id = parcel.readString(); }

    protected void writeToParcel(Parcel parcel) { parcel.writeString(this.id); }

    public String getId() { return id; }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put(FIELD_ID, this.id);
        return json;
    }
}
