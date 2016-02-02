package com.jiggie.android;

import android.annotation.SuppressLint;
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
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jiggie.android.R;
import com.jiggie.android.component.SimpleJSONObject;
import com.jiggie.android.component.database.DatabaseConnection;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Login;
import com.android.volley.VolleyError;
import com.appsflyer.AppsFlyerLib;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;

import io.fabric.sdk.android.Fabric;

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
    final static String mPackageName = "com.jiggie.android";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //region Initialize third party libraries

        FacebookSdk.sdkInitialize(this);
        //Fabric.with(this, new Crashlytics());
        AppsFlyerLib.setAppsFlyerKey(super.getString(R.string.appsflyer_devkey));

        //endregion

        this.database = new DatabaseConnection(this);
    }

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
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException  e) {
                // ignore
            }
        }
    }

    public static App getInstance() { return instance; }

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

    public static SharedPreferences getSharedPreferences() { return PreferenceManager.getDefaultSharedPreferences(instance); }

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

    public DatabaseConnection getDatabase() { return this.database; }

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

    public void trackMixPanelEvent(String eventName) { this.trackMixPanelEvent(eventName, new SimpleJSONObject()); }
    public void trackMixPanelEvent(String eventName, SimpleJSONObject json) {
        final String location = Login.getCurrentLogin() == null ? null : Login.getCurrentLogin().getLocation();
        final String[] locations = TextUtils.isEmpty(location) ? new String[] { "", "" } : location.split(",");
        final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network = connManager.getActiveNetworkInfo();

        //Added by Aga
        json.putString("App Release", getVersionName(this));
        json.putString("App Version", getVersionCode(this));
        //------------

        json.putString("Carrier", this.getSimOperatorName());
        json.putString("City", locations[0].trim());
        json.putString("Country", locations[1].trim());
        json.putString("Device Model", Build.MODEL);
        json.putString("Manufacturer", Build.MANUFACTURER);
        json.putString("Distinct Id", this.getDeviceId());
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
        MixpanelAPI.getInstance(this, super.getString(R.string.mixpanel_token)).track(eventName, json);
    }

    //Added by Aga
    private String getVersionName(Context c){
        PackageInfo pi = null;
        try {
            pi = c.getPackageManager()
                    .getPackageInfo(mPackageName, PackageManager.GET_META_DATA);

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return pi.versionName;
    }

    private static String getVersionCode(Context c){
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
        if (this.deviceId == null) {
            final TelephonyManager telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
            final String deviceId = telephonyManager.getDeviceId();
            this.deviceId = ((TextUtils.isEmpty(deviceId)) || (deviceId.equals("000000000000000"))) ? Settings.Secure.getString(super.getContentResolver(), Settings.Secure.ANDROID_ID) : deviceId;
        }
        return this.deviceId;
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
                this.phoneType  = "GSM";
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

    public void setUserLoggedIn() { getSharedPreferences().edit().putBoolean("loggedIn", true).apply(); }

}
