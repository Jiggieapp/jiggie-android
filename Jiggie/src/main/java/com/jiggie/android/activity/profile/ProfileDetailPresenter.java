package com.jiggie.android.activity.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Wandy on 5/2/2016.
 */
public interface ProfileDetailPresenter {

    void onResume();

    void onDestroy();

    void fetchMemberInfo(final String fb_id);

    void getPhoto();

    void onFinishTakePhoto(int requestCode, Uri uri, ContentResolver contentResolver);

    void onFinishTakeKitkatPhoto(int requestCode, Uri uri, int takeFlags, ContentResolver contentResolver, Cursor imageCursor);
}
