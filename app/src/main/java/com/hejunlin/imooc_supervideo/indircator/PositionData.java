package com.hejunlin.imooc_supervideo.indircator;

/**
 * Created by hejunlin on 17/4/8.
 */

public class PositionData {

    public int mLeft;//左坐标
    public int mRight;//右坐标
    public int mBottom;
    public int mTop;

    public int mContentLeft;//内容区域左坐标
    public int mContentRight;
    public int mContentBottom;
    public int mContentTop;

    /**
     * 控件本身宽度
     * @return
     */
    public int width() {
        return mRight - mLeft;
    }

    /**
     * 控件本身高度
     * @return
     */
    public int height() {
        return mBottom - mTop;
    }

    /**
     * 内容区域宽度
     * @return
     */
    public int contentWidth() {
        return mContentRight - mContentLeft;
    }

    /**
     * 内容区域高度
     * @return
     */
    public int contentHeight() {
        return mContentBottom - mContentTop;
    }

    /**
     * 水平方向上终点位置
     * @return
     */
    public int horizonalCenter() {
        return mLeft + width() / 2;
    }

    public int verticalCenter() {
        return mTop + height() / 2;
    }


}
