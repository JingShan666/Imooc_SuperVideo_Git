package com.hejunlin.imooc_supervideo;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.hejunlin.imooc_supervideo.base.BaseFragment;

/**
 * Created by hejunlin on 17/3/21.
 */

public class BlogFragment extends BaseFragment {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private static final  int MAX_VALUE= 100;
    private static final  String BLOG_URL = "http://m.blog.csdn.net/blog/index?username=hejjunlin";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blog;
    }

    @Override
    protected void initView() {
        mWebView = bindViewId(R.id.webview);
        mProgressBar = bindViewId(R.id.pb_progress);
        WebSettings settings = mWebView.getSettings();//用来设置webview的属性
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        mProgressBar.setMax(MAX_VALUE);
        mWebView.loadUrl(BLOG_URL);
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);//加载过程中更新进度
            if (newProgress == MAX_VALUE) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    protected void initData() {

    }
}
