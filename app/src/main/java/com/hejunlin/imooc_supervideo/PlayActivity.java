package com.hejunlin.imooc_supervideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hejunlin.imooc_supervideo.model.sohu.Video;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = PlayActivity.class.getSimpleName();
    private String mUrl;
    private int mStreamType;
    private int mCurrentPosition;
    private Video mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mUrl = getIntent().getStringExtra("url");
        mStreamType = getIntent().getIntExtra("type", 0);
        mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
        mVideo = getIntent().getParcelableExtra("video");

        Log.d(TAG, ">> ulr " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
        Log.d(TAG, ">> video " + mVideo);
    }
}
