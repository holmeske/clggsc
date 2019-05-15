package com.sc.clgg.activity.my

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.activity.login.LoginRegisterActivity
import com.sc.clgg.activity.my.set.AboutUsActivity
import com.sc.clgg.activity.my.set.FeedbackActivity
import com.sc.clgg.activity.my.set.ModifyPasswordActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.VersionInfoBean
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.UpdateHelper
import com.sc.clgg.util.makeDIAL
import kotlinx.android.synthetic.main.activity_set.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetActivity : BaseImmersionActivity() {
    private var http: Call<VersionInfoBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        initTitle("设置")

        tv_change_password.setOnClickListener { startActivity<ModifyPasswordActivity>() }
        tv_feedback.setOnClickListener { startActivity<FeedbackActivity>() }
        tv_custom_service.setOnClickListener {
            makeDIAL(ConstantValue.SERVICE_TEL)
        }
        tv_about.setOnClickListener { startActivity<AboutUsActivity>() }
        tv_check_apk_update.setOnClickListener {
            http = RetrofitHelper().versionInfo.apply {
                enqueue(object : Callback<VersionInfoBean> {
                    override fun onFailure(call: Call<VersionInfoBean>, t: Throwable) {
                        toast(R.string.network_anomaly)
                    }

                    override fun onResponse(call: Call<VersionInfoBean>, response: Response<VersionInfoBean>) {
                        response.body()?.let {
                            UpdateHelper().checkUpdateInfo(this@SetActivity, it.single?.code, it.single?.type!!, it.single?.url, true)
                        }
                    }
                })
            }
        }

        exitTxt.setOnClickListener {
            ExitDialog(this@SetActivity).show("温馨提示", "确定退出登录？") { _, _ ->
                ConfigUtil().clear()
                startActivity<MainActivity>()
                startActivity<LoginRegisterActivity>()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }
}
