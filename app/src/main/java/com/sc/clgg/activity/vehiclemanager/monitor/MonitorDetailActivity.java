package com.sc.clgg.activity.vehiclemanager.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.myvehicle.VehicleActivity;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.MyVehicleBean;
import com.sc.clgg.bean.VehicleLocation;
import com.sc.clgg.bean.VehicleLocationBean;
import com.sc.clgg.bean.VehicleLocationDetailBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.Tools;
import com.sc.clgg.util.Utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.DateUtil;

public class MonitorDetailActivity extends BaseActivity implements AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.map) MapView map;
    @BindView(R.id.tv_car_no) TextView tv_car_no;
    @BindView(R.id.tv_car_status_1) TextView tv_car_status_1;
    @BindView(R.id.tv_car_status_2) TextView tv_car_status_2;
    @BindView(R.id.tv_car_status_3) TextView tv_car_status_3;
    @BindView(R.id.tv_car_postion) TextView tv_car_postion;
    @BindView(R.id.tv_last_time_format) TextView tv_last_time_format;//最后定位时间 tv
    @BindView(R.id.relative_last_time) RelativeLayout relative_last_time;//最后定位时间 rl
    @BindView(R.id.titlebar_title) TextView tv_title;
    private AMap aMap;
    private VehicleLocationBean mVehicleLocationBean;
    private VehicleLocation mVehicleLocation;

    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private boolean isVisible = true;

    @Override
    protected int layoutRes() {
        return R.layout.activity_monitor_detail;
    }

    @Override
    public void initBefore(Bundle savedInstanceState) {
        map.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        mVehicleLocationBean = getIntent().getParcelableExtra("info");
        mVehicleLocation = getIntent().getParcelableExtra("allTnfo");


        aMap = map.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.setInfoWindowAdapter(this);

        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isVisible) {
                    loadData();
                }
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        isVisible = false;
        super.onDestroy();
        if (map != null) {
            map.onDestroy();
        }
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdownNow();
        }
        mScheduledExecutorService = null;
    }

    private void loadData() {
        HttpRequestHelper.getVehicleLocation(new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                VehicleLocationDetailBean mVehicleLocationDetailBean = new Gson().fromJson(body, VehicleLocationDetailBean.class);
                initData(mVehicleLocationDetailBean.getData());
            }
        }, new ConfigUtil().getUserid(), mVehicleLocationBean.getCarno());
    }

    private void initData(VehicleLocationDetailBean.DataBean bean) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        if (bean == null) {
            return;
        }
        if (!TextUtils.isEmpty(bean.getCarno())) {
            tv_car_no.setText(bean.getCarno());
            findViewById(R.id.tv_car_report).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_arrow_right).setVisibility(View.VISIBLE);
        }
        switch (bean.getStatus()) {
            case 2:
                relative_last_time.setVisibility(View.VISIBLE);
                tv_car_status_1.setVisibility(View.GONE);
                tv_car_status_2.setVisibility(View.VISIBLE);
                tv_car_status_3.setVisibility(View.GONE);
                break;
            case 1:
                relative_last_time.setVisibility(View.GONE);
                tv_car_status_1.setVisibility(View.VISIBLE);
                tv_car_status_2.setVisibility(View.GONE);
                tv_car_status_3.setVisibility(View.GONE);
                break;
            case 0:
                relative_last_time.setVisibility(View.VISIBLE);
                tv_car_status_1.setVisibility(View.GONE);
                tv_car_status_2.setVisibility(View.GONE);
                tv_car_status_3.setVisibility(View.VISIBLE);
                break;
        }

        //逆地理编码
        if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude())) {
            LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        }

        tv_last_time_format.setText(bean.getTime() == null ? "" : bean.getTime());

        tv_title.setText(bean.getCarno() != null ? bean.getCarno() : "");
        addMarkersToMap(bean);
    }

    @OnClick({R.id.tv_select_vehical, R.id.ll, R.id.rl})
    void a(View v) {
        switch (v.getId()) {
            case R.id.tv_select_vehical:
                Intent intent = new Intent(this, ChooseVehicleActivity.class);
                intent.putExtra("allTnfo", mVehicleLocation);
                startActivity(intent);
                break;

            case R.id.ll:
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                final String date = DateUtil.format(calendar.getTimeInMillis());

                HttpRequestHelper.getMyTeam(new ConfigUtil().getUserid(), date, new HttpCallBack() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(String body) {
                        if (TextUtils.isEmpty(body)) {
                            return;
                        }
                        MyVehicleBean mMyVehicleBean = new Gson().fromJson(body, MyVehicleBean.class);
                        for (VehicleLocationBean b : mMyVehicleBean.getList()) {
                            if (b.getCarno().equals(mVehicleLocationBean.getCarno())) {
                                startActivity(new Intent(MonitorDetailActivity.this, VehicleActivity.class).putExtra("bean", b).putExtra("date", date));
                                return;
                            }
                        }
                    }

                    @Override
                    public void onError(String body) {
                        super.onError(body);
                        Tools.Toast(getString(R.string.network_anomaly));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideProgressDialog();
                    }
                });
                break;

            case R.id.rl:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        if (map != null) {
            map.onResume();
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            map.onSaveInstanceState(outState);
        }
    }

    private void addMarkersToMap(VehicleLocationDetailBean.DataBean bean) {
        List<Marker> markers = aMap.getMapScreenMarkers();

        if (markers != null && markers.size() > 0) {
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).remove();
                markers.get(i).destroy();
            }
        }

        aMap.clear();
        LatLngBounds.Builder builder = LatLngBounds.builder();

        LatLng latLng = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
        builder.include(latLng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.snippet(bean.getCarno());

        switch (bean.getStatus()) {
            case 2:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.monitor_offline));
                break;
            case 1:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.monitor_driving));
                break;
            case 0:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.monitor_pause));
                break;
            default:
                if (bean.getSpeed() > 0) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.monitor_driving));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.monitor_pause));
                }
                break;
        }
        markerOptions.anchor(0.5f, 0.5f);

        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(bean);

        int width = Utils.getScreenWidth(this);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), width * 4 / 5, width * 4 / 5, 0);
        aMap.moveCamera(cu);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//设置缩放等级

        getInfoWindow(marker);
        marker.showInfoWindow();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        VehicleLocationDetailBean.DataBean info = (VehicleLocationDetailBean.DataBean) marker.getObject();
        View view = View.inflate(this, R.layout.view_vehicle_mark, null);
        TextView textCarNo = (TextView) view.findViewById(R.id.text_car_no);
        TextView text_car_speed = (TextView) view.findViewById(R.id.text_car_speed);
        text_car_speed.setVisibility(View.VISIBLE);
        LinearLayout root = (LinearLayout) view.findViewById(R.id.linear_root);
        switch (info.getStatus()) {
            case 2:
                root.setBackgroundResource(R.drawable.monitor_marker_1);
                text_car_speed.setVisibility(View.GONE);
                break;
            case 1:
                root.setBackgroundResource(R.drawable.monitor_marker_3);
                text_car_speed.setVisibility(View.VISIBLE);

                text_car_speed.setText(new DecimalFormat("#.00").format(info.getSpeed()) + "公里/小时");
                break;
            case 0:
                root.setBackgroundResource(R.drawable.monitor_marker_2);
                text_car_speed.setVisibility(View.GONE);
                break;
            default:
                if (info.getSpeed() > 0) {
                    root.setBackgroundResource(R.drawable.monitor_marker_3);
                    text_car_speed.setVisibility(View.VISIBLE);
                } else {
                    root.setBackgroundResource(R.drawable.monitor_marker_2);
                    text_car_speed.setVisibility(View.GONE);
                }
                break;
        }
        textCarNo.setText(info.getCarno());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        VehicleLocationBean info = (VehicleLocationBean) marker.getObject();
        View view = View.inflate(this, R.layout.view_vehicle_mark, null);
        TextView textCarNo = (TextView) view.findViewById(R.id.text_car_no);
        TextView text_car_speed = (TextView) view.findViewById(R.id.text_car_speed);
        text_car_speed.setVisibility(View.VISIBLE);
        LinearLayout root = (LinearLayout) view.findViewById(R.id.linear_root);
        switch (info.getStatus()) {
            case "2":
                root.setBackgroundResource(R.drawable.monitor_marker_1);
                text_car_speed.setVisibility(View.GONE);
                break;
            case "1":
                root.setBackgroundResource(R.drawable.monitor_marker_3);
                text_car_speed.setVisibility(View.VISIBLE);
                text_car_speed.setText(info.getSpeed() == null ? "" : info.getSpeed() + "公里/小时");
                break;
            case "0":
                root.setBackgroundResource(R.drawable.monitor_marker_2);
                text_car_speed.setVisibility(View.GONE);
                break;
            default:
                if (Double.parseDouble(info.getSpeed()) > 0) {
                    root.setBackgroundResource(R.drawable.monitor_marker_3);
                    text_car_speed.setVisibility(View.VISIBLE);
                } else {
                    root.setBackgroundResource(R.drawable.monitor_marker_2);
                    text_car_speed.setVisibility(View.GONE);
                }
                break;
        }
        textCarNo.setText(info.getCarno());
        text_car_speed.setText(info.getSpeed() == null ? "" : info.getSpeed() + "公里/小时");
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 0x00898:
                mVehicleLocationBean = data.getParcelableExtra("info");
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                tv_car_postion.setText(String.format(result.getRegeocodeAddress().getFormatAddress(), "附近"));
            } else {
                Tools.Toast("逆地理编码失败");
            }
        } else {
            Tools.Toast("逆地理编码失败");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }
}