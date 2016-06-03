package com.jiggie.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.appsflyer.AppsFlyerLib;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.jiggie.android.component.SimpleJSONObject;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.database.DatabaseConnection;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.TrackManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.PostAppsFlyerModel;
import com.jiggie.android.model.PostMixpanelModel;
import com.jiggie.android.model.PromoMixpanelModel;
import com.jiggie.android.model.ReferEventMixpanelModel;
import com.jiggie.android.model.SettingModel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

import static android.support.v4.app.ActivityCompat.finishAffinity;
import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by rangg on 21/10/2015.
 */
public class App extends Application {
    private DisplayMetrics displayMetrics;
    private DatabaseConnection database;
    private String simOperatorName;
    private String phoneType;
    private String deviceId;
    private String osName;

    private static App instance;
    static String mPackageName;

    public static MixpanelAPI mixpanelAPI;
    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    public static Activity runningActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        mPackageName = getApplicationContext().getPackageName();
        //region Initialize third party libraries

        FacebookSdk.sdkInitialize(this);
        AppsFlyerLib.setAppsFlyerKey(super.getString(R.string.appsflyer_devkey));
        //Fabric.with(this, new Crashlytics());
        //endregion
        this.database = new DatabaseConnection(this);

        // Setup handler for uncaught exceptions.
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            // log it & phone home.
            if (runningActivity != null) {
                finishAffinity(runningActivity);
            }
            androidDefaultUEH.uncaughtException(thread, ex);
        }
    };


    @SuppressWarnings("unused")
    @SuppressLint("PackageManagerGetSignatures")
    private void detectSignature() {
        final SharedPreferences preferences = getSharedPreferences();

        if (preferences.getStringSet("signatures", null) == null) {
            try {
                final PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
                final HashSet<String> signatures = new HashSet<>(info.signatures.length);
                final MessageDigest md = MessageDigest.getInstance("SHA");

                for (Signature signature : info.signatures) {
                    md.update(signature.toByteArray());
                    signatures.add(Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }

                preferences.edit().putStringSet("signatures", signatures).apply();
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                // ignore
            }
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static String getErrorMessage(Throwable throwable) {
        if (throwable instanceof VolleyError)
            return VolleyHandler.parseError(instance, (VolleyError) throwable);
        else if (!TextUtils.isEmpty(throwable.getLocalizedMessage()))
            return throwable.getLocalizedMessage();
        else if (!TextUtils.isEmpty(throwable.getMessage()))
            return throwable.getMessage();
        else return throwable.getClass().toString();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.show();
        return dialog;
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(instance);
    }

    public static void safeClose(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String getCachePath(String subPath) {
        File rootCache = (rootCache = super.getExternalCacheDir()) == null ? super.getCacheDir() : rootCache;
        rootCache = subPath == null ? new File(rootCache.getAbsolutePath()) : new File(rootCache.getAbsolutePath() + '/' + subPath);
        if (!rootCache.exists())
            rootCache.mkdirs();
        return rootCache.getAbsolutePath() + '/';
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String getDataPath(String subPath) {
        final String dataPath = "/Android/data/" + super.getPackageName();
        File rootPath = (rootPath = Environment.getExternalStorageDirectory()) == null ? super.getFilesDir() : rootPath;
        rootPath = subPath == null ? new File(rootPath.getAbsolutePath() + dataPath) : new File(rootPath.getAbsolutePath() + dataPath + '/' + subPath);
        if (!rootPath.exists())
            rootPath.mkdirs();
        return rootPath.getAbsolutePath() + '/';
    }

    public static String getFacebookImage(String facebookId, int width) {
        width = width == 0 ? 1000 : width;
        return String.format("https://graph.facebook.com/%s/picture?width=%d&height=%d", facebookId, width, width);
    }

    public DatabaseConnection getDatabase() {
        return this.database;
    }

    @Override
    public void onTerminate() {
        this.database.close();
        super.onTerminate();
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            super.unregisterReceiver(receiver);
        } catch (IllegalArgumentException unused) {
            //receiver already unregistered, let's ignore it---
        }
    }

    public void trackMixPanelEvent(String eventName) {
        this.trackMixPanelEvent(eventName, new SimpleJSONObject());
    }

    public void trackMixPanelEvent(String eventName, SimpleJSONObject json) {
        final String location = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin().getLocation();

        String[] locations = null;
        try {
            locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
        } catch (Exception e) {

        }
        final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network = connManager.getActiveNetworkInfo();

        final LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        final SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();


        if (eventName.equals("Install")) {
            if (!Utils.AFmedia_source.equals(""))
                json.putString("AFmedia_source", Utils.AFmedia_source);
            else
                json.putString("AFmedia_source", Utils.AF_ORGANIC);
            if (!Utils.AFcampaign.equals(""))
                json.putString("AFcampaign", Utils.AFcampaign);
            else
                json.putString("AFcampaign", Utils.AF_ORGANIC);
            if (!Utils.AFinstall_type.equals(""))
                json.putString("AFinstall_type", Utils.AFinstall_type);
            else
                json.putString("AFinstall_type", Utils.AF_ORGANIC);
        } else {
            if (!Utils.AFmedia_source.equals(""))
                json.putString("AFmedia_source", Utils.AFmedia_source);
            if (!Utils.AFcampaign.equals(""))
                json.putString("AFcampaign", Utils.AFcampaign);
            if (!Utils.AFinstall_type.equals(""))
                json.putString("AFinstall_type", Utils.AFinstall_type);
        }


        //Added by Aga
        json.putString("App Release", getVersionName(this));
        json.putString("App Version", getVersionCode(this));
        //------------

        json.putString("Carrier", this.getSimOperatorName());
        try {
            json.putString("City", locations[0].trim());
        } catch (Exception e) {
            json.putString("City", Utils.BLANK);
        }

        try {
            json.putString("Country", locations[1].trim());
        } catch (Exception e) {
            json.putString("Country", Utils.BLANK);
        }
        json.putString("Device Model", Build.MODEL);
        json.putString("Manufacturer", Build.MANUFACTURER);

        json.putString("Model", Build.MODEL);
        json.putString("OS Version", this.getDeviceOSName());
        json.putString("Radio", this.getPhoneType());
        json.putString("Screen Height", String.valueOf(this.getDisplayMetrics().heightPixels));
        json.putString("Screen Width", String.valueOf(this.getDisplayMetrics().widthPixels));
        json.putBoolean("Wifi", (network != null) && (network.getType() == ConnectivityManager.TYPE_WIFI));
        json.putString("Time", Common.ISO8601_DATE_FORMAT_UTC.format(new Date()));
        json.putString("iOS IFA", this.getDeviceId());
        json.putString("Operating System", "Android");
        json.putString("Mixpanel Library", "Android");
        //json.putString("Library Version", "");
        json.putString("Region", locations[0]);


        if (login != null) {
            json.putString("Distinct Id", login.getFb_id());
            try {
                json.putString("age", StringUtility.getAge2(login.getBirthday()));
            } catch (Exception e) {

            }
            json.putString("birthday", login.getBirthday());
            json.putString("email", login.getEmail());
            json.putString("first_name", login.getUser_first_name());
            json.putString("last_name", login.getUser_last_name());
            json.putString("name_and_fb_id", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());

            if (settingModel != null) {
                json.putString("gender", settingModel.getData().getGender());
                json.putString("gender_interest", settingModel.getData().getGender_interest());

                if (eventName.equals("Sign Up")) {
                    json.putString("invite_code", settingModel.getInvite_code());
                }else if (eventName.equals("Log In")) {
                    json.putString("invite_code", settingModel.getInvite_code());
                }


            }
        }

        if (eventName.equals("Sign Up")) {
            getInstanceMixpanel().alias(login.getFb_id(), null);
            setPeopleMixpanel(login, settingModel);
            setSuperPropertiesMixpanel(login, settingModel);
            setSyncMixpanel(login, settingModel);
            setAppsFlyer(login);
        } else if (eventName.equals("Log In")) {
            getInstanceMixpanel().identify(mixpanelAPI.getDistinctId());
            //getInstanceMixpanel().identify(login.getFb_id());
            //getInstanceMixpanel().alias(login.getFb_id(), null);
            setPeopleMixpanel(login, settingModel);
            setSuperPropertiesMixpanel(login, settingModel);
            setSyncMixpanel(login, settingModel);
            setAppsFlyer(login);
        }
        getInstanceMixpanel().track(eventName, json);
    }

    public void trackMixPanelCommerce(String eventName, CommEventMixpanelModel commEventMixpanelModel) {

        //USER PART-----------
        LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();
        //setPeopleMixpanelComm(login, settingModel);
        //END OF USER PART---------

        //EVENT PART--------------
        setEventMixpanelComm(eventName, login, commEventMixpanelModel);
        //END OF EVENT PART-------

    }

    private void setEventMixpanelComm(String eventName, LoginModel login, CommEventMixpanelModel commEventMixpanelModel) {
        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("First Name", login.getUser_first_name());
            json.putString("Last Name", login.getUser_last_name());
            final String location = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin().getLocation();

            String[] locations = null;
            try {
                locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
            } catch (Exception e) {

            }
            String city, country;
            try {
                city = locations[0].trim();
            } catch (Exception e) {
                city = Utils.BLANK;
            }
            try {
                country = locations[1].trim();
            } catch (Exception e) {
                country = Utils.BLANK;
            }
            json.putString("Location", city + " " + country);

        }

        if (!eventName.equals(Utils.COMM_ORDER_LIST)) {
            json.putString("Event Name", commEventMixpanelModel.getEventName());
            json.putString("Event Venue Name", commEventMixpanelModel.getEventVenueName());
            json.putString("Event Venue City", commEventMixpanelModel.getEventVenueCity());
            json.putString("Event Start Date", commEventMixpanelModel.getEventStartDate());
            json.putString("Event End Date", commEventMixpanelModel.getEventEndDate());
            json.putString("Event Tags", commEventMixpanelModel.getTag().toString());
            json.putString("Event Description", commEventMixpanelModel.getEventDescription());
        }

        if (!eventName.equals(Utils.COMM_PRODUCT_LIST) && !eventName.equals(Utils.COMM_ORDER_LIST)) {
            json.putString("Ticket Name", commEventMixpanelModel.getTicketName());
            json.putString("Ticket Type", commEventMixpanelModel.getTicketType());
            json.putString("Ticket Price", commEventMixpanelModel.getTicketPrice());
            json.putString("Ticket Max Per Guest", commEventMixpanelModel.getTicketMaxPerGuest());
        }

        if (eventName.equals(Utils.COMM_FINISH_VA) || eventName.equals(Utils.COMM_FINISH)) {
            json.putString("Purchase Quantity", commEventMixpanelModel.getPurchaseQuantity());
            json.putString("Purchase Amount", commEventMixpanelModel.getPurchaseAmount());
            json.putString("Purchase Discount", commEventMixpanelModel.getPurchaseDiscount());
            json.putString("Purchase Payment", commEventMixpanelModel.getPurchasePayment());
            json.putString("Purchase Quantity", commEventMixpanelModel.getPurchaseQuantity());
            if (commEventMixpanelModel.isReservation()) {
                json.putString("Total Guest", commEventMixpanelModel.getTotalGuest());
            }
        }
        getInstanceMixpanel().track(eventName, json);
    }

    public void setPeopleMixpanelComm(LoginModel login, SettingModel settingModel) {
        getInstanceMixpanel().getPeople().identify(mixpanelAPI.getDistinctId());

        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("FB ID", login.getFb_id());
            json.putString("First Name", login.getUser_first_name());
            json.putString("Last Name", login.getUser_last_name());
            json.putString("Device", Build.DEVICE);
            json.putString("Birthday", login.getBirthday());
            json.putString("City Country", login.getBirthday());

            final String location = login == null ? null : login.getLocation();
            String[] locations = null;
            try {
                locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
            } catch (Exception e) {

            }
            try {
                json.putString("City Country", locations[0].trim() + " " + locations[1].trim());
            } catch (Exception e) {
                json.putString("City Country", locations[0].trim());
            }
            json.putString("Email", login.getEmail());

            if (settingModel != null) {
                json.putString("Gender", settingModel.getData().getGender());
                json.putString("Gender Interest", settingModel.getData().getGender_interest());
            }

            json.putString("Name and FB ID", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
        }
        json.putString("App Version", getVersionCode(this));
        json.putString("OS", "Android");
        json.putString("OS Version", this.getDeviceOSName());
        getInstanceMixpanel().getPeople().set(json);
    }

    public MixpanelAPI getInstanceMixpanel() {
        if (mixpanelAPI == null)
            mixpanelAPI = MixpanelAPI.getInstance(this, super.getString(R.string.mixpanel_token));
        return mixpanelAPI;
    }

    //TRACK MIXPANEL REFERRAL===================
    public void trackMixPanelReferral(String eventName, ReferEventMixpanelModel referEventMixpanelModel) {

        //USER PART-----------
        LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();
        //setPeopleMixpanelRefer(login, settingModel);
        //END OF USER PART---------

        //EVENT PART--------------
        setEventMixpanelRefer(eventName, referEventMixpanelModel);
        //END OF EVENT PART-------

    }

    private void setEventMixpanelRefer(String eventName, ReferEventMixpanelModel referEventMixpanelModel) {
        SimpleJSONObject json = new SimpleJSONObject();

        if(eventName.equals(Utils.INVITE_FRIENDS_SCREEN)){
            json.putString("Promo Code", referEventMixpanelModel.getPromo_code());
        }else if (eventName.equals(Utils.REFERRAL_FACEBOOK)||eventName.equals(Utils.REFERRAL_PHONE)||eventName.equals(Utils.REFERRAL_PHONE_ALL)||eventName.equals(Utils.REFERRAL_MESSAGE)||
                eventName.equals(Utils.REFERRAL_COPY)) {
            json.putString("Promo Code", referEventMixpanelModel.getPromo_code());
            json.putString("Promo URL", referEventMixpanelModel.getPromo_url());
        }else{
            json.putString("Promo Code", referEventMixpanelModel.getPromo_code());
            json.putString("Promo URL", referEventMixpanelModel.getPromo_url());
            json.putString("Contact Full Name", referEventMixpanelModel.getContact_fullname());
            json.putString("Contact Email", referEventMixpanelModel.getContact_email().toString());
            json.putString("Contact Phone", referEventMixpanelModel.getContact_phone().toString());
        }
        getInstanceMixpanel().track(eventName, json);
    }

    public void setPeopleMixpanelRefer(LoginModel login, SettingModel settingModel) {
        getInstanceMixpanel().getPeople().identify(mixpanelAPI.getDistinctId());

        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("FB ID", login.getFb_id());
            json.putString("First Name", login.getUser_first_name());
            json.putString("Last Name", login.getUser_last_name());
            json.putString("Device", Build.DEVICE);
            json.putString("Birthday", login.getBirthday());
            json.putString("City Country", login.getBirthday());

            final String location = login == null ? null : login.getLocation();
            String[] locations = null;
            try {
                locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
            } catch (Exception e) {

            }
            try {
                json.putString("City Country", locations[0].trim() + " " + locations[1].trim());
            } catch (Exception e) {
                json.putString("City Country", locations[0].trim());
            }
            json.putString("Email", login.getEmail());

            if (settingModel != null) {
                json.putString("Gender", settingModel.getData().getGender());
                json.putString("Gender Interest", settingModel.getData().getGender_interest());
            }

            json.putString("Name and FB ID", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
        }
        json.putString("App Version", getVersionCode(this));
        json.putString("OS", "Android");
        json.putString("OS Version", this.getDeviceOSName());
        getInstanceMixpanel().getPeople().set(json);
    }
    //END OF TRACK MIXPANEL REFERRAL============

    //Track MIXPANEL PICTURE UPLOAD======================
    public void trackMixPanelPictureUp(String eventName, String urlImage) {

        //USER PART-----------
        LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();
        //setPeopleMixpanelPictureUp(login, settingModel);
        //END OF USER PART---------

        //EVENT PART--------------
        setEventMixpanelPictureUp(eventName, urlImage);
        //END OF EVENT PART-------

    }

    private void setEventMixpanelPictureUp(String eventName, String urlImage) {
        SimpleJSONObject json = new SimpleJSONObject();

        json.putString("Image URL", urlImage);
        getInstanceMixpanel().track(eventName, json);
    }

    public void setPeopleMixpanelPictureUp(LoginModel login, SettingModel settingModel) {
        getInstanceMixpanel().getPeople().identify(mixpanelAPI.getDistinctId());

        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("FB ID", login.getFb_id());
            json.putString("First Name", login.getUser_first_name());
            json.putString("Last Name", login.getUser_last_name());
            json.putString("Device", Build.DEVICE);
            json.putString("Birthday", login.getBirthday());
            json.putString("City Country", login.getBirthday());

            final String location = login == null ? null : login.getLocation();
            String[] locations = null;
            try {
                locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
            } catch (Exception e) {

            }
            try {
                json.putString("City Country", locations[0].trim() + " " + locations[1].trim());
            } catch (Exception e) {
                json.putString("City Country", locations[0].trim());
            }
            json.putString("Email", login.getEmail());

            if (settingModel != null) {
                json.putString("Gender", settingModel.getData().getGender());
                json.putString("Gender Interest", settingModel.getData().getGender_interest());
            }

            json.putString("Name and FB ID", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
        }
        json.putString("App Version", getVersionCode(this));
        json.putString("OS", "Android");
        json.putString("OS Version", this.getDeviceOSName());
        getInstanceMixpanel().getPeople().set(json);
    }
    //END OF MIXPANEL PICTURE UPLOAD======================

    //MIXPANEL PROMO CODE=================================
    public void trackMixPanelPromo(String eventName, PromoMixpanelModel promoMixpanelModel) {

        //USER PART-----------
        LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();
        //setPeopleMixpanelPromo(login, settingModel);
        //END OF USER PART---------

        //EVENT PART--------------
        setEventMixpanelPromo(eventName, promoMixpanelModel);
        //END OF EVENT PART-------
    }

    private void setEventMixpanelPromo(String eventName, PromoMixpanelModel promoMixpanelModel) {
        SimpleJSONObject json = new SimpleJSONObject();

        json.putString("Code", promoMixpanelModel.getCode());
        json.putString("Status", promoMixpanelModel.getStatus());
        json.putString("Response Message", promoMixpanelModel.getMessage());
        getInstanceMixpanel().track(eventName, json);
    }

    public void setPeopleMixpanelPromo(LoginModel login, SettingModel settingModel) {
        getInstanceMixpanel().getPeople().identify(mixpanelAPI.getDistinctId());

        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("FB ID", login.getFb_id());
            json.putString("First Name", login.getUser_first_name());
            json.putString("Last Name", login.getUser_last_name());
            json.putString("Device", Build.DEVICE);
            json.putString("Birthday", login.getBirthday());
            json.putString("City Country", login.getBirthday());

            final String location = login == null ? null : login.getLocation();
            String[] locations = null;
            try {
                locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
            } catch (Exception e) {

            }
            try {
                json.putString("City Country", locations[0].trim() + " " + locations[1].trim());
            } catch (Exception e) {
                json.putString("City Country", locations[0].trim());
            }
            json.putString("Email", login.getEmail());

            if (settingModel != null) {
                json.putString("Gender", settingModel.getData().getGender());
                json.putString("Gender Interest", settingModel.getData().getGender_interest());
            }

            json.putString("Name and FB ID", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
        }
        json.putString("App Version", getVersionCode(this));
        json.putString("OS", "Android");
        json.putString("OS Version", this.getDeviceOSName());
        getInstanceMixpanel().getPeople().set(json);
    }
    //END OF MIXPANEL PROMO CODE=================================

    public void setSuperPropertiesMixpanel(LoginModel login, SettingModel settingModel) {
        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("first_name", login.getUser_first_name());
            json.putString("last_name", login.getUser_last_name());
            json.putString("birthday", login.getBirthday());
            json.putString("email", login.getEmail());
            json.putString("location", login.getLocation());
            try {
                json.putString("age", StringUtility.getAge2(login.getBirthday()));
            } catch (Exception e) {

            }
        }

        if (settingModel != null) {
            json.putString("gender", settingModel.getData().getGender());
            json.putString("gender_interest", settingModel.getData().getGender_interest());
        }

        json.putString("os_version", this.getDeviceOSName());
        json.putString("device_type", Build.MODEL);
        json.putString("app_version", getVersionCode(this));
        getInstanceMixpanel().registerSuperProperties(json);
    }

    public void setPeopleMixpanel(LoginModel login, SettingModel settingModel) {
        //getInstanceMixpanel().identify(mixpanelAPI.getDistinctId());
        getInstanceMixpanel().getPeople().identify(mixpanelAPI.getDistinctId());

        SimpleJSONObject json = new SimpleJSONObject();
        if (login != null) {
            json.putString("first_name", login.getUser_first_name());
            json.putString("last_name", login.getUser_last_name());
            json.putString("birthday", login.getBirthday());
            json.putString("email", login.getEmail());
            json.putString("fb_id", login.getFb_id());
            json.putString("name_and_fb_id", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
            try {
                json.putString("age", StringUtility.getAge2(login.getBirthday()));
            } catch (Exception e) {

            }
        }

        if (settingModel != null) {
            json.putString("gender", settingModel.getData().getGender());
            json.putString("gender_interest", settingModel.getData().getGender_interest());
        }

        json.putString("app_version", getVersionCode(this));

        getInstanceMixpanel().getPeople().set(json);
    }

    public void trackMixPanelViewEventDetail(String eventName, EventDetailModel.Data.EventDetail eventDetail) {
        SimpleJSONObject json = new SimpleJSONObject();
        final String location = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin().getLocation();

        String[] locations = null;
        try {
            locations = TextUtils.isEmpty(location) ? new String[]{"", ""} : location.split(",");
        } catch (Exception e) {

        }

        final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network = connManager.getActiveNetworkInfo();

        final LoginModel login = AccountManager.loadLogin() == null ? null : AccountManager.loadLogin();
        final SettingModel settingModel = AccountManager.loadSetting() == null ? null : AccountManager.loadSetting();

        if (eventName.equals("Install")) {
            if (!Utils.AFmedia_source.equals(""))
                json.putString("AFmedia_source", Utils.AFmedia_source);
            else
                json.putString("AFmedia_source", Utils.AF_ORGANIC);
            if (!Utils.AFcampaign.equals(""))
                json.putString("AFcampaign", Utils.AFcampaign);
            else
                json.putString("AFcampaign", Utils.AF_ORGANIC);
            if (!Utils.AFinstall_type.equals(""))
                json.putString("AFinstall_type", Utils.AFinstall_type);
            else
                json.putString("AFinstall_type", Utils.AF_ORGANIC);
        } else {
            if (!Utils.AFmedia_source.equals(""))
                json.putString("AFmedia_source", Utils.AFmedia_source);
            if (!Utils.AFcampaign.equals(""))
                json.putString("AFcampaign", Utils.AFcampaign);
            if (!Utils.AFinstall_type.equals(""))
                json.putString("AFinstall_type", Utils.AFinstall_type);
        }

        //Added by Aga
        json.putString("App Release", getVersionName(this));
        json.putString("App Version", getVersionCode(this));
        //------------

        json.putString("Carrier", this.getSimOperatorName());

        try {
            json.putString("City", locations[0].trim());
        } catch (Exception e) {
            json.putString("City", Utils.BLANK);
        }

        try {
            json.putString("Country", locations[1].trim());
        } catch (Exception e) {
            json.putString("Country", Utils.BLANK);
        }


        json.putString("Device Model", Build.MODEL);
        json.putString("Manufacturer", Build.MANUFACTURER);

        json.putString("Model", Build.MODEL);
        json.putString("OS Version", this.getDeviceOSName());
        json.putString("Radio", this.getPhoneType());
        json.putString("Screen Height", String.valueOf(this.getDisplayMetrics().heightPixels));
        json.putString("Screen Width", String.valueOf(this.getDisplayMetrics().widthPixels));
        json.putBoolean("Wifi", (network != null) && (network.getType() == ConnectivityManager.TYPE_WIFI));
        json.putString("Time", Common.ISO8601_DATE_FORMAT_UTC.format(new Date()));
        json.putString("iOS IFA", this.getDeviceId());
        json.putString("Operating System", "Android");
        json.putString("Mixpanel Library", "Android");
        //json.putString("Library Version", "");

        try {
            json.putString("Region", locations[0]);
        } catch (Exception e) {
            json.putString("Region", Utils.BLANK);
        }


        if (login != null) {
            json.putString("Distinct Id", login.getFb_id());
            try {
                json.putString("age", StringUtility.getAge2(login.getBirthday()));
            } catch (Exception e) {

            }
            json.putString("birthday", login.getBirthday());
            json.putString("email", login.getEmail());
            json.putString("first_name", login.getUser_first_name());
            json.putString("last_name", login.getUser_last_name());
            json.putString("name_and_fb_id", login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());

            if (settingModel != null) {
                json.putString("gender", settingModel.getData().getGender());
                json.putString("gender_interest", settingModel.getData().getGender_interest());
            }
        }

        if (eventName.equals("View Event Details")||eventName.equals("Like Event Detail")||eventName.equals("Unlike Event Detail")) {
            if (eventDetail != null) {
                try {
                    json.putString("Event Description", eventDetail.getDescription());
                } catch (Exception e) {
                    json.putString("Event Description", Utils.BLANK);
                }
                try {
                    json.putString("Event Name", eventDetail.getTitle());
                } catch (Exception e) {
                    json.putString("Event Name", Utils.BLANK);
                }
                try {
                    json.putString("Event Tags", eventDetail.getTags().toString());
                } catch (Exception e) {
                    json.putString("Event Tags", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Description", eventDetail.getVenue().getDescription());
                } catch (Exception e) {
                    json.putString("Event Venue Description", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Neighborhood", eventDetail.getVenue().getNeighborhood());
                } catch (Exception e) {
                    json.putString("Event Venue Neighborhood", Utils.BLANK);
                }
                try {
                    json.putString("Event End Time", eventDetail.getEnd_datetime());
                } catch (Exception e) {
                    json.putString("Event End Time", Utils.BLANK);
                }
                try {
                    json.putString("Event Start Time", eventDetail.getStart_datetime());
                } catch (Exception e) {
                    json.putString("Event Start Time", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue City", eventDetail.getVenue().getCity());
                } catch (Exception e) {
                    json.putString("Event Venue City", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Name", eventDetail.getVenue_name());
                } catch (Exception e) {
                    json.putString("Event Venue Name", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Zip", eventDetail.getVenue().getZip());
                } catch (Exception e) {
                    json.putString("Event Venue Zip", Utils.BLANK);
                }

            }
        } else if (eventName.equals("Share Event")) {
            if (eventDetail != null) {
                try {
                    json.putString("Event Description", eventDetail.getDescription());
                } catch (Exception e) {
                    json.putString("Event Description", Utils.BLANK);
                }
                try {
                    json.putString("Event Id", eventDetail.get_id());
                } catch (Exception e) {
                    json.putString("Event Id", Utils.BLANK);
                }
                try {
                    json.putString("Event Name", eventDetail.getTitle());
                } catch (Exception e) {
                    json.putString("Event Name", Utils.BLANK);
                }
                try {
                    json.putString("Event Tags", eventDetail.getTags().toString());
                } catch (Exception e) {
                    json.putString("Event Tags", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Description", eventDetail.getVenue().getDescription());
                } catch (Exception e) {
                    json.putString("Event Venue Description", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Neighborhood", eventDetail.getVenue().getNeighborhood());
                } catch (Exception e) {
                    json.putString("Event Venue Neighborhood", Utils.BLANK);
                }
                try {
                    json.putString("Event End Time", eventDetail.getEnd_datetime());
                } catch (Exception e) {
                    json.putString("Event End Time", Utils.BLANK);
                }
                try {
                    json.putString("Event Start Time", eventDetail.getStart_datetime());
                } catch (Exception e) {
                    json.putString("Event Start Time", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue City", eventDetail.getVenue().getCity());
                } catch (Exception e) {
                    json.putString("Event Venue City", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Name", eventDetail.getVenue_name());
                } catch (Exception e) {
                    json.putString("Event Venue Name", Utils.BLANK);
                }
                try {
                    json.putString("Event Venue Zip", eventDetail.getVenue().getZip());
                } catch (Exception e) {
                    json.putString("Event Venue Zip", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter Email", login.getEmail());
                    }

                } catch (Exception e) {
                    json.putString("Inviter Email", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter First Name", login.getUser_first_name());
                    }

                } catch (Exception e) {
                    json.putString("Inviter First Name", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter Last Name", login.getUser_last_name());
                    }

                } catch (Exception e) {
                    json.putString("Inviter Last Name", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter Birthday", login.getBirthday());
                    }

                } catch (Exception e) {
                    json.putString("Inviter Birthday", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter FB ID", login.getFb_id());
                    }

                } catch (Exception e) {
                    json.putString("Inviter FB ID", Utils.BLANK);
                }
                try {
                    if (settingModel != null) {
                        json.putString("Inviter Gender", settingModel.getData().getGender());
                    }

                } catch (Exception e) {
                    json.putString("Inviter Gender", Utils.BLANK);
                }
                try {
                    if (login != null) {
                        json.putString("Inviter Whole Name", login.getUser_first_name() + " " + login.getUser_last_name());
                    }

                } catch (Exception e) {
                    json.putString("Inviter Whole Name", Utils.BLANK);
                }
            }
        }

        getInstanceMixpanel().track(eventName, json);
    }

    //Added by Aga
    public static String getVersionName(Context c) {
        PackageInfo pi = null;
        try {
            pi = c.getPackageManager()
                    .getPackageInfo(mPackageName, PackageManager.GET_META_DATA);

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return pi.versionName;
    }

    private static String getVersionCode(Context c) {
        PackageInfo pi = null;
        try {
            pi = c.getPackageManager()
                    .getPackageInfo(mPackageName, PackageManager.GET_META_DATA);

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return String.valueOf(pi.versionCode);
    }
    //----------------------

    public String getDeviceOSName() {
        if (this.osName == null) {
            final Field[] fields = Build.VERSION_CODES.class.getFields();
            final Object placeHolder = new Object();
            int index = fields.length;

            while ((--index) >= 0) {
                try {
                    final Field field = fields[index];
                    final int fieldValue = field.getInt(placeHolder);

                    if (fieldValue == Build.VERSION.SDK_INT) {
                        this.osName = field.getName();
                        break;
                    }
                } catch (IllegalAccessException e) {
                    // wrong field, just skip it.
                }
            }
        }
        return this.osName;
    }

    private DisplayMetrics getDisplayMetrics() {
        if (this.displayMetrics == null) {
            final DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) super.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
            this.displayMetrics = metrics;
        }
        return this.displayMetrics;
    }

    private String getDeviceId() {
        /*if (this.deviceId == null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                checkPermission(Manifest.permission.READ_PHONE_STATE, App.getInstance().getApplicationContext());
            }
            else
            {
                final TelephonyManager telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
                final String deviceId = telephonyManager.getDeviceId();
                this.deviceId = ((TextUtils.isEmpty(deviceId)) || (deviceId.equals("000000000000000"))) ? Settings.Secure.getString(super.getContentResolver(), Settings.Secure.ANDROID_ID) : deviceId;
            }
        }
        return this.deviceId;*/

        /*if(this.deviceId == null)
        {
            final String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver()
                    , android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode()
                    , ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            this.deviceId = deviceUuid.toString();
        }
        return this.deviceId;*/

        if (this.deviceId == null) {
            this.deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        Utils.DEVICE_ID = deviceId;
        return this.deviceId;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(final String permission
            , final Activity activity) {
        int hasWriteContactsPermission = checkSelfPermission(permission);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(activity, new String[]{permission},
                    Utils.PERMISSION_REQUEST);
            return;
        } else return;
    }

    private String getSimOperatorName() {
        if (this.simOperatorName == null) {
            final TelephonyManager telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
            this.simOperatorName = telephonyManager.getSimOperatorName();
        }
        return this.simOperatorName;
    }

    private String getPhoneType() {
        if (this.phoneType == null) {
            final TelephonyManager telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
                this.phoneType = "GSM";
            else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
                this.phoneType = "CDMA";
            else
                this.phoneType = "";
        }
        return this.phoneType;
    }

    public boolean isUserLoggedIn() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean facebookValid = accessToken != null && !accessToken.isExpired() && !TextUtils.isEmpty(accessToken.getUserId());
        return facebookValid && getSharedPreferences().getBoolean("loggedIn", false);
    }

    public void setUserLoggedIn() {
        getSharedPreferences().edit().putBoolean("loggedIn", true).apply();
    }


    public static void savePreference(final String key, final Object object) {
        SharedPreferences.Editor sharedPreferencesEditor = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .edit();
        if (object instanceof Integer) {
            sharedPreferencesEditor.putInt(key, (Integer) object);
        } else if (object instanceof String) {
            sharedPreferencesEditor.putString(key, (String) object);
        } else if (object instanceof Boolean) {
            sharedPreferencesEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Long) {
            sharedPreferencesEditor.putLong(key, (Long) object);
        } else if (object instanceof Float) {
            sharedPreferencesEditor.putFloat(key, (Float) object);
        } else if (object instanceof Set<?>) {
            sharedPreferencesEditor.putStringSet(key, (Set<String>) object);
        }
        sharedPreferencesEditor.apply();
    }

    private static String idChatActive = "";

    public static void setIdChatActive(String id) {
        idChatActive = id;
    }

    public static String getIdChatActive() {
        return idChatActive;
    }

    public void setSyncMixpanel(LoginModel login, SettingModel settingModel) {
        //sync mixpanel API
        PostMixpanelModel postMixpanelModel = new PostMixpanelModel();
        postMixpanelModel.setDevice_type(Build.MODEL);
        postMixpanelModel.setOs_version(this.getDeviceOSName());
        postMixpanelModel.setApp_version(getVersionCode(this));
        if (login != null) {
            postMixpanelModel.setFb_id(login.getFb_id());
            postMixpanelModel.setLocation(login.getLocation());
            postMixpanelModel.setName_and_fb_id(login.getUser_first_name() + "_" + login.getUser_last_name() + "_" + login.getFb_id());
            postMixpanelModel.setEmail(login.getEmail());
            postMixpanelModel.setLast_name(login.getUser_last_name());
            postMixpanelModel.setBirthday(login.getBirthday());
            postMixpanelModel.setFirst_name(login.getUser_first_name());
            try {
                postMixpanelModel.setAge(StringUtility.getAge2(login.getBirthday()));
            } catch (Exception e) {

            }
        }

        if (settingModel != null) {
            postMixpanelModel.setGender(settingModel.getData().getGender());
            postMixpanelModel.setGender_interest(settingModel.getData().getGender_interest());
            postMixpanelModel.setInvite_code(settingModel.getInvite_code());
        }

        if (login != null) {
            TrackManager.loaderMixpanel(login.getFb_id(), postMixpanelModel);
        }
        //-----------------
    }

    public void setAppsFlyer(LoginModel login) {
        if (login != null) {
            PostAppsFlyerModel postAppsFlyerModel = new PostAppsFlyerModel();
            postAppsFlyerModel.setFb_id(login.getFb_id());
            //String appsflyer = "{\n  \\\"af_status\\\" : \\\""+Utils.AFinstall_type+"\\\",\\n  \\\"media_source\\\" : \\\""+Utils.AFmedia_source+"\\\",\\n  \\\"campaign\\\" : \\\""+Utils.AFcampaign+"\\\"\\n}";

            String media_source = Utils.AFmedia_source;
            String campaign = Utils.AFcampaign;
            String install_type = Utils.AFinstall_type;

            String click_time = Utils.AFclick_time;
            String install_time = Utils.AFinstall_time;
            String af_sub1 = Utils.AFsub1;

            if (media_source.equals(Utils.BLANK)) {
                media_source = Utils.AF_ORGANIC;
            }
            if (campaign.equals(Utils.BLANK)) {
                campaign = Utils.AF_ORGANIC;
            }
            if (install_type.equals(Utils.BLANK)) {
                install_type = Utils.AF_ORGANIC;
            }

            //String appsflyer = "{  \"af_status\" : \""+install_type+"\",  \"media_source\" : \""+media_source+"\",  \"campaign\" : \""+campaign+"\"}";
            String appsflyer = "{  \"af_status\" : \"" + install_type + "\",  \"media_source\" : \"" + media_source + "\",  \"campaign\" : \"" + campaign + "\", \"click_time\" : \"" + click_time + "\", \"install_time\" : \"" + install_time + "\", \"af_sub1\" : \"" + af_sub1 + "\"}";


            postAppsFlyerModel.setAppsflyer(appsflyer);

            String sd = String.valueOf(new Gson().toJson(postAppsFlyerModel));

            TrackManager.loaderAppsFlyer(postAppsFlyerModel);
        }
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        // System.exit(1); // kill off the crashed app

        //App.getInstance().
        android.os.Process.killProcess(android.os.Process.myPid());
    }



}