package com.sc.clgg.activity.etc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sc.clgg.R
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_pre_recharge_finish.*
import kotlinx.android.synthetic.main.view_titlebar.*

class PreRechargeFinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_recharge_finish)

        titlebar_title.text = "完成"

        intent.getParcelableExtra<StatusBean>("data")?.let {
            tv_success_money.text = "${it.money}"
            tv_order_number.text = "${it.RWasteSn}"
            tv_card_number.text = "${it.cardNo}"
            tv_car_number.text = "${it.carNo}"
            it.money?.toDouble()?.let {
                tv_success_money.text = "${it / 100}元"
                tv_can_write_balance.text = "${it / 100}元"
            }
        }
        tv_back_home.setOnClickListener { startActivity(ETCActivity::class.java) }
    }
}
