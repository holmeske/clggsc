package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.startActivity
import etc.obu.data.CardInformation
import kotlinx.android.synthetic.main.activity_balance_query.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceQueryActivity : BaseImmersionActivity() {
    private var card: CardInformation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_query)

        init()
    }

    private fun init() {
        card = intent.getParcelableExtra("card")

        val type = intent.getStringExtra("type")
        if ("add".equals(type)) {
            titlebar_title.text = "确认卡片信息"
            tv_bind_card.visibility = View.VISIBLE
            tv_bind_card.setOnClickListener {
                RetrofitHelper().bindingCard(card?.cardId, card?.vehicleNumber).enqueue(object : Callback<StatusBean> {
                    override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                        startActivity(Intent(this@BalanceQueryActivity, BindCardResultActivity::class.java).putExtra("result", 0))
                    }

                    override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                        response.body()?.let {
                            if (it.success) {
                                startActivity(Intent(this@BalanceQueryActivity, BindCardResultActivity::class.java).putExtra("result", 1))
                            } else {
                                toast("${it.msg}")
                            }
                        }
                    }
                })

            }
        } else {
            titlebar_title.text = "查询页面"
            tv_back_home.visibility = View.VISIBLE
            tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
        }

        tv_card_no.text = card?.cardId
        tv_balance.text = String.format("%.2f", card?.balance?.toDouble()!! / 100)

        RetrofitHelper().getCardInfo(card?.cardId, "0").enqueue(object : Callback<CardInfo> {
            override fun onFailure(call: Call<CardInfo>, t: Throwable) {
            }

            override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                response.body()?.let {
                    tv_can_circle_balance.text = String.format("%.2f", it.RQcMoney?.toDouble()!! / 100)
                    tv_card_id.text = it.RVLP
                }
            }
        })
    }

}
