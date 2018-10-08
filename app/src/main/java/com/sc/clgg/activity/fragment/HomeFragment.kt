package com.sc.clgg.activity.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sc.clgg.R
import com.sc.clgg.activity.*
import com.sc.clgg.activity.basic.WebActivity
import com.sc.clgg.activity.contact.TruckManageContact
import com.sc.clgg.activity.presenter.TruckManagePresenter
import com.sc.clgg.activity.vehiclemanager.gps.PositioningActivity
import com.sc.clgg.application.App
import com.sc.clgg.bean.Banner
import com.sc.clgg.bean.VersionInfoBean
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.ActivityHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.Tools
import com.sc.clgg.util.UpdateApkUtil
import com.sc.clgg.util.statusBarHeight
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File.separator
import java.util.*

/**
 * @author：lvke
 * @date：2018/2/27 17:02
 */
class HomeFragment : Fragment(), TruckManageContact {
    private var viewCreated: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated = true

        Tools.getScreenInfo(App.instance)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            title.setPadding(0, 0, 0, 0)
            title.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()
        }

        TruckManagePresenter(this).checkUpdate()

        getBannerList()

        service_station.onClick {
            activity?.startActivity(Intent(activity, ServiceStationActivity::class.java)
                    .putExtra("stationType", "0").putExtra("title", "陕汽服务站"))
        }

        operator.onClick {
            activity?.startActivity(Intent(activity, ServiceStationActivity::class.java)
                    .putExtra("stationType", "1").putExtra("title", "营运证服务商"))
        }
        accessory_dealer.onClick {
            activity?.startActivity(Intent(activity, ServiceStationActivity::class.java)
                    .putExtra("stationType", "2").putExtra("title", "配件经销商"))
        }

        hb_vehicle_monitor.setHomeButtonOnClickListener {
            ActivityHelper.startAcScale(activity, PositioningActivity::class.java)
        }
        hb_my_vehicle.setHomeButtonOnClickListener {
            ActivityHelper.startAcScale(activity, MileageStatisticalActivity::class.java)
        }
        hb_driving_score.setHomeButtonOnClickListener {
            //            startActivity(Intent(activity, PathRecordActivity::class.java)
//                    .putExtra("carno", "晋C64989").putExtra("vin", "HX114675")
//                    .putExtra("startDate", "20180716000000")
//                    .putExtra("endDate", "20180716235959") )
            ActivityHelper.startAcScale(activity, ConsumptionStatisticalActivity::class.java)
        }
        hb_maintenance_home.setHomeButtonOnClickListener {
            ActivityHelper.startAcScale(activity, RepairActivity::class.java)
        }
        hb_my_tallybook.setHomeButtonOnClickListener {
            ActivityHelper.startAcScale(activity, FaultDiagnosisActivity::class.java)
        }
        hb_financial_after_market.setHomeButtonOnClickListener {
            if (ConfigUtil().userid.isEmpty()) {
                startActivity(Intent(activity, LoginRegisterActivity::class.java))
            } else {
                ActivityHelper.startAcScale(activity, TallyBookActivity::class.java)
            }
        }
        //RechargeDialog(activity!!).show()
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
        //LogHelper.e(path)
        bean?.single?.type?.let { UpdateApkUtil().checkUpdateInfo(activity, bean.single?.code, it, bean.single?.url, false) }
    }

    private inner class GlideImageLoader : ImageLoader() {
        override fun displayImage(context: Context, path: Any, imageView: ImageView) {
            /*注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！*/

            Glide.with(context).load(path).apply(RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView)
        }
    }

    private lateinit var bannerHttp: Call<Banner>
    private fun getBannerList() {
        bannerHttp = RetrofitHelper().bannerList.apply {
            enqueue(object : Callback<Banner> {
                override fun onFailure(call: Call<Banner>, t: Throwable) {
                }

                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    response.body()?.data?.let {
                        banner?.setImages(
                                ArrayList<String>().apply {
                                    for (bean in it) {
                                        add(bean.img!!)
                                    }
                                }
                        )?.setImageLoader(GlideImageLoader())?.setOnBannerListener { position ->

                            it[position].takeIf { !(it.name.isNullOrEmpty() || it.url.isNullOrEmpty()) }?.apply {
                                startActivity(Intent(activity, WebActivity::class.java).putExtra("name", name).putExtra("url", url))
                            }

                        }?.start()
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerHttp.cancel()
    }
}
