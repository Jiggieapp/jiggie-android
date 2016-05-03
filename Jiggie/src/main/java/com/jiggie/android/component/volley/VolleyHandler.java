package com.jiggie.android.component.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by rangg on 28/09/2015.
 */
public class VolleyHandler {
    public static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;

    private RequestQueue volleyRequestQueue;
    private DefaultRetryPolicy retryPolicy;
    private String serverHost;

    private static VolleyHandler instance;
    public static VolleyHandler getInstance() { return instance != null ? instance : (instance = new VolleyHandler() ); }

    public VolleyHandler() { this.retryPolicy = new DefaultRetryPolicy(15000, 1, 1); }

    public <T> void createVolleyRequest(String subUrl, JSONObject json, VolleyRequestListener<T, JSONObject> listener) {
        this.addRequest(new VolleyJsonRequest<>(json == null ? Request.Method.GET : Request.Method.POST, this.getServerHost() + subUrl, json, listener));
    }

    public <T> void createVolleyRequest(String subURL, Model model, VolleyRequestListener<T, JSONObject> listener) {
        try {
            final JSONObject json = model == null ? null : model.toJsonObject();
            this.createVolleyRequest(subURL, json, listener);
        } catch (JSONException e) {
            listener.onErrorResponse(new VolleyError(e));
        }
    }

    public <T> void createVolleyRequest(String subURL, VolleyRequestListener<T, JSONObject> listener) {
        this.createVolleyRequest(subURL, (JSONObject) null, listener);
    }

    public <T> void createVolleyArrayRequest(String subUrl, JSONObject json, VolleyRequestListener<T, JSONArray> listener) {
        this.addRequest(new VolleyJsonArrayRequest<>(json == null ? Request.Method.GET : Request.Method.POST, this.getServerHost() + subUrl, json, listener));
    }

    public <T> void createVolleyArrayRequest(String subURL, VolleyRequestListener<T, JSONArray> listener) {
        this.createVolleyArrayRequest(subURL, null, listener);
    }

    private void addRequest(Request request) {
        request.setRetryPolicy(this.retryPolicy);
        this.getVolleyRequestQueue().add(request);
    }

    public static JSONObject getData(JSONObject json) { return json == null ? null : json.optJSONObject("data"); }

    public static JSONArray getArrayData(JSONObject json) { return json == null ? null : json.optJSONArray("data"); }

    public static String parseError(Context context, VolleyError error) {
        if (error instanceof TimeoutError)
            return context.getString(R.string.error_could_not_connect_server);
        else if ((error instanceof ServerError) || (error instanceof AuthFailureError))
            return context.getString(R.string.error_connection_failed);
        else if (error instanceof NetworkError)
            return context.getString(R.string.error_no_connection);
        else
            return context.getString(R.string.error_unknown);
    }

    /*public String getServerHost() {
        return this.serverHost == null ? (this.serverHost = App.getInstance().getString(R.string.server_host)) : this.serverHost;
    }*/

    public String getServerHost() {
        return this.serverHost == null ? (this.serverHost = Utils.BASE_URL+"app/v3/") : this.serverHost;
    }

    //Added by Aga----
    /*public String getServerHost2() {
        return this.serverHost == null ? (this.serverHost = "http://api-dev.jiggieapp.com/app/v3/") : this.serverHost;
    }*/
    //-------

    public RequestQueue getVolleyRequestQueue() {
        if (this.volleyRequestQueue == null) {
            this.volleyRequestQueue = new RequestQueue(new DiskBasedCache(new File(App.getInstance().getCachePath("volley")), DEFAULT_DISK_USAGE_BYTES), new BasicNetwork(new HurlStack()));
            this.volleyRequestQueue.start();
        }
        return this.volleyRequestQueue;
    }
}
