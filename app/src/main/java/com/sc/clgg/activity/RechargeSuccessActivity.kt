package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.view_read_card.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*

class RechargeSuccessActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_success)

        titlebar_title.text = "成功"
        iv_nav.setImageResource(R.drawable.pay_success_nav_step_icon)
    }
}
