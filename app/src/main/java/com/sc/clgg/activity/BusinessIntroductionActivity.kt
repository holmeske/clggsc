package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_business_introduction.*
import kotlinx.android.synthetic.main.view_titlebar.*

class BusinessIntroductionActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_introduction)

        titlebar_title.text="业务介绍"


        tv_title.text=intent.getStringExtra("title")
        tv_text.text=intent.getStringExtra("text")
    }
}
