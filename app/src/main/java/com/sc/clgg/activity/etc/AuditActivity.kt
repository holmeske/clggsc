package com.sc.clgg.activity.etc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_audit.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.startActivity

class AuditActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit)

        titlebar_left.visibility = View.GONE
        init()
    }

    private fun init() {
        titlebar_title.text = "审核中"

        val hint = getString(R.string.audit_hint)

        val spannableString = SpannableString(hint)

        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color._4285f4)), 22, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(MyClick(this@AuditActivity), 22, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        tv_2.text = spannableString
        tv_2.movementMethod = LinkMovementMethod.getInstance()

        tv_back_home.setOnClickListener { startActivity<MainActivity>() }

    }

    internal inner class MyClick(private val mContext: Context) : ClickableSpan() {

        override fun onClick(@NonNull widget: View) {
            mContext.startActivity(Intent(mContext, ApplyStateNewActivity::class.java))
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(mContext, R.color._4285f4)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
