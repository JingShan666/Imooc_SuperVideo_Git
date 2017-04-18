package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.sohu.VideoList;

/**
 * Created by hejunlin on 17/4/1.
 */

public interface OnGetVideoListener {
    void OnGetVideoSuccess(VideoList videoList);
    void OnGetVideoFailed(ErrorInfo info);
}
