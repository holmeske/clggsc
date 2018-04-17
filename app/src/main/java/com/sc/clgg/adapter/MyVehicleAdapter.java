package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.contact.CallbackListener;
import com.sc.clgg.activity.vehiclemanager.myvehicle.VehicleActivity;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.VehicleLocationBean;
import com.sc.clgg.dialog.AlertDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.util.List;
import java.util.Map;

import tool.helper.LogHelper;

/**
 * @author lvke
 */
public class MyVehicleAdapter extends BaseAdNet<VehicleLocationBean> {

    private GeocodeSearch mGeocodeSearch;
    private RegeocodeQuery mRegeocodeQuery;
    private String mDate;
    private CallbackListener mCallbackListener;

    public MyVehicleAdapter(Activity activity, String mDate) {
        super(activity);
        mGeocodeSearch = new GeocodeSearch(activity);

        this.mDate = mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_my_vehicle, null);

        TextView tvFleetPlace = (TextView) convertView.findViewById(R.id.tv_fleet_place);
        TextView tvCarNo = (TextView) convertView.findViewById(R.id.tv_car_no);
        TextView tvCarPostion = (TextView) convertView.findViewById(R.id.tv_car_postion);
        TextView tvCarMile = (TextView) convertView.findViewById(R.id.tv_car_mile);
        TextView tvCarStatus1 = (TextView) convertView.findViewById(R.id.tv_car_status_1);
        TextView tvCarStatus2 = (TextView) convertView.findViewById(R.id.tv_car_status_2);
        TextView tvCarStatus3 = (TextView) convertView.findViewById(R.id.tv_car_status_3);
        ImageView imgCarStatus2 = (ImageView) convertView.findViewById(R.id.img_car_status_2);

        if (mDatas.get(position) != null) {
            final VehicleLocationBean bean = mDatas.get(position);

            switch (position) {
                case 0:
                    tvFleetPlace.setBackgroundResource(R.drawable.fleet_item_1);
                    break;
                case 1:
                    tvFleetPlace.setBackgroundResource(R.drawable.fleet_item_2);
                    break;
                case 2:
                    tvFleetPlace.setBackgroundResource(R.drawable.fleet_item_3);
                    break;
                default:
                    tvFleetPlace.setBackgroundResource(R.drawable.fleet_item_4);
                    break;
            }

            tvFleetPlace.setText(position + 1 + "");

            if (!TextUtils.isEmpty(bean.getCarno())) {
                tvCarNo.setText(bean.getCarno());
            }
            if (bean.getDayMileage() != null && bean.getDayMileage() > 0) {
                tvCarMile.setText(Html.fromHtml(bean.getDayMileage() + "<font color='#cccccc'><small> 公里</small></font>"));
            } else {
                tvCarMile.setText(Html.fromHtml("0" + "<font color='#cccccc'><small> 公里</small></font>"));
            }
            /** 逆地理编码 */
            if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude())) {
                LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
                mRegeocodeQuery = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                mGeocodeSearch.getFromLocationAsyn(mRegeocodeQuery);
                mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearchListener(tvCarPostion));
            } else {
                tvCarPostion.setText("暂无位置信息");
            }

            if (!TextUtils.isEmpty(bean.getStatus())) {
                if (bean.getStatus().equals("2")) {
                    tvCarStatus1.setVisibility(View.GONE);
                    tvCarStatus2.setVisibility(View.VISIBLE);
                    imgCarStatus2.setVisibility(View.VISIBLE);
                    tvCarStatus3.setVisibility(View.GONE);
                } else if (bean.getStatus().equals("1")) {
                    tvCarStatus1.setVisibility(View.VISIBLE);
                    tvCarStatus2.setVisibility(View.GONE);
                    imgCarStatus2.setVisibility(View.GONE);
                    tvCarStatus3.setVisibility(View.GONE);
                } else if (bean.getStatus().equals("0")) {
                    tvCarStatus1.setVisibility(View.GONE);
                    tvCarStatus2.setVisibility(View.GONE);
                    imgCarStatus2.setVisibility(View.GONE);
                    tvCarStatus3.setVisibility(View.VISIBLE);
                } else {
                    if (Double.parseDouble(bean.getSpeed()) > 0) {
                        tvCarStatus1.setVisibility(View.VISIBLE);
                        tvCarStatus2.setVisibility(View.GONE);
                        imgCarStatus2.setVisibility(View.GONE);
                        tvCarStatus3.setVisibility(View.GONE);
                    } else {
                        tvCarStatus1.setVisibility(View.GONE);
                        tvCarStatus2.setVisibility(View.GONE);
                        imgCarStatus2.setVisibility(View.GONE);
                        tvCarStatus3.setVisibility(View.VISIBLE);
                    }
                }
            }
            if ("1".equals(bean.getDelFlag())) {
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialogHelper.instance.show(mActivity, "确定删除" + bean.getCarno() + "?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpRequestHelper.deleteVehicle(bean.getVincode(), new HttpCallBack() {
                                    @Override
                                    public void onSuccess(String body) {
                                        Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
                                        if ((boolean) map.get("success")) {
                                            Tools.Toast("删除车辆成功");
                                            if (mCallbackListener != null) {
                                                mCallbackListener.callback();
                                            }
                                        } else {
                                            Tools.Toast("删除车辆失败");
                                        }
                                    }
                                });
                            }
                        });
                        return true;
                    }
                });
            }
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(mActivity, VehicleActivity.class);
                intent.putExtra("bean", bean);
                intent.putExtra("mDate", mDate);
                mActivity.startActivity(intent);
            });
        }

        return convertView;
    }

    public void setData(List<VehicleLocationBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        mCallbackListener = callbackListener;
    }

    private class GeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
        private TextView mTextView;

        public GeocodeSearchListener(TextView textView) {
            mTextView = textView;
        }

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            try {
                LogHelper.v("i = " + i + "\n" + regeocodeResult.getRegeocodeAddress().getFormatAddress());
                mTextView.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress() + "附近");

                LogHelper.v("RegeocodeResult = " + regeocodeResult.getRegeocodeAddress().getFormatAddress());
            } catch (Exception e) {
                LogHelper.e(e);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            LogHelper.v("GeocodeResult = " + new Gson().toJson(geocodeResult));
        }
    }
}
