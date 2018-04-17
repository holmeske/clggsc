package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.PathwayBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.TimeHelper;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import tool.helper.LogHelper;

/**
 * 历史轨迹
 *
 * @author lvke
 */
public class HistoricalRouteActivity extends BaseActivity {

    @BindView(R.id.next_week) public Button next_week;
    @BindView(R.id.last_week) public Button last_week;
    private TextView baobiao, tv_day, tv_weeek, pathway;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    private Calendar birthdayModify = Calendar.getInstance();
    private String carNo;
    private PathwayBean mPathwayBean;
    private String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Override
    protected int layoutRes() {
        return R.layout.activity_historical_route;
    }

    @Override
    public void initTitle() {
        super.initTitle();
        setTitle(getString(R.string.accom_car_pathway));
    }

    @Override
    protected void init() {
        baobiao = (TextView) findViewById(R.id.baobiao);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_weeek = (TextView) findViewById(R.id.tv_weeek);
        pathway = (TextView) findViewById(R.id.pathway);

        Intent intent = getIntent();

        carNo = intent.getStringExtra("truckNo");
        // 指定一个日期
        clearData();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        baobiao.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "途径");
        tv_day.setText((calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
        tv_weeek.setText(calendar.get(Calendar.DAY_OF_WEEK) > 0 ? weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1] : "");

        mCalendar.add(Calendar.DAY_OF_MONTH, -1);

        loadData(carNo, mCalendar);
    }

    private void loadData(String carNo, Calendar calendar) {
        HttpRequestHelper.historicalRoute(carNo, TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "000000",
                TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "235959", new HttpCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            mPathwayBean = new Gson().fromJson(body, PathwayBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        initView();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgressDialog();
                        next_week.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideProgressDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                next_week.setEnabled(true);
                            }
                        }, 1000);
                    }
                });
    }

    public void initView() {
        baobiao.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "途径");
        tv_day.setText((calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
        tv_weeek.setText(calendar.get(Calendar.DAY_OF_WEEK) > 0 ? weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1] : "");

//        if (mPathwayBean != null) {
//            if (mPathwayBean.getPassCity() != null && mPathwayBean.getPassCity().size() > 0) {
//                StringBuffer buffer = new StringBuffer();
//                for (int i = 0; i < mPathwayBean.getPassCity().size(); i++) {
//                    if (i == mPathwayBean.getPassCity().size() - 1) {
//                        buffer.append(mPathwayBean.getPassCity().get(i).getName());
//                    } else {
//                        buffer.append(mPathwayBean.getPassCity().get(i).getName() + "-");
//                    }
//                }
//                pathway.setText(buffer.toString());
//            }
//        }
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.next_week:     //下一天
                clearData();
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                loadData(carNo, calendar);
                break;
            case R.id.last_week:    //上一天
                clearData();
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                loadData(carNo, calendar);
                break;
            case R.id.baobiao:
                showDatePickerDialog();
                break;
            case R.id.relative_t:
                Intent intent = new Intent(this, LocusFreeActivity.class);
                intent.putExtra("carno", carNo);
                intent.putExtra("gps", mPathwayBean);
                LogHelper.e(mPathwayBean.toString());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void clearData() {
        baobiao.setText("");
        tv_day.setText("");
        tv_weeek.setText("");
        pathway.setText("");
    }


    /***
     * 时间选择器
     */
    public void showDatePickerDialog() {
        DatePicker datePicker = new DatePicker(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setCalendarViewShown(false);
            datePicker.setMaxDate(System.currentTimeMillis());
        }
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayModify.set(Calendar.YEAR, year);
                birthdayModify.set(Calendar.MONTH, monthOfYear);
                birthdayModify.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    int MAX = 2015;
                    if (year > MAX) {
                        view.updateDate(MAX, monthOfYear, dayOfMonth);
                        birthdayModify.set(Calendar.YEAR, MAX);
                    }
                }
            }
        });

        final AlertDialog.Builder materialDialog = new AlertDialog.Builder(this);
        materialDialog
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendar.set(Calendar.YEAR, birthdayModify.get(Calendar.YEAR));
                        calendar.set(Calendar.MONTH, birthdayModify.get(Calendar.MONTH));
                        calendar.set(Calendar.DAY_OF_MONTH, birthdayModify.get(Calendar.DAY_OF_MONTH));
                        dialog.dismiss();

                        clearData();
                        showProgressDialog();
                        loadData(carNo, calendar);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hideProgressDialog();
                    }
                }).show();
    }
}
