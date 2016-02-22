package com.jiggie.android.component;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 11/01/2016.
 */
public class SimpleJSONObject extends JSONObject {
    public SimpleJSONObject() { }
    public SimpleJSONObject(String initiateKey, String initiateValue) {
        this.putString(initiateKey, initiateValue);
    }

    public JSONObject putString(String key, String value) {
        try {
            return super.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject putBoolean(String key, boolean value) {
        try {
            return super.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
