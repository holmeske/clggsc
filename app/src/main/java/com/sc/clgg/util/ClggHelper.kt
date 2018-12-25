package com.sc.clgg.util

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.sc.clgg.activity.WebActivity
import com.sc.clgg.tool.helper.LogHelper
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader

/**
 * @author：lvke
 * @date：2018/10/15 14:48
 */
private class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        /*注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！*/
        Glide.with(context).load(path).apply(RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView)
    }
}

fun Banner?.setData(context: Context? = null, data: ArrayList<com.sc.clgg.bean.Banner.Bean>? = null, hideTitle: Boolean) {
    this?.setImages(
            ArrayList<String>().apply {
                if (data != null) {
                    for (bean: com.sc.clgg.bean.Banner.Bean? in data) {
                        bean?.img?.let { add(it) }
                    }
                }
            }
    )?.setDelayTime(5000)?.setImageLoader(GlideImageLoader())?.setOnBannerListener { position ->

        data!![position].takeIf { !(it.name.isNullOrEmpty() || it.url.isNullOrEmpty()) }?.apply {
            context!!.startActivity(Intent(context, WebActivity::class.java).putExtra("name", name)
                    .putExtra("url", url).putExtra("hideTitle", hideTitle))
        }

    }?.start()
}

/**
 * 快捷 打印日志
 */
fun logcat(o: Any?) {
    LogHelper.e(Gson().toJson(o))
}
