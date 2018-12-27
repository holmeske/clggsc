package com.sc.clgg.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.google.gson.Gson
import com.sc.clgg.BuildConfig
import com.sc.clgg.R
import com.sc.clgg.activity.etc.CardIntroduceActivity
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Check
import com.sc.clgg.bean.WeChatOrder
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.config.ConstantValue.WX_PARTNER_ID
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.wxapi.WeChatPayUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : BaseImmersionActivity() {

    override fun onResume() {
        super.onResume()
        if (EasyPermissions.hasPermissions(this, *ConstantValue.PERMISSION_NEED)) {
            init()
        }
    }

    @SuppressLint("SdCardPath")
    private fun init() {
        val v = 0
        when (v) {
            1 -> {

                RetrofitHelper().wxPay("56", "1").enqueue(object : Callback<WeChatOrder> {
                    override fun onResponse(call: Call<WeChatOrder>, response: Response<WeChatOrder>) {
                        val request = PayReq()
                        response.body()?.let {
                            if (it.success) {
                                request.appId = it.data?.appid
                                request.partnerId = WX_PARTNER_ID
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
            2 -> {
                val info = "{\"businessLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/5SJX5)8F0JWG@[KM447E4RF.jpg\",\"cardType\":\"3\",\"certSn\":\"34222419920309133X\",\"etcCardApplyVehicleVoList\":[{\"carColor\":\"红\",\"carNo\":\"沪DA3719\",\"carNoColor\":\"黄\",\"carOwner\":\"上海远行供应链(集团)有限公司\",\"carType\":\"五型货车\",\"carWeight\":\"18000\",\"engineNumber\":\"1614D071069\",\"function\":\"营业货车\",\"imageId\":\"25ba3a2720814c248db4a40129e180a3\",\"vehicleLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/5SJX5)8F0JWG@[KM447E4RF.jpg\",\"vinCode\":\"LZGJDNR18EX030565\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"000000\",\"linkMobile\":\"18949924714\",\"recipientsAddress\":\"上海 上海市 静安区永和路118弄东方环球企业中心2号楼\",\"recipientsName\":\"吕科\",\"recipientsPhone\":\"18949924714\",\"userName\":\"吕科\",\"userType\":\"2\",\"verificationCode\":\"221599\"}"

                RetrofitHelper().apply(Gson().fromJson(info, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                    }

                    override fun onFailure(call: Call<Check>, t: Throwable) {
                    }
                })
            }
            3 -> {
                startActivity(Intent(this, BleActivity::class.java))
                finish()
            }
            4 -> {
                startActivity(Intent(this, CardIntroduceActivity::class.java))
                finish()
            }
            else -> {
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }
    }

}
