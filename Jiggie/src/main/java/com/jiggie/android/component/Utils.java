package com.jiggie.android.component;

import android.app.Activity;

/**
 * Created by LTE on 1/29/2016.
 */
public class Utils {

    public static String FROM_SIGN_IN = "sign_in";
    public static String FROM_MEMBER_SETTING = "member_setting";
    //public static String FROM_GET_MEMBER_SETTING = "get_member_setting";
    public static String FROM_EVENT = "event";
    public static String FROM_SOCIAL_FEED = "social_feed";
    public static String FROM_SOCIAL_MATCH = "social_match";
    public static String FROM_CHAT = "chat";
    public static String FROM_CHAT_CONVERSATION = "chat_conversation";
    //public static String FROM_MORE = "more";
    public static String FROM_EVENT_DETAIL = "event_detail";
    public static String FROM_EVENT_GUEST = "event_guest";
    public static String FROM_PROFILE_DETAIL = "profile_detail";
    public static String FROM_PROFILE_SETTING = "profile_setting";
    public static String FROM_PROFILE_EDIT = "profile_edit";
    public static String FROM_SHARE_LINK = "share_link";

    public static boolean SHOW_WALKTHROUGH_EVENT = true;
    public static boolean SHOW_WALKTHROUGH_SOCIAL = true;
    public static boolean SHOW_WALKTHROUGH_CHAT = true;
    public static String SET_WALKTHROUGH_EVENT = "walkthrough_event";
    public static String SET_WALKTHROUGH_SOCIAL = "walkthrough_social";
    public static String SET_WALKTHROUGH_CHAT = "walkthrough_chat";

    //ERROR CODE & MESSAGE
    public static String MSG_EXCEPTION = "Failed: ";
    //--------------------

    public static String PREFERENCE_SETTING = "setting";
    public static String SETTING_MODEL = "setting_model";
    public static String PREFERENCE_LOGIN = "login";
    public static String LOGIN_MODEL = "login_model";

    public static int myPixel(Activity a,int dip){
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int)((dip-0.5f)*scale);
        return pixel;
    }
    //-----

    public final static String BASE_URL = "http://api-dev.jiggieapp.com/";
    //public final static String URL = "http://api.jiggieapp.com/";
    public final static String URL_EVENTS = BASE_URL + "app/v3/events/list/{fb_id}";

    public final static String URL_LOGIN = BASE_URL + "app/v3/login";
    public final static String URL_MEMBER_SETTING = BASE_URL + "app/v3/membersettings";
    public final static String URL_GET_SETTING = BASE_URL + "app/v3/membersettings";
    public final static String URL_EVENT_DETAIL = BASE_URL + "app/v3/event/details/{id}/{fb_id}/{gender_interest}";
    public final static String URL_CHAT_CONVERSATION = BASE_URL + "app/v3/chat/conversation/{fb_id}/{to_id}";
    public final static String URL_CHAT_LIST = BASE_URL + "app/v3/conversations";
    public final static String URL_GUEST_INTEREST = BASE_URL + "app/v3/event/interest/{event_id}/{fb_id}/{gender_interest}";
    public final static String URL_MEMBER_INFO = BASE_URL + "app/v3/memberinfo/{fb_id}";
    public final static String URL_SHARE_APPS = BASE_URL + "app/v3/invitelink";
    public final static String URL_SHARE_EVENT = BASE_URL + "app/v3/invitelink";
    public final static String URL_SOCIAL_FEED = BASE_URL + "app/v3/partyfeed/list/{fb_id}/{gender_interest}";
    public final static String URL_SOCIAL_MATCH = BASE_URL + "app/v3/partyfeed/match/{fb_id}/{from_id}/{type}";
    public final static String URL_EDIT_ABOUT = BASE_URL + "app/v3/updateuserabout";

}
