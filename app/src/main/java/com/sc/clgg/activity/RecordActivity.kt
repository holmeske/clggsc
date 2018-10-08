package com.sc.clgg.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.PopupWindow
import android.widget.Toast
import com.bigkoo.pickerview.TimePickerView
import com.sc.clgg.R
import com.sc.clgg.adapter.RecordAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Record
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CalendarHelper
import com.sc.clgg.util.setTextChangeListener
import com.sc.clgg.widget.RecordPop
import kotlinx.android.synthetic.main.activity_record.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RecordActivity : BaseImmersionActivity() {
    private var call: Call<Map<String, Any>>? = null
    private var recordType: String? = "0"
    private var createTimeStamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        recordType = intent.getStringExtra("recordType")
        var title = intent.getStringExtra("title")

        if (title=="记录收入"){
            costType="23"
        }else{
            costType="27"
        }

        account_name.text = intent.getStringExtra("str")
        initTitle(title)

        createTimeStamp = System.currentTimeMillis()
        tv_month.text = CalendarHelper.getCurrentMonth().toString()
        tv_day.text = CalendarHelper.getCurrentDay().toString()
        initListener()

        window.decorView.post {
            var poper = RecordPop()
            if (title == "记录收入") {
                mPopupWindow = poper.initIncome(this@RecordActivity)
                incomeList = poper.incomeList
            } else {
                mPopupWindow = poper.initSpending(this@RecordActivity)
                incomeList = poper.spendingList
            }

            adapter = poper.adapter

            poper.setItemClickListener(object : RecordAdapter.OnItemClickListener {
                override fun click(s1: String?, s2: String?, i: Int) {
                    costType = s1
                    account_name.text = s2

                    (incomeList as MutableList<Record>?)!!.forEach {
                        it.isChecked = false
                    }
                    (incomeList as MutableList<Record>?)!![i].isChecked = true

                    mPopupWindow?.dismiss()
                }
            })
            mPopupWindow?.showAsDropDown(tv_year, 0, 0)
        }

        tv_year.onClick {
            mTimePickerView?.show()
        }
        initTimePickerView()
    }

    private var mPopupWindow: PopupWindow? = null
    private var costType: String? = "23"
    private var adapter: RecordAdapter? = null
    private var incomeList: List<Record>? = null

    private fun initListener() {
        account_name.onClick {
            adapter?.refresh(incomeList)
            mPopupWindow?.showAsDropDown(tv_year, 0, 0)
        }
        account_num.setTextChangeListener {
            if (!it.contains(".") && it.length > 6) {
                account_num.setText(it.substring(0, 6))
            }
        }

        sure.onClick {
            var s1 = account_num.text.toString()
            if (s1.isEmpty()) {
                Toast.makeText(applicationContext, "请输入记账金额", Toast.LENGTH_SHORT).show()
                return@onClick
            }

            val index = s1.indexOf(".")
            if (s1.contains(".") && index < s1.length - 2) {
                s1 = s1.substring(0, index + 3)
            }

            if (costType!!.isEmpty()) {
                toast("请选择收入类型")
                return@onClick
            }

            var s2 = et_thing.text.toString()

            showProgressDialog()
            call = RetrofitHelper().add(createTimeStamp, s1, recordType, costType, s2)
            call?.enqueue(object : Callback<Map<String, Any>> {
                override fun onFailure(call: Call<Map<String, Any>>?, t: Throwable?) {
                    hideProgressDialog()
                }

                override fun onResponse(call: Call<Map<String, Any>>?, response: Response<Map<String, Any>>?) {
                    hideProgressDialog()
                    val map = response?.body()
                    if (map?.get("success") as Boolean) {
                        toast("记账成功")
                        finish()
                    }
                }
            })
        }

    }

    private var mTimePickerView: TimePickerView? = null
    private val selectedCalendar = Calendar.getInstance()
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    private fun initTimePickerView() {
        startCalendar.set(CalendarHelper.getCurrentYear(), CalendarHelper.getCurrentMonth() - 1, CalendarHelper.getCurrentDay() - 7)
        endCalendar.set(CalendarHelper.getCurrentYear(), CalendarHelper.getCurrentMonth() - 1, CalendarHelper.getCurrentDay())

        mTimePickerView = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener { date, v ->
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            selectedCalendar.time = date

            createTimeStamp = date.time

            tv_year.text = selectedCalendar.get(Calendar.YEAR).toString()
            tv_month.text = (selectedCalendar.get(Calendar.MONTH) + 1).toString()
            tv_day.text = selectedCalendar.get(Calendar.DAY_OF_MONTH).toString()

        }).setType(booleanArrayOf(true, true, true, false, false, false))//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build()
    }

}
