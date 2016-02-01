package com.jiggie.android.component.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import com.jiggie.android.App;
import com.android.jiggie.R;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Login;

/**
 * Created by rangg on 02/11/2015.
 */
public class FacebookImageSyncService extends Service {
    public static final String GRAPH_URL = "https://graph.facebook.com/";
    public static final int IMAGE_COUNT = 4;

    private SharedPreferences preferences;
    private boolean running;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.preferences == null)
            this.preferences = App.getSharedPreferences();
        if ((!this.running) && (App.getInstance().isUserLoggedIn())) {
            this.running = true;
            final AccessToken token = AccessToken.getCurrentAccessToken();
            new GraphRequest(token, String.format("/%s/albums", token.getUserId()), null, HttpMethod.GET, this.albumCallback).executeAsync();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private GraphRequest.Callback albumCallback = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            final JSONArray array = VolleyHandler.getArrayData(response.getJSONObject());
            final int length = array == null ? 0 : array.length();
            boolean found = false;

            for (int i = 0; i < length; i++) {
                final JSONObject obj = array.optJSONObject(i);
                final String name = obj == null ? null : obj.optString("name");

                if ("Profile Pictures".equals(name)) {
                    final String id = obj.optString("id");
                    new GraphRequest(AccessToken.getCurrentAccessToken(), String.format("/%s/photos", id), null, HttpMethod.GET, new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            createImageDownloadTask().execute(response);
                        }
                    }).executeAsync();
                    found = true;
                    break;
                }
            }

            if ((length == 0) || (!found))
                running = false;
        }
    };

    private AsyncTask<GraphResponse, Void, HashSet<String>> createImageDownloadTask() {
        return new AsyncTask<GraphResponse, Void, HashSet<String>>() {
            @Override
            protected HashSet<String> doInBackground(GraphResponse... params) {
                final HashSet<String> files = (HashSet<String>) preferences.getStringSet(Common.PREF_IMAGES, new HashSet<String>());
                final JSONArray array = VolleyHandler.getArrayData(params[0].getJSONObject());
                final AccessToken token = AccessToken.getCurrentAccessToken();
                final String path = App.getInstance().getDataPath(Common.PREF_IMAGES);
                final int length = array == null ? 0 : array.length();
                final String[] newFiles = new String[length];
                final boolean newLogin = files.size() == 0;
                final byte[] buffer = new byte[4096];

                FileOutputStream outputStream = null;
                InputStream inputStream = null;
                boolean changed = false;
                int read;

                if ((!App.getInstance().isUserLoggedIn()) || (token == null)) {
                    // user sign out while the image is being uploaded
                    return null;
                }

                for (int i = 0; i < length; i++) {
                    try {
                        final JSONObject obj = array.optJSONObject(i);
                        final String id = obj == null ? null : obj.optString("id");
                        newFiles[i] = id;

                        // only download limited number of images
                        if (i >= IMAGE_COUNT)
                            break;

                        // download the image from facebook
                        if ((id != null) && (!files.contains(id))) {
                            final URL imgUrl = new URL(String.format("%s%s/picture?type=normal&access_token=%s", GRAPH_URL, id, token.getToken()));
                            inputStream = imgUrl.openConnection().getInputStream();
                            outputStream = new FileOutputStream(path + id);

                            while ((read = inputStream.read(buffer)) > 0)
                                outputStream.write(buffer, 0, read);

                            files.add(id);
                        }
                    } catch (IOException e) {
                        Log.e("FacebookImageSync", App.getErrorMessage(e), e);
                    } finally {
                        App.safeClose(outputStream);
                        App.safeClose(inputStream);
                    }
                }

                if (!newLogin) {
                    final Iterator<String> imgIterator = files.iterator();
                    while (imgIterator.hasNext()) {
                        final String img = imgIterator.next();
                        boolean removed = true;

                        for (int i = 0; i < length; i++) {
                            final String newImg = newFiles[i];
                            if (img.equals(newImg)) {
                                // image is still exist in the facebook album, let's ignore it for now.
                                removed = false;
                                break;
                            }
                        }
                        if (removed) {
                            // image has been removed from facebook album, let's remove it from local cache.
                            imgIterator.remove();
                            changed = true;
                        }
                    }
                }

                // save the list of images to the cache.
                preferences.edit()
                        .putStringSet(Common.PREF_IMAGES, files)
                        .putString(Common.PREF_IMAGE, newFiles[0])
                        .apply();
                changed = changed || preferences.getBoolean(Common.PREF_IMAGES_UPLOADED, false);

                if ((newLogin) || (changed))
                    return files;
                else
                    return null;
            }

            @Override
            protected void onPostExecute(HashSet<String> files) {
                if (files != null) {
                    preferences.edit().putBoolean(Common.PREF_IMAGES_UPLOADED, false).apply();
                    imageUpload(files);
                } else
                    running = false;
            }
        };
    }

    private void imageUpload(HashSet<String> images) {
        final App app = App.getInstance();
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final String path = app.getDataPath(Common.PREF_IMAGES);
        final RequestParams params = new RequestParams();
        int count = 1;

        if ((!app.isUserLoggedIn()) || (accessToken == null)) {
            // user sign out while the image is being uploaded
            return;
        }

        params.put(Login.FIELD_FACEBOOK_ID, accessToken.getUserId());
        for(String img : images) {
            if (TextUtils.isEmpty(img))
                continue;
            try {
                params.put("photo" + count, new File(path + img));
                count++;
            } catch (FileNotFoundException e) {
                // wow, where is the file?.
            }
        }

        if (count > 1) {
            final AsyncHttpClient client = new AsyncHttpClient();
            final String host = app.getString(R.string.server_host);

            client.post(host + "updatephotos", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    preferences.edit().putBoolean(Common.PREF_IMAGES_UPLOADED, true).apply();
                    running = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("FacebookImageSyncPost", responseBody != null && responseBody.length > 0 ? new String(responseBody) : App.getErrorMessage(error), error);
                    preferences.edit().putBoolean(Common.PREF_IMAGES_UPLOADED, false).apply();
                    running = false;
                }
            });
        } else
            running = false;
    }
}
