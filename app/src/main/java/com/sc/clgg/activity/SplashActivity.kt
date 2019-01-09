package com.sc.clgg.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.sc.clgg.activity.etc.CardIntroduceActivity
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.activity.etc.opencard.CarCertificationActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.config.ConstantValue
import pub.devrel.easypermissions.EasyPermissions


class SplashActivity : BaseImmersionActivity() {

    override fun onResume() {
        super.onResume()
        if (EasyPermissions.hasPermissions(this, *ConstantValue.PERMISSION_NEED)) {
            init()
        }
    }

    @SuppressLint("SdCardPath")
    private fun init() {
        val v = 2
        when (v) {
            1 -> {
                //var gson = "{\"agentIdcardImgBehind\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1546938864614.jpg\",\"agentIdcardImgFront\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1546938864614.jpg\",\"agentName\":\"张三\",\"agentPhone\":\"15380836693\",\"businessLicenseImg\":\"\",\"cardType\":\"2\",\"certSn\":\"320721199402171625\",\"certType\":\"101\",\"etcCardApplyVehicleVoList\":[{\"address\":\"中国(上海）自由贸易试验区顺通路5号B棱D158室\",\"axleNumber\":\"6\",\"carColor\":\"黄\",\"vehicleType\":\"重型半挂牵引车\",\"carNo\":\"沪DA3719\",\"carNoColor\":\"黄\",\"carNoImageId\":\"6001f443e75e4f6898ce7a60e1655237\",\"carOwner\":\"上海远行供应链管理集团有限公司\",\"carType\":\"一型客车\",\"engineNumber\":\"1614D071069\",\"function\":\"营业货车\",\"model\":\"陕汽牌SX4186NR361\",\"tyreNumber\":\"5\",\"vehicleFrontImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/05.jpg\",\"vehicleImageId\":\"fa857cb1815d4d4f8346b47c38539ca9\",\"vehicleLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1547019838747.jpg\",\"vinCode\":\"LZGJDNR18EX030565\"},{\"address\":\"中国(上海）自由贸易试验区顺通路5号B棱D158室\",\"axleNumber\":\"6\",\"carColor\":\"黄\",\"vehicleType\":\"重型半挂牵引车\",\"carNo\":\"沪DA3719\",\"carNoColor\":\"黄\",\"carNoImageId\":\"6001f443e75e4f6898ce7a60e1655237\",\"carOwner\":\"上海远行供应链管理集团有限公司\",\"carType\":\"一型客车\",\"engineNumber\":\"1614D071069\",\"function\":\"营业货车\",\"model\":\"陕汽牌SX4186NR361\",\"tyreNumber\":\"5\",\"vehicleFrontImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/05.jpg\",\"vehicleImageId\":\"fa857cb1815d4d4f8346b47c38539ca9\",\"vehicleLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1547019838747.jpg\",\"vinCode\":\"LZGJDNR18EX030565\"}],\"idcardImgBehind\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1546938864614.jpg\",\"idcardImgFront\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/1546938864614.jpg\",\"invitationCode\":\"\",\"linkMobile\":\"\",\"recipientsAddress\":\"上海 上海市 静安区东方环球企业118弄2号楼\",\"recipientsName\":\"李雯雯\",\"recipientsPhone\":\"18351926827\",\"userName\":\"\",\"userType\":\"1\",\"verificationCode\":\"\"}"
                //startActivity(Intent(this, InfoCertificationActivity::class.java).putExtra("info", Gson().fromJson(gson, CertificationInfo::class.java)))

                /*RetrofitHelper().apply(Gson().fromJson(gson, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                    }
                })*/
                finish()
            }
            2 -> {
                startActivity(Intent(this, CardIntroduceActivity::class.java))
                finish()
            }
            3 -> {
                startActivity(Intent(this, BleActivity::class.java))
                finish()
            }
            4 -> {
                startActivity(Intent(this, CarCertificationActivity::class.java))
                finish()
            }
            else -> {
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }
    }

}
