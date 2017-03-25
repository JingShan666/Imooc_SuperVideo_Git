package com.hejunlin.imooc_supervideo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hejunlin.imooc_supervideo.R;

/**
 * Created by hejunlin on 17/3/19.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){
        return (T) findViewById(resId);
    }

    protected void setSupportActionBar(){
        mToolBar = bindViewId(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    protected void setActionBarIcon(int resId){
        if (mToolBar != null){
            mToolBar.setNavigationIcon(resId);
        }
    }


}
