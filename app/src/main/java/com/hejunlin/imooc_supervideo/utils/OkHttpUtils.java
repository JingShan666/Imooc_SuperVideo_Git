package com.hejunlin.imooc_supervideo.utils;

import com.hejunlin.imooc_supervideo.AppManager;

import okhttp3.Callback;
import okhttp3.Request;

/**
 * Created by hejunlin on 17/4/4.
 */

public class OkHttpUtils {

    private static final String REQUEST_TAG = "okhttp";

    public static Request buildRuquest(String url) {
        if (AppManager.isNetWorkAvailable()) {
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
            return request;
        }
        return null;
    }

    public static void excute(String url, Callback callback) {
        Request request = buildRuquest(url);
        excute(request,callback);
    }

    public static void excute(Request request, Callback callback) {
        AppManager.getHttpClient().newCall(request).enqueue(callback);
    }
}
