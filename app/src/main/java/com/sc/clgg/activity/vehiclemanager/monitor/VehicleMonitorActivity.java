package com.sc.clgg.activity.vehiclemanager.monitor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.VehicleLocation;
import com.sc.clgg.bean.VehicleLocationBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.ActivityHelper;
import tool.helper.LogHelper;

/**
 * @author lvke
 *         车辆监控
 */
public class VehicleMonitorActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMap.OnMapLoadedListener {

    @BindView(R.id.map) MapView map;

    private boolean isVisible = true;
    private AMap aMap;
    private VehicleLocation mVehicleLocation;
    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
        if (map != null) {
            map.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVisible = false;
        if (map != null) {
            map.onDestroy();
        }
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService.shutdownNow();
        }
        mScheduledExecutorService = null;
    }

    @OnClick(R.id.tv_select_car)
    void a() {
        if (mVehicleLocation != null) {
            Intent intent = new Intent(this, ChooseVehicleActivity.class);
            intent.putExtra("allTnfo", mVehicleLocation);
            ActivityHelper.startActivityScale(this, intent);
        }
    }

    @Override
    public void initTitle() {
        setTitle(getString(R.string.vehicle_monitoring));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_vehicle_monitor;
    }

    @Override
    public void initBefore(Bundle savedInstanceState) {
        if (map != null) {
            map.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void init() {
        aMap = map.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NAVI);//设置地图模式。
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);//设置“高德地图”Logo的位置。
        aMap.getUiSettings().setZoomControlsEnabled(true);//设置缩放按钮是否可见。
        aMap.getUiSettings().setScrollGesturesEnabled(true);//设置拖拽手势是否可用。
        aMap.getUiSettings().setScaleControlsEnabled(true);//设置比例尺控件是否可见
        aMap.getUiSettings().setTiltGesturesEnabled(false);//设置倾斜手势是否可用。
        aMap.getUiSettings().setRotateGesturesEnabled(false);//设置旋转手势是否可用。
        aMap.setOnMarkerClickListener(this);//设置marker点击事件监听接口。

        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LogHelper.e("isVisible = " + isVisible);
                if (isVisible) {
                    loadData();
                }
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

    private void loadData() {
        HttpRequestHelper.getVehicleLocation(new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                mVehicleLocation = new Gson().fromJson(body, VehicleLocation.class);
                LogHelper.e(mVehicleLocation.toString());
                if (mVehicleLocation != null && mVehicleLocation.getSuccess()) {
                    List<VehicleLocationBean> dataList = new ArrayList<>();
                    dataList.addAll(mVehicleLocation.getLeaveList());
                    dataList.addAll(mVehicleLocation.getNullList());
                    dataList.addAll(mVehicleLocation.getRunList());
                    dataList.addAll(mVehicleLocation.getStopList());
                    dataList.addAll(mVehicleLocation.getWarnList());
                    LogHelper.v("     dataList = " + new Gson().toJson(dataList));
                    if (dataList.size() > 0) {
                        List<VehicleLocationBean> newList = new ArrayList<>();
                        for (VehicleLocationBean bean : dataList) {
                            if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude())) {
                                newList.add(bean);
                            }
                        }
                        addMarkersToMap(newList);
                    }
                }
            }
        }, new ConfigUtil().getUserid());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            map.onSaveInstanceState(outState);
        }
    }

    private void addMarkersToMap(List<VehicleLocationBean> list) {
        aMap.clear();//从地图上删除所有的overlay（marker，circle，polyline 等对象）。
        aMap.reloadMap();//重新加载地图引擎，即调用此接口时会重新加载底图数据，覆盖物不受影响。

        LatLngBounds.Builder builder = LatLngBounds.builder();

        //过滤不包含经纬度的不完整bean
        for (VehicleLocationBean bean : list) {
            LatLng latLng = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
            builder.include(latLng);
            MarkerOptions markerOptions = new MarkerOptions();//它定义了marker 的属性信息。
            markerOptions.position(latLng);//设置Marker覆盖物的位置坐标。

            View view = View.inflate(this, R.layout.view_amap_marker, null);
            TextView textCarNo = (TextView) view.findViewById(R.id.text_car_no);
            LinearLayout root = (LinearLayout) view.findViewById(R.id.linear_root);
            ImageView img_car_status = (ImageView) view.findViewById(R.id.img_car_status);

            switch (bean.getStatus()) {
                case "2":
                    root.setBackgroundResource(R.drawable.monitor_marker_1);

                    img_car_status.setBackgroundResource(R.drawable.monitor_offline);
                    break;
                case "1":
                    root.setBackgroundResource(R.drawable.monitor_marker_3);

                    img_car_status.setBackgroundResource(R.drawable.monitor_driving);
                    break;
                case "0":
                    root.setBackgroundResource(R.drawable.monitor_marker_2);

                    img_car_status.setBackgroundResource(R.drawable.monitor_pause);
                    break;
                default:
                    if (Double.parseDouble(bean.getSpeed()) > 0) {
                        root.setBackgroundResource(R.drawable.monitor_marker_3);

                        img_car_status.setBackgroundResource(R.drawable.monitor_driving);
                    } else {
                        root.setBackgroundResource(R.drawable.monitor_marker_2);

                        img_car_status.setBackgroundResource(R.drawable.monitor_pause);
                    }
                    break;
            }
            textCarNo.setText(bean.getCarno());

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(view)));//设置Marker覆盖物的图标。
            view.setDrawingCacheEnabled(false);
            markerOptions.anchor(0.5f, 0.5f);//设置Marker覆盖物的锚点比例。
            aMap.addMarker(markerOptions).setObject(bean);//设置Marker覆盖物的附加信息对象。用户可以自定义Marker的属性。
        }

        int width = Utils.getScreenWidth(this);
        try {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width * 4 / 5, width * 4 / 5, 0));//按照传入的CameraUpdate参数改变地图状态。
            aMap.moveCamera(CameraUpdateFactory.zoomTo(6));//设置缩放等级
//            aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(41.065873,114.74589)));//"latitude":"39.85862","longitude":"118.051276"
        } catch (Exception e) {
            LogHelper.e(e);
        }
    }

    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    public void onMapLoaded() {//当地图加载完成后回调此方法
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        VehicleLocationBean info = (VehicleLocationBean) marker.getObject();
        Intent intent = new Intent(this, MonitorDetailActivity.class);
        intent.putExtra("info", info);
        intent.putExtra("allTnfo", mVehicleLocation);
        startActivity(intent);
        return false;//true 返回true表示该点击事件已被处理，不再往下传递（如底图点击不会被触发），返回false则继续往下传递。
    }

}
