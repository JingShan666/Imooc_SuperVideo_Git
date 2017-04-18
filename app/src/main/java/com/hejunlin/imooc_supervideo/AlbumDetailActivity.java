package com.hejunlin.imooc_supervideo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hejunlin.imooc_supervideo.api.OnGetAlbumDetailListener;
import com.hejunlin.imooc_supervideo.api.OnGetVideoPlayUrlListener;
import com.hejunlin.imooc_supervideo.api.SiteApi;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.detail.AlbumPlayGridFragment;
import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.utils.ImageUtils;

public class AlbumDetailActivity extends BaseActivity {

    private static final String TAG = AlbumDetailActivity.class.getSimpleName();
    private Album mAlbum;
    private boolean mIsShowDesc;
    private int mVideoNo;
    private ImageView mAlbumImg;
    private TextView mAlbumName;
    private TextView mDirector;
    private TextView mMainActor;
    private TextView mAlbumDesc;
    private boolean mIsFavor;
    private AlbumPlayGridFragment mFragment;
    private Button mSuperBitstreamButton;
    private Button mNormalBitstreamButton;
    private Button mHighBitstreamButton;
    private int mCurrentVideoPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        mAlbum = getIntent().getParcelableExtra("album");
        mVideoNo = getIntent().getIntExtra("videoNo", 0);
        mIsShowDesc = getIntent().getBooleanExtra("isShowDesc", false);
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(mAlbum.getTitle());//显示标题

        mAlbumImg = bindViewId(R.id.iv_album_image);
        mAlbumName = bindViewId(R.id.tv_album_name);
        mDirector = bindViewId(R.id.tv_album_director);
        mMainActor = bindViewId(R.id.tv_album_mainactor);
        mAlbumDesc = bindViewId(R.id.tv_album_desc);
        mSuperBitstreamButton = bindViewId(R.id.bt_super);
        mSuperBitstreamButton.setOnClickListener(mOnSuperClickListener);
        mNormalBitstreamButton = bindViewId(R.id.bt_normal);
        mNormalBitstreamButton.setOnClickListener(mOnNormalClickListener);
        mHighBitstreamButton = bindViewId(R.id.bt_high);
        mHighBitstreamButton.setOnClickListener(mOnHighClickListener);
    }

    private View.OnClickListener mOnSuperClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener mOnNormalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener mOnHighClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    //三个button有共同点,tag设置的id是一样,value值不一样
    private void handleButtonClick(View v) {
        Button button =  (Button) v;
        String url = (String) button.getTag(R.id.key_video_url);
        int type = (int) button.getTag(R.id.key_video_stream);//码流类型
        Video video = (Video) button.getTag(R.id.key_video);
        int currentPosition = (int) button.getTag(R.id.key_current_video_number);
        if (AppManager.isNetWorkAvailable()) {
            if (AppManager.isNetworkWifiAvailable()) {
                Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("type",type);
                intent.putExtra("currentPosition",currentPosition);
                intent.putExtra("video",video);
                startActivity(intent);
            } else {
                // TODO 拓展
            }
        }

    }

    @Override
    protected void initData() {
        mAlbumName.setText(mAlbum.getTitle());
        //导演
        if (!TextUtils.isEmpty(mAlbum.getDirector())) {
            mDirector.setText(getResources().getString(R.string.director) + mAlbum.getDirector());
            mDirector.setVisibility(View.VISIBLE);
        } else {
            mDirector.setVisibility(View.GONE);
        }
        //主演
        if (!TextUtils.isEmpty(mAlbum.getMainActor())) {
            mMainActor.setText(getResources().getString(R.string.mainactor) + mAlbum.getMainActor());
            mMainActor.setVisibility(View.VISIBLE);
        } else {
            mMainActor.setVisibility(View.GONE);
        }
        //描述
        if (!TextUtils.isEmpty(mAlbum.getAlbumDesc())) {
            mAlbumDesc.setText(mAlbum.getAlbumDesc());
            mAlbumDesc.setVisibility(View.VISIBLE);
        } else {
            mAlbumDesc.setVisibility(View.GONE);
        }
        //海报图
        if (!TextUtils.isEmpty(mAlbum.getHorImgUrl())) {
            ImageUtils.disPlayImage(mAlbumImg, mAlbum.getHorImgUrl());
        }

        if (!TextUtils.isEmpty(mAlbum.getVerImgUrl())) {
            ImageUtils.disPlayImage(mAlbumImg, mAlbum.getVerImgUrl());
        }
        SiteApi.onGetAlbumDetail(1, mAlbum, new OnGetAlbumDetailListener() {
            @Override
            public void onGetAlbumDetailSuccess(final Album album) {
                Log.d(TAG, ">> onGetAlbumDetailSuccess album " + album.getVideoTotal());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFragment =  AlbumPlayGridFragment.newInstance(album, mIsShowDesc, 0);
                        mFragment.setPlayVideoSelectedListener(mPlayVideoSelectedListener);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container,mFragment);
                        ft.commit();
                        getFragmentManager().executePendingTransactions();
                    }
                });

            }

            @Override
            public void onGetAlbumDetailFailed(ErrorInfo info) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://actionbar 左边箭头id
                finish();
                return true;
            case R.id.action_favor_item:
                if (mIsFavor) {
                    mIsFavor = false;
                    // TODO 收藏状态存储
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_unfavor_item:
                if (!mIsFavor) {
                    mIsFavor = true;
                    // TODO 收藏状态存储
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已添加收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_detail_menu,menu);
        return true;
    }

    /**
     * 必须先创建menu,否则会报空指针
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favitem = menu.findItem(R.id.action_favor_item);
        MenuItem unfavitem = menu.findItem(R.id.action_unfavor_item);
        favitem.setVisible(mIsFavor);
        unfavitem.setVisible(!mIsFavor);
        return super.onPrepareOptionsMenu(menu);
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

    private AlbumPlayGridFragment.OnPlayVideoSelectedListener mPlayVideoSelectedListener = new AlbumPlayGridFragment.OnPlayVideoSelectedListener() {
        @Override
        public void OnPlayVideoSelected(Video video, int position) {
            mCurrentVideoPosition = position;
            SiteApi.onGetVideoPlayUrl(1, video, mVideoUrlListener);
        }
    };

    public class StreamType {
        public static final int SUPER = 1;
        public static final int NORMAL = 2;
        public static final int HIGH = 3;
    }

    private OnGetVideoPlayUrlListener mVideoUrlListener = new OnGetVideoPlayUrlListener() {
        @Override
        public void onGetSuperUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetSuperUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSuperBitstreamButton.setVisibility(View.VISIBLE);
                    mSuperBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mSuperBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mSuperBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mSuperBitstreamButton.setTag(R.id.key_video_stream, StreamType.SUPER); //码流
                }
            });
        }

        @Override
        public void onGetNoramlUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetNoramlUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNormalBitstreamButton.setVisibility(View.VISIBLE);
                    mNormalBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mNormalBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mNormalBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mNormalBitstreamButton.setTag(R.id.key_video_stream, StreamType.NORMAL); //码流
                }
            });
        }

        @Override
        public void onGetHighUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetHighUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHighBitstreamButton.setVisibility(View.VISIBLE);
                    mHighBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mHighBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mHighBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mHighBitstreamButton.setTag(R.id.key_video_stream, StreamType.HIGH); //码流
                }
            });
        }

        @Override
        public void onGetFailed(ErrorInfo info) {
            Log.d(TAG,">> onGetFailed url " + info.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideAllButton();//请求播放源失败,不展示s
                }
            });
        }
    };

    private void hideAllButton() {
        mSuperBitstreamButton.setVisibility(View.GONE);
        mNormalBitstreamButton.setVisibility(View.GONE);
        mHighBitstreamButton.setVisibility(View.GONE);
    }
}
