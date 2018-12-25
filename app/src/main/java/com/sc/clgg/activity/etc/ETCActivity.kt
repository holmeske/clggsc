package com.sc.clgg.activity.etc

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sc.clgg.R
import com.sc.clgg.adapter.ETCAdapter
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_etc.*
import kotlinx.android.synthetic.main.view_titlebar.*

class ETCActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etc)

        /*val dialog = UndevelopedHintDialog(this@ETCActivity)
        dialog.show()
        dialog.findViewById<View>(R.id.iv_2).setOnClickListener { dialog.dismiss() }
        dialog.setOnDismissListener { finish() }*/
        init()
    }

    private fun init() {
        titlebar_left.visibility = View.GONE
        titlebar_title.text = "远行通"
        ll_a.findViewById<TextView>(R.id.tv_title).text = "A卡是什么？"
        ll_a.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        ll_b.findViewById<TextView>(R.id.tv_title).text = "B卡是什么？"
        ll_b.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        ll_c.findViewById<TextView>(R.id.tv_title).text = "退卡、销卡、补卡等业务咨询"
        ll_c.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        recyclerView.adapter = ETCAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
    }
}
