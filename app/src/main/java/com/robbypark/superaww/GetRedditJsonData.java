package com.robbypark.superaww;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robby on 9/30/2017.
 */

public class GetRedditJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetRedditJsonData";

    public interface OnDataAvailable {
        void onDataAvailable(DownloadStatus status, List<Photo> photos, String after);
    }

    private List<Photo> mPhotoList = null;
    private String mAfter;
    private String mBaseUrl;
    private OnDataAvailable mCallback;
    private DownloadStatus mStatus;

    public GetRedditJsonData(OnDataAvailable callback, String baseUrl, String after) {
        Log.d(TAG, "GetRedditJsonData: called");
        mBaseUrl = baseUrl;
        mCallback = callback;
        mAfter = after;
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        //because getRawData is being called from an async task, run on same thread
        getRawData.runInSameThread(mBaseUrl, mAfter);
        return mPhotoList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: started. Status = " + status);
        mStatus = status;
        if (status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
        }
        try {
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            mAfter = jsonData.getString("after");
            Log.d(TAG, "onDownloadComplete: after = " + mAfter);
            JSONArray itemsArray = jsonData.getJSONArray("children");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i).getJSONObject("data");

                String title = jsonPhoto.getString("title");
                String permalink = jsonPhoto.getString("permalink");
                String url = jsonPhoto.getString("url");
                int score = jsonPhoto.getInt("ups");

                if((url.contains("imgur") || url.contains("i.redd.it")) && url.contains(".jpg")){
                    mPhotoList.add(new Photo(title, score, permalink, url));
                    Log.d(TAG, "onDownloadComplete: " + i + " " + mPhotoList.get(mPhotoList.size()-1).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onDownloadComplete: Error processing JSON data " + e.getMessage());
            mStatus = DownloadStatus.FAILED_OR_EMPTY;
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        if (mStatus == DownloadStatus.OK && photos != null) {
            mCallback.onDataAvailable(mStatus, photos, mAfter);
        }
    }
}
