package com.sc.clgg.activity.vehiclemanager.myvehicle

import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.adapter.MyVehicleAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Vehicle
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.ActivityHelper
import kotlinx.android.synthetic.main.activity_my_vehicle.*
import kotlinx.android.synthetic.main.view_titlebar.*
import retrofit2.Call
import retrofit2.Response

/**
 * 我的车辆
 * @author lvke
 */
class MyVehicleActivity : BaseImmersionActivity() {
    private var adapter: MyVehicleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicle)

        initTitle("我的车辆")
        titlebar_right.text = "添加车辆"
        titlebar_right.setOnClickListener { ActivityHelper.startAcScale(this@MyVehicleActivity, AddVehicleActivity::class.java) }

        adapter = MyVehicleAdapter()
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv.adapter = adapter
        adapter?.setCallbackListener { if (adapter?.dataList?.size == 0) tv_nocar.visibility = View.VISIBLE else tv_nocar.visibility = View.GONE }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private var http: Call<Vehicle>? = null

    private fun loadData() {
        http = RetrofitHelper().myVehicle()
        http?.enqueue(object : retrofit2.Callback<Vehicle> {
            override fun onFailure(call: Call<Vehicle>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Vehicle>?, response: Response<Vehicle>?) {
                response?.body()?.let {
                    if (it.vehicleInfoList?.isNullOrEmpty()!!) {
                        tv_nocar.visibility = View.VISIBLE
                    } else {
                        tv_nocar.visibility = View.GONE
                        adapter?.refresh(it.vehicleInfoList)
                    }
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }

}
