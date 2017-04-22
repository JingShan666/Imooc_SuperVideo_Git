package com.hejunlin.imooc_supervideo.api;

import android.text.TextUtils;
import android.util.Log;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;
import com.hejunlin.imooc_supervideo.model.Channel;
import com.hejunlin.imooc_supervideo.model.ErrorInfo;
import com.hejunlin.imooc_supervideo.model.Site;
import com.hejunlin.imooc_supervideo.model.sohu.Video;
import com.hejunlin.imooc_supervideo.model.sohu.VideoList;
import com.hejunlin.imooc_supervideo.utils.MD5;
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
    private static final int BITSTREAM_SUPER = 100;
    private static final int BITSTREAM_NORMAL = 101;
    private static final int BITSTREAM_HIGH = 102;
    //http://static.meizi.app.m.letv.com/android/mod/mob/ctl/listalbum/act/index/src/1/cg/2/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/1/ps/30/pcode/010110263/version/5.6.2.mindex.html
    private final static String ALBUM_LIST_URL_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_DOCUMENTARY_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/3/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_SHOW_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    //http://static.meizi.app.m.letv.com/android/mod/mob/ctl/album/act/detail/id/10026309/pcode/010410000/version/2.1.mindex.html
    private final static String ALBUM_DESC_URL_FORMAT = "http://static.meizi.app.m.letv.com/" +
            "android/mod/mob/ctl/album/act/detail/id/%s/pcode/010410000/version/2.1.mindex.html";
    //key : bh65OzqYYYmHRQ
    private final static String SEVER_TIME_URL = "http://dynamic.meizi.app.m.letv.com/android/dynamic.php?mod=mob&ctl=timestamp&act=timestamp&pcode=010410000&version=5.4";

    //http://static.app.m.letv.com/android/mod/mob/ctl/videolist/act/detail/id/10026309/vid/0/b/1/s/30/o/-1/m/1/pcode/010410000/version/2.1.mindex.html
    private final static String ALBUM_VIDEOS_URL_FORMAT = "http://static.app.m.letv.com/" +
            "android/mod/mob/ctl/videolist/act/detail/id/%s/vid/0/b/%s/s/%s/o/%s/m/%s/pcode/010410000/version/2.1.mindex.html";

    //arg: mmsid currentServerTime key vid
    private final static String VIDEO_FILE_URL_FORMAT = "http://dynamic.meizi.app.m.letv.com/android/dynamic.php?mmsid=" +
            "%s&playid=0&tss=ios&pcode=010410000&version=2.1&tm=%s&key=%s&vid=" +
            "%s&ctl=videofile&mod=minfo&act=index";

    private final static String VIDEO_REAL_LINK_APPENDIX = "&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone";

    //http://play.g3proxy.lecloud.com/vod/v2/MjYwLzkvNTIvbGV0di11dHMvMTQvdmVyXzAwXzIyLTEwOTczMjQ5NzUtYXZjLTE5OTY1OS1hYWMtNDgwMDAtMjU4NjI0MC04Mzk3NjQ4OC04MmQxMGVlM2I3ZTdkMGU5ZjE4YzM1NDViMWI4MzI4Yi0xNDkyNDA2MDE2MTg4Lm1wNA==?b=259&mmsid=64244666&tm=1492847915&key=22f2f114ed643e0d08596659e5834cd6&platid=3&splatid=347&playid=0&tss=ios&vtype=21&cvid=711590995389&payff=0&pip=83611a86979ddb3df8ef0fb41034f39c&format=1&sign=mb&dname=mobile&expect=3&p1=0&p2=00&p3=003&tag=mobile&pid=10031263&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone

    private Long mTimeOffSet = Long.MAX_VALUE;

    public LetvApi() {
        fetchServerTime();
    }

    private void fetchServerTime() {
        if (mTimeOffSet != Long.MAX_VALUE) {
            return;
        }
        OkHttpUtils.excute(SEVER_TIME_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, ">> onFailure !!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, ">> onResponse failed!!");
                    return;
                }
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String time = jsonObject.optString("time");
                    updateServerTime(Long.parseLong(time));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateServerTime(Long time) {
        if (mTimeOffSet == Long.MAX_VALUE) {
            mTimeOffSet = System.currentTimeMillis()/1000 - time;
        }
    }

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


    /**
     * 补全详情页数据
     * @param album
     * @param listener
     */
    public void onGetAlbumDetail(final Album album, final OnGetAlbumDetailListener listener) {
        //TODO
        final String url = String.format(ALBUM_DESC_URL_FORMAT, album.getAlbumId());
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetAlbumDetail", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetAlbumDetailFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetAlbumDetail", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetAlbumDetailFailed(info);
                    return;
                }
                // 补全album信息
                String result = response.body().string();
                try {
                    JSONObject albumJson = new JSONObject(result);
                    if (albumJson.optJSONObject("body") != null) {
                        JSONObject albumJsonBody = albumJson.optJSONObject("body");
                        if (albumJsonBody.optJSONObject("picCollections") != null){
                            JSONObject jsonImg =  albumJsonBody.optJSONObject("picCollections");
                            if (!TextUtils.isEmpty(jsonImg.optString("150*200"))) {
                                album.setHorImgUrl(StringEscapeUtils.unescapeJava(jsonImg.optString("150*200")));
                            }
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("description"))) {
                            album.setAlbumDesc(albumJsonBody.optString("description"));
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("nowEpisodes"))) {
                            album.setVideoTotal(Integer.parseInt(albumJsonBody.optString("nowEpisodes")));
                        }
                        //directory starring
                        if (!TextUtils.isEmpty(albumJsonBody.optString("directory"))) {
                            album.setDirector(albumJsonBody.optString("directory"));
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("starring"))) {
                            album.setMainActor(albumJsonBody.optString("starring"));
                        }
                        if (listener != null) {
                            listener.onGetAlbumDetailSuccess(album);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //取video相关信息
    public void onGetVideo(final Album album, int pageSize, int pageNo, final OnGetVideoListener listener) {
        final String url = String.format(ALBUM_VIDEOS_URL_FORMAT, album.getAlbumId(), pageNo, pageSize, "-1", "1");
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideo", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.OnGetVideoFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideo", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.OnGetVideoFailed(info);
                    return;
                }
                String result = response.body().string();
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.optJSONObject("body") != null) {
                        JSONObject bodyJson = resultJson.optJSONObject("body");
                        JSONArray jsonArray = bodyJson.optJSONArray("videoInfo");
                        if (jsonArray != null) {
                            if (jsonArray.length() > 0) {
                                VideoList videoList = new VideoList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Video video = new Video();
                                    //videoInfo表示每个视频info
                                    JSONObject videoInfo = jsonArray.getJSONObject(i);
                                    video.setAid(Long.parseLong(album.getAlbumId()));
                                    video.setSite(album.getSite().getSiteId());
                                    //nameCn: "择天记03"
                                    if (!TextUtils.isEmpty(videoInfo.optString("nameCn"))) {
                                        video.setVideoName(videoInfo.optString("nameCn"));
                                    }
                                    //mid: "64271196" 表示解释乐视视频源需要的
                                    if (!TextUtils.isEmpty(videoInfo.optString("mid"))) {
                                        video.setMid(Long.parseLong(videoInfo.optString("mid")));
                                    }
                                    if (!TextUtils.isEmpty(videoInfo.optString("id"))) {
                                        video.setVid(Long.parseLong(videoInfo.optString("id")));
                                    }
                                    videoList.add(video);
                                }
                                if (videoList.size() > 0 && listener != null) {
                                    listener.OnGetVideoSuccess(videoList);
                                } else {
                                    ErrorInfo info  = buildErrorInfo(url, "onGetVideo", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                                    listener.OnGetVideoFailed(info);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //取视频播放url
    public void onGetVideoPlayUrl(final Video video, final OnGetVideoPlayUrlListener listener) {
        //args : mid, servertime, key, vid
        final String url = String.format(VIDEO_FILE_URL_FORMAT, video.getMid(), getCurrentServerTime(), getKey(video, getCurrentServerTime()), video.getVid());
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideoPlayUrl", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideoPlayUrl", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetFailed(info);
                    return;
                }
                String result = response.body().string();
                try {
                    JSONObject resultJson = new JSONObject(result);
                    JSONObject infosJson = resultJson.getJSONObject("body").getJSONObject("videofile").getJSONObject("infos");
                    if (infosJson != null) {
                        JSONObject normalInfoObject = infosJson.getJSONObject("mp4_350");
                        if (normalInfoObject != null) {
                            String normalUrl = "";
                            if (!TextUtils.isEmpty(normalInfoObject.optString("mainUrl"))) {
                                normalUrl = normalInfoObject.optString("mainUrl");
                                normalUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(normalInfoObject.optString("backUrl1"))) {
                                normalUrl = normalInfoObject.optString("backUrl1");
                                normalUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(normalInfoObject.optString("backUrl2"))) {
                                normalUrl = normalInfoObject.optString("backUrl2");
                                normalUrl += VIDEO_REAL_LINK_APPENDIX;
                            }
                            getRealUrl(video, normalUrl, BITSTREAM_NORMAL, listener);
                        }
                        JSONObject highInfoObject = infosJson.getJSONObject("mp4_1000");
                        if (highInfoObject != null) {
                            String highUrl = "";
                            if (!TextUtils.isEmpty(highInfoObject.optString("mainUrl"))) {
                                highUrl = highInfoObject.optString("mainUrl");
                                highUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(highInfoObject.optString("backUrl1"))) {
                                highUrl = highInfoObject.optString("backUrl1");
                                highUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(highInfoObject.optString("backUrl2"))) {
                                highUrl = highInfoObject.optString("backUrl2");
                                highUrl += VIDEO_REAL_LINK_APPENDIX;
                            }
                            getRealUrl(video, highUrl, BITSTREAM_HIGH, listener);
                        }
                        JSONObject superfoObject = infosJson.getJSONObject("mp4_1300");
                        if (superfoObject != null) {
                            String superUrl = "";
                            if (!TextUtils.isEmpty(superfoObject.optString("mainUrl"))) {
                                superUrl = superfoObject.optString("mainUrl");
                                superUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(superfoObject.optString("backUrl1"))) {
                                superUrl = superfoObject.optString("backUrl1");
                                superUrl += VIDEO_REAL_LINK_APPENDIX;
                            } else if (!TextUtils.isEmpty(highInfoObject.optString("backUrl2"))) {
                                superUrl = superfoObject.optString("backUrl2");
                                superUrl += VIDEO_REAL_LINK_APPENDIX;
                            }
                            getRealUrl(video, superUrl, BITSTREAM_SUPER, listener);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //http://play.g3proxy.lecloud.com/vod/v2/MjYwLzkvNTIvbGV0di11dHMvMTQvdmVyXzAwXzIyLTEwOTczMjQ5NzUtYXZjLTE5OTY1OS1hYWMtNDgwMDAtMjU4NjI0MC04Mzk3NjQ4OC04MmQxMGVlM2I3ZTdkMGU5ZjE4YzM1NDViMWI4MzI4Yi0xNDkyNDA2MDE2MTg4Lm1wNA==?b=259&mmsid=64244666&tm=1492847915&key=22f2f114ed643e0d08596659e5834cd6&platid=3&splatid=347&playid=0&tss=ios&vtype=21&cvid=711590995389&payff=0&pip=83611a86979ddb3df8ef0fb41034f39c&format=1&sign=mb&dname=mobile&expect=3&p1=0&p2=00&p3=003&tag=mobile&pid=10031263&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone
    //解析以上url返回的location字段,即为真实url
    private void getRealUrl(final Video video, String normalUrl, final int type, final OnGetVideoPlayUrlListener listener) {
        OkHttpUtils.excute(normalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Nothing
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String realUrl = jsonObject.optString("location");
                    if (type == BITSTREAM_NORMAL) {
                        video.setNormalUrl(realUrl);
                        if (listener != null) {
                            listener.onGetNoramlUrl(video, realUrl);
                        }
                    }
                    if (type == BITSTREAM_HIGH) {
                        video.setHighUrl(realUrl);
                        if (listener != null) {
                            listener.onGetHighUrl(video, realUrl);
                        }
                    }
                    if (type == BITSTREAM_SUPER) {
                        video.setHighUrl(realUrl);
                        if (listener != null) {
                            listener.onGetSuperUrl(video, realUrl);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getKey(Video video, String serverTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(video.getMid());
        sb.append(",");
        sb.append(serverTime);
        sb.append(",");
        sb.append("bh65OzqYYYmHRQ");
        return MD5.toMd5(sb.toString());
    }

    private String getCurrentServerTime() {
        if (mTimeOffSet != Long.MAX_VALUE) {
            return String.valueOf(System.currentTimeMillis()/1000 - mTimeOffSet);
        } else {
            return null;
        }
    }
}
