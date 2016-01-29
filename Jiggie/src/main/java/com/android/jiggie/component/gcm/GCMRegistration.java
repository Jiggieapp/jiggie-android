package com.android.jiggie.component.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.android.jiggie.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by rangga on 05/08/2015.
 */
public class GCMRegistration extends AsyncTask<Void, Void, String> {
    private Listener listener;
    private Context context;
    private Handler handler;

    public GCMRegistration(Context context, Listener listener) {
        this.handler = new Handler();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            final InstanceID instanceID = InstanceID.getInstance(this.context);
            return instanceID.getToken(this.context.getString(R.string.gcm_project_number), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (final IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onGCMError(e);
                }
            });
        }
        return null;
    }

    public void run(Executor executor) {
        if (this.listener.onGCMPreExecute())
            this.executeOnExecutor(executor);
    }

    @Override
    protected void onPostExecute(String regId) {
        this.listener.onGCMCompleted(regId);
    }

    public interface Listener {
        boolean onGCMPreExecute();
        void onGCMError(Exception e);
        void onGCMCompleted(String regId);
    }
}
