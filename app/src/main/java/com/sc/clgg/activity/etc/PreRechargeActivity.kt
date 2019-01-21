package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CardList
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.PreRechargeHintDialog
import com.sc.clgg.dialog.RechargeDialog
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.logcat
import kotlinx.android.synthetic.main.activity_pre_recharge.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PreRechargeActivity : BaseImmersionActivity() {

    /**
     * 可圈存余额
     */
    private var canCircleMoney: Double = -1.0
    private var card: CardList.Card? = null
    private var money: String = "0"
    private var wasteSn: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_recharge)
        card = intent.getParcelableExtra("card")
        //card?.cardId="37011801220102886198"
        logcat(card)
        getCardInfo(card?.cardId)
        init()
    }

    private fun init() {
        titlebar_title.text = getString(R.string.pre_recharge)

        tv_card.text = card?.cardId
        tv_carno.text = card?.vlp

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
                            canCircleMoney = it.RQcMoney!!.toDouble() / 100
                            RAdjust = it.RAdjust!!.toInt()
                            when {
                                canCircleMoney > 0.0 -> v_1.setBackgroundResource(R.drawable.bg_gray_5)
                                canCircleMoney == 0.0 -> v_1.setBackgroundResource(R.drawable.bg_blue_5)
                                else -> v_1.setBackgroundResource(R.drawable.bg_blue_5)
                            }
                            tv_can_write_amount.text = "${String.format("%.2f", canCircleMoney)}元"
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
                PreRechargeHintDialog(this).apply {
                    show()
                    setData(canCircleMoney)
                    setCancelListener { dismiss() }
                }
            } else if (canCircleMoney == 0.0) {
                RechargeDialog(this).apply { setCardNumber(card?.cardId);setItemClickListener { money = it };setWasteSnListener { wasteSn = it } }.show()
            }

        }
        titlebar_right.text=getString(R.string.in_card)
        titlebar_right.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onMessageEvent(event: MessageEvent) {
        //EventBus.getDefault().removeStickyEvent(event)
        if (event.value == 4) {
            LogHelper.e("充值确认")
            confirmPayStatus()
            /*RetrofitHelper().surePayMoney(WeChatPayCache.cardNo, WeChatPayCache.money).enqueue(object : Callback<StatusBean> {
                override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                    hideProgressDialog()
                    response.body()?.let {
                        if (it.success) {
                            toast("支付成功")
                            startActivity(Intent(this@PreRechargeActivity, PreRechargeFinishActivity::class.java)
                                    .putExtra("data", response.body()))
                        } else {
                            toast("${it.msg}")
                        }
                    }
                }

                override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                    hideProgressDialog()
                    toast(R.string.network_anomaly)
                }
            })*/
        }
    }

    private var handler = Handler()
    private var runnable: Runnable = Runnable { confirmPayStatus() }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun confirmPayStatus() {
        showProgressDialog("支付确认中...", false)
        RetrofitHelper().confirmPayStatus(card?.cardId).enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                response.body()?.let {
                    if (it.success) {
                        hideProgressDialog()
                        toast("支付成功")
                        LogHelper.e("查询卡信息")
                        startActivity(Intent(this@PreRechargeActivity, PreRechargeFinishActivity::class.java)
                                .putExtra("card", card)
                                .putExtra("money", money).putExtra("wasteSn",wasteSn))
                    } else {
                        if (it.payStatus != 2) {
                            hideProgressDialog()
                            toast("${it.msg}")
                        } else {
                            handler.postDelayed(runnable, 3000)
                        }
                    }
                }
            }
        })
    }
}
