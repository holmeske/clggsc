package com.sc.clgg.activity.fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.sc.clgg.R
import com.sc.clgg.config.NetField
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.ConfigUtil
import kotlinx.android.synthetic.main.fragment_mall.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class MallFragment : Fragment() {
    private var historyUrl: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogHelper.e("onViewCreated() --->" + this.javaClass.simpleName)
        super.onViewCreated(view, savedInstanceState)

        init("${NetField.SITE}shop/index")

        v_reload?.onClick { webView.loadUrl(historyUrl) }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            LogHelper.e("onResume() --->商城")
        }
    }

    private var mWebSettings: WebSettings? = null
    private fun init(url: String) {
        mWebSettings = webView.settings

        //设置自适应屏幕，两者合用
        mWebSettings?.useWideViewPort = true //将图片调整到适合webview的大小
        mWebSettings?.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        mWebSettings?.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        mWebSettings?.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings?.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        mWebSettings?.cacheMode = WebSettings.LOAD_DEFAULT
        mWebSettings?.allowFileAccess = true //设置可以访问文件
        mWebSettings?.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        mWebSettings?.loadsImagesAutomatically = true //支持自动加载图片
        mWebSettings?.defaultTextEncodingName = "utf-8"//设置编码格式
        mWebSettings?.javaScriptEnabled = true
        webView?.addJavascriptInterface(JavaScriptinterface(activity!!), "Android")
        mWebSettings?.textZoom = 100

        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url != null && url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    var intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                view.loadUrl(url)
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                LogHelper.e("onReceivedError   ")
                v_reload?.visibility = View.VISIBLE
                toast("检查网络后重试")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                historyUrl = url
                LogHelper.e("onPageFinished   " + historyUrl)

            }

        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, i: Int) {
                super.onProgressChanged(webView, i)

                super.onProgressChanged(webView, i)
            }

        }

        webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            LogHelper.i("tag", "url=$url")

            LogHelper.i("tag", "userAgent=$userAgent")

            LogHelper.i("tag", "contentDisposition=$contentDisposition")

            LogHelper.i("tag", "mimetype=$mimetype")

            LogHelper.i("tag", "contentLength=$contentLength")

            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        })
    }

    inner class JavaScriptinterface(internal var context: Context) {

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        fun getInfo(): String {
            "${ConfigUtil().userid},${ConfigUtil().realName},${ConfigUtil().mobile}".let {
                LogHelper.e(it)
                return it
            }
        }
    }
}
