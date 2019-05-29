package com.sc.clgg.activity.vehicle.fault

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.FaultDetailAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.FaultDetail
import com.sc.clgg.dialog.UndevelopedHintDialog
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
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv.adapter = adapter
        LogHelper.e("传递的数据 = " + Gson().toJson(data))
        LogHelper.e("传递的数据大小 = " + data?.size)
        adapter?.refresh(data)

        if (data?.size == 0) {
            val dialog = UndevelopedHintDialog(this@FaultDetailActivity)
            dialog.show()
            dialog.findViewById<View>(R.id.tv_1)?.let {
                it as TextView
                it.setCompoundDrawablesWithIntrinsicBounds(null,
                        ContextCompat.getDrawable(this@FaultDetailActivity, R.drawable.diagnosis_popup_icon), null, null)
                it.text = getString(R.string.s_1)
            }
            dialog.findViewById<View>(R.id.tv_2)?.run {
                this as TextView
                text = "我知道了"
                setOnClickListener { dialog.dismiss() }
            }
            dialog.setOnDismissListener { finish() }
        }
    }


}
