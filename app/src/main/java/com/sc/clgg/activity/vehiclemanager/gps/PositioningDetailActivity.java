package com.sc.clgg.activity.vehiclemanager.gps;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.sc.clgg.R;
import com.sc.clgg.activity.ShareActivity;
import com.sc.clgg.activity.vehiclemanager.myvehicle.PathRecordActivity;
import com.sc.clgg.adapter.SelectVehicleAdapter;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.bean.Location;
import com.sc.clgg.bean.LocationDetail;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.DateHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.util.TimeHelper;
import com.sc.clgg.util.Tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 */
public class PositioningDetailActivity extends BaseImmersionActivity implements AMap.InfoWindowAdapter, GeocodeSearch.OnGeocodeSearchListener {

    private MapView map;
    private AMap aMap;

    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private boolean isVisible = true;
    private String carno, vin;
    private retrofit2.Call<LocationDetail> call;
    private ConstraintLayout select_root;
    private RecyclerView mRecyclerView;
    private EditText mSearchView;
    private SelectVehicleAdapter mSelectVehicleAdapter;
    private List<Location.Data> searchList = new ArrayList<>();
    private LocationDetail.Data mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning_detail);
        map=findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        init();
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
    protected void onDestroy() {
        isVisible = false;
        super.onDestroy();
        call.cancel();
        if (map != null) {
            map.onDestroy();
        }
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdownNow();
        }
        mScheduledExecutorService = null;
    }

    @Override
    public void onBackPressed() {
        if (select_root != null && select_root.getVisibility() == View.VISIBLE) {
            select_root.setVisibility(View.GONE);
            return;
        } else {
            super.onBackPressed();
        }
    }

    protected void init() {
        carno = getIntent().getStringExtra("carno");
        vin = getIntent().getStringExtra("vin");
        findViewById(R.id.trajectory_playback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                startActivity(new Intent(PositioningDetailActivity.this, PathRecordActivity.class)
                        .putExtra("carno", carno).putExtra("vin", vin)
                        .putExtra("startDate", TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "000000")
                        .putExtra("endDate", TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "235959"));
            }
        });
        List<Location.Data> dataList = getIntent().getParcelableArrayListExtra("array");

        initTitle(carno);
        ((TextView) findViewById(R.id.titlebar_right)).setText("切换车辆");
        select_root = findViewById(R.id.select_root);
        mSearchView = select_root.findViewById(R.id.et_select_vehicle);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mSelectVehicleAdapter.refresh(dataList);
                }
                searchList.clear();
                for (Location.Data data : dataList) {
                    if (data.getCarno().contains(s)) {
                        searchList.add(data);
                    }
                }
                mSelectVehicleAdapter.refresh(searchList);
            }
        });

        mRecyclerView = select_root.findViewById(R.id.recyclerView);
        mSelectVehicleAdapter = new SelectVehicleAdapter(dataList, new SelectVehicleAdapter.SelectVehicleListener() {
            @Override
            public void select(String s, String c) {
                vin = s;
                carno = c;
                LogHelper.e("vin = " + vin + "   carno = " + carno);
                initTitle(carno);
                ((TextView) findViewById(R.id.truck_name)).setText(carno);
                select_root.setVisibility(View.GONE);
                loadData(vin);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSelectVehicleAdapter);

        findViewById(R.id.titlebar_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_root.setVisibility(View.VISIBLE);
                select_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_root.setVisibility(View.GONE);
                    }
                });
            }
        });

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
                    loadData(vin);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);

        findViewById(R.id.location_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (mData != null && !TextUtils.isEmpty(mData.getLongitude()) && !TextUtils.isEmpty(mData.getLatitude())) {
                    url = "http://m.amap.com/?q=" + mData.getLatitude() + "," + mData.getLongitude();
                    LogHelper.e("url = " + url);

                    startActivity(new Intent(PositioningDetailActivity.this, ShareActivity.class)
                            .putExtra("title", "车轮滚滚实时位置分享")
                            .putExtra("content", carno + "-" + mData.getAddress())
                            .putExtra("url", url));
                    overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
                }
            }
        });
    }

    private void loadData(String vin) {

        call = new RetrofitHelper().locationDetail(vin);
        call.enqueue(new Callback<LocationDetail>() {
            @Override
            public void onResponse(retrofit2.Call<LocationDetail> call, Response<LocationDetail> response) {
                mData = response.body().getData();
                initData(mData);
            }

            @Override
            public void onFailure(retrofit2.Call<LocationDetail> call, Throwable t) {
                LogHelper.e("onFailure");
            }
        });


    }

    private void initData(LocationDetail.Data bean) {

        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        if (bean == null) {
            return;
        }

        //逆地理编码
        if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude())) {
            LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        }

        ((TextView) findViewById(R.id.truck_name)).setText(carno);

        ((TextView) findViewById(R.id.address)).setText(bean.getAddress());

        switch (bean.getStatus()) {
            case "3":
                ((TextView) findViewById(R.id.truck_state)).setText("离线");
                findViewById(R.id.truck_state).setBackgroundResource(R.drawable.bg_grey);
                if (!TextUtils.isEmpty(bean.getTime())) {
                    try {
                        Date date = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(bean.getTime());

                        ((TextView) findViewById(R.id.truck_speed)).setText("离线时长：" + DateHelper.secondToTime(System.currentTimeMillis() - date.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "1":
                ((TextView) findViewById(R.id.truck_state)).setText("行驶");
                findViewById(R.id.truck_state).setBackgroundResource(R.drawable.bg_green);

                ((TextView) findViewById(R.id.truck_speed)).setText("速度：" + bean.getSpeed() + "km/h");
                break;
            case "2":
                ((TextView) findViewById(R.id.truck_state)).setText("停车");
                findViewById(R.id.truck_state).setBackgroundResource(R.drawable.bg_orange);

                if (!TextUtils.isEmpty(bean.getTime())) {
                    try {
                        Date date = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(bean.getTime());

                        ((TextView) findViewById(R.id.truck_speed)).setText("停车时长：" + DateHelper.secondToTime(System.currentTimeMillis() - date.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }

        DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        try {
            Date date = format1.parse(bean.getTime());
            ((TextView) findViewById(R.id.time)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            ((TextView) findViewById(R.id.time)).setText("");
        }

        addMarkersToMap(bean);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            map.onSaveInstanceState(outState);
        }
    }

    private void addMarkersToMap(LocationDetail.Data bean) {
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
        markerOptions.snippet(carno);

        switch (bean.getStatus()) {
            case "1":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ico_speed));
                break;
            case "3":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ico_offline));
                break;
            case "2":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ico_park));
                break;
            default:
                break;
        }
        markerOptions.anchor(0.5f, 0.5f);

        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(bean);
        marker.setRotateAngle(new Random().nextInt(100));

        int width = MeasureHelper.getScreenWidth(this);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), width * 4 / 5, width * 4 / 5, 0);
        aMap.moveCamera(cu);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//设置缩放等级

        getInfoWindow(marker);
        marker.showInfoWindow();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LogHelper.e("getInfoWindow(Marker marker)");
        LocationDetail.Data info = (LocationDetail.Data) marker.getObject();

        TextView textMarker = new TextView(this);
        textMarker.setPadding(6, 2, 6, 2);
        switch (info.getStatus()) {
            case "3":
                textMarker.setTextColor(ContextCompat.getColor(this, R.color.offline));
                textMarker.setBackgroundResource(R.drawable.bg_offline);
                break;
            case "1":
                textMarker.setTextColor(ContextCompat.getColor(this, R.color.speed));
                textMarker.setBackgroundResource(R.drawable.bg_speed);
                break;
            case "2":
                textMarker.setTextColor(ContextCompat.getColor(this, R.color.park));
                textMarker.setBackgroundResource(R.drawable.bg_park);
                break;
            default:
                break;
        }
        textMarker.setText(carno);
        return textMarker;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LogHelper.e("getInfoContents(Marker marker)");
        return null;
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                //((TextView) findViewById(R.id.address)).setText(String.format(result.getRegeocodeAddress().getFormatAddress(), "附近"));
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