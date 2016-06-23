package com.jiggie.android.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.BuildConfig;
import com.jiggie.android.R;
import com.jiggie.android.model.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Response;

/**
 * Created by LTE on 1/29/2016.
 */
public class Utils {
    public static final String FROM_SIGN_IN = "sign_in";
    public static final String FROM_MEMBER_SETTING = "member_setting";
    //public static final String FROM_GET_MEMBER_SETTING = "get_member_setting";
    public static final String FROM_EVENT = "event";
    public static final String FROM_SOCIAL_FEED = "social_feed";
    public static final String FROM_SOCIAL_MATCH = "social_match";
    public static final String FROM_CHAT = "chat";
    public static final String FROM_CHAT_CONVERSATION = "chat_conversation";
    public static final String FROM_BLOCK_CHAT = "block_chat";
    public static final String FROM_DELETE_CHAT = "delete_chat";
    public static final String FROM_ADD_CHAT = "add_chat";
    //public static final String FROM_MORE = "more";
    public static final String FROM_EVENT_DETAIL = "event_detail";
    public static final String FROM_EVENT_GUEST = "event_guest";
    public static final String FROM_PROFILE_DETAIL = "profile_detail";
    public static final String FROM_PROFILE_SETTING = "profile_setting";
    public static final String FROM_PROFILE_EDIT = "profile_edit";
    public static final String FROM_SHARE_LINK = "share_link";
    public static final String FROM_SETUP_TAGS = "setup_tags";
    public static final String FROM_GUEST_CONNECT = "guest_connect";
    public static final String FROM_VERIFY_VERIFICATION_CODE = "verify_verification_code";
    public static final String FROM_WALKTHROUGH = "walkthrough";
    public static final String FROM_PRODUCT_LIST = "product_list";
    public static final String FROM_SUMMARY = "summary";
    public static final String FROM_COMPLETING_WALKTHROUGH_LOCATION = "complete_walkthrough_location";
    public static final String FROM_APPSFLYER = "appsflyer";
    public static final String FROM_MIXPANEL = "mixpanel";
    public static final String HAS_LOAD_GROUP_INFO = "has_load_group_info";

    public static boolean SHOW_WALKTHROUGH_EVENT = true;
    public static boolean SHOW_WALKTHROUGH_SOCIAL = true;
    public static boolean SHOW_WALKTHROUGH_CHAT = true;
    public static final String SET_WALKTHROUGH_EVENT = "walkthrough_event";
    public static final String SET_WALKTHROUGH_SOCIAL = "walkthrough_social";
    public static final String SET_WALKTHROUGH_CHAT = "walkthrough_chat";
    public static final String TAB_EVENT = "event";
    public static final String TAB_CHAT = "chat";
    public static final String TAB_SOCIAL = "social";

    public static final String ACTION_LIKE_YES = "yes";
    public static final String ACTION_LIKE_NO = "no";

    //ERROR CODE & MESSAGE
    //public static final String MSG_EXCEPTION = "Failed: ";
    //changed by wandy 12-02-2016
    public static String MSG_EXCEPTION = "";
    public static final String RESPONSE_FAILED = "Failed response";
    public static final String MSG_EMPTY_DATA = "Empty data";
    public static int CODE_FAILED = 0;
    public static int CODE_SUCCESS = 200;
    public static int CODE_EMPTY_DATA = 204;
    public static final String BLANK = "";
    //--------------------

    public static final String PREFERENCE_SETTING = "setting";
    public static final String SETTING_MODEL = "setting_model";
    public static final String MEMBER_SETTING_MODEL = "member_setting_model";
    public static final String TAGS_LIST = "tags_list";
    public static final String EVENT_LIST = "event_list";
    public static final String PREFERENCE_LOGIN = "login";
    public static final String LOGIN_MODEL = "login_model";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String COUNTER_EVENT = "counter_event";
    public static final String HAS_SET_ALARM = "has_set_alarm";
    public static final String INVITE_CODE = "invite_code";
    public static final String INVITE_CODE_MSG_SHARE = "msg_share";
    public static final String PREFERENCE_TAGLIST = "taglist";
    public static final String PREFERENCE_TAGLIST_NEW = "taglist_new";
    public static final String TAGLIST_MODEL = "taglist_model";
    public static final String IS_FIRST_RUN = "is_first_run";
    public static final String IS_NEED_TO_BE_REDIRECTED_TO_EVENT_DETAIL
            = "is_already_redirected_to_event_detail";
    public static String DEVICE_ID = "";
    public static final String GUEST_DETAilS = "guest_details";
    public static final String VERSION_CODE = "version_code";
    public static final String PREFERENCE_GPS = "gps";

    //Ecommerce var-----------
    public static final String TYPE_CC = "cc";
    public static final String TYPE_VA = "va";
    public static final String TYPE_BP = "bp";
    public static final String TYPE_BCA = "bca";
    //------------------------

    //SALE TYPE var-----------
    public static final String SALE_TYPE = "sale_type";
    public static final String TYPE_MINIMUM_DEPOSIT = "minimum";
    public static final String TYPE_EXACT_PRICE = "exact";
    public static final String TYPE_RESERVE = "reserve";
    //------------------------

    //FIREBASE CHAT------------
    public static final String ROOM_ID = "roomId";
    public static final String ROOM_EVENT = "roomEvent";
    public static final String ROOM_TYPE = "roomType";
    public static final String LOAD_ROOM_DETAIL = "loadRoomDetail";
    //-------------------------

    public static int myPixel(Activity a, int dip) {
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int) ((dip - 0.5f) * scale);
        return pixel;
    }

    public static int myPixel(Context a, int dip) {
        float scale = a.getResources().getDisplayMetrics().density;
        int pixel = (int) ((dip - 0.5f) * scale);
        return pixel;
    }
    //-----

    //public static final String BASE_URL = "http://api-dev.jiggieapp.com/";
    //public static final String BASE_URL = "http://api.jiggieapp.com/";
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String URL_EVENTS = BASE_URL + "app/v3/events/list/{fb_id}";

    public static final String URL_LOGIN = BASE_URL + "app/v3/login";
    public static final String URL_MEMBER_SETTING = BASE_URL + "app/v3/membersettings";
    public static final String URL_GET_SETTING = BASE_URL + "app/v3/membersettings";
    public static final String URL_EVENT_DETAIL = BASE_URL + "app/v3/event/details/{id}/{fb_id}/{gender_interest}";
    public static final String URL_CHAT_CONVERSATION = BASE_URL + "app/v3/chat/conversation/{fb_id}/{to_id}";
    public static final String URL_CHAT_LIST = BASE_URL + "app/v3/conversations";
    public static final String URL_GUEST_INTEREST = BASE_URL + "app/v3/event/interest/{event_id}/{fb_id}/{gender_interest}";
    public static final String URL_MEMBER_INFO = BASE_URL + "app/v3/memberinfo/{fb_id}";
    public static final String URL_SHARE_APPS = BASE_URL + "app/v3/invitelink";
    public static final String URL_SHARE_EVENT = BASE_URL + "app/v3/invitelink";
    public static final String URL_SOCIAL_FEED = BASE_URL + "app/v3/partyfeed/list/{fb_id}/{gender_interest}";
    public static final String URL_GUEST_MATCH = BASE_URL + "app/v3/partyfeed/match/{fb_id}/{from_id}/{type}";
    public static final String URL_SOCIAL_MATCH = BASE_URL + "app/v3/partyfeed_socialmatch/match/{fb_id}/{from_id}/{type}";
    public static final String URL_UPDATE_MATCH_ME = BASE_URL + "app/v3/partyfeed/settings/{fb_id}/{match_me}";
    public static final String URL_EDIT_ABOUT = BASE_URL + "app/v3/updateuserabout";
    public static final String URL_GET_ACCESS_TOKEN = BASE_URL + "app/v3/userlogin";
    public static final String URL_TAGSLIST = BASE_URL + "app/v3/user/tagslist";
    public static final String URL_TAGSLIST_NEW = BASE_URL + "app/v3/user/tagslist2";
    public static final String URL_BLOCK_CHAT = BASE_URL + "app/v3/blockuserwithfbid";
    public static final String URL_DELETE_CHAT = BASE_URL + "app/v3/deletemessageswithfbid";
    public static final String URL_ADD_CHAT = BASE_URL + "app/v3/messages/add";
    public static final String URL_ADD_GROUP_CHAT = BASE_URL + "app/v3/group/firebase";
    public static final String URL_CHAT_FIREBASE = BASE_URL + "app/v3/chat/firebase/{fb_id}";
    public static final String URL_ADD_GROUP_CHAT_JANNES = BASE_URL + "app/v3/group/notif";

    public static final String URL_VERIFY_PHONE_NUMBER = BASE_URL + "app/v3/user/phone/verification/send/{fb_id}/{phone}";
    public static final String URL_VERIFY_VERIFICATION_CODE = BASE_URL + "app/v3/user/phone/verification/validate/{fb_id}/{token}";
    public static final String URL_WALKTHROUGH = BASE_URL + "app/v3/count_walkthrough";
    public static final String URL_GET_ORDER_LIST = BASE_URL + "app/v3/product/order_list/{fb_id}";

    public static final String URL_APPSFLYER = BASE_URL + "app/v3/appsflyerinfo";
    public static final String URL_MIXPANEL = BASE_URL + "app/v3/user/sync/superproperties/";

    public static final String URL_PRODUCT_LIST = BASE_URL + "app/v3/product/list/{event_id}";
    public static final String URL_SUMMARY = BASE_URL + "app/v3/product/summary";
    public static final String URL_PAYMENT = BASE_URL + "app/v3/product/payment";
    public static final String URL_GET_CC = BASE_URL + "app/v3/product/credit_card/{fb_id}";
    public static final String URL_POST_CC = BASE_URL + "app/v3/product/post_cc";
    public static final String URL_DELETE_CC = BASE_URL + "app/v3/product/delete_cc";
    public static final String URL_SUCCESS_SCREEN_VABP = BASE_URL + "app/v3/product/success_screen/{order_id}";
    public static final String URL_SUCCESS_SCREEN_WALKTHROUGH = BASE_URL + "app/v3/product/walkthrough_payment";
    public static final String URL_PAYMENT_METHOD = BASE_URL + "app/v3/product/payment_method";
    public static final String URL_SUPPORT = BASE_URL + "app/v3/product/support";
    public static final String URL_GUEST_INFO = BASE_URL + "app/v3/product/guest_info/{fb_id}";
    public final static String URL_POST_LOCATION = BASE_URL + "app/v3/save_longlat";
    public final static String URL_FREE_PAYMENT = BASE_URL + "app/v3/product/free_payment";
    public static final String URL_GET_THEME = BASE_URL + "app/v3/events/themes";
    public final static String URL_UPLOAD = BASE_URL + "app/v3/member/upload";
    public final static String URL_LIKE_EVENT = BASE_URL + "app/v3/event/likes/{event_id}/{fb_id}/{action}";
    public final static String URL_CITY = BASE_URL + "app/v3/user/citylist";

    public final static String URL_DELETE_PHOTO = BASE_URL + "app/v3/remove_profileimage";
    public final static String URL_POST_FRIEND_LIST = BASE_URL + "app/v3/credit/social_friends";
    public final static String URL_LIST_SOCIAL_FRIENDS = BASE_URL + "app/v3/credit/list_social_friends";

    //INVITE URL=============
    public final static String URL_POST_CONTACT = BASE_URL + "app/v3/credit/contact";
    public final static String URL_INVITE = BASE_URL + "app/v3/credit/invite";
    public final static String URL_INVITE_ALL = BASE_URL + "app/v3/credit/invite_all";
    public final static String URL_GET_INVITE_CODE = BASE_URL + "app/v3/credit/invite_code/{fb_id}";
    //END OF INVITE URL============



    //REDEEM CODE URL===============
    public final static String URL_REDEEM_CODE = BASE_URL + "app/v3/credit/redeem_code";
    //END OF REDEEM CODE URL===============

    //city
    public final static String URL_GET_CITIES = BASE_URL + "app/v3/user/citylist";

    public final static String URL_CREDIT_BALANCE = BASE_URL + "app/v3/credit/balance_credit/{fb_id}";

    public static void d(final String tag, final String value) {
        if(BuildConfig.DEBUG)
        {
            Log.d(tag, value);
        }
    }

    public static final String DATE_TODAY = "today";
    public static final String DATE_TOMORROW = "tomorrow";
    public static final String DATE_UPCOMING = "upcoming";

    public static final String COMM_PRODUCT_LIST = "Product List";
    public static final String COMM_PRODUCT_DETAIL = "Product Detail";
    public static final String COMM_GUEST_INFO = "Guest Info";
    public static final String COMM_PURCHASE_CONFIRMATION = "Purchase Confirmation";
    public static final String COMM_PAYMENT_SELECTION = "Payment Selection";
    public static final String COMM_VA_INSTRUCTION = "VA Instruction";
    public static final String COMM_CREDIT_CARD = "Credit Card";
    public static final String COMM_FINISH_VA = "Commerce Finish VA";
    public static final String COMM_FINISH = "Commerce Finish";
    public static final String COMM_ORDER_LIST = "Order List";

    public static final String REFERRAL_FACEBOOK = "Share Referral Facebook";
    public static final String REFERRAL_PHONE = "Share Referral Phone";
    public static final String REFERRAL_PHONE_SINGULAR = "Share Referral Phone Singular";
    public static final String REFERRAL_PHONE_ALL = "Share Referral Phone All";
    public static final String REFERRAL_MESSAGE = "Share Referral Message";
    public static final String REFERRAL_COPY = "Share Referral Copy";
    public static final String INVITE_FRIENDS_SCREEN = "Invite Friends Screen";

    public static final String PICTURE_UPLOAD = "Picture Upload";
    public static final String PICTURE_DELETE = "Picture Delete";

    public static final String PROMO_CODE = "Use Promo Code";

    public static final String CHANGE_CITY = "Change City";

    public static final String TAG = Utils.class.getSimpleName();
    //AppsFlyer properties----
    public static String AFinstall_type = "";
    public static String AFcampaign = "";
    public static String AFmedia_source = "";

    public static String AFclick_time = "";
    public static String AFinstall_time = "";
    public static String AFsub1 = "";
    public static String AFsub2 = "";

    public static final String AF_ORGANIC = "Organic";
    //------------------------

    public final static String NOL_RUPIAH = "0";
    public final static String TAG_ISREFRESH = "is_refresh";
    public static boolean isRefreshDetail = false;
    public static String event_id_refresh = Utils.BLANK;
    public static int count_like_new = 0;

    public static final String TYPE_ANDROID = "2";

    public static String calculateTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        //format.setTimeZone(Ty/c  xv
        //  meZone.getTimeZone("UTC"));
        try {
            Date d1 = format.parse(date);
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

            //count midnight
            // today
            /*Calendar midnight = new GregorianCalendar();
            // reset hour, minutes, seconds and millis
            midnight.set(Calendar.HOUR_OF_DAY, 0);
            midnight.set(Calendar.MINUTE, 0);
            midnight.set(Calendar.SECOND, 0);
            midnight.set(Calendar.MILLISECOND, 0);
            //end of count midnight

            long diff =  d1.getTime() - midnight.getTimeInMillis();
            if(diff <= 24 * 60 * 60 * 1000)
            {
                return DATE_TODAY;
            }
            else if(diff <= 2 * 24 * 60 * 60 * 1000)
            {
                return DATE_TOMORROW;
            }
            else return DATE_UPCOMING;*/

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

    public static final String print(Response response) {
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

    //broadcast receiver
    public static final String CHECK_NEW_MESSAGE_RECEIVER = "check_new_message_receiver";
    public static final String FETCH_CHAT_RECEIVER = "fetch_chat_receiver";

    public static final String IS_ON = "is_on";

    public static final String PAYMENT_STATUS_AWAITING_PAYMENT = "awaiting_payment";
    public static final String PAYMENT_STATUS_EXPIRE = "expire";
    public static final String PAYMENT_STATUS_PAID = "paid";
    public static final String PAYMENT_STATUS_VOID = "void";
    public static final String PAYMENT_STATUS_REFUND = "refund";
    public static final String PAYMENT_STATUS_RESERVE = "reserved";
    private static final String SCHEME = "jiggie://";
    private static final String SCHEME_HOST_EVENT_DETAIL = "event_detail";
    private static final String SCHEME_HOST_EVENT_LIST = "event_list";
    public static final String[] JIGGIE_URLS =
            { SCHEME + SCHEME_HOST_EVENT_LIST
            , SCHEME + SCHEME_HOST_EVENT_DETAIL };

    private static int screenWidth = 0;
    public static final int REQUEST_CODE_CHOOSE_COUNTRY = 28;
    public static final int REQUEST_CODE_CHOOSE_CITY = 29;


    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isLocationServicesAvailable(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }*/

        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            isAvailable = true;
        }else{
            isAvailable = false;
        }

        /*boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);*/

        //return isAvailable && (coarsePermissionCheck || finePermissionCheck);
        return  isAvailable;
    }

    public static String getTimeForEvent(Date startDate, Date endDate, String timezone)
    {
        /*if(timezone == null || timezone.isEmpty())
        {
            timezone = "7";
        }

        if(timezone == null || timezone.isEmpty())
        {
            format.setTimeZone(TimeZone.getDefault());
        }*/
        String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format
                , Common.getStartDateTimeInTimezone(timezone).format(startDate)
                , Common.getEndDateTimeInTimezone(timezone).format(endDate));
        return simpleDate;
    }
}
