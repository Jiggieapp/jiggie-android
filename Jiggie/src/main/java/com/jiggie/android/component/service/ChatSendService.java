package com.jiggie.android.component.service;

import android.util.Log;

import com.jiggie.android.App;
import com.jiggie.android.component.database.ChatTable;
import com.jiggie.android.component.database.DatabaseConnection;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.model.Chat;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by rangg on 23/12/2015.
 */
public class ChatSendService extends GcmTaskService {
    public static final String TAG = ChatSendService.class.getName();

    public static void registerSchedule() {
        final GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(App.getInstance());
        final OneoffTask task = new OneoffTask.Builder()
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setService(ChatSendService.class)
                .setExecutionWindow(0L, 1L)
                .setPersisted(true)
                .setTag(TAG)
                .build();
        gcmNetworkManager.cancelTask(TAG, ChatSendService.class);
        gcmNetworkManager.schedule(task);
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        registerSchedule();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        final DatabaseConnection db = App.getInstance().getDatabase();
        int result = GcmNetworkManager.RESULT_SUCCESS;
        Chat chat;

        while ((chat = ChatTable.getUnprocessed(db)) != null) {
            try {
                final RequestFuture<JSONObject> future = RequestFuture.newFuture();
                final JsonObjectRequest request = new JsonObjectRequest(VolleyHandler.getInstance().getServerHost() + "messages/add", chat.toJsonObject(), future, future);

                VolleyHandler.getInstance().getVolleyRequestQueue().add(request);
                future.get();
                ChatTable.delete(db, chat);
            } catch (JSONException | InterruptedException | ExecutionException e) {
                result = GcmNetworkManager.RESULT_RESCHEDULE;
                Log.e(TAG, App.getErrorMessage(e), e);
            }
        }
        return result;
    }
}
