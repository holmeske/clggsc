package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startActivity(Intent(this, LaunchActivity::class.java))
//        finish()

        startActivity(Intent(this, VehicleCertificationActivity::class.java))

    }
}
