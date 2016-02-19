package com.jiggie.android.component;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

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
    public static String FROM_BLOCK_CHAT = "block_chat";
    public static String FROM_DELETE_CHAT = "delete_chat";
    public static String FROM_ADD_CHAT = "add_chat";
    //public static String FROM_MORE = "more";
    public static String FROM_EVENT_DETAIL = "event_detail";
    public static String FROM_EVENT_GUEST = "event_guest";
    public static String FROM_PROFILE_DETAIL = "profile_detail";
    public static String FROM_PROFILE_SETTING = "profile_setting";
    public static String FROM_PROFILE_EDIT = "profile_edit";
    public static String FROM_SHARE_LINK = "share_link";
    public static String FROM_SETUP_TAGS = "setup_tags";
    public static String FROM_GUEST_CONNECT = "guest_connect";
    public static String FROM_VERIFY_VERIFICATION_CODE = "verify_verification_code";
    public static String ACCESS_TOKEN = "access_token";

    public static boolean SHOW_WALKTHROUGH_EVENT = true;
    public static boolean SHOW_WALKTHROUGH_SOCIAL = true;
    public static boolean SHOW_WALKTHROUGH_CHAT = true;
    public static String SET_WALKTHROUGH_EVENT = "walkthrough_event";
    public static String SET_WALKTHROUGH_SOCIAL = "walkthrough_social";
    public static String SET_WALKTHROUGH_CHAT = "walkthrough_chat";

    //ERROR CODE & MESSAGE
    //public static String MSG_EXCEPTION = "Failed: ";
    //changed by wandy 12-02-2016
    public static String MSG_EXCEPTION = "";
    public static String RESPONSE_FAILED = "Failed response";
    public static String MSG_EMPTY_DATA = "Empty data";
    public static int CODE_SUCCESS = 200;
    public static int CODE_EMPTY_DATA = 204;
    //--------------------

    public static String PREFERENCE_SETTING = "setting";
    public static String SETTING_MODEL = "setting_model";
    public static String PREFERENCE_LOGIN = "login";
    public static String LOGIN_MODEL = "login_model";
    public static String PREFERENCE_TAGLIST = "taglist";
    public static String TAGLIST_MODEL = "taglist_model";
    public static String IS_FIRST_RUN = "is_first_run";
    public static String MEMBER_SETTING_MODEL = "member_setting_model";

    //AppsFlyer properties----
    public static String AFinstall_type = "";
    public static String AFcampaign = "";
    public static String AFmedia_source = "";
    //------------------------

    public static int myPixel(Activity a,int dip){
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int)((dip-0.5f)*scale);
        return pixel;
    }
    //-----

    public final static String BASE_URL = "http://api-dev.jiggieapp.com/";
    //public final static String BASE_URL = "http://api.jiggieapp.com/";
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
    public final static String URL_GUEST_MATCH = BASE_URL + "app/v3/partyfeed/match/{fb_id}/{from_id}/{type}";
    public final static String URL_SOCIAL_MATCH = BASE_URL + "app/v3/partyfeed_socialmatch/match/{fb_id}/{from_id}/{type}";
    public final static String URL_EDIT_ABOUT = BASE_URL + "app/v3/updateuserabout";
    public final static String URL_TAGSLIST = BASE_URL + "app/v3/user/tagslist";
    public final static String URL_BLOCK_CHAT = BASE_URL + "app/v3/blockuserwithfbid";
    public final static String URL_DELETE_CHAT = BASE_URL + "app/v3/deletemessageswithfbid";
    public final static String URL_ADD_CHAT = BASE_URL + "app/v3/messages/add";
    public final static String URL_VERIFY_PHONE_NUMBER = BASE_URL + "app/v3/user/phone/verification/send/{fb_id}/{phone}";
    public final static String URL_VERIFY_VERIFICATION_CODE = BASE_URL + "app/v3/user/phone/verification/validate/{fb_id}/{token}";

    public static void d(final String title, final String text) {
        Log.d(title, text);
    }

    //permission for Marshmallow
    public static final int PERMISSION_REQUEST = 284;

    /*@TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(final String permission
            , final Activity activity)
    {
        int requestCode = 0;
        if(permission.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE))
        {
            requestCode = PERMISSION_REQUEST;
        }
        int hasPermission = activity.checkSelfPermission(permission);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{permission},
                    requestCode);
            return;
        }
        else return;
    }*/
}
