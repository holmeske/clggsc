package com.sc.clgg.activity.vehiclemanager.myvehicle

import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import com.db.chart.tooltip.Tooltip
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.sc.clgg.R
import com.sc.clgg.bean.NearMileBean
import com.sc.clgg.bean.VehicleDetailBean
import com.sc.clgg.bean.VehicleLocationBean
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import com.sc.clgg.util.Tools
import kotlinx.android.synthetic.main.activity_vehicle.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.LogHelper
import java.text.DecimalFormat


/**
 * 我的车辆列表详情
 *
 * @author lvke
 */
@Suppress("DEPRECATION")
class VehicleActivity : AppCompatActivity() {

    private var mPassData: VehicleLocationBean? = null
    private var mVehicleDetailBean: VehicleDetailBean? = null
    private var mDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_vehicle)
        super.onCreate(savedInstanceState)

        mPassData = intent.getParcelableExtra("bean")
        LogHelper.e("mPassData = " + Gson().toJson(mPassData))
        mDate = intent.getStringExtra("date")
        loadData()
        initView()
        initListener()
    }

    private fun loadData() {
        HttpRequestHelper.myVehicle(mPassData?.carno, mDate, object : HttpCallBack() {
            override fun onSuccess(body: String) {
                mVehicleDetailBean = Gson().fromJson(body, VehicleDetailBean::class.java)

                initBarChart(mVehicleDetailBean?.listMileage)

                val driverName = with(mVehicleDetailBean?.driverName) {
                    if (isNullOrEmpty()) "" else this
                }
                val driverMobile = with(mVehicleDetailBean?.mobile) {
                    if (isNullOrEmpty()) "" else this
                }
                tv_drivename_tel?.text = Html.fromHtml("<font color='#669933'>$driverName</font>$driverMobile")
            }

            override fun onStart() {
                super.onStart()
                progressBar.visibility = View.VISIBLE
            }

            override fun onFinish() {
                super.onFinish()
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onStop() {
        super.onStop()
        OkGo.getInstance().cancelTag("myVehicle")
    }

    private fun initView() {
        /*if (mPassData?.dayMileage != null && mPassData?.dayMileage!! > 0) {
            tv_today_mile?.text = Html.fromHtml("当日行驶 <font color='#E40011'><big>${mPassData?.dayMileage}</big></font> 公里")
        } else {
            tv_today_mile?.text = Html.fromHtml("当日行驶 <font color='#E40011'><big>0</big></font> 公里")
        }*/
        tv_today_mile?.text = with(mPassData?.dayMileage) {
            Html.fromHtml("当日行驶 <font color='#E40011'><big>${if (this!! > 0) toString() else 0}</big></font> 公里")
        }

        tv_day_oil?.text = with(mPassData!!.oilcost) {
            if (this!! > 0) this.toString() else "0"
        }

        tv_day_oilkm?.text = with(mPassData?.oilcostphkm) {
            if (this!! > 0) this.toString() else "0"
        }

        titlebar_title?.text = with(mPassData?.carno) {
            if (isNullOrEmpty()) "" else this
        }
    }

    private fun initListener() {
        if (mPassData?.carno.isNullOrEmpty()) Tools.Toast("车牌号不能为空")
        else {
            time_rpt.onClick { startActivity(Intent(this@VehicleActivity, OilWearActivity::class.java).putExtra("carno", mPassData?.carno)) }
            mile_rpt.onClick { startActivity(Intent(this@VehicleActivity, MileageActivity::class.java).putExtra("carno", mPassData?.carno)) }
            time_tj.onClick { startActivity(Intent(this@VehicleActivity, HistoricalRouteActivity::class.java).putExtra("carno", mPassData?.carno)) }
        }
    }

    private fun initBarChart(list: List<NearMileBean>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        barchart?.reset()
        val mLabels = arrayOfNulls<String>(list.size)
        val mValues = FloatArray(list.size)

        var j = 0
        for (i in list.indices.reversed()) {
            try {
                mLabels[j] = "${list[i].date.substring(4, 6).toInt()}/${list[i].date.substring(6, 8).toInt()}"
                mValues[j] = list[i].mileage.toFloat()
                j++
            } catch (e: Exception) {
                LogHelper.e(e)
            }

        }

        if (mLabels.isEmpty() || mValues.isEmpty()) {
            barchart?.visibility = View.GONE
            return
        }

        val data = BarSet(mLabels, mValues)

        data.color = Color.parseColor("#A6CDEC")

        val tip = Tooltip(this, R.layout.barchart_tooltip, R.id.value)

        tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1.toFloat()),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)).duration = 200

        tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0.toFloat()),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)).duration = 200

        tip.setVerticalAlignment(Tooltip.Alignment.TOP_TOP)
        barchart?.setTooltips(tip)
        barchart?.addData(data)
        //垂直坐标的最大值最小值以及刻度
        barchart?.setAxisBorderValues(0f, 1500f, 300f)
        //X.Y轴的相关设置，是否有轴线（有/没有），刻度在的位置（里/外/无）
        barchart?.setLabelsFormat(DecimalFormat("0' 公里'"))
        barchart?.show(Animation().inSequence(.7f, intArrayOf(1, 0, 2, 3, 4, 5, 6)))
    }


}
