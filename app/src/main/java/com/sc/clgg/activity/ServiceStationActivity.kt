package com.sc.clgg.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sc.clgg.R
import com.sc.clgg.activity.basic.WebActivity
import com.sc.clgg.activity.contact.ItemClickListener
import com.sc.clgg.adapter.ServiceStationAdapter
import com.sc.clgg.application.CURRENT_LOCATION
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.ServiceStation
import com.sc.clgg.config.NetField
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.RecycleViewHelper
import com.sc.clgg.util.statusBarHeight
import com.sc.clgg.widget.AreaPopHelper
import kotlinx.android.synthetic.main.activity_service_station.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceStationActivity : BaseImmersionActivity() {
    private var tabs: List<TextView>? = null
    private var tabUnderlines: List<View>? = null
    private var adapter: ServiceStationAdapter? = null
    private var pageNo = 1
    private val pageSize = 5
    //0-按位置查询 1-按区域查询
    private var queryType = "0"
    private var area: String? = ""
    private var noMore: Boolean = false
    private var stationType: String? = "0"
    private var title: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_station)

        stationType = intent.getStringExtra("stationType")
        title = intent.getStringExtra("title")

        titlebar_right.text = "业务介绍"

        when (stationType) {
            "0" -> titlebar_right.setOnClickListener {
                startActivity(Intent(this@ServiceStationActivity, WebActivity::class.java)
                        .putExtra("name", "陕汽服务站")
                        .putExtra("url", NetField.SERVICE_STATION))
            }
            "1" -> titlebar_right.setOnClickListener {
                startActivity(Intent(this@ServiceStationActivity, WebActivity::class.java)
                        .putExtra("name", "营运证服务商")
                        .putExtra("url", NetField.OPERATION_CERTIFICATE))
            }
            "2" -> titlebar_right.setOnClickListener {
                startActivity(Intent(this@ServiceStationActivity, WebActivity::class.java)
                        .putExtra("name", "配件经销商")
                        .putExtra("url", NetField.PARTS_DISTRIBUTOR))
            }
        }

        initTitle(title)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            titlebar_top?.visibility = View.GONE
        } else {
            titlebar_top.height = statusBarHeight()
        }

        adapter = ServiceStationAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        RecycleViewHelper().addListener(recyclerView) {
            if (!noMore) {
                pageNo++
                loadData(queryType, area!!, pageNo, pageSize)
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_more), Toast.LENGTH_SHORT).show()
            }
        }

        tabs = listOf(tv_query_by_position, tv_query_by_regional)
        tabUnderlines = listOf(v2, v3)

        showTab(0, tabs)
        showTab(0, tabUnderlines)

        tv_query_by_position.setOnClickListener {
            recyclerView.setPadding(0, MeasureHelper.dp2px(this@ServiceStationActivity, 12f), 0, 0)
            recyclerView.scrollToPosition(0)
            ll_choose.visibility = View.GONE
            showTab(0, tabs)
            showTab(0, tabUnderlines)
            noMore = false
            pageNo = 1
            queryType = "0"
            area = ""
            loadData(queryType, area!!, pageNo, pageSize)
        }
        tv_query_by_regional.setOnClickListener {
            recyclerView.setPadding(0, 0, 0, 0)
            ll_choose.visibility = View.VISIBLE
            showTab(1, tabs)
            showTab(1, tabUnderlines)
            noMore = false
            pageNo = 1
            queryType = "1"

            if (area.equals("")) {

                area = CURRENT_LOCATION.province
                loadData(queryType, area!!, pageNo, pageSize)
            }
        }

        again_choose_area.setOnClickListener {
            noMore = false
            pageNo = 1
            queryType = "1"
            showPop()
        }

        swipeRefreshLayout.setOnRefreshListener {
            noMore = false
            pageNo = 1
            loadData(queryType, area!!, pageNo, pageSize)
        }
        adapter?.setOnRefreshCallback {

        }

        adapter?.setLoadMoreListener(View.OnClickListener { view ->
            (view as TextView).text = "加载中..."
            if (!noMore) {
                pageNo++
                loadData(queryType, area!!, pageNo, pageSize)
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_more), Toast.LENGTH_SHORT).show()
            }
        })
        loadData(queryType, area!!, pageNo, pageSize)
    }

    private fun showPop() {
        var popHelper = AreaPopHelper()
        var mPopupWindow = popHelper.init(this@ServiceStationActivity)
        popHelper.setItemClickListener(ItemClickListener {
            mPopupWindow?.dismiss()
            noMore = false
            pageNo = 1
            area = it
            LogHelper.e("" + it)
            loadData(queryType, area!!, pageNo, pageSize)
        })

        mPopupWindow.showAsDropDown(v2, 0, MeasureHelper.dp2px(this@ServiceStationActivity, 12f))
    }

    private fun showTab(showIndex: Int, views: List<View>?) {
        for (v in views!!) v.isSelected = views.indexOf(v) == showIndex
    }

    private lateinit var http: Call<ServiceStation>

    fun loadData(queryType: String, area: String, pageNo: Int, pageSize: Int) {
        http = RetrofitHelper().getServiceStation(queryType, area, stationType, pageNo, pageSize).apply {
            swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = true }
            enqueue(object : Callback<ServiceStation> {
                override fun onFailure(call: Call<ServiceStation>, t: Throwable) {
                    swipeRefreshLayout?.isRefreshing = false
                    adapter?.refreshLast(0)
                }

                override fun onResponse(call: Call<ServiceStation>, response: Response<ServiceStation>) {
                    swipeRefreshLayout?.isRefreshing = false
                    response.body()?.let { it ->
                        service_station_num.text = "${area}共${it.page?.total}家$title"
                        if (it.page?.total == 0) {
                            adapter?.clear()
                            return@let
                        }
                        it.page?.rows?.let {
                            if (pageNo == 1) {
                                adapter?.clear()
                            }
                            adapter?.refresh(stationType, it, it.size < pageSize)
                        }
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        http.cancel()
    }
}
