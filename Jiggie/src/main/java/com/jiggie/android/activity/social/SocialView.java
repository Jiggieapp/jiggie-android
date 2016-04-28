package com.jiggie.android.activity.social;

/**
 * Created by Wandy on 4/28/2016.
 */
public interface SocialView {

    void showProgressDialog();
    void dismissProgressDialog();
    void showErrorLayout();
    void hideErrorLayout();
    void updateUI();
}
