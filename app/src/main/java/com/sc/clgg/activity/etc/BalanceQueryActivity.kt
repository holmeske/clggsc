package com.sc.clgg.activity.etc

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.startActivity
import etc.obu.data.CardInformation
import kotlinx.android.synthetic.main.activity_balance_query.*
import kotlinx.android.synthetic.main.view_titlebar.*
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
        titlebar_title.text = "查询页面"

        tv_card_no.text = card?.cardId
        tv_balance.text = String.format("%.2f", card?.balance?.toDouble()!!/100)

        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }

        RetrofitHelper().getCardInfo(card?.cardId, "0").enqueue(object : Callback<CardInfo> {
            override fun onFailure(call: Call<CardInfo>, t: Throwable) {
            }

            override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                response.body()?.let {
                    tv_can_circle_balance.text = String.format("%.2f", it.RQcMoney?.toDouble()!!/100)
                    tv_card_id.text = it.RVLP
                }
            }
        })
    }

}
