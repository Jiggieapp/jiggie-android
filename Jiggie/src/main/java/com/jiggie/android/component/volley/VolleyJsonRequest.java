package com.jiggie.android.component.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by rangg on 28/09/2015.
 */
public class VolleyJsonRequest<T> extends JsonRequest<JSONObject> {
    private VolleyRequestListener<T, JSONObject> listener;
    private T value;

    public VolleyJsonRequest(int method, String url, JSONObject body, final VolleyRequestListener<T, JSONObject> listener) {
        super(method, url, body == null ? null : body.toString(), null, listener);
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.listener.onResponseCompleted(this.value);
    }

    @Override
    public String getBodyContentType() { return "application/json"; }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            final JSONObject json = new JSONObject(jsonString);
            this.value = this.listener.onResponseAsync(json);
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}