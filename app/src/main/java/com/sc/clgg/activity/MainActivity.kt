package com.sc.clgg.activity

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gyf.barlibrary.ImmersionBar
import com.sc.clgg.R
import com.sc.clgg.activity.fragment.*
import com.sc.clgg.activity.login.LoginRegisterActivity
import com.sc.clgg.activity.my.MyMessageActivity
import com.sc.clgg.activity.my.MyVehicleActivity
import com.sc.clgg.activity.my.SetActivity
import com.sc.clgg.activity.my.userinfo.PersonalDataActivity
import com.sc.clgg.activity.nocar.IdentityActivity
import com.sc.clgg.adapter.FragmentAdapter
import com.sc.clgg.base.BaseAppCompatActivity
import com.sc.clgg.bean.Banner
import com.sc.clgg.bean.CarNetEvent
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.dp2px
import com.sc.clgg.util.statusBarHeight
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity


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

        setDrawerLayout()
        checked(0)
        tv_home.isSelected = true
    }

    private fun setDrawerLayout() {
        if (!ConfigUtil().isLogined(this)) {
            tv_nickname.text = "未登录"
            tv_user_type.visibility = View.GONE
        } else {
            tv_nickname.text = ConfigUtil().nickName
        }
        //去认证
        tv_certification.setOnClickListener {
            startActivity<IdentityActivity>()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            iv_open.setPadding(dp2px(15f), statusBarHeight() + 15, 15, 0)
        } else {
            iv_open.setPadding(dp2px(15f), 15, 15, 0)
        }
        iv_open.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        Glide.with(this).load(ConfigUtil().icon)
                .apply(RequestOptions().error(R.drawable.bg_00a0e9_circle).placeholder(R.drawable.bg_00a0e9_circle))
                //.circleCrop()
                .into(iv_head)

        ll_mymessage.setOnClickListener { if (ConfigUtil().isLogined(this)) startActivity<MyMessageActivity>() }
        ll_mycar.setOnClickListener { if (ConfigUtil().isLogined(this)) startActivity<MyVehicleActivity>() }
        ll_personal_data.setOnClickListener { if (ConfigUtil().isLogined(this)) startActivity<PersonalDataActivity>() }

        tv_set.setOnClickListener { if (ConfigUtil().isLogined(this)) startActivity<SetActivity>() }
        tv_exit.setOnClickListener {
            ExitDialog(this).show("温馨提示", "确定退出登录？") { _, _ ->
                ConfigUtil().clear()
                startActivity<MainActivity>()
                startActivity<LoginRegisterActivity>()
                finish()
            }
        }
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
    fun onCarNetEvent(event: CarNetEvent) {
        checked(1)
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
