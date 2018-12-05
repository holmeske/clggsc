package com.sc.clgg.activity.basic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.util.ConfigUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author lvke
 */
public class WebActivity extends BaseImmersionActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private WebSettings mWebSettings;

    public static void start(Context context, String title, String url) {
        if (null == url || url.isEmpty()) {
            return;
        }
        context.startActivity(new Intent(context, WebActivity.class).putExtra("name", title).putExtra("url", url));
    }

    public static void start(Context context, String title, String url, Boolean hideTitle) {
        if (null == url || url.isEmpty()) {
            return;
        }
        context.startActivity(new Intent(context, WebActivity.class).putExtra("name", title).putExtra("url", url).putExtra("hideTitle", hideTitle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        setTitle(getIntent().getStringExtra("name"));
        super.onCreate(savedInstanceState);

        LogHelper.e("name = " + getIntent().getStringExtra("name"));
        LogHelper.e("url = " + getIntent().getStringExtra("url"));

        mWebView = findViewById(R.id.wv_contents);
        mProgressBar = findViewById(R.id.progressbar);

        init();
        hideTitlebar();
    }

    private void hideTitlebar() {
        if (getIntent().getBooleanExtra("hideTitle", false)) {
            findViewById(R.id.titlebar).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.titlebar_left).setOnClickListener(v -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        });
    }

    private void init() {
        mWebSettings = mWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        mWebSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setAllowFileAccess(true); //设置可以访问文件
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebSettings.setBlockNetworkImage(false);//是否阻止网络数据
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setDomStorageEnabled(true);

        mWebView.addJavascriptInterface(new JsInterface(), "Android");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogHelper.e("WebView Url = " + url);
                if (url != null && url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(url)));
                        } else {
                            Toast.makeText(WebActivity.this, "电话权限被禁止，请打开设置-应用权限页面开通", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    mProgressBar.setVisibility(GONE);
                } else {
                    if (mProgressBar.getVisibility() == GONE) {
                        mProgressBar.setVisibility(VISIBLE);
                    }
                    mProgressBar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
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

        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
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


    private class JsInterface {
        @JavascriptInterface
        public String getInfo() {
            ConfigUtil conf = new ConfigUtil();
            return conf.getUserid() + "," + conf.getRealName() + "," + conf.getMobile();
        }
    }
}
