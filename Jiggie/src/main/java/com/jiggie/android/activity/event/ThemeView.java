package com.jiggie.android.activity.event;

import com.jiggie.android.model.ThemeResultModel;

import java.util.ArrayList;

/**
 * Created by wandywijayanto on 6/17/16.
 */
public interface ThemeView {

    void onFinishLoadTheme(ThemeResultModel result);
    void onFailLoadTheme();
}
