package com.hejunlin.imooc_supervideo.model.sohu;

import android.util.Log;

import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;

import java.util.ArrayList;

/**
 * Created by hejunlin on 17/4/15.
 */

public class VideoList extends ArrayList<Video> {
    private static final String TAG = VideoList.class.getSimpleName();

    public void debug () {
        for (Video a : this) {
            Log.d(TAG, ">> videList " + a.toString());
        }
    }
}
