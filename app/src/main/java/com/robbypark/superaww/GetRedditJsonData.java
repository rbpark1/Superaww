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


    private List<Photo> mPhotoList = null;
    private String mBaseUrl;

    public GetRedditJsonData(String baseUrl) {
        Log.d(TAG, "GetRedditJsonData: called");
        mBaseUrl = baseUrl;
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        //because getRawData is being called from an async task, run on same thread
        getRawData.runInSameThread(mBaseUrl);
        return mPhotoList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status){
        Log.d(TAG, "onDownloadComplete: started. Status = " + status);

        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();
        }
        try{
            JSONObject jsonRoot =  new JSONObject(data);
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray itemsArray = jsonData.getJSONArray("children");
            for(int i = 0; i < itemsArray.length(); i++){
                JSONObject jsonPhoto = itemsArray.getJSONObject(i).getJSONObject("data");
                String title = jsonPhoto.getString("title");
                String permalink = jsonPhoto.getString("permalnk");
                String url = jsonPhoto.getString("url");
                int score = jsonPhoto.getInt("ups");
                mPhotoList.add(new Photo(title, score, permalink, url));
                Log.d(TAG, "onDownloadComplete: " + mPhotoList.get(i).toString());
            }

//            JSONArray itemsArray = jsonData.getJSONArray("items");
//
//            for(int i = 0; i < itemsArray.length(); i++){
//                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
//
//            }


        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "onDownloadComplete: Error processing JSON data " +e.getMessage() );
            status = DownloadStatus.FAILED_OR_EMPTY;
        }
        Log.e(TAG, "onDownloadComplete: ends");
    }
}
