package com.jiggie.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

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
    public static String MEMBER_SETTING_MODEL = "member_setting_model";
    public static String TAGS_LIST = "tags_list";
    public static String EVENT_LIST = "event_list";
    public static String PREFERENCE_LOGIN = "login";
    public static String LOGIN_MODEL = "login_model";
    public static String ACCESS_TOKEN = "access_token";

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
    public final static String URL_GET_ACCESS_TOKEN =  BASE_URL + "app/v3/userlogin";
    public static final String URL_GET_PRODUCT_LIST = BASE_URL + "app/v3/product/list/{event_id}";

    public static void d(final String tag,final String value) {
        Log.d(tag, value);
    }

    public static final String DATE_TODAY = "today";
    public static final String DATE_TOMORROW = "tomorrow";
    public static final String DATE_UPCOMING = "upcoming";

    public static String calculateTime(String date) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date d1 = format.parse(date);
            Long current = new Date().getTime();
            Date midnight = new Date(current - current % (24 * 60 * 60 * 1000));

            long diff = Math.abs(d1.getTime() - midnight.getTime());

            long diffDays = diff / (24 * 60 * 60 * 1000);
            if(diffDays == 0)
                return DATE_TODAY;
            else if(diffDays == 1)
                return DATE_TOMORROW;
            else
                return DATE_UPCOMING;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }

}
