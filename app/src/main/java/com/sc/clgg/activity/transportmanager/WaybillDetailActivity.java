//package com.sc.clgg.activity.transportmanager;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.sc.clgg.R;
//import com.sc.clgg.base.BaseAppCompatActivity;
//import com.sc.clgg.bean.ListDetailBean;
//import com.sc.clgg.bean.OrderBean;
//import com.sc.clgg.bean.WayBillBean;
//import com.sc.clgg.bean.WaybillDetailBean;
//import com.sc.clgg.dialog.HintSelectDialog;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.util.Tools;
//
//import java.util.List;
//
//import toolbox.helper.LogHelper;
//
///**
// * Author：lvke
// * CreateDate：2017/10/9
// */
//
//public class WaybillDetailActivity extends BaseAppCompatActivity {
//    private String orderNo, order_status;
//
//    private TextView tv_with_car_time, tv_pickup_goods_time, tv_sign_for_time, tv_waybill_number, tv_waybill_amount, tv_waybill_amount_bottom, tv_operation, tv_order_amount;
//    private LinearLayout ll_root;
//    private HintSelectDialog mHintSelectDialog;
//    private FrameLayout fl_operation;
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_waybill_detail_constaint);
//
//        ll_root = (LinearLayout) findViewById(R.id.root);
//        tv_with_car_time = (TextView) findViewById(R.id.tv_with_car_time);
//        tv_pickup_goods_time = (TextView) findViewById(R.id.tv_pickup_goods_time);
//        tv_sign_for_time = (TextView) findViewById(R.id.tv_sign_for_time);
//
//        tv_waybill_number = (TextView) findViewById(R.id.tv_waybill_number);
//        tv_waybill_amount = (TextView) findViewById(R.id.tv_waybill_amount);
//        tv_waybill_amount_bottom = (TextView) findViewById(R.id.tv_waybill_amount_bottom);
//        tv_operation = (TextView) findViewById(R.id.tv_operation);
//        fl_operation = (FrameLayout) findViewById(R.id.fl_operation);
//
//
//        tv_order_amount = (TextView) findViewById(R.id.tv_order_amount);
//        init();
//    }
//
//    private void init() {
//        orderNo = getIntent().getStringExtra("order_no");
//        order_status = getIntent().getStringExtra("order_status");
//        loadData();
//    }
//
//    private void loadData() {
//        HttpRequestHelper.getWaybillDetail(orderNo, order_status, new HttpCallBack() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showProgressDialog();
//            }
//
//            @Override
//            public void onSuccess(String body) {
//                WaybillDetailBean mWaybillDetail = new Gson().fromJson(body, WaybillDetailBean.class);
//                if (mWaybillDetail != null) {
//                    WayBillBean mWayBill = mWaybillDetail.getWayBill();
//
//                    tv_waybill_amount.setText("￥ " + mWayBill.getOrderAmount());
//                    tv_waybill_amount_bottom.setText("￥ " + mWayBill.getOrderAmount());
//
//                    tv_with_car_time.setText(TextUtils.isEmpty(mWaybillDetail.getTimeWait()) ?
//                            "调度时间: ---------------------" : "调度时间: " + mWaybillDetail.getTimeWait());
//                    tv_pickup_goods_time.setText(TextUtils.isEmpty(mWaybillDetail.getTimeProgress()) ?
//                            "提货时间: ---------------------" : "提货时间: " + mWaybillDetail.getTimeProgress());
//                    tv_sign_for_time.setText(TextUtils.isEmpty(mWaybillDetail.getTimeDeliveried()) ?
//                            "签收时间: ---------------------" : "签收时间: " + mWaybillDetail.getTimeDeliveried());
//
//                    if (mWayBill.getWaybillStatus().equals("10")) {
//                        tv_operation.setText(R.string.pick_up_goods);
//                        fl_operation.setVisibility(View.VISIBLE);
//                        tv_waybill_amount.setVisibility(View.GONE);
//
//                        tv_operation.setOnClickListener(new OperationListener(mWayBill.getWaybillStatus(), mWayBill.getWaybillNo()));
//                    } else if (mWayBill.getWaybillStatus().equals("20")) {
//                        tv_operation.setText(R.string.sign_for);
//                        fl_operation.setVisibility(View.VISIBLE);
//                        tv_waybill_amount.setVisibility(View.GONE);
//                        tv_operation.setOnClickListener(new OperationListener(mWayBill.getWaybillStatus(), mWayBill.getWaybillNo()));
//                    } else {
//                        fl_operation.setVisibility(View.GONE);
//                        tv_waybill_amount.setVisibility(View.VISIBLE);
//                    }
//
//                    tv_waybill_number.setText("运单号: " + mWayBill.getWaybillNo());
//
//                    setOrder(mWaybillDetail.getOrder(), mWayBill);
//                }
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
//    private void setOrder(List<OrderBean> orderList, WayBillBean wayBillBean) {
//        if (orderList == null || orderList.size() == 0) {
//            return;
//        }
//        tv_order_amount.setText("订单列表(" + orderList.size() + ")");
//
//        for (OrderBean order : orderList) {
//            View view = View.inflate(this, R.layout.view_waybill_detail_order, null);
//            LogHelper.e("orderList.indexOf(order) = " + orderList.indexOf(order));
//
//            view.findViewById(R.id.v_line).setVisibility(orderList.indexOf(order) == 0 ? View.GONE : View.VISIBLE);
//
//            TextView tv_order_no = (TextView) view.findViewById(R.id.tv_order_no);
//            tv_order_no.setText(String.valueOf(order.getOrderNo()));
//
//            TextView tv_order_addresser = (TextView) view.findViewById(R.id.tv_order_addresser);
//            tv_order_addresser.setText("发:" + order.getDispatchContact() == null ? "" : order.getDispatchContact() + "\t\t" + wayBillBean.getDispatchProvince()
//                    + "-" + wayBillBean.getDispatchCity() + "-" + order.getDispatchDistrict() + " " + order.getDispatchAddress());
//            TextView tv_order_consignee = (TextView) view.findViewById(R.id.tv_order_consignee);
//            tv_order_consignee.setText("收:" + order.getConsigneeContact() == null ? "" : order.getConsigneeContact() + "\t\t" + wayBillBean.getConsigneeProvince()
//                    + "-" + wayBillBean.getConsigneeCity() + "-" + order.getConsigneeDistrict() + " " + order.getConsigneeAddress());
//
//            ll_root.addView(view);
//
//            List<ListDetailBean> mDetailList = order.getListDetail();
//
//            if (mDetailList == null || mDetailList.size() == 0) {
//                return;
//            }
//
//            LinearLayout ll_cargo = (LinearLayout) view.findViewById(R.id.ll_cargo);
//            ll_cargo.removeAllViews();
//
//            for (ListDetailBean detail : mDetailList) {
//                View v = View.inflate(this, R.layout.view_cargo_detail, null);
//                ((TextView) v.findViewById(R.id.tv_order_name)).setText(String.valueOf(detail.getDetailCargoName()));
//                if (mDetailList.indexOf(detail) == 0) {
//                    v.findViewById(R.id.dashline).setVisibility(View.GONE);
//                } else {
//                    v.findViewById(R.id.dashline).setVisibility(View.VISIBLE);
//                }
//
//                if (detail.getDetailCargoCubic() == 0) {
//                    ((TextView) v.findViewById(R.id.tv_order_size)).setText(detail.getDetailCargoQuantity() + "件/" + detail.getDetailCargoWeight() + "吨");
//                } else {
//                    ((TextView) v.findViewById(R.id.tv_order_size)).setText(detail.getDetailCargoQuantity() + "件/" + detail.getDetailCargoWeight() + "吨/"
//                            + detail.getDetailCargoCubic() + "方");
//                }
//                ((TextView) v.findViewById(R.id.tv_order_type)).setText(getDetailCargoName(detail.getDetailCargoType()));
//                ll_cargo.addView(v);
//            }
//        }
//    }
//
//    private String getDetailCargoName(String type) {
//        String s = "";
//        switch (type) {
//            case "90":
//                s = "电子\n产品";
//                break;
//            case "92":
//                s = "商品\n汽车";
//                break;
//            case "93":
//                s = "冷藏\n货物";
//                break;
//            case "94":
//                s = "大宗\n货物";
//                break;
//            case "95":
//                s = "快速\n消费品";
//                break;
//            case "96":
//                s = "农\n产品";
//                break;
//            case "999":
//                s = "其他";
//                break;
//            default:
//                break;
//        }
//        return s;
//    }
//
//    class OperationListener implements View.OnClickListener {
//        private String waybillStatus, waybillNo;
//
//        public OperationListener(String waybillStatus, String waybillNo) {
//            this.waybillStatus = waybillStatus;
//            this.waybillNo = waybillNo;
//        }
//
//        @Override
//        public void onClick(final View view) {
//            mHintSelectDialog = new HintSelectDialog(WaybillDetailActivity.this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (waybillStatus.equals("10")) {
//                        HttpRequestHelper.pickUpGoods(waybillNo, new HttpCallBack() {
//                            @Override
//                            public void onStart() {
//                                super.onStart();
//                                showProgressDialog();
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                                hideProgressDialog();
//                            }
//
//                            @Override
//                            public void onSuccess(String body) {
//                                mHintSelectDialog.dismiss();
//                                Tools.Toast("提货成功");
//                                view.setVisibility(View.GONE);
//                            }
//                        });
//                    } else if (waybillStatus.equals("20")) {
//                        HttpRequestHelper.signFor(waybillNo, new HttpCallBack() {
//                            @Override
//                            public void onStart() {
//                                super.onStart();
//                                showProgressDialog();
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                                hideProgressDialog();
//                            }
//
//                            @Override
//                            public void onSuccess(String body) {
//                                mHintSelectDialog.dismiss();
//                                Tools.Toast("签收成功");
//                                view.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                }
//            });
//            if (waybillStatus.equals("10")) {
//                mHintSelectDialog.show();
//                mHintSelectDialog.setHint("是否提货？");
//            } else if (waybillStatus.equals("20")) {
//                mHintSelectDialog.show();
//                mHintSelectDialog.setHint("是否签收？");
//            }
//
//        }
//    }
//}
