package com.jiggie.android.model;

/**
 * Created by LTE on 2/3/2016.
 */
public class ChatResponseModel {

    String fromFunction;
    ChatConversationModel.Data.ChatConversations data;

    public ChatResponseModel(String fromFunction, ChatConversationModel.Data.ChatConversations data){
        this.fromFunction = fromFunction;
        this.data = data;
    }

    public String getFromFunction() {
        return fromFunction;
    }

    public ChatConversationModel.Data.ChatConversations getData() {
        return data;
    }
}
