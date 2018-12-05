package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.DialogHelper
import kotlinx.android.synthetic.main.activity_repair.*


class RepairActivity : BaseImmersionActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair)
        initTitle("一键报修")
        call_hotline.setOnClickListener {
            val list = arrayListOf("029-86955159", "029-86955858")
            DialogHelper.showTelDialog(this@RepairActivity, list)
        }
    }

}
