package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_recharge.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class RechargeActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
        titlebar_title.text = getString(R.string.etc_czqc)

        v_1.onClick { startActivity(Intent(this@RechargeActivity,ReadCardActivity::class.java)) }
    }
}
