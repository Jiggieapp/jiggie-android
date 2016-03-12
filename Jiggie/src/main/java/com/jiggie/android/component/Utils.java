package com.jiggie.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.BuildConfig;
import com.jiggie.android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Response;

/**
 * Created by LTE on 1/29/2016.
 */
public class Utils {
    public static String BLANK = "";
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
    public static String FROM_WALKTHROUGH = "walkthrough";
    public static String FROM_PRODUCT_LIST = "product_list";
    public static String FROM_SUMMARY = "summary";
    public static String FROM_COMPLETING_WALKTHROUGH_LOCATION = "complete_walkthrough_location";
    public static String FROM_APPSFLYER = "appsflyer";
    public static String FROM_MIXPANEL = "mixpanel";

    public static boolean SHOW_WALKTHROUGH_EVENT = true;
    public static boolean SHOW_WALKTHROUGH_SOCIAL = true;
    public static boolean SHOW_WALKTHROUGH_CHAT = true;
    public static String SET_WALKTHROUGH_EVENT = "walkthrough_event";
    public static String SET_WALKTHROUGH_SOCIAL = "walkthrough_social";
    public static String SET_WALKTHROUGH_CHAT = "walkthrough_chat";
    public static String TAB_EVENT = "event";
    public static String TAB_CHAT = "chat";
    public static String TAB_SOCIAL = "social";

    //ERROR CODE & MESSAGE
    //public static String MSG_EXCEPTION = "Failed: ";
    //changed by wandy 12-02-2016
    public static String MSG_EXCEPTION = "";
    public static String RESPONSE_FAILED = "Failed response";
    public static String MSG_EMPTY_DATA = "Empty data";
    public static int CODE_FAILED = 0;
    public static int CODE_SUCCESS = 200;
    public static int CODE_EMPTY_DATA = 204;
    public static String BLANK = "";
    //--------------------

    public static String PREFERENCE_SETTING = "setting";
    public static String SETTING_MODEL = "setting_model";
    public static String MEMBER_SETTING_MODEL = "member_setting_model";
    public static String TAGS_LIST = "tags_list";
    public static String EVENT_LIST = "event_list";
    public static String PREFERENCE_LOGIN = "login";
    public static String LOGIN_MODEL = "login_model";
    public static String ACCESS_TOKEN = "access_token";
    public static String PREFERENCE_TAGLIST = "taglist";
    public static String TAGLIST_MODEL = "taglist_model";
    public static String IS_FIRST_RUN = "is_first_run";
    public static String DEVICE_ID = "";

    public static int myPixel(Activity a, int dip) {
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int) ((dip - 0.5f) * scale);
        return pixel;
    }
    //-----

    //public final static String BASE_URL = "http://api-dev.jiggieapp.com/";
    //public final static String BASE_URL = "http://api.jiggieapp.com/";
    public final static String BASE_URL = BuildConfig.BASE_URL;
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
    public final static String URL_GET_ACCESS_TOKEN = BASE_URL + "app/v3/userlogin";
    public final static String URL_TAGSLIST = BASE_URL + "app/v3/user/tagslist";
    public final static String URL_BLOCK_CHAT = BASE_URL + "app/v3/blockuserwithfbid";
    public final static String URL_DELETE_CHAT = BASE_URL + "app/v3/deletemessageswithfbid";
    public final static String URL_ADD_CHAT = BASE_URL + "app/v3/messages/add";
    public final static String URL_VERIFY_PHONE_NUMBER = BASE_URL + "app/v3/user/phone/verification/send/{fb_id}/{phone}";
    public final static String URL_VERIFY_VERIFICATION_CODE = BASE_URL + "app/v3/user/phone/verification/validate/{fb_id}/{token}";
    public final static String URL_WALKTHROUGH = BASE_URL + "app/v3/count_walkthrough";

    public final static String URL_APPSFLYER = BASE_URL + "app/v3/appsflyerinfo";
    public final static String URL_MIXPANEL = BASE_URL + "app/v3/user/sync/superproperties/";

    public final static String URL_PRODUCT_LIST = BASE_URL + "app/v3/product/list/{event_id}";
    public final static String URL_SUMMARY = BASE_URL + "app/v3/product/summary";
    public final static String URL_PAYMENT = BASE_URL + "app/v3/product/payment";
    public final static String URL_GET_CC = BASE_URL + "app/v3/product/credit_card/{fb_id}";

    public static void d(final String tag, final String value) {
        Log.d(tag, value);
    }

    public static final String DATE_TODAY = "today";
    public static final String DATE_TOMORROW = "tomorrow";
    public static final String DATE_UPCOMING = "upcoming";

    public static final String TAG = Utils.class.getSimpleName();
    //AppsFlyer properties----
    public static String AFinstall_type = "";
    public static String AFcampaign = "";
    public static String AFmedia_source = "";

    public static String AFclick_time = "";
    public static String AFinstall_time = "";
    public static String AFsub1 = "";

    public static String AF_ORGANIC = "Organic";
    //------------------------

    public static String calculateTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date d1 = format.parse(date);
            Long current = new Date().getTime();

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            long diff = d1.getTime() - c.getTimeInMillis();
            if(diff < 0)
            {
                return DATE_TODAY;
            }
            else if(diff < (24 * 60 * 60 * 1000))
            {
                return DATE_TOMORROW;
            }
            else return DATE_UPCOMING;
            /*long diff = Math.abs(d1.getTime() - midnight.getTime());
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays == 0)
                return DATE_TODAY;
            else if (diffDays == 1)
                return DATE_TOMORROW;
            else
                return DATE_UPCOMING;
            */
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

    public static String print(Response response) {
        return new Gson().toJson(response.body());
    }

    public static void loadImage(Object object, ImageView target) {
        loadImage(object, target, DiskCacheStrategy.RESULT);
    }

    public static void loadImage(Object object, ImageView target, DiskCacheStrategy cacheStrategy) {
        Glide.with(App.getInstance().getApplicationContext()).load(object).diskCacheStrategy(cacheStrategy).into(target);
    }

    public static int getVersion(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo
                    (context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }
}
