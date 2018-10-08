package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.bean.PathRecord;
import com.sc.clgg.http.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.DecimalFormatHelper;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 * 轨迹回放
 */
public class PathRecordActivity extends BaseImmersionActivity implements OnMarkerClickListener, AMap.InfoWindowAdapter {
    @BindView(R.id.car_num) TextView car_num;
    @BindView(R.id.current_mileage) TextView current_mileage;
    @BindView(R.id.total_mileage) TextView total_mileage;
    @BindView(R.id.time_second) TextView time_second;
    @BindView(R.id.time_date) TextView time_date;
    @BindView(R.id.seekbar) SeekBar mSeekBar;
    @BindView(R.id.start) ImageView start;
    @BindView(R.id.day_before) TextView day_before;
    @BindView(R.id.day_after) TextView day_after;
    @BindView(R.id.playback_time) TextView playback_time;
    @BindView(R.id.day_before_seven) TextView day_before_seven;
    @BindView(R.id.day_before_three) TextView day_before_three;
    @BindView(R.id.day_custom) TextView day_custom;
    // 地图对象
    private AMap aMap;

    private MapView mapView;
    private String carno, vin, startDate, endDate;
    private PathRecord mPathRecord;
    //回放轨迹总数据
    private List<LatLng> allPoints = new ArrayList<>();
    //当前运行的轨迹数据
    private List<LatLng> currentPoints = new ArrayList<>();
    //轨迹回放的记录下标
    private int mPauseIndex;
    private SmoothMoveMarker mSmoothMoveMarker;

    //去重处理后的轨迹数据集合
    private List<PathRecord.Path> newList = new ArrayList<>();
    private int totalDuration = 10;
    private Handler mHandler;
    private TextView tv_speed;
    private double firstPoint, distance;
    private Call mCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_record);
        unbinder = ButterKnife.bind(this);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initTitle("轨迹回放");
        carno = getIntent().getStringExtra("carno");
        vin = getIntent().getStringExtra("vin");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        car_num.setText(carno);
        initMapView();
        loadData();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        LogHelper.e("轨迹回放完成");
                        start.setImageResource(R.drawable.track_ico_play);
                        start.setVisibility(View.VISIBLE);
                        mSmoothMoveMarker.setVisible(true);

                        removeMessages(2);
                        current_mileage.setText(distance + "km");
                        LogHelper.e("message = 0    " + current_mileage.getText().toString());
                        break;
                    case 1:
                        hideProgressDialog();
                        addMarkersToMap(newList);

                        double lastPoint = Double.parseDouble(newList.get(newList.size() - 1).getTotalMileage());
                        firstPoint = Double.parseDouble(newList.get(0).getTotalMileage());
                        distance = lastPoint - firstPoint;

                        total_mileage.setText("/" + DecimalFormatHelper.formatTwo(distance) + "km");

                        PathRecord.Path firstPathBean = newList.get(0);
                        if (!TextUtils.isEmpty(firstPathBean.getGpsTime()) && firstPathBean.getGpsTime().length() == 14) {

                            time_second.setText(
                                    new StringBuilder().append(firstPathBean.getGpsTime().substring(8, 10))
                                            .append(":")
                                            .append(firstPathBean.getGpsTime().substring(10, 12))
                                            .append(":")
                                            .append(firstPathBean.getGpsTime().substring(12, 14))
                                            .toString()
                            );
                            time_date.setText(
                                    new StringBuilder()
                                            .append(firstPathBean.getGpsTime().substring(4, 6))
                                            .append("-")
                                            .append(firstPathBean.getGpsTime().substring(6, 8))
                                            .toString()
                            );
                        }
                        tv_speed.setText(firstPathBean.getSpeed());
                        break;
                    case 2:
                        PathRecord.Path currentPathBean = newList.get(mPauseIndex);
                        double currentPoint = Double.parseDouble(currentPathBean.getTotalMileage());

                        current_mileage.setText(DecimalFormatHelper.formatTwo(currentPoint - firstPoint) + "km");

                        LogHelper.e("message = 2    " + current_mileage.getText().toString());

                        if (!TextUtils.isEmpty(currentPathBean.getGpsTime()) && currentPathBean.getGpsTime().length() == 14) {

                            time_second.setText(
                                    new StringBuilder().append(currentPathBean.getGpsTime().substring(8, 10))
                                            .append(":")
                                            .append(currentPathBean.getGpsTime().substring(10, 12))
                                            .append(":")
                                            .append(currentPathBean.getGpsTime().substring(12, 14))
                                            .toString()
                            );
                            time_date.setText(
                                    new StringBuilder()
                                            .append(currentPathBean.getGpsTime().substring(4, 6))
                                            .append("-")
                                            .append(currentPathBean.getGpsTime().substring(6, 8))
                                            .toString()
                            );
                        }
                        tv_speed.setText(currentPathBean.getSpeed());
                        break;
                    default:
                        break;
                }
            }

        };

        initListener();
    }

    private void updateTimeSpeedMile(){

    }

    private void loadData() {
        showProgressDialog();
        mCall = new RetrofitHelper().pathRecord(vin, startDate, endDate);
        mCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                mPathRecord = (PathRecord) response.body();

                if (!mPathRecord.getSuccess()) {
                    Toast.makeText(PathRecordActivity.this, mPathRecord.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                } else {
                    if (mPathRecord.getData() == null || mPathRecord.getData().size() == 0) {
                        Toast.makeText(PathRecordActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                if (mPathRecord != null && mPathRecord.getData() != null && mPathRecord.getData().size() > 0) {
                    LogHelper.e("原始点数 = " + mPathRecord.getData().size());

                    newList = mPathRecord.getData();
                    Collections.reverse(newList);
                                    /*for (PathRecord.Path path : mPathRecord.getData()) {
                                        if (!newList.contains(path)) {
                                            newList.add(path);
                                        }
                                    }*/
                    if (PathRecordActivity.this != null) {
                        LogHelper.e("sendEmptyMessage 1 ");
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgressDialog();
                if (!t.getMessage().equals("Socket closed")) {
                    Toast.makeText(PathRecordActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        /*HttpRequestHelper.pathRecord(vin, startDate, endDate,

                new HttpCallBack() {
                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(String body) {
                        mPathRecord = new Gson().fromJson(body, PathRecord.class);

                        if (!mPathRecord.getSuccess()) {
                            Toast.makeText(PathRecordActivity.this, mPathRecord.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        } else {
                            if (mPathRecord.getData() == null || mPathRecord.getData().size() == 0) {
                                Toast.makeText(PathRecordActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (mPathRecord != null && mPathRecord.getData() != null && mPathRecord.getData().size() > 0) {
                                    LogHelper.e("原始点数 = " + mPathRecord.getData().size());

                                    newList = mPathRecord.getData();
                                    Collections.reverse(newList);

                                    *//*for (PathRecord.Path path : mPathRecord.getData()) {
                                        if (!newList.contains(path)) {
                                            newList.add(path);
                                        }
                                    }*//*
                                    // LogHelper.e("contains去重后点数 = " + newList.size());
                                    if (PathRecordActivity.this != null) {
                                        LogHelper.e("sendEmptyMessage 1 ");
                                        mHandler.sendEmptyMessage(1);
                                    }
                                }
                            }
                        }).start();


                    }

                    @Override
                    public void onError(String body) {
                        super.onError(body);
                        hideProgressDialog();
                        Toast.makeText(PathRecordActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null) {
            mCall.cancel();
        }
        LogHelper.e("isExecuted =  " + mCall.isExecuted());
        LogHelper.e("isCanceled =  " + mCall.isCanceled());
        mHandler.removeCallbacksAndMessages(null);
        mapView.onDestroy();
        if (mSmoothMoveMarker != null) {
            mSmoothMoveMarker.stopMove();
            mSmoothMoveMarker.destroy();
        }
        LogHelper.e("onDestroy");
    }

    private void setCurrentLocation(Double latitude, Double longitude, int zoom) {
        if (latitude == null || longitude == null) {
            return;
        }
        // 设置缩放比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        // 设置中心点
        LatLng position = new LatLng(latitude, longitude);

        aMap.moveCamera(CameraUpdateFactory.changeLatLng(position));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initMapView() {
        if (aMap == null) {
            aMap = mapView.getMap();
//            aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
//            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
//            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        }
    }

    private void addMarkersToMap(List<PathRecord.Path> gps) {
        initSmoothMoveMarker();
        int s = gps.size();
        LogHelper.e("addMarkersToMap点数 = " + s);
        if (aMap != null) {
            aMap.clear();
        }

        Double latitude, longitude;  //描绘出第一个GPS位置作为起始位置
        // 当前经纬度
        LatLng currentLatLng;
        if (gps != null && gps.size() > 0) {
            setCurrentLocation(Double.parseDouble(gps.get(s / 2).getLat()), Double.parseDouble(gps.get(s / 2).getLng()), 6); //设置第一个GPS信息为当前位置
            latitude = Double.parseDouble(gps.get(0).getLat());
            longitude = Double.parseDouble(gps.get(0).getLng());
        } else {
            return;
        }
        // 默认位置是北京
        if (latitude == null || longitude == null) {
            currentLatLng = new LatLng(39.90403, 116.407525);
        } else {
            currentLatLng = new LatLng(latitude, longitude);
        }
        mSmoothMoveMarker.setVisible(true);
        mSmoothMoveMarker.setPosition(currentLatLng);
        mSmoothMoveMarker.getMarker().setInfoWindowEnable(true);
        getInfoWindow(mSmoothMoveMarker.getMarker());
        mSmoothMoveMarker.getMarker().showInfoWindow();

        List<LatLng> points = new ArrayList<>();

        for (int i = 0, size = gps.size(); i < size; i++) {
            PathRecord.Path traGPS = gps.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(traGPS.getLat()), Double.parseDouble(traGPS.getLng()));
            points.add(latLng);
            allPoints.add(latLng);

            /*if (i == gps.size() - 1) {
                MarkerOptions markerOptionDes = new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_b));
                aMap.addMarker(markerOptionDes);
            }*/
        }

        if (points != null && points.size() > 0) {
            aMap.addPolyline(new PolylineOptions().addAll(points).width(4).color(Color.parseColor("#ff00ff")));
        }

        currentPoints.addAll(allPoints);
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (currentPoints.size() > 0) {
                    setSmoothData(currentPoints);
                    startRun();
                    sendStartSignal(totalDuration);
                    start.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void initSmoothMoveMarker() {
        if (mSmoothMoveMarker == null) {
            mSmoothMoveMarker = new SmoothMoveMarker(aMap);
        }
        mSmoothMoveMarker.setVisible(true);
        mSmoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.map_ico_speed));
        mSmoothMoveMarker.setTotalDuration(totalDuration);

        mSmoothMoveMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(double d) {
                /**
                 * 最后一次取到的值即为暂停时的数据，getIndex() 方法取到的是当前 list 的下标, v 为剩余距离
                 */
                mPauseIndex = mSmoothMoveMarker.getIndex();

                mHandler.sendEmptyMessage(2);
            }
        });
    }

    private void setSmoothData(List<LatLng> data) {
        LogHelper.e("设置回放轨迹的数量 = " + data.size());

        LogHelper.e("startRun()");

        LatLng drivePoint = data.get(0);

        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(data, drivePoint);

        data.set(pair.first, drivePoint);

        List<LatLng> subList = data.subList(pair.first, data.size());

        LogHelper.e("SmoothMoveMarker 数据 = " + new Gson().toJson(subList));
        mSmoothMoveMarker.setPoints(subList);
    }

    private void startRun() {
        mSmoothMoveMarker.startSmoothMove();
    }

    private void sendStartSignal(int duration) {
        LogHelper.e(duration + "秒后发送完成信号");
        mHandler.sendEmptyMessageDelayed(0, duration * 1000);
    }

    private void removeStartSignal() {
        LogHelper.e("删除信号");
        mHandler.removeMessages(0);
    }

    private void pauseRun() {
        LogHelper.e("pauseRun()");
        mSmoothMoveMarker.stopMove();
    }

    private void continueRun() {
        LogHelper.e("continueRun()");
        LatLng drivePoint = currentPoints.get(mPauseIndex + 1);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(currentPoints, drivePoint);
        currentPoints.set(pair.first, drivePoint);
        currentPoints = currentPoints.subList(pair.first, currentPoints.size());
        mSmoothMoveMarker.setPoints(currentPoints);
        mSmoothMoveMarker.startSmoothMove();
    }

    private void initListener() {
        /*mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            private float p;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                p = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentProgress = (int) p;
                LogHelper.e("progress = " + p);
                int index = (int) (p / (float) 100 * (float) newList.size());
                LogHelper.e("Seekbar的位置对应 轨迹总数的下标" + index);
                int time = totalDuration - (int) (p / (float) 100 * totalDuration);
                LogHelper.e("剩余播放时间 = " + time + "秒");

                LogHelper.e("Seekbar选中的比例" + index);
                String lat = newList.get(index).getLat();
                String lng = newList.get(index).getLng();
//                LogHelper.e(lat + "," + lng);
//                marker.setPosition(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
//                marker.setVisible(true);
//                marker.setRotateAngle(Float.parseFloat(newList.get(index).getDir()));

                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));

                mSmoothMoveMarker.setRotate(Float.parseFloat(newList.get(index).getDir()));
                mSmoothMoveMarker.setPosition(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                mSmoothMoveMarker.setVisible(true);
                currentDuration = time;
                mSmoothMoveMarker.setTotalDuration(time);
                currentPoints = allPoints.subList(index, allPoints.size());
//                currentList = newList.subList(index, newList.size());
                LogHelper.e("SeekBar取点后的轨迹数量 = " + currentPoints.size());

                removeStartSignal();
            }
        });*/
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = View.inflate(this, R.layout.view_path_record, null);
        tv_speed = view.findViewById(R.id.tv_speed);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
