package com.sc.clgg.activity.fragment


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.config.NetField
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.statusBarHeight
import kotlinx.android.synthetic.main.fragment_mall.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast

class MallFragment : Fragment() {
    private var historyUrl: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mall, container, false)
    }

    fun canGoBack(): Boolean {
        return webView.canGoBack()
    }

    fun goBack() {
        webView?.goBack()
    }


    private var viewIsCreated: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewIsCreated = true

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            titlebar_top.visibility = View.GONE
        } else {
            titlebar_top.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, activity!!.statusBarHeight())
        }
        titlebar_left.visibility = View.GONE
        titlebar_left.setOnClickListener {
            if (webView.canGoBack()) webView?.goBack()
        }
        titlebar_title.text = "商城"

        init("${NetField.SITE}shop/index")

        v_reload?.setOnClickListener { webView.loadUrl(historyUrl) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (viewIsCreated && isVisibleToUser) {
            LogHelper.e("商城页面 setUserVisibleHint")

            webView.resumeTimers()
            webView.onResume()
        }
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

        mWebSettings?.javaScriptEnabled = true
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

        mWebSettings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        mWebSettings?.blockNetworkImage = false//是否阻止网络数据
        mWebSettings?.setSupportMultipleWindows(true)
        mWebSettings?.domStorageEnabled = true

        webView?.addJavascriptInterface(JsInterface(), "Android")
        //mWebSettings?.textZoom = 100

        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    var intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                view.loadUrl(url)
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                LogHelper.e("onReceivedError()")
                v_reload?.visibility = View.VISIBLE

                activity?.let {
                    if ((it as MainActivity).currenMainTabIndex == 2) {
                        it.toast("检查网络后重试")
                    }
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                LogHelper.e("onPageStarted  $url")
                historyUrl = url
                if ("${NetField.SITE}shop/index" != url) {
                    titlebar_left.visibility = View.VISIBLE
                } else {
                    titlebar_left.visibility = View.GONE
                }
            }

        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, i: Int) {
                super.onProgressChanged(webView, i)

                super.onProgressChanged(webView, i)
            }

        }

    }

    inner class JsInterface {

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
