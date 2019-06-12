package com.sc.clgg.activity.vehicle.energy

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.sc.clgg.R
import com.sc.clgg.adapter.ConsumptionStatisticalAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Consumption
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.*
import com.sc.clgg.widget.MyMarkerView
import kotlinx.android.synthetic.main.activity_consumption_statistical.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ConsumptionStatisticalActivity : BaseImmersionActivity() {
    private var adpter: ConsumptionStatisticalAdapter? = null
    private var dateStr: String? = null
    private var year: Int = 0
    private var month: Int = 0
    private var maxDay: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_statistical)

        initTitle("能耗统计")

        adpter = ConsumptionStatisticalAdapter(ConsumptionStatisticalAdapter.ItemClickListener { vin, carno ->
            startActivity(Intent(this@ConsumptionStatisticalActivity, ConsumptionDetailActivity::class.java)
                    .putExtra("date", dateStr)
                    .putExtra("vin", vin)
                    .putExtra("carno", carno)
                    .putExtra("month", month)
                    .putExtra("year", year)
            )
        })

        recyclerview?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerview?.adapter = adpter

        initTimePickerView()
        initListener()

        month = CalendarHelper.getCurrentMonth()
        dateStr = DateHelper.formatCurrentTime("yyyyMM")
        tv_date?.text = DateHelper.formatCurrentTime("yyyy-MM")

        maxDay = selectedCalendar.getActualMaximum(Calendar.DATE)
        tv4.text = String.format(getString(R.string.s_0), maxDay)

        chart.setNoDataText("")
        loadData(dateStr)
    }

    private var call: Call<Consumption>? = null

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    private fun loadData(date: String?) {

        call = RetrofitHelper().oilsteams(date)
        showProgressDialog()
        call?.enqueue(object : Callback<Consumption> {
            override fun onFailure(call: Call<Consumption>?, t: Throwable?) {
                hideProgressDialog()
            }

            override fun onResponse(call: Call<Consumption>?, response: Response<Consumption>?) {
                hideProgressDialog()

                response?.body()?.let { it2 ->
                    if (!it2.success) {
                        it2.msg?.let { it1 -> toast(it1) }
                        return
                    } else {
                        it2.data?.let { it ->

                            tv_month_mileage?.text = DecimalFormatHelper.formatTwo(it.totalFuel)
                            one_vehice_month_mileage?.text = DecimalFormatHelper.formatTwo(it.hundredFuel)

                            val data = ArrayList<Float>()
                            it.dayDetails?.let {
                                if (it.isNotEmpty()) {
                                    for ((_, totalFuel) in it) {
                                        data.add(totalFuel!!.toFloat())
                                    }
                                }
                            }
                            adpter?.refresh(it.dataList)

                            if (maxDay == 31) {
                                tv2.translationX = -MeasureHelper.dp2px(this@ConsumptionStatisticalActivity, 8f).toFloat()
                                tv3.translationX = -MeasureHelper.dp2px(this@ConsumptionStatisticalActivity, 8f).toFloat()
                            }
                            initChart(data, if (data.isEmpty()) 0f else (Math.ceil((Collections.max(data) / 100f).toDouble()) * 100f).toFloat(), maxDay)
                        }
                    }
                }
            }
        })

    }

    private var mTimePickerView: TimePickerView? = null
    private val selectedCalendar = Calendar.getInstance()
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    private fun initTimePickerView() {
        startCalendar.set(CalendarHelper.getCurrentYear(), CalendarHelper.getCurrentMonth() - 1, 0)

        mTimePickerView = TimePickerBuilder(this, OnTimeSelectListener { date, _ ->
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            selectedCalendar.time = date

            year = selectedCalendar.get(Calendar.YEAR)
            month = selectedCalendar.get(Calendar.MONTH) + 1

            tv_date.text = DateHelper.formatCurrentTime(selectedCalendar, "yyyy-MM")

            dateStr = DateHelper.formatCurrentTime(selectedCalendar, "yyyyMM")
            loadData(dateStr)
            maxDay = selectedCalendar.getActualMaximum(Calendar.DATE)
            tv4.text = String.format(getString(R.string.s_0), maxDay)
        }).setType(booleanArrayOf(true, true, false, false, false, false))//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setOutSideColor(ContextCompat.getColor(this, R.color.white)) //设置外部遮罩颜色
                .setDecorView(null)
                .build()
    }

    private var change: Boolean = false
    private fun initListener() {
        tv_date.setOnClickListener {
            mTimePickerView?.show()
        }
        tv_mileage.setOnClickListener {
            val drawable: Drawable?
            if (!change) {
                change = true
                drawable = ContextCompat.getDrawable(application, R.drawable.energy_ico_up)

            } else {
                change = false
                drawable = ContextCompat.getDrawable(application, R.drawable.energy_ico_down)
            }
            drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            tv_mileage.setCompoundDrawables(null, null, drawable, null)
            adpter?.reverse()
        }
    }

    private fun initChart(data: List<Float>, maximum: Float, maxDay: Int? = 30) {

        val params = chart.layoutParams
        if (data.size == 1) {
            params.width = (MeasureHelper.getScreenWidth(this) / maxDay!!) * 3
        } else {
//          params.width =MeasureHelper.getScreenWidth(this)
            params.width = ((MeasureHelper.getScreenWidth(this) - MeasureHelper.dp2px(this, 10f)) / maxDay!!) * data.size
            +MeasureHelper.dp2px(this, 10f)
        }

        LogHelper.e("数组长度 = " + data.size + "   maximum = " + maximum + "   maxDay = " + maxDay + "   width = " + params.width)

        chart.setNoDataText("")
        //val xLabels = listOf("1日", "8日", "15日", "22日", "31日")

        chart.setDrawGridBackground(false)

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)
        //是否拖拽
        chart.isDragEnabled = true
        //是否缩放
        chart.setScaleEnabled(false)

        chart.setPinchZoom(true)

        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.chartView = chart // For bounds control
        chart.marker = mv

        val xAxis = chart.xAxis
        //是否显示X轴网格线
        xAxis.setDrawGridLines(false)

        xAxis.enableGridDashedLine(10f, 10f, 0f)
        //X坐标 文字  显示的位置
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //X轴坐标显示文字的数量
//        xAxis.setLabelCount(xLabels.size, true)

        //设置 X轴 文字
//        xAxis.setValueFormatter { value, axis -> xLabels.get(value.toInt()) }
        //是否显示X坐标文字
        xAxis.setDrawLabels(false)


        val leftAxis = chart.axisLeft
        leftAxis.removeAllLimitLines()

        //Y坐标 最小值
        leftAxis.axisMinimum = 0f
        //Y坐标 最大值
        leftAxis.axisMaximum = maximum
        //是否显示Y轴 单位值
        leftAxis.setDrawLabels(false)

//        leftAxis.setYOffset(20f);
        leftAxis.setDrawGridLines(false)
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(false)

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true)

        leftAxis.setDrawAxisLine(false)

        chart.axisRight.isEnabled = false

        //chart.getViewPortHandler().setMaximumScaleY(2f);
        //chart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(data, maxDay)
        //绘制时间
        chart.animateX(1000)

        val l = chart.legend

        // modify the legend ...
        l.form = Legend.LegendForm.NONE

        val sets = chart.data.dataSets
        for (iSet in sets) {
//            val set = iSet as LineDataSet
//            set.setValueFormatter { value, _, _, _ ->
//                value.toString()
//            }
        }

        val colorList = java.util.ArrayList<Int>()
        for (i in 0..data.size) {
            if (i in listOf(0, 7, 14, 21, maxDay)) {
                colorList.add(Color.parseColor("#444444"))
            } else {
                colorList.add(Color.TRANSPARENT)
            }
        }
        chart.data.getDataSetByIndex(0).setValueTextColors(colorList)
    }

    private fun setData(datas: List<Float>, maxDay: Int? = 30) {
        val values = ArrayList<Entry>()

        val colorList = ArrayList<Int>()


        for (i in datas.indices) {
            if (i in listOf(0, 7, 14, 21, maxDay)) {
                values.add(Entry(i.toFloat(), datas[i], ContextCompat.getDrawable(this, R.drawable.bg_point_red)))
                colorList.add(Color.parseColor("#444444"))
            } else {
                values.add(Entry(i.toFloat(), datas[i]))
                colorList.add(Color.TRANSPARENT)
            }
        }

        val dataSet: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            dataSet = chart.data.getDataSetByIndex(0) as LineDataSet
            dataSet.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a cardType

            //                     参数2：0轴线 下面的文字
            dataSet = LineDataSet(values, "")
            //是否显示自定义的坐标点
            dataSet.setDrawIcons(true)

            // set the line to be drawn like this "- - - - - -"
            //            set1.enableDashedLine(10f, 5f, 0f);
            //            set1.enableDashedHighlightLine(10f, 5f, 0f);
            dataSet.color = Color.BLACK
            dataSet.setCircleColor(Color.BLACK)
            dataSet.lineWidth = 1f
            dataSet.circleRadius = 3f
            dataSet.setDrawCircleHole(false)
            dataSet.setDrawCircles(false)
            dataSet.valueTextSize = 9f
            dataSet.setDrawFilled(true)
            dataSet.formLineWidth = 1f
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            //            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            dataSet.formSize = 15f

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                dataSet.fillDrawable = drawable
            } else {
                dataSet.fillColor = Color.parseColor("#80ed1437")
            }

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(dataSet) // add the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }
}
