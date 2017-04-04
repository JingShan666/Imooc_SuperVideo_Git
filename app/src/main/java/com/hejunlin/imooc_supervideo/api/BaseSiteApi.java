package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.Channel;

/**
 * Created by hejunlin on 17/4/1.
 */

public abstract class BaseSiteApi {

    public abstract void onGetChannelAlbums(Channel channel, int pageNo, int pageSize, OnGetChannelAlbumListener listener);
}
