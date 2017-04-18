package com.hejunlin.imooc_supervideo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejunlin on 17/4/15.
 */

public class CustomGridView extends GridView {

    private Context mContext;
    private List<ViewHolder> mFooterViewList = new ArrayList<>();
    private boolean isLoading;
    private boolean mHasMoreItem;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnScrolledListener mOnScrollListener;

    //加载更多
    public interface OnLoadMoreListener {
        void onLoadMoreItems();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public interface OnScrolledListener {
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public void setOnScrollListener(OnScrolledListener listener) {
        mOnScrollListener = listener;
    }

    public CustomGridView(Context context) {
        super(context);
        initView (context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView (context);
    }

    public boolean isHasMoreItem() {
        return mHasMoreItem;
    }

    public void setHasMoreItem(boolean hasMoreItem) {
        this.mHasMoreItem = hasMoreItem;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    private void initView(Context context) {
        mContext = context;
        isLoading = false;
        //添加LoadingView
        LoadingView loadingView = new LoadingView(mContext);
        addFooterView(loadingView);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                 if (mOnScrollListener != null) {
                     mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                 }
                 if (totalItemCount > 0) {
                     int lastViewVisible = firstVisibleItem + visibleItemCount;
                     // 1.不是正在加载中
                     // 2.已经加载多最后了
                     // 3.当前可见的最后一个item==总视图可见item
                     if (!isLoading && mHasMoreItem && lastViewVisible == totalItemCount) {
                         if (mOnLoadMoreListener != null) {
                             mOnLoadMoreListener.onLoadMoreItems();
                         }
                     }
                 }
            }
        });
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView (context);
    }

    //添加footerView
    public void addFooterView(View view, Object data, boolean isSelcted) {
        ViewHolder holder = new ViewHolder();
        FrameLayout fl = new FullWidthViewLayout(mContext);
        fl.addView(view);
        holder.view = view;
        holder.data = data;
        holder.viewContainer = fl;
        holder.isSelected = isSelcted;
        mFooterViewList.add(holder);
    }

    public void addFooterView(View view) {
        addFooterView(view, null, true);
    }

    // 移除footerview
    public void removeFooterView(View v) {
        if (mFooterViewList.size() > 0) {
            removeHolder(v, mFooterViewList);
        }
    }

    private void removeHolder(View view, List<ViewHolder> list) {
        for (int i = 0; i < list.size(); i++) {
            ViewHolder holder = list.get(i);
            if (holder.view == view) {
                list.remove(i);
                break;
            }
        }
    }

    public void notifyChanged() {
        this.requestLayout();
        this.invalidate();
    }

    //FooterView容器
    class ViewHolder{
        public View view;
        public ViewGroup viewContainer;
        public Object data;
        public boolean  isSelected;
    }

    /**
     * 此layout用于FooterView填充整个宽度
     */
    class FullWidthViewLayout extends FrameLayout {

        public FullWidthViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int targetWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            MeasureSpec.makeMeasureSpec(targetWidth, MeasureSpec.getMode(widthMeasureSpec));
        }
    }

}
