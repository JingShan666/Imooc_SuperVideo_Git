package com.hejunlin.imooc_supervideo.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.indircator.CoolIndicatorLayout;
import com.hejunlin.imooc_supervideo.indircator.IPagerIndicatorView;
import com.hejunlin.imooc_supervideo.indircator.IPagerTitle;
import com.hejunlin.imooc_supervideo.indircator.ViewPagerITitleView;
import com.hejunlin.imooc_supervideo.indircator.ViewPagerIndicatorAdapter;
import com.hejunlin.imooc_supervideo.indircator.ViewPagerIndicatorLayout;
import com.hejunlin.imooc_supervideo.indircator.ViewPagerWrapper;
import com.hejunlin.imooc_supervideo.indircator.ViewPaperIndicatorView;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.imooc_supervideo.model.Site;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DetailListActivity extends BaseActivity {

    private static final String CHANNEL_ID = "channid";
    private int mChannId;
    private ViewPager mViewPager;
    String [] mSiteNames = new String[] {"搜狐视频","乐视视频"};
    private List<String> mDataSet = Arrays.asList(mSiteNames);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            mChannId = intent.getIntExtra(CHANNEL_ID, 0);
        }
        Channel channel = new Channel(mChannId, this);
        String titleName = channel.getChannelName();

        setSupportActionBar();//表示当前页面支持actionbar
        setSupportArrowActionBar(true);
        setTitle(titleName);

        mViewPager = bindViewId(R.id.pager);
        CoolIndicatorLayout coolIndicatorLayout = bindViewId(R.id.viewpager_indicator);
        //组配indicator及title
        ViewPagerIndicatorLayout viewPagerIndicatorLayout = new ViewPagerIndicatorLayout(this);
        viewPagerIndicatorLayout.setAdapter(new ViewPagerIndicatorAdapter() {
            @Override
            public int getCount() {
                return mDataSet.size();
            }

            @Override
            public IPagerTitle getTitle(Context context, final int index) {
                ViewPagerITitleView viewPagerITitleView = new ViewPagerITitleView(context);
                viewPagerITitleView.setText(mDataSet.get(index));
                viewPagerITitleView.setNormalColor(Color.parseColor("#333333"));
                viewPagerITitleView.setSelectedColor(Color.parseColor("#e94220"));
                viewPagerITitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return viewPagerITitleView;
            }

            @Override
            public IPagerIndicatorView getIndicator(Context conext) {
                ViewPaperIndicatorView viewPaperIndicatorView = new ViewPaperIndicatorView(conext);
                viewPaperIndicatorView.setFillColor(Color.parseColor("#ebe4e3"));
                return viewPaperIndicatorView;
            }
        });

        coolIndicatorLayout.setPagerIndicatorLayout(viewPagerIndicatorLayout);
        ViewPagerWrapper.with(coolIndicatorLayout, mViewPager).compose();
        mViewPager.setAdapter(new SitePagerAdapter(getSupportFragmentManager(),this, mChannId));
    }

    //处理左上角返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    class SitePagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private int mChannelID;
        private HashMap<Integer,DetailListFragment> mPagerMap;

        public SitePagerAdapter(FragmentManager fm, Context context, int channelid) {
            super(fm);
            mContext = context;
            mChannelID = channelid;
            mPagerMap = new HashMap<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj =  super.instantiateItem(container, position);
            if (obj instanceof DetailListFragment) {
                mPagerMap.put(position, (DetailListFragment) obj);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPagerMap.remove(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = DetailListFragment.newInstance(position + 1, mChannelID);
            return fragment;
        }

        @Override
        public int getCount() {
            return Site.MAX_SITE;
        }

    }

    public static void launchDetailListActivity(Context context, int channelId) {
        Intent intent = new Intent(context, DetailListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHANNEL_ID, channelId);
        context.startActivity(intent);
    }
}
