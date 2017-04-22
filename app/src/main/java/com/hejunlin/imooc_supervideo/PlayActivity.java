package com.hejunlin.imooc_supervideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayActivity extends BaseActivity {

    private static final String TAG = PlayActivity.class.getSimpleName();
    private String mUrl;
    private int mStreamType;
    private int mCurrentPosition;
    private Video mVideo;
    private IjkVideoView mVideoView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        mUrl = getIntent().getStringExtra("url");
        mStreamType = getIntent().getIntExtra("type", 0);
        mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
        mVideo = getIntent().getParcelableExtra("video");

        Log.d(TAG, ">> ulr " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
        Log.d(TAG, ">> video " + mVideo);
        //init player
        mVideoView = bindViewId(R.id.video_view);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    @Override
    protected void initData() {

    }
}
