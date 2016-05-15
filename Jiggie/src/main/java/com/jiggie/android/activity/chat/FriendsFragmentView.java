package com.jiggie.android.activity.chat;

import com.jiggie.android.model.FriendListModel;

/**
 * Created by Wandy on 5/12/2016.
 */
public interface FriendsFragmentView {
    void clearAdapter();
    void setAdapter(FriendListModel friendListModel);
}
