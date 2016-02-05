package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatConversationModel;
import com.jiggie.android.model.ChatListModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by LTE on 2/3/2016.
 */
public interface ChatInterface {

    @GET(Utils.URL_CHAT_CONVERSATION)
    Call<ChatConversationModel> getChatConversations(@Path("fb_id") String fb_id, @Path("to_id") String to_id);

    @GET(Utils.URL_CHAT_LIST)
    Call<ChatListModel> getChatList(@Query("fb_id") String fb_id);

}
