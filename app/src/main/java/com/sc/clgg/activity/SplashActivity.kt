package com.sc.clgg.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.google.gson.Gson
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.activity.etc.opencard.CarCertificationActivity
import com.sc.clgg.activity.etc.opencard.InfoCertificationActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
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
        val v = 0
        when (v) {
            1 -> {
                var gson ="{\n" +
                        "  \"agentIdcardImgBehind\": \"\",\n" +
                        "  \"agentIdcardImgFront\": \"\",\n" +
                        "  \"agentName\": \"\",\n" +
                        "  \"agentPhone\": \"\",\n" +
                        "  \"businessLicenseImg\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/1540376781532.jpg\",\n" +
                        "  \"cardType\": \"3\",\n" +
                        "  \"certSn\": \"\",\n" +
                        "  \"certType\": \"\",\n" +
                        "  \"etcCardApplyVehicleVoList\": [\n" +
                        "    {\n" +
                        "      \"address\": \"\",\n" +
                        "      \"axleNumber\": \"\",\n" +
                        "      \"carColor\": \"\",\n" +
                        "      \"carLicenseType\": \"\",\n" +
                        "      \"carNo\": \"\",\n" +
                        "      \"carNoColor\": \"\",\n" +
                        "      \"carNoImageId\": \"29038823318c45718e4c1cec9a57e6e9\",\n" +
                        "      \"carOwner\": \"\",\n" +
                        "      \"carOwnerCertificateNumber\": \"\",\n" +
                        "      \"carOwnerCertificateType\": \"\",\n" +
                        "      \"carType\": \"\",\n" +
                        "      \"engineNumber\": \"\",\n" +
                        "      \"function\": \"\",\n" +
                        "      \"model\": \"\",\n" +
                        "      \"tyreNumber\": \"\",\n" +
                        "      \"vehicleFrontImg\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/1540376781532.jpg\",\n" +
                        "      \"vehicleImageId\": \"4ce3bab240e14186abe6a8799a0bff22\",\n" +
                        "      \"vehicleLicenseImg\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/IMG20181212113028.jpg\",\n" +
                        "      \"vinCode\": \"\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"address\": \"\",\n" +
                        "      \"axleNumber\": \"\",\n" +
                        "      \"carColor\": \"\",\n" +
                        "      \"carLicenseType\": \"\",\n" +
                        "      \"carNo\": \"\",\n" +
                        "      \"carNoColor\": \"\",\n" +
                        "      \"carNoImageId\": \"90e56485fbc04d0a9b09537d4bbe4ccf\",\n" +
                        "      \"carOwner\": \"\",\n" +
                        "      \"carOwnerCertificateNumber\": \"\",\n" +
                        "      \"carOwnerCertificateType\": \"\",\n" +
                        "      \"carType\": \"\",\n" +
                        "      \"engineNumber\": \"\",\n" +
                        "      \"function\": \"\",\n" +
                        "      \"model\": \"\",\n" +
                        "      \"tyreNumber\": \"\",\n" +
                        "      \"vehicleFrontImg\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/1540376789719.jpg\",\n" +
                        "      \"vehicleImageId\": \"63b0810aa8364f2e89b98df535786729\",\n" +
                        "      \"vehicleLicenseImg\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/06.jpg\",\n" +
                        "      \"vinCode\": \"\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"idcardImgBehind\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/1540376781532.jpg\",\n" +
                        "  \"idcardImgFront\": \"/data/user/0/com.sc.clgg/cache/takephoto_cache/IMG20190107172209.jpg\",\n" +
                        "  \"invitationCode\": \"\",\n" +
                        "  \"linkMobile\": \"\",\n" +
                        "  \"recipientsAddress\": \"\",\n" +
                        "  \"recipientsName\": \"\",\n" +
                        "  \"recipientsPhone\": \"\",\n" +
                        "  \"userName\": \"\",\n" +
                        "  \"userType\": \"2\",\n" +
                        "  \"verificationCode\": \"\"\n" +
                        "}"

                /*RetrofitHelper().apply(Gson().fromJson(gson, CertificationInfo::class.java)).enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<Check>, response: Response<Check>) {
                    }
                })*/
                startActivity(Intent(this, InfoCertificationActivity::class.java).putExtra("info", Gson().fromJson(gson, CertificationInfo::class.java)))
finish()
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
