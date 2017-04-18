package com.hejunlin.imooc_supervideo.model.sohu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejunlin on 17/4/15.
 */

/**
 * VideoResult 表示详情页面返回Video信息,二级结构,包含Video
 */
public class VideoData {
    @Expose
    private int count;

    @Expose
    private List<Video> videos = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Video> getVideoList() {
        return videos;
    }

    public void setVideoList(List<Video> videoList) {
        this.videos = videoList;
    }
}
