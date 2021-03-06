package com.jiggie.android.activity.social;

import com.jiggie.android.model.MemberSettingModel;

/**
 * Created by Wandy on 4/28/2016.
 */
public interface SocialFilterPresenter {
    void onResume();

    void onDestroy();

    void fetchSetting();

    void updateSetting(MemberSettingModel memberSettingResultModel);

    void updateMatchMe(String value);
}
