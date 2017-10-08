package com.robbypark.superaww;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRedditJsonData.OnDataAvailable, DownloadImage.OnImageAvailable {
    private static final String TAG = "MainActivity";

    private PhotoView mPhotoView;
    private Button mBtnNext;
    private Button mBtnOut;
    private ProgressBar mProgressBar;

    private List<Photo> mPhotoList;
    private String mAfter = "";
    private int count = 0;
    private MainActivity mActivity = this;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mBtnNext = (Button) findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoList.isEmpty() || isLoading){
                    //do nothing
                } else {
                    if(count < mPhotoList.size()){//if there are still unviewed photos in the array
                        DownloadImage downloadImage = new DownloadImage(mActivity);//download/load image into photoview
                        downloadImage.execute(mPhotoList.get(count).getUrl());
                        isLoading = true;
                        toggleProgressBar();
                    } else {//all photos in array have been viewed, get new data using after
                        GetRedditJsonData getRedditJsonData = new GetRedditJsonData(mActivity, "https://www.reddit.com/user/316nuts/m/superaww/.json", mAfter);
                        getRedditJsonData.execute();
                        count = 0;
                    }

                }
            }
        });

        mBtnOut = (Button) findViewById(R.id.btnOut);
        mBtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage(mPhotoList.get(count-1).getTitle() + "\n" + mPhotoList.get(count-1).getScore() + " upvotes");
                builder.setPositiveButton("open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("https://www.reddit.com" + mPhotoList.get(count-1).getPermalink());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mPhotoView = (PhotoView) findViewById(R.id.imageView);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetRedditJsonData getRedditJsonData = new GetRedditJsonData(this, "https://www.reddit.com/user/316nuts/m/superaww/.json", mAfter);
        getRedditJsonData.execute();
        Log.d(TAG, "onResume: ends");
    }


    @Override
    public void onDataAvailable(DownloadStatus status, List<Photo> photos, String after) {
        Log.d(TAG, "onDataAvailable: starts");
        mPhotoList = photos;
        mAfter = after;
        DownloadImage downloadImage = new DownloadImage(this);
        downloadImage.execute(mPhotoList.get(0).getUrl());
        isLoading = true;
        toggleProgressBar();
        Log.d(TAG, "onDataAvailable: ends");
    }


    @Override
    public void onImageAvailable(Drawable drawable) {
        Log.d(TAG, "onImageAvailable: starts");
        mPhotoView.setImageDrawable(drawable);
        count++;
        isLoading = false;
        toggleProgressBar();
        Log.d(TAG, "onImageAvailable: ends");
    }

    private void toggleProgressBar(){
        if(isLoading){//loading in progress, show progress bar
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}

