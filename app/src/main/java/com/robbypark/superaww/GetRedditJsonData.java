package com.robbypark.superaww;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Robby on 9/30/2017.
 */

public class GetRedditJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete{

    @Override
    protected List<Photo> doInBackground(String... params) {
        return null;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        
    }
}
