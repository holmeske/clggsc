package com.sc.clgg.activity.etc

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.ApplyStateAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.ApplyStateList
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_apply_state.*
import kotlinx.android.synthetic.main.view_titlebar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyStateActivity : BaseImmersionActivity() {
    private var mApplyStateAdapter = ApplyStateAdapter()
    private var dataList = ArrayList<ApplyStateList.ApplyState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_state)

        titlebar_title.text = getString(R.string.apply_state_query)
        titlebar_right?.let { it.visibility = View.VISIBLE; it.text = "全部";it.setOnClickListener { mApplyStateAdapter.refresh(dataList);tv_apply_state?.text = "" } }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mApplyStateAdapter
        loadData()

        tv_apply_state.setOnClickListener {
            val data = listOf("审核中", "审核驳回", "审核通过", "开卡成功", "开卡失败", "卡片寄出")
            PickerViewHelper().creat(this, data)
            { options1, _, _, _ ->
                tv_apply_state.text = data[options1]
                when (options1) {
                    0 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.checkStatus == "0" })
                    }
                    1 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.checkStatus == "2" })
                    }
                    2 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.isSuccess == "0" })
                    }
                    3 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.isSuccess == "1" && it.expressInfo.isNullOrEmpty() })
                    }
                    4 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.isSuccess == "2" })
                    }
                    else -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.isSuccess == "1" && !it.expressInfo.isNullOrEmpty() })
                    }
                }
            }
        }
    }

    private var http: Call<ApplyStateList>? = null
    private fun loadData() {
        showProgressDialog()
        http = RetrofitHelper().applyStateList.apply {
            enqueue(object : Callback<ApplyStateList> {
                override fun onFailure(call: Call<ApplyStateList>, t: Throwable) {
                    hideProgressDialog()
                }

                override fun onResponse(call: Call<ApplyStateList>, response: Response<ApplyStateList>) {
                    hideProgressDialog()
                    response.body()?.etcCardApplyOpenList?.let {
                        mApplyStateAdapter.clear()
                        mApplyStateAdapter.notifyItemInserted(it)
                        dataList = it
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }
}
