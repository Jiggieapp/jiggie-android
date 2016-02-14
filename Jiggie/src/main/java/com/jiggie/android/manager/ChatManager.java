package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.ChatInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatActionModel;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ChatConversationModel;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.ChatResponseModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/3/2016.
 */
public class ChatManager {

    private static ChatInterface chatInterface;
    public static final String FROM_LOAD = "load";
    public static final String FROM_CHECK_NEW = "check";
    //public static boolean NEED_REFRESH_CHATLIST = true;

    public static ArrayList<ChatListModel.Data.ChatLists> dataChatList = new ArrayList<ChatListModel.Data.ChatLists>();

    public static void initChatService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        chatInterface = retrofit.create(ChatInterface.class);
    }

    private static ChatInterface getInstance(){
        if(chatInterface == null)
            initChatService();

        return chatInterface;
    }

    private static void getChatConversations(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().getChatConversations(fb_id, to_id).enqueue(callback);
    }

    private static void getChatList(String fb_id, Callback callback) throws IOException {
        getInstance().getChatList(fb_id).enqueue(callback);
    }

    private static void blockChat(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().blockChat(fb_id, to_id).enqueue(callback);
    }

    private static void deleteChat(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().deleteChat(fb_id, to_id).enqueue(callback);
    }

    private static void addChat(ChatAddModel chatAddModel, Callback callback) throws IOException {
        getInstance().addChat(Utils.URL_ADD_CHAT, chatAddModel).enqueue(callback);
    }

    public static void loaderChatConversations(String fb_id, String to_id, final String fromFunction){
        try {
            getChatConversations(fb_id, to_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        ChatConversationModel dataTemp = (ChatConversationModel) response.body();
                        EventBus.getDefault().post(new ChatResponseModel(fromFunction, dataTemp.getData().getChat_conversations()));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Log.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderChatList(String fb_id){
        try {
            getChatList(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        ChatListModel dataTemp = (ChatListModel) response.body();
                        dataChatList = dataTemp.getData().getChat_lists();

                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Log.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderBlockChat(String fb_id, String to_id){
        try {
            blockChat(fb_id, to_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_BLOCK_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderDeleteChat(String fb_id, String to_id){
        try {
            deleteChat(fb_id, to_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_DELETE_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderAddChat(ChatAddModel chatAddModel){
        try {
            addChat(chatAddModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_ADD_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
