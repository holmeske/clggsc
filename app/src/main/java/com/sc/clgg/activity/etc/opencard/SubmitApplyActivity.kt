package com.sc.clgg.activity.etc.opencard

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
import com.sc.clgg.activity.WebActivity
import com.sc.clgg.activity.etc.AuditActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Check
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_submit_apply.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitApplyActivity : BaseImmersionActivity() {
    private var certificationInfo: CertificationInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_apply)

        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("开卡信息 = ${Gson().toJson(certificationInfo)}")

        certificationInfo?.certType?.let {
            fun updateCertType(string: String) {
                certificationInfo?.certType = string
            }
            when (it) {
                "身份证含临时身份证" -> updateCertType("101")
                "护照" -> updateCertType("102")
                "港澳居民来往内地通行证" -> updateCertType("103")
                "台湾居民来往大陆通行证" -> updateCertType("104")
                "统一社会信用代码证书" -> updateCertType("201")
                "组织机构代码证" -> updateCertType("202")
                "营业执照" -> updateCertType("203")
                else -> {
                }
            }
        }
        LogHelper.e("证件类型修改后的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }

    private fun init() {
        titlebar_title.text = "提交申请"
        certificationProgressView.setProress(4)

        val hint = getString(R.string.submint_hint)

        val spannableString = SpannableString(hint)

        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color._4285f4)), hint.length - 4, hint.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(MyClick(this@SubmitApplyActivity), hint.length - 4, hint.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_hint.text = spannableString
        tv_hint.movementMethod = LinkMovementMethod.getInstance()

        tv_submit_card.setOnClickListener {
            if (!checkBox.isChecked) {
                toast("请勾选用户协议")
                return@setOnClickListener
            }
            showProgressDialog()
            http = RetrofitHelper().apply(certificationInfo).apply {
                enqueue(object : Callback<Check> {
                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                        hideProgressDialog()
                        response.body()?.let {
                            if (it.success) {
                                startActivity(AuditActivity::class.java)
                            } else {
                                toast("${it.msg}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Check>, t: Throwable) {
                        hideProgressDialog()
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
            WebActivity.start(this@SubmitApplyActivity, "用户协议", ConstantValue.USER_AGREEMENT)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(mContext, R.color._4285f4)
        }
    }

}
