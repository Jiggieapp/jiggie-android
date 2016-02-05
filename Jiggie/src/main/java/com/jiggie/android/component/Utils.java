package com.jiggie.android.component;

import android.app.Activity;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    public static String EVENT_LIST = "event_list";

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
    public final static String URL_EVENT_DETAIL = BASE_URL + "app/v3/event/details/{id}/{fb_id}/{gender_interest}";

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
}
