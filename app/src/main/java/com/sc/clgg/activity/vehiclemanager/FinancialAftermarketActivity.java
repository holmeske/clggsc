//package com.sc.clgg.activity.vehiclemanager;
//
//import android.content.Intent;
//import android.view.View;
//
//import com.sc.clgg.R;
//import com.sc.clgg.activity.basic.WebActivity;
//import com.sc.clgg.base.BaseActivity;
//
//import toolbox.helper.ActivityHelper;
//
///*金融和后市场产品*/
//public class FinancialAftermarketActivity extends BaseActivity implements View.OnClickListener {
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_financial_aftermarket;
//    }
//
//    @Override
//    public void initTitle() {
//        setTitle(getString(R.string.financial_aftermarket));
//    }
//
//    @Override
//    protected void init() {
////        findViewById(R.id.relat_bft).setOnClickListener(this);
////        findViewById(R.id.relat_kczl).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.relat_bft:
//                //保险分期付款
//                ActivityHelper.startActivityScale(this, new Intent(FinancialAftermarketActivity.this, WebActivity.class)
//                        .putExtra("name", "保险分期付款")
//                        .putExtra("url", "http://www.clgg.com/truckApp/bft.html"));
//                break;
//            case R.id.relat_kczl:
//                //卡车租赁
//                ActivityHelper.startActivityScale(this, new Intent(FinancialAftermarketActivity.this, WebActivity.class)
//                        .putExtra("name", "卡车租赁")
//                        .putExtra("url", "http://wxsp.kachego.com:3000/#/productList"));
//                break;
//            case R.id.relat_yp:
//                ActivityHelper.startActivityScale(this, new Intent(FinancialAftermarketActivity.this, WebActivity.class)
//                        .putExtra("name", "油品")
//                        .putExtra("url", "http://www.clgg.com/truckApp/rhy.html"));
//                break;
//            case R.id.relat_luntai:
//                ActivityHelper.startActivityScale(this, new Intent(FinancialAftermarketActivity.this, WebActivity.class)
//                        .putExtra("name", "轮胎")
//                        .putExtra("url", "http://www.clgg.com/truckApp/tyre.html"));
//                break;
//            default:
//                break;
//        }
//
//    }
//}
