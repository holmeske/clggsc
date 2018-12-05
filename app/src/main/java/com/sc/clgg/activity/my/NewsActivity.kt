package com.sc.clgg.activity.my

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sc.clgg.R
import com.sc.clgg.adapter.MessageAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Message
import com.sc.clgg.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_news.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : BaseImmersionActivity() {
    var adapter: MessageAdapter? = null
    private var pageNo = 1
    private val pageSize = 20
    private var position = "0"
    private var noMore: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initTitle(intent.getStringExtra("title"))
        position = intent.getStringExtra("position")

        adapter = MessageAdapter()
        if (position == "1") {
            adapter?.webDetailTitle = "新闻资讯"
        }
        if (position == "11") {
            adapter?.webDetailTitle = "活动公告"
        }

        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv.adapter = adapter

        swipeRefreshLayout?.setOnRefreshListener(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
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
                    if (it.data?.rows.isNullOrEmpty()||it.data?.rows?.size==0) {
                        tv_nomessage.visibility = View.VISIBLE
                        return
                    } else {
                        tv_nomessage.visibility = View.GONE
                    }

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
