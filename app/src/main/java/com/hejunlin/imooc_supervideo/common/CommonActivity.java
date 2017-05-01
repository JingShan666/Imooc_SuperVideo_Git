package com.hejunlin.imooc_supervideo.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏与历史记录的基类,不关心具体业务
 */
public abstract class CommonActivity extends BaseActivity {

    private GridView mGridView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AlbumList mAlbumList = new AlbumList();
    private CommonDBHelper mCommonDBHelper;
    private CommonAdapter mAdapter;
    private TextView mEmptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_favorite;
    }

    public abstract String getTitleText();
    public abstract String getEmptyText();
    public abstract Context getContext();
    public abstract String getTableName();

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(getTitleText());

        mGridView = bindViewId(R.id.gv_favorite);
        mEmptyView = bindViewId(R.id.tv_empty);
        mSwipeRefreshLayout = bindViewId(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        //设置刷新时控件颜色渐变
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mGridView.setOnScrollListener(mScrollListener);
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
                if (mAdapter.isSelected()) {
                    showDeleteDialog();
                } else {
                    //选中态
                    setShowChecked(true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CommonActivity.this);
        dialog.setMessage(getResources().getString(R.string.favorite_dialog_message));
        DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //删除选中的item
                        deleteAllSelectedItem();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //取消选中状态
                        setShowChecked(false);
                        break;
                }
            }
        };
        dialog.setPositiveButton(getResources().getString(R.string.favorite_dialog_sure), mDialogListener);
        dialog.setNegativeButton(getResources().getString(R.string.favorite_dialog_quit), mDialogListener);
        dialog.show();
    }

    private void deleteAllSelectedItem() {
        //1、list存哪些item被选中
        //2、遍历list删除
        //3、刷新ui
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getItem(i).isIsChecked()) {//item被选中
                list.add(i); // 添加position到list容器中
            }
        }
        for (int k : list) { //遍历删除
            Album album = mAdapter.getFavorAlbmList().get(k).getAlbum();//获取album
            mCommonDBHelper.delete(album.getAlbumId(), album.getSite().getSiteId());
        }
        reloadData();
    }

    /**
     * 设置item的选中状态
     * @param isSelected
     */
    private void setShowChecked(boolean isSelected) {
        if (!isSelected) {
            mAdapter.optionCheckedAllItem(isSelected);
        }
        mAdapter.setShowChecked(isSelected);
        mAdapter.notifyDataSetInvalidated();//刷新数据
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
            reloadData();
        }
    };

    private void reloadData() {
        mAlbumList = mCommonDBHelper.getAllData();
        isNeedShowEmptyView();
        mAdapter = new CommonAdapter(CommonActivity.this, mAlbumList);
        mGridView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        //adapter item checked become false
        mAdapter.setShowChecked(false);
    }

    private void isNeedShowEmptyView() {
        if (mAlbumList.size() == 0) {//没有数据显示
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getEmptyText());
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        mCommonDBHelper = new CommonDBHelper(getContext());
        mCommonDBHelper.setParams(getTableName());
        mAlbumList = mCommonDBHelper.getAllData();
        isNeedShowEmptyView();
        mAdapter = new CommonAdapter(this, mAlbumList);
        mGridView.setAdapter(mAdapter);
    }

}
