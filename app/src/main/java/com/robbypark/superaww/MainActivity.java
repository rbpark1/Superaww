package com.robbypark.superaww;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements GetRedditJsonData.OnDataAvailable{
    private static final String TAG = "MainActivity";

    private ImageView mGifImageView;
    private Button mButton;

    private DownloadStatus mStatus;
    private List<Photo> mPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGifImageView = (ImageView) findViewById(R.id.imageView);
        mButton = (Button) findViewById(R.id.btnNext);
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
        mPhotoList = photos;
        mGifImageView.setImageDrawable(mPhotoList.get(0).getImage());
    }
}
