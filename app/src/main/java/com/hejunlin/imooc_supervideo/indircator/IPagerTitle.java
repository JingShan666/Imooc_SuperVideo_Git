package com.hejunlin.imooc_supervideo.indircator;

/**
 * Created by hejunlin on 17/4/8.
 */

public interface IPagerTitle {
    /**
     *
     * @param index  第几个
     * @param totalCount 总共多少个
     */
    void onSelected(int index, int totalCount);

    void onDisSelected(int index, int totalCount);

    /**
     *
     * @param index
     * @param totalCount
     * @param leavePercent  取值 0.0f - 1.0f (1.0f表示完全离开)
     * @param isLeftToRight 是否从左向右离开
     */
    void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight);

    /**
     *
     * @param index
     * @param totalCount
     * @param enterPercent 取值 0.0f - 1.0f (1.0f表示完全进入)
     * @param isLeftToRight 是否从左向右进入
     */
    void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight);
}
