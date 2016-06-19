package com.jiggie.android.activity.event;

import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.ThemeResultModel;

/**
 * Created by wandywijayanto on 6/17/16.
 */
public class ThemePresenterImplementation implements ThemePresenter{

    ThemeView themeView;

    public ThemePresenterImplementation(ThemeView themeView)
    {
        this.themeView = themeView;
    }

    @Override
    public void loadThemesEvent(String themeId) {
        EventManager.loadTheme(themeId, new OnResponseListener()
        {
            @Override
            public void onSuccess(Object object) {
                ThemeResultModel result = (ThemeResultModel) object;
                themeView.onFinishLoadTheme(result);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                themeView.onFailLoadTheme();
            }
        });
    }
}
