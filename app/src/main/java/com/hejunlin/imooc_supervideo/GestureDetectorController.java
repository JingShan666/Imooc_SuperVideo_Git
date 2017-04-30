package com.hejunlin.imooc_supervideo;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static com.hejunlin.imooc_supervideo.GestureDetectorController.ScrollType.NOTHING;

/**
 * Created by hejunlin on 17/4/29.
 */

public class GestureDetectorController implements GestureDetector.OnGestureListener {

    private static final String TAG = GestureDetectorController.class.getSimpleName();
    private GestureDetector mGestureDetector;
    private IGestureListener mGestureListener;
    private int mWidth;
    private ScrollType mCurrentType;

    public GestureDetectorController(Context context, IGestureListener listener) {
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mGestureListener = listener;
        mGestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mCurrentType = NOTHING;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mGestureListener != null) {
            if (mCurrentType != NOTHING) {
                switch (mCurrentType) {
                    case VERTICAL_LEFT:
                        mGestureListener.onScrollVerticalLeft(distanceY, e1.getY() - e2.getY());
                        break;
                    case VERTICAL_RIGH:
                        mGestureListener.onScrollVerticalRight(distanceY, e1.getY() - e2.getY());
                        break;
                    case HORIZONTAL:
                        mGestureListener.onScrollHorizontal(distanceX, e2.getX() - e1.getX());
                        break;
                }
                return false;
            }
        }
        //水平方向上滑动判断
        if (Math.abs(distanceY) <= Math.abs(distanceX)) {
            mCurrentType = ScrollType.HORIZONTAL;
            mGestureListener.onScrollStart(mCurrentType);
            return false;
        }
        int i = mWidth / 3;
        //左滑判断
        if (e1.getX() <= i) {
            mCurrentType = ScrollType.VERTICAL_LEFT;
            mGestureListener.onScrollStart(mCurrentType);
        } else if (e1.getX() > i*2) {//右滑判断
            mCurrentType = ScrollType.VERTICAL_RIGH;
            mGestureListener.onScrollStart(mCurrentType);
        } else {
            mCurrentType = NOTHING;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
       return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    public enum ScrollType {
        NOTHING,
        VERTICAL_LEFT,
        VERTICAL_RIGH,
        HORIZONTAL
    }

    public interface IGestureListener {
        void onScrollStart(ScrollType type);//开始滑动
        void onScrollHorizontal(float x1, float x2);//水平滑动
        void onScrollVerticalLeft(float y1, float y2);//左滑
        void onScrollVerticalRight(float y1, float y2);//右滑
    }
}
