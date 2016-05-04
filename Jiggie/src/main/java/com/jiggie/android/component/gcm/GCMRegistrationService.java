package com.jiggie.android.component.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Model;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by rangg on 09/09/2015.
 */
public class GCMRegistrationService extends Service implements GCMRegistration.Listener {
    public static final String TAG_UPDATED = "gcm_updated";
    public static final String TAG = "GCM_REGISTRATION";
    private boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            new GCMRegistration(this, this).run(AsyncTask.SERIAL_EXECUTOR);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onGCMPreExecute() {
        App.getSharedPreferences().edit().putBoolean(TAG_UPDATED, false).apply();
        return true;
    }

    @Override
    public void onGCMError(Exception e) {
        Log.e(TAG, App.getErrorMessage(e), e);
        this.isRunning = false;
        super.stopSelf();
    }

    @Override
    public void onGCMCompleted(String regId) {
        try {
            final String id = URLEncoder.encode(regId, "UTF-8");
            final String fbId = URLEncoder.encode(AccessToken.getCurrentAccessToken().getUserId(), "UTF-8");
            final String url = String.format("apntoken/%s/%s", fbId, id);

            VolleyHandler.getInstance().createVolleyRequest(url, (Model)null, new VolleyRequestListener<Void, JSONObject>() {
                @Override
                public Void onResponseAsync(JSONObject jsonObject) { return null; }

                @Override
                public void onResponseCompleted(Void value) {
                    App.getSharedPreferences().edit().putBoolean(TAG_UPDATED, true).apply();
                    isRunning = false;
                    stopSelf();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("GCMRegistration", App.getErrorMessage(error), error);
                    isRunning = false;
                    stopSelf();
                }
            });
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
