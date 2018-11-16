package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.TruckCircleMessageAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.NoReadInfo
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_truck_circle_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TruckCircleMessageActivity : BaseImmersionActivity() {
    private var adapter: TruckCircleMessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_circle_message)
        initTitle("卡友圈互动")

        adapter = TruckCircleMessageAdapter()
        recyclerView.layoutManager=  LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogHelper.e("requestCode=$requestCode  resultCode=$resultCode  data=$data  ")
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private var call: Call<NoReadInfo>? = null
    private fun loadData() {
        call = RetrofitHelper().noReadInfo()
        call?.enqueue(object : Callback<NoReadInfo> {
            override fun onFailure(call: Call<NoReadInfo>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<NoReadInfo>?, response: Response<NoReadInfo>?) {
                response?.body()?.let {
                    if (it.allNotReadInfo.isNullOrEmpty()||it.allNotReadInfo?.size==0) {
                        tv_nomessage.visibility = View.VISIBLE
                        return
                    } else {
                        tv_nomessage.visibility = View.GONE
                    }
                    adapter?.refresh(it.allNotReadInfo)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }
}
