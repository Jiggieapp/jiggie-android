package com.jiggie.android.activity.social;

/**
 * Created by Wandy on 4/28/2016.
 */
public interface SocialFilterPresenter {
    void onResume();

    void onDestroy();

    void fetchSetting();
}
