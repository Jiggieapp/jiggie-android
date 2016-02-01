package com.jiggie.android.model;

import org.json.JSONObject;

/**
 * Created by rangg on 07/01/2016.
 */
public class ShareLink {
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_URL = "url";

    private String message;
    private String url;

    public ShareLink(JSONObject json) {
        this.message = json.optString(FIELD_MESSAGE);
        this.url = json.optString(FIELD_URL);
    }

    public String getMessage() { return message; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return String.format("%s\n\n%s", this.message, this.url);
    }
}
