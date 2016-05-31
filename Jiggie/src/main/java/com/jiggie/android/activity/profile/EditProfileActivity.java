package com.jiggie.android.activity.profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.view.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Wandy on 5/2/2016.
 */
public class EditProfileActivity extends ToolbarActivity
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
    @Bind(R.id.main_profile_picture_plus)
    ImageView mainProfilePicturePlus;
    @Bind(R.id.secondary_profile_picture_plus)
    ImageView secondaryProfilePicturePlus;
    @Bind(R.id.third_profile_picture_plus)
    ImageView thirdProfilePicturePlus;
    @Bind(R.id.fourth_profile_picture_plus)
    ImageView fourthProfilePicturePlus;
    @Bind(R.id.fifth_profile_picture_plus)
    ImageView fifthProfilePicturePlus;
    @Bind(R.id.txtDescription)
    TextView txtDescription;
    @Bind(R.id.txtTitleDescription)
    TextView txtTitleDescription;
    @Bind(R.id.txtLocation)
    TextView txtLocation;

    String fb_id;
    private ProfileDetailPresenterImplementation profilePresenter;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;

    private float smallerRadius = 3.0f;

    private final String TAG = EditProfileActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_IMAGE_KITKAT_REQUEST = 2;
    private final int EDIT_PROFILE = 9;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 28;


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

        final LoginModel loginModel = AccountManager.loadLogin();

        if (TextUtils.isEmpty(loginModel.getLocation())) {
            txtLocation.setVisibility(View.GONE);
        } else if (loginModel.getLocation().equalsIgnoreCase("n/a")) {
            txtLocation.setVisibility(View.GONE);
        } else {
            txtLocation.setText(loginModel.getLocation());
        }

        fetchDetail();
    }

    @Override
    public void loadImages(ArrayList<String> photos) {
        int photoSize = photos.size();
        if (photoSize > 5)
            photoSize = 5;
        for (int i = 0; i < photoSize; i++) {
            final String url = photos.get(i);
            ImageView view = removeTransparent(i);
            loadIntoView(url, view, i);
        }

        for (int i = photos.size(); i < 5; i++) {
            ImageView plusView = getPlusImageView(i);
            plusView.setImageDrawable(getResources().getDrawable(R.drawable.plus_button_image_view));

            ImageView view = removeTransparent(i);
            view.setImageBitmap(null);
        }
    }

    @Override
    public void onFinishUpload(int position) {
        ImageView view = removeTransparent(position);
    }

    @Override
    public void onFailUpload(int position) {
        ImageView view = removeTransparent(position);
        view.setImageBitmap(null);
        ImageView plus = getPlusImageView(position);
        plus.setImageDrawable(getResources().getDrawable(R.drawable.plus_button_image_view));

    }

    private void loadIntoView(final String url, final ImageView view
            , final int position) {
        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(view);
        ImageView plus = getPlusImageView(position);
        plus.setImageDrawable(getResources().getDrawable(R.drawable.cross_button_image_view));
    }

    @Override
    public void loadImageToCertainView(final String tempUrl, final int position) {

        ImageView view = makeTransparent(position);
        loadIntoView(tempUrl, view, position);
    }

    @Override
    public ImageView makeTransparent(int position) {
        ImageView view = getImageView(position);
        view.setImageAlpha(100);
        view.setScaleType(scaleType);
        return view;
    }


    @Override
    public ImageView removeTransparent(int position) {
        ImageView view = getImageView(position);
        view.setImageAlpha(255);
        view.setScaleType(scaleType);
        return view;
    }

    private ImageView getImageView(int position) {
        switch (position) {
            case 0:
                return mainProfilePicture;
            case 1:
                return secondaryProfilePicture;
            case 2:
                return thirdProfilePicture;
            case 3:
                return fourthProfilePicture;
            case 4:
                return fifthProfilePicture;
        }
        return null;
    }

    private ImageView getPlusImageView(int position) {
        switch (position) {
            case 0:
                return mainProfilePicturePlus;
            case 1:
                return secondaryProfilePicturePlus;
            case 2:
                return thirdProfilePicturePlus;
            case 3:
                return fourthProfilePicturePlus;
            case 4:
                return fifthProfilePicturePlus;
        }
        return null;
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
        txtDescription.setText(memberInfo.getAbout());
        txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, getResources().getString(R.string.socket_timeout_exception), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void getPhoto() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        else if(Build.VERSION.SDK_INT >= 23)
        {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_DENIED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        else {
            startGalleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startGalleryIntent();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(EditProfileActivity.this
                            , "You must enable the permission before you can upload photos", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startGalleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_KITKAT_REQUEST);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PICK_IMAGE_KITKAT_REQUEST
                        || requestCode == PICK_IMAGE_KITKAT_REQUEST)) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                if (requestCode == PICK_IMAGE_REQUEST) {
                    profilePresenter.onFinishTakePhoto(requestCode, uri, getContentResolver());
                } else if (requestCode == PICK_IMAGE_KITKAT_REQUEST) {
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    try {
                        String id = uri.getLastPathSegment().split(":")[1];
                        final String[] imageColumns = {MediaStore.Images.Media.DATA};
                        final String imageOrderBy = null;

                        Uri urii = getUri();
                        Cursor imageCursor = managedQuery(urii, imageColumns,
                                MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

                        profilePresenter.onFinishTakeKitkatPhoto(requestCode, uri, takeFlags
                                , getContentResolver(), imageCursor);
                    } catch (IndexOutOfBoundsException e) {
                        Utils.d(TAG, "out of bound exception");
                    }
                }


            }
        } else if (resultCode == RESULT_OK && requestCode == EDIT_PROFILE) {
            if (TextUtils.isEmpty(AccountManager.loadLogin().getAbout())) {
                txtDescription.setVisibility(View.GONE);
            } else {
                txtDescription.setVisibility(View.VISIBLE);
                this.txtDescription.setText(AccountManager.loadLogin().getAbout());
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profilePresenter.doLoadImage(resultUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
    public Bitmap onFinishTakePhoto(int requestCode, Uri uri) {
        return null;
    }

    @OnClick(R.id.main_profile_picture_plus)
    public void onMainProfilePictureClick() {
        profilePresenter.onImageClick(1);
    }


    @OnClick(R.id.secondary_profile_picture_plus)
    public void onSecodaryProfilePictureClick() {
        profilePresenter.onImageClick(2);
    }


    @OnClick(R.id.third_profile_picture_plus)
    public void onThirdProfilePictureClick() {
        profilePresenter.onImageClick(3);
    }


    @OnClick(R.id.fourth_profile_picture_plus)
    public void onFourthProfilePictureClick() {
        profilePresenter.onImageClick(4);
    }


    @OnClick(R.id.fifth_profile_picture_plus)
    public void onFifthProfilePictureClick() {
        profilePresenter.onImageClick(5);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnEdit)
    void btnEditOnClick() {
        super.startActivityForResult(new Intent(this, ProfileEditActivity.class)
                .putExtra(Common.FIELD_ABOUT, txtDescription.getText()), EDIT_PROFILE);
    }

    @Override
    public void doCrop(String imagePath) {
        CropImage.activity(Uri.parse(imagePath))
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
}
