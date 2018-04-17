package com.sc.clgg.activity.vehiclemanager.tallybook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.TallyBookAdapter;
import com.sc.clgg.application.App;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.TallyBookBean;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.Tools;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.CalendarHelper;
import tool.helper.CheckHelper;
import tool.helper.LogHelper;
import tool.helper.TextHelper;

/**
 * 我的记账本
 *
 * @author lvke
 */
public class MyTallyBookActivity extends BaseActivity {
    @BindView(R.id.tv_year_month) TextView mTvYearMonth;
    @BindView(R.id.tv_month_income) TextView mTvMonthIncome;
    @BindView(R.id.tv_month_spending) TextView mTvMonthSpending;
    @BindView(R.id.tv_month_balance) TextView mTvMonthBalance;

    @BindView(R.id.ll_month_start) LinearLayout mLlMonthStart;
    @BindView(R.id.ll_month_end) LinearLayout mLlMonthEnd;
    @BindView(R.id.tv_month_start) TextView mTvMonthStart;
    @BindView(R.id.tv_month_end) TextView mTvMonthEnd;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private TallyBookAdapter adapter;
    private TimePickerView mPvYeayMonth, mPvDay;
    /**
     * 当前时间
     */
    private Calendar selectedCalendar = Calendar.getInstance();
    /**
     * 开始时间
     */
    private Calendar startCalendar = Calendar.getInstance();
    /**
     * 结束时间
     */
    private Calendar endCalendar = Calendar.getInstance();
    /**
     * 起始日期  格式：yyyy-MM-dd
     */
    private String startDate, endDate;
    private int startDay, endDay;
    /**
     * 当前页面选中的年月
     */
    private int currentYear, currentMonth;
    private TimePickerView.Builder mBuilderMonth;
    private LoadingDialogHelper mLoadingDialogHelper;

    @OnClick({R.id.tv_year_month, R.id.ll_month_start, R.id.ll_month_end, R.id.tv_month_income, R.id.tv_month_spending, R.id.tv_remenmber})
    void a(View v) {
        switch (v.getId()) {
            case R.id.tv_year_month:
                mPvYeayMonth.show();
                break;
            case R.id.ll_month_start:
                mPvDay.show(mLlMonthStart);
                break;
            case R.id.ll_month_end:
                mPvDay.show(mLlMonthEnd);
                break;
            case R.id.tv_month_income:
                startActivity(new Intent(this, IncomeActivity.class).putExtra("year", selectedCalendar.get(Calendar.YEAR))
                        .putExtra("month", selectedCalendar.get(Calendar.MONTH) + 1));
                break;
            case R.id.tv_month_spending:
                startActivity(new Intent(this, SpendingActivity.class).putExtra("start", currentYear).putExtra("end", currentMonth));
                break;
            case R.id.tv_remenmber:
                startActivity(new Intent(this, TallyActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void initTitle() {
        setTitle(getString(R.string.my_tally_book));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_my_tally_book;
    }

    @Override
    protected void init() {
        mLoadingDialogHelper = new LoadingDialogHelper(this);
        LogHelper.e("当前时间：" + CalendarHelper.getCurrentYear() + CalendarHelper.getCurrentMonth() + CalendarHelper.getCurrentDay());
        initDatePcker();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData(currentYear, currentMonth, startDate, endDate);
    }

    private void initDatePcker() {
        startCalendar.set(2013, 5, 18);

        mPvYeayMonth = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {// 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                selectedCalendar.setTime(date);
                mPvDay = mBuilderMonth.build();

                int maxDay;
                int year = selectedCalendar.get(Calendar.YEAR);
                int month = selectedCalendar.get(Calendar.MONTH) + 1;

                mTvYearMonth.setText(TextHelper.instance.setTextStyle(App.getInstance(), year + "年\n", 12, R.color.white, month + "月", 20, R.color.white));

                mTvMonthStart.setText(TextHelper.instance.zeroPadding(month) + "\t01");

                if (CalendarHelper.getCurrentMonth() == month) {
                    mTvMonthEnd.setText(getString(R.string.today));

                    startDate = year + "-" + TextHelper.instance.zeroPadding(month) + "-01";
                    endDate = year + "-" + TextHelper.instance.zeroPadding(month) + "-" + TextHelper.instance.zeroPadding(CalendarHelper.getCurrentDay());

                    startDay = 1;
                    endDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    maxDay = selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    mTvMonthEnd.setText(TextHelper.instance.zeroPadding(month) + "\t" + TextHelper.instance.zeroPadding(maxDay));

                    startDate = year + "-" + TextHelper.instance.zeroPadding(month) + "-01";
                    endDate = year + "-" + TextHelper.instance.zeroPadding(month) + "-" + TextHelper.instance.zeroPadding(maxDay);

                    startDay = 1;
                    endDay = maxDay;
                }

                LogHelper.e("startDate = " + startDate + "\t\tendDate = " + endDate);
                loadData(year, month, null, null);
            }
        }).setType(new boolean[]{true, true, false, false, false, false})//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

        mBuilderMonth = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                if (v.getId() == R.id.ll_month_start) {
                    startDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if (calendar.get(Calendar.DAY_OF_MONTH) > endDay) {
                        Tools.Toast("开始日期不可大于结束日期");
                        LogHelper.e("startDay = " + startDay + "\t\t\tendDay = " + endDay);
                        return;
                    }

                    selectedCalendar.setTime(date);
                    mTvMonthStart.setText(TextHelper.instance.zeroPadding(selectedCalendar.get(Calendar.MONTH) + 1) + "\t" + TextHelper.instance.zeroPadding(selectedCalendar.get(Calendar.DAY_OF_MONTH)));

                    startDate = CalendarHelper.getDateString(date);
                }

                if (v.getId() == R.id.ll_month_end) {
                    endDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if (calendar.get(Calendar.DAY_OF_MONTH) < startDay) {
                        Tools.Toast("结束日期不可小于开始日期");
                        LogHelper.e("startDay = " + startDay + "\t\t\tendDay = " + endDay);
                        return;
                    }

                    selectedCalendar.setTime(date);
                    int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                    int month = selectedCalendar.get(Calendar.MONTH) + 1;

                    if (day == CalendarHelper.getCurrentDay() && month == CalendarHelper.getCurrentMonth()) {
                        mTvMonthEnd.setText(getString(R.string.today));
                    } else {
                        mTvMonthEnd.setText(TextHelper.instance.zeroPadding(selectedCalendar.get(Calendar.MONTH) + 1) + "\t" + TextHelper.instance.zeroPadding(day));
                    }
                    endDate = CalendarHelper.getDateString(date);
                }

                LogHelper.e("startDate = " + startDate + "\t\tendDate = " + endDate);
                loadData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, startDate, endDate);
            }
        }).setType(new boolean[]{false, false, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null);
        mPvDay = mBuilderMonth.build();
    }

    private void initData() {
        adapter = new TallyBookAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        startDate = calendar.get(Calendar.YEAR) + "-" + TextHelper.instance.zeroPadding(calendar.get(Calendar.MONTH) + 1) + "-01";
        endDate = calendar.get(Calendar.YEAR) + "-" + TextHelper.instance.zeroPadding(calendar.get(Calendar.MONTH) + 1) + "-" + TextHelper.instance.zeroPadding(calendar.get(Calendar.DAY_OF_MONTH));
        LogHelper.e("startDate = " + startDate + "\t\tendDate = " + endDate);
        startDay = 1;
        endDay = CalendarHelper.getCurrentDay();
        LogHelper.e("startDay = " + startDay + "\t\t\tendDay = " + endDay);

        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH) + 1;
        mTvYearMonth.setText(TextHelper.instance.setSpannable(App.getInstance(),
                new String[]{year + "年\n", String.valueOf(month), " 月"},
                new int[]{R.color.white, R.color.white, R.color.white},
                new int[]{12, 22, 12}));

        mTvMonthStart.setText(TextHelper.instance.zeroPadding(selectedCalendar.get(Calendar.MONTH) + 1) + "\t01");
        mTvMonthEnd.setText(getString(R.string.today));

        loadData(year, month, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPvYeayMonth = null;
        mPvDay = null;
    }

    private void loadData(int year, int month, String start, String end) {
        currentYear = year;
        currentMonth = month;
        HttpRequestHelper.tallyBookHome(new ConfigUtil().getUserid(), String.valueOf(year), String.valueOf(month), start, end, new HttpCallBack() {

            @Override
            public void onStart() {
                super.onStart();
                mLoadingDialogHelper.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mLoadingDialogHelper.dismiss();
            }

            @Override
            public void onSuccess(String body) {
                try {
                    if (!CheckHelper.isJson(body)) {
                        return;
                    }
                    TallyBookBean tallyBookBeanBean = new Gson().fromJson(body, TallyBookBean.class);

                    double income = tallyBookBeanBean.getData().getIncome();
                    double expend = tallyBookBeanBean.getData().getExpend();

                    mTvMonthBalance.setText(TextHelper.instance.setTextStyle(App.getInstance(), "本月结余(元)\n", 12, R.color.white,
                            new DecimalFormat("0.00").format(income - expend), 20, R.color.yellow));
                    mTvMonthIncome.setText(TextHelper.instance.setTextStyle(App.getInstance(), getString(R.string.month_income), 12, R.color.white,
                            "\n" + new DecimalFormat("0.00").format(income), 20, R.color.white));
                    mTvMonthSpending.setText(TextHelper.instance.setTextStyle(App.getInstance(), getString(R.string.month_spending), 12, R.color.white,
                            "\n" + new DecimalFormat("0.00").format(expend), 20, R.color.white));

                    adapter.refresh(tallyBookBeanBean.getData().getWaybillList(), tallyBookBeanBean.getData().getCount());
                } catch (Exception e) {
                    LogHelper.e(e);
                }
            }
        });
    }
}
