package com.hejunlin.imooc_supervideo.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hejunlin on 17/4/3.
 */

public class AlbumList extends ArrayList<Album>{

    private static final String TAG = AlbumList.class.getSimpleName();

    public void debug () {
        for (Album a : this) {
            Log.d(TAG, ">> albumlist " + a.toString());
        }
    }

}
