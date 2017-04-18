package com.hejunlin.imooc_supervideo.api;

import android.content.Context;
import android.util.Log;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.imooc_supervideo.model.Site;
import com.hejunlin.imooc_supervideo.model.sohu.Video;

/**
 * Created by hejunlin on 17/4/1.
 */

public class SiteApi {

    public static void onGetChannelAlbums(Context context, int pageNo, int pageSize, int siteId, int channelId, OnGetChannelAlbumListener listener) {
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetChannelAlbums(new Channel(channelId, context), pageNo, pageSize , listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetChannelAlbums(new Channel(channelId, context), pageNo, pageSize , listener);
                break;
        }
    }

    public static void onGetAlbumDetail(int siteId, Album album, OnGetAlbumDetailListener listener) {
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetAlbumDetail(album, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetAlbumDetail(album, listener);
                break;
        }
    }

    /**
     * 取video相关信息
     * @param siteId
     * @param album
     * @param listener
     */
    public static void onGetVideo(int siteId, int pageSize, int pageNo, Album album, OnGetVideoListener listener) {
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetVideo(album, pageSize, pageNo, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetVideo(album,  pageSize, pageNo, listener);
                break;
        }
    }

    public static void onGetVideoPlayUrl(int siteId, Video video, OnGetVideoPlayUrlListener listener) {
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetVideoPlayUrl(video,  listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetVideoPlayUrl(video,   listener);
                break;
        }
    }

}
