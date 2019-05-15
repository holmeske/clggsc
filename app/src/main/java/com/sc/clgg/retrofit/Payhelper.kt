package com.sc.clgg.retrofit

import android.content.Context
import com.sc.clgg.BuildConfig
import com.sc.clgg.R
import com.sc.clgg.bean.WeChatOrder
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.wxapi.WeChatPayUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author：lvke
 * @date：2018/11/26 14:50
 */
/**
 * 微信支付
 */
fun Context.wxPay(cardNo: String?, acount: String?, out_trade_no: String?) {
    RetrofitHelper().wxPay(cardNo, acount, out_trade_no).enqueue(object : Callback<WeChatOrder> {
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



