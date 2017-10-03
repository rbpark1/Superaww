package com.robbypark.superaww;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
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
    private Button mBtnNext;
    private Button mBtnOut;
    private List<Photo> mPhotoList;
    private String mAfter = "";
    private int count = 0;
    private MainActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnNext = (Button) findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoList.isEmpty()){
                    //do nothing
                } else {
                    if(count < mPhotoList.size()){
                        DownloadImage downloadImage = new DownloadImage(mActivity);
                        downloadImage.execute(mPhotoList.get(count).getUrl());
                    } else {
                        GetRedditJsonData getRedditJsonData = new GetRedditJsonData(mActivity, "https://www.reddit.com/user/316nuts/m/superaww/.json", mAfter);
                        getRedditJsonData.execute();
                    }

                }
            }
        });

        mBtnOut = (Button) findViewById(R.id.btnOut);
        mBtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("test message");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView);
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
