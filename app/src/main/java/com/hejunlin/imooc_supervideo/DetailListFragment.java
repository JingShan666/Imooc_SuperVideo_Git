package com.hejunlin.imooc_supervideo;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hejunlin.imooc_supervideo.base.BaseFragment;

/**
 * Created by hejunlin on 17/3/28.
 */

public class DetailListFragment extends BaseFragment {

    private static int mSiteId;
    private static int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";

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
    protected int getLayoutId() {
        return R.layout.fragment_detailist;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
