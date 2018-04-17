package com.sc.clgg.activity.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.sc.clgg.R
import com.sc.clgg.activity.contact.TruckManageContact
import com.sc.clgg.activity.presenter.TruckManagePresenter
import com.sc.clgg.activity.vehiclemanager.FinancialAftermarketActivity
import com.sc.clgg.activity.vehiclemanager.drivingscore.DrivingScoreActivity
import com.sc.clgg.activity.vehiclemanager.maintenance.MaintenanceHomeActivity
import com.sc.clgg.activity.vehiclemanager.monitor.VehicleMonitorActivity
import com.sc.clgg.activity.vehiclemanager.myvehicle.MyVehicleActivity
import com.sc.clgg.activity.vehiclemanager.tallybook.MyTallyBookActivity
import com.sc.clgg.application.App
import com.sc.clgg.application.App.screenWidth
import com.sc.clgg.bean.VersionInfoBean
import com.sc.clgg.util.Tools
import com.sc.clgg.util.UpdateApkUtil
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_vehicle_manage.*
import kotlinx.android.synthetic.main.view_title_only.*
import org.jetbrains.anko.doAsync
import tool.helper.ActivityHelper
import tool.helper.LogHelper
import tool.helper.MeasureUtils
import java.io.File.separator

/**
 * @author：lvke
 * @date：2018/2/27 17:02
 */
class VehicleManageFragment : Fragment(), TruckManageContact {
    private var viewCreated: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_vehicle_manage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated = true

        tv_title?.text = getString(R.string.accom_car)

        Tools.getScreenInfo(App.getInstance())

        val height = MeasureUtils().getScreenWH(App.getInstance())[1]
        val h = (height - MeasureUtils.dp2px(App.getInstance(), (44 + 231 + 45).toFloat())).toFloat()

        ll_one?.layoutParams?.height = (h / 2).toInt()
        ll_two?.layoutParams?.height = (h / 2).toInt()

        val presenter = TruckManagePresenter(this)
        presenter.checkUpdate()

        doAsync { presenter.addressArrange(activity?.applicationContext) }

        //banner 原始宽高：1242px * 692px     屏幕宽高:width*height
        banner?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 692 * screenWidth / 1242)
        banner?.setImages(listOf(R.drawable.banner_01))?.setImageLoader(GlideImageLoader())?.setOnBannerListener { position -> LogHelper.e("点击了第" + position + "张") }?.start()

        hb_vehicle_monitor.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, VehicleMonitorActivity::class.java) }
        hb_my_vehicle.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, MyVehicleActivity::class.java) }
        hb_driving_score.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, DrivingScoreActivity::class.java) }
        hb_maintenance_home.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, MaintenanceHomeActivity::class.java) }
        hb_my_tallybook.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, MyTallyBookActivity::class.java) }
        hb_financial_after_market.setHomeButtonOnClickListener { ActivityHelper.startAcScale(activity, FinancialAftermarketActivity::class.java) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (viewCreated) {
            when {
                isVisibleToUser -> banner?.startAutoPlay()
                else -> banner?.stopAutoPlay()
            }
        }
    }

    override fun getVersionInfo(bean: VersionInfoBean?) {
        val path = """${getExternalStorageDirectory().path}${getDataDirectory()}$separator${activity?.packageName}${getDownloadCacheDirectory()}${separator}clggsc.apk"""
        LogHelper.e(path)
        bean?.single?.type?.let { UpdateApkUtil().checkUpdateInfo(activity, bean.single?.code, it, bean.single?.url, false) }
    }

    private inner class GlideImageLoader : ImageLoader() {
        override fun displayImage(context: Context, path: Any, imageView: ImageView) {
            /*注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！*/
            Glide.with(context).load(path).into(imageView)
        }
    }
}
