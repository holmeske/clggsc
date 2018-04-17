package com.sc.clgg.activity.vehiclemanager.myvehicle

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.sc.clgg.R
import com.sc.clgg.adapter.MyVehicleAdapter
import com.sc.clgg.base.BaseAppCompatActivity
import com.sc.clgg.bean.MyVehicleBean
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import com.sc.clgg.util.*
import kotlinx.android.synthetic.main.activity_my_vehicle.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.*
import java.util.*

/**
 * 我的车辆
 * @author lvke
 */
class MyVehicleActivity : BaseAppCompatActivity() {
    private var adapter: MyVehicleAdapter? = null
    private var mMyVehicleBean: MyVehicleBean? = null
    private var countMile: Double = 0.0
    private val calendar = Calendar.getInstance()
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_vehicle)
        super.onCreate(savedInstanceState)

        titlebar_title?.text = "我的车辆"
        titlebar_right_text?.text = "+ 添加车辆"
//        val layoutParams = titlebar_right_text?.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.setMargins(0, 0, MeasureUtils.dp2px(applicationContext, 10f), 0)
//        layoutParams.height = MeasureUtils.dp2px(applicationContext, 30f)
        titlebar_right_text?.layoutParams.let {
            it as ConstraintLayout.LayoutParams
            it.setMargins(0, 0, MeasureUtils.dp2px(applicationContext, 10f), 0)
            it.height = MeasureUtils.dp2px(applicationContext, 30f)
        }

        titlebar_right_text?.setPadding(MeasureUtils.dp2px(applicationContext, 6f), 0, MeasureUtils.dp2px(applicationContext, 6f), 0)
        titlebar_right_text?.textSize = 12f
        titlebar_right_text?.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            titlebar_right_text?.background = ContextCompat.getDrawable(this, R.drawable.add_car_list_selector)
        }

        init()
        tv_count_date.onClick { showDatePickerDialog() }
        titlebar_right_text.onClick { ActivityHelper.startAcScale(this@MyVehicleActivity, AddVehicleActivity::class.java) }

        progressBar = creatProgressBar()
    }


    override fun onRestart() {
        super.onRestart()
        LogHelper.e("onRestart()")
        loadData(date)
    }

    private var progressBar: ProgressBar? = null

    override fun onDestroy() {
        super.onDestroy()
        removeProgressBar(progressBar)
        OkGo.getInstance().cancelTag("okgo")
    }

    private fun init() {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        date = DateUtil.format(calendar.timeInMillis)

        adapter = MyVehicleAdapter(this, date)
        lv?.adapter = adapter

        adapter?.setCallbackListener { loadData(date) }

        tv_count_date?.text = DateUtil.format("yyyy年MM月dd日", calendar.timeInMillis)
        loadData(date)
    }

    private fun loadData(date: String?) {
        HttpRequestHelper.getMyTeam(ConfigUtil().userid, date, object : HttpCallBack() {
            override fun onStart() {
                super.onStart()
                progressBar?.show()
            }

            override fun onSuccess(body: String) {
                if (TextUtils.isEmpty(body)) {
                    return
                }
                mMyVehicleBean = Gson().fromJson(body, MyVehicleBean::class.java)
                if (mMyVehicleBean != null) {
                    convertData()
                }
            }

            override fun onError(body: String) {
                super.onError(body)
                Tools.Toast(getString(R.string.network_anomaly))
            }

            override fun onFinish() {
                super.onFinish()
                progressBar?.hide()
            }
        })
    }

    private fun convertData() {
        countMile = 0.0
        if (mMyVehicleBean?.list != null && mMyVehicleBean?.list?.size!! > 0) {
            for ((dayMileage) in mMyVehicleBean?.list!!) if (dayMileage != null && dayMileage > 0) {
                try {
                    countMile += dayMileage
                } catch (e: Exception) {
                    LogHelper.e(e)
                }

            }
            initView()
        }
    }

    private fun initView() {
        if (countMile > 0) {
            tv_count_mileage?.text = Compat.fromHtml("日总里程： <Big>" + java.text.DecimalFormat("#.00").format(countMile) + "</Big> 公里")
        } else {
            tv_count_mileage?.text = Compat.fromHtml("日总里程： <Big>0</Big> 公里")
        }

        tv_count_date?.text = TimeHelper.long2time("yyyy年MM月dd日", calendar.timeInMillis)
        adapter?.setData(mMyVehicleBean?.list)
    }

    //时间选择器
    private fun showDatePickerDialog() {

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            date = DateUtil.format(calendar.timeInMillis)

            tv_count_date?.text = DateUtil.format("yyyy年MM月dd日", calendar.timeInMillis)
            adapter?.setDate(date)
            loadData(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

}
