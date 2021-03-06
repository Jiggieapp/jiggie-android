package com.jiggie.android.activity.social;

import com.facebook.AccessToken;
import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.SettingModel;

/**
 * Created by Wandy on 4/28/2016.
 */
public class SocialFilterImplementation implements SocialFilterPresenter{
    private SocialView socialView;
    public static final String TAG = SocialFilterImplementation.class.getSimpleName();

    public SocialFilterImplementation(SocialView socialView) {
        this.socialView = socialView;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void fetchSetting() {
        if(socialView != null)
            socialView.showProgressDialog();
        AccountManager.loaderSettingNew(AccessToken.getCurrentAccessToken().getUserId(),
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        //socialView.dismissProgressDialog();
                        socialView.hideErrorLayout();
                        MemberSettingResultModel result = (MemberSettingResultModel) object;
                        socialView.updateUI(result);
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {
                        socialView.showErrorLayout();
                    }
                }
        );
    }

    @Override
    public void updateSetting( final MemberSettingModel memberSettingModel) {
       // MemberSettingModel memberSettingModel = new MemberSettingModel(memberSettingResultModel);
        AccountManager.loaderMemberSetting(memberSettingModel, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                AccountManager.saveMemberSetting(memberSettingModel);
                SettingModel settingModel = AccountManager.loadSetting();
                settingModel.getData().setGender_interest(memberSettingModel.getGender_interest());
                boolean isFeed;
                if(memberSettingModel.getFeed() == 0)
                    isFeed = false;
                else isFeed = true;
                settingModel.getData().getNotifications().setFeed(isFeed);
                AccountManager.saveSetting(settingModel);
                socialView.onSuccess();
            }

            @Override
            public void onFailure(int responseCode, String message) {
                socialView.onFailure();
            }
        });
    }

    @Override
    public void updateMatchMe(final String value) {
        AccountManager.updateMatchMe(value, new OnResponseListener()
        {
            @Override
            public void onSuccess(Object object) {
                SettingModel settingModel = AccountManager.loadSetting();
                if(value.equalsIgnoreCase("yes"))
                {
                    settingModel.setMatchme(true);
                }
                else
                {
                    settingModel.setMatchme(false);
                }
                AccountManager.saveSetting(settingModel);
                socialView.updateSetting();
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });
    }
}
