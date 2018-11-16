package com.sc.clgg.activity

import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_gender.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*

class GenderActivity : BaseImmersionActivity() {
    private var g: String? = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)
        initTitle("设置性别")
        titlebar_left.visibility = View.GONE
        titlebar_left_text.text = "取消"
        titlebar_right.text = "完成"

        titlebar_left_text.setOnClickListener {
            finish()
        }
        titlebar_right.setOnClickListener {
            PersonalDataActivity.DATA?.gender = g!!
            finish()
        }
        intent.getStringExtra("gender").let {
            if (it == "1") {
                iv_female.visibility = View.GONE
                iv_male.visibility = View.VISIBLE
            } else if (it == "0") {
                iv_male.visibility = View.GONE
                iv_female.visibility = View.VISIBLE
            }
        }


        tv_male.setOnClickListener {
            iv_female.visibility = View.GONE
            iv_male.visibility = View.VISIBLE
            g = "1"
        }
        tv_female.setOnClickListener {
            iv_male.visibility = View.GONE
            iv_female.visibility = View.VISIBLE
            g = "0"
        }

    }


}
