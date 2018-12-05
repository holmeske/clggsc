package com.sc.clgg.activity.etc

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_recharge_success.*
import kotlinx.android.synthetic.main.view_read_card.*
import kotlinx.android.synthetic.main.view_titlebar.*

class RechargeSuccessActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_success)

        titlebar_title.text = "成功"
        iv_nav.setImageResource(R.drawable.pay_success_nav_step_icon)

//        tv_back_home.setOnClickListener { startActivity(Intent(this@RechargeSuccessActivity,MainActivity::class.java).putExtra("activity","recharge")) }
        tv_back_home.setOnClickListener {startActivity(MainActivity::class.java)}
    }
}
