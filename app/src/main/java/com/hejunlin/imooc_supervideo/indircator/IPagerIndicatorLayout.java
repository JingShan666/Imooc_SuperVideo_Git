package com.hejunlin.imooc_supervideo.indircator;

/**
 * Created by hejunlin on 17/4/8.
 */

public interface IPagerIndicatorLayout extends IPagerChangeListener{

    /**
     * 添加到CoolIndicatorLayout上
     */
    void onAttachCoolIndicatorLayout();

    /**
     * 从CoolIndicatorLayout下移除
     */
    void onDetachCoolIndicatorLayout();
}
