package com.sc.clgg.activity.my.userinfo

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_nick_name.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.textColor

class NickNameActivity : BaseImmersionActivity() {
    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        et.filters = arrayOf(InputFilter.LengthFilter(intent.getIntExtra("maxLength", 20)))

        type = intent.getStringExtra("cardType")

        when {
            type.equals("name") -> {
                initTitle("设置姓名")
                et.setText(intent.getStringExtra("name"))
            }
            type.equals("nickname") -> {
                initTitle("设置昵称")
                et.setText(intent.getStringExtra("nickname"))
            }
            type.equals("signature") -> {
                initTitle("设置签名")
                et.setText(intent.getStringExtra("signature"))
            }
            type.equals("invite") -> {
                initTitle("设置邀请码")
                et.keyListener = DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                et.setText(intent.getStringExtra("invite"))
            }
        }

        titlebar_left.visibility = View.GONE
        titlebar_left_text.text = "取消"
        titlebar_right.text = "完成"

        titlebar_right.textColor = Color.parseColor("#93afff")

        titlebar_left_text.setOnClickListener {
            finish()
        }
        titlebar_right.setOnClickListener {
            when {
                type.equals("name") -> PersonalDataActivity.DATA?.realName = et.text.toString()
                type.equals("nickname") -> PersonalDataActivity.DATA?.nickName = et.text.toString()
                type.equals("signature") -> PersonalDataActivity.DATA?.clientSign = et.text.toString()
                type.equals("invite") -> PersonalDataActivity.DATA?.inviteCode = et.text.toString()
            }
            finish()
        }

        et.setTextChangeListener {
            titlebar_right.textColor = Color.parseColor("#ffffff")
        }

        iv_deleteall.setOnClickListener { et.setText("") }
    }

}
