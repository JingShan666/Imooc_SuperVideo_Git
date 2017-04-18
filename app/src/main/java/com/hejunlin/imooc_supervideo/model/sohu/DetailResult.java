package com.hejunlin.imooc_supervideo.model.sohu;

import com.google.gson.annotations.Expose;

/**
 * Created by hejunlin on 17/4/4.
 */

/**
 * 搜狐数据频道数据返回集
 */
public class DetailResult {

    @Expose
    private long status;

    @Expose
    private String statusText;

    //for 详情页
    @Expose
    private ResultAlbum data;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public ResultAlbum getResultAlbum() {
        return data;
    }

    public void setResultAlbum(ResultAlbum resultAlbum) {
        this.data = resultAlbum;
    }
}
