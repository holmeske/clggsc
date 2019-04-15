package com.sc.clgg.activity.etc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sc.clgg.R
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_bind_card_result.*

class BindCardResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_card_result)

        val result = intent.getIntExtra("result", -1)
        if (result == 1) {
            iv.setImageResource(R.drawable.card_success)
            tv.text = "绑卡成功"
        } else {
            iv.setImageResource(R.drawable.card_fail)
            tv.text = "绑卡失败请稍后再试"
        }
        tv_back_home.setOnClickListener { startActivity(EtcActivity::class.java) }
    }
}
