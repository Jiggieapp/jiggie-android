package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by Wandy on 5/12/2016.
 */
public class PostFriendModel {

    private String fb_id;
    private ArrayList<String> friends_fb_id;

    public PostFriendModel(String userId, ArrayList<String> friendsFbId) {
        this.fb_id = userId;
        this.friends_fb_id = friendsFbId;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public ArrayList<String> getFriends_fb_id() {
        return friends_fb_id;
    }

    public void setFriends_fb_id(ArrayList<String> friends_fb_id) {
        this.friends_fb_id = friends_fb_id;
    }
}
