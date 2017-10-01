package com.robbypark.superaww;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Robby on 10/1/2017.
 */

public class DownloadImage extends AsyncTask<String, Void, Drawable> {

    private static final String TAG = "DownloadImage";

    public interface OnImageAvailable {
        void onImageAvailable(Drawable drawable);
    }

    OnImageAvailable mCallback;

    public DownloadImage(OnImageAvailable callback) {
        mCallback = callback;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String src = params[0];
        Drawable drawable = null;
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            drawable = Drawable.createFromStream(is, "src");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: error downloading image" + e.getMessage());
        }
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (mCallback != null)
            mCallback.onImageAvailable(drawable);
    }
}
