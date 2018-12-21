package com.sc.clgg.retrofit

import android.content.Context
import com.sc.clgg.BuildConfig
import com.sc.clgg.R
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.bean.WeChatOrder
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.tool.helper.RandomHelper
import com.sc.clgg.wxapi.WeChatPayUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author：lvke
 * @date：2018/11/26 14:50
 */
/**
 * 微信支付
 */
fun Context.wxPay(cardNo: String?, acount: String?) {
    RetrofitHelper().wxPay(cardNo, acount).enqueue(object : Callback<WeChatOrder> {
        override fun onResponse(call: Call<WeChatOrder>, response: Response<WeChatOrder>) {
            val request = PayReq()
            response.body()?.let {
                if (it.success) {
                    request.appId = it.data?.appid
                    request.partnerId = ConstantValue.WX_PARTNER_ID
                    request.prepayId = it.data?.prepay_id
                    request.packageValue = BuildConfig.APPLICATION_ID
                    request.nonceStr = it.data?.nonce_str
                    request.timeStamp = System.currentTimeMillis().toString()
                    request.sign = it.data?.sign
                    WeChatPayUtil(applicationContext).toPay(request)
                } else {
                    toast("${it.msg}")
                }
            }
        }

        override fun onFailure(call: Call<WeChatOrder>, t: Throwable) {
            toast(R.string.network_anomaly)
        }
    })
}

/**
 * 流水号
 */
fun Context.getWasteSn(currentTimeMillis:Long,cardNo: String):String {
    return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(currentTimeMillis)+ cardNo.substring(cardNo.length - 4) + RandomHelper.two()
}

fun Context.getWasteSnThree(currentTimeMillis:Long,cardNo: String):String {
    return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(currentTimeMillis)+ cardNo.substring(cardNo.length - 4) + RandomHelper.three()
}

fun Context.surePayMoney(cardNo: String?, money: String?, wasteSn: String?) {
    RetrofitHelper().surePayMoney(cardNo, money, wasteSn).enqueue(object : Callback<StatusBean> {
        override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
            response.body()?.let {
                if (it.success) {
                    toast("支付成功")
                } else {
                    toast("${it.msg}")
                }
            }
        }

        override fun onFailure(call: Call<StatusBean>, t: Throwable) {
            toast(R.string.network_anomaly)
        }
    })
}

internal class WeChatPayCache {
    companion object {
        var cardNo: String? = ""
        var money: String? = ""
        var wasteSn: String? = ""

        fun setValue(cardNumber: String, money: String, wasteSn: String) {
            this.wasteSn = wasteSn
        }
        fun setValue(  wasteSn: String) {
            this.wasteSn = wasteSn
        }

        fun initValue() {
            cardNo = ""
            money = ""
            wasteSn = ""
        }
    }
}



