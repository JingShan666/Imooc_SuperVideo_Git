package com.hejunlin.imooc_supervideo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hejunlin on 17/3/25.
 */

public class HomePicAdapter extends PagerAdapter {
    private Context mContext;

    private int[] mDes = new int[] {
            R.string.a_name,
            R.string.b_name,
            R.string.c_name,
            R.string.d_name,
            R.string.e_name,
    };

    private int[] mImg = new int[] {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
    };

    public HomePicAdapter(Activity activity) {
        mContext = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.home_pic_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_dec);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
        textView.setText(mDes[position]);
        imageView.setImageResource(mImg[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
