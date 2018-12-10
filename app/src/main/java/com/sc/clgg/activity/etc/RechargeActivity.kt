package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_recharge.*
import kotlinx.android.synthetic.main.view_titlebar.*

/*ETC 充值圈存*/
class RechargeActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
        titlebar_title.text = getString(R.string.etc_czqc)

        v_1.setOnClickListener { startActivity(Intent(this@RechargeActivity, ReadCardActivity::class.java)) }
        v_3.setOnClickListener { startActivity(MyCardActivity::class.java) }
    }
}
