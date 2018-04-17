//package com.sc.clgg.activity.vehiclemanager.maintenance;
//
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.sc.clgg.R;
//import com.sc.clgg.base.BaseActivity;
//
//import toolbox.helper.ActivityHelper;
//
///**
// * 维修保养主界面
// *
// * @author lvke
// */
//public class MaintenanceHomeActivity extends BaseActivity {
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_maintenance_home_constaint;
//    }
//
//    @Override
//    public void initTitle() {
//        setTitle(getString(R.string.maintenance));
//    }
//
//    @Override
//    protected void init() {
//        initOnClick();
//    }
//
//    public void initOnClick() {
//        findViewById(R.id.weiXiuLayout).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                /**维修站*/
//                ActivityHelper.startAcMove(MaintenanceHomeActivity.this, MaintenanceActivity.class);
//            }
//        });
//        findViewById(R.id.jiayouLayout).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                /**加油加气站*/
//                ActivityHelper.startAcMove(MaintenanceHomeActivity.this, FuelGasActivity.class);
//            }
//        });
//        findViewById(R.id.yuanQuLayout).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                /**物流园区*/
//                ActivityHelper.startAcMove(MaintenanceHomeActivity.this, LogisticsParkActivity.class);
//            }
//        });
//    }
//
//}
