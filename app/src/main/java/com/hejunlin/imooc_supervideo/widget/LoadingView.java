package com.hejunlin.imooc_supervideo.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.hejunlin.imooc_supervideo.R;

/**
 * Created by hejunlin on 17/4/15.
 */

public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_view_layout ,this);
    }
}
