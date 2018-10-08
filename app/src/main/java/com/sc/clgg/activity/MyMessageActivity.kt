package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.IsNotReadInfo
import com.sc.clgg.http.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_my_message.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyMessageActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_message)

        initTitle("我的消息")

        v_1.onClick { startActivity(Intent(this@MyMessageActivity, TruckCircleMessageActivity::class.java)) }
        v_2.onClick {
            startActivity(Intent(this@MyMessageActivity, MessageActivity::class.java)
                    .putExtra("title", "新闻资讯").putExtra("position", "1"))
        }
        v_3.onClick {
            startActivity(Intent(this@MyMessageActivity, MessageActivity::class.java)
                    .putExtra("title", "活动公告").putExtra("position", "11"))
        }


    }

    override fun onResume() {
        super.onResume()
        isNotReadInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    private var call: Call<IsNotReadInfo>? = null

    private fun isNotReadInfo() {
        call = RetrofitHelper().isNotReadInfo
        call?.enqueue(object : Callback<IsNotReadInfo> {
            override fun onFailure(call: Call<IsNotReadInfo>?, t: Throwable?) {
                v_point1.visibility = View.GONE
                v_point2.visibility = View.GONE
                v_point3.visibility = View.GONE
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<IsNotReadInfo>?, response: Response<IsNotReadInfo>?) {
                response?.body()?.let {
                    v_point1.visibility = if (it.DriverCircleType) View.VISIBLE else View.GONE

                    v_point2.visibility = if (it.News) View.VISIBLE else View.GONE

                    v_point3.visibility = if (it.Activities) View.VISIBLE else View.GONE
                }
            }
        })
    }
}
