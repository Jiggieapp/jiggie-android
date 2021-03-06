package com.jiggie.android.activity.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.adapter.ProfileLikedEventsAdapter;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.view.HeaderProfileView;
import com.jiggie.android.view.HeaderView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 14/11/2015.
 */
public class ProfileDetailActivity extends ToolbarActivity
        implements ViewTreeObserver.OnGlobalLayoutListener
        , SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener, ProfileDetailView
        , ProfileLikedEventsAdapter.OnEventContainerClick

{
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.imageViewPager)
    ViewPager imageViewPager;
    @Bind(R.id.titles)
    CirclePageIndicator titlePageIndicator;
    /*@Bind(R.id.txtDescription)
    TextView txtDescription;
    @Bind(R.id.txtLocation)
    TextView txtLocation;
    @Bind(R.id.btnEdit)
    ImageButton btnEdit;
    @Bind(R.id.txtUser)
    TextView txtUser;
    @Bind(R.id.txtTitleDescription)
    TextView txtTitleDescription;
    @Bind(R.id.lblPhoneNumber)
    TextView lblPhoneNumber;

    @Bind(R.id.img_has_table)
    ImageView imgHasTable;
    @Bind(R.id.img_has_ticket)
    ImageView imgHasTicket;*/

    @Bind(R.id.appBar)
    AppBarLayout appBar;

    @Bind(R.id.recycler_profile_liked_events)
    RecyclerView recyclerLikedEvents;

    @Bind(R.id.toolbar_header_view)
    protected HeaderProfileView toolbarHeaderView;

    @Bind(R.id.float_header_view)
    protected HeaderProfileView floatHeaderView;

    @Bind(R.id.img_edit_top)
    ImageView imgEditTop;

    @Bind(R.id.img_edit)
    ImageView imgEdit;

    private ImagePagerIndicatorAdapter pagerIndicatorAdapter;
    private MemberInfoModel memberInfoModel;
    private GuestModel.Data.GuestInterests guest;
    String fb_id;
    public static final String TAG = ProfileDetailActivity.class
            .getSimpleName();
    private boolean isMe = false;
    ProfileDetailPresenter profilePresenter;
    ProfileLikedEventsAdapter adapter;
    private boolean isHideToolbarView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_detail);
        super.bindView();

        profilePresenter = new ProfileDetailPresenterImplementation(this);
        //EventBus.getDefault().register(this);

        this.pagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        //this.imagePagerIndicator.setAdapter(this.pagerIndicatorAdapter.getIndicatorAdapter());
        titlePageIndicator.setViewPager(this.imageViewPager);
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.refreshLayout.setOnRefreshListener(this);
        this.appBar.addOnOffsetChangedListener(this);

        super.setToolbarTitle("", true);
    }

    @Override
    public void onGlobalLayout() {
        this.refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        fb_id = super.getIntent().getStringExtra(Common.FIELD_FACEBOOK_ID);
        //fb_id = "1206589092693600";
        if (fb_id == null) {
            fb_id = AccessToken.getCurrentAccessToken().getUserId();
            //fb_id = "10153452897043547"; //richard
            //fb_id = "10153418311072858"; //wandy
            //fb_id = "10205703989179267"; /fazlur
            isMe = true;
        } else {
            if (fb_id.equalsIgnoreCase(AccessToken.getCurrentAccessToken().getUserId()))
                isMe = true;
        }
        //fb_id = "10205703989179267";

        if (isMe) {
            imgEdit.setVisibility(View.VISIBLE);
            imgEditTop.setVisibility(View.VISIBLE);
        } else {
            imgEdit.setVisibility(View.GONE);
            imgEditTop.setVisibility(View.GONE);
        }


        if (!isMe) {
            App.getInstance().trackMixPanelEvent("View Member Profile");
        }

        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        //this.btnEdit.setVisibility(View.GONE);
        this.refreshLayout.setRefreshing(true);

        //AccountManager.loaderMemberInfo(fb_id);
        fetchDetail();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

        if (verticalOffset == 0) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    /*public void onEvent(MemberInfoModel message) {
        super.setToolbarTitle(message.getData().getMemberinfo().getFirst_name(), true);
        memberInfoModel = message;

        final String age = StringUtility.getAge2(message.getData().getMemberinfo().getBirthday());

        //Added by Aga 22-2-2016--------
        String[] photos;

        if (isMe) {
            final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
            final HashSet<String> profileImages = (HashSet<String>) App.getSharedPreferences().getStringSet(Common.PREF_IMAGES, new HashSet<String>());
            photos = profileImages.toArray(new String[profileImages.size()]);
            final int size = photos.length;
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    photos[i] = String.format("file:///%s%s", dataPath, photos[i]);
                }
            } else {
                final ArrayList<String> userPhotos = message.getData().getMemberinfo().getPhotos();
                photos = new String[userPhotos.size()];
                for (int i = 0; i < userPhotos.size(); i++) {
                    photos[i] = userPhotos.get(i);
                }
            }

            final LoginModel loginModel = AccountManager.loadLogin();

            if (TextUtils.isEmpty(loginModel.getLocation())) {
                txtLocation.setVisibility(View.GONE);
            } else if (loginModel.getLocation().equalsIgnoreCase("n/a")) {
                txtLocation.setVisibility(View.GONE);
            } else {
                txtLocation.setText(loginModel.getLocation());
            }

            if (TextUtils.isEmpty(message.getData().getMemberinfo().getAbout())) {
                if (TextUtils.isEmpty(loginModel.getAbout()))
                    txtDescription.setVisibility(View.GONE);
                else {
                    txtDescription.setText(loginModel.getAbout());
                }
            } else {
                txtDescription.setText(message.getData().getMemberinfo().getAbout());
            }
        } else {
            photos = message.getData().getMemberinfo().getPhotos().toArray(new String[message.getData().getMemberinfo().getPhotos().size()]);
            txtLocation.setText(message.getData().getMemberinfo().getLocation());
            //AccountManager.loadLogin().getAbout()
            txtDescription.setText(message.getData().getMemberinfo().getAbout());

            if (TextUtils.isEmpty(message.getData().getMemberinfo().getLocation()))
                txtLocation.setVisibility(View.GONE);
            else if (message.getData().getMemberinfo().getLocation().equalsIgnoreCase("n/a"))
                txtLocation.setVisibility(View.GONE);
            if (TextUtils.isEmpty(message.getData().getMemberinfo().getAbout()))
                txtDescription.setVisibility(View.GONE);
        }
        //-----------------------

        this.pagerIndicatorAdapter.setImages(photos);
        String name = message.getData().getMemberinfo().getFirst_name() + " " + message.getData().getMemberinfo().getLast_name();
        txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));

        if (isMe) //saya
        {
            btnEdit.setVisibility(View.VISIBLE);
            //wandy 0-03-2016
            //lblPhoneNumber.setVisibility(View.VISIBLE);
            lblPhoneNumber.setVisibility(View.GONE);
            SettingModel settingModel = AccountManager.loadSetting();
            final String phoneNo = settingModel.getData().getPhone();
            if (phoneNo == null || phoneNo.isEmpty()) {
                lblPhoneNumber.setText(getResources().getString(R.string.verify_your_phone_number));
            } else {
                lblPhoneNumber.setText(phoneNo);
            }
        } else //guest
        {
            btnEdit.setVisibility(View.GONE);
        }
        setToolbarTitle(name, true);
        refreshLayout.setRefreshing(false);
    }*/

    public void onEvent(ExceptionModel message) {
        if (message.getFrom().equals(Utils.FROM_PROFILE_DETAIL)) {
            Toast.makeText(ProfileDetailActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    @SuppressWarnings("unused")
    @Nullable
    @OnClick(R.id.btnEdit)
    void btnEditOnClick() {
        super.startActivityForResult(new Intent(this, ProfileEditActivity.class)
                .putExtra(Common.FIELD_ABOUT, AccountManager.loadLogin().getAbout()), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (TextUtils.isEmpty(AccountManager.loadLogin().getAbout())) {
                //txtDescription.setVisibility(View.GONE);
            } else {
                //txtDescription.setVisibility(View.VISIBLE);
                //this.txtDescription.setText(AccountManager.loadLogin().getAbout());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*@OnClick(R.id.lblPhoneNumber)
    @SuppressWarnings("unused")
    public void onVerifyPhoneNumberClick() {
        Intent i = new Intent(this, VerifyPhoneNumberActivity.class);
        startActivity(i);
    }*/

    @Override
    public void loadImages(ArrayList<String> photos) {
        /*photos = memberInfo.getPhotos().toArray(new String[memberInfo.getPhotos().size()]);
        txtLocation.setText(memberInfo.getLocation());*/
        this.pagerIndicatorAdapter.setImages(photos.toArray(new String[photos.size()]));
    }

    @Override
    public void fetchDetail() {
        profilePresenter.fetchMemberInfo(fb_id);
    }

    @Override
    public void onSuccess(MemberInfoModel memberInfoModel) {
        MemberInfoModel.Data.MemberInfo memberInfo = memberInfoModel.getData().getMemberinfo();
        //setToolbarTitle(memberInfo.getFirst_name(), true);
        refreshLayout.setRefreshing(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ProfileLikedEventsAdapter(this, memberInfo, isMe, this);
        recyclerLikedEvents.setLayoutManager(linearLayoutManager);
        recyclerLikedEvents.setAdapter(adapter);

        toolbarHeaderView.bindTo(memberInfo.getFirst_name() + " "
                + memberInfo.getLast_name());
        floatHeaderView.bindTo(memberInfo.getFirst_name() + " "
                + memberInfo.getLast_name());
    }

    /*@Override
    public void onSuccess(MemberInfoModel memberInfoModel) {
        MemberInfoModel.Data.MemberInfo memberInfo = memberInfoModel.getData().getMemberinfo();
        super.setToolbarTitle(memberInfo.getFirst_name(), true);

        final String age = StringUtility.getAge2(memberInfo.getBirthday());
        Boolean hasTicket = false;
        Boolean hasTable = false;

        if (isMe) {
            *//*final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
            final HashSet<String> profileImages = (HashSet<String>) App.getSharedPreferences().getStringSet(Common.PREF_IMAGES, new HashSet<String>());
            photos = profileImages.toArray(new String[profileImages.size()]);
            final int size = photos.length;
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    photos[i] = String.format("file:///%s%s", dataPath, photos[i]);
                }
            } else {
                final ArrayList<String> userPhotos = memberInfo.getPhotos();
                photos = new String[userPhotos.size()];
                for (int i = 0; i < userPhotos.size(); i++) {
                    photos[i] = userPhotos.get(i);
                }
            }*//*

            final LoginModel loginModel = AccountManager.loadLogin();

            if (TextUtils.isEmpty(loginModel.getLocation())) {
                txtLocation.setVisibility(View.GONE);
            } else if (loginModel.getLocation().equalsIgnoreCase("n/a")) {
                txtLocation.setVisibility(View.GONE);
            } else {
                txtLocation.setText(loginModel.getLocation());
            }

            if (TextUtils.isEmpty(memberInfo.getAbout())) {
                if (TextUtils.isEmpty(loginModel.getAbout()))
                    txtDescription.setVisibility(View.GONE);
                else {
                    txtDescription.setText(loginModel.getAbout());
                }
            } else {
                txtDescription.setText(memberInfo.getAbout());
            }

            hasTicket = false;
            hasTable = false;
        } else {
            txtDescription.setText(memberInfo.getAbout());
            if (TextUtils.isEmpty(memberInfo.getLocation()))
                txtLocation.setVisibility(View.GONE);
            else if (memberInfo.getLocation().equalsIgnoreCase("n/a"))
                txtLocation.setVisibility(View.GONE);
            if (TextUtils.isEmpty(memberInfo.getAbout()))
                txtDescription.setVisibility(View.GONE);

            hasTicket = memberInfo.getBadge_ticket();
            hasTable = memberInfo.getBadge_booking();

            ArrayList<MemberInfoModel.Data.MemberInfo.LikesEvent> likesEvents = memberInfo.getLikes_event();
        }

        String name = memberInfo.getFirst_name() + " "
                + memberInfo.getLast_name();

        txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));
        if (isMe) //saya
        {
            btnEdit.setVisibility(View.VISIBLE);
            lblPhoneNumber.setVisibility(View.GONE);
            SettingModel settingModel = AccountManager.loadSetting();
            final String phoneNo = settingModel.getData().getPhone();
            if (phoneNo == null || phoneNo.isEmpty()) {
                lblPhoneNumber.setText(getResources().getString(R.string.verify_your_phone_number));
            } else {
                lblPhoneNumber.setText(phoneNo);
            }
        } else //guest
        {
            btnEdit.setVisibility(View.GONE);
            if(hasTicket)
            {
                imgHasTicket.setVisibility(View.VISIBLE);
                imgHasTicket.bringToFront();
            }

            if(hasTable)
            {
                imgHasTable.setVisibility(View.VISIBLE);
                imgHasTable.bringToFront();
            }
        }
        setToolbarTitle(name, true);
        refreshLayout.setRefreshing(false);
    }*/

    @Override
    public void onFailure() {
        //Toast.makeText(ProfileDetailActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, getResources().getString(R.string.socket_timeout_exception), Toast.LENGTH_SHORT).show();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getPhoto() {

    }

    @Override
    public void loadImageToCertainView(String tempUrl, int position) {

    }

    @Override
    public void onFinishUpload(int position) {

    }

    @Override
    public void onFailUpload(int position) {

    }

    @Override
    public void doCrop(Uri filepath, Uri destination) {

    }

    @Override
    public ImageView makeTransparent(int position) {
        return null;
    }

    @Override
    public ImageView removeTransparent(int position) {
        return null;
    }

    @Override
    public Bitmap onFinishTakePhoto(int requestCode, Uri uri) {
        return null;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isMe)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_profile, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.edit_profile:
                Intent i = new Intent(this, EditProfileActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onEventContainerClick(final String eventId) {
        Intent i = new Intent(this, EventDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, eventId);
        i.putExtra("from", "profile");
        super.startActivity(i);
    }

    @OnClick(R.id.img_edit)
    void onImgEditClick() {
        redirectToEditProfile();
    }

    @OnClick(R.id.img_edit_top)
    void onImgEditTopClick() {
        redirectToEditProfile();
    }

    void redirectToEditProfile() {
        Intent i = new Intent(this, EditProfileActivity.class);
        startActivity(i);
    }
}