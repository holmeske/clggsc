//package com.sc.clgg.activity.vehiclemanager.tallybook;
//
//import android.graphics.Color;
//import android.view.View;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.TimePickerView;
//import com.google.gson.Gson;
//import com.sc.clgg.R;
//import com.sc.clgg.adapter.IncomeAdapter;
//import com.sc.clgg.base.BaseActivity;
//import com.sc.clgg.bean.IncomeBean;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.view.ExtraListView;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import butterknife.OnClick;
//import toolbox.helper.ActivityHelper;
//
///**
// * 记账本收入明细
// *
// * @author lvke
// */
//public class IncomeActivity extends BaseActivity {
//
//    private Calendar mSelectedCalendar = Calendar.getInstance();
//    private Calendar mStartCalendar = Calendar.getInstance();
//    private Calendar endCalendar = Calendar.getInstance();
//
//    private IncomeAdapter adapter;
//    private TimePickerView mTimePickerView;
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_income;
//    }
//
//    @Override
//    protected void init() {
//        mStartCalendar.set(2013, 5, 18);
//        mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                mSelectedCalendar.setTime(date);
//                loadData(mSelectedCalendar.get(Calendar.YEAR), mSelectedCalendar.get(Calendar.MONTH) + 1);
//            }
//        }).setType(new boolean[]{true, true, false, false, false, false})
//                .setLabel("", "", "", "", "", "")
//                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(21)
//                .setDate(mSelectedCalendar)
//                .setRangDate(mStartCalendar, endCalendar)
//                .setBackgroundId(R.color.white) //设置外部遮罩颜色
//                .setDecorView(null)
//                .build();
//
//        loadData(getIntent().getIntExtra("year", mSelectedCalendar.get(Calendar.YEAR)),
//                getIntent().getIntExtra("month", mSelectedCalendar.get(Calendar.MONTH) + 1));
//    }
//
//    private void loadData(int startDate, int endDate) {
//        HttpRequestHelper.getIncomeList(startDate, endDate, new HttpCallBack() {
//            @Override
//            public void onSuccess(String body) {
//                IncomeBean bean = new Gson().fromJson(body, IncomeBean.class);
//
//                ((TextView) findViewById(R.id.tv_money)).setText(new DecimalFormat("0.00").format(bean.getData().getTotalAmount()));
//
//                adapter = new IncomeAdapter(IncomeActivity.this, bean.getData().getData());
//                ((ExtraListView) findViewById(R.id.mList)).setAdapter(adapter);
//            }
//        });
//    }
//
//    @OnClick({R.id.ibtn_left, R.id.moreTxt})
//    void a(View v) {
//        switch (v.getId()) {
//            case R.id.ibtn_left:
//                ActivityHelper.finishAcMove(this);
//                break;
//            case R.id.moreTxt:
//                mTimePickerView.show();
//                break;
//            default:
//                break;
//        }
//    }
//
//}
