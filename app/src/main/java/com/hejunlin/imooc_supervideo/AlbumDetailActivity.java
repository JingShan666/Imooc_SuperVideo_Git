package com.hejunlin.imooc_supervideo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.model.Album;

public class AlbumDetailActivity extends BaseActivity {

    private Album mAlbum;
    private boolean mIsShowDesc;
    private int mVideoNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        mAlbum = getIntent().getParcelableExtra("album");
        mVideoNo = getIntent().getIntExtra("videoNo", 0);
        mIsShowDesc = getIntent().getBooleanExtra("isShowDesc", true);
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(mAlbum.getTitle());//显示标题
    }

    @Override
    protected void initData() {

    }

    public static void launch(Activity activity, Album album, int vidoeNo, boolean isShowDesc) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("album", album);
        intent.putExtra("videoNo", vidoeNo);
        intent.putExtra("isShowDesc",isShowDesc);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, Album album) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("album", album);
        activity.startActivity(intent);
    }
}
