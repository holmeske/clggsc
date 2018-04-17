package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.GPS;
import com.sc.clgg.bean.PathwayBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹地图
 *
 * @author lvke
 */
public class LocusFreeActivity extends BaseActivity implements OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,
        OnClickListener, InfoWindowAdapter {
    // 地图对象
    private AMap aMap;
    // 当前经纬度
    private LatLng currentLatLng;
    // 有跳动效果的marker对象
    private Marker marker2;
    // 高德地图
    private MapView mapView;
    // 维修保养重新定位
    private TextView img_service_baoyang, car_num, pathway;

    private String carno;

    private PathwayBean bean;

    @Override
    public void initTitle() {
        setTitle("轨迹");
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_locus_free;
    }

    @Override
    public void initBefore(Bundle savedInstanceState) {
        super.initBefore(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        initMapView();
        initView();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * @return void
     * @Description描述: 地图添加覆盖物
     */
    private void addMarkersToMap(List<GPS> gps) {

        if (aMap != null) {
            aMap.clear();
        }
        //121.445961； 纬度：31.28836623

        // 获取定位数据
        Double latitude = null;  //描绘出第一个GPS位置作为起始位置
        Double longitude = null;

        if (gps != null && gps.size() > 0) {
            latitude = gps.get(0).getLatitude();
            longitude = gps.get(0).getLongitude();
        } else {
            currentLatLng = new LatLng(39.90403, 116.407525);
            return;
        }
        // 默认位置是北京
        if (latitude == null || longitude == null) {
            currentLatLng = new LatLng(39.90403, 116.407525);
        } else {
            currentLatLng = new LatLng(latitude, longitude);
        }

        MarkerOptions markerOption = new MarkerOptions().position(currentLatLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_g));
        marker2 = aMap.addMarker(markerOption);
        // marker旋转0度
        marker2.setRotateAngle(0);

        setCurrentLocation(latitude, longitude, 8); //设置第一个GPS信息为当前位置

        List<LatLng> points = new ArrayList<>();

        for (int i = 0; i < gps.size(); i++) {
            GPS traGPS = gps.get(i);
            LatLng latLng = new LatLng(traGPS.getLatitude(), traGPS.getLongitude());
            points.add(latLng);

            if (i == gps.size() - 1) {
                MarkerOptions markerOptionDes = new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_b));
                aMap.addMarker(markerOptionDes);
            }
        }

        addPath(new ArrayList<>(), points, false);
    }


    public void addPath(List<Polyline> paths, List<LatLng> points, boolean append) {
        if (points.size() == 0) {
            return;
        }
        if (append && paths.size() != 0) {
            Polyline lastPolyline = paths.get(paths.size() - 1);
            ArrayList<LatLng> pathPoints = new ArrayList<LatLng>();
            pathPoints.addAll(lastPolyline.getPoints());
            pathPoints.addAll(points);
            lastPolyline.setPoints(pathPoints);
        } else {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(points).width(5).color(Color.RED);
            Polyline polyline = aMap.addPolyline(polylineOptions);
            paths.add(polyline);
        }
        points.clear();
    }

    /**
     * @return void
     * @Description描述: 初始地图本数据
     */
    private void initMapView() {

        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        }

        // 设置中心点
//		setCurrentLocation(CLGGApplication.mLocationBean.getLatitude(), CLGGApplication.mLocationBean.getLongitude(), 14);
    }

    /**
     * @return void
     * @Description描述: 初始化基本数据
     */
    private void initView() {
        img_service_baoyang = (TextView) this.findViewById(R.id.img_service_baoyang);
        car_num = (TextView) this.findViewById(R.id.car_num);
        pathway = (TextView) this.findViewById(R.id.pathway);

        img_service_baoyang.setOnClickListener(this);

        carno = getIntent().getStringExtra("carno");
        bean = getIntent().getParcelableExtra("gps");
        car_num.setText(carno);
        pathway.setVisibility(View.VISIBLE);
        if (bean.getGpsList() != null && bean.getGpsList().size() > 0) {
            addMarkersToMap(bean.getGpsList());
        }
    }


    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(currentLatLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * currentLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * currentLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
//				aMap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    /**
     * @param view
     * @Description描述: 按钮点击事件
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // 定位
            case R.id.img_service_baoyang:
                // 设置中心点
                setCurrentLocation(currentLatLng.latitude, currentLatLng.longitude, 8);
                break;
        }
    }

    /**
     * 刷新当前位置
     */
    private void setCurrentLocation(Double latitude, Double longitude, int zoom) {

        if (latitude == null || longitude == null) {
            return;
        }

        // 设置缩放比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        // 设置中心点
        LatLng position = new LatLng(latitude, longitude);
        if (marker2 != null) {
            marker2.setPosition(position);
        }
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(position));
    }


    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (aMap == null) {
            return false;
        }
        // 点击了当前位置
        if (marker.equals(marker2)) {
            jumpPoint(marker);
        } else {
            // -----------------------------------
        }
        return false;
    }

    @Override
    public void onMarkerDrag(Marker arg0) {

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {

    }

    @Override
    public void onMarkerDragStart(Marker arg0) {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {

    }

    @Override
    public void onMapLoaded() {

    }


}
