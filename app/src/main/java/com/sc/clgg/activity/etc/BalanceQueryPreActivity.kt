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

        titlebar_title.text = getString(R.string.my_etc_balance_query)

        tv_read_card.setOnClickListener {
            if (isOpenBluetoothLocation()) readCard()
        }
    }

    private fun readCard() {

        val intRandom = "1234"
        var intMac = ""
        val key = "2D65d001246ade79151C634be75264AF"
        try {
            intMac = NewDES.PBOC_3DES_MAC(intRandom, key).substring(0, 8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogHelper.e("蓝牙设备 - 开始连接")
        showProgressDialog("正在读卡")
        job = GlobalScope.launch {

            App.getInstance().mObuInterface.connectDevice().apply {

                hideProgressDialog()

                yield()
                if (serviceCode == 0) {

                    LogHelper.e("蓝牙设备 - 连接成功")
                    if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                        LogHelper.e("蓝牙设备 - 认证成功")
                        val cardInfo = CardInformation()
                        if (App.getInstance().mObuInterface.getCardInformation(cardInfo).serviceCode == 0) {
                            LogHelper.e(Gson().toJson(cardInfo))
                            startActivity(Intent(this@BalanceQueryPreActivity, BalanceQueryActivity::class.java).putExtra("card", cardInfo))
                        }
                    }

                } else {
                    LogHelper.e("连接设备状态返回信息 is ${Gson().toJson(this)}")
                    LogHelper.e(serviceInfo)
                    launch(Dispatchers.Main) { toast(serviceInfo) }
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
