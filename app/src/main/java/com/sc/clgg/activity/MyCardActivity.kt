package com.sc.clgg.activity

import android.os.Bundle
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.MyCardAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Card
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.view_titlebar.*

class MyCardActivity : BaseImmersionActivity() {
    private var adapter: MyCardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)


    }

    override fun onResume() {
        super.onResume()
        Looper.myQueue().addIdleHandler {
            initView()

            false
        }
    }

    private fun initView() {
        titlebar_title.text = getString(R.string.my_etc_card)

        iv_car_number.setOnClickListener { }
        iv_car_type.setOnClickListener { }
        recyclerView.adapter = MyCardAdapter().apply {
            adapter = this
            var list = arrayListOf<Card>()
            for (i in 1..3) {
                var card = Card("鲁通A卡", "6217 6677 1118 999", "陕A123456")
                list.add(card)
            }
            adapter?.refresh(list)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)


        tv_apply_state.setOnClickListener { startActivity(ApplyStateActivity::class.java) }
    }
}
