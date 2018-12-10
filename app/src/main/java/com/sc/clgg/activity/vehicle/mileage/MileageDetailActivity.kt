package com.sc.clgg.activity.vehicle.mileage

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.sc.clgg.R
import com.sc.clgg.adapter.StatisticalDetailAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.MileageDetail
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_mileage_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MileageDetailActivity : BaseImmersionActivity() {
    var adpter: StatisticalDetailAdapter? = null
    var year: String? = ""
    var month: Int = 0
    var maximum: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mileage_detail)

        initTitle("统计详情")
        month = intent.getIntExtra("month", 0)
        year = intent.getStringExtra("year")
        val date = intent.getStringExtra("date")


        adpter = StatisticalDetailAdapter(year!!.toInt(), month, intent.getStringExtra("carno"), intent.getStringExtra("vin"))
        recyclerview?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerview?.adapter = adpter

        tv_date.text = date
        tv_carno.text = intent.getStringExtra("carno")

        val calendar = Calendar.getInstance()
        LogHelper.e("" + year + "\t\t" + month)
        calendar.set(year!!.toInt(), month, 0)
        maximum=  calendar.getActualMaximum(Calendar.DATE)
        tv4.text = "${maximum}日"

        loadData(date, intent.getStringExtra("vin"))
    }

    private lateinit var http: Call<MileageDetail>
    private fun loadData(date: String, vin: String) {
        http = RetrofitHelper().mileageDetail(date, vin).apply {
            showProgressDialog()
            enqueue(object : Callback<MileageDetail> {
                override fun onFailure(call: Call<MileageDetail>, t: Throwable) {
                    hideProgressDialog()
                }

                override fun onResponse(call: Call<MileageDetail>, response: Response<MileageDetail>) {
                    hideProgressDialog()
                    response.body()?.apply {
                        tv_mileage?.text = data?.intervalMileage

                        val indexs = ArrayList<Int>()
                        val data = ArrayList<Float>()

                        this.data?.detailDataList?.let {
                            it.forEach { (date, _) ->
                                indexs.add(date?.toInt()!!)
                            }

                            for (i in 1..maximum) {
                                if (i in indexs) {
                                    data.add(it[indexs.indexOf(i)].dayMileage?.toFloat()!!)
                                } else {
                                    data.add(0f)
                                }
                            }

                            adpter?.refresh(it)
                        }

                        initChart(data, (Math.ceil((Collections.max(data) / 100f).toDouble()) * 100f).toFloat())
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        http.cancel()
    }

    private fun initChart(data: List<Float>, maximum: Float) {
        LogHelper.e("图表数据 = " + data.size)

//        chart.layoutParams.width = App.screenWidth * data?.size / 5 - MeasureUtils.dp2px(this, 24f)

       // val xLabels = listOf("1日", "8日", "15日", "22日", "31日")

        chart.setDrawGridBackground(false)

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)
        //是否拖拽
        chart.isDragEnabled = false
        //是否缩放
        chart.setScaleEnabled(false)

        chart.setPinchZoom(true)

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
            set.setValueFormatter { value, _, _, _ ->
                value.toString()
            }
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
        val values = ArrayList<Entry>()

        val colorList = ArrayList<Int>()

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
            // create a dataset and give it a type

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
