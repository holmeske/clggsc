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
import kotlinx.android.synthetic.main.activity_read_card.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class ReadCardActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card)

        val type = intent.getStringExtra("type")
        if ("add" == type) {
            titlebar_title.text = "添加卡片 读卡"
        } else {
            titlebar_title.text = getString(R.string.read_card)
        }

        tv_no_card_recharge.setOnClickListener { startActivity(Intent(this, MyCardActivity::class.java).putExtra("click", true)) }

        tv_read_card.setOnClickListener {
            if (!isOpenBluetoothLocation()) {
                return@setOnClickListener
            }
            showProgressDialog("正在读卡")
            LogHelper.e("开始连接蓝牙设备")
            val intRandom = "1234"
            var intMac = ""
            val key = "2D65d001246ade79151C634be75264AF"
            try {
                intMac = NewDES.PBOC_3DES_MAC(intRandom, key).substring(0, 8)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            doAsync {
                App.getInstance().mObuInterface.connectDevice().apply {
                    hideProgressDialog()
                    if (serviceCode == 0) {

                        LogHelper.e("蓝牙设备 - 连接成功")
                        if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                            LogHelper.e("认证成功")
                            val cardInfo = CardInformation()
                            if (App.getInstance().mObuInterface.getCardInformation(cardInfo).serviceCode == 0) {
                                LogHelper.e(Gson().toJson(cardInfo))
                                if ("add" == type) {
                                    startActivity(Intent(this@ReadCardActivity, BalanceQueryActivity::class.java)
                                            .putExtra("card", cardInfo)
                                            .putExtra("type", "add"))
                                } else {
                                    startActivity(Intent(this@ReadCardActivity, WriteCardActivity::class.java).putExtra("card", cardInfo))
                                }
                            }
                        }

                    } else {
                        LogHelper.e(serviceInfo)
                        runOnUiThread { toast(serviceInfo) }
                    }
                }
            }
        }
    }

}
