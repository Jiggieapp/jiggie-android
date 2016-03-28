package com.jiggie.android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.setup.SetupNotificationActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.adapter.TutorialFragmentAdapter;
import com.jiggie.android.component.gcm.GCMRegistration;
import com.jiggie.android.component.service.FacebookImageSyncService;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.TagsListModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.HListView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    private static final String TAG = SignInFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, rootView);

        EventBus.getDefault().register(this);

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
                parameters.putString("fields", "id, email, gender, birthday, bio, first_name, last_name, location");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }
    };

    private GraphRequest.GraphJSONObjectCallback profileCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            String c = object.toString();
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

                //Added by Aga 2-2-2016
                LoginModel loginModel = new LoginModel();
                //loginModel.setVersion("1.1.0");
                String v = App.getVersionName(getActivity());
                loginModel.setVersion(v);
                loginModel.setUser_first_name(object.optString("first_name"));
                loginModel.setAbout(object.optString("bio"));
                loginModel.setApn_token(gcmId);
                loginModel.setProfil_image_url("");
                loginModel.setUserId("");
                loginModel.setLocation(location == null ? null : location.optString("name"));

                loginModel.setBirthday(Common.FACEBOOK_DATE_FORMAT.format(birthDay));
                loginModel.setFb_id(object.optString("id"));
                loginModel.setUser_last_name(object.optString("last_name"));
                loginModel.setEmail(object.optString("email"));
                loginModel.setGender(object.optString("gender"));

                //Added by Aga 11-2-2016
                loginModel.setDevice_type("2");
                //------------

                loginModel.setDevice_id(Utils.DEVICE_ID);

                //String sd = String.valueOf(new Gson().toJson(loginModel));

                AccountManager.loaderLogin(loginModel);

                String name = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();

                App.getInstance().setUserLoggedIn();
                App.getSharedPreferences().edit()
                        .putString(Common.PREF_FACEBOOK_NAME, name)
                        .putString(Common.PREF_FACEBOOK_ID, loginModel.getFb_id())
                        .apply();
                AccountManager.saveLogin(loginModel);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void onEvent(SettingModel message){
        String responses = new Gson().toJson(message);
        Utils.d("res", responses);

        final MainActivity activity = (MainActivity) getActivity();
        final App app = App.getInstance();
        progressDialog.dismiss();

        AccountManager.saveSetting(message);

        //refresh Credit card list----
        if(CommerceManager.arrCCScreen!=null&&CommerceManager.arrCCScreen.size()>0){
            CommerceManager.arrCCScreen.clear();
        }
        //--------------------

        //setupWalkthrough(message.is_new_user(), message.isShow_walkthrough());

        if (App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)
                && !message.is_new_user()) {
            app.trackMixPanelEvent("Log In");
            if (activity != null)
                activity.navigateToHome();
            else
                app.startActivity(new Intent (App.getInstance(), MainActivity.class)
                        //.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            // Start new activity from app context instead of current activity. This prevent crash when activity has been destroyed.
            app.trackMixPanelEvent("Sign Up");
            //wandy 24-03-2016
            //app.startActivity(new Intent(app, SetupTagsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            doOperator();
            //end of wandy 24-03-2016

            app.startService(new Intent
                    (app, FacebookImageSyncService.class));
            if (activity != null)
                activity.finish();
        }
    }

    private void doOperator() {
        Observable<TagsListModel> observableTagList
                = Observable.create(new Observable.OnSubscribe<TagsListModel>() {
            @Override
            public void call(final Subscriber<? super TagsListModel> subscriber) {
                EventManager.loaderTagsList(new OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        subscriber.onNext((TagsListModel) object);

                    }

                    @Override
                    public void onFailure(ExceptionModel exceptionModel) {
                        subscriber.onError(new Throwable(exceptionModel.getMessage()));
                    }
                });
            }
        })
        .doOnNext(new Action1<TagsListModel>() {
            @Override
            public void call(TagsListModel tagsListModel) {
                //Utils.d(TAG, "doOnNext");
                //EventManager.saveTagsList(tagsListModel);
                EventManager.saveTags(tagsListModel.getData().getTagslist());
            }
        })
        .doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                btnSignIn.setEnabled(true);
                progressDialog.dismiss();
            }
        });

        observableTagList
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<TagsListModel>() {
                @Override
                public void call(TagsListModel tagsListModel) {
                    actionDone();
                }
            });
    }

    private void actionDone() {
        final MemberSettingModel memberSettingModel = new MemberSettingModel();
        final SettingModel currentSettingModel = AccountManager.loadSetting();
        memberSettingModel.setAccount_type(currentSettingModel.getData().getAccount_type());
        memberSettingModel.setLocation(1);
        memberSettingModel.setGender(currentSettingModel.getData().getGender());
        memberSettingModel.setGender_interest(currentSettingModel.getData().getGender_interest());
        memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        memberSettingModel.setChat(1);
        memberSettingModel.setFeed(1);
        memberSettingModel.setExperiences(TextUtils.join(",", getTags()));

        AccountManager.loaderMemberSetting(memberSettingModel);
        currentSettingModel.getData().getNotifications().setLocation(true);
        currentSettingModel.getData().getNotifications().setChat(true);
        currentSettingModel.getData().getNotifications().setFeed(true);

        //ArrayList<String> arrExperiences = new ArrayList<String>(Arrays.asList(intent.getStringArrayExtra(SetupTagsActivity.PARAM_EXPERIENCES)));
        /*final String[] tags = this.getTags().toArray(new String[this.getTags().size()]);
        ArrayList<String> arrExperiences = new ArrayList<String>();
        for(String tag : tags)
        {
            arrExperiences.add(tag);
        }*/
        //App.getInstance().trackMixPanelEvent("Walkthrough Tags");
        //currentSettingModel.getData().setExperiences(arrExperiences);

        AccountManager.saveSetting(currentSettingModel);
        App.getSharedPreferences().edit().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
        App.getInstance().startActivity(new Intent(App.getInstance()
                , MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private Set<String> getTags() {
        Set<String> tags = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getStringSet(Utils.TAGS_LIST, null);
        return tags;
        //return null;
    }

    public void onEvent(ExceptionModel message){
        Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_LONG).show();
        btnSignIn.setEnabled(true);
        progressDialog.dismiss();
    }

    //must use unit test
    /*private void setupWalkthrough(boolean isNewUser, boolean isShowWalkthrough){
        if(isNewUser){
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, true).commit();
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, true).commit();
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, true).commit();
        }else{
            if(isShowWalkthrough){

                boolean wkEvent = App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false);
                boolean wkSocial = App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false);
                boolean wkChat = App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_CHAT, false);

                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, wkEvent).commit();
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, wkSocial).commit();
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, wkChat).commit();

            }
            else{
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false).commit();
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, false).commit();
            }
        }
    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
