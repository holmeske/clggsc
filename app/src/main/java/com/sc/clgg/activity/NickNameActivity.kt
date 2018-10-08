package com.sc.clgg.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_nick_name.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

class NickNameActivity : BaseImmersionActivity() {
    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        type = intent.getStringExtra("type")

        if (type.equals("name")) {
            initTitle("设置姓名")

            et.setText(intent.getStringExtra("name"))

        } else if (type.equals("nickname")) {
            initTitle("设置昵称")
            et.setText(intent.getStringExtra("nickname"))
        } else if (type.equals("signature")) {
            initTitle("设置签名")
            et.setText(intent.getStringExtra("signature"))
        }
        titlebar_left.visibility = View.GONE
        titlebar_left_text.text = "取消"
        titlebar_right.text = "完成"

        titlebar_right.textColor = Color.parseColor("#93afff")

        titlebar_left_text.onClick {
            finish()
        }
        titlebar_right.onClick {
            if (type.equals("name")) {
                PersonalDataActivity.DATA?.realName = et.text.toString()
            } else if (type.equals("nickname")) {
                PersonalDataActivity.DATA?.nickName = et.text.toString()
            } else if (type.equals("signature")) {
                PersonalDataActivity.DATA?.clientSign = et.text.toString()
            }
            finish()
        }

        et.setTextChangeListener {
            titlebar_right.textColor = Color.parseColor("#ffffff")
        }

        iv_deleteall.onClick { et.setText("") }
    }

}
