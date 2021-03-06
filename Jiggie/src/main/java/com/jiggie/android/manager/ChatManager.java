package com.jiggie.android.manager;

import com.jiggie.android.api.ChatInterface;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ChatActionModel;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.model.ChatConversationModel;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.ChatResponseModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessGroupRoomModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LTE on 2/3/2016.
 */
public class ChatManager extends BaseManager{

    private static ChatInterface chatInterface;
    private static ChatInterface chatInterface2;
    public static final String FROM_LOAD = "load";
    public static final String FROM_CHECK_NEW = "check";
    //public static boolean NEED_REFRESH_CHATLIST = true;

    public static ArrayList<ChatListModel.Data.ChatLists> dataChatList = new ArrayList<ChatListModel.Data.ChatLists>();

    public static void initChatService(){
        chatInterface = getRetrofit().create(ChatInterface.class);
    }

    private static ChatInterface getInstance(){
        if(chatInterface == null)
        {
            initChatService();
            //chatInterface = getRetrofit().create(ChatInterface.class);
        }
        return chatInterface;
    }

    private static ChatInterface getInstance2()
    {
        if(chatInterface2 == null)
        {
            chatInterface2 = getRetrofit().create(ChatInterface.class);
        }
        return chatInterface2;
    }

    private static void getChatConversations(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().getChatConversations(fb_id, to_id).enqueue(callback);
    }

    private static void getChatList(String fb_id, Callback callback) throws IOException {
        getInstance().getChatList(fb_id).enqueue(callback);
    }

    /*private static void getChatList2(String fb_id, Callback callback) throws IOException {
        getInstance().getChatList2(fb_id).(callback);
    }*/


    private static void blockChat(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().blockChat(fb_id, to_id).enqueue(callback);
    }

    private static void deleteChat(String fb_id, String to_id, Callback callback) throws IOException {
        getInstance().deleteChat(fb_id, to_id).enqueue(callback);
    }

    private static void addChat(ChatAddModel chatAddModel, Callback callback) throws IOException {
        getInstance().addChat(Utils.URL_ADD_CHAT, chatAddModel).enqueue(callback);
    }

    private static void migrateChatFirebase(String fb_id, Callback callback) throws IOException {
        getInstance().migrateChatFirebase(fb_id).enqueue(callback);
    }

    private static void addGroupChat(HashMap<String, Object> groupModel, Callback callback) throws IOException {
        getInstance().groupChat(Utils.URL_ADD_GROUP_CHAT, groupModel).enqueue(callback);
    }

    private static void addGroupChatJannes(HashMap<String, Object> groupModel, Callback callback) throws IOException {
        getInstance().groupChatJannes(Utils.URL_ADD_GROUP_CHAT_JANNES, groupModel).enqueue(callback);
    }

    private static void addChatFirebase(HashMap<String, Object> chatModel, Callback callback) throws IOException {
        getInstance().addChatFirebase(Utils.URL_ADD_CHAT_FIREBASE, chatModel).enqueue(callback);
    }

    private static void deleteChatFirebase(HashMap<String, Object> deleteModel, Callback callback) throws IOException {
        getInstance().deleteChatFirebase(Utils.URL_DELETE_CHAT_FIREBASE, deleteModel).enqueue(callback);
    }

    private static void blockChatFirebase(HashMap<String, Object> blockModel, Callback callback) throws IOException {
        getInstance().blockChatFirebase(Utils.URL_BLOCK_CHAT_FIREBASE, blockModel).enqueue(callback);
    }

    public static void loaderChatConversations(String fb_id, String to_id, final String fromFunction){
        try {
            getChatConversations(fb_id, to_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        ChatConversationModel dataTemp = (ChatConversationModel) response.body();
                        EventBus.getDefault().post(new ChatResponseModel(fromFunction, dataTemp.getData().getChat_conversations()));
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT_CONVERSATION, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderChatList2(final String fb_id, final OnResponseListener onResponseListener)
    {
        try {
            getChatList(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        ChatListModel dataTemp = (ChatListModel) response.body();
                        //EventBus.getDefault().post(dataTemp);
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }


    public static void loaderChatList(String fb_id){
        try {
            getChatList(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        ChatListModel dataTemp = (ChatListModel) response.body();
                        dataChatList = dataTemp.getData().getChat_lists();

                        EventBus.getDefault().post(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderBlockChat(String fb_id, String to_id){
        try {
            blockChat(fb_id, to_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_BLOCK_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.MSG_EXCEPTION + t.toString()));

                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_BLOCK_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderDeleteChat(String fb_id, String to_id){
        try {
            deleteChat(fb_id, to_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_DELETE_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.MSG_EXCEPTION + t.toString()));

                }

                @Override
                public void onNeedToRestart() {

                }

            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_DELETE_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderAddChat(ChatAddModel chatAddModel){
        try {
            addChat(chatAddModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(new ChatActionModel(Utils.FROM_ADD_CHAT, dataTemp));
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }


            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_ADD_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMigrateChatFirebase(final String fb_id, final OnResponseListener onResponseListener)
    {
        try {
            migrateChatFirebase(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS||responseCode==403){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderGroupChat(final HashMap<String, Object> groupModel, final OnResponseListener onResponseListener)
    {
        try {
            addGroupChat(groupModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        SuccessGroupRoomModel dataTemp = (SuccessGroupRoomModel) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderGroupChatJannes(final HashMap<String, Object> groupModel, final OnResponseListener onResponseListener)
    {
        try {
            addGroupChatJannes(groupModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderAddChatJannes(ChatAddModel chatAddModel, final OnResponseListener onResponseListener){
        try {
            addChat(chatAddModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }


            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderBlockChatNew(String fb_id, String to_id, final OnResponseListener onResponseListener){
        try {
            blockChat(fb_id, to_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));

                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderAddChatFirebase(final HashMap<String, Object> chatModel, final OnResponseListener onResponseListener)
    {
        try {
            addChatFirebase(chatModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderBlockChatFirebase(final HashMap<String, Object> blockModel, final OnResponseListener onResponseListener)
    {
        try {
            blockChatFirebase(blockModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderDeleteChatFirebase(final HashMap<String, Object> deleteModel, final OnResponseListener onResponseListener)
    {
        try {
            deleteChatFirebase(deleteModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    int responseCode = response.code();

                    if(responseCode==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    }else if(responseCode==Utils.CODE_EMPTY_DATA){
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EMPTY_DATA));
                    }else{
                        onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("Exception", t.toString());
                    onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }
        catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(new ExceptionModel(Utils.FROM_CHAT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
