package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;

/**
 * Created by hejunlin on 17/4/1.
 */

public interface OnGetAlbumDetailListener {
    void onGetAlbumDetailSuccess(Album album);
    void onGetAlbumDetailFailed(ErrorInfo info);
}
