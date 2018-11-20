package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.dialog.PreRechargeHintDialog
import kotlinx.android.synthetic.main.activity_pre_recharge.*
import kotlinx.android.synthetic.main.view_titlebar.*

class PreRechargeActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_recharge)

        init()
    }

    private fun init() {
        titlebar_title.text = getString(R.string.pre_recharge)

        v_1.setOnClickListener { PreRechargeHintDialog(this@PreRechargeActivity).apply { show();setData(100.0)
        setCancelListener { dismiss()}} }
        v_2.setOnClickListener { }
    }
}
