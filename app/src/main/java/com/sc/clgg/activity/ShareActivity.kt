package com.sc.clgg.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.UmengHelper
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_share.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast


class ShareActivity : Activity() {
    private var title: String? = ""
    private var content: String? = ""
    private var url: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        url = intent.getStringExtra("url")

        LogHelper.e("分享url  = " + url)
        LogHelper.e("分享地址  = " + content)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun init() {
        v.onClick { finish() }
        tv_wechat.onClick {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.WEIXIN,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_circle.onClick {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.WEIXIN_CIRCLE,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_qq.onClick {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.QQ,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_cancel.onClick {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UmengHelper().destroy(this)
    }

    internal var mUMShareListener: UMShareListener = object : UMShareListener {
        override fun onStart(share_media: SHARE_MEDIA) {
            LogHelper.e("onStart")
        }

        override fun onResult(share_media: SHARE_MEDIA) {
            LogHelper.e("onResult")
            toast("分享成功")
        }


        override fun onError(share_media: SHARE_MEDIA, throwable: Throwable) {
            LogHelper.e("onError" + throwable.toString())
            toast("分享失败")
        }

        override fun onCancel(share_media: SHARE_MEDIA) {
            LogHelper.e("onCancel")
            toast("分享取消")
        }
    }


    private fun share(activity: Activity, var1: SHARE_MEDIA, url: String, title: String, description: String, listener: UMShareListener) {
        UmengHelper().share(activity, var1,
                url,
                title,
                "abcd....................",
                object : UMShareListener {
                    override fun onResult(p0: SHARE_MEDIA?) {
                        LogHelper.e("onResult")
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                        LogHelper.e("onCancel")
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        LogHelper.e("onError" + p1.toString())
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                        LogHelper.e("onStart")
                    }
                })
    }

}
