package com.sc.clgg.activity.etc

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.ApplyStateNewAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.ApplyStateListBean
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_apply_state.*
import kotlinx.android.synthetic.main.view_titlebar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyStateNewActivity : BaseImmersionActivity() {
    private var mApplyStateAdapter = ApplyStateNewAdapter()
    private var dataList = ArrayList<ApplyStateListBean.A>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_state)

        titlebar_title.text = getString(R.string.apply_state_query)
        titlebar_right?.let {
            it.visibility = View.VISIBLE; it.text = "全部";it.setOnClickListener {
            mApplyStateAdapter.refresh(dataList);tv_apply_state?.text = ""
        }
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mApplyStateAdapter
        loadData()

        tv_apply_state.setOnClickListener {
            val data = listOf("新增", "审核成功", "审核失败", "开卡成功", "开卡失败", "接口调用失败，请重试a")
            PickerViewHelper().creat(this, data)
            { options1, _, _, _ ->
                tv_apply_state.text = data[options1]
                when (options1) {
                    0 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "0" })
                    }
                    1 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "1" })
                    }
                    2 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "2" })
                    }
                    3 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "3" })
                    }
                    4 -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "4" })
                    }
                    else -> {
                        mApplyStateAdapter.refresh(dataList.filter { it.status == "5" })
                    }
                }
            }
        }
    }

    private var http: Call<ApplyStateListBean>? = null

    private fun loadData() {
        showProgressDialog()
        http = RetrofitHelper().cardList_icbc().apply {
            enqueue(object : Callback<ApplyStateListBean> {
                override fun onFailure(call: Call<ApplyStateListBean>, t: Throwable) {
                    hideProgressDialog()
                }

                override fun onResponse(call: Call<ApplyStateListBean>, response: Response<ApplyStateListBean>) {
                    hideProgressDialog()
                    response.body()?.rows?.let {
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
