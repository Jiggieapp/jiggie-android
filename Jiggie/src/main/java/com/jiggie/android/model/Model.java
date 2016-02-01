package com.jiggie.android.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 22/10/2015.
 */
public interface Model {
    JSONObject toJsonObject() throws JSONException;
}
