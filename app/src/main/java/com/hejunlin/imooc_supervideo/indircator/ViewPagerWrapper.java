package com.hejunlin.imooc_supervideo.indircator;

import android.support.v4.view.ViewPager;

/**
 * Created by hejunlin on 17/4/8.
 */

public class ViewPagerWrapper {

    private CoolIndicatorLayout mCoolIndicatorLayout;
    private ViewPager mViewPager;

    public ViewPagerWrapper(CoolIndicatorLayout layout, ViewPager viewPager) {
        mCoolIndicatorLayout = layout;
        mViewPager = viewPager;
    }

    public static ViewPagerWrapper with(CoolIndicatorLayout layout, ViewPager viewPager) {
        return new ViewPagerWrapper(layout, viewPager);
    }

    /**
     * 只要有viewpager变化,指示器及title都回有变化
     */
    public void compose() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCoolIndicatorLayout.onPagerScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mCoolIndicatorLayout.onPagerSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mCoolIndicatorLayout.onPagerScrollStateChanged(state);
            }
        });
    }
}
