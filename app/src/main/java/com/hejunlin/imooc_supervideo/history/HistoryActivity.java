package com.hejunlin.imooc_supervideo.history;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.hejunlin.imooc_supervideo.R;
import com.hejunlin.imooc_supervideo.base.BaseActivity;
import com.hejunlin.imooc_supervideo.common.CommonActivity;
import com.hejunlin.imooc_supervideo.model.Album;
import com.hejunlin.imooc_supervideo.model.AlbumList;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends CommonActivity {

    @Override
    public String getTitleText() {
        return getResources().getString(R.string.history_title);
    }

    @Override
    public String getEmptyText() {
        return getResources().getString(R.string.history_empty);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getTableName() {
        return "history";
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

}
