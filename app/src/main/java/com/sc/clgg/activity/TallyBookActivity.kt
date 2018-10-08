package com.sc.clgg.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bigkoo.pickerview.TimePickerView
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.TallyBookAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.bean.TallyBook
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CalendarHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_tally_book.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TallyBookActivity : BaseImmersionActivity() {
    private var mTallyBookAdapter: TallyBookAdapter? = null
    private var call: Call<TallyBook>? = null
    var dataList: ArrayList<TallyBook.Info>? = null
    private var call_delete: Call<Check>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tally_book)

        initTitle("记账本")
        rv.layoutManager = LinearLayoutManager(this)
        mTallyBookAdapter = TallyBookAdapter()
        mTallyBookAdapter?.setOnDelListener(object : TallyBookAdapter.onSwipeListener {
            override fun onDel(id: Int, pos: Int) {
                call_delete = RetrofitHelper().remove(id)
                call_delete?.enqueue(object : Callback<Check> {
                    override fun onFailure(call: Call<Check>?, t: Throwable?) {
                        toast("删除失败")
                        loadData(year, month)
                    }

                    override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                        LogHelper.e("response: " + Gson().toJson(response?.body()))
                        var check = response?.body()
                        if (check?.success!!) {
                            dataList?.removeAt(pos)
                            mTallyBookAdapter?.notifyItemRemoved(pos)
                        }
                        loadData(year, month)
                    }
                })
            }
        })
        rv.adapter = mTallyBookAdapter

        initTimePickerView()

        year = CalendarHelper.getCurrentYear().toString()
        month = CalendarHelper.getCurrentMonth().toString()

        tv_year?.text = year
        tv_month?.text = month

        loadData(year, month)

        tv_year.onClick { mTimePickerView?.show() }

        record_income.onClick {
            startActivity(Intent(this@TallyBookActivity, RecordActivity::class.java)
                    .putExtra("title", "记录收入").putExtra("str", "工资").putExtra("recordType", "0"))
        }
        record_spending.onClick {
            startActivity(Intent(this@TallyBookActivity, RecordActivity::class.java)
                    .putExtra("title", "记录支出").putExtra("str", "高速费").putExtra("recordType", "1"))
        }
    }

    private var year: String? = ""
    private var month: String? = ""
    override fun onResume() {
        super.onResume()
        loadData(year, month)
    }

    private fun loadData(queryYear: String?, queryMonth: String?) {
        call = RetrofitHelper().tallybook(queryYear, queryMonth)
        call?.enqueue(object : Callback<TallyBook> {
            override fun onResponse(call: Call<TallyBook>?, response: Response<TallyBook>?) {
                var bean = response?.body()
                tv_income?.text = bean?.allIncome
                tv_spending?.text = bean?.allExpense
                bean?.allInfo?.let {
                    if (it.isNotEmpty()) {
                        it[0].show = true
                        for (i in 0..it.size - 1) {
                            if (i == it.size - 1) continue
                            it[i + 1].show = !it[i].costDate.equals(it[i + 1].costDate)
                        }
                    }
                    dataList = it as ArrayList<TallyBook.Info>
                    mTallyBookAdapter?.refresh(it)
                }
            }

            override fun onFailure(call: Call<TallyBook>?, t: Throwable?) {
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    private var mTimePickerView: TimePickerView? = null
    private val selectedCalendar = Calendar.getInstance()
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()
    private fun initTimePickerView() {
        startCalendar.set(CalendarHelper.getCurrentYear(), CalendarHelper.getCurrentMonth() - 1, 0)
        mTimePickerView = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener { date, v ->
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            selectedCalendar.time = date

            year = selectedCalendar.get(Calendar.YEAR).toString()
            month = (selectedCalendar.get(Calendar.MONTH) + 1).toString()

            tv_year?.text = year
            tv_month?.text = month
            loadData(year, month)
        }).setType(booleanArrayOf(true, true, false, false, false, false))//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null).build()
    }

}
