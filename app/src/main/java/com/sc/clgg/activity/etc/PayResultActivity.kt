package com.sc.clgg.activity.etc

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_pay_result.*

class PayResultActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_result)

        tv_recharge_des?.text=intent.getStringExtra("msg")?:""
        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
    }
}
