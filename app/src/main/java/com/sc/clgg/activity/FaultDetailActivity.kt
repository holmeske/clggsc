package com.sc.clgg.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.FaultDetailAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.FaultDetail
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_fault_detail.*

class FaultDetailActivity : BaseImmersionActivity() {
    private var adapter: FaultDetailAdapter? = null
    private var data: List<FaultDetail>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fault_detail)

        initTitle("故障详情")
        data = intent.getParcelableArrayListExtra("data")

        adapter = FaultDetailAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        LogHelper.e("传递的数据 = "+Gson().toJson(data))
        LogHelper.e("传递的数据大小 = "+data?.size)
        adapter?.refresh(data)
    }


}
