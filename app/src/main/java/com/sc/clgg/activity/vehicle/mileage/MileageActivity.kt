package com.sc.clgg.activity.vehicle.mileage

import android.content.Intent
import android.graphics.Color
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
import com.sc.clgg.adapter.MileageStatisticalAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Mileage
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CalendarHelper
import com.sc.clgg.tool.helper.DateHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.widget.MyMarkerView
import kotlinx.android.synthetic.main.activity_mileage.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MileageActivity : BaseImmersionActivity() {
    var adpter: MileageStatisticalAdapter? = null
    private var dateStr: String? = ""
    var year: String? = ""
    var month: Int = 0
    private var maxDay: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mileage)

        initTitle("里程统计")
        chart.setNoDataText("")

        adpter = MileageStatisticalAdapter(MileageStatisticalAdapter.ItemClickListener { vin, carno ->
            LogHelper.e("month = " + month)
            startActivity(Intent(this@MileageActivity, MileageDetailActivity::class.java)
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
        dateStr = DateHelper.formatCurrentTime("yyyy-MM")
        year = DateHelper.formatCurrentTime("yyyy")
        tv_date.text = dateStr

        maxDay = selectedCalendar.getActualMaximum(Calendar.DATE)
        tv4.text = "${maxDay}日"
        loadData(dateStr)
    }

    override fun onDestroy() {
        super.onDestroy()
        http.cancel()
    }

    private lateinit var http: retrofit2.Call<Mileage>
    private fun loadData(date: String?) {
        http = RetrofitHelper().mileage(date).apply {
            showProgressDialog()
            enqueue(object : Callback<Mileage> {
                override fun onResponse(call: Call<Mileage>, response: Response<Mileage>) {
                    hideProgressDialog()

                    response.body()?.let {
                        if (!it.success) {
                            toast("${it.msg}");return@let
                        }
                        tv_month_mileage?.text = it.totalMileages
                        tv_day_mileage?.text = it.totalDays
                        one_vehice_month_mileage?.text = it.singleTotal

                        val data = ArrayList<Float>()
                        for ((_, dayMileages) in it.chartList!!) {
                            data.add(dayMileages!!.toFloat())
                        }
                        adpter?.refresh(it.details)

                        initChart(data, if (data.isEmpty()) 0f else (Math.ceil((Collections.max(data) / 100f).toDouble()) * 100f).toFloat())
                    }
                }

                override fun onFailure(call: Call<Mileage>, t: Throwable) {
                    hideProgressDialog()
                }
            })
        }

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

            selectedCalendar.time
            month = selectedCalendar.get(Calendar.MONTH) + 1

            maxDay = selectedCalendar.getActualMaximum(Calendar.DATE)
            tv4.text = "${maxDay}日"
            dateStr = DateHelper.formatCurrentTime(selectedCalendar, "yyyy-MM")
            tv_date.text = dateStr
            loadData(dateStr)
        }).setType(booleanArrayOf(true, true, false, false, false, false))//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build()
    }

    private fun initListener() {
        tv_date.setOnClickListener {
            mTimePickerView?.show()
        }
    }


    private fun initChart(data: List<Float>, maximum: Float) {
        LogHelper.e("图表数据 = " + data.size)
//        var params = chart.layoutParams
//        params.width = ((MeasureHelper.getScreenWidth(this) - MeasureHelper.dp2px(this, 10f)) / maxDay) * data.size + MeasureHelper.dp2px(this, 10f)

        chart.setNoDataText("暂无数据")
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
        setData(data)
        //绘制时间
        chart.animateX(1000)

        val l = chart.legend

        // modify the legend ...
        l.form = Legend.LegendForm.NONE

        val sets = chart.data.dataSets
        for (iSet in sets) {
            val set = iSet as LineDataSet
//            set.setValueFormatter { value, _, _, _ ->
//                value.toString()
//            }
        }

        val colorList = java.util.ArrayList<Int>()
        for (i in 0..data.size) {
            if (i in listOf(0, 7, 14, 21, 30)) {
                colorList.add(Color.parseColor("#444444"))
            } else {
                colorList.add(Color.TRANSPARENT)
            }
        }
        chart.data.getDataSetByIndex(0).setValueTextColors(colorList)
    }

    private fun setData(datas: List<Float>) {
        var values = ArrayList<Entry>()

        var colorList = ArrayList<Int>()


        for (i in datas.indices) {
            if (i in listOf(0, 7, 14, 21, 30)) {
                values.add(Entry(i.toFloat(), datas[i], ContextCompat.getDrawable(this, R.drawable.bg_point)))
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
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue)
                dataSet.fillDrawable = drawable
            } else {
                dataSet.fillColor = Color.BLUE
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
