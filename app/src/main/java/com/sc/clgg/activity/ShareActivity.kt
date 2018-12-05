package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sc.clgg.R
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.UmengHelper
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_share.*
import org.jetbrains.anko.toast


class ShareActivity : AppCompatActivity() {
    private var title: String? = ""
    private var content: String? = ""
    private var url: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        url = intent.getStringExtra("url")

        LogHelper.e("分享url  = $url")
        LogHelper.e("分享地址  = $content")
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this)?.onActivityResult(requestCode, resultCode, data)
    }

    private fun init() {
        v?.setOnClickListener { finish() }
        tv_wechat?.setOnClickListener {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.WEIXIN,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_circle?.setOnClickListener {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.WEIXIN_CIRCLE,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_qq?.setOnClickListener {
            UmengHelper().share(this@ShareActivity, SHARE_MEDIA.QQ,
                    url,
                    title,
                    content,
                    mUMShareListener)
        }

        tv_cancel?.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UmengHelper().destroy(this)
    }

    private var mUMShareListener: UMShareListener = object : UMShareListener {
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

}
