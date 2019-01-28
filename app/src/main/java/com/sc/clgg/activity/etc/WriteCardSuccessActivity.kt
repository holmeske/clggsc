package com.sc.clgg.activity.etc

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
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

        titlebar_left.visibility = View.GONE
        iv_nav.setImageResource(R.drawable.pay_success_nav_step_icon)

        if (intent.getIntExtra("failure", -1) == 1) {
            titlebar_title.text = "充值成功 未写卡"
            tv_suc_str.text = "可圈存金额:"

            tv_success_money.text = "${intent.getIntExtra("qc", 0).toDouble() / 100}元"
            tv_can_circle.text = "${intent.getIntExtra("qc", 0).toDouble() / 100}元"

            intent.getParcelableExtra<CircleSave>("data")?.let {

                tv_order_number.text = it.RWasteSn

                tv_car_number.text = it.carNo

                tv_card_number.text = it.cardNo
            }
        } else {
            titlebar_title.text = "圈存成功"
            tv_suc_str.text = "成功圈存:"

            tv_can_circle.text = "0.00元"

            intent.getParcelableExtra<CircleSave>("data")?.let {

                it.realMoney?.toDouble()?.let { tv_success_money.text = "${it / 100}元" }

                tv_order_number.text = it.RWasteSn

                tv_car_number.text = it.carNo

                tv_card_number.text = it.cardNo
            }
        }

        tv_card_balance.text = "${intent.getIntExtra("balance", 0).toDouble() / 100}元"

        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(EtcActivity::class.java)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
