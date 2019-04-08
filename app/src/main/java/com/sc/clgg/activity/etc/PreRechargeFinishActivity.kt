package com.sc.clgg.activity.etc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CardList
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_pre_recharge_finish.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreRechargeFinishActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_recharge_finish)
        titlebar_left.visibility = View.GONE
        titlebar_title.text = "完成"

        intent.getParcelableExtra<CardList.Card>("data")?.let {
            tv_card_number.text = "${it.cardNo}"
            tv_car_number.text = "${it.carNo}"

            showProgressDialog()
            RetrofitHelper().getCardInfo(it.cardNo, "0").apply {
                enqueue(object : Callback<CardInfo> {
                    override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                        hideProgressDialog()
                        toast(R.string.network_anomaly)
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                        hideProgressDialog()
                        response.body()?.let {
                            it.RQcMoney?.toDouble()?.let {
                                tv_can_write_balance.text = "${String.format("%.2f", it / 100)}元"
                            }
                        }
                    }
                })
            }
        }
        intent.getStringExtra("money")?.toDouble()?.let {
            tv_success_money.text = "${String.format("%.2f", it / 100)}元"
        }
        intent.getStringExtra("wasteSn")?.let {
            tv_order_number.text = "${it}"
        }

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
