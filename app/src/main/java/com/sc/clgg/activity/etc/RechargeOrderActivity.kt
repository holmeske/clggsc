package com.sc.clgg.activity.etc

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.RechargeOrderAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.RechargeOrderList
import com.sc.clgg.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_recharge_order.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RechargeOrderActivity : BaseImmersionActivity() {
    private var adapter = RechargeOrderAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_order)

        init()
    }

    private fun init() {
        titlebar_title.text = "充值订单"
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        RetrofitHelper().rechargeOrderList.enqueue(object : Callback<RechargeOrderList> {
            override fun onFailure(call: Call<RechargeOrderList>, t: Throwable) {
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<RechargeOrderList>, response: Response<RechargeOrderList>) {
                response.body()?.run {
                    if (success) {
                        payOrdersVoList?.let {
                            if (it.isNullOrEmpty()) {
                                tv_no_data.visibility = View.VISIBLE
                            } else {
                                tv_no_data.visibility = View.GONE
                            }
                            adapter.notifyItemInserted(it)
                        }
                    } else {
                        toast("$msg")
                        tv_no_data.visibility = View.VISIBLE
                    }
                }
            }
        })
    }


}
