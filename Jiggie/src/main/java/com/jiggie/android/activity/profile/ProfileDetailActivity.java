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
import com.android.jiggie.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Guest;
import com.jiggie.android.model.Login;
import com.jiggie.android.model.Setting;
import com.jiggie.android.model.UserProfile;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONObject;

import java.util.HashSet;

import butterknife.Bind;
import butterknife.OnClick;
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
    private UserProfile currentProfile;
    private Guest guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_detail);
        super.bindView();

        this.pagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        this.imagePagerIndicator.setAdapter(this.pagerIndicatorAdapter.getIndicatorAdapter());
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onGlobalLayout() {
        this.refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        final Guest guest = super.getIntent().getParcelableExtra(Guest.class.getName());

        if (guest == null) {
            final Login login = Login.getCurrentLogin();
            super.setToolbarTitle(login.getName(), true);

            final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
            final HashSet<String> profileImages = (HashSet<String>) App.getSharedPreferences().getStringSet(Common.PREF_IMAGES, new HashSet<String>());
            final String[] photos = profileImages.toArray(new String[profileImages.size()]);
            final int size = photos.length;

            for (int i = 0; i < size; i++)
                photos[i] = String.format("file:///%s%s", dataPath, photos[i]);

            this.pagerIndicatorAdapter.setImages(photos);
            this.txtLocation.setText(login.getLocation());
            this.txtDescription.setText(login.getAbout());
            this.txtUser.setText(login.getName());
            this.btnEdit.setVisibility(View.GONE);
        } else {
            App.getInstance().trackMixPanelEvent("View Member Profile");
            super.setToolbarTitle(guest.getName(), true);
            this.txtDescription.setText(guest.getAbout());
            this.txtUser.setText(guest.getName());
            this.btnEdit.setVisibility(View.GONE);
            this.guest = guest;
        }

        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        this.btnEdit.setVisibility(View.GONE);
        this.refreshLayout.setRefreshing(true);
        final String userId = AccessToken.getCurrentAccessToken().getUserId();
        final String guestId = this.guest == null ? userId : this.guest.getFacebookId();
        final String url = String.format("memberinfo/%s/%s/%s", Setting.getCurrentSetting().getAccountType(), guestId, userId);

        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<UserProfile, JSONObject>() {
            @Override
            public UserProfile onResponseAsync(JSONObject jsonObject) { return currentProfile = new UserProfile(jsonObject); }

            @Override
            public void onResponseCompleted(UserProfile value) {
                final String age = StringUtility.getAge(value.getBirthDate());
                txtLocation.setText(value.getLocation());
                txtDescription.setText(value.getAbout());

                if (guest != null)
                    pagerIndicatorAdapter.setImages(value.getPhotos());
                if (TextUtils.isEmpty(value.getLocation()))
                    txtLocation.setVisibility(View.GONE);
                else if (value.getLocation().equalsIgnoreCase("n/a"))
                    txtLocation.setVisibility(View.GONE);
                if (TextUtils.isEmpty(value.getAbout()))
                    txtDescription.setText(R.string.about);

                txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? value.getName() : String.format("%s, %s", value.getName(), age));
                btnEdit.setVisibility(guest == null ? View.VISIBLE : View.GONE);
                setToolbarTitle(value.getName(), true);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileDetailActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnEdit)
    void btnEditOnClick() { super.startActivityForResult(new Intent(this, ProfileEditActivity.class).putExtra(UserProfile.FIELD_ABOUT, this.currentProfile.getAbout()), 0); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.currentProfile.setAbout(data.getStringExtra(UserProfile.FIELD_ABOUT));
            this.txtDescription.setText(this.currentProfile.getAbout());
            this.currentProfile.save(this);
        }
    }
}
