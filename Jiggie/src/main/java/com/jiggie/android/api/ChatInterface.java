package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.model.ChatConversationModel;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Success2Model;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;

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

}
