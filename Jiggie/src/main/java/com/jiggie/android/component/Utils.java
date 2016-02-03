package com.jiggie.android.component;

import android.app.Activity;
import android.util.Log;

/**
 * Created by LTE on 1/29/2016.
 */
public class Utils {

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
    public static String MEMBER_SETTING_MODEL = "member_setting_model";
    public static String TAGS_LIST = "tags_list";

    public static int myPixel(Activity a,int dip){
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int)((dip-0.5f)*scale);
        return pixel;
    }
    //-----

    public final static String BASE_URL = "http://api-dev.jiggieapp.com/";
    //public final static String URL = "http://api.jiggieapp.com/";
    public final static String GET_EVENTS = BASE_URL + "app/v3/events/list/{fb_id}";
    //public final static String GET_EVENTS = URL + "app/v3/events/list/{fb_id}/{gender_interest}";

    public final static String URL_LOGIN = BASE_URL + "app/v3/login";
    public final static String URL_MEMBER_SETTING = BASE_URL + "app/v3/membersettings";

    public static void d(final String tag,final String value) {
        Log.d(tag, value);
    }
}
