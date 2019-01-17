package com.sc.clgg.activity

import android.content.Intent
import com.sc.clgg.activity.etc.ble.BleActivity
import com.sc.clgg.activity.etc.opencard.InfoCertificationActivity
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

    private fun init() {
        val v = 0
        when (v) {
            1 -> {
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
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }
    }

}
