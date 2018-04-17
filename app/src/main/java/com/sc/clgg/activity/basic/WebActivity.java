package com.sc.clgg.activity.basic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sc.clgg.R;
import tool.helper.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WebActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.wv_contents)
    WebView mWebView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        setTitle(getIntent().getStringExtra("name"));
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        mWebSettings = mWebView.getSettings();

        //设置自适应屏幕，两者合用
        mWebSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setAllowFileAccess(true); //设置可以访问文件
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.loadUrl(getIntent().getStringExtra("url"));
//        mWebView.loadUrl("http://app.speiyou.com/");https://hao.360.cn/

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    mProgressBar.setVisibility(GONE);
                } else {
                    if (mProgressBar.getVisibility() == GONE)
                        mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogHelper.i("tag", "url=" + url);

                LogHelper.i("tag", "userAgent=" + userAgent);

                LogHelper.i("tag", "contentDisposition=" + contentDisposition);

                LogHelper.i("tag", "mimetype=" + mimetype);

                LogHelper.i("tag", "contentLength=" + contentLength);

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
        if (mUnbinder != null) mUnbinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        //  return true;
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

}
