package com.hejunlin.imooc_supervideo.model.sohu;

/**
 * Created by hejunlin on 17/4/4.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 真正搜狐频道数据结构
 */
public class ResultAlbum {

    //@SerializedName
    //表示将对象的属性跟json里的字段对应映射。
    //解决定义属性和json字段不一样的问题

    @SerializedName("album_desc")
    @Expose
    private String tvDesc; // 专辑描述

    @Expose
    private String director;//导演

    @SerializedName("hor_high_pic")
    @Expose
    private String horHighPic;//横图url

    @SerializedName("ver_high_pic")
    @Expose
    private String verHighPic;//竖图url

    @SerializedName("main_actor")
    @Expose
    private String mainActor;//主演

    @SerializedName("album_name")
    @Expose
    private String albumName;//专辑名字

    @Expose
    private String tip;//更新到xxx集、更新到xxx期

    @SerializedName("aid")
    @Expose
    private String albumId;//专辑id

    @SerializedName("latest_video_count")
    @Expose
    private int lastVideoCount;//专辑最新更新的集数

    @SerializedName("total_video_count")
    @Expose
    private int totalVideoCount;//专辑总集数

    public String getTvDesc() {
        return tvDesc;
    }

    public void setTvDesc(String tvDesc) {
        this.tvDesc = tvDesc;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getHorHighPic() {
        return horHighPic;
    }

    public void setHorHighPic(String horHighPic) {
        this.horHighPic = horHighPic;
    }

    public String getVerHighPic() {
        return verHighPic;
    }

    public void setVerHighPic(String verHighPic) {
        this.verHighPic = verHighPic;
    }

    public String getMainActor() {
        return mainActor;
    }

    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }

    public int getLastVideoCount() {
        return lastVideoCount;
    }

    public void setLastVideoCount(int lastVideoCount) {
        this.lastVideoCount = lastVideoCount;
    }

    public int getTotalVideoCount() {
        return totalVideoCount;
    }

    public void setTotalVideoCount(int totalVideoCount) {
        this.totalVideoCount = totalVideoCount;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
