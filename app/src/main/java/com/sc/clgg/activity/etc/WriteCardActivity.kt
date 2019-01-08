package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.application.App
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardInfo
import com.sc.clgg.bean.CircleSave
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.ConfirmCircleDialog
import com.sc.clgg.dialog.PreRechargeHintDialog
import com.sc.clgg.dialog.RechargeDialog
import com.sc.clgg.etc.NewDES
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.retrofit.WeChatPayCache
import com.sc.clgg.tool.helper.LogHelper
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
    private var card: CardInformation? = null
    private var RQcMoney = 0
    private var RAdjust = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_card)

        titlebar_title.text = getString(R.string.recharge_write_card)
        iv_nav.setImageResource(R.drawable.pay_nav_step_icon)
        card = intent.getParcelableExtra("card")

        tv_card.text = card?.cardId
        tv_carno.text = card?.vehicleNumber
        card?.balance?.toDouble()?.let { tv_balance.text = "${String.format("%.2f", it / 100)}元" }

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
                RechargeDialog(this).apply { setCardNumber(card?.cardId) }.show()
            }
        }

        init()
    }

    private fun init() {
        RetrofitHelper().getCardInfo(card?.cardId, "0").enqueue(object : Callback<CardInfo> {
            override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                response.body()?.let {
                    RQcMoney = it.RQcMoney!!.toInt()
                    RAdjust = it.RAdjust!!.toInt()
                    tv_carno.text = it.RVLP
                    it.RQcMoney?.toDouble()?.let { tv_can_write.text = "${String.format("%.2f", it / 100)} 元" }

                    if (it.RQcMoney?.toDouble()!! > 0) {
                        tv_recharge_circle.text = "圈存"
                        cl_go_circle.isSelected = true
                        cl_go_recharge.isSelected = false

                        cl_go_recharge.isEnabled = false
                    } else {
                        tv_recharge_circle.text = "充值"
                        cl_go_circle.isSelected = false
                        cl_go_recharge.isSelected = true

                        cl_go_circle.isEnabled = false
                    }
                }
            }
        })
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

    private fun qc() {
        showProgressDialog(false)
        Thread {
            var connectStatus = App.getInstance().mObuInterface.getConnectStatus()
            if (connectStatus.toString().equals("ConnectStatus.SERVICES_DISCOVERED")) {
                writeCard(card!!)
            } else {
                connectWriteCard()
            }
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

    private var handler = Handler()
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            confirmPayStatus()
        }
    }

    private fun confirmPayStatus() {
        RetrofitHelper().confirmPayStatus(WeChatPayCache.cardNo).enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                response.body()?.let {
                    if (it.success) {
                        hideProgressDialog()
                        toast("支付成功")
                        PreRechargeHintDialog(this@WriteCardActivity).apply {
                            show()
                            setData("请保持蓝牙盒子开启", "(蓝色指示灯闪亮)")
                            setCancelListener {
                                RetrofitHelper().getCardInfo(card?.cardId, "0").enqueue(object : Callback<CardInfo> {
                                    override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                                        toast(R.string.network_anomaly)
                                    }

                                    override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                                        response.body()?.let {
                                            RQcMoney = it.RQcMoney!!.toInt()
                                            RAdjust = it.RAdjust!!.toInt()
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
                            toast("${it.msg}")
                            startActivity(Intent(this@WriteCardActivity, PayResultActivity::class.java)
                                    .putExtra("msg", it.msg))
                        } else {
                            handler.postDelayed(runnable, 3000)
                        }
                    }
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: MessageEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        if (event?.value == 4) {
            LogHelper.e("充值确认")
            showProgressDialog()
            confirmPayStatus()
            /*RetrofitHelper().surePayMoney(WeChatPayCache.cardNo, WeChatPayCache.money).enqueue(object : Callback<StatusBean> {
                override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                    hideProgressDialog()
                    response.body()?.let {
                        if (it.success) {
                            toast("支付成功")
                            PreRechargeHintDialog(this@WriteCardActivity).apply {
                                show()
                                setData("请保持蓝牙盒子开启", "(蓝色指示灯闪亮)")
                                setCancelListener {
                                    RetrofitHelper().getCardInfo(card?.cardId, "0").enqueue(object : Callback<CardInfo> {
                                        override fun onFailure(call: Call<CardInfo>, t: Throwable) {
                                            toast(R.string.network_anomaly)
                                        }

                                        override fun onResponse(call: Call<CardInfo>, response: Response<CardInfo>) {
                                            response.body()?.let {
                                                RQcMoney = it.RQcMoney!!.toInt()
                                                RAdjust = it.RAdjust!!.toInt()
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
                                                qc()
                                            }
                                        }
                                    })

                                    dismiss()
                                }
                            }
                        } else {
                            startActivity(Intent(this@WriteCardActivity, PayResultActivity::class.java)
                                    .putExtra("msg", it.msg))
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

    private fun connectWriteCard() {
        var bluetoothSn: String
        var KEY = "2D65d001246ade79151C634be75264AF"
        var intRandom: String
        var intMac = ""
        LogHelper.e("开始连接")
        App.getInstance().mObuInterface.connectDevice().apply {
            if (serviceCode == 0) {
                LogHelper.e("连接成功")

                LogHelper.e("开始读取蓝牙设备信息")
                App.getInstance().mObuInterface.getDeviceInformation().apply {
                    if (serviceCode == 0) {
                        LogHelper.e("读取蓝牙设备信息成功")
                        bluetoothSn = Sn

                        intRandom = "1234"
                        try {
                            intMac = NewDES.PBOC_3DES_MAC(intRandom, KEY)!!.substring(0, 8)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                            LogHelper.e("认证成功")

                            val cardInfo = CardInformation()
                            val status = App.getInstance().mObuInterface.getCardInformation(cardInfo)
                            LogHelper.e("读卡 = " + Gson().toJson(status))

                            if (status.serviceCode == 0) {
                                LogHelper.e("读卡成功 " + Gson().toJson(cardInfo))
                                writeCard(cardInfo)
                            }

                        }


                    } else {
                        hideProgressDialog()
                        LogHelper.e("读取蓝牙设备信息失败")
                    }
                }
            } else {
                LogHelper.e("连接失败")
                hideProgressDialog()
            }
        }
    }

    private fun writeCard(cardInfo: CardInformation) {
        App.getInstance().mObuInterface.getDeviceInformation().apply {

            var pinCode = ""
            if ("40" == cardInfo.cardVersion) {
                pinCode = "313233343536"
            } else {
                pinCode = "123456"
            }
            val loadMac1Status = App.getInstance().mObuInterface.loadCreditGetMac1(
                    cardInfo.cardId,
                    RQcMoney + RAdjust,
                    "000000000000",
                    pinCode,
                    "02",
                    "01")
            val info = loadMac1Status.serviceInfo.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (s in info) {
                LogHelper.e("info元素 = $s")
                if (s.startsWith("a_cid=")) {
                    a_cid = s.substring(6, s.length)
                } else if (s.startsWith("a_pt=")) {
                    a_pt = s.substring(5, s.length)
                } else if (s.startsWith("a_rnd=")) {
                    a_rnd = s.substring(6, s.length)
                } else if (s.startsWith("a_cbb=")) {
                    a_cbb = s.substring(6, s.length)
                } else if (s.startsWith("a_m1=")) {
                    a_m1 = s.substring(5, s.length)
                } else if (s.startsWith("a_on=")) {
                    a_on = s.substring(5, s.length)
                }
            }

            if (loadMac1Status.serviceCode == 0) {
                RetrofitHelper().loadMoney(
                        cardInfo.cardId,
                        RQcMoney.toString(),
                        RAdjust.toString(),
                        a_m1,
                        a_cbb, a_rnd, a_on, Sn).enqueue(object : Callback<CircleSave> {
                    override fun onResponse(call: Call<CircleSave>, response: Response<CircleSave>) {
                        hideProgressDialog()
                        if (response.isSuccessful && response.body()?.success!!) {

                            var date = ""
                            val timeStr = response.body()!!.RWriteTime
                            if (!TextUtils.isEmpty(timeStr)) {
                                date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date(timeStr!!).time)
                            }
                            val dateMac2 = date + response.body()!!.Mac2!!
                            LogHelper.e("dateMac2 = $dateMac2")
                            val writeCardStatu = App.getInstance().mObuInterface.loadCreditWriteCard(dateMac2)
                            LogHelper.e("写卡 = " + Gson().toJson(writeCardStatu))
                            var chargeFlag = "-1"
                            if (writeCardStatu.serviceCode == 0) {
                                chargeFlag = "0"
                            } else {
                                chargeFlag = "-1"
                            }
                            RetrofitHelper().sureLoadMoney(cardInfo.cardId,
                                    response.body()!!.RChargeLsh,
                                    cardInfo.balanceString, chargeFlag, writeCardStatu.serviceInfo,
                                    a_on,
                                    (RQcMoney + RAdjust).toString(),
                                    response.body()!!.RWriteTime!!.replace("/".toRegex(), "-"))
                                    .enqueue(object : Callback<CircleSave> {
                                        override fun onResponse(call: Call<CircleSave>, response: Response<CircleSave>) {
                                            hideProgressDialog()
                                            if (response.body()!!.success) {
                                                val cardInfo = CardInformation()
                                                App.getInstance().mObuInterface.getCardInformation(cardInfo)

                                                Toast.makeText(applicationContext, "圈存成功", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this@WriteCardActivity,
                                                        WriteCardSuccessActivity::class.java)
                                                        .putExtra("data", response.body())
                                                        .putExtra("balance", cardInfo.balance))
                                            } else {
                                                Toast.makeText(applicationContext, "圈存失败", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<CircleSave>, t: Throwable) {
                                            hideProgressDialog()
                                            toast(R.string.network_anomaly)
                                        }
                                    })
                        }
                    }

                    override fun onFailure(call: Call<CircleSave>, t: Throwable) {
                        hideProgressDialog()
                        toast(R.string.network_anomaly)
                    }
                })
            }
        }
    }
}


