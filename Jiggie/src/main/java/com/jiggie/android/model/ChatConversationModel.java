package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/3/2016.
 */
public class ChatConversationModel {

    int response;
    String msg;
    Data data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        ChatConversations chat_conversations;

        public ChatConversations getChat_conversations() {
            return chat_conversations;
        }

        public void setChat_conversations(ChatConversations chat_conversations) {
            this.chat_conversations = chat_conversations;
        }

        public class ChatConversations{
            String fromId;
            String fromName;
            String profile_image;
            ArrayList<Messages> messages;
            String event_name;

            public String getEvent_name() {
                return event_name;
            }

            public void setEvent_name(String event_name) {
                this.event_name = event_name;
            }



            public String getFromId() {
                return fromId;
            }

            public void setFromId(String fromId) {
                this.fromId = fromId;
            }

            public String getFromName() {
                return fromName;
            }

            public void setFromName(String fromName) {
                this.fromName = fromName;
            }

            public String getProfile_image() {
                return profile_image;
            }

            public void setProfile_image(String profile_image) {
                this.profile_image = profile_image;
            }

            public ArrayList<Messages> getMessages() {
                return messages;
            }

            public void setMessages(ArrayList<Messages> messages) {
                this.messages = messages;
            }

            public class Messages{
                String created_at;
                String header;
                String message;
                boolean isFromYou;

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getHeader() {
                    return header;
                }

                public void setHeader(String header) {
                    this.header = header;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public boolean isFromYou() {
                    return isFromYou;
                }

                public void setIsFromYou(boolean isFromYou) {
                    this.isFromYou = isFromYou;
                }
            }

            String last_message;
            String last_updated;
            int unread;
            String fb_id;
            boolean hasreplied;

            public String getLast_message() {
                return last_message;
            }

            public void setLast_message(String last_message) {
                this.last_message = last_message;
            }

            public String getLast_updated() {
                return last_updated;
            }

            public void setLast_updated(String last_updated) {
                this.last_updated = last_updated;
            }

            public int getUnread() {
                return unread;
            }

            public void setUnread(int unread) {
                this.unread = unread;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public boolean isHasreplied() {
                return hasreplied;
            }

            public void setHasreplied(boolean hasreplied) {
                this.hasreplied = hasreplied;
            }

        }

    }

}
