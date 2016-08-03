package com.jiggie.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.PurchaseHistoryActivity;
import com.jiggie.android.activity.invite.InviteCodeActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.activity.profile.ProfileSettingActivity;
import com.jiggie.android.activity.promo.PromotionsActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.gcm.GCMRegistrationService;
import com.jiggie.android.component.service.FacebookImageSyncService;
import com.jiggie.android.fragment.HomeFragment;
import com.jiggie.android.fragment.SignInFragment;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.manager.CreditBalanceManager;
import com.jiggie.android.manager.ShareManager;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.manager.TooltipsManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestInfo;
import com.jiggie.android.model.ShareLinkModel;
import com.jiggie.android.model.SuccessCreditBalanceModel;
import com.jiggie.android.presenter.GuestPresenter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 21/10/2015.
 */
public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    private boolean active;
    public static final String TAG = MainActivity.class.getSimpleName();

    String appsfl = "";
    GoogleApiClient mGoogleApiClient = null;
    String rate = "0";
    boolean canEditRate = true;
    float firstRate = 0;

    private boolean isFirstRun() {
        final SharedPreferences pref = App.getSharedPreferences();
        if (Utils.getVersion(this) < 1021) //1021 is 22-02-2016 build
        {
            //clear all
            App.getSharedPreferences().edit().clear().apply();
            return false;
        } else {
            boolean isFirstRun = pref.getBoolean(Utils.IS_FIRST_RUN, true);
            return isFirstRun;
        }
    }

    private Bundle savedInstanceState;

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        this.active = true;

        this.savedInstanceState = savedInstanceState;

        AppsFlyerLib.sendTracking(MainActivity.this);

        if (isFirstRun()) {
            final SharedPreferences pref = App.getSharedPreferences();
            pref.edit().putBoolean(Utils.IS_FIRST_RUN, false).commit();
            App.getInstance().trackMixPanelEvent("Install");

            //TOOLTIP PART===============
            //TooltipsManager.clearTimeTooltip();
            TooltipsManager.validateTime(Calendar.getInstance().getTimeInMillis());
            //END OF TOOLTIP PART===============
        }

        //TOOLTIP PART===============
        /*TooltipsManager.clearTimeTooltip();
        TooltipsManager.validateTime(Calendar.getInstance().getTimeInMillis());*/

        //END OF TOOLTIP PART===============

        // validate GCM Version and update if necessary
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);

        if (code == ConnectionResult.SUCCESS) {
            //App.getInstance().trackMixPanelEvent("Log In");
            this.onActivityResult(REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
        } else if (api.isUserResolvableError(code) && api.showErrorDialogFragment(this, code, REQUEST_GOOGLE_PLAY_SERVICES)) {
            // wait for onActivityResult call (see below)
        } else {
            final String str = GoogleApiAvailability.getInstance().getErrorString(code);
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        /*Intent a = getIntent();
        boolean isRefresh = a.getBooleanExtra(Utils.TAG_ISREFRESH, false);
        if(isRefresh){
            EventBus.getDefault().post(EventsFragment.TAG);
        }*/

        /*if (App.getInstance().isUserLoggedIn()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //cekCounter();
                    if (AccountManager.getCounterEvent() == 5) {
                        if (InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())) {
                            startActivity(new Intent(MainActivity.this, InviteFriendsActivity.class));
                        }
                    }
                }
            }, 1000);
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /*@Override
    protected void onStart() {
        if(mGoogleApiClient!=null)
            mGoogleApiClient.connect();
        super.onStart();
    }*/

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void checkLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = null;
        /*try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }catch (SecurityException e){
            Utils.d(getString(R.string.tag_location),e.toString());
        }*/

        LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            try {
                mLastLocation = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {
                Utils.d(getString(R.string.tag_location), e.toString());
            }

            if (mLastLocation != null) {
                SocialManager.lat = String.valueOf(mLastLocation.getLatitude());
                SocialManager.lng = String.valueOf(mLastLocation.getLongitude());
            }
        }

        if (mLastLocation != null) {
            SocialManager.lat = String.valueOf(mLastLocation.getLatitude());
            SocialManager.lng = String.valueOf(mLastLocation.getLongitude());

            HomeFragment.sendLocationInfo(true);
        } else {
            Utils.d(getString(R.string.tag_location), getString(R.string.error_loc_failed));
        }
        //actionResults();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utils.d(getString(R.string.tag_location), getString(R.string.error_loc_failed));
        //actionResults();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utils.d(getString(R.string.tag_location), getString(R.string.error_loc_suspended));
        //actionResults();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //insertDummyContact();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static HomeFragment homeFragment;
    private static SignInFragment signInFragment;

    public HomeFragment getHomeFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            homeFragment.setArguments(super.getIntent().getExtras());
        }
        return homeFragment;
    }

    public SignInFragment getSignInFragment() {
        if (signInFragment == null) {
            signInFragment = new SignInFragment();
        }
        return signInFragment;
    }

    public void navigateToHome() {
        signInFragment = null;
        if (savedInstanceState == null) {
            homeFragment = null;
            homeFragment = getHomeFragment();
        } else {
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "homefragment");
        }

        /*mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        FragmentOne fragment = new FragmentOne();

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();*/

        final FragmentManager fragmentManager = super.getSupportFragmentManager();

        final int fragmentCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++)
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        final Fragment fragment = getHomeFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isLocationServicesAvailable(this)) {
            checkLocation();
        } else {
            boolean isAlreadyOpen = App.getSharedPreferences().getBoolean(Utils.PREFERENCE_GPS, false);
            if (!isAlreadyOpen) {
                App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, true).commit();
                showDialog();
            }
        }
    }

    private void showDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getString(R.string.msg_dialog_sett_location))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, true).commit();
                        dialog.dismiss();
                        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, true).commit();
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.show();
    }

    boolean showRate = true;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_GOOGLE_PLAY_SERVICES) && (resultCode == Activity.RESULT_OK)) {
            // Track AppsFlyer Install
            AppsFlyerLib.sendTracking(super.getApplicationContext());

            registerAppsFlyerConversion();
            //Toast.makeText(MainActivity.this, appsfl, Toast.LENGTH_LONG).show();

            //checkLocation and post it
            //checkLocation();

            if (!App.getInstance().isUserLoggedIn()) {
                homeFragment = null;
                SignInFragment fragment;
                if(savedInstanceState == null)
                {
                    signInFragment = null;
                    fragment = getSignInFragment();
                }
                else
                {
                    fragment = (SignInFragment) getSupportFragmentManager()
                            .getFragment(savedInstanceState, "signinfragment");
                }
                super.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            } else {
                //Check availability GPS Location
                if (Utils.isLocationServicesAvailable(this)) {
                    checkLocation();
                } else {
                    boolean isAlreadyOpen = App.getSharedPreferences().getBoolean(Utils.PREFERENCE_GPS, false);
                    if (!isAlreadyOpen) {
                        App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, true).commit();
                        showDialog();
                    }
                }
                //End here

                super.startService(new Intent(this, FacebookImageSyncService.class));
                if (!App.getSharedPreferences().getBoolean(GCMRegistrationService.TAG_UPDATED, false))
                    super.startService(new Intent(this, GCMRegistrationService.class));

                if (!App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)) {
                    //super.startActivity(new Intent(this, SetupTagsActivity.class));
                    final SignInFragment fragment = new SignInFragment();
                    super.getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
                    //super.finish();
                } else {
                    //wandy 20-04-2016
                    //sblm navigate to home, pastikan sudah ambil guest info sekali aja
                    if (!App.getInstance().getSharedPreferences().getBoolean(Utils.HAS_LOAD_GUEST_INFO, false)) {
                        App.getSharedPreferences().edit().putBoolean
                                (Utils.HAS_LOAD_GUEST_INFO, true).apply();
                        final GuestPresenter guestPresenter = new GuestPresenter();
                        guestPresenter.loadGuestInfo(new GuestPresenter.OnFinishGetGuestInfo() {
                            @Override
                            public void onFinish(GuestInfo guestInfo) {
                                guestPresenter.saveGuest(guestInfo);
                                navigateToHome();
                            }

                            @Override
                            public void onFailed() {
                                navigateToHome();
                            }
                        });
                    } else {
                        this.navigateToHome();
                        //showRateDialog();
                        showNewRateDialog();
                        /*if(showRate){
                            showRate = false;
                            showNewRateDialog();
                        }*/
                    }

                }

                //INVITE FRIENDS PART===========================
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //cekCounter();
                        if(AccountManager.getCounterEvent()==5){
                            if (InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())) {
                                startActivity(new Intent(MainActivity.this, InviteFriendsActivity.class));
                            }
                        }
                    }
                }, 1000);*/
                //END OF INVITE FRIENDS PART===========================
            }
        } else if (requestCode == REQUEST_GOOGLE_PLAY_SERVICES)
            super.onBackPressed();
    }

    private void actionResults() {
        if (!App.getInstance().isUserLoggedIn()) {
            final SignInFragment fragment = new SignInFragment();
            super.getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        } else {
            super.startService(new Intent(this, FacebookImageSyncService.class));
            if (!App.getSharedPreferences().getBoolean(GCMRegistrationService.TAG_UPDATED, false))
                super.startService(new Intent(this, GCMRegistrationService.class));

            if (!App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)) {
                //super.startActivity(new Intent(this, SetupTagsActivity.class));
                final SignInFragment fragment = new SignInFragment();
                super.getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
                //super.finish();
            } else {
                this.navigateToHome();
                //showRateDialog();
                showNewRateDialog();
                /*if(showRate){
                    showRate = false;
                    showNewRateDialog();
                }*/
            }
        }
    }

    private void registerAppsFlyerConversion() {
        AppsFlyerLib.registerConversionListener(super.getApplicationContext(), new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                String media_source = map.get("media_source") == null ? null : map.get("media_source");
                String campaign = map.get("campaign") == null ? null : map.get("campaign");
                String af_status = map.get("af_status") == null ? null : map.get("af_status");
                String click_time = map.get("click_time") == null ? null : map.get("click_time");
                String install_time = map.get("install_time") == null ? null : map.get("install_time");
                String af_sub1 = map.get("af_sub1") == null ? null : map.get("af_sub1");
                String af_sub2 = map.get("af_sub2") == null ? null : map.get("af_sub2");

                if (media_source != null)
                    Utils.AFmedia_source = media_source;
                if (campaign != null)
                    Utils.AFcampaign = campaign;
                if (af_status != null)
                    Utils.AFinstall_type = af_status;
                if (click_time != null)
                    Utils.AFclick_time = click_time;
                if (install_time != null)
                    Utils.AFinstall_time = install_time;
                if (af_sub1 != null)
                    Utils.AFsub1 = af_sub1;
                if (af_sub2 != null)
                    Utils.AFsub2 = af_sub2;
            }

            @Override
            public void onInstallConversionFailure(String s) {
                //Toast.makeText(MainActivity.this, "a", Toast.LENGTH_LONG).show();
                Utils.d("123appsflyer", "a");
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                //Toast.makeText(MainActivity.this, "b", Toast.LENGTH_LONG).show();
                Utils.d("123appsflyer", "b");
            }

            @Override
            public void onAttributionFailure(String s) {
                //Toast.makeText(MainActivity.this, "c", Toast.LENGTH_LONG).show();
                Utils.d("123appsflyer", "c");
            }
        });
    }

    private void showRateDialog() {
        if (this.isActive()) {
            final String snoozePref = "rate_next";
            final SharedPreferences pref = App.getSharedPreferences();
            final long lastTime = pref.getLong(snoozePref, 0);

            if (lastTime == 0) {
                // rate app dialog never showed, wait for 1 day app usage.
                final long nextTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
                pref.edit().putLong(snoozePref, nextTime).apply();
            } else if (lastTime <= System.currentTimeMillis()) {
                final long nextTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // 7 Days
                pref.edit().putLong(snoozePref, nextTime).apply();

                new AlertDialog.Builder(this)
                        .setTitle(R.string.rate_app)
                        .setMessage(R.string.rate_app_desc)
                        .setNegativeButton(R.string.never, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pref.edit().putLong(snoozePref, Long.MAX_VALUE).apply();
                            }
                        })
                        .setNeutralButton(R.string.later, null)
                        .setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + App.class.getPackage().getName())));
                                pref.edit().putLong(snoozePref, Long.MAX_VALUE).apply();
                            }
                        }).show();
            }
        }
    }

    protected boolean isActive() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) ?
                !super.isDestroyed() : this.active;
    }

    @Override
    protected void onDestroy() {
        this.active = false;

        if (App.mixpanelAPI != null) {
            App.mixpanelAPI.flush();
        }

        //refresh Credit card list----
        if (CommerceManager.arrCCScreen != null && CommerceManager.arrCCScreen.size() > 0) {
            CommerceManager.arrCCScreen.clear();
        }
        if (CommerceManager.arrCCLocal != null && CommerceManager.arrCCLocal.size() > 0) {
            CommerceManager.arrCCLocal.clear();
        }
        //--------------------

        App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, false).commit();
        homeFragment = null;
        System.gc();
        super.onDestroy();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        Class<? extends Activity> target = null;
        switch (item.getItemId()) {
            case R.id.action_settings:
                target = ProfileSettingActivity.class;
                break;
            /*case R.id.action_social_filter:
                target = SocialFilterActivity.class;
                break;*/
            case R.id.action_profile:
                //target = EditProfileActivity.class;
                target = ProfileDetailActivity.class;
                break;
            case R.id.action_support:
                mailSupport();
                break;
            /*case R.id.action_filter:
                target = FilterActivity.class;
                break;*/
            case R.id.action_invite:
                //inviteFriends();
                startActivity(new Intent(MainActivity.this, InviteCodeActivity.class));
                break;
            case R.id.action_promo:
                startActivity(new Intent(MainActivity.this, PromotionsActivity.class));
                break;
            case R.id.action_orderlist:
                startActivity(new Intent(this, PurchaseHistoryActivity.class));
                break;
            case R.id.action_logout:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.confirmation)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getSharedPreferences().edit().clear().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
                                App.getSharedPreferences().edit().clear().apply();
                                LoginManager.getInstance().logOut();

                                AccountManager.onLogout();

                                //getActivity().finish();

                                //added by Aga 22-1-2016
                                Intent i = new Intent(MainActivity.this, SplashActivity.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();

                                /*Intent intent = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);*/
                                //finish();

                                //----------------------

                            }
                        }).show();
                break;
            case R.id.action_creditbalance:
                progressDialog = App.showProgressDialog(MainActivity.this);
                CreditBalanceManager.loaderCreditBalance(AccessToken.getCurrentAccessToken().getUserId(), new CreditBalanceManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        SuccessCreditBalanceModel successCreditBalanceModel = (SuccessCreditBalanceModel) object;
                        showCreditBalance(successCreditBalanceModel.getData().getBalance_credit().getTot_credit_active());
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {
                        hideProgressDialog();
                    }
                });
                break;
        }
        if (target != null) {
            i = new Intent(this, target);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog ad = null;

    private void showCreditBalance(String creditBalance) {
        hideProgressDialog();
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.credit_balance)
                .setMessage("Your credit balance is " + StringUtility.getCreditBalanceFormat(creditBalance))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ad != null && ad.isShowing())
                            ad.dismiss();
                    }
                });
        ad = al.create();
        ad.show();
    }

    private void mailSupport() {
        final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", super.getString(R.string.support_email), null));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{super.getString(R.string.support_email)}); // hack for android 4.3
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.support));
        super.startActivity(Intent.createChooser(intent, super.getString(R.string.support)));
    }

    private void inviteFriends() {
        if (this.shareLink != null) {
            String link = String.format("%s\n\n%s", shareLink.getMessage(), shareLink.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, link).setType("text/plain"), getString(R.string.invite)));
            App.getInstance().trackMixPanelEvent("Share App");

            //Added 16-2-2016
            hideProgressDialog();
            //-------------
        } else {
            progressDialog = App.showProgressDialog(MainActivity.this);
            ShareManager.loaderShareApps(AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    private ShareLinkModel shareLink;
    ProgressDialog progressDialog = null;

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void onEvent(ShareLinkModel message) {
        App.getInstance().trackMixPanelEvent("Share App");
        if (MainActivity.this != null) {
            String link = String.format("%s\n\n%s", message.getMessage(), message.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, link).setType("text/plain"), getString(R.string.invite)));
            shareLink = message;
        }
        hideProgressDialog();
    }

    public void onEvent(ExceptionModel exceptionModel) {
        if (exceptionModel.getFrom().equalsIgnoreCase(Utils.FROM_SHARE_LINK)) {
            hideProgressDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.d(TAG, "onSaveInstanceState");
        if (homeFragment != null && App.getInstance().isUserLoggedIn()) {
            getSupportFragmentManager()
                    .putFragment(outState, "homefragment", getHomeFragment());
        } else if (signInFragment != null && !App.getInstance().isUserLoggedIn()) {
            getSupportFragmentManager()
                    .putFragment(outState, "signinfragment", getSignInFragment());
        }
    }

   /* @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Utils.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }*/

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Utils.d(TAG, "onRestoreInstanceState 2");
    }*/

    private void showNewRateDialog() {

        if (this.isActive()) {
            final String snoozePref = "rate_next";
            final SharedPreferences pref = App.getSharedPreferences();
            final long lastTime = pref.getLong(snoozePref, 0);

            if (lastTime == 0) {
                // rate app dialog never showed, wait for 1 day app usage.
                final long nextTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
                pref.edit().putLong(snoozePref, nextTime).apply();
            } else if (lastTime <= System.currentTimeMillis()) {
                final long nextTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // 7 Days
                pref.edit().putLong(snoozePref, nextTime).apply();

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_rate);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                Button btnLater = (Button) dialog.findViewById(R.id.btn_later);
                final Button btnSendFeedback = (Button) dialog.findViewById(R.id.btn_send);
                final RelativeLayout relFeedback = (RelativeLayout)dialog.findViewById(R.id.rel_feedback);
                final EditText edtFeedback = (EditText)dialog.findViewById(R.id.edt_feedback);
                AppCompatRatingBar ratingBar = (AppCompatRatingBar)dialog.findViewById(R.id.rate_bar);
                View relOutside = (View)dialog.findViewById(R.id.layout_dialog_rate);
                RelativeLayout relDialog = (RelativeLayout)dialog.findViewById(R.id.rel_dialog);

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                // Filled stars
                setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(MainActivity.this, R.color.purple));
                // Half filled stars
                setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(MainActivity.this, R.color.purple));

                try{
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion == Build.VERSION_CODES.M){
                        // Empty stars
                        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(MainActivity.this, R.color.text_grey_caption));
                    } else{
                        // Empty stars
                        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(MainActivity.this, R.color.purple));
                    }
                }catch (Exception e){
                    setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(MainActivity.this, R.color.text_grey_caption));
                }

                relFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtFeedback.requestFocus();
                        edtFeedback.requestFocusFromTouch();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                });

                btnSendFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        pref.edit().putLong(snoozePref, Long.MAX_VALUE).apply();

                        HashMap<String, Object> rateModel = new HashMap<>();
                        rateModel.put("fb_id", AccessToken.getCurrentAccessToken().getUserId());
                        rateModel.put("rate", rate);
                        rateModel.put("feed_back", edtFeedback.getText().toString());
                        rateModel.put("device_type", "2");
                        rateModel.put("version", getVersionName(MainActivity.this));
                        rateModel.put("model", Build.MODEL);

                        progressDialog = App.showProgressDialog(MainActivity.this);

                        AccountManager.loaderRate(rateModel, new AccountManager.OnResponseListener() {
                            @Override
                            public void onSuccess(Object object) {
                                hideProgressDialog();

                                showThanksDialog();
                            }

                            @Override
                            public void onFailure(int responseCode, String message) {
                                hideProgressDialog();
                            }
                        });

                    }
                });
                btnLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        rate = String.valueOf(rating);

                        if(canEditRate){
                            canEditRate = false;
                            firstRate = rating;
                            ratingBar.setRating(rating);

                            if(rating==0){
                                relFeedback.setVisibility(View.GONE);
                                btnSendFeedback.setVisibility(View.GONE);
                            }else if(rating>0&&rating<4){
                                relFeedback.setVisibility(View.VISIBLE);
                                btnSendFeedback.setVisibility(View.VISIBLE);
                            }else{
                                dialog.dismiss();
                                pref.edit().putLong(snoozePref, Long.MAX_VALUE).apply();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + App.class.getPackage().getName())));
                            }

                        }else{
                            ratingBar.setRating(firstRate);
                        }

                        ratingBar.setStepSize(0);
                    }
                });
                relOutside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                relDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do nothing
                    }
                });

                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            DrawableCompat.setTint(drawable, color);
        else
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private String getVersionName(Context c) {
        PackageInfo pi = null;
        try {
            pi = c.getPackageManager()
                    .getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return pi.versionName;
    }

    private void showThanksDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate_thx);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        View relOutside = (View)dialog.findViewById(R.id.layout_dialog_rate);
        RelativeLayout relDialog = (RelativeLayout)dialog.findViewById(R.id.rel_dialog);

        relOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        relDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Timer timerDialog = new Timer();
        TimerTask tskDialog = new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };
        timerDialog.schedule(tskDialog, 3000);
    }
}
