package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.basic.MainActivity
import com.sc.clgg.activity.usersettings.AboutUsActivity
import com.sc.clgg.activity.usersettings.ModifyPasswordActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.VersionInfoBean
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.Tools
import com.sc.clgg.util.UpdateApkUtil
import kotlinx.android.synthetic.main.activity_set.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetActivity : BaseImmersionActivity() {
    private var http: Call<VersionInfoBean>? = null
    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        initTitle("设置")

        tv_change_password.setOnClickListener { startActivity(Intent(this@SetActivity, ModifyPasswordActivity::class.java)) }
        tv_feedback.setOnClickListener { startActivity(Intent(this@SetActivity, FeedbackActivity::class.java)) }
        tv_custom_service.setOnClickListener { Tools.callPhone(ConstantValue.SERVICE_TEL, this@SetActivity) }
        tv_about.setOnClickListener { startActivity(Intent(this@SetActivity, AboutUsActivity::class.java)) }
        tv_check_apk_update.setOnClickListener {
            http = RetrofitHelper().versionInfo.apply {
                enqueue(object : Callback<VersionInfoBean> {
                    override fun onFailure(call: Call<VersionInfoBean>, t: Throwable) {
                        toast(R.string.network_anomaly)
                    }

                    override fun onResponse(call: Call<VersionInfoBean>, response: Response<VersionInfoBean>) {
                        response.body()?.let {
                            UpdateApkUtil().checkUpdateInfo(this@SetActivity, it.single?.code, it.single?.type!!, it.single?.url, true)
                        }
                    }
                })
            }
        }

        exitTxt.setOnClickListener {
            ExitDialog(this@SetActivity).show("温馨提示", "确定退出登录？") { _, _ ->
                ConfigUtil().clear()
                startActivity(Intent(this@SetActivity, MainActivity::class.java))
                startActivity(Intent(this@SetActivity, LoginRegisterActivity::class.java))
                finish()
            }
        }
    }
}
