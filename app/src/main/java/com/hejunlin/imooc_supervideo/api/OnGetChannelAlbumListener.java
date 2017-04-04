package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.AlbumList;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;

/**
 * Created by hejunlin on 17/4/1.
 */

public interface OnGetChannelAlbumListener {
    void onGetChannelAlbumSuccess(AlbumList albumList);
    void onGetChannelAlbumFailed(ErrorInfo info);
}
