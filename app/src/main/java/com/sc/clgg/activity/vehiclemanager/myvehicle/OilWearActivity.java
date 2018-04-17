package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * @author lvke
 */
public class OilWearActivity extends BaseActivity {
    private ExtraListView mList;
    private ScrollView sv_root;
    private Button next_week;
    private TextView baobiao, car_driver_count_m, car_driver_count_mtv, idle_time, idle_time_tv, idle_time_tv_m, car_driver_count_tv;
    private LinearLayout idle_percent_linear;
    private String carno = null;
    private int presentMonth = 0;  //最近周
    private int presentYear = 0;  //最近年
    private MileageAdapter adpter = null;
    private int progressMax = 0;
    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    private String[] paramo = null;
    private String[] paramt = null;

    @Override
    public void initTitle() {
        setTitle(getString(R.string.accom_car_con));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_oil_wear;
    }

    @Override
    protected void init() {
        idle_percent_linear = (LinearLayout) findViewById(R.id.idle_percent_linear);
        mList = (ExtraListView) findViewById(R.id.mList);
        sv_root = (ScrollView) findViewById(R.id.sv_root);
        next_week = (Button) findViewById(R.id.next_week);
        baobiao = (TextView) findViewById(R.id.baobiao);
        car_driver_count_m = (TextView) findViewById(R.id.car_driver_count_m);
        car_driver_count_mtv = (TextView) findViewById(R.id.car_driver_count_mtv);
        idle_time = (TextView) findViewById(R.id.idle_time);
        idle_time_tv = (TextView) findViewById(R.id.idle_time_tv);
        idle_time_tv_m = (TextView) findViewById(R.id.idle_time_tv_m);
        car_driver_count_tv = (TextView) findViewById(R.id.car_driver_count_tv);

        carno = getIntent().getStringExtra("carno");
        presentMonth = mCalendar.get(Calendar.MONTH) + 1;
        presentYear = mCalendar.get(Calendar.YEAR);

        loadData(carno, presentMonth, presentYear);
    }

    private void loadData(String carno, Object presentMonth, Object presentYear) {
        HttpRequestHelper.oilWear(carno, presentMonth + "", presentYear + "", new HttpCallBack() {

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
                MonRptBn bean = null;
                try {
                    bean = new Gson().fromJson(body, MonRptBn.class);
                    initView(bean);
                    setProgressMax(bean);
                    adpter.setData((ArrayList) bean.getList(), false, progressMax);
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
            car_driver_count_m.setText(bn.getTotalOilCost() == null ? "" : bn.getTotalOilCost());  //月油耗量
            idle_time.setText(bn.getAverageOilCostPhKm() == null ? "" : bn.getAverageOilCostPhKm());
            idle_percent_linear.setVisibility(View.GONE);
            car_driver_count_mtv.setText("升");
            car_driver_count_m.setTextColor(Color.rgb(198, 99, 120));
            car_driver_count_mtv.setTextColor(Color.rgb(198, 99, 120));
            idle_time_tv.setText("升/百公里");
            idle_time_tv_m.setText("平均油耗");

            car_driver_count_tv.setText("本月油耗");

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon_50_youhao_gray);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            car_driver_count_tv.setCompoundDrawables(drawable, null, null, null);

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
                    if (report != null && !TextUtils.isEmpty(report.getOilcost() + "")) {
                        int reportM = (int) report.getOilcost();
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
