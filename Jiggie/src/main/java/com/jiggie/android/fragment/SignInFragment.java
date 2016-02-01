package com.jiggie.android.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.adapter.TutorialFragmentAdapter;
import com.jiggie.android.component.gcm.GCMRegistration;
import com.jiggie.android.component.service.FacebookImageSyncService;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Login;
import com.jiggie.android.model.Setting;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by rangg on 21/10/2015.
 */
public class SignInFragment extends Fragment {
    private static final String[] FACEBOOK_PERMISSIONS = new String[]{
            "public_profile", "email", "user_about_me", "user_birthday", "user_photos", "user_location"
    };

    @Bind(R.id.imagePagerIndicator)
    HListView imagepagerIndicator;
    @Bind(R.id.imageViewPager)
    ViewPager imageViewPager;
    @Bind(R.id.placeHolder)
    ImageView placeHolder;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.btnSignIn)
    Button btnSignIn;

    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private Handler fadeHandler;
    private int previousPage;
    private View rootView;
    private String gcmId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, rootView);
        LoginManager.getInstance().registerCallback(this.callbackManager = CallbackManager.Factory.create(), this.facebookCallback);

        final ImagePagerIndicatorAdapter imagePagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getActivity().getSupportFragmentManager(), this.imageViewPager);
        this.imagepagerIndicator.setAdapter(imagePagerIndicatorAdapter.getIndicatorAdapter());

        final TutorialFragmentAdapter tutorialAdapter = new TutorialFragmentAdapter(super.getFragmentManager(), this.imageViewPager);
        this.fadeHandler = new Handler();
        this.imagepagerIndicator.setAdapter(tutorialAdapter.getIndicatorAdapter());
        this.imageViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (getContext() != null) {
                    if (position == 0) {
                        fadeHandler.removeCallbacks(fadeInRunnable);
                        fadeHandler.removeCallbacks(fadeOutRunnable);
                        fadeHandler.postDelayed(fadeOutRunnable, 200);
                    } else if ((position == 1) && (previousPage == 0)) {
                        fadeHandler.removeCallbacks(fadeInRunnable);
                        fadeHandler.removeCallbacks(fadeOutRunnable);
                        fadeHandler.postDelayed(fadeInRunnable, 200);
                    }
                }
                previousPage = position;
            }
        });
    }

    private Runnable fadeInRunnable = new Runnable() {
        @Override
        public void run() {
            if (getContext() != null) {
                placeHolder.setImageResource(R.mipmap.signup1);
                Glide.with(getContext()).load(R.mipmap.signup2).skipMemoryCache(true).crossFade(1000).centerCrop().into(imageView);
            }
        }
    };

    private Runnable fadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (getContext() != null) {
                placeHolder.setImageResource(R.mipmap.signup2);
                Glide.with(getContext()).load(R.mipmap.signup1).skipMemoryCache(true).crossFade(1000).into(imageView);
            }
        }
    };

    @OnClick(R.id.btnSignIn)
    @SuppressWarnings("unused")
    void btnSignInOnClick() {
        this.btnSignIn.setEnabled(false);
        this.progressDialog = App.showProgressDialog(getContext());

        new GCMRegistration(super.getContext(), new GCMRegistration.Listener() {
            @Override
            public boolean onGCMPreExecute() {
                return true;
            }

            @Override
            public void onGCMError(Exception e) {
                Toast.makeText(getContext(), App.getErrorMessage(e), Toast.LENGTH_LONG).show();
                btnSignIn.setEnabled(true);
                progressDialog.dismiss();
            }

            @Override
            public void onGCMCompleted(String regId) {
                gcmId = regId;
                LoginManager.getInstance().logInWithReadPermissions(SignInFragment.this, Arrays.asList(FACEBOOK_PERMISSIONS));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onCancel() {
            if (getContext() != null) {
                btnSignIn.setEnabled(true);
                progressDialog.dismiss();
            }
        }

        @Override
        public void onError(FacebookException error) {
            if (getContext() != null) {
                Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_LONG).show();
                btnSignIn.setEnabled(true);
                progressDialog.dismiss();
            }
        }

        @Override
        public void onSuccess(LoginResult loginResult) {
            if (getContext() != null) {
                // Validate permissions
                for (String perm : loginResult.getRecentlyDeniedPermissions()) {
                    if (StringUtility.contains(FACEBOOK_PERMISSIONS, perm, true)) {
                        Toast.makeText(getContext(), getString(R.string.facebook_permission_required), Toast.LENGTH_LONG).show();
                        btnSignIn.setEnabled(true);
                        progressDialog.dismiss();
                        return;
                    }
                }

                final AccessToken token = AccessToken.getCurrentAccessToken();
                final Bundle parameters = new Bundle();

                final GraphRequest request = GraphRequest.newMeRequest(token, profileCallback);
                parameters.putString("fields", "id, email, gender, birthday, about, first_name, last_name, location");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }
    };

    private GraphRequest.GraphJSONObjectCallback profileCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            if (getContext() == null) {
                // fragment already destroyed
                return;
            } else if (response.getError() != null) {
                Toast.makeText(getContext(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                btnSignIn.setEnabled(true);
                progressDialog.dismiss();
                return;
            }

            try {
                final Date birthDay = Common.FACEBOOK_DATE_FORMAT.parse(object.optString("birthday"));
                final JSONObject location = object.optJSONObject("location");
                final Login login = new Login();

                login.setAbout(object.optString("about"));
                login.setBirthday(Common.SHORT_DATE_FORMAT.format(birthDay));
                login.setEmail(object.optString("email"));
                login.setFacebookId(object.optString("id"));
                login.setFirstName(object.optString("first_name"));
                login.setLastName(object.optString("last_name"));
                login.setGender(object.optString("gender"));
                login.setLocation(location == null ? null : location.optString("name"));
                login.setApnToken(gcmId);
                login.save(getContext());

                VolleyHandler.getInstance().createVolleyRequest("login", login, loginListener);

                App.getInstance().setUserLoggedIn();
                App.getSharedPreferences().edit()
                        .putString(Common.PREF_FACEBOOK_NAME, login.getName())
                        .putString(Common.PREF_FACEBOOK_ID, login.getFacebookId())
                        .apply();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private VolleyRequestListener<Void, JSONObject> loginListener = new VolleyRequestListener<Void, JSONObject>() {
        @Override
        public Void onResponseAsync(JSONObject response) {
            new Setting(VolleyHandler.getData(response)).save();
            return null;
        }

        @Override
        public void onResponseCompleted(Void value) {
            final MainActivity activity = (MainActivity) getActivity();
            final App app = App.getInstance();
            progressDialog.dismiss();

            /*boolean isNewUser = App.getSharedPreferences().getBoolean(Setting.FIELD_NEW_USER, false);
            if(isNewUser){*/
            //set first login to show walkthrough
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, true).commit();
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, true).commit();
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, true).commit();
            //-----------------------------------
            //}

            if (App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)) {
                app.trackMixPanelEvent("Login");
                if (activity != null)
                    activity.navigateToHome();
                else
                    app.startActivity(new Intent(App.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                // Start new activity from app context instead of current activity. This prevent crash when activity has been destroyed.
                app.trackMixPanelEvent("SignUp");
                app.startActivity(new Intent(app, SetupTagsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                app.startService(new Intent(app, FacebookImageSyncService.class));
                if (activity != null)
                    activity.finish();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_LONG).show();
            btnSignIn.setEnabled(true);
            progressDialog.dismiss();
        }
    };
}
