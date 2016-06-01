package com.jiggie.android.activity.profile;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.jiggie.android.App;
import com.jiggie.android.component.ImageCompressionAsyncTask;
import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessUploadModel;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(
                uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        doCropImage(filePath);
        //doLoadImage(filePath);
        //profileDetailView.doCrop(filePath);
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
        doCropImage(selectedImagePath);
        //doLoadImage(selectedImagePath);
        //profileDetailView.doCrop(selectedImagePath);
    }


    public ProfileDetailPresenterImplementation(ProfileDetailView profileDetailView) {
        this.profileDetailView = profileDetailView;
    }

    private void doCropImage(final String filepath)
    {
        profileDetailView.doCrop(Uri.parse("file:///" + filepath), Uri.parse("file://" + getProfilePicFile().getPath()));
    }

    private File getProfilePicFile()
    {
        if(memberInfo != null && memberInfo.getPhotos() != null)
        {
            final int position = memberInfo.getPhotos().size();
            File file = new File(App.getInstance().getFilesDir().getPath(), "profile"+position+".jpg");
            return file;
        }
        return null;
    }

    protected void doLoadImage(final String url)
    {
        ImageCompressionAsyncTask imageCompression = new ImageCompressionAsyncTask() {
            @Override
            protected void onPostExecute(byte[] imageBytes) {
                final int position = memberInfo.getPhotos().size();
                // image here is compressed & ready to be sent to the server
                //FileUtils.writeByteArrayToFile(new File("pathname"), myByteArray);
                FileOutputStream fos = null;
                try {
                    //File file = new File(App.getInstance().getFilesDir().getPath(), "profile"+position+".jpg");
                    //Utils.d(TAG, "urlDoInBG " + url);
                    //File file = getProfilePicFile();
                    //File file = new File("/" + url);
                    File file = new File(App.getInstance().getFilesDir().getPath(), "profile"+position+".jpg");
                    fos = new FileOutputStream(file.getAbsolutePath());
                    fos.write(imageBytes);
                    fos.close();

                    final String url = file.getAbsolutePath();
                    profileDetailView.loadImageToCertainView(/*url*/ file.getAbsolutePath()
                            , memberInfo.getPhotos().size());
                    memberInfo.getPhotos().add(url);
                    doUpload(url, position);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        imageCompression.execute(url);
    }

    private void doUpload(final String path, final int position) {
        File file = new File(path);
        if (file.exists()) {
            AccountManager.doUpload(file, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    memberInfo.getPhotos().remove(position);
                    memberInfo.getPhotos().add(position, ((SuccessUploadModel) object).getUrl()
                            + "?" + System.currentTimeMillis());
                    App.getInstance().trackMixPanelPictureUp(Utils.PICTURE_UPLOAD, ((SuccessUploadModel) object).getUrl());
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
        if (memberInfo != null && memberInfo.getPhotos() != null && memberInfo.getPhotos().size() >= position) //ada isinya
        {
            profileDetailView.makeTransparent(position - 1);
            final String url = memberInfo.getPhotos().get(position - 1);
            Utils.d(TAG, "doDelete " + position);
            doDelete(url, position - 1);
            App.getInstance().trackMixPanelPictureUp(Utils.PICTURE_DELETE, url);
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
