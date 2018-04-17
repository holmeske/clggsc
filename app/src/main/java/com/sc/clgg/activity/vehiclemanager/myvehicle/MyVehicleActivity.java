//package com.sc.clgg.activity.vehiclemanager.myvehicle;
//
//import android.app.DatePickerDialog;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.lzy.okgo.OkGo;
//import com.sc.clgg.R;
//import com.sc.clgg.adapter.MyVehicleAdapter;
//import com.sc.clgg.base.BaseAppCompatActivity;
//import com.sc.clgg.bean.MyVehicleBean;
//import com.sc.clgg.bean.VehicleLocationBean;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.util.ConfigUtil;
//import com.sc.clgg.util.TimeHelper;
//import com.sc.clgg.util.Tools;
//
//import java.util.Calendar;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import toolbox.helper.ActivityHelper;
//import toolbox.helper.Compat;
//import toolbox.helper.DateUtil;
//import toolbox.helper.LogHelper;
//import toolbox.helper.MeasureUtils;
//
///**
// * 我的车辆
// *
// * @author lvke
// */
//public class MyVehicleActivity extends BaseAppCompatActivity {
//    @BindView(R.id.tv_count_mileage) TextView tv_count_mileage;
//    @BindView(R.id.tv_count_date) TextView tv_count_date;
//    @BindView(R.id.lv) ListView lv;
//    @BindView(R.id.titlebar_title) TextView tv_title;
//    @BindView(R.id.titlebar_right_text) TextView tv_right;
//    private Unbinder unbinder;
//    private MyVehicleAdapter adapter;
//    private MyVehicleBean mMyVehicleBean;
//    private Double countMile = 0d;
//    private Calendar calendar = Calendar.getInstance();
//    private String date;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_vehicle);
//        unbinder = ButterKnife.bind(this);
//
//        tv_title.setText("我的车辆");
//        tv_right.setText("+ 添加车辆");
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_right.getLayoutParams();
//        layoutParams.setMargins(0, 0, MeasureUtils.dp2px(getApplicationContext(), 10), 0);
//        layoutParams.height = MeasureUtils.dp2px(getApplicationContext(), 30);
//        tv_right.setPadding(MeasureUtils.dp2px(getApplicationContext(), 6), 0, MeasureUtils.dp2px(getApplicationContext(), 6), 0);
//        tv_right.setTextSize(12);
//        tv_right.setVisibility(View.VISIBLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            tv_right.setBackground(ContextCompat.getDrawable(this, R.drawable.add_car_list_selector));
//        }
//
//        init();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (unbinder != Unbinder.EMPTY) {
//            unbinder.unbind();
//        }
//        OkGo.getInstance().cancelTag("okgo");
//    }
//
//    @OnClick(R.id.titlebar_left)
//    void c() {
//        finish();
//    }
//
//    @OnClick(R.id.tv_count_date)
//    void a() {
//        showDatePickerDialog();
//    }
//
//    @OnClick(R.id.titlebar_right_text)
//    void b() {
//        ActivityHelper.startAcScale(this, AddVehicleActivity.class);
//    }
//
//
//    private void init() {
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        date = DateUtil.format(calendar.getTimeInMillis());
//
//        adapter = new MyVehicleAdapter(this, date);
//        lv.setAdapter(adapter);
//
//        adapter.setCallbackListener(() -> loadData(date));
//
//        tv_count_date.setText(DateUtil.format("yyyy年MM月dd日", calendar.getTimeInMillis()));
//        loadData(date);
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        LogHelper.e("onRestart()");
//        loadData(date);
//    }
//
//    private void loadData(String date) {
//        HttpRequestHelper.getMyTeam(new ConfigUtil().getUserid(), date, new HttpCallBack() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showProgressDialog();
//            }
//
//            @Override
//            public void onSuccess(String body) {
//                if (TextUtils.isEmpty(body)) {
//                    return;
//                }
//                mMyVehicleBean = new Gson().fromJson(body, MyVehicleBean.class);
//                if (mMyVehicleBean != null) {
//                    convertData();
//                }
//            }
//
//            @Override
//            public void onError(String body) {
//                super.onError(body);
//                Tools.Toast(getString(R.string.network_anomaly));
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideProgressDialog();
//            }
//        });
//    }
//
//    private void convertData() {
//        countMile = 0d;
//        if (mMyVehicleBean.getList() != null && mMyVehicleBean.getList().size() > 0) {
//            for (VehicleLocationBean bean : mMyVehicleBean.getList()) {
//                if (bean.getDayMileage() != null && bean.getDayMileage() > 0) {
//                    try {
//                        countMile += bean.getDayMileage();
//                    } catch (Exception e) {
//                        LogHelper.e(e);
//                    }
//                }
//            }
//            initView();
//        }
//    }
//
//    private void initView() {
//        if (countMile > 0) {
//            tv_count_mileage.setText(Compat.fromHtml("日总里程： <Big>" + new java.text.DecimalFormat("#.00").format(countMile) + "</Big> 公里"));
//        } else {
//            tv_count_mileage.setText(Compat.fromHtml("日总里程： <Big>0</Big> 公里"));
//        }
//
//        tv_count_date.setText(TimeHelper.long2time("yyyy年MM月dd日", calendar.getTimeInMillis()));
//        adapter.setData(mMyVehicleBean.getList());
//    }
//
//    //时间选择器
//    public void showDatePickerDialog() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
//            calendar.set(year, month, dayOfMonth);
//
//            date = DateUtil.format(calendar.getTimeInMillis());
//
//            tv_count_date.setText(DateUtil.format("yyyy年MM月dd日", calendar.getTimeInMillis()));
//            adapter.setDate(date);
//            loadData(date);
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//        datePickerDialog.show();
//    }
//
//}
