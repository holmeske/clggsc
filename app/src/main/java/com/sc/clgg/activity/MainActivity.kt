package com.sc.clgg.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import com.gyf.barlibrary.ImmersionBar
import com.sc.clgg.R
import com.sc.clgg.activity.fragment.*
import com.sc.clgg.adapter.FragmentAdapter
import com.sc.clgg.base.BaseAppCompatActivity
import com.sc.clgg.bean.Banner
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author：lvke
 * @date：2018/2/27 11:08
 */

class MainActivity : BaseAppCompatActivity() {
    private lateinit var textViews: List<TextView>

    /**
     * 标记当前底部的index
     */
    var currenMainTabIndex: Int = 0

    private var mallFragment: MallFragment? = null

    var bannerData: ArrayList<Banner.Bean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager?.offscreenPageLimit = 5
        mallFragment = MallFragment()
        viewpager?.adapter = FragmentAdapter(supportFragmentManager, listOf(HomeFragment(), CarNetFragment(), mallFragment, TruckFriendsFragment(), MyFragment()))

        textViews = listOf(tv_home, tv_car_net, tv_mall, tv_truck_circle, tv_my)

        tv_home.setOnClickListener { checked(0) }
        tv_car_net.setOnClickListener { checked(1) }
        tv_mall.setOnClickListener { checked(2) }
        tv_truck_circle.setOnClickListener { checked(3) }
        tv_my.setOnClickListener { checked(4) }
        ImmersionBar.with(this).init()

        checked(0)
        tv_home.isSelected = true

        LogHelper.e("${intent.getIntExtra("tab", 0)}")
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
        checked(event.value)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    private fun showTab(showIndex: Int, textViews: List<TextView>) {
        for (tv in textViews) tv.isSelected = textViews.indexOf(tv) == showIndex
    }

    fun checked(i: Int) {
        viewpager?.setCurrentItem(i, false)
        currenMainTabIndex = i
        showTab(i, textViews)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currenMainTabIndex == 2 && mallFragment?.canGoBack()!!) {
                mallFragment?.goBack()
            } else {
                ExitDialog(this).show("退出提示", "确定退出车轮滚滚？") { _, _ -> System.exit(0) }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
