package com.hejunlin.imooc_supervideo.model.sohu;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hejunlin.imooc_supervideo.model.Site;

/**
 * Created by hejunlin on 17/4/15.
 */

public class Video implements Parcelable {

    @Expose
    private Long vid; //视频id

    @Expose
    @SerializedName("video_name")
    private String videoName;//视频名字

    @Expose
    @SerializedName("hor_high_pic")
    private String horHighPic;//视频横图

    @Expose
    @SerializedName("ver_high_pic")
    private String verHighPic;//视频竖图

    @Expose
    private String title;

    @Expose
    private int site;

    @Expose
    private long aid; //专辑id

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getSuperUrl() {
        return superUrl;
    }

    public void setSuperUrl(String superUrl) {
        this.superUrl = superUrl;
    }

    public String getNormalUrl() {
        return normalUrl;
    }

    public void setNormalUrl(String normalUrl) {
        this.normalUrl = normalUrl;
    }

    public String getHighUrl() {
        return highUrl;
    }

    public void setHighUrl(String highUrl) {
        this.highUrl = highUrl;
    }

    private String superUrl;
    private String normalUrl;
    private String highUrl;
    private long mid;

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "Video{" +
                "vid=" + vid +
                ", videoName='" + videoName + '\'' +
                ", horHighPic='" + horHighPic + '\'' +
                ", verHighPic='" + verHighPic + '\'' +
                ", title='" + title + '\'' +
                ", site=" + site +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Video () {
    }

    private Video(Parcel in) {
        this.aid = in.readLong();
        this.vid = in.readLong();
        this.title = in.readString();
        this.videoName = in.readString();
        this.horHighPic = in.readString();
        this.verHighPic = in.readString();
        this.superUrl = in.readString();
        this.normalUrl = in.readString();
        this.highUrl = in.readString();
        this.site = in.readInt();
    }
    //　parcel对象读写序列要对应

    @Override
    public void writeToParcel(Parcel parcel, int in) {
        parcel.writeLong(aid);
        parcel.writeLong(vid);
        parcel.writeString(title);
        parcel.writeString(videoName);
        parcel.writeString(horHighPic);
        parcel.writeString(verHighPic);
        parcel.writeString(superUrl);
        parcel.writeString(normalUrl);
        parcel.writeString(highUrl);
        parcel.writeInt(site);
    }
}
