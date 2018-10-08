package com.sc.clgg.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sc.clgg.R
import com.sc.clgg.adapter.MessageAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Message
import com.sc.clgg.http.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_message.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageActivity : BaseImmersionActivity() {
    var adapter: MessageAdapter? = null
    private var pageNo = 1
    private val pageSize = 20
    private var position = "0"
    private var noMore: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        initTitle(intent.getStringExtra("title"))
        position = intent.getStringExtra("position")

        adapter = MessageAdapter()
        if (position.equals("1")) {
            adapter?.webDetailTitle = "新闻资讯"
        }
        if (position.equals("11")) {
            adapter?.webDetailTitle = "活动公告"
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        swipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            noMore = false
            pageNo = 1
            loadData(position, pageNo, pageSize)
        })

        adapter?.setLoadMoreListener(View.OnClickListener { view ->
            (view as TextView).text = "加载中..."
            if (!noMore) {
                pageNo++
                loadData(position, pageNo, pageSize)
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_more), Toast.LENGTH_SHORT).show()
            }
        })
        loadData(position, pageNo, pageSize)
    }

    private var call: Call<Message>? = null

    private fun loadData(position: String?, pageNo: Int, pageSize: Int) {
        swipeRefreshLayout?.isRefreshing = true
        call = RetrofitHelper().messages(position, pageNo.toString(), pageSize.toString())
        call?.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                swipeRefreshLayout?.isRefreshing = false

                adapter?.refreshLast(0)
            }

            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                swipeRefreshLayout?.isRefreshing = false

                response?.body()?.let {
                    if (it.success) {
                        if (pageNo == 1) {
                            adapter?.clear()
                        }
                        adapter?.refresh(it.data?.rows, it.data?.rows?.size!! < pageSize)
                    } else {
                        it.msg?.let {
                            toast(it)
                        }
                    }
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

}
