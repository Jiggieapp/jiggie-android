package com.jiggie.android.model;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rangg on 03/11/2015.
 */
public class Common {
    public static final String FIELD_GENDER_INTEREST = "gender_interest";
    public static final String FIELD_ACCOUNT_TYPE = "account_type";
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_FACEBOOK_ID = "fb_id";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_CHAT = "chat";
    public static final String FIELD_FEED = "feed";

    public static String FIELD_MATCH_ME = "matchme";
    public static String FIELD_PHONE = "phone";

    public static String FIELD_EVENT_NAME = "event_name";
    public static String FIELD_EVENT_ID = "event_id";
    public static String FIELD_VENUE_NAME = "venue_name";
    public static String FIELD_STARTTIME = "starttime";
    public static String FIELD_ABOUT = "about";

    public static String FIELD_TRANS_TYPE = "type";
    public static String TYPE_PURCHASE = "purchase";
    public static String TYPE_RESERVATION = "reservation";
    public static String FIELD_GUEST_NAME = "guest_name";
    public static String FIELD_GUEST_EMAIL = "guest_email";
    public static String FIELD_GUEST_PHONE = "about";
    public static String FIELD_PRICE = "price";
    public static String FIELD_PAYMENT_TYPE = "payment_type";
    public static String FIELD_ORDER_ID = "order_id";
    public static String FIELD_WALKTHROUGH_PAYMENT = "walkthrough_payment";
    /*public static String FIELD_TICKET_ID = "ticket_id";
    public static String FIELD_TICKET_NAME = "ticket_name";
    public static String FIELD_TICKET_DESC = "ticket_desc";
    public static String FIELD_MAX_PURCHASE = "max_purchase";
    public static String FIELD_PRICE = "price";*/

    /*"is_new_card": "",
            "type": "bp",
            "token_id": "",
            "order_id": 1457668217795*/

    public static final String PREF_IMAGES_UPLOADED = "images-uploaded";
    public static final String PREF_FACEBOOK_ID = FIELD_FACEBOOK_ID;
    public static final String PREF_FACEBOOK_NAME = "fb_name";
    public static final String PREF_IMAGES = "images";
    public static final String PREF_IMAGE = "image";

    public static final String BUNDLE_IMAGE = PREF_IMAGE;

    public static final SimpleDateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat SERVER_DATE_FORMAT_ALT = new SimpleDateFormat("EEEE, MMMM d, h:mm a", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_12_HOUR_FORMAT = new SimpleDateFormat("h:mm a", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_24_HOUR_FORMAT = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat FACEBOOK_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static final SimpleDateFormat ISO8601_DATE_FORMAT_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat SERVER_DATE_FORMAT_ALT_UTC = new SimpleDateFormat("EEEE, MMMM d, h:mm a", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_12_HOUR_FORMAT_UTC = new SimpleDateFormat("h:mm a", Locale.getDefault());
    public static final SimpleDateFormat SIMPLE_24_HOUR_FORMAT_UTC = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    public static final SimpleDateFormat SHORT_DATE_FORMAT_UTC = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static final SimpleDateFormat SERVER_DATE_FORMAT_COMM = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_COMM_TRANSACTION = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_COMM_TICKET = new SimpleDateFormat("dd MMMM yyyy - HH:mm:ss", Locale.getDefault());

    static {
        final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        ISO8601_DATE_FORMAT_UTC.setTimeZone(utcTimeZone);
        SERVER_DATE_FORMAT_ALT_UTC.setTimeZone(utcTimeZone);
        SIMPLE_12_HOUR_FORMAT_UTC.setTimeZone(utcTimeZone);
        SIMPLE_24_HOUR_FORMAT_UTC.setTimeZone(utcTimeZone);
        SHORT_DATE_FORMAT_UTC.setTimeZone(utcTimeZone);
    }

    /*Social Feeds*/
    public static final String SOCIAL_FEED_TYPE_APPROVED = "approved";
    public static final String PHONE_NUMBER = "phone_number";

    //push notifications
    public static final String KEY = "Jiggie";
    public static final String PUSH_NOTIFICATIONS_FROM_ID = "fromId";
    public static final String PUSH_NOTIFICATIONS_FROM_NAME = "fromName";
    public static final String PUSH_NOTIFICATIONS_TO_ID= "toId";
    public static final String PUSH_NOTIFICATIONS_TYPE_GENERAL = "general";
    public static final String PUSH_NOTIFICATIONS_TYPE_EVENT = "event";
    public static final String PUSH_NOTIFICATIONS_TYPE_MATCH = "match";
    public static final String PUSH_NOTIFICATIONS_TYPE_MESSAGE = "message";
    public static final String PUSH_NOTIFICATIONS_TYPE_SOCIAL = "social";
    public static final String PUSH_NOTIFICATIONS_TYPE_CHAT = "chat";
    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_TYPE = "type";

    public static final String TO_TAB_SOCIAL = "to_tab_social";
    public static final String TO_TAB_CHAT = "to_tab_chat";
}
