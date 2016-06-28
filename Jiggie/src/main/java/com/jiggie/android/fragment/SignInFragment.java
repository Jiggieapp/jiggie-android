package com.jiggie.android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.TutorialFragmentAdapter;
import com.jiggie.android.component.gcm.GCMRegistration;
import com.jiggie.android.component.service.FacebookImageSyncService;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.TagsListModel;
import com.jiggie.android.view.CircleIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
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
            "public_profile", "email", "user_about_me", "user_birthday", "user_photos", "user_location",
            "user_friends"
    };

    /*@Bind(R.id.imagePagerIndicator)
    HListView imagepagerIndicator;*/
    @Bind(R.id.placeHolder)
    ImageView placeHolder;
    @Bind(R.id.imageView)
    ImageView imageView;
    /*@Bind(R.id.btnSignIn)
    Button btnSignIn;*/
    @Bind(R.id.imagePagerIndicator)
    LinearLayout imagePagerIndicator;
    @Bind(R.id.imageViewPager)
    ViewPager imageViewPager;
    @Bind(R.id.txt_skip)
    TextView txtSkip;
    /*@Bind(R.id.txt_we_dont)
    TextView txtWeDont;*/

    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private Handler fadeHandler;
    private int previousPage;
    private View rootView;
    private String gcmId;
    private static final String TAG = SignInFragment.class.getSimpleName();

    Button btnSignIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, rootView);

        EventBus.getDefault().register(this);

        LoginManager.getInstance().registerCallback(this.callbackManager = CallbackManager.Factory.create(), this.facebookCallback);

        /*final ImagePagerIndicatorAdapter imagePagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getActivity().getSupportFragmentManager(), this.imageViewPager);
        this.imagepagerIndicator.setAdapter(imagePagerIndicatorAdapter.getIndicatorAdapter());*/

        final TutorialFragmentAdapter tutorialAdapter = new TutorialFragmentAdapter(super.getFragmentManager(), this.imageViewPager);
        this.fadeHandler = new Handler();

        //CUSTOM CIRCLE INDICATOR PART===============
        for (int i = 0; i < tutorialAdapter.getIndicatorLength(); i++) {
            CircleIndicatorView circleIndicatorView = new CircleIndicatorView(getActivity(), false);
            imagePagerIndicator.addView(circleIndicatorView);
            ImageView imgIndicator = (ImageView) circleIndicatorView.getImgIndicator();
            if (i == 0) {
                imgIndicator.setSelected(true);
            }
        }
        //END HERE

        //this.imagepagerIndicator.setAdapter(tutorialAdapter.getIndicatorAdapter());
        this.imageViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            /*@Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if ((position == 4) && (previousPage == 3)){
                    imagePagerIndicator.setVisibility(View.GONE);
                    txtSkip.setVisibility(View.GONE);
                }else if((position == 5) && (previousPage == 4)){
                    imagePagerIndicator.setVisibility(View.VISIBLE);
                    txtSkip.setVisibility(View.VISIBLE);
                }
            }*/

            /*@Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }*/

            @Override
            public void onPageSelected(int position) {
                if (getContext() != null) {
                    /*if ((position == 4) && (previousPage == 5)) {
                        fadeHandler.removeCallbacks(fadeInRunnable);
                        fadeHandler.removeCallbacks(fadeOutRunnable);
                        fadeHandler.postDelayed(fadeOutRunnable, 200);

                    } else if ((position == 5) && (previousPage == 4)) {
                        fadeHandler.removeCallbacks(fadeInRunnable);
                        fadeHandler.removeCallbacks(fadeOutRunnable);
                        fadeHandler.postDelayed(fadeInRunnable, 200);
                    }*/

                    /*placeHolder.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            placeHolder.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();

                    imageView.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imageView.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();*/

                    TutorialFragmentAdapter.TutorialFragment fragment =
                            (TutorialFragmentAdapter.TutorialFragment) tutorialAdapter.getItem(position);

                    if (position == 5) {
                        fragment.getContentView().setVisibility(View.GONE);
                        fragment.getImageViews().setVisibility(View.GONE);
                        fragment.getImageHelps().setVisibility(View.VISIBLE);

                        imagePagerIndicator.setVisibility(View.GONE);
                        txtSkip.setVisibility(View.GONE);
                    } else {
                        fragment.getImageHelps().setVisibility(View.GONE);

                        imagePagerIndicator.setVisibility(View.VISIBLE);
                        txtSkip.setVisibility(View.VISIBLE);

                        if (position == 4) {
                            txtSkip.setText("NEXT");
                        } else {
                            txtSkip.setText("SKIP");
                        }
                    }

                    btnSignIn = fragment.getBtnSignIn();
                    btnSignIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickSignIn();
                        }
                    });

                    fragment.getImageHelps().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewPager.setCurrentItem(0);
                        }
                    });

                }

                for (int i = 0; i < tutorialAdapter.getIndicatorLength(); i++) {
                    CircleIndicatorView circleIndicatorView = (CircleIndicatorView) imagePagerIndicator.getChildAt(i);
                    ImageView imgIndicator = (ImageView) circleIndicatorView.getImgIndicator();
                    if (i == position) {
                        imgIndicator.setSelected(true);
                    } else {
                        imgIndicator.setSelected(false);
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

                //placeHolder.setImageResource(R.mipmap.signup1);
                /*placeHolder.setBackgroundColor(Color.BLACK);
                Glide.with(getContext()).load(R.mipmap.signup1).skipMemoryCache(true).crossFade(1000).into(imageView);*/
            }
        }
    };

    private Runnable fadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (getContext() != null) {
                placeHolder.setImageResource(R.mipmap.signup2);
                Glide.with(getContext()).load(R.mipmap.signup1).skipMemoryCache(true).crossFade(1000).into(imageView);

                /*placeHolder.setBackgroundColor(Color.BLACK);
                Glide.with(getContext()).load(R.mipmap.signup1).skipMemoryCache(true).crossFade(1000).into(imageView);*/
            }
        }
    };

    /*@OnClick(R.id.btnSignIn)
    @SuppressWarnings("unused")
    void btnSignInOnClick() {

    }*/

    private void onClickSignIn() {
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
                LoginManager.getInstance().logInWithReadPermissions(SignInFragment.this
                        , Arrays.asList(FACEBOOK_PERMISSIONS));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.txt_skip)
    @SuppressWarnings("unused")
    void skipOnClick() {
        imageViewPager.setCurrentItem(5);
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
                parameters.putString("fields", "id, email, gender, birthday, bio, first_name" +
                        ", last_name, location, friends");
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
                final LoginModel loginModel = new LoginModel();
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
                loginModel.setAge(StringUtility.getAge2(loginModel.getBirthday()));
                //Added by Aga 11-2-2016
                loginModel.setDevice_type("2");
                //------------

                loginModel.setDevice_id(Utils.DEVICE_ID);

                /*try {
                    AccountManager.getFriendList(new JSONObject(object.optString("friends")), new com.jiggie.android.listener.OnResponseListener() {
                        @Override
                        public void onSuccess(Object object) {
                            String name = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();

                            AccountManager.loaderLogin(loginModel);
                            App.getInstance().setUserLoggedIn();
                            App.getSharedPreferences().edit()
                                    .putString(Common.PREF_FACEBOOK_NAME, name)
                                    .putString(Common.PREF_FACEBOOK_ID, loginModel.getFb_id())
                                    .apply();
                            AccountManager.saveLogin(loginModel);
                        }

                        @Override
                        public void onFailure(int responseCode, String message) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                String name = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();
                AccountManager.loaderLogin(loginModel);
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

    public void onEvent(SettingModel message) {
        final MainActivity activity = (MainActivity) getActivity();
        final App app = App.getInstance();
        progressDialog.dismiss();

        AccountManager.saveSetting(message);

        //refresh Credit card list----
        if (CommerceManager.arrCCScreen != null && CommerceManager.arrCCScreen.size() > 0) {
            CommerceManager.arrCCScreen.clear();
        }
        if (CommerceManager.arrCCLocal != null && CommerceManager.arrCCLocal.size() > 0) {
            CommerceManager.arrCCLocal.clear();
        }
        //--------------------

        //setupWalkthrough(message.is_new_user(), message.isShow_walkthrough());

        if (App.getSharedPreferences().getBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, false)
                && !message.is_new_user()) {
            app.trackMixPanelEvent("Log In");
            if (activity != null)
                activity.navigateToHome();
            else
                app.startActivity(new Intent(App.getInstance(), MainActivity.class)
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

        FirebaseChatManager.fb_id = AccessToken.getCurrentAccessToken().getUserId();
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
        //wandy 08-06-2016
        memberSettingModel.setArea_event("jakarta");
        AccountManager.loaderMemberSetting(memberSettingModel);

        currentSettingModel.getData().getNotifications().setLocation(true);
        currentSettingModel.getData().getNotifications().setChat(true);
        currentSettingModel.getData().getNotifications().setFeed(true);
        //wandy 09-06-2016
        currentSettingModel.getData().setAreaEvent("jakarta");
        //end of wandy 09-06-2016

        AccountManager.saveSetting(currentSettingModel);
        App.getSharedPreferences().edit().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
        /*getActivity().finish();
        App.getInstance().startActivity(new Intent(App.getInstance()
                , MainActivity.class)
                *//*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK));*//*
                *//*.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*//*
                *//*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*//*);*/
        Intent i = new Intent(App.getInstance(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(i);

    }

    private Set<String> getTags() {
        Set<String> tags = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getStringSet(Utils.TAGS_LIST, null);
        return tags;
        //return null;
    }

    public void onEvent(ExceptionModel message) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
