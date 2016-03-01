package com.jiggie.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.FilterActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.activity.profile.ProfileSettingActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.gcm.GCMRegistrationService;
import com.jiggie.android.component.service.FacebookImageSyncService;
import com.jiggie.android.fragment.HomeFragment;
import com.jiggie.android.fragment.SignInFragment;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jiggie.android.manager.ShareManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.ShareLinkModel;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 21/10/2015.
 */
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    private boolean active;
    public static final String TAG = MainActivity.class.getSimpleName();

    private boolean isFirstRun()
    {
        final SharedPreferences pref = App.getSharedPreferences();
        if(Utils.getVersion(this) < 1021) //1021 is 22-02-2016 build
        {
            //clear all
            App.getSharedPreferences().edit().clear().apply();
            return false;
        }
        else
        {
            boolean isFirstRun = pref.getBoolean(Utils.IS_FIRST_RUN, true);
            return isFirstRun;
        }
    }



    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        this.active = true;

        if(isFirstRun()){
            final SharedPreferences pref = App.getSharedPreferences();
            pref.edit().putBoolean(Utils.IS_FIRST_RUN, false).commit();
            App.getInstance().trackMixPanelEvent("Install");
        }

        // validate GCM Version and update if necessary
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);

        if (code == ConnectionResult.SUCCESS) {
            App.getInstance().trackMixPanelEvent("Log In");
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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

    public void navigateToHome() {
        final FragmentManager fragmentManager = super.getSupportFragmentManager();
        final int fragmentCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++)
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        final Fragment fragment = new HomeFragment();
        fragment.setArguments(super.getIntent().getExtras());
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_GOOGLE_PLAY_SERVICES) && (resultCode == Activity.RESULT_OK)) {
            // Track AppsFlyer Install
            AppsFlyerLib.sendTracking(super.getApplicationContext());

            registerAppsFlyerConversion();

            if (!App.getInstance().isUserLoggedIn()) {
                final SignInFragment fragment = new SignInFragment();
                super.getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
            } else {
                super.startService(new Intent(this, FacebookImageSyncService.class));
                if (!App.getSharedPreferences().getBoolean(GCMRegistrationService.TAG_UPDATED, false))
                    super.startService(new Intent(this, GCMRegistrationService.class));

                if (!App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)) {
                    super.startActivity(new Intent(this, SetupTagsActivity.class));
                    super.finish();
                } else {
                    this.navigateToHome();
                    showRateDialog();
                }
            }
        } else if (requestCode == REQUEST_GOOGLE_PLAY_SERVICES)
            super.onBackPressed();
    }

    private void registerAppsFlyerConversion(){
        AppsFlyerLib.registerConversionListener(super.getApplicationContext(), new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                String media_source = map.get("media_source") == null ? null : map.get("media_source");
                String campaign = map.get("campaign") == null ? null : map.get("campaign");
                String af_status = map.get("af_status") == null ? null : map.get("af_status");
                if (media_source != null)
                    Utils.AFmedia_source = media_source;
                if (campaign != null)
                    Utils.AFcampaign = campaign;
                if (af_status != null)
                    Utils.AFinstall_type = af_status;

                /*String d = map.toString();
                String a = Utils.AFmedia_source+" "+Utils.AFcampaign+" "+Utils.AFinstall_type;
                Toast.makeText(MainActivity.this, a, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onInstallConversionFailure(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

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
            }else if (lastTime <= System.currentTimeMillis()) {
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

    protected boolean isActive()
    {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) ?
            !super.isDestroyed() : this.active;
    }

    @Override
    protected void onDestroy() {
        this.active = false;
        if(App.mixpanelAPI!=null){
            App.mixpanelAPI.flush();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        Class<? extends Activity> target = null;
        switch (item.getItemId())
        {
            case R.id.action_settings:
                target = ProfileSettingActivity.class;
                break;
            case R.id.action_profile:
                target = ProfileDetailActivity.class;
                break;
            case R.id.action_support:
                mailSupport();
                break;
            case R.id.action_filter:
                target = FilterActivity.class;
                break;
            case R.id.action_invite:
                inviteFriends();
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
                                LoginManager.getInstance().logOut();
                                //getActivity().finish();

                                //added by Aga 22-1-2016
                                Intent i = new Intent(MainActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                //----------------------

                            }
                        }).show();
                break;
        }
        if(target != null)
        {
            i = new Intent(this, target);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void mailSupport() {
        final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", super.getString(R.string.support_email), null));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {super.getString(R.string.support_email)}); // hack for android 4.3
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

    private void hideProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public void onEvent(ShareLinkModel message){
        App.getInstance().trackMixPanelEvent("Share App");
        if (MainActivity.this != null) {
            String link = String.format("%s\n\n%s", message.getMessage(), message.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, link).setType("text/plain"), getString(R.string.invite)));
            shareLink = message;
        }
        hideProgressDialog();
    }

    public void onEvent(ExceptionModel exceptionModel)
    {
        if(exceptionModel.getFrom().equalsIgnoreCase(Utils.FROM_SHARE_LINK))
        {
            hideProgressDialog();
        }
    }

}
