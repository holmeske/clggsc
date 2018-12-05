package com.sc.clgg.activity.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sc.clgg.R
import com.sc.clgg.activity.vehicle.energy.ConsumptionStatisticalActivity
import com.sc.clgg.activity.etc.ETCActivity
import com.sc.clgg.activity.login.LoginRegisterActivity
import com.sc.clgg.activity.vehicle.tally.TallyBookActivity
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.activity.WebActivity
import com.sc.clgg.activity.vehicle.locate.LocateActivity
import com.sc.clgg.bean.Banner
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author：lvke
 * @date：2018/10/12 14:37
 */
class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            title.setPadding(0, 0, 0, 0)
            title.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()
        }
        tv_more_car.setOnClickListener { WebActivity.start(activity, "整车", ConstantValue.MORE_CAR) }
        tv_car_heavy.setOnClickListener { WebActivity.start(activity, "整车", ConstantValue.CAR_HEAVY) }
        tv_car_medium.setOnClickListener { WebActivity.start(activity, "整车", ConstantValue.CAR_MEDIUM) }
        tv_car_new.setOnClickListener { WebActivity.start(activity, "整车", ConstantValue.CAR_NEW) }

        tv_more_truck_goods.setOnClickListener { (activity as MainActivity).checked(2) }
        tv_tire.setOnClickListener { WebActivity.start(activity, "轮胎", ConstantValue.TIRE) }
        tv_lube.setOnClickListener { WebActivity.start(activity, "润滑油", ConstantValue.LUBE) }
        tv_etc_card.setOnClickListener { activity?.startActivity(ETCActivity::class.java) }

        tv_more_financial.setOnClickListener { (activity as MainActivity).checked(2) }
        tv_lease.setOnClickListener { WebActivity.start(activity, "融资租赁", ConstantValue.LEASE) }
        tv_factoring.setOnClickListener { WebActivity.start(activity, "商业保理", ConstantValue.FACTORING) }
        tv_insurance.setOnClickListener { WebActivity.start(activity, "保险经纪", ConstantValue.INSURANCE) }

        tv_vehicle_positioning.setOnClickListener { activity!!.startActivity(LocateActivity::class.java) }
        tv_consumption_statistical.setOnClickListener { activity!!.startActivity(ConsumptionStatisticalActivity::class.java) }
        tv_tally_book.setOnClickListener { activity!!.startActivity(if (ConfigUtil().userid.isEmpty()) LoginRegisterActivity::class.java else TallyBookActivity::class.java) }
        tv_more.setOnClickListener { (activity as MainActivity).checked(1) }

        getBannerList()

        swipeRefreshLayout?.setOnRefreshListener {
            getBannerList()
        }
    }

    private fun getBannerList() {
        RetrofitHelper().bannerList.apply {
            enqueue(object : Callback<Banner> {
                override fun onFailure(call: Call<Banner>, t: Throwable) {
                    activity?.toast(R.string.network_anomaly)
                    swipeRefreshLayout?.isRefreshing = false
                }

                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    swipeRefreshLayout?.isRefreshing = false
                    response.body()?.data?.let {
                        banner_top.setData(activity, it.banner, false)
                        banner_hot.setData(activity, it.hot, false)
                        banner_select.setData(activity, it.wellChosen, false)

                        it.rushLeft?.get(0)?.run {
                            iv_shell.setImage(img)
                            tv_shell_name.text = name
                            tv_shell_des.text = details
                            iv_shell.setOnClickListener { WebActivity.start(activity, name, url, false) }
                        }

                        it.rushRight?.get(0)?.run {
                            iv_yuanxing_tire.setImage(img)
                            tv_yuanxing_tire_name.text = name
                            tv_yuanxing_tire_des.text = details
                            iv_yuanxing_tire.setOnClickListener { WebActivity.start(activity, name, url, false) }
                        }

                        (activity as MainActivity).bannerData = it.banner
                    }
                }
            })
        }
    }

}
