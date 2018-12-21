package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CardList
import com.sc.clgg.dialog.PreRechargeHintDialog
import com.sc.clgg.dialog.RechargeDialog
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.logcat
import kotlinx.android.synthetic.main.activity_pre_recharge.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreRechargeActivity : BaseImmersionActivity() {

    /**
     * 可圈存余额
     */
    private var canCircleMoney: Double = -1.0
    private lateinit var card: CardList.Card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_recharge)
        //card = intent.getParcelableExtra("card")
        //if (card==null){
            card=  CardList.Card("3", "沪00001", "37011801220102886198")
       // }
        logcat(card)
        getCardInfo(card.cardId)
        init()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun init() {
        titlebar_title.text = getString(R.string.pre_recharge)

        when (card.cardType) {
            "2" -> {
                tv_card_type.text = "鲁通A卡"
            }
            "3" -> {
                tv_card_type.text = "鲁通B卡"
            }
            else -> {
                tv_card_type.text = ""
            }
        }
        tv_card.text = card.cardId
        tv_carno.text = card.vlp

        initPreRechargeListener()
    }

    private fun getCardInfo(cardNo: String?) {
        showProgressDialog(false)
        RetrofitHelper().getCardInfo(cardNo, "0").apply {
            enqueue(object : Callback<CardInfo> {
                override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                    hideProgressDialog()
                    toast(R.string.network_anomaly)
                }

                override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                    hideProgressDialog()
                    response.body()?.let {
                        if (it.success) {
                            canCircleMoney = it.RQcMoney!!.toDouble()
                            RAdjust=it.RAdjust!!.toInt()
                            if (canCircleMoney > 0.0) {
                                v_1.setBackgroundResource(R.drawable.bg_gray_5)
                            } else if (canCircleMoney == 0.0) {
                                v_1.setBackgroundResource(R.drawable.bg_blue_5)
                            } else {
                                v_1.setBackgroundResource(R.drawable.bg_blue_5)
                            }
                            tv_can_write_amount.text = "${it.RQcMoney}元"
                        } else {
                            toast("${it.msg}")
                        }
                    }
                }
            })
        }
    }

      private var RAdjust = 1
    private fun initPreRechargeListener() {
        v_1.setOnClickListener {

            if (canCircleMoney > 0.0) {
                /*PreRechargeHintDialog(this@PreRechargeActivity).apply {
                    show()
                    setData(canCircleMoney)
                    setCancelListener { dismiss() }
                }*/
                startActivity(Intent(this,BleActivity::class.java)
                        .putExtra("RQcMoney",canCircleMoney.toInt())
                        .putExtra("RAdjust",RAdjust))
            } else if (canCircleMoney == 0.0) {

                RechargeDialog(this).apply {
                    setCardNumber(card.cardId)
                }.show()

            }

        }
    }

}
