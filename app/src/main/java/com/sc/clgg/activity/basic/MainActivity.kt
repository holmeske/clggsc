package com.sc.clgg.activity.basic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.TextView
import com.gyf.barlibrary.ImmersionBar
import com.sc.clgg.R
import com.sc.clgg.activity.LoginRegisterActivity
import com.sc.clgg.activity.fragment.ETCFragment
import com.sc.clgg.activity.fragment.HomeFragment
import com.sc.clgg.activity.fragment.MyFragment
import com.sc.clgg.activity.fragment.TruckFriendsFragment
import com.sc.clgg.adapter.FragmentAdapter
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.ConfigUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * @author：lvke
 * @date：2018/2/27 11:08
 */

class MainActivity : AppCompatActivity() {
    private lateinit var textViews: List<TextView>

    /**
     * 标记当前底部的index
     */
    var currenMainTabIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager?.offscreenPageLimit = 4
        viewpager?.adapter = FragmentAdapter(supportFragmentManager, listOf(HomeFragment(), ETCFragment(), TruckFriendsFragment(), MyFragment()))

        textViews = listOf(tv_truck_manage, tv_transport_manage, tv_user_setttings, tv_my)

        tv_truck_manage.onClick { checked(0) }
        tv_transport_manage.onClick { checked(1) }
        tv_user_setttings.onClick { checked(2) }
        tv_my.onClick {
            if (ConfigUtil().userid.isEmpty()) startActivity(Intent(this@MainActivity, LoginRegisterActivity::class.java))
            else checked(3)
        }
        ImmersionBar.with(this).init()

        tv_truck_manage.isSelected = true

        /*val nfc = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

        Toast.makeText(this@MainActivity,
                String.format("NFC支持%s", nfc), Toast.LENGTH_SHORT)
                .show()*/


    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    private fun showTab(showIndex: Int, textViews: List<TextView>) {
        for (tv in textViews) tv.isSelected = textViews.indexOf(tv) == showIndex
    }

    private fun checked(i: Int) {
        viewpager?.setCurrentItem(i, false)
        currenMainTabIndex = i
        showTab(i, textViews)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onResume() {
        LogHelper.e("onResume() --->首页")
        super.onResume()
        if (currenMainTabIndex == 3 && ConfigUtil().userid.isEmpty()) {
            checked(0)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: MessageEvent) {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDialog(this).show("退出提示", "确定退出车轮滚滚？") { _, _ -> System.exit(0) }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
