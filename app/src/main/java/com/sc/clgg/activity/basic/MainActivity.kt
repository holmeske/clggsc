package com.sc.clgg.activity.basic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.TextView
import com.sc.clgg.R
import com.sc.clgg.activity.fragment.TransportManageFragment
import com.sc.clgg.activity.fragment.UserSettingsFragment
import com.sc.clgg.activity.fragment.VehicleManageFragment
import com.sc.clgg.adapter.FragmentAdapter
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.dialog.SetGpsDialog
import com.sc.clgg.util.ConfigUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.AppHelper


/**
 * @author：lvke
 * @date：2018/2/27 11:08
 */
class MainActivity : AppCompatActivity() {

    private var textViews: List<TextView>? = null

    private var setGpsDialog: SetGpsDialog? = null
    /**
     * 标记当前底部的index
     */
    var currenMainTabIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager?.offscreenPageLimit = 2
        viewpager?.adapter = FragmentAdapter(supportFragmentManager, listOf(VehicleManageFragment(), TransportManageFragment(), UserSettingsFragment()))

        setGpsDialog = SetGpsDialog(this)
        textViews = listOf(tv_truck_manage, tv_transport_manage, tv_user_setttings)

        tv_truck_manage.onClick { checked(0) }
        tv_transport_manage.onClick { checked(1) }
        tv_user_setttings.onClick {
            if (ConfigUtil().userid.isEmpty()) startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            else checked(2)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!AppHelper.isGpsOpen(applicationContext) && !setGpsDialog!!.dialog.isShowing) {
            setGpsDialog!!.show()
        }
    }

    private fun showTab(showIndex: Int, textViews: List<TextView>?) {
        for (tv in textViews!!) tv.isSelected = textViews.indexOf(tv) == showIndex
    }

    private fun checked(i: Int) {
        viewpager!!.setCurrentItem(i, false)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.value > 99) tv_unread!!.text = getString(R.string.more_than_99)
        else tv_unread!!.text = event.value.toString()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDialog(this).show("退出提示", "确定退出车轮滚滚？") { _, _ -> System.exit(0) }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
