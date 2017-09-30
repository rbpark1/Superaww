package com.robbypark.superaww;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetRedditJsonData getRedditJsonData = new GetRedditJsonData("https://www.reddit.com/user/316nuts/m/superaww/.json");
        getRedditJsonData.execute();
        Log.d(TAG, "onResume: ends");
    }
}
