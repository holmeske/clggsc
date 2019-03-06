package com.sc.clgg.activity

import android.content.Intent
import android.graphics.Color
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.coroutines.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions


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
            3 ->{

            }
            1 -> {
//                val json ="{\"agentIdcardImgBehind\":\"/storage/sdcard0/Tencent/QQ_Images/-5774e1b6b7cf7a24.jpg\",\"agentIdcardImgFront\":\"/storage/sdcard0/Tencent/QQ_Images/3d1d1d950cbe8fb7.jpg\",\"agentName\":\"？？\",\"agentPhone\":\"11111111111\",\"businessLicenseImg\":\"/storage/sdcard0/Tencent/QQ_Images/-5f3a40f0b7504488.jpg\",\"cardType\":\"3\",\"certSn\":\"？？\",\"certType\":\"203\",\"etcCardApplyVehicleVoList\":[{\"address\":\"江苏省淮安市清浦区武墩街20号\",\"axleNumber\":\"\",\"carColor\":\"蓝\",\"carNo\":\"？？\",\"carNoColor\":\"蓝\",\"carNoImageId\":\"7411f9ba97ce47cdabf49b4a1c78b88d\",\"carOwner\":\"梁昊\",\"carType\":\"一型货车\",\"engineNumber\":\"C91320535\",\"function\":\"营业货车\",\"model\":\"雪佛兰牌SGM7140MTB\",\"tyreNumber\":\"\",\"vehicleFrontImg\":\"/storage/sdcard0/Tencent/QQ_Images/-16e27a6af82f678c.jpg\",\"vehicleImageId\":\"5a02503a6d41478fad1216318e989e72\",\"vehicleLicenseImg\":\"/storage/sdcard0/Tencent/QQ_Images/4486d5f5c2a6e6fb.jpg\",\"vehicleType\":\"小型轿车\",\"vinCode\":\"LSGSA52M6DD092219\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"\",\"linkMobile\":\"\",\"recipientsAddress\":\"北京 北京市 东城区？？？？？\",\"recipientsName\":\"？？\",\"recipientsPhone\":\"11111111111\",\"userName\":\"\",\"userType\":\"2\"}"
                //val json = "{\"agentIdcardImgBehind\":\"/data/data/com.sc.clgg/cache/takephoto_cache/-5774e1b6b7cf7a24.jpg\",\"agentIdcardImgFront\":\"/data/data/com.sc.clgg/cache/takephoto_cache/3d1d1d950cbe8fb7.jpg\",\"agentName\":\"..\",\"agentPhone\":\"110\",\"businessLicenseImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/-5f3a40f0b7504488.jpg\",\"cardType\":\"3\",\"certSn\":\"..\",\"certType\":\"203\",\"etcCardApplyVehicleVoList\":[{\"address\":\"江苏省淮安市清浦区武墩街20号\",\"axleNumber\":\"\",\"carColor\":\"蓝\",\"carNo\":\"..\",\"carNoColor\":\"蓝\",\"carNoImageId\":\"d5f038359b21466489f2e9c37c6756b8\",\"carOwner\":\"梁昊\",\"carType\":\"一型货车\",\"engineNumber\":\"C91320535\",\"function\":\"营业货车\",\"model\":\"雪佛兰牌SGM7140MTB\",\"tyreNumber\":\"\",\"vehicleFrontImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/-6975c7b0d1ae3f68.jpg\",\"vehicleImageId\":\"000b257698b7402fafc4ea6791c6725a\",\"vehicleLicenseImg\":\"/data/data/com.sc.clgg/cache/takephoto_cache/4486d5f5c2a6e6fb.jpg\",\"vehicleType\":\"小型轿车\",\"vinCode\":\"LSGSA52M6DD092219\"}],\"idcardImgBehind\":\"\",\"idcardImgFront\":\"\",\"invitationCode\":\"\",\"linkMobile\":\"\",\"recipientsAddress\":\"北京 北京市 东城区.....\",\"recipientsName\":\"..\",\"recipientsPhone\":\"11111111111\",\"userName\":\"\",\"userType\":\"2\"}"

                /*RetrofitHelper().apply(Gson().fromJson(json, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>, t: Throwable) {
                        LogHelper.e("onFailure   ${t.message}")
                    }

                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                        LogHelper.e("onResponse   ${response.body().toString()}")
                    }
                })*/

                job = GlobalScope.launch {
                    val deffered = async { RetrofitHelper().area.execute() }

                    val http = deffered.await()

                    withContext(Dispatchers.Main) {
                        if (http.isSuccessful) {
                            toast("成功")
                        } else {
                            LogHelper.e("失败 is ${http.errorBody().toString()}")
                            toast(R.string.network_anomaly)
                        }
                    }
                }
                job.start()

            }
            2 -> {
                Thread {
                    Looper.prepare()
                    var ts = Toast.makeText(this, "", Toast.LENGTH_SHORT)
                    ts.view = Button(this).apply { text = "button";backgroundColor=Color.GREEN }
                    ts.show()
                    Looper.loop()
                }.start()
            }
            else -> {
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }

    }

    private lateinit var job: Job

}
