package com.sc.clgg.activity.vehiclemanager.myvehicle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.MyVehicleAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Vehicle
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.ActivityHelper
import kotlinx.android.synthetic.main.activity_my_vehicle.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick
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
        titlebar_right.onClick { ActivityHelper.startAcScale(this@MyVehicleActivity, AddVehicleActivity::class.java) }

        adapter = MyVehicleAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private var call: Call<Vehicle>? = null

    private fun loadData() {
        call = RetrofitHelper().myVehicle()
        call?.enqueue(object : retrofit2.Callback<Vehicle> {
            override fun onFailure(call: Call<Vehicle>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Vehicle>?, response: Response<Vehicle>?) {
                response?.body()?.vehicleInfoList?.let { adapter?.refresh(it) }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

}
