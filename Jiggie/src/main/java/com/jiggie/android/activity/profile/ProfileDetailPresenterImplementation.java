package com.jiggie.android.activity.profile;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.MemberInfoModel;

import java.io.File;
import java.io.IOException;

/**
 * Created by Wandy on 5/2/2016.
 */
public class ProfileDetailPresenterImplementation implements ProfileDetailPresenter {
    ProfileDetailView profileDetailView;
    private final String TAG = ProfileDetailPresenterImplementation.class.getSimpleName();
    private MemberInfoModel memberInfoModel;

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        profileDetailView = null;
    }

    @Override
    public void fetchMemberInfo(String fb_id) {
        if (profileDetailView != null) {
            AccountManager.loaderMemberInfo(fb_id, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    memberInfoModel = (MemberInfoModel) object;
                    profileDetailView.onSuccess(memberInfoModel);
                    profileDetailView.loadImages(memberInfoModel.getData().getMemberinfo().getPhotos());
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    profileDetailView.onFailure();
                }
            });
        }

    }

    @Override
    public void getPhoto() {
        profileDetailView.getPhoto();
    }

    @Override
    public void onFinishTakePhoto(int requestCode, Uri uri, ContentResolver contentResolver) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(
                uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        doUpload(filePath);

        //Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

        //profileDetailView.onFinishTakePhoto(bitmap);
    }


    public ProfileDetailPresenterImplementation(ProfileDetailView profileDetailView) {
        this.profileDetailView = profileDetailView;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onFinishTakeKitkatPhoto(int requestCode, Uri uri
            , int takeFlags, ContentResolver contentResolver, Cursor imageCursor) {
        // Check for the freshest data.
        contentResolver.takePersistableUriPermission(uri, takeFlags);

        String selectedImagePath = null;
        if (imageCursor.moveToFirst()) {
            selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        doUpload(selectedImagePath);
    }

    private void doUpload(final String path) {
        File file = new File(path);
        if (file.exists()) {
            AccountManager.doUpload(file, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {

                }

                @Override
                public void onFailure(int responseCode, String message) {

                }
            });
        } else Utils.d(TAG, "not exist");
    }

    @Override
    public void onImageClick(int position) {
        if (memberInfoModel.getData().getMemberinfo().getPhotos().size() >= position)
        {
            ProfileM
        }
    }
}
