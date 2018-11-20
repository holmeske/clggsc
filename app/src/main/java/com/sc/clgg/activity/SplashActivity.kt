package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sc.clgg.activity.basic.LaunchActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LaunchActivity::class.java))
        finish()
        val info="{\"businessLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/5SJX5)8F0JWG@[KM447E4RF.jpg\",\"cardType\":\"3\",\"certSn\":\"34222419920309133X\",\"etcCardApplyVehicleVoList\":[{\"carColor\":\"红\",\"carNo\":\"沪DA3719\",\"carNoColor\":\"黄\",\"carOwner\":\"上海远行供应链(集团)有限公司\",\"carType\":\"五型货车\",\"carWeight\":\"18000\",\"engineNumber\":\"1614D071069\",\"function\":\"营业货车\",\"imageId\":\"25ba3a2720814c248db4a40129e180a3\",\"vehicleLicenseImg\":\"/data/user/0/com.sc.clgg/cache/takephoto_cache/5SJX5)8F0JWG@[KM447E4RF.jpg\",\"vinCode\":\"LZGJDNR18EX030565\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"000000\",\"linkMobile\":\"18949924714\",\"recipientsAddress\":\"上海 上海市 静安区永和路118弄东方环球企业中心2号楼\",\"recipientsName\":\"吕科\",\"recipientsPhone\":\"18949924714\",\"userName\":\"吕科\",\"userType\":\"2\",\"verificationCode\":\"221599\"}"
//        startActivity(Intent(this, ETCActivity::class.java))
    }
}
