package com.sc.clgg.activity.nocar

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.view_titlebar.*

class DrivingLicenseActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driving_license)

        initView()
    }

    private fun initView() {
         titlebar_title.text ="驾驶证认证"
    }

}
