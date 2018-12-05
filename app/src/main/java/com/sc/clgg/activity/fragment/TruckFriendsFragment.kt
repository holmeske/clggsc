package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.sc.clgg.R
import com.sc.clgg.activity.CommentActivity
import com.sc.clgg.activity.LoginRegisterActivity
import com.sc.clgg.activity.PublishDynamicActivity
import com.sc.clgg.adapter.TruckFriendsAdapter
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.bean.TruckFriend
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.RecycleViewHelper
import com.sc.clgg.util.statusBarHeight
import kotlinx.android.synthetic.main.fragment_truck_friends.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




/**
 * @author：lvke
 * @date：2018/5/10 16:06
 */
class TruckFriendsFragment : BaseFragment() {
    private var adapter: TruckFriendsAdapter? = null
    private var pageNum = 1
    private val pageSize = 5
    private var noMore = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_truck_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            title.setPadding(0, 0, 0, 0)
            title.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()

            title_right.setPadding(0, 0, 0, 0)
            title_right.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()
        }
        init()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: MessageEvent) {
        updateItem()
    }

    fun updateItem(){
        LogHelper.e("开始更新")
        if (CommentActivity.position!=-1&& CommentActivity.commen!=null&& !adapter?.listAll.isNullOrEmpty()){
            adapter?.listAll!![CommentActivity.position]?.driverCircleCommentList?.add(CommentActivity.commen!!)
            adapter?.notifyItemChanged(CommentActivity.position, 2)
        }
    }


    override fun onLazyFetchData() {
        super.onLazyFetchData()
        LogHelper.e("onLazyFetchData() --->卡友圈")
        if (recyclerView.childCount == 0) {
            loadData()
        }
    }

    override fun onResume() {
        LogHelper.e("onResume() --->卡友圈")
        super.onResume()
        if (userVisibleHint) {
            loadData()
        }
    }

    private fun init() {
        adapter = TruckFriendsAdapter()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        recyclerView.addItemDecoration(divider)

        initListener()

        RecycleViewHelper().addListener(recyclerView, object : RecycleViewHelper.OnScrollBottomListener {
            override fun onBottom() {
                if (!noMore) {
                    pageNum++
                    loadData()
                } else {
                    Toast.makeText(activity, getString(R.string.no_more), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private var loadMoreView: TextView? = null

    private fun initListener() {
        title_right?.setOnClickListener {
            if (ConfigUtil().userid.isEmpty()) {
                startActivity(Intent(activity, LoginRegisterActivity::class.java))
            } else {
                startActivity(Intent(activity, PublishDynamicActivity::class.java))
            }
        }
        swipeRefreshLayout?.setOnRefreshListener {
            noMore = false
            pageNum = 1
            loadData()
        }
        adapter?.setLoadMoreListener(View.OnClickListener { view ->
            (view as TextView).text = "加载中..."
            loadMoreView = view
            if (!noMore) {
                pageNum++
                loadData()
            } else {
                Toast.makeText(activity, getString(R.string.no_more), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var call: Call<TruckFriend>? = null
    private fun loadData() {
        call = RetrofitHelper().driverCircle(pageNum, pageSize)
        call?.enqueue(object : Callback<TruckFriend> {
            override fun onFailure(call: Call<TruckFriend>?, t: Throwable?) {
                swipeRefreshLayout?.isRefreshing = false
                loadMoreView?.text = "加载更多"
                adapter?.refreshLast()
                activity?.toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<TruckFriend>?, response: Response<TruckFriend>?) {
                swipeRefreshLayout?.isRefreshing = false
                if (!response?.body()?.success!!) {
                    activity?.toast(response.body()?.msg!!)
                } else {
                    response.body()?.pageInfo?.list?.let {
                        if (pageNum == 1) {
                            adapter?.clear()
                        }
                        adapter?.refresh(it, it.size < pageSize)
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
