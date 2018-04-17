package com.sc.clgg.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import org.jetbrains.anko.windowManager
import tool.helper.MeasureUtils


/**
 * @author：lvke
 * @date：2018/2/26 16:35
 */

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

fun Activity.start(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun Context.creatProgressBar(): ProgressBar {
    val progressBar = ProgressBar(this)
    val layoutParams = WindowManager.LayoutParams(MeasureUtils.dp2px(this.applicationContext, 30f),
            MeasureUtils.dp2px(this.applicationContext, 30f), 0, 0, PixelFormat.TRANSPARENT)
    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
    windowManager?.addView(progressBar, layoutParams)

    progressBar.setFocusableInTouchMode(true);
    progressBar.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                windowManager.removeView(progressBar)
            }
            return true
        }
    })
    return progressBar
}

fun Context.removeProgressBar(view: View?) {
    windowManager.removeViewImmediate(view)
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}




