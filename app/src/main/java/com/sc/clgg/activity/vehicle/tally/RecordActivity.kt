package com.sc.clgg.activity.vehicle.tally

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.PopupWindow
import android.widget.Toast
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.sc.clgg.R
import com.sc.clgg.adapter.RecordAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Record
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CalendarHelper
import com.sc.clgg.widget.RecordPop
import kotlinx.android.synthetic.main.activity_record.*
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
        val title = intent.getStringExtra("title")

        costType = if (title == "记录收入") {
            "23"
        }else{
            "27"
        }

        account_name.text = intent.getStringExtra("str")
        initTitle(title)

        createTimeStamp = System.currentTimeMillis()
        tv_month.text = CalendarHelper.getCurrentMonth().toString()
        tv_day.text = CalendarHelper.getCurrentDay().toString()
        initListener()

        window.decorView.post {
            val poper = RecordPop()
            if (title == "记录收入") {
                mPopupWindow = poper.initIncome(this@RecordActivity)
                incomeList = poper.incomeList
            } else {
                mPopupWindow = poper.initSpending(this@RecordActivity)
                incomeList = poper.spendingList
            }

            adapter = poper.adapter

            poper.setItemClickListener { s1, s2, i ->
                costType = s1
                account_name.text = s2

                (incomeList as MutableList<Record>?)!!.forEach {
                    it.isChecked = false
                }
                (incomeList as MutableList<Record>?)!![i].isChecked = true

                mPopupWindow?.dismiss()
            }
            mPopupWindow?.showAsDropDown(tv_year, 0, 0)
        }

        tv_year.setOnClickListener {
            mTimePickerView?.show()
        }
        initTimePickerView()
    }

    private var mPopupWindow: PopupWindow? = null
    private var costType: String? = "23"
    private var adapter: RecordAdapter? = null
    private var incomeList: List<Record>? = null

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(c: CharSequence, start: Int, before: Int,
                                   count: Int) {
            var s = c
            if (s.toString().contains(".")) {
                if (s.length - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3)
                    account_num.setText(s)
                    account_num.setSelection(s.length)
                }
            }
            if (s.toString().trim { it <= ' ' }.substring(0) == ".") {
                s = "0$s"
                account_num.setText(s)
                account_num.setSelection(2)
            }
            if (s.toString().startsWith("0") && s.toString().trim { it <= ' ' }.length > 1) {
                if (s.toString().substring(1, 2) != ".") {
                    account_num.setText(s.subSequence(0, 1))
                    account_num.setSelection(1)
                    return
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private fun initListener() {
        account_name.setOnClickListener {
            adapter?.refresh(incomeList)
            mPopupWindow?.showAsDropDown(tv_year, 0, 0)
        }
        /*account_num.setTextChangeListener {
            if (!it.contains(".") && it.length > 6) {
                account_num.setText(it.substring(0, 6))
            }
        }*/
        account_num.addTextChangedListener(textWatcher)

        sure.setOnClickListener {
            var s1 = account_num.text.toString()
            if (s1.isEmpty()) {
                Toast.makeText(applicationContext, "请输入记账金额", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val index = s1.indexOf(".")
            if (s1.contains(".") && index < s1.length - 2) {
                s1 = s1.substring(0, index + 3)
            }

            if (costType!!.isEmpty()) {
                toast("请选择收入类型")
                return@setOnClickListener
            }

            val s2 = et_thing.text.toString()

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
                    } else {
                        toast("${map["msg"]}")
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

        mTimePickerView = TimePickerBuilder(this, OnTimeSelectListener { date, _ ->
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
                .setContentTextSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build()
    }

}
