package com.hejunlin.imooc_supervideo.live;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.player.PlayActivity;
import com.hejunlin.imooc_supervideo.R;

/**
 * Created by hejunlin on 17/4/30.
 */

public class LiveItemAdapter extends RecyclerView.Adapter<LiveItemAdapter.ViewHolder> {

    private Context mContext;
    // 数据集
    private String[] mDataList = new String[] {
            "CCTV-1 综合","CCTV-2 财经","CCTV-3 综艺","CCTV-4 中文国际(亚)","CCTV-5 体育",
            "CCTV-6 电影","CCTV-7 军事农业","CCTV-8 电视剧", "CCTV-9 纪录","CCTV-10 科教",
            "CCTV-11 戏曲","CCTV-12 社会与法","CCTV-13 新闻","CCTV-14 少儿","CCTV-15 音乐",
            "湖南卫视","北京卫视","天津卫视","湖北卫视","东方卫视",
    };

    private int[] mIconList = new int[] {
            R.drawable.cctv_1, R.drawable.cctv_2, R.drawable.cctv_3, R.drawable.cctv_4, R.drawable.cctv_5,
            R.drawable.cctv_6, R.drawable.cctv_7, R.drawable.cctv_8, R.drawable.cctv_9, R.drawable.cctv_10,
            R.drawable.cctv_11, R.drawable.cctv_12, R.drawable.cctv_13, R.drawable.cctv_14, R.drawable.cctv_15,
            R.drawable.hunan_tv,R.drawable.beijing_tv,R.drawable.tianjing_tv,R.drawable.hubei_tv,R.drawable.dongfang_tv,
    };

    private String [] mUrlList = new String[]{
            "http://220.248.175.231:6610/001/2/ch00000090990000001022/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001014/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001023/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001015/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001016/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001017/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001018/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001019/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001020/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001021/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001027/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001028/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001029/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001030/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001031/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001053/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001077/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001069/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.230:6610/001/2/ch00000090990000001047/index.m3u8?virtualDomain=001.live_hls.zte.com",
            "http://220.248.175.231:6610/001/2/ch00000090990000001081/index.m3u8?virtualDomain=001.live_hls.zte.com",
    };

    public LiveItemAdapter(Context context) {
        mContext = context;
    }
    // view 相关
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    // 数据相关
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mIcon.setImageResource(mIconList[position]);
        holder.mTitle.setText(mDataList[position]);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.launch((Activity)mContext, mUrlList[position], mDataList[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIcon;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_live_icon);
            mTitle = (TextView) itemView.findViewById(R.id.tv_live_title);
        }
    }

}
