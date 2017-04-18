package com.hejunlin.imooc_supervideo.api;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.Site;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.utils.OkHttpUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hejunlin on 17/4/1.
 */

public class LetvApi extends BaseSiteApi {

    private static final String TAG = LetvApi.class.getSimpleName();
    private static final int LETV_CHANNELID_MOVIE = 1; //乐视电影频道ID
    private static final int LETV_CHANNELID_SERIES = 2; //乐视电视剧频道ID
    private static final int LETV_CHANNELID_VARIETY = 11; //乐视综艺频道ID
    private static final int LETV_CHANNELID_DOCUMENTRY = 16; //乐视纪录片频道ID
    private static final int LETV_CHANNELID_COMIC = 5; //乐视动漫频道ID
    private static final int LETV_CHANNELID_MUSIC = 9; //乐视音乐频道ID

    //http://static.meizi.app.m.letv.com/android/mod/mob/ctl/listalbum/act/index/src/1/cg/2/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/1/ps/30/pcode/010110263/version/5.6.2.mindex.html
    private final static String ALBUM_LIST_URL_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_DOCUMENTARY_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/3/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_SHOW_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    @Override
    public void onGetChannelAlbums(Channel channel, int pageNo, int pageSize, OnGetChannelAlbumListener listener) {
        String url =  getChannelAlbumUrl(channel,pageNo, pageSize);
        doGetChannelAlbumsByUrl(url, listener);
    }

    private String getChannelAlbumUrl(Channel channel, int pageNo, int pageSize) {
        if (channel.getChannelId() == Channel.DOCUMENTRY) {
            return String.format(ALBUM_LIST_URL_DOCUMENTARY_FORMAT, conVertChannleId(channel), pageNo, pageSize);
        } else if (channel.getChannelId() == Channel.DOCUMENTRY) {
            return String.format(ALBUM_LIST_URL_SHOW_FORMAT, conVertChannleId(channel), pageNo, pageSize);
        }
        return String.format(ALBUM_LIST_URL_FORMAT, conVertChannleId(channel), pageNo, pageSize);
    }

    private int conVertChannleId(Channel channel) {
        int channelId = -1;//-1 无效值
        switch (channel.getChannelId()) {
            case Channel.SHOW:
                channelId = LETV_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = LETV_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = LETV_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = LETV_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTRY:
                channelId = LETV_CHANNELID_DOCUMENTRY;
                break;
            case Channel.VARIETY:
                channelId = LETV_CHANNELID_VARIETY;
                break;
        }
        return channelId;
    }

    private void doGetChannelAlbumsByUrl(final String url, final OnGetChannelAlbumListener listener) {
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
                String json = response.body().string();
                try {
                    JSONObject resultJson = new JSONObject(json);
                    JSONObject bodyJson = resultJson.optJSONObject("body");
                    if (bodyJson.optInt("album_count") > 0) {
                        AlbumList list = new AlbumList();
                        JSONArray albumListJosn = bodyJson.optJSONArray("album_list");
                        for (int i = 0; i< albumListJosn.length(); i++) {
                            Album album = new Album(Site.LETV);
                            JSONObject albumJson = albumListJosn.getJSONObject(i);
                            album.setAlbumId(albumJson.getString("aid"));
                            album.setAlbumDesc(albumJson.getString("subname"));
                            album.setTitle(albumJson.getString("name"));
                            album.setTip(albumJson.getString("subname"));
                            JSONObject jsonImage = albumJson.getJSONObject("images");
                            //读取【400*300】字符
                            String imageurl = StringEscapeUtils.unescapeJava(jsonImage.getString("400*300"));
                            album.setHorImgUrl(imageurl);
                            list.add(album);
                        }
                        if (list != null) {
                            if (list.size() > 0 && listener != null) {
                                listener.onGetChannelAlbumSuccess(list);
                            }
                        } else {
                            ErrorInfo info  = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                            listener.onGetChannelAlbumFailed(info);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ErrorInfo info  = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_PARSE_JSON);
                    listener.onGetChannelAlbumFailed(info);
                }
            }
        });
    }

    private ErrorInfo buildErrorInfo(String url, String functionName, Exception e, int type) {
        ErrorInfo info  = new ErrorInfo(Site.LETV, type);
        info.setExceptionString(e.getMessage());
        info.setFunctionName(functionName);
        info.setUrl(url);
        info.setTag(TAG);
        info.setClassName(TAG);
        return info;
    }


    public void onGetAlbumDetail(Album album, OnGetAlbumDetailListener listener) {
        //TODO
    }

    public void onGetVideo(Album album, int pageSize, int pageNo, OnGetVideoListener listener) {
        //TODO
    }


    //取视频播放url
    public void onGetVideoPlayUrl(Video video, OnGetVideoPlayUrlListener listener) {
        //TODO
    }
}
