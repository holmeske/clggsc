package com.sc.clgg.activity.vehiclemanager.tallybook

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bigkoo.pickerview.TimePickerView
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.IncomeAdapter
import com.sc.clgg.bean.IncomeBean
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import kotlinx.android.synthetic.main.activity_income.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.ActivityHelper
import java.text.DecimalFormat
import java.util.*

/**
 * 记账本收入明细
 *
 * @author lvke
 */
class IncomeActivity : AppCompatActivity() {

    private val mSelectedCalendar = Calendar.getInstance()
    private val mStartCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    private var mTimePickerView: TimePickerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_income)
        super.onCreate(savedInstanceState)
        init()
        ibtn_left.onClick { ActivityHelper.finishAcMove(this@IncomeActivity) }
        moreTxt.onClick { mTimePickerView!!.show() }
    }

    private fun init() {
        mStartCalendar.set(2013, 5, 18)
        mTimePickerView = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener { date, _ ->
            mSelectedCalendar.time = date
            loadData(mSelectedCalendar.get(Calendar.YEAR), mSelectedCalendar.get(Calendar.MONTH) + 1)
        }).setType(booleanArrayOf(true, true, false, false, false, false))
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(mSelectedCalendar)
                .setRangDate(mStartCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build()

        loadData(intent.getIntExtra("year", mSelectedCalendar.get(Calendar.YEAR)),
                intent.getIntExtra("month", mSelectedCalendar.get(Calendar.MONTH) + 1))
    }

    private fun loadData(startDate: Int, endDate: Int) {
        HttpRequestHelper.getIncomeList(startDate, endDate, object : HttpCallBack() {
            override fun onSuccess(body: String) {
                val (_, _, data) = Gson().fromJson(body, IncomeBean::class.java)

                tv_money.text = DecimalFormat("0.00").format(data!!.totalAmount)

                mList.adapter = IncomeAdapter(this@IncomeActivity, data.data)
            }
        })
    }

}
