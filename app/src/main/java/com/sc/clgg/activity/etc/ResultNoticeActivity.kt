package com.sc.clgg.activity.etc

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_result_notice.*
import kotlinx.android.synthetic.main.view_titlebar.*

class ResultNoticeActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_notice)

        titlebar_title.text = "结果通知"

        tv_recharge_result?.text = intent.getStringExtra("title") ?: "支付失败"
        tv_recharge_des?.text = intent.getStringExtra("msg") ?: ""

        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
    }
}
