package com.hejunlin.imooc_supervideo.indircator;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 标题
 * Created by hejunlin on 17/4/8.
 */
public class ViewPagerITitleView extends TextView implements IViewPagerTitleView {

    private int mNormalColor;
    private int mSelectedColor;

    public ViewPagerITitleView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        setGravity(Gravity.CENTER);//居中显示
        int padding = dip2px(context, 20);
        setPadding(padding, 0, padding, 0);
        setSingleLine();//单行
        setEllipsize(TextUtils.TruncateAt.END);//文字超过区域打点隐藏
    }

    /**
     * dip转换成像素
     * @param context
     * @param dip
     * @return
     */
    private int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        //通过Rect与画笔映射,取到对应的宽高
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() /2 - contentWidth /2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();//字的测量
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight()/2 - contentHeight /2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        //通过Rect与画笔映射,取到对应的宽高
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() /2 + contentWidth /2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();//字的测量
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight()/2 + contentHeight /2);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mSelectedColor);//选中时颜色变化
    }

    @Override
    public void onDisSelected(int index, int totalCount) {
        setTextColor(mNormalColor);//离开时颜色恢复正常
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight) {

    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }
}
