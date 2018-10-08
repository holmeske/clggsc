package com.sc.clgg.activity.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.sc.clgg.R
import com.sc.clgg.adapter.ETCAdapter
import kotlinx.android.synthetic.main.fragment_etc.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*

class ETCFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_etc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titlebar_left.visibility = View.GONE
        titlebar_title.text = "远行通"
        ll_a.findViewById<TextView>(R.id.tv_title).text = "A卡是什么？"
        ll_a.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        ll_b.findViewById<TextView>(R.id.tv_title).text = "B卡是什么？"
        ll_b.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        ll_c.findViewById<TextView>(R.id.tv_title).text = "退卡、销卡、补卡等业务咨询"
        ll_c.findViewById<TextView>(R.id.tv_content).text = "我是内容，我是内容，我是内容..."

        recyclerView.adapter = ETCAdapter()
        recyclerView.layoutManager = GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)


    }

}
