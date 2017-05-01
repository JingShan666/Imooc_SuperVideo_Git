package com.hejunlin.imooc_supervideo.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.detail.AlbumDetailActivity;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.utils.DateUtils;
import com.hejunlin.imooc_supervideo.utils.SysUtils;
import com.hejunlin.imooc_supervideo.widget.media.IjkVideoView;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayActivity extends BaseActivity implements GestureDetectorController.IGestureListener{

    private static final String TAG = PlayActivity.class.getSimpleName();
    private static final int CHECK_TIME = 1;
    private static final int CHECK_BATTERY = 2;
    private static final int CHECK_PROGRESS = 3;
    private static final int AUTO_HIDE_TIME = 10000;
    private static final int AFTER_DRAGGLE_HIDE_TIME = 3000;
    private String mUrl;
    private int mStreamType;
    private int mCurrentPosition;
    private Video mVideo;
    private IjkVideoView mVideoView;
    private RelativeLayout mLoadingLayout;
    private TextView mLoadingText;
    private FrameLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mBackButton;
    private TextView mVideoNameView;
    private TextView mSysTimeView;
    private ImageView mBigPauseButton;
    private CheckBox mPlayOrPauseButton;
    private TextView mVideoCurrentTime;
    private TextView mVideoTotalTime;
    private TextView mBitStreamView;
    private EventHandler mEventHandler;
    private boolean mIsPanelShowing = false;
    private int mBatteryLevel;
    private ImageView mBatteryView;
    private boolean mIsMove = false;//是否在屏幕上滑动
    private SeekBar mSeekBar;
    private Formatter mFormatter;
    private StringBuilder mFormatterBuilder;
    private boolean mIsDragging;
    private GestureDetectorController mGestureController;
    private TextView mDragHorizontalView;
    private TextView mDragVerticalView;
    private long mScrollProgress;
    private boolean mIsHorizontalScroll;
    private boolean mIsVerticalScroll;
    private int mCurrentLight;
    private int mMaxLight = 255;
    private int mCurrentVolume;
    private int mMaxVolume = 10;
    private AudioManager mAudioManager;
    private String mLiveTitle;//直播节目标题

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
            mBatteryReceiver = null;
        }
        //释放audiofocus
        mAudioManager.abandonAudioFocus(null);
    }

    /**
     * 通过广播获取系统电量情况
     */
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLevel = intent.getIntExtra("level", 0);
            Log.d(TAG, ">> mBatteryReceiver onReceive mBatteryLevel=" + mBatteryLevel);
        }
    };

    @Override
    public void onScrollStart(GestureDetectorController.ScrollType type) {
        mIsMove = true;
        switch (type) {
            case HORIZONTAL:
                mDragHorizontalView.setVisibility(View.VISIBLE);
                mScrollProgress = -1;
                mIsHorizontalScroll = true;//水平滑动标识
                break;
            case VERTICAL_LEFT:
                setComposeDrawableAndText(mDragVerticalView, R.drawable.ic_light, this);
                mDragVerticalView.setVisibility(View.VISIBLE);
                updateVerticalText(mCurrentLight, mMaxLight);
                mIsVerticalScroll = true;
                break;
            case VERTICAL_RIGH:
                if (mCurrentVolume > 0) {
                    setComposeDrawableAndText(mDragVerticalView, R.drawable.volume_normal, this);
                } else {
                    setComposeDrawableAndText(mDragVerticalView, R.drawable.volume_no, this);
                }
                mDragVerticalView.setVisibility(View.VISIBLE);
                updateVerticalText(mCurrentVolume, mMaxVolume);
                mIsVerticalScroll = true;
                break;
        }
    }
    //用于组合图片及文字
    private void setComposeDrawableAndText(TextView textView, int drawableId, Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        //这四个参数表示把drawable绘制在矩形区域
        drawable.setBounds(0,0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //设置图片在文字的上方
        //The Drawables must already have had drawable.setBounds called.
        textView.setCompoundDrawables(null, drawable, null , null);
    }
    //更新垂直方向上滑动时的百分比
    private void updateVerticalText(int current, int total) {
        NumberFormat formater = NumberFormat.getPercentInstance();
        formater.setMaximumFractionDigits(0);//设置整数部分允许最大小数位 66.5%->66%
        String percent = formater.format((double)(current)/(double) total);
        mDragVerticalView.setText(percent);
    }
    //更新水平方向seek的进度, duration表示变化后的duration
    private void updateHorizontalText(long duration) {
        String text = stringForTime((int)duration) + "/" + stringForTime(mVideoView.getDuration());
        mDragHorizontalView.setText(text);
    }

    // 更新进度
    @Override
    public void onScrollHorizontal(float x1, float x2) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int MAX_SEEK_STEP = 300000;//最大滑动5分钟
        int offset = (int) (x2 / width * MAX_SEEK_STEP) + mVideoView.getCurrentPosition();
        long progress = Math.max(0, Math.min(mVideoView.getDuration(), offset));
        mScrollProgress = progress;
        updateHorizontalText(progress);
    }

    @Override
    public void onScrollVerticalLeft(float y1, float y2) {
        int height = getResources().getDisplayMetrics().heightPixels;
        int offset = (int) (mMaxLight * y1)/ height;
        if (Math.abs(offset) > 0) {
            mCurrentLight += offset;//得到变化后的亮度
            mCurrentLight = Math.max(0, Math.min(mMaxLight, mCurrentLight));
            // 更新系统亮度
            SysUtils.setBrightness(this, mCurrentLight);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putInt("shared_preferences_light", mCurrentLight);
            editor.commit();
            updateVerticalText(mCurrentLight, mMaxLight);
        }
    }

    @Override
    public void onScrollVerticalRight(float y1, float y2) {
        int height = getResources().getDisplayMetrics().heightPixels;
        int offset = (int) (mMaxVolume * y1)/ height;
        if (Math.abs(offset) > 0) {
            mCurrentVolume += offset;//得到变化后的声音
            mCurrentVolume = Math.max(0, Math.min(mMaxVolume, mCurrentVolume));
            // 更新系统声音
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume/10, 0);
            updateVerticalText(mCurrentVolume, mMaxVolume);
        }
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_TIME:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSysTimeView.setText(DateUtils.getCurrentTime());
                        }
                    });
                    break;
                case CHECK_BATTERY:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCurrentBattery();
                        }
                    });
                    break;
                case CHECK_PROGRESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long duration = mVideoView.getDuration();
                            long nowduration = (mSeekBar.getProgress() * duration)/1000L;
                            mVideoCurrentTime.setText(stringForTime((int)nowduration));
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mIsMove == false) {
                toggleTopAndBottomLayout();
            } else {
                mIsMove = false;
            }
            //水平方向,up时,seek到对应位置播放
            if (mIsHorizontalScroll) {
                mIsHorizontalScroll = false;
                mVideoView.seekTo((int)mScrollProgress);
                //一次down,up结束后mDragHorizontalView隐藏
                mDragHorizontalView.setVisibility(View.GONE);
            }
            if (mIsVerticalScroll) {
                mDragVerticalView.setVisibility(View.GONE);
                mIsVerticalScroll = false;
            }
        }
        return mGestureController.onTouchEvent(event);
    }

    @Override
    protected void initView() {
        mUrl = getIntent().getStringExtra("url");
        mLiveTitle = getIntent().getStringExtra("title");
        mStreamType = getIntent().getIntExtra("type", 0);
        mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
        mVideo = getIntent().getParcelableExtra("video");
        Log.d(TAG, ">> ulr " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
        Log.d(TAG, ">> video " + mVideo);
        mEventHandler = new EventHandler(Looper.myLooper());
        initAudio();
        initLight();
        initGesture();
        initTopAndBottomView();
        initCenterView();
        initListener();
        //init player
        mVideoView = bindViewId(R.id.video_view);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mLoadingLayout = bindViewId(R.id.rl_loading_layout);
        mLoadingText = bindViewId(R.id.tv_loading_info);
        mLoadingText.setText("正在加载中...");
        mVideoView.setVideoURI(Uri.parse(mUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mLoadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        toggleTopAndBottomLayout();
    }

    private void initCenterView() {
        mDragHorizontalView = bindViewId(R.id.tv_horiontal_gesture);
        mDragVerticalView = bindViewId(R.id.tv_vertical_gesture);
    }

    private void initAudio() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 10;// 系统声音取值是0-10,*10为了和百分比相关
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 10;
    }

    private void initLight() {
        mCurrentLight = SysUtils.getDefaultBrightness(this);
        if (mCurrentLight == -1) {//获取不到亮度sharedpreferences文件
            mCurrentLight = SysUtils.getBrightness(this);
        }
    }

    private void initGesture() {
        mGestureController = new GestureDetectorController(this, this);
    }

    private void initTopAndBottomView() {
        mTopLayout = bindViewId(R.id.fl_player_top_container);
        mBottomLayout = bindViewId(R.id.ll_player_bottom_layout);
        mBackButton = bindViewId(R.id.iv_player_close);//返回按钮
        mVideoNameView = bindViewId(R.id.tv_player_video_name);//video标题
        mBatteryView = bindViewId(R.id.iv_battery);
        mSysTimeView = bindViewId(R.id.tv_sys_time);//系统时间
        mBigPauseButton = bindViewId(R.id.iv_player_center_pause);//屏幕中央暂停按钮
        mPlayOrPauseButton = bindViewId(R.id.cb_play_pause);//底部播放暂停按钮
        mVideoCurrentTime = bindViewId(R.id.tv_current_video_time);//当前播放进度
        mVideoTotalTime = bindViewId(R.id.tv_total_video_time);//视频总时长
        mBitStreamView = bindViewId(R.id.tv_bitstream);//码流
        mSeekBar = bindViewId(R.id.sb_player_seekbar);
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mFormatterBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatterBuilder,Locale.getDefault());
    }

    private void initListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBigPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
                updatePlayPauseStatus(true);
            }
        });
        mPlayOrPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayPause();
            }
        });
    }

    private void toggleTopAndBottomLayout() {
        if (mIsPanelShowing) {
            hideTopAndBottomLayout();
        } else {
            showTopAndBottomLayout();
            //先显示,没有任何操作,就5s后隐藏
            mEventHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            }, AUTO_HIDE_TIME);
        }
    }

    private void showTopAndBottomLayout() {
        mIsPanelShowing = true;
        mTopLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        updateProgress();
        if (mEventHandler != null) {
            mEventHandler.removeMessages(CHECK_TIME);
            Message msg = mEventHandler.obtainMessage(CHECK_TIME);
            mEventHandler.sendMessage(msg);

            mEventHandler.removeMessages(CHECK_BATTERY);
            Message batterymsg = mEventHandler.obtainMessage(CHECK_BATTERY);
            mEventHandler.sendMessage(batterymsg);

            mEventHandler.removeMessages(CHECK_PROGRESS);
            Message progressmsg = mEventHandler.obtainMessage(CHECK_PROGRESS);
            mEventHandler.sendMessage(progressmsg);
        }
        switch (mStreamType) {
            case AlbumDetailActivity.StreamType.SUPER:
                mBitStreamView.setText(getResources().getString(R.string.stream_super));
                break;
            case AlbumDetailActivity.StreamType.NORMAL:
                mBitStreamView.setText(getResources().getString(R.string.stream_normal));
                break;
            case AlbumDetailActivity.StreamType.HIGH:
                mBitStreamView.setText(getResources().getString(R.string.stream_high));
                break;
            default:
                break;
        }
    }

    private void hideTopAndBottomLayout() {
        if (mIsDragging == true) {
            return;
        }
        mIsPanelShowing = false;
        mTopLayout.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
    }

    private void handlePlayPause() {
        //TODO
        if (mVideoView.isPlaying()) {//视频正在播放
            mVideoView.pause();
            updatePlayPauseStatus(false);
        } else {
            mVideoView.start();
            updatePlayPauseStatus(true);
        }
    }

    private void updatePlayPauseStatus(boolean isPlaying) {
        mBigPauseButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        mPlayOrPauseButton.invalidate();
        mPlayOrPauseButton.setChecked(isPlaying);
        mPlayOrPauseButton.refreshDrawableState();
    }


    @Override
    protected void initData() {
        Log.d(TAG, ">> initData mVideo=" + mVideo);
        if (mVideo != null) {
            Log.d(TAG, ">> initData mVideoName" + mVideo.getVideoName());
            mVideoNameView.setText(mVideo.getVideoName());
        }
        if (mLiveTitle != null) {
            mVideoNameView.setText(mLiveTitle);
        }
    }

    private void setCurrentBattery() {
        Log.d(TAG, ">> setCurrentBattery level " + mBatteryLevel);
        if ( 0 < mBatteryLevel && mBatteryLevel <= 10) {
            mBatteryView.setBackgroundResource(R.drawable.ic_battery_10);
        } else if (10 < mBatteryLevel && mBatteryLevel <= 20) {
            mBatteryView.setBackgroundResource(R.drawable.ic_battery_20);
        } else if (20 < mBatteryLevel && mBatteryLevel <= 50) {
            mBatteryView.setBackgroundResource(R.drawable.ic_battery_50);
        } else if (50 < mBatteryLevel && mBatteryLevel <= 80) {
            mBatteryView.setBackgroundResource(R.drawable.ic_battery_80);
        } else if (80 < mBatteryLevel && mBatteryLevel <= 100) {
            mBatteryView.setBackgroundResource(R.drawable.ic_battery_100);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // seekbar进度发生变化时回调
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            long duration = mVideoView.getDuration();//视频时长
            long nowPosition = (duration * progress) / 1000L;
            mVideoCurrentTime.setText(stringForTime((int) nowPosition));
        }

        // seekbar开始拖动时回调
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsDragging = true;
        }

        // seekbar拖动完成后回调
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsDragging = false;
            int progress = seekBar.getProgress();//最后拖动停止的进度
            long duration = mVideoView.getDuration();//视频时长
            long newPosition = (duration * progress) / 1000L;//当前的进度
            mVideoView.seekTo((int) newPosition);
            mEventHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            },AFTER_DRAGGLE_HIDE_TIME);
        }
    };

    private void updateProgress() {
        int currentPosition = mVideoView.getCurrentPosition();//当前的视频位置
        int duration = mVideoView.getDuration();//视频时长
        if (mSeekBar != null) {
            if (duration > 0) {
                //转成long型,避免溢出
                long pos = currentPosition * 1000L/ duration;
                mSeekBar.setProgress((int) pos);
            }
            int perent = mVideoView.getBufferPercentage();//已经缓冲的进度
            mSeekBar.setSecondaryProgress(perent);//设置缓冲进度
            mVideoCurrentTime.setText(stringForTime(currentPosition));
            mVideoTotalTime.setText(stringForTime(duration));
        }
    }


    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60; //换成秒
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600);
        mFormatterBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    //从直播模块跳转过来
    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

}
