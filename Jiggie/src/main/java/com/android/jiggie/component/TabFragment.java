package com.android.jiggie.component;

/**
 * Created by rangg on 21/10/2015.
 */
public interface TabFragment {
    String getTitle();
    void onTabSelected();
    void setHomeMain(HomeMain homeMain);
}
