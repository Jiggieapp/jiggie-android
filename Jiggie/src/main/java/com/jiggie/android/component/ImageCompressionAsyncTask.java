package com.jiggie.android.component;

import android.os.AsyncTask;

/**
 * Created by Wandy on 5/11/2016.
 */
public abstract class ImageCompressionAsyncTask extends AsyncTask<String, Void, byte[]> {

    @Override
    protected byte[] doInBackground(String... strings) {
        if(strings.length == 0 || strings[0] == null)
            return null;
        return ImageUtils.compressImage(strings[0]);
    }

    protected abstract void onPostExecute(byte[] imageBytes) ;
}
