package com.sc.clgg.activity.basic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.sc.clgg.R
import com.sc.clgg.util.ConfigUtil

class LaunchActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            init()
        }
    }

    private fun init() {
        startActivity(Intent(this, if (ConfigUtil().userid.isNotEmpty()) MainActivity::class.java else LoginActivity::class.java))
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out)
        finish()
    }

}
