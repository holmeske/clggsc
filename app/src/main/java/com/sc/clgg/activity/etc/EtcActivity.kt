package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sc.clgg.R
import com.sc.clgg.activity.BusinessIntroductionActivity
import com.sc.clgg.adapter.EtcAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.BusinessNoteList
import com.sc.clgg.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_etc.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EtcActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etc)

        init()
        loadData()
    }

    private fun init() {
        titlebar_title.text = "远行通"

        recyclerView.adapter = EtcAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
    }

    private fun loadData() {
        RetrofitHelper().getEtcBusinessNote().enqueue(object : Callback<BusinessNoteList> {
            override fun onFailure(call: Call<BusinessNoteList>, t: Throwable) {
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<BusinessNoteList>, response: Response<BusinessNoteList>) {
                response.body()?.etcBusinessNoteList?.run {
                    ll_a.findViewById<TextView>(R.id.tv_title).text = get(0).title
                    ll_a.findViewById<TextView>(R.id.tv_content).text = get(0).text

                    ll_a.setOnClickListener {
                        startActivity(Intent(this@EtcActivity, BusinessIntroductionActivity::class.java)
                                .putExtra("title", get(0)?.title).putExtra("text", get(0)?.text))
                    }

                    ll_b.findViewById<TextView>(R.id.tv_title).text = get(1)?.title
                    ll_b.findViewById<TextView>(R.id.tv_content).text = get(1)?.text

                    ll_b.setOnClickListener {
                        startActivity(Intent(this@EtcActivity, BusinessIntroductionActivity::class.java)
                                .putExtra("title", get(1)?.title)
                                .putExtra("text", get(1)?.text))
                    }

                    ll_c.findViewById<TextView>(R.id.tv_title).text = get(2)?.title
                    ll_c.findViewById<TextView>(R.id.tv_content).text = get(2)?.text

                    ll_c.setOnClickListener {
                        startActivity(Intent(this@EtcActivity, BusinessIntroductionActivity::class.java)
                                .putExtra("title", get(2)?.title)
                                .putExtra("text", get(2)?.text))
                    }
                }
            }
        })
    }
}
