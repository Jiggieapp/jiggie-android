package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/3/2016.
 */
public class ChatListModel {

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
        ArrayList<ChatLists> chat_lists;

        public ArrayList<ChatLists> getChat_lists() {
            return chat_lists;
        }

        public void setChat_lists(ArrayList<ChatLists> chat_lists) {
            this.chat_lists = chat_lists;
        }

        public class ChatLists{
            String fromId;
            String fromName;
            String profile_image;
            String last_message;
            String last_updated;
            int unread;
            String fb_id;
            boolean hasreplied;

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
