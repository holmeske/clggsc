//package com.sc.clgg.activity.vehiclemanager.myvehicle;
//
//import android.animation.PropertyValuesHolder;
//import android.content.Intent;
//import android.graphics.Color;
//import android.text.Html;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.db.chart.animation.Animation;
//import com.db.chart.model.BarSet;
//import com.db.chart.tooltip.Tooltip;
//import com.db.chart.view.BarChartView;
//import com.google.gson.Gson;
//import com.lzy.okgo.OkGo;
//import com.sc.clgg.R;
//import com.sc.clgg.base.BaseActivity;
//import com.sc.clgg.bean.NearMileBean;
//import com.sc.clgg.bean.VehicleDetailBean;
//import com.sc.clgg.bean.VehicleLocationBean;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.util.Tools;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
//import toolbox.helper.LogHelper;
//
//
///**
// * 我的车辆列表详情
// *
// * @author lvke
// */
//public class VehicleActivity extends BaseActivity implements View.OnClickListener {
//    private BarChartView barchart;
//    private VehicleLocationBean mPassData;
//    private TextView tv_today_mile, tv_day_oil, tv_day_oilkm, tv_title, tv_drivename_tel, mile_rpt, time_rpt, time_tj;
//    private VehicleDetailBean mVehicleDetailBean;
//    private ImageButton ibtn_left;
//
//    private String mDate;
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_vehicle_constaint;
//    }
//
//    @Override
//    protected void init() {
//        mPassData = getIntent().getParcelableExtra("bean");
//        LogHelper.e("mPassData = " + new Gson().toJson(mPassData));
//        mDate = getIntent().getStringExtra("mDate");
//        loadData();
//        initView();
//    }
//
//    private void loadData() {
//        HttpRequestHelper.myVehicle(mPassData.getCarno(), mDate, new HttpCallBack() {
//            @Override
//            public void onSuccess(String body) {
//                mVehicleDetailBean = new Gson().fromJson(body, VehicleDetailBean.class);
//
//                if (mVehicleDetailBean != null) {
//                    initBarChart(mVehicleDetailBean.getListMileage());
//
//                    String driverName = "";
//                    String driverMobile = "";
//                    if (!TextUtils.isEmpty(mVehicleDetailBean.getDriverName())) {
//                        driverName = mVehicleDetailBean.getDriverName();
//                    }
//                    if (!TextUtils.isEmpty(mVehicleDetailBean.getMobile())) {
//                        driverMobile = mVehicleDetailBean.getMobile();
//                    }
//                    tv_drivename_tel.setText(Html.fromHtml("<font color='#669933'>" + driverName + "</font>  " + driverMobile));
//                }
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                showProgressDialog();
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
//    @Override
//    protected void onStop() {
//        super.onStop();
//        OkGo.getInstance().cancelTag("myVehicle");
//    }
//
//    private void initView() {
//        barchart = (BarChartView) findViewById(R.id.barchart);
//        tv_today_mile = (TextView) findViewById(R.id.tv_today_mile);
//        tv_day_oil = (TextView) findViewById(R.id.tv_day_oil);
//        tv_day_oilkm = (TextView) findViewById(R.id.tv_day_oilkm);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        ibtn_left = (ImageButton) findViewById(R.id.ibtn_left);
//        tv_drivename_tel = (TextView) findViewById(R.id.tv_drivename_tel);
//        mile_rpt = (TextView) findViewById(R.id.mile_rpt);
//        time_rpt = (TextView) findViewById(R.id.time_rpt);
//        time_tj = (TextView) findViewById(R.id.time_tj);
//
//        ibtn_left.setOnClickListener(this);
//        mile_rpt.setOnClickListener(this);
//        time_rpt.setOnClickListener(this);
//        time_tj.setOnClickListener(this);
//
//        if (mPassData != null) {
//            if (mPassData.getDayMileage() != null && mPassData.getDayMileage() > 0) {
//                tv_today_mile.setText(Html.fromHtml("当日行驶 <font color='#E40011'><big>" + mPassData.getDayMileage() + "</big></font> 公里"));
//            } else {
//                tv_today_mile.setText(Html.fromHtml("当日行驶 <font color='#E40011'><big>0</big></font> 公里"));
//            }
//            if (mPassData.getOilcost() != null && mPassData.getOilcost() > 0) {
//                tv_day_oil.setText(String.valueOf(mPassData.getOilcost()));
//            } else {
//                tv_day_oil.setText("0");
//            }
//            if (mPassData.getOilcostphkm() > 0) {
//                tv_day_oilkm.setText(String.valueOf(mPassData.getOilcostphkm()));
//            } else {
//                tv_day_oilkm.setText("0");
//            }
//            if (!TextUtils.isEmpty(mPassData.getCarno())) {
//                tv_title.setText(mPassData.getCarno());
//            } else {
//                tv_title.setText("");
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (TextUtils.isEmpty(mPassData.getCarno())) {
//            Tools.Toast("车牌号不能为空");
//            return;
//        }
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.ibtn_left:
//                onBackPressed();
//                break;
//            case R.id.time_rpt:
//                intent = new Intent(this, OilWearActivity.class);
//                intent.putExtra("carno", mPassData.getCarno());
//                startActivity(intent);
//                break;
//            case R.id.mile_rpt:
//                intent = new Intent(this, MileageActivity.class);
//                intent.putExtra("carno", mPassData.getCarno());
//                startActivity(intent);
//                break;
//            case R.id.time_tj:
//                intent = new Intent(this, HistoricalRouteActivity.class);
//                intent.putExtra("truckNo", mPassData.getCarno());
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void initBarChart(List<NearMileBean> list) {
//        if (list == null || list.size() == 0) {
//            return;
//        }
//        barchart.reset();
//        String[] mLabels = new String[list.size()];
//        float[] mValues = new float[list.size()];
//
//        /*for (NearMileBean nearMile : mVehicleDetailBean.getListMileage()) {
//
//        }*/
//
//        int j = 0;
//        for (int i = list.size() - 1; i >= 0; i--) {
//            try {
//                mLabels[j] = Integer.parseInt(list.get(i).getDate().substring(4, 6)) + "/" + Integer.parseInt(list.get(i).getDate().substring(6, 8));
//                mValues[j] = Float.parseFloat(list.get(i).getMileage());
//                j++;
//            } catch (Exception e) {
//                LogHelper.e(e);
//            }
//        }
//
//        if (mLabels.length <= 0 || mValues.length <= 0) {
//            barchart.setVisibility(View.GONE);
//            return;
//        }
//
//        BarSet data = new BarSet(mLabels, mValues);
//
//        data.setColor(Color.parseColor("#A6CDEC"));
//
//        Tooltip tip = new Tooltip(this, R.layout.barchart_tooltip, R.id.value);
//
//        tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
//                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
//                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)).setDuration(200);
//
//        tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
//                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
//                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)).setDuration(200);
//
//        tip.setVerticalAlignment(Tooltip.Alignment.TOP_TOP);
//        barchart.setTooltips(tip);
//        barchart.addData(data);
//        //垂直坐标的最大值最小值以及刻度
//        barchart.setAxisBorderValues(0, 1500, 300);
//        //X.Y轴的相关设置，是否有轴线（有/没有），刻度在的位置（里/外/无）
//        barchart.setLabelsFormat(new DecimalFormat("0' 公里'"));
//        barchart.show(new Animation().inSequence(.7f, new int[]{1, 0, 2, 3, 4, 5, 6}));
//    }
//
//
//}
