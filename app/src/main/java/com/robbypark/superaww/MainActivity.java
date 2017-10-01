package com.robbypark.superaww;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRedditJsonData.OnDataAvailable, DownloadImage.OnImageAvailable {
    private static final String TAG = "MainActivity";

    private ImageView mImageView;
    private Button mButton;
    private DownloadStatus mStatus;
    private List<Photo> mPhotoList;
    private int count = 0;
    private MainActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.btnNext);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoList.isEmpty()){
                    //do nothing
                } else {
                    if(count < mPhotoList.size()){
                        DownloadImage downloadImage = new DownloadImage(mActivity);
                        downloadImage.execute(mPhotoList.get(count).getUrl());
                    } else {
                        //get new entries
                    }

                }
            }
        });
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetRedditJsonData getRedditJsonData = new GetRedditJsonData(this, "https://www.reddit.com/user/316nuts/m/superaww/.json");
        getRedditJsonData.execute();
        Log.d(TAG, "onResume: ends");
    }


    @Override
    public void onDataAvailable(DownloadStatus status, List<Photo> photos) {
        Log.d(TAG, "onDataAvailable: starts");
        mPhotoList = photos;
        DownloadImage downloadImage = new DownloadImage(this);
        downloadImage.execute(mPhotoList.get(0).getUrl());
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onImageAvailable(Drawable drawable) {
        Log.d(TAG, "onImageAvailable: starts");
        mImageView.setImageDrawable(drawable);
        count++;
        Log.d(TAG, "onImageAvailable: ends");
    }
}
