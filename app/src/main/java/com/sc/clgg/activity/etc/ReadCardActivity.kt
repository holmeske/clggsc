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
import etc.obu.data.ServiceStatus
import kotlinx.android.synthetic.main.activity_read_card.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class ReadCardActivity : BaseImmersionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card)

        titlebar_title.text = getString(R.string.read_card)

        tv_no_card_recharge.setOnClickListener { startActivity(Intent(this, MyCardActivity::class.java).putExtra("click", true)) }

        tv_read_card.setOnClickListener {
            if (!isOpenBluetoothLocation()) {
                return@setOnClickListener
            }
            showProgressDialog("正在读卡")
            LogHelper.e("开始连接蓝牙设备")
            var intRandom = ""
            var intMac = ""
            val KEY = "2D65d001246ade79151C634be75264AF"
            intRandom = "1234"
            try {
                intMac = NewDES.PBOC_3DES_MAC(intRandom, KEY).substring(0, 8)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Thread(Runnable {
                App.getInstance().mObuInterface.connectDevice().apply {
                    runOnUiThread { hideProgressDialog() }
                    if (serviceCode == 0) {

                        //runOnUiThread { toast("连接蓝牙设备成功") }
                        LogHelper.e("连接蓝牙设备成功")
                        if (App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) == 0) {
                            //runOnUiThread { toast("认证成功") }
                            LogHelper.e("认证成功")
                            val cardInfo = CardInformation()
                            val status = App.getInstance().mObuInterface.getCardInformation(cardInfo)
                            if (status.serviceCode == 0) {
                                //runOnUiThread { toast("读卡成功") }
                                LogHelper.e("${Gson().toJson(cardInfo)}")
                                startActivity(Intent(this@ReadCardActivity, WriteCardActivity::class.java)
                                        .putExtra("card", cardInfo))
                            }
                        }

                    } else {
                        LogHelper.e("连接蓝牙设备失败")
                        runOnUiThread { toast("连接蓝牙设备失败") }
                    }
                }

            }).start()

        }
    }

    /**
     * 连接蓝牙设备
     */
    private fun connectDevice(): ServiceStatus {
        return App.getInstance().mObuInterface.connectDevice()
    }

    /**
     * 断开蓝牙设备
     */
    private fun disconnectDevice(): ServiceStatus {
        return App.getInstance().mObuInterface.disconnectDevice()
    }

    /**
     * 认证设备
     */
    private fun intAuthDev(): Boolean {
        var intRandom = "1234"
        val KEY = "2D65d001246ade79151C634be75264AF"
        var intMac = ""
        try {
            intMac = NewDES.PBOC_3DES_MAC(intRandom, KEY).substring(0, 8)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return App.getInstance().mObuInterface.intAuthDev(intRandom.length / 2, intRandom, intMac) === 0
    }

    /**
     * 读取卡片信息
     */
    private fun getCardInformation(cardInfo: CardInformation): ServiceStatus {
        return App.getInstance().mObuInterface.getCardInformation(cardInfo)
    }

    private fun readCardInfo() {
        LogHelper.e("认证成功")
        val cardInfo = CardInformation()
        val status = App.getInstance().mObuInterface.getCardInformation(cardInfo)
        LogHelper.e("卡片信息 = " + Gson().toJson(status))
        if (status.getServiceCode() == 0) {
            LogHelper.e("读卡成功 ")

            var pinCode = ""
            if ("40" == cardInfo.cardVersion) {
                pinCode = "313233343536"
            } else {
                pinCode = "123456"
            }

            val loadMac1Status = App.getInstance().mObuInterface.loadCreditGetMac1(
                    "37011801220102886102",
                    100,
                    "000000000000",
                    pinCode,
                    "02",
                    "01")
            val info = loadMac1Status.getServiceInfo().split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var mac1 = ""
            for (s in info) {
                LogHelper.e("info元素 = $s")
                if (s.startsWith("a_m1=")) {
                    mac1 = s.substring(5, s.length)
                }
            }
            LogHelper.e("loadCreditGetMac1 = " + Gson().toJson(loadMac1Status))

            if (loadMac1Status.getServiceCode() == 0) {
                loadCreditWriteCard("")
            }

        }
    }

    private fun loadCreditWriteCard(mac2: String): ServiceStatus {
        val time = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis())

        val writeCardStatu = App.getInstance().mObuInterface.loadCreditWriteCard(time + mac2)
        LogHelper.e("loadCreditWriteCard = " + Gson().toJson(writeCardStatu))
        if (writeCardStatu.getServiceCode() == 0) {
            toast("写卡成功")
        }
        return writeCardStatu
    }

}
