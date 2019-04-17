package com.sc.clgg.activity.vehicle.energy

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
import com.sc.clgg.adapter.ConsumptionDetailAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.ConsumptionDetail
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.DecimalFormatHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_consumption_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ConsumptionDetailActivity : BaseImmersionActivity() {
    var adpter: ConsumptionDetailAdapter? = null
    private var dateStr: String? = null

    var vin: String? = ""
    var carno: String? = ""
    var maximum: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_detail)

        initTitle("统计详情")
        dateStr = intent.getStringExtra("date")
        vin = intent.getStringExtra("vin")
        carno = intent.getStringExtra("carno")

        tv_month_mileage?.text = carno

        adpter = ConsumptionDetailAdapter()
        recyclerview?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerview?.adapter = adpter


        tv_date?.text = dateStr
        loadData(dateStr)

        val calendar = Calendar.getInstance()
        LogHelper.e(""+intent.getIntExtra("year",0)+"\t\t"+intent.getIntExtra("month",0))
        calendar.set(intent.getIntExtra("year",0), intent.getIntExtra("month",0), 0)
        maximum=  calendar.getActualMaximum(Calendar.DATE)
        tv4.text = "${maximum}日"
    }

    private var call: Call<ConsumptionDetail>? = null

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    private fun loadData(date: String?) {

        call = RetrofitHelper().oilsteamsDetail(vin, date)
        showProgressDialog()
        call?.enqueue(object : Callback<ConsumptionDetail> {
            override fun onFailure(call: Call<ConsumptionDetail>?, t: Throwable?) {
                hideProgressDialog()
            }

            override fun onResponse(call: Call<ConsumptionDetail>?, response: Response<ConsumptionDetail>?) {
                hideProgressDialog()

                response?.body()?.data?.get(0)?.let {


                    tv_day_mileage?.text = DecimalFormatHelper.formatTwo(it.oilGassEntity?.totalFuel)
                    one_vehice_month_mileage?.text = DecimalFormatHelper.formatTwo(it.oilGassEntity?.hundredFuel)

                    val indexs = java.util.ArrayList<Int>()
                    val data = java.util.ArrayList<Float>()

                    for ((_, _, _, clctDate) in it.dayDetails!!) {
                        indexs.add(clctDate?.substring(6, 8)!!.toInt())
                    }

                    for (i in 1..maximum) {
                        if (i in indexs) {
                            data.add(it.dayDetails?.get(indexs.indexOf(i))?.totalFuel?.toFloat()!!)
                        } else {
                            data.add(0f)
                        }
                    }


                    LogHelper.e("mileage.details = " + it.dayDetails?.size)

                    adpter?.refresh(it.dayDetails)

                    initChart(data, (Math.ceil((Collections.max(data) / 100f).toDouble()) * 100f).toFloat())
                }
            }
        })

    }


    private fun initChart(data: List<Float>, maximum: Float) {
        LogHelper.e("图表数据 = " + data.size)

//        chart.layoutParams.width = App.screenWidth * data?.size / 5 - MeasureUtils.dp2px(this, 24f)
        chart.setNoDataText("暂无数据")
        //val xLabels = listOf("1日", "8日", "15日", "22日", "31日")

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
//      xAxis.setLabelCount(xLabels.size, true)

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
        val values = ArrayList<Entry>()

        val colorList = ArrayList<Int>()


        for (i in datas.indices) {
            if (i in listOf(0, 7, 14, 21, 30)) {
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
