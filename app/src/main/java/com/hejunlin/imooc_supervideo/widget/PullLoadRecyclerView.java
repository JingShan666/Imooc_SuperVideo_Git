package com.hejunlin.imooc_supervideo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;

/**
 * Created by hejunlin on 17/3/29.
 */

public class PullLoadRecyclerView extends LinearLayout {

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefresh = false; //是否是刷新
    private boolean mIsLoadMore = false; //是否是加载更多
    private RecyclerView mRecyclerView;
    private View mFootView;
    private AnimationDrawable mAnimationDrawable;
    private OnPullLoadMoreListener mOnPullLoadMoreListener;

    public PullLoadRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PullLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullLoadRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pull_loadmore_layout, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //设置刷新时控件颜色渐变
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh());

        //处理RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true); //设置固定大小
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//使用默认动画
        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefresh || mIsLoadMore;
            }
        });

        mRecyclerView.setVerticalScrollBarEnabled(false);//隐藏滚动条
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll());
        mFootView = view.findViewById(R.id.footer_view);
        ImageView imageView = (ImageView) mFootView.findViewById(R.id.iv_load_img);
        imageView.setBackgroundResource(R.drawable.imooc_loading);
        mAnimationDrawable = (AnimationDrawable) imageView.getBackground();

        TextView textView = (TextView) mFootView.findViewById(R.id.tv_load_text);
        mFootView.setVisibility(View.GONE);
        //view 包含swipeRefreshLayout, RecyclerView, FootView
        this.addView(view);//
    }

    //外部可以设置recyclerview的列数
    public void setGridLayout(int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(mContext, spanCount);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
             if (!mIsRefresh) {
                 mIsRefresh = true;
                 refreshData();
             }
        }
    }

    class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = 0;
            int lastItem = 0;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int totalCount = manager.getItemCount();
            if (manager instanceof GridLayoutManager) {
                GridLayoutManager gridlayoutManager = (GridLayoutManager) manager;
                //第一个完全可见的item
                firstItem = gridlayoutManager.findFirstCompletelyVisibleItemPosition();
                //最后一个完全可见的item
                lastItem = gridlayoutManager.findLastCompletelyVisibleItemPosition();
                if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
                    lastItem = gridlayoutManager.findLastVisibleItemPosition();
                }
            }
            //什么时候触发上拉加载更多?
            // 1.加载更多是false
            // 2.totalCount - 1 === lastItem
            // 3.mSwipeRefreshLayout可以用
            // 4. 不是处于下拉刷新状态
            // 5. 偏移量dx > 0 或dy > 0
            if (!mIsLoadMore
                && totalCount - 1 == lastItem
                && mSwipeRefreshLayout.isEnabled()
                && !mIsRefresh
                && (dx > 0 || dy > 0)) {
                mIsLoadMore = true;
                //在加载更多时,禁止mSwipeRefreshLayout使用
                mSwipeRefreshLayout.setEnabled(false);
                loadMoreData();
            } else {
                mSwipeRefreshLayout.setEnabled(true);
            }
        }
    }

    private void refreshData() {
        if (mOnPullLoadMoreListener != null) {
            mOnPullLoadMoreListener.reRresh();
        }
    }

    private void loadMoreData() {
        if (mOnPullLoadMoreListener != null) {
            mFootView.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mFootView.setVisibility(View.VISIBLE);
                    mAnimationDrawable.start();
                }
            }).start();
            invalidate();
            mOnPullLoadMoreListener.loadMore();
        }
    }

    //设置刷新完毕
    public void setRefreshCompleted() {
        mIsRefresh = false;
        setRefreshing(false);
    }

    //设置是否正在刷新
    private void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    public void setLoadMoreCompleted() {
        mIsLoadMore = false;
        mIsRefresh = false;
        setRefreshing(false);
        mFootView.animate().translationY(mFootView.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300).start();
    }

    public interface OnPullLoadMoreListener {
        void reRresh();
        void loadMore();
    }

    public void setOnPullLoadMoreListener(OnPullLoadMoreListener listener) {
        mOnPullLoadMoreListener = listener;
    }
}
