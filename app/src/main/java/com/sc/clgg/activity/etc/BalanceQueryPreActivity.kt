package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.application.App
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.etc.NewDES
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.isOpenBluetoothLocation
import etc.obu.data.CardInformation
import kotlinx.android.synthetic.main.activity_balance_query_pre.*
import kotlinx.android.synthetic.main.view_titlebar.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast

class BalanceQueryPreActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_query_pre)

        titlebar_title.text = "我的ETC卡余额查询"

        tv_read_card.setOnClickListener {
            if (isOpenBluetoothLocation()) readCard()
        }
    }

    private var a_on = ""
    private fun readCard() {

        val intRandom = "1234"
        var intMac = ""
        val key = "2D65d001246ade79151C634be75264AF"
        try {
            intMac = NewDES.PBOC_3DES_MAC(intRandom, key).substring(0, 8)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        job = GlobalScope.launch {
            launch(Dispatchers.Main) { showProgressDialog("正在读卡") }
            val deffered = async {
                LogHelper.e("蓝牙设备 - 开始连接")
                App.getInstance().mObuInterface.connectDevice().apply {

                    launch(Dispatchers.Main) { hideProgressDialog() }

                    yield()
                    if (serviceCode == 0) {

                        LogHelper.e("蓝牙设备 - 连接成功")
                        if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                            LogHelper.e("蓝牙设备 - 认证成功")
                            val cardInfo = CardInformation()
                            if (App.getInstance().mObuInterface.getCardInformation(cardInfo).serviceCode == 0) {
                                LogHelper.e(Gson().toJson(cardInfo))
                                startActivity(Intent(this@BalanceQueryPreActivity, BalanceQueryActivity::class.java).putExtra("card", cardInfo))
                                /*App.getInstance().mObuInterface.getDeviceInformation().apply {

                                    var pinCode = if ("40" == cardInfo.cardVersion) "313233343536" else "123456"

                                    val mac1Status = App.getInstance().mObuInterface.loadCreditGetMac1(cardInfo.cardId, 0 + 0, "000000000000",
                                            pinCode, "02", "01")

                                    val serviceInfo = mac1Status.serviceInfo.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                    for (s in serviceInfo) {
                                        LogHelper.e("info元素 = $s")
                                        if (s.startsWith("a_on=")) {
                                            a_on = s.substring(5, s.length)
                                        }
                                    }
                                    RetrofitHelper().sureLoadMoney(cardInfo.cardId, "89201903110948390434", (cardInfo.balance + 0 + 0).toString(),
                                            "-1", "00009302", a_on, (0 + 0).toString(),
                                            "2019/03/11 09:43:51"?.replace("/".toRegex(), "-")).enqueue(object : Callback<CircleSave> {
                                        override fun onResponse(call: Call<CircleSave>, response: Response<CircleSave>) {
                                            LogHelper.e(Gson().toJson(response.body()))
                                        }

                                        override fun onFailure(call: Call<CircleSave>, t: Throwable) {
                                            LogHelper.e(t.message)
                                        }
                                    })
                                }*/
                            }
                        }

                    } else {
                        LogHelper.e("连接设备状态返回信息 is ${Gson().toJson(this)}")
                        LogHelper.e("$serviceInfo")
                        launch(Dispatchers.Main) { toast("$serviceInfo") }
                    }
                }
            }
        }

    }

    private var job: Job? = null

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
