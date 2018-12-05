package com.sc.clgg.activity

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.os.Bundle
import android.os.Looper
import android.text.Spannable
import com.sc.clgg.R
import com.sc.clgg.activity.fragment.CircleDepositRecordFragment
import com.sc.clgg.activity.fragment.TrafficDetailFragment
import com.sc.clgg.adapter.TabAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.tool.helper.TextHelper
import kotlinx.android.synthetic.main.activity_card_detail.*
import kotlinx.android.synthetic.main.view_titlebar.*

class CardDetailActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        init()
    }

    override fun onResume() {
        super.onResume()
        //Looper.myQueue().addIdleHandler { init();false }
    }

    private fun init() {
        titlebar_title.text = "ETC卡详情"

        tv_card_balance.text = customText("卡内余额: ", "100.00元")
        tv_card_can_write.text = customText("可写卡余额: ", "100.00元")

        tabLayout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit=3;
        viewPager?.adapter = TabAdapter(supportFragmentManager,
                listOf(TrafficDetailFragment(), CircleDepositRecordFragment(), CircleDepositRecordFragment()),
                listOf("通行明细", "圈存记录", "充值圈存记录"))
    }

    private fun customText(s1: String, s2: String): Spannable {
        return TextHelper.instance.setSpannable(this,
                arrayOf(s1, s2),
                intArrayOf(R.color.black_333, R.color._ff0000),
                intArrayOf(13, 13),
                intArrayOf(NORMAL, BOLD))
    }
}
