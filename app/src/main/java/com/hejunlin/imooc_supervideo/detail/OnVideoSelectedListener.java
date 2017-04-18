package com.hejunlin.imooc_supervideo.detail;

import com.hejunlin.imooc_supervideo.model.sohu.Video;

/**
 * Created by hejunlin on 17/4/15.
 */

public interface OnVideoSelectedListener {
    void onVideoSelected(Video video, int position);
}
