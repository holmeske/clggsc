package com.sc.clgg.util

import android.content.Context
import android.content.Intent
import android.support.annotation.DrawableRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sc.clgg.R
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun EditText.setTextChangeListener(body: (key: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            body(s.toString())
        }
    })
}

fun Context.startActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun Context.statusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    LogHelper.e("状态栏高 = ${result}")
    return result
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun formatDataStr(str: String, oldPattern: String, newPattern: String): String {
    //yyyyMMddHHmmss yyyyMMddHHmmss
    val format1 = SimpleDateFormat(oldPattern, Locale.getDefault())

    try {
        val date = format1.parse(str)
        return SimpleDateFormat(newPattern, Locale.getDefault()).format(date)
    } catch (e: ParseException) {
        return ""
    }
}

fun ImageView.setDefaultRoundedCornerPicture(context: Context, @DrawableRes resourceId: Int) {
    Glide.with(context).load(resourceId).apply(
            RequestOptions.bitmapTransform(RoundedCorners(MeasureHelper.dp2px(context.applicationContext, 5f)))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .override(if (measuredWidth == 0) 100 else measuredWidth, if (measuredHeight == 0) 100 else measuredHeight)
    ).into(this)
}

fun ImageView.setRoundedCornerPicture(context: Context, url: String? = "") {
    //if (!TextUtils.isEmpty(url)) {
    //LogHelper.e("ImageView 宽高：" + measuredWidth + "," + measuredHeight)
    Glide.with(context).load(url).apply(
            RequestOptions.bitmapTransform(RoundedCorners(MeasureHelper.dp2px(context.applicationContext, 5f)))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .override(if (measuredWidth == 0) 100 else measuredWidth, if (measuredHeight == 0) 100 else measuredHeight)
    )
            /*.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            LogHelper.e("onLoadFailed")
            return false
        }

        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
            LogHelper.e("onResourceReady")
            return false
        }
    })*/
            .into(this)
    //}
}

fun ImageView.setPicture(context: Context, url: String? = "") {
    Glide.with(context).load(url).apply(
            RequestOptions().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher)
    ).into(this)
}







