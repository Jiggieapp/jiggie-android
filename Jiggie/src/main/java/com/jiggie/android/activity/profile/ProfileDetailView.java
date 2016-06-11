package com.jiggie.android.activity.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.SuccessUploadModel;

import java.util.ArrayList;

/**
 * Created by Wandy on 5/2/2016.
 */
public interface ProfileDetailView {

    void loadImages(ArrayList<String> photos);
    void fetchDetail();
    void onSuccess(MemberInfoModel memberInfoModel);
    void onFailure();
    void getPhoto();
    void loadImageToCertainView(final String tempUrl, int position);
    void onFinishUpload(final int position);
    void onFailUpload(final int position);
    void doCrop(Uri filepath, Uri destination);
    ImageView makeTransparent(final int position);
    ImageView removeTransparent(final int position);

    Bitmap onFinishTakePhoto(int requestCode, Uri uri);
}
