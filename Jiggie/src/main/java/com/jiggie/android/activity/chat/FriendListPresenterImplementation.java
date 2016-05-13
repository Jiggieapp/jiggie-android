package com.jiggie.android.activity.chat;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.FriendListModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wandy on 5/12/2016.
 */
public class FriendListPresenterImplementation implements FriendListPresenter {

    private final String TAG = FriendListPresenterImplementation.this.getClass().getSimpleName();
    private FriendsFragmentView friendsFragmentView;

    public FriendListPresenterImplementation(final FriendsFragmentView friendsFragmentView)
    {
        this.friendsFragmentView = friendsFragmentView;
    }

    @Override
    public void loadFriendList(JSONObject object) {
        AccountManager.getFriendList(object
                , new com.jiggie.android.listener.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                friendsFragmentView.clearAdapter();
                FriendListModel friendListModel = (FriendListModel) object;
                friendsFragmentView.setAdapter(friendListModel);
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });
    }

    public void loadFriendFromFb()
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() +"/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if(response.getJSONObject() != null)
                            loadFriendList(response.getJSONObject());
                    }
                }
        ).executeAsync();

    }
}
