package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.model.sohu.VideoList;

/**
 * Created by hejunlin on 17/4/1.
 */

public interface OnGetVideoPlayUrlListener {

    void onGetSuperUrl(Video video, String url);//超清url

    void onGetNoramlUrl(Video video, String url);//标清url

    void onGetHighUrl(Video video, String url);//高清url

    void onGetFailed(ErrorInfo info);

}
