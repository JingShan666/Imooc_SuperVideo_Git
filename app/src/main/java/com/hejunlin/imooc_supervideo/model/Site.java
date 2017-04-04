package com.hejunlin.imooc_supervideo.model;

import android.content.Context;

import com.hejunlin.imooc_supervideo.R;

/**
 * Created by hejunlin on 17/3/28.
 */

public class Site {

    public static final int LETV = 1;
    public static final int SOHU = 2;

    private int siteId;
    private String siteName;
    private Context mContext;
    public static final int MAX_SITE = 2;

    public Site(int id, Context context) {
        siteId = id;
        mContext = context;
        switch (siteId) {
            case SOHU:
                siteName = mContext.getResources().getString(R.string.site_sohu);
                break;
            case LETV:
                siteName = mContext.getResources().getString(R.string.site_letv);
                break;
        }
    }

    public int getSiteId() {
        return siteId;
    }

}
