package com.jiggie.android.activity.profile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.view.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Wandy on 5/2/2016.
 */
public class NewProfileDetailActivity extends ToolbarActivity
        implements ProfileDetailView {

    @Bind(R.id.txtUser)
    TextView txtUser;
    @Bind(R.id.main_profile_picture)
    RoundedImageView mainProfilePicture;
    @Bind(R.id.secondary_profile_picture)
    RoundedImageView secondaryProfilePicture;
    @Bind(R.id.third_profile_picture)
    RoundedImageView thirdProfilePicture;
    @Bind(R.id.fourth_profile_picture)
    RoundedImageView fourthProfilePicture;
    @Bind(R.id.fifth_profile_picture)
    RoundedImageView fifthProfilePicture;

    String fb_id;
    ProfileDetailPresenterImplementation profilePresenter;

    private float smallerRadius = 3.0f;

    private final String TAG = NewProfileDetailActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_IMAGE_KITKAT_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_detail);
        super.bindView();

        profilePresenter = new ProfileDetailPresenterImplementation(this);
        mainProfilePicture.setRounded(true);
        secondaryProfilePicture.setRounded(true);
        secondaryProfilePicture.setRadius(smallerRadius);
        thirdProfilePicture.setRounded(true);
        thirdProfilePicture.setRadius(smallerRadius);
        fourthProfilePicture.setRounded(true);
        fourthProfilePicture.setRadius(smallerRadius);
        fifthProfilePicture.setRounded(true);
        fifthProfilePicture.setRadius(smallerRadius);
        fetchDetail();
    }

    @Override
    public void loadImages(ArrayList<String> photos) {
        for (int i = 0; i < photos.size(); i++) {
            final String url = photos.get(i);
            ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
            switch (i) {
                case 0:
                    mainProfilePicture.setScaleType(scaleType);
                    loadIntoView(url, mainProfilePicture);
                    break;
                case 1:
                    secondaryProfilePicture.setScaleType(scaleType);
                    loadIntoView(url, secondaryProfilePicture);
                    break;
                case 2:
                    thirdProfilePicture.setScaleType(scaleType);
                    loadIntoView(url, thirdProfilePicture);
                    break;
                case 3:
                    fourthProfilePicture.setScaleType(scaleType);
                    loadIntoView(url, fourthProfilePicture);
                    break;
                case 4:
                    fifthProfilePicture.setScaleType(scaleType);
                    loadIntoView(url, fifthProfilePicture);
                    break;
            }
        }
    }

    void loadIntoView(final String url, final ImageView view) {
        Glide
                .with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(view);
    }

    @Override
    public void fetchDetail() {
        profilePresenter.fetchMemberInfo(AccessToken.getCurrentAccessToken().getUserId());
    }

    @Override
    public void onSuccess(MemberInfoModel memberInfoModel) {
        MemberInfoModel.Data.MemberInfo memberInfo = memberInfoModel.getData().getMemberinfo();
        super.setToolbarTitle(memberInfo.getFirst_name(), true);
        final String age = StringUtility.getAge2(memberInfo.getBirthday());
        final String name = memberInfo.getFirst_name() + " "
                + memberInfo.getLast_name();

        txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));
    }

    @Override
    public void onFailure() {
    }

    @OnClick(R.id.fifth_profile_picture)
    public void onFifthProfilePictureClick() {
        profilePresenter.getPhoto();
    }

    @Override
    public void getPhoto() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_KITKAT_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            profilePresenter.onFinishTakePhoto(requestCode, uri, getContentResolver());
        } else if (requestCode == PICK_IMAGE_KITKAT_REQUEST && resultCode == RESULT_OK) {
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            String id = uri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;

            Uri urii = getUri();
            Cursor imageCursor = managedQuery(urii, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

            profilePresenter.onFinishTakeKitkatPhoto(requestCode, uri, takeFlags
                    , getContentResolver(), imageCursor);
        }
    }

    // By using this method get the Uri of Internal/External Storage for Media
    public Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    @Override
    public Bitmap onFinishTakePhoto ( int requestCode, Uri uri){
        return null;
    }

}
