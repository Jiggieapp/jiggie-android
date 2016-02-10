package com.jiggie.android.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.MemberInfoModel;
import com.facebook.AccessToken;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by rangg on 14/11/2015.
 */
public class ProfileDetailActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.imagePagerIndicator) HListView imagePagerIndicator;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout refreshLayout;
    @Bind(R.id.imageViewPager) ViewPager imageViewPager;
    @Bind(R.id.txtDescription) TextView txtDescription;
    @Bind(R.id.txtLocation) TextView txtLocation;
    @Bind(R.id.btnEdit) ImageButton btnEdit;
    @Bind(R.id.txtUser) TextView txtUser;

    private ImagePagerIndicatorAdapter pagerIndicatorAdapter;
    //private UserProfile currentProfile;
    private MemberInfoModel memberInfoModel;
    private GuestModel.Data.GuestInterests guest;
    String fb_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_detail);
        super.bindView();

        EventBus.getDefault().register(this);

        this.pagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        this.imagePagerIndicator.setAdapter(this.pagerIndicatorAdapter.getIndicatorAdapter());
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onGlobalLayout() {
        this.refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        App.getInstance().trackMixPanelEvent("View Member Profile");
        fb_id = super.getIntent().getStringExtra(Common.FIELD_FACEBOOK_ID);
        if(fb_id==null){
            fb_id = AccessToken.getCurrentAccessToken().getUserId();
        }

        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        this.btnEdit.setVisibility(View.GONE);
        this.refreshLayout.setRefreshing(true);

        AccountManager.loaderMemberInfo(fb_id);
    }

    public void onEvent(MemberInfoModel message){
        super.setToolbarTitle(message.getData().getMemberinfo().getFirst_name(), true);
        memberInfoModel = message;

        final String age = StringUtility.getAge2(message.getData().getMemberinfo().getBirthday());

        txtLocation.setText(message.getData().getMemberinfo().getLocation());
        txtDescription.setText(message.getData().getMemberinfo().getAbout());

        final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
        final String[] photos = message.getData().getMemberinfo().getPhotos().toArray(new String[message.getData().getMemberinfo().getPhotos().size()]);
        final int size = photos.length;

        this.pagerIndicatorAdapter.setImages(photos);

        if (TextUtils.isEmpty(message.getData().getMemberinfo().getLocation()))
            txtLocation.setVisibility(View.GONE);
        else if (message.getData().getMemberinfo().getLocation().equalsIgnoreCase("n/a"))
            txtLocation.setVisibility(View.GONE);
        if (TextUtils.isEmpty(message.getData().getMemberinfo().getAbout()))
            txtDescription.setText(R.string.about);

        String name = message.getData().getMemberinfo().getFirst_name() + " " + message.getData().getMemberinfo().getLast_name();

        txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));
        btnEdit.setVisibility(guest == null ? View.VISIBLE : View.GONE);
        setToolbarTitle(name, true);
        refreshLayout.setRefreshing(false);
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_PROFILE_DETAIL)){
            Toast.makeText(ProfileDetailActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnEdit)
    void btnEditOnClick() { super.startActivityForResult(new Intent(this, ProfileEditActivity.class).putExtra(Common.FIELD_ABOUT, memberInfoModel.getData().getMemberinfo().getAbout()), 0); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //this.currentProfile.setAbout(data.getStringExtra(UserProfile.FIELD_ABOUT));
            this.txtDescription.setText(memberInfoModel.getData().getMemberinfo().getAbout());
            //this.currentProfile.save(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}