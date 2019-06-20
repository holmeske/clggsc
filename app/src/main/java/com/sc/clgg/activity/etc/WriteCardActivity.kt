package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.application.App
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CircleSave
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.bean.WxPayEvent
import com.sc.clgg.dialog.ConfirmCircleDialog
import com.sc.clgg.dialog.PreRechargeHintDialog
import com.sc.clgg.dialog.RechargeDialog
import com.sc.clgg.dialog.WriteCardHintDialog
import com.sc.clgg.etc.NewDES
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.isOpenBluetoothLocation
import etc.obu.data.CardInformation
import kotlinx.android.synthetic.main.activity_write_card.*
import kotlinx.android.synthetic.main.view_read_card.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WriteCardActivity : BaseImmersionActivity() {
    private lateinit var card: CardInformation
    private var RQcMoney = 0
    private var RAdjust = 0
    private var RCarNo: String? = ""
    private var wasteSn: String? = ""
    private var justCircle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_card)

        titlebar_title.text = getString(R.string.recharge_write_card)
        iv_nav.setImageResource(R.drawable.pay_nav_step_icon)
        card = intent.getParcelableExtra("card")

        tv_card.text = card.cardId
        tv_carno.text = card.vehicleNumber
        card.balance.toDouble().let { tv_balance.text = "${String.format("%.2f", it / 100)}元" }

        tv_recharge_circle.setOnClickListener {
            if (tv_recharge_circle.text == "圈存") {
                ConfirmCircleDialog(this@WriteCardActivity).run {
                    show()
                    setData(RQcMoney.toDouble() / 100)
                    setConfirmListener {
                        dismiss()
                        qc()
                    }
                    setCancelListener { toast("取消");dismiss() }
                }
            } else {
                RechargeDialog(this).apply { setCardNumber(card.cardId) }.show()
            }
        }
        updateCardInfo()
    }

    private fun updateCardInfo() {
        RetrofitHelper().getCardInfo(card.cardId, "0").enqueue(object : Callback<CardInfo> {
            override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                toast(R.string.network_anomaly)
                resultNotice("支付异常", "应答错误：卡状态查询失败！联系方式：400-888-1122")
            }

            override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                response.body()?.let {
                    if (it.success) {
                        setViewData(it)
                        justCircle = it.RQcMoney?.toDouble()!! > 0
                    } else {
                        resultNotice("支付异常", "${it.msg}")
                    }
                }
            }
        })
    }

    /**
     * 卡信息处理
     */
    private fun setViewData(it: CardInfo?) {
        RQcMoney = it?.RQcMoney?.toInt()!!
        RAdjust = it.RAdjust?.toInt()!!
        RCarNo = it.RVLP
        tv_carno?.text = it.RVLP
        it.RQcMoney?.toDouble()?.let { tv_can_write?.text = "${String.format("%.2f", it / 100)} 元" }

        if (it.RQcMoney?.toDouble()!! > 0) {
            tv_recharge_circle?.text = "圈存"
            cl_go_circle?.isSelected = true
            cl_go_recharge?.isSelected = false

            cl_go_recharge?.isEnabled = false
        } else {
            tv_recharge_circle?.text = "充值"
            cl_go_circle?.isSelected = false
            cl_go_recharge?.isSelected = true

            cl_go_circle?.isEnabled = false
        }
    }

    /**
     * a_cid   卡号（后16位）
     */
    private var a_cid = ""
    /**
     * a_pt     充值金额（单位分）
     */
    private var a_pt = ""
    /**
     *a_cbb   充值前余额（单位分）
     */
    private var a_rnd = ""
    /**
     * a_m1    MAC1码
     */
    private var a_cbb = ""
    /**
     * a_cid   卡号（后16位）
     */
    private var a_m1 = ""
    /**
     *a_on     联机交易序号
     */
    private var a_on = ""

    /**
     * 圈存
     */
    private fun qc() {
        if (!isOpenBluetoothLocation()) return
        showProgressDialog()
        Thread {
            if (App.getInstance().mObuInterface.connectStatus.toString() == "SERVICES_DISCOVERED") writeCard(card) else connectWriteCard()
        }.start()
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

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.e("断开设备连接  ${Gson().toJson(App.getInstance().mObuInterface.disconnectDevice())}")
        payStatusHttp?.cancel()
    }

    private var handler = Handler()
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            confirmPayStatus()
        }
    }

    private var payStatusHttp: Call<StatusBean>? = null
    /**
     * 获取支付确认状态
     */
    private fun confirmPayStatus() {
        payStatusHttp = RetrofitHelper().confirmPayStatus(card.cardId)
        payStatusHttp?.enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                hideProgressDialog()
                resultNotice("支付异常", "订单异常，请联系客服进行处理，客服工作时间：9:00-18:00 ；联系方式：400-888-1122")
            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                response.body()?.let {
                    if (it.success) {
                        hideProgressDialog()
                        wasteSn = it.RWasteSn
                        toast("支付成功")
                        PreRechargeHintDialog(this@WriteCardActivity).apply {
                            show()
                            setData("请保持蓝牙盒子开启", "(蓝色指示灯闪亮)")
                            setCancelListener {
                                RetrofitHelper().getCardInfo(card.cardId, "0").enqueue(object : Callback<CardInfo> {
                                    override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                                        toast(R.string.network_anomaly)
                                    }

                                    override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                                        response.body()?.run {
                                            setViewData(this)
                                            qc()
                                        }
                                    }
                                })
                                dismiss()
                            }
                        }
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

    private fun resultNotice(title: String? = "", msg: String? = "") {
        startActivity(Intent(this@WriteCardActivity, ResultNoticeActivity::class.java).putExtra("title", title).putExtra("msg", msg))
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onWxPayEvent(event: WxPayEvent) {
        showProgressDialog(false)
        confirmPayStatus()
    }

    /**
     * 连接设备写卡
     */
    private fun connectWriteCard() {
        val key = "2D65d001246ade79151C634be75264AF"
        val intRandom = "1234"
        var intMac = ""
        LogHelper.e("开始连接 - 蓝牙设备")
        App.getInstance().mObuInterface.connectDevice().apply {
            if (serviceCode == 0) {
                LogHelper.e("连接蓝牙设备 - 成功")

                App.getInstance().mObuInterface.deviceInformation.apply {
                    if (serviceCode == 0) {
                        LogHelper.e("读取蓝牙设备信息 - 成功")

                        try {
                            intMac = NewDES.PBOC_3DES_MAC(intRandom, key).substring(0, 8)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                            LogHelper.e("设备对系统认证 - 成功")

                            val cardInfo = CardInformation()
                            if (App.getInstance().mObuInterface.getCardInformation(cardInfo).serviceCode == 0) {
                                LogHelper.e("读取卡片信息 - 成功")
                                if (cardInfo.cardId == card.cardId) {
                                    writeCard(cardInfo)
                                } else {
                                    hideProgressDialog()
                                    LogHelper.e(" ---- 已连接其他蓝牙设备 ----")
                                    LogHelper.e(" ---- 自动断开其他蓝牙设备 ----")
                                    LogHelper.e("断开蓝牙设备 is ${Gson() to App.getInstance().mObuInterface.disconnectDevice()}")

                                    WriteCardHintDialog(this@WriteCardActivity).run {
                                        show();setData(card.cardId, cardInfo.cardId)
                                        setCancelListener {
                                            dismiss()
                                            qcFailure()
                                        }
                                    }
                                }
                            } else {
                                hideProgressDialog()
                                LogHelper.e("读取卡片信息 - 失败")
                            }
                            LogHelper.e("卡片信息 = ${Gson().toJson(cardInfo)}")

                        } else {
                            hideProgressDialog()
                            LogHelper.e("设备对系统认证 - 失败")
                        }

                    } else {
                        hideProgressDialog()
                        LogHelper.e("读取蓝牙设备信息 - 失败")
                    }
                }
            } else {
                hideProgressDialog()
                LogHelper.e(message)
                toast(message)
            }
        }
    }

    /**
     * 圈存失败提示
     */
    private fun qcFailure() {
        startActivity(Intent(this@WriteCardActivity, WriteCardSuccessActivity::class.java)
                .putExtra("failure", 1)
                .putExtra("data", CircleSave().apply { cardNo = card.cardId;carNo = RCarNo;RWasteSn = wasteSn })
                .putExtra("qc", RQcMoney)
                .putExtra("carNo", card.vehicleNumber)
                .putExtra("balance", card.balanceString))
    }

    /**
     * 写卡
     */
    private fun writeCard(cardInfo: CardInformation) {

        App.getInstance().mObuInterface.deviceInformation.apply {

            val pinCode = if ("40" == cardInfo.cardVersion) "313233343536" else "123456"

            val mac1Status = App.getInstance().mObuInterface.loadCreditGetMac1(cardInfo.cardId, RQcMoney + RAdjust, "000000000000",
                    pinCode, "02", "01")

            val serviceInfo = mac1Status.serviceInfo.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (s in serviceInfo) {
                LogHelper.e("info元素 = $s")
                when {
                    s.startsWith("a_cid=") -> a_cid = s.substring(6, s.length)
                    s.startsWith("a_pt=") -> a_pt = s.substring(5, s.length)
                    s.startsWith("a_rnd=") -> a_rnd = s.substring(6, s.length)
                    s.startsWith("a_cbb=") -> a_cbb = s.substring(6, s.length)
                    s.startsWith("a_m1=") -> a_m1 = s.substring(5, s.length)
                    s.startsWith("a_on=") -> a_on = s.substring(5, s.length)
                }
            }

            if (mac1Status.serviceCode == 0) {
                RetrofitHelper().loadMoney(cardInfo.cardId, RQcMoney.toString(), RAdjust.toString(), a_m1, a_cbb, a_rnd, a_on, Sn).enqueue(object : Callback<CircleSave> {
                    override fun onResponse(call: Call<CircleSave>, response: Response<CircleSave>) {
                        hideProgressDialog()
                        response.body()?.run {
                            if (success) {
                                var date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date(RWriteTime).time)

                                LogHelper.e("mac2 = $date$Mac2")

                                val writeCardStatu = App.getInstance().mObuInterface.loadCreditWriteCard(date + Mac2)

                                LogHelper.e("写卡 = ${Gson().toJson(writeCardStatu)}")

                                var chargeFlag = if (writeCardStatu.serviceCode == 0) "0" else "-1"

                                RetrofitHelper().sureLoadMoney(cardInfo.cardId, RChargeLsh, (cardInfo.balance + RQcMoney + RAdjust).toString(),
                                        chargeFlag, writeCardStatu.serviceInfo, a_on, (RQcMoney + RAdjust).toString(),
                                        RWriteTime?.replace("/".toRegex(), "-")).enqueue(object : Callback<CircleSave> {
                                    override fun onResponse(call: Call<CircleSave>, response: Response<CircleSave>) {
                                        hideProgressDialog()
                                        response.body()?.run {
                                            if (success) {
                                                if (writeCardStatu.serviceCode != 0) {
                                                    resultNotice("圈存失败", "圈存失败，钱款稍后在可圈存余额中查看。")
                                                } else {
                                                    var c = CardInformation()
                                                    Toast.makeText(applicationContext, "圈存成功", Toast.LENGTH_SHORT).show()
                                                    if (App.getInstance().mObuInterface.getCardInformation(c).serviceCode == 0) {
                                                        startActivity(Intent(this@WriteCardActivity, WriteCardSuccessActivity::class.java)
                                                                .putExtra("data", this)
                                                                .putExtra("justCircle", justCircle)
                                                                .putExtra("cardNo", c.cardId)
                                                                .putExtra("carNo", c.vehicleNumber)
                                                                .putExtra("balance", c.balance))
                                                    } else {
                                                        startActivity(Intent(this@WriteCardActivity, WriteCardSuccessActivity::class.java)
                                                                .putExtra("data", this)
                                                                .putExtra("justCircle", justCircle)
                                                                .putExtra("carNo", c.vehicleNumber)
                                                                .putExtra("cardNo", c.cardId)
                                                                .putExtra("balance", cardInfo.balance + RQcMoney + RAdjust))
                                                    }
                                                }
                                            } else {
                                                if (writeCardStatu.serviceCode != 0) {
                                                    resultNotice("圈存失败", "$msg")
                                                } else {
                                                    resultNotice("圈存成功", "$msg")
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<CircleSave>, t: Throwable) {
                                        hideProgressDialog()
                                        if (writeCardStatu.serviceCode != 0) {
                                            resultNotice("圈存失败", "圈存失败，请稍后再试，请勿在行车中圈存，以及圈存中请勿拔出卡片。")
                                        } else {
                                            resultNotice("圈存成功", "圈存成功，卡状态存在异常，请联系客服处理。联系电话：400-888-1122")
                                        }
                                    }
                                })
                            } else {
                                if (justCircle) {
                                    resultNotice("圈存失败", "$msg")
                                } else {
                                    qcFailure()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CircleSave>, t: Throwable) {
                        hideProgressDialog()
                        if (justCircle) {
                            resultNotice("圈存失败", "网络异常，请稍后再试。")
                        } else {
                            qcFailure()
                        }
                    }
                })
            }
        }
    }


}


