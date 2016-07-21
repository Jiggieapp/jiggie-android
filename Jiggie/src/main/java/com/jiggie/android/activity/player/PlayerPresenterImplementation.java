package com.jiggie.android.activity.player;

import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.PlayerManager;
import com.jiggie.android.model.SoundcloudModel;

/**
 * Created by Wandy on 7/19/2016.
 */
public class PlayerPresenterImplementation implements PlayerPresenter {

    private PlayerView playerView;
    public PlayerPresenterImplementation(PlayerView playerView)
    {
        this.playerView = playerView;
    }

    @Override
    public void getTrackDetail(String trackId) {
        PlayerManager.getTrackDetail(trackId, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                SoundcloudModel soundcloudModel = (SoundcloudModel) object;
                playerView.getTrackDetail(soundcloudModel);
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });
    }

}
