package com.jiggie.android.activity.profile;

/**
 * Created by Wandy on 5/3/2016.
 */
public class ProfileDetailModel {

    private String fb_id;
    private String url;

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProfileDetailModel(final String url, final String fb_id)
    {
        this.url = url;
        this.fb_id = fb_id;
    }
}
