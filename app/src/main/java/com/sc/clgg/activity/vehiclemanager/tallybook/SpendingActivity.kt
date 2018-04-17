package com.sc.clgg.activity.vehiclemanager.tallybook

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bigkoo.pickerview.TimePickerView
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.SpendingAdapter
import com.sc.clgg.bean.ExpendListBean
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import kotlinx.android.synthetic.main.activity_spending.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.ActivityHelper
import java.text.DecimalFormat
import java.util.*

//记账本 - 支出
class SpendingActivity : AppCompatActivity() {
    private var mAdapter: SpendingAdapter? = null
    private var mTimePickerView: TimePickerView? = null
    private val mSelectedCalendar = Calendar.getInstance(Locale.CHINA)
    private val mStartCalendar = Calendar.getInstance(Locale.CHINA)
    private val endCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_spending)
        tv_title?.setText(R.string.notebook_detail)
        super.onCreate(savedInstanceState)
        tv_tip?.setText(R.string.str_expend_tip)

        init()
        ibtn_left.onClick { ActivityHelper.finishAcMove(this@SpendingActivity) }
        moreTxt.onClick { mTimePickerView?.show() }
    }

    fun init() {
        mStartCalendar.set(2013, 5, 18)
        mTimePickerView = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener { date, _ ->
            mSelectedCalendar.time = date
            loadData(mSelectedCalendar.get(Calendar.YEAR), mSelectedCalendar.get(Calendar.MONTH) + 1)
        }).setType(booleanArrayOf(true, true, false, false, false, false))//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(mSelectedCalendar)
                .setRangDate(mStartCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build()

        loadData(intent.getIntExtra("start", 0), intent.getIntExtra("end", 0))
    }

    private fun loadData(year: Int, month: Int) {
        HttpRequestHelper.expendList(year, month, object : HttpCallBack() {
            override fun onSuccess(body: String) {
                Gson().fromJson(body, ExpendListBean::class.java).let {
                    tv_money?.text = DecimalFormat("0.00").format(it.data?.countPrice)
                    mAdapter = SpendingAdapter(this@SpendingActivity, it)
                    mList?.adapter = mAdapter
                    mAdapter?.setData(it, year, month)
                }
            }
        })
    }

}
