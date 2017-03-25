package com.hejunlin.imooc_supervideo;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.base.BaseFragment;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;

/**
 * Created by hejunlin on 17/3/21.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private GridView mGridView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LoopViewPager viewPager = bindViewId(R.id.looperviewpager);
        CircleIndicator indicator = bindViewId(R.id.indicator);
        viewPager.setAdapter(new HomePicAdapter(getActivity()));
        viewPager.setLooperPic(true);//5s自动轮播
        indicator.setViewPager(viewPager); //indicator需要知道viewpager
        mGridView = bindViewId(R.id.gv_channel);
        mGridView.setAdapter(new ChannelAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, ">> onItemClick " + position);
                switch (position) {
                    case 6:
                        //TODO 跳转直播
                        break;
                    case 7:
                        //TODO  跳转收藏
                        break;
                    case 8:
                        //TODO  跳转历史记录
                        break;
                    default:
                        //TODO  跳转对应频道
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    class ChannelAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Channel.MAX_COUNT;
        }

        @Override
        public Channel getItem(int position) {
            return new Channel(position + 1, getActivity());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Channel chanel = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.home_grid_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_home_item_text);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_home_item_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(chanel.getChannelName());
            int id = chanel.getChannelId();
            int imgResId = -1;
            switch (id) {
                case Channel.SHOW:
                    imgResId = R.drawable.ic_show;
                    break;
                case Channel.MOVIE:
                    imgResId = R.drawable.ic_movie;
                    break;
                case Channel.COMIC:
                    imgResId = R.drawable.ic_comic;
                    break;
                case Channel.DOCUMENTRY:
                    imgResId = R.drawable.ic_movie;
                    break;
                case Channel.MUSIC:
                    imgResId = R.drawable.ic_music;
                    break;
                case Channel.VARIETY:
                    imgResId = R.drawable.ic_variety;
                    break;
                case Channel.LIVE:
                    imgResId = R.drawable.ic_live;
                    break;
                case Channel.FAVORITE:
                    imgResId = R.drawable.ic_bookmark;
                    break;
                case Channel.HISTORY:
                    imgResId = R.drawable.ic_history;
                    break;
            }

            holder.imageView.setImageDrawable(getActivity().getResources().getDrawable(imgResId));

            return convertView;
        }
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

}
