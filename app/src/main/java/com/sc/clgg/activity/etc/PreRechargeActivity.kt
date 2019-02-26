package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CardList
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.bean.WxPayEvent
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

        titlebar_title.text = getString(R.string.pre_recharge)
        titlebar_right.text = getString(R.string.in_card)

        tv_card.text = card?.cardId
        tv_carno.text = card?.vlp

        initListener()
    }

    private fun resultNotice(title: String? = "", msg: String? = "") {
        startActivity(Intent(this@PreRechargeActivity, ResultNoticeActivity::class.java).putExtra("title", title).putExtra("msg", msg))
    }

    private var http_2: Call<CardInfo>? = null
    private fun getCardInfo(cardNo: String?) {
        showProgressDialog(false)
        http_2 = RetrofitHelper().getCardInfo(cardNo, "0").apply {
            enqueue(object : Callback<CardInfo> {
                override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                    hideProgressDialog()
                    resultNotice("支付异常", "应答错误：卡状态查询失败！联系方式：400-888-1122")
                }

                override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                    hideProgressDialog()
                    response.body()?.let {
                        if (it.success) {

                            it.RQcMoney?.toDouble()?.run { canCircleMoney = this / 100 }

                            when {
                                canCircleMoney > 0.0 -> v_1.setBackgroundResource(R.drawable.bg_gray_5)
                                canCircleMoney == 0.0 -> v_1.setBackgroundResource(R.drawable.bg_blue_5)
                                else -> v_1.setBackgroundResource(R.drawable.bg_blue_5)
                            }
                            tv_can_write_amount.text = "${String.format("%.2f", canCircleMoney)}元"
                        } else {
                            resultNotice("支付异常", "${it.msg}")
                        }
                    }
                }
            })
        }
    }

    private fun initListener() {
        v_1.setOnClickListener {

            if (canCircleMoney > 0) {
                PreRechargeHintDialog(this).apply {
                    show()
                    setData(canCircleMoney)
                    setCancelListener { dismiss() }
                }
            } else if (canCircleMoney == 0.0) {
                RechargeDialog(this).apply { setCardNumber(card?.cardId);setItemClickListener { money = it };setWasteSnListener { wasteSn = it } }.show()
            }

        }

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
    fun onWxPayEvent(event: WxPayEvent) {
        LogHelper.e("充值确认")
        confirmPayStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)

        http_1?.cancel()
        http_2?.cancel()
    }

    private var handler = Handler()
    private var runnable: Runnable = Runnable { confirmPayStatus() }
    private var http_1: Call<StatusBean>? = null
    private fun confirmPayStatus() {
        showProgressDialog("支付确认中...", false)
        http_1 = RetrofitHelper().confirmPayStatus(card?.cardId).apply {
            enqueue(object : Callback<StatusBean> {
                override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                    hideProgressDialog()
                    resultNotice("支付异常", "订单异常，请联系客服进行处理，客服工作时间：9:00-18:00 ；联系方式：400-888-1122")
                }

                override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                    response.body()?.let {
                        if (it.success) {
                            hideProgressDialog()
                            startActivity(Intent(this@PreRechargeActivity, PreRechargeFinishActivity::class.java)
                                    .putExtra("data", card)
                                    .putExtra("money", money)
                                    .putExtra("wasteSn", wasteSn))
                        } else {
                            if (it.payStatus != 2) {
                                hideProgressDialog()
                                resultNotice("支付失败", it.msg)
                            } else {
                                handler.postDelayed(runnable, 3000)
                            }
                        }
                    }
                }
            })
        }
    }

}

