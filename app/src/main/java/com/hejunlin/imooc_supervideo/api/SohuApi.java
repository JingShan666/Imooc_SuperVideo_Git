package com.hejunlin.imooc_supervideo.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hejunlin.imooc_supervideo.AppManager;
import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.Site;
import com.hejunlin.imooc_supervideo.model.sohu.Result;
import com.hejunlin.imooc_supervideo.model.sohu.ResultAlbum;
import com.hejunlin.imooc_supervideo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hejunlin on 17/4/1.
 */

public class SohuApi extends BaseSiteApi {

    private static final String TAG = SohuApi.class.getSimpleName();
    private static final int SOHU_CHANNELID_MOVIE = 1; //搜狐电影频道ID
    private static final int SOHU_CHANNELID_SERIES = 2; //搜狐电视剧频道ID
    private static final int SOHU_CHANNELID_VARIETY = 7; //搜狐综艺频道ID
    private static final int SOHU_CHANNELID_DOCUMENTRY = 8; //搜狐纪录片频道ID
    private static final int SOHU_CHANNELID_COMIC = 16; //搜狐动漫频道ID
    private static final int SOHU_CHANNELID_MUSIC = 24; //搜狐音乐频道ID

    //http://api.tv.sohu.com/v4/search/channel.json?cid=2&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47&page=1&page_size=1
    private final static String API_CHANNEL_ALBUM_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=6.2.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";

    @Override
    public void onGetChannelAlbums(Channel channel, int pageNo, int pageSize, OnGetChannelAlbumListener listener) {
        String url = getChannelAlbumUrl(channel, pageNo, pageSize);
        doGetChannelAlbumsByUrl(url, listener);
    }

    public void doGetChannelAlbumsByUrl(final String url, final OnGetChannelAlbumListener listener) {
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 if (listener != null) {
                     ErrorInfo info  = buildErrorInfo(url, "doGetChannelAlbumsByUrl", e, ErrorInfo.ERROR_TYPE_URL);
                     listener.onGetChannelAlbumFailed(info);
                 }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if (!response.isSuccessful()) {
                     ErrorInfo info  = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_HTTP);
                     listener.onGetChannelAlbumFailed(info);
                     return;
                 }
                 // 1、取到数据映射Result
                 // 2、转换ResultAlbum变成Album
                 // 3、Album存到AlbumLis中
                Result result = AppManager.getGson().fromJson(response.body().string(), Result.class);
                AlbumList albumList = toConvertAlbumList(result);
                if (albumList != null) {
                    if (albumList.size() > 0 && listener != null) {
                        listener.onGetChannelAlbumSuccess(albumList);
                    }
                } else {
                    ErrorInfo info  = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                    listener.onGetChannelAlbumFailed(info);
                }

            }
        });
    }

    private AlbumList toConvertAlbumList(Result result) {
        if (result.getData().getResultAlbumList().size() > 0) { //说明有数据
            AlbumList albumList = new AlbumList();
            for (ResultAlbum resultAlbum : result.getData().getResultAlbumList()) {
                Album album  = new Album(Site.SOHU, AppManager.getContext());
                album.setAlbumDesc(resultAlbum.getTvDesc());
                album.setAlbumId(resultAlbum.getAlbumId());
                album.setHorImgUrl(resultAlbum.getHorHighPic());
                album.setMainActor(resultAlbum.getMainActor());
                album.setTip(resultAlbum.getTip());
                album.setTitle(resultAlbum.getAlbumName());
                album.setVerImgUrl(resultAlbum.getVerHighPic());
                album.setDirector(resultAlbum.getDirector());
                albumList.add(album);
            }
            return albumList;
        }

        return  null;
    }

    private ErrorInfo buildErrorInfo(String url, String functionName, Exception e, int type) {
        ErrorInfo info  = new ErrorInfo(Site.SOHU, type);
        info.setExceptionString(e.getMessage());
        info.setFunctionName(functionName);
        info.setUrl(url);
        info.setTag(TAG);
        info.setClassName(TAG);
        return info;
    }

    private String getChannelAlbumUrl(Channel channel, int pageNo, int pageSize) {
        //格式化url
        return String.format(API_CHANNEL_ALBUM_FORMAT, toConvertChannelId(channel),pageNo, pageSize);
    }

    //自定义频道ID与真实频道id转换
    private int toConvertChannelId(Channel channel) {
        int channelId = -1;//-1 无效值
        switch (channel.getChannelId()) {
            case Channel.SHOW:
                channelId = SOHU_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = SOHU_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = SOHU_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = SOHU_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTRY:
                channelId = SOHU_CHANNELID_DOCUMENTRY;
                break;
            case Channel.VARIETY:
                channelId = SOHU_CHANNELID_VARIETY;
                break;
        }
        return channelId;
    }



}
