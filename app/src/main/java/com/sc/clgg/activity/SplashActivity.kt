package com.sc.clgg.activity

import android.content.Intent
import com.google.gson.Gson
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Check
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
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

    private fun init() {
        val v = 0
        when (v) {
            1 -> {
                val json ="{\"agentIdcardImgBehind\":\"/storage/sdcard0/Tencent/QQ_Images/-5774e1b6b7cf7a24.jpg\",\"agentIdcardImgFront\":\"/storage/sdcard0/Tencent/QQ_Images/3d1d1d950cbe8fb7.jpg\",\"agentName\":\"？？\",\"agentPhone\":\"11111111111\",\"businessLicenseImg\":\"/storage/sdcard0/Tencent/QQ_Images/-5f3a40f0b7504488.jpg\",\"cardType\":\"3\",\"certSn\":\"？？\",\"certType\":\"203\",\"etcCardApplyVehicleVoList\":[{\"address\":\"江苏省淮安市清浦区武墩街20号\",\"axleNumber\":\"\",\"carColor\":\"蓝\",\"carNo\":\"？？\",\"carNoColor\":\"蓝\",\"carNoImageId\":\"7411f9ba97ce47cdabf49b4a1c78b88d\",\"carOwner\":\"梁昊\",\"carType\":\"一型货车\",\"engineNumber\":\"C91320535\",\"function\":\"营业货车\",\"model\":\"雪佛兰牌SGM7140MTB\",\"tyreNumber\":\"\",\"vehicleFrontImg\":\"/storage/sdcard0/Tencent/QQ_Images/-16e27a6af82f678c.jpg\",\"vehicleImageId\":\"5a02503a6d41478fad1216318e989e72\",\"vehicleLicenseImg\":\"/storage/sdcard0/Tencent/QQ_Images/4486d5f5c2a6e6fb.jpg\",\"vehicleType\":\"小型轿车\",\"vinCode\":\"LSGSA52M6DD092219\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"\",\"linkMobile\":\"\",\"recipientsAddress\":\"北京 北京市 东城区？？？？？\",\"recipientsName\":\"？？\",\"recipientsPhone\":\"11111111111\",\"userName\":\"\",\"userType\":\"2\"}"

                RetrofitHelper().apply(Gson().fromJson(json, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>, t: Throwable) {
                        LogHelper.e("onFailure   ${t.message}")
                    }

                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                        LogHelper.e("onResponse   ${response.body().toString()}")
                    }
                })

            }
            else -> {
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }
    }

}
