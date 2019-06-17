package com.sc.clgg.activity.nocar

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_identity.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity

class IdentityActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity)

        initView()
        setListener()
    }

    private fun initView() {
        titlebar_title.text = "身份认证"
        tv_driver.isSelected = true
    }

    private fun setListener() {
        ll_driver.setOnClickListener {
            tv_driver.isSelected = true
            v_driver.backgroundColor = ContextCompat.getColor(this, R.color._ee8031)

            tv_shipper.isSelected = false
            v_shipper.backgroundColor = ContextCompat.getColor(this, R.color._ccc)
        }
        ll_shipper.setOnClickListener {
            tv_driver.isSelected = false
            v_driver.backgroundColor = ContextCompat.getColor(this, R.color._ccc)

            tv_shipper.isSelected = true
            v_shipper.backgroundColor = ContextCompat.getColor(this, R.color._ee8031)
        }
        tv_next.setOnClickListener {
            startActivity<DrivingLicenseActivity>()
        }
    }
}
