package com.jiggie.android.activity.profile;

import android.graphics.Bitmap;
import android.net.Uri;

import com.jiggie.android.model.MemberInfoModel;

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

    Bitmap onFinishTakePhoto(int requestCode, Uri uri);
}
