package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import kotlinx.android.synthetic.main.activity_card_introduce.*
import kotlinx.android.synthetic.main.view_titlebar.*

class CardIntroduceActivity : BaseImmersionActivity() {
    private var certificationInfo = CertificationInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_introduce)
        titlebar_title.text = "鲁通A卡和B卡的区别"
        tv_apply_a.setOnClickListener {
            certificationInfo.cardType = "2"
            next()
        }
        tv_apply_b.setOnClickListener {
            certificationInfo.cardType = "3"
            next()
        }
    }

    private fun next() {
        startActivity(Intent(this, IdentityCertificationActivity::class.java).putExtra("info", certificationInfo))
    }
}
