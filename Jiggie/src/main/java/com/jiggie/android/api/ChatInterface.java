package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.model.ChatConversationModel;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Success2Model;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by LTE on 2/3/2016.
 */
public interface ChatInterface {

    @GET(Utils.URL_CHAT_CONVERSATION)
    Call<ChatConversationModel> getChatConversations(@Path("fb_id") String fb_id, @Path("to_id") String to_id);

    @GET(Utils.URL_CHAT_LIST)
    Call<ChatListModel> getChatList(@Query("fb_id") String fb_id);

    @GET(Utils.URL_BLOCK_CHAT)
    Call<Success2Model> blockChat(@Query("fromId") String fromId, @Query("toId") String toId);

    @GET(Utils.URL_DELETE_CHAT)
    Call<Success2Model> deleteChat(@Query("fromId") String fromId, @Query("toId") String toId);

    @POST
    Call<Success2Model> addChat(@Url String url, @Body ChatAddModel chatAddModel);

    @POST
    Call<Success2Model> groupChat(@Url String url, @Body HashMap<String, Object> groupModel);

    @GET(Utils.URL_CHAT_FIREBASE)
    Call<Success2Model> migrateChatFirebase(@Path("fb_id") String fb_id);

    @POST
    Call<Success2Model> groupChatJannes(@Url String url, @Body HashMap<String, Object> groupModel);

    @POST
    Call<Success2Model> addChatFirebase(@Url String url, @Body HashMap<String, Object> chatModel);

    @POST
    Call<Success2Model> blockChatFirebase(@Url String url, @Body HashMap<String, Object> blockModel);

    @POST
    Call<Success2Model> deleteChatFirebase(@Url String url, @Body HashMap<String, Object> deleteModel);

}
