package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.MileageAdapter;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.MonRptBn;
import com.sc.clgg.bean.Report;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;
import com.sc.clgg.view.ExtraListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * 里程
 *
 * @author lvke
 */
public class MileageActivity extends BaseActivity {

    private ExtraListView mList;
    private ScrollView sv_root;
    private Button next_week, last_week;
    private TextView baobiao, car_driver_count_m, idle_time, idle_percent, car_driver_count, fuel_for_100_km;
    private String carno = null;
    private int presentMonth = 0;  //最近周
    private int presentYear = 0;  //最近年
    private int monDrive = 0;  //周驾驶时间
    private int monPercent = 0; //周怠速比
    private int progressMax = 0;
    private List<Report> monthRpt = null;
    private MileageAdapter adpter = null;
    private int nearMonth = 0;
    private int nearYear = 0;
    private boolean initNearTime = true;
    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    private String[] paramo = null;
    private String[] paramt = null;


    @Override
    public void initTitle() {
        setTitle(getString(R.string.accom_car_mile));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_oil_wear;
    }

    @Override
    protected void init() {
        mList = (ExtraListView) findViewById(R.id.mList);
        sv_root = (ScrollView) findViewById(R.id.sv_root);
        next_week = (Button) findViewById(R.id.next_week);
        last_week = (Button) findViewById(R.id.last_week);

        baobiao = (TextView) findViewById(R.id.baobiao);
        car_driver_count_m = (TextView) findViewById(R.id.car_driver_count_m);
        idle_time = (TextView) findViewById(R.id.idle_time);
        idle_percent = (TextView) findViewById(R.id.idle_percent);


        carno = getIntent().getStringExtra("carno");

        presentMonth = mCalendar.get(Calendar.MONTH) + 1;
        presentYear = mCalendar.get(Calendar.YEAR);

        loadData(carno, presentMonth, presentYear);
    }

    private void loadData(String carno, Object presentMonth, Object presentYear) {
        HttpRequestHelper.mileage(carno, presentMonth + "", presentYear + "", new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String body) {
                MonRptBn bn = null;
                try {
                    bn = new Gson().fromJson(body, MonRptBn.class);
                    if (initNearTime) {
                        nearMonth = mCalendar.get(Calendar.MONTH);
                        nearYear = mCalendar.get(Calendar.YEAR);
                        initNearTime = !initNearTime;
                    }
                    initView(bn);
                    setProgressMax(bn);
                    adpter.setData((ArrayList) bn.getList(), true, progressMax);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                sv_root.smoothScrollTo(0, 0);
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                Tools.Toast(getString(R.string.network_anomaly));
            }
        });
    }

    public void initView(MonRptBn bn) {
        if (bn != null) {
            monthRpt = bn.getList();
            if (bn.getTotalMileage() != null && bn.getTotalMileage() > 0)
                car_driver_count_m.setText(new DecimalFormat("#.00").format(bn.getTotalMileage()) + "");
            else
                car_driver_count_m.setText("0");

            idle_time.setText(bn.getTotalRunningTime() == null ? "" : bn.getTotalRunningTime());
            idle_percent.setText(bn.getAverageIdlePercentage() == null ? "" : bn.getAverageIdlePercentage());

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon_40_licheng);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) findViewById(R.id.car_driver_count_tv)).setCompoundDrawables(drawable, null, null, null);

            adpter = new MileageAdapter(this);
            mList.setAdapter(adpter);

            baobiao.setText(presentYear + "年" + presentMonth + "月报表");

            if (presentMonth >= (mCalendar.get(Calendar.MONTH) + 1) && mCalendar.get(Calendar.YEAR) == presentYear)  //月大于等于当前月  且在今年
                next_week.setVisibility(View.GONE);
            else
                next_week.setVisibility(View.VISIBLE);
        }
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.next_week:     //下一月
                if (presentMonth + 1 > 12) {
                    presentMonth = 0;
                    presentYear += 1;
                }
                loadData(carno, ++presentMonth, presentYear);
                break;
            case R.id.last_week:    //上一月
                if (presentMonth - 1 < 1) {
                    presentMonth = 13;
                    presentYear -= 1;
                }
                loadData(carno, --presentMonth, presentYear);
                break;
            case R.id.baobiao:
                showDatePickerDialog(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                break;
            default:
                break;
        }
    }


    private void setProgressMax(MonRptBn bn) {
        if (bn != null) {
            if (bn.getList() != null && bn.getList().size() > 0) {
                for (Report report : bn.getList()) {
                    if (report != null && !TextUtils.isEmpty(report.getWorkingmileage() + "")) {
                        int reportM = (int) report.getWorkingmileage();
                        if (progressMax < reportM) {
                            progressMax = reportM;
                        }
                    }
                }
            }
        }
    }

    public void showDatePickerDialog(final Context mContext, final int year, final int week) {
        Calendar calendar = Calendar.getInstance();

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_area_picker, null);

        final NumberPicker num_f = (NumberPicker) inflate.findViewById(R.id.picker_province);

        TextView tv_weekormon = (TextView) inflate.findViewById(R.id.tv_weekormon);

        tv_weekormon.setText("月");

        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.WEEK_OF_YEAR, week);

        paramo = new String[year - 1970 + 1];  //46

        int j = 0;

        for (int i = year; i >= 1970; i--) {  //2016
            paramo[j] = i + "";
            j++;
        }

        num_f.setMinValue(0);
        num_f.setMaxValue(paramo.length - 1);
        num_f.setWrapSelectorWheel(false);
        num_f.setDisplayedValues(paramo);  //默认第一个

        paramt = new String[week];

        int k = 0;

        for (int i = week; i >= 1; i--) {
            paramt[k] = i + "";
            k++;
        }

        final NumberPicker num_t = (NumberPicker) inflate.findViewById(R.id.picker_city);
        num_t.setMinValue(0);
        num_t.setMaxValue(week - 1);
        num_t.setWrapSelectorWheel(false);
        num_t.setDisplayedValues(paramt);

        num_f.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChange(final NumberPicker view, int scrollState) {
                switch (scrollState) {
                    case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                        ((Activity) mContext).runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                paramt = null;
                                int k = 0;
                                if (!paramo[view.getValue()].equals(year + "")) {
                                    paramt = new String[12];
                                    for (int i = 12; i >= 1; i--) {
                                        paramt[k] = i + "";
                                        k++;
                                    }
                                } else {
                                    paramt = new String[week];
                                    for (int i = week; i >= 1; i--) {
                                        paramt[k] = i + "";
                                        k++;
                                    }
                                }
                                num_t.setDisplayedValues(null);
                                num_t.setMaxValue(paramt.length - 1);
                                num_t.setWrapSelectorWheel(false);
                                num_t.setDisplayedValues(paramt);  //默认第一个
                            }
                        }));

                        break;
                    default:
                        break;
                }
            }
        });
//    	num_t.setOnValueChangedListener(this);
//    	num_t.setFormatter(this);

        final AlertDialog.Builder materialDialog = new AlertDialog.Builder(mContext);
        materialDialog
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mon_or_week = num_t.getDisplayedValues()[num_t.getValue()];
                        String year = num_f.getDisplayedValues()[num_f.getValue()];
                        presentMonth = Integer.parseInt(mon_or_week);
                        presentYear = Integer.parseInt(year);

                        loadData(carno, mon_or_week, year);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();


    }
}
