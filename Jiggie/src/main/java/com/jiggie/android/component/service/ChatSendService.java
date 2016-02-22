package com.jiggie.android.component.service;

import android.util.Log;

import com.google.gson.Gson;
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
import com.jiggie.android.model.ChatAddModel;

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

                //Added by Aga------
                ChatAddModel chatAddModel = new ChatAddModel();
                chatAddModel.setFromId(chat.getFromId());
                chatAddModel.setHeader("");
                chatAddModel.setFromName(chat.getFromName());
                chatAddModel.setMessage(chat.getMessage());
                chatAddModel.setHosting_id("");
                chatAddModel.setKey("kT7bgkacbx73i3yxma09su0u901nu209mnuu30akhkpHJJ");
                chatAddModel.setToId(chat.getToId());

                JSONObject chatObj = new JSONObject(new Gson().toJson(chatAddModel));
                //--------------------

                final JsonObjectRequest request = new JsonObjectRequest(VolleyHandler.getInstance().getServerHost() + "messages/add", chatObj, future, future);

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
