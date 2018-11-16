package com.sc.clgg.activity

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Check
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_submit_application.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitApplicationActivity : BaseImmersionActivity() {
    private var certificationInfo: CertificationInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_application)

        certificationProgressView.setProress(4)
        certificationInfo = intent.getParcelableExtra("info")
        init()
    }

    private fun init() {
        titlebar_title.text = "提交申请"
        certificationProgressView.setProress(4)

        var hint = getString(R.string.submint_hint)

        var spannableString = SpannableString(hint)

        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color._4285f4)), hint.length - 4, hint.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(MyClick(this@SubmitApplicationActivity), hint.length - 4, hint.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_hint.setText(spannableString)
        tv_hint.movementMethod = LinkMovementMethod.getInstance()

        tv_submit_card.setOnClickListener {
            LogHelper.e("认证信息 = ${Gson().toJson(certificationInfo)}")
            if (!checkBox.isChecked) {
                toast("请勾选用户协议")
                return@setOnClickListener
            }
            http = RetrofitHelper().apply(certificationInfo).apply {
                enqueue(object : Callback<Check> {
                    override fun onResponse(call: retrofit2.Call<Check>, response: Response<Check>) {
                        response.body()?.let {
                            if (it.success) {
                                startActivity(AuditActivity::class.java)
                            } else {
                                toast("${it.msg}")
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<Check>, t: Throwable) {
                        toast(R.string.network_anomaly)
                    }
                })
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }

    private var http: Call<Check>? = null

    internal inner class MyClick(private val mContext: Context) : ClickableSpan() {

        override fun onClick(@NonNull widget: View) {
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(mContext, R.color._4285f4)
        }
    }
}
