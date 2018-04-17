package com.sc.clgg.activity.vehiclemanager.drivingscore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.OnClickListener
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.DrivingScoreAdapter
import com.sc.clgg.bean.DrivingScoreBean
import com.sc.clgg.config.NetField
import com.sc.clgg.http.ParamsHelper
import com.sc.clgg.http.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_drive_score.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tool.helper.LogHelper


/**
 * 驾驶评分
 *
 * @author lvke
 */
class DrivingScoreActivity : AppCompatActivity(), OnClickListener {

    private var adapter: DrivingScoreAdapter? = null
    private val datas = arrayOf("上周", "本周", "本月")
    private var currentIndex = 1
    private var timeLine = "02"
    private var call: Call<DrivingScoreBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_drive_score)
        title = getString(R.string.driving_score)
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        adapter = DrivingScoreAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter

        triangle_left?.setOnClickListener(this)
        triangle_right?.setOnClickListener(this)

        tv_count_date?.text = datas[currentIndex]

        retrofit(datas[currentIndex])

    }

    private fun retrofit(date: String) {
        when (date) {
            "上周" -> timeLine = "01"
            "本周" -> timeLine = "02"
            "本月" -> timeLine = "03"
        }
        progressBar.visibility = View.VISIBLE
        call = RetrofitHelper()
                .init(NetField.SITE)
                .drivingScore(ParamsHelper.drivingScore(timeLine))

        call?.enqueue(object : Callback<DrivingScoreBean> {
            override fun onResponse(call: Call<DrivingScoreBean>, response: Response<DrivingScoreBean>) {
                progressBar.visibility = View.INVISIBLE

                LogHelper.e("""onResponse()  ${call.request().url()}  ${response.isSuccessful}
                         ${response.headers()}""")

                response.body().let {
                    LogHelper.e("DrivingScoreBean == " + Gson().toJson(it))
                    adapter?.refresh(it?.list, timeLine)
                }

            }

            override fun onFailure(call: Call<DrivingScoreBean>, t: Throwable) {
                LogHelper.e("onFailure() ${t.message}")

                progressBar.visibility = View.INVISIBLE
            }

        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.triangle_left -> {
                if (currentIndex == 0) return

                currentIndex--
                tv_count_date?.text = datas[currentIndex]
                retrofit(datas[currentIndex])
            }

            R.id.triangle_right -> {
                if (currentIndex == 2) return

                currentIndex++
                tv_count_date?.text = datas[currentIndex]
                retrofit(datas[currentIndex])
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!call?.isCanceled!!) call?.cancel()
    }
}
