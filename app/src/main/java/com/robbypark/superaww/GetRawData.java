package com.robbypark.superaww;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Robby on 9/30/2017.
 */

enum DownloadStatus {
    IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK
}

public class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";

    //interface needed for use when GetRawData is called from data formatting Async Task
    public interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    public GetRawData(OnDownloadComplete callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }
    
    public void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");
        if(mCallback != null){
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (params == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code was " + response);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder result = new StringBuilder();

            //constructs String containing raw JSON data
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();


        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException reading data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: security exception, need permission? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }
        //if this point is reached, connection / data download failed
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (mCallback != null) {
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
    }
}
