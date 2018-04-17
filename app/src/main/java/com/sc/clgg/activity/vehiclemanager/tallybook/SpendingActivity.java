//package com.sc.clgg.activity.vehiclemanager.tallybook;
//
//import android.graphics.Color;
//import android.view.View;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.TimePickerView;
//import com.google.gson.Gson;
//import com.sc.clgg.R;
//import com.sc.clgg.adapter.ExpenditureListAd;
//import com.sc.clgg.base.BaseActivity;
//import com.sc.clgg.bean.ExpendListBean;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.view.ExtraListView;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import toolbox.helper.ActivityHelper;
//
////记账本 - 支出
//public class SpendingActivity extends BaseActivity {
//    @BindView(R.id.tv_title) TextView tv_title;
//    @BindView(R.id.tv_money) TextView tv_money;
//    @BindView(R.id.mList) ExtraListView mList;
//    @BindView(R.id.tv_tip) TextView tv_tip;
//    private ExpenditureListAd adapter;
//    private TimePickerView mTimePickerView;
//    private Calendar mSelectedCalendar = Calendar.getInstance(Locale.CHINA);
//    private Calendar mStartCalendar = Calendar.getInstance(Locale.CHINA);
//    private Calendar endCalendar = Calendar.getInstance();
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_spending;
//    }
//
//    @Override
//    protected void init() {
//        tv_title.setText(R.string.notebook_detail);
//        tv_tip.setText(R.string.str_expend_tip);
//
//        mStartCalendar.set(2013, 5, 18);
//        mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                mSelectedCalendar.setTime(date);
//                loadData(mSelectedCalendar.get(Calendar.YEAR), mSelectedCalendar.get(Calendar.MONTH) + 1);
//            }
//        }).setType(new boolean[]{true, true, false, false, false, false})//年月日时分秒 的显示与否，不设置则默认全部显示
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
//        loadData(getIntent().getIntExtra("start", 0), getIntent().getIntExtra("end", 0));
//    }
//
//    private void loadData(final int year, final int month) {
//        HttpRequestHelper.expendList(year, month, new HttpCallBack() {
//            @Override
//            public void onSuccess(String body) {
//                ExpendListBean bean = new Gson().fromJson(body, ExpendListBean.class);
//                tv_money.setText(new DecimalFormat("0.00").format(bean.getData().getCountPrice()));
//                if (adapter == null) {
//                    adapter = new ExpenditureListAd(SpendingActivity.this, bean);
//                    mList.setAdapter(adapter);
//                    adapter.setData(bean, year, month);
//                } else {
//                    adapter.setData(bean, year, month);
//                }
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
//}
