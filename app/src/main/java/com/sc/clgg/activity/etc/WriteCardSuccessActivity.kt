package com.sc.clgg.activity.etc

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CircleSave
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_write_card_success.*
import kotlinx.android.synthetic.main.view_read_card.*
import kotlinx.android.synthetic.main.view_titlebar.*

class WriteCardSuccessActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_card_success)

        titlebar_title.text = "圈存成功"
        iv_nav.setImageResource(R.drawable.pay_success_nav_step_icon)
        intent.getParcelableExtra<CircleSave>("data")?.let {
            it.realMoney?.toDouble()?.let { tv_success_money.text = "${it / 100}元" }
            tv_order_number.text = it.RWasteSn

            it.money?.let {
                tv_success_money.text = "${it.toDouble() / 100}元"
            }
        }
        tv_card_number.text = intent.getStringExtra("cardNo")
        tv_car_number.text = intent.getStringExtra("carNo")

        tv_card_balance.text = "${intent.getIntExtra("balance", 0).toDouble() / 100}元"

        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
    }
}
