package com.hejunlin.imooc_supervideo.indircator;

import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

/**
 * Created by hejunlin on 17/4/8.
 */

public class ViewPagerIndicatorHelper {

    private int mCurrentIndex;
    private int mTotalCount;
    private int mScrollState = ViewPager.SCROLL_STATE_IDLE;
    private int mLastIndex;
    private float mLastPositionOffsetSum;
    private SparseArray<Float> mLeavePercents = new SparseArray<>();
    private SparseBooleanArray mDeSelectedItems = new SparseBooleanArray();


    public ViewPagerIndicatorHelper() {

    }

    public void setScrollListener(IPagerTitle listener) {
        mScrollListener = listener;
    }

    private IPagerTitle mScrollListener;

    public void onPageScrolled(int position, float positionOffset, int positionPixel) {
        float currentPositionOffsetSum = position + positionOffset;
        boolean isLeftToRight = currentPositionOffsetSum > mLastPositionOffsetSum;
        int safePosition = getSafeIndex(position);
        //不是就绪状态时
        if (mScrollState != ViewPager.SCROLL_STATE_IDLE) {
            int enterIndex, leaveIndex;
            float enterPerent, leavePercent;
            if (isLeftToRight) {
                enterIndex = getSafeIndex(position + 1);
                enterPerent = positionOffset;
                leaveIndex = safePosition;
                leavePercent = positionOffset;
            } else {
                enterIndex = safePosition;
                enterPerent = 1.0f - positionOffset;
                leaveIndex = getSafeIndex(safePosition + 1);
                leavePercent = 1.0f - positionOffset;
            }

            for (int i = 0; i < mTotalCount; i++) {
                if (i == enterIndex || i == leaveIndex) {
                    continue;
                }
                Float leavedPercent = mLeavePercents.get(i, 0.0f);
                if (leavedPercent != 1.0f) {
                    mScrollListener.onLeave(i, mTotalCount, 1.0f, isLeftToRight);
                    mLeavePercents.put(i, 1.0f);
                }
            }
            if (enterIndex == leaveIndex) {
                if (enterIndex == mTotalCount - 1 && mLeavePercents.get(enterIndex, 0.0f) != 0.0f && enterPerent == 0.0f && isLeftToRight) {
                    boolean disPatchEnterEvent = mScrollState == ViewPager.SCROLL_STATE_DRAGGING || enterIndex == mCurrentIndex;
                    if (disPatchEnterEvent) {
                        mScrollListener.onEnter(enterIndex, mTotalCount, 1.0f, true);
                        mLeavePercents.put(enterIndex, 0.0f);
                    }
                }
                return;
            }
            if (1.0f - mLeavePercents.get(enterIndex, 0.0f) != enterPerent) {
                boolean disPatchEnterEvent = mScrollState == ViewPager.SCROLL_STATE_DRAGGING || enterIndex == mCurrentIndex;
                if (disPatchEnterEvent) {
                    mScrollListener.onEnter(enterIndex, mTotalCount, enterPerent, isLeftToRight);
                    mLeavePercents.put(enterIndex, 1.0f - enterIndex);
                }
            }

            if (mLeavePercents.get(leaveIndex, 0.0f) != leavePercent) {
                if (isLeftToRight && leaveIndex == getSafeIndex(mCurrentIndex) && leavePercent == 0.0f) {
                    boolean disPatchEnterEvent = mScrollState == ViewPager.SCROLL_STATE_DRAGGING || leaveIndex == mCurrentIndex;
                    if (disPatchEnterEvent) {
                        mScrollListener.onEnter(leaveIndex, mTotalCount, 1.0f, true);
                        mLeavePercents.put(leaveIndex, 0.0f);
                    }
                } else {
                    boolean disPatchLeaveEvent = mScrollState == ViewPager.SCROLL_STATE_DRAGGING
                            || leaveIndex == mLastIndex
                            || (leaveIndex == mCurrentIndex -1) && mLeavePercents.get(leaveIndex, 0.0f) != 1.0f
                            || (leaveIndex == mCurrentIndex +1) && mLeavePercents.get(leaveIndex, 0.0f) != 1.0f;
                    if (disPatchLeaveEvent) {
                        mScrollListener.onLeave(leaveIndex, mTotalCount, leavePercent, isLeftToRight);
                    }
                }
            }
        }
        //滚动状态时
        else {
            for (int i =0; i < mTotalCount; i++) {
                if (i == mCurrentIndex) {
                    continue;
                }
                boolean deSelected = mDeSelectedItems.get(i);
                if (!deSelected) {
                    mScrollListener.onDisSelected(i, mTotalCount);
                }
                Float leavedPercent = mLeavePercents.get(i, 0.0f);
                if (leavedPercent != 1.0f) {
                    mScrollListener.onLeave(i, mTotalCount, 1.0f, isLeftToRight);
                    mLeavePercents.put(i, 1.0f);
                }
            }
            mScrollListener.onEnter(mCurrentIndex, mTotalCount, 1.0f, false);
            mLeavePercents.put(mCurrentIndex, 0.0f);
            mScrollListener.onSelected(mCurrentIndex, mTotalCount);
            mDeSelectedItems.put(mCurrentIndex, false);
        }
        mLastPositionOffsetSum = currentPositionOffsetSum;

    }

    public int getSafeIndex(int position) {
        return  Math.max(Math.min(position, mTotalCount - 1), 0);
    }

    public void onPageSelected(int position) {
        int currentIndex = setCurrentIndex(position);
        if (mScrollListener != null) {
            mScrollListener.onSelected(mCurrentIndex, mTotalCount);
            mDeSelectedItems.put(mCurrentIndex, false);
            for (int i = 0, j = mTotalCount; i < j; i++) {
                if (i == mCurrentIndex) {
                    continue;
                }
                boolean disSelected = mDeSelectedItems.get(i);
                if (!disSelected) {
                    mScrollListener.onDisSelected(i, mTotalCount);
                    mDeSelectedItems.put(i, true);
                }
            }
        }
    }

    public void onPageScrollStateChanged(int scrollState) {
        mScrollState = scrollState;
    }

    public int setCurrentIndex(int index) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = getSafeIndex(index);//重新赋值
        return mCurrentIndex;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public int getScrollState() {
        return mScrollState;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

}
