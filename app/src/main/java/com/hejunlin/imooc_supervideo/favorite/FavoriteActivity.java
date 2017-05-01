package com.hejunlin.imooc_supervideo.favorite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.live.LiveActivity;
import com.hejunlin.imooc_supervideo.model.AlbumList;

public class FavoriteActivity extends BaseActivity {

    private GridView mGridView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AlbumList mAlbumList = new AlbumList();
    private FavoriteDBHelper mFavoriteDBHelper;
    private FavoriteAdapter mAdapter;
    private TextView mEmptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(getResources().getString(R.string.favorite_title));

        mGridView = bindViewId(R.id.gv_favorite);
        mEmptyView = bindViewId(R.id.tv_empty);
        mSwipeRefreshLayout = bindViewId(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        //设置刷新时控件颜色渐变
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mGridView.setOnScrollListener(mScrollListener);
        mFavoriteDBHelper = new FavoriteDBHelper(this);
        mAlbumList = mFavoriteDBHelper.getAllData();
        isNeedShowEmptyView();
        mAdapter = new FavoriteAdapter(this, mAlbumList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://actionbar 左边箭头id
                finish();
                return true;
            case R.id.action_delete:
                //TODO
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private GridView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean enable = false;
            if (mGridView != null && mGridView.getChildCount() > 0) {
                //第一个item是否可见
                boolean isFirstItemVisible = mGridView.getFirstVisiblePosition() == 0;
                //第一个item离顶部是否为0
                boolean topOfFirstItemVisible = mGridView.getChildAt(0).getTop() == 0;
                enable = isFirstItemVisible && topOfFirstItemVisible;
            }
            //下拉刷新是否可用
            mSwipeRefreshLayout.setEnabled(enable);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mAlbumList = mFavoriteDBHelper.getAllData();
            isNeedShowEmptyView();
            mAdapter = new FavoriteAdapter(FavoriteActivity.this, mAlbumList);
            mGridView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
            //TODO adapter item checked become false
        }
    };

    private void isNeedShowEmptyView() {
        if (mAlbumList.size() == 0) {//没有数据显示
            mEmptyView.setText(getResources().getString(R.string.favorite_empty));
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

}
