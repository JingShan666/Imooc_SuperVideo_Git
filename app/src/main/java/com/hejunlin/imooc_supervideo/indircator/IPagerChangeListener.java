package com.hejunlin.imooc_supervideo.indircator;

/**
 * Created by hejunlin on 17/4/8.
 */

public interface IPagerChangeListener {

    /**
     * 页面选中时回调
     * @param position
     */
    void onPagerSelected(int position);

    /**
     * 当页面滑动时回调
     * @param position  位置
     * @param positionOffsetPercent 0.0f-1.0f 滚动百分比
     * @param positionOffsetPixel 距离
     */
    void onPagerScrolled(int position, float positionOffsetPercent, int positionOffsetPixel);


    /**
     * 页面滑动状态发生变化时回调
     * 如从静止到滑动,或滑动到静止
     * @param position
     */
    void onPagerScrollStateChanged(int position);
}
