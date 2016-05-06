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
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessUploadModel;

import java.io.File;
import java.io.IOException;

/**
 * Created by Wandy on 5/2/2016.
 */
public class ProfileDetailPresenterImplementation implements ProfileDetailPresenter {
    ProfileDetailView profileDetailView;
    private final String TAG = ProfileDetailPresenterImplementation.class.getSimpleName();
    private MemberInfoModel memberInfoModel;
    private MemberInfoModel.Data.MemberInfo memberInfo;

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
                    memberInfo = memberInfoModel.getData().getMemberinfo();
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
        /*Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(
                uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        doLoadImage(filePath);

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
        doLoadImage(selectedImagePath);
    }

    private void doLoadImage(final String url)
    {
        final int position = memberInfo.getPhotos().size();
        profileDetailView.loadImageToCertainView(url
                , memberInfo.getPhotos().size());
        memberInfo.getPhotos().add(url);
        doUpload(url, position);
    }

    private void doUpload(final String path, final int position) {
        File file = new File(path);
        if (file.exists()) {
            AccountManager.doUpload(file, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    memberInfo.getPhotos().remove(position);
                    memberInfo.getPhotos().add(position, ((SuccessUploadModel) object).getUrl());
                    profileDetailView.onFinishUpload(position);
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    memberInfo.getPhotos().remove(position);
                    profileDetailView.onFailUpload(position);
                }
            });
        }
    }

    @Override
    public void onImageClick(int position) {
        if (memberInfo.getPhotos().size() >= position) //ada isinya
        {
            profileDetailView.makeTransparent(position - 1);
            final String url = memberInfo.getPhotos().get(position - 1);
            Utils.d(TAG, "url " + url);
            doDelete(url, position - 1);
        }
        else
        {
            profileDetailView.getPhoto();
        }
    }

    private void doDelete(final String url, final int position)
    {
        AccountManager.doDelete(url, new OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        memberInfo.getPhotos().remove(position);
                        profileDetailView.loadImages(memberInfo.getPhotos());
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {

                    }
                }
        );
    }

}
