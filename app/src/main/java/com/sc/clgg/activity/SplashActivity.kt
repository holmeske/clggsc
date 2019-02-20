package com.sc.clgg.activity

import android.content.Intent
import com.google.gson.Gson
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.activity.etc.opencard.InfoCertificationActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Check
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
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
                val info = "{\"agentIdcardImgBehind\":\"/data/data/com.sc.clgg/cache/takephoto_cache/20140302235502_AxFyM.jpeg\",\"agentIdcardImgFront\":\"/data/data/com.sc.clgg/cache/takephoto_cache/40409.png\",\"agentName\":\"王总\",\"agentPhone\":\"6666666666666\",\"businessLicenseImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/IMG_20180604_111748.jpg\",\"cardType\":\"3\",\"certSn\":\"33333333333333333333\",\"certType\":\"203\",\"etcCardApplyVehicleVoList\":[{\"address\":\"999999\",\"axleNumber\":\"\",\"carColor\":\"蓝\",\"carNo\":\"999999\",\"carNoColor\":\"蓝\",\"carNoImageId\":\"97074e2ed4a64f01a998d36f895088b1\",\"carOwner\":\"999999999999\",\"carType\":\"一型货车\",\"engineNumber\":\"999999\",\"function\":\"营业货车\",\"model\":\"999999\",\"tyreNumber\":\"\",\"vehicleFrontImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/行驶证.jpg\",\"vehicleImageId\":\"c034b268f4434a7a858b00ac600c5501\",\"vehicleLicenseImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/Screenshot_2018-06-30-17-56-04.jpeg\",\"vehicleType\":\"999999\",\"vinCode\":\"999999\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"\",\"linkMobile\":\"\",\"recipientsAddress\":\"北京 北京市 东城区故宫～军机处\",\"recipientsName\":\"林总\",\"recipientsPhone\":\"11111111111\",\"userName\":\"\",\"userType\":\"2\"}"

                RetrofitHelper().apply(Gson().fromJson(info, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<Check>, response: Response<Check>) {

                    }

                })
            }
            2 -> {
                startActivity(Intent(this, InfoCertificationActivity::class.java))
                finish()
            }
            3 -> {
                startActivity(Intent(this, BleActivity::class.java))
                finish()
            }
            4 -> {
            }
            else -> {
//                WriteCardHintDialog(this).apply { show();setData("000", "111");setCancelListener {  } }
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }
    }

}
