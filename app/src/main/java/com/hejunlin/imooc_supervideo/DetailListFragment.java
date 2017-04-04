package com.hejunlin.imooc_supervideo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.base.BaseFragment;
import com.hejunlin.imooc_supervideo.model.Site;
import com.hejunlin.imooc_supervideo.widget.PullLoadRecyclerView;

/**
 * Created by hejunlin on 17/3/28.
 */

public class DetailListFragment extends BaseFragment {

    private static int mSiteId;
    private static int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private PullLoadRecyclerView mRecyclerView;
    private TextView mEmptyView;
    private int mColumns;
    private DetailListAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final int REFRESH_DURATION = 1500;
    private static final int LOADMORE_DURATION = 3000;

    public DetailListFragment() {
    }

    public static Fragment newInstance(int siteId, int channld) {
        DetailListFragment fragment = new DetailListFragment();
        mSiteId = siteId;
        mChannelId = channld;
        Bundle bundle = new Bundle();
        bundle.putInt(SITE_ID, siteId);
        bundle.putInt(CHANNEL_ID, channld);
        fragment.setArguments(bundle); //如:乐视,电视剧页面,fragment需要知道
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(); //第一次加载数据
        mAdapter = new DetailListAdapter();
        if (mSiteId == Site.LETV) { // 乐视下相关频道2列
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detailist;
    }

    @Override
    protected void initView() {
        mEmptyView = bindViewId(R.id.tv_empty);
        mEmptyView.setText(getActivity().getResources().getString(R.string.load_more_text));
        mRecyclerView = bindViewId(R.id.pullloadRecyclerView);
        mRecyclerView.setGridLayout(3);
        mRecyclerView.setAdapter(mAdapter); //
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());

    }

    private void reRreshData() {
        //TODO 请求接口,加载数据
    }

    private void loadData() {
        //TODO 请求接口,加载更多数据
    }

    class PullLoadMoreListener implements PullLoadRecyclerView.OnPullLoadMoreListener {

        @Override
        public void reRresh() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reRreshData();
                    mRecyclerView.setRefreshCompleted();
                }
            }, REFRESH_DURATION);
        }

        @Override
        public void loadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    mRecyclerView.setLoadMoreCompleted();
                }
            }, LOADMORE_DURATION);
        }
    }

    class DetailListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public void setColumns(int columns){
            //TODO
        }
    }
    @Override
    protected void initData() {

    }
}
