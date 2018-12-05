package com.sc.clgg.activity.vehicle.locate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.bean.Location;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.MeasureHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 */
public class LocateActivity extends BaseImmersionActivity implements AMap.OnMarkerClickListener, AMap.OnMapLoadedListener {

    private MapView map;
    private ProgressBar mProgressBar;

    private boolean isVisible = true;
    private AMap aMap;
    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Call<Location> call;

    private Location mLocation;
    private ArrayList<Location.Data> array;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        mProgressBar = findViewById(R.id.progressBar);
        map = findViewById(R.id.map);

        map.onCreate(savedInstanceState);

        init();
    }

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
        if (call != null) {
            call.cancel();
        }
        if (map != null) {
            map.onDestroy();
        }
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService.shutdownNow();
        }
        mScheduledExecutorService = null;

    }

    protected void init() {
        initTitle("GPS定位");

        aMap = map.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NAVI);//设置地图模式。
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);//设置“高德地图”Logo的位置。
        aMap.getUiSettings().setZoomControlsEnabled(true);//设置缩放按钮是否可见。
        aMap.getUiSettings().setScrollGesturesEnabled(true);//设置拖拽手势是否可用。
        aMap.getUiSettings().setScaleControlsEnabled(true);//设置比例尺控件是否可见
        aMap.getUiSettings().setTiltGesturesEnabled(false);//设置倾斜手势是否可用。
        aMap.getUiSettings().setRotateGesturesEnabled(false);//设置旋转手势是否可用。
        aMap.setOnMarkerClickListener(this);//设置marker点击事件监听接口。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(6));

        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LogHelper.e("isVisible = " + isVisible);
                if (isVisible) {
                    loadData();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    private void loadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        call = new RetrofitHelper().location();
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(@NonNull Call<Location> call, Response<Location> response) {
                mProgressBar.setVisibility(View.GONE);
                mLocation = response.body();
                if (mLocation.getSuccess()) {
                    if (mLocation.getData() != null) {
                        List<Location.Data> dataList = mLocation.getData();

                        ArrayList<Location.Data> newList = new ArrayList<>();
                        for (Location.Data bean : dataList) {
                            if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude()) && !TextUtils.isEmpty(bean.getStatus())) {
                                newList.add(bean);
                            }
                        }
                        array = newList;
                        if (array.size() == 1) {
                            Intent intent = new Intent(LocateActivity.this, LocationDetailActivity.class);
                            intent.putExtra("carno", array.get(0).getCarno());
                            intent.putExtra("vin", array.get(0).getVin());
                            intent.putParcelableArrayListExtra("array", array);
                            startActivity(intent);
                        } else {
                            addMarkersToMap(newList);
                        }
                    }
                } else {
                    Toast.makeText(LocateActivity.this, mLocation.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                LogHelper.e("onFailure");
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            map.onSaveInstanceState(outState);
        }
    }

    private void addMarkersToMap(List<Location.Data> list) {
        aMap.clear();//从地图上删除所有的overlay（marker，circle，polyline 等对象）。
        aMap.reloadMap();//重新加载地图引擎，即调用此接口时会重新加载底图数据，覆盖物不受影响。

        LatLngBounds.Builder builder = LatLngBounds.builder();

        //过滤不包含经纬度的不完整bean
        for (Location.Data bean : list) {
            LatLng latLng = new LatLng(Double.parseDouble(bean.getLatitude()), Double.parseDouble(bean.getLongitude()));
            builder.include(latLng);

            MarkerOptions markerOptions = new MarkerOptions();//它定义了marker 的属性信息。
            markerOptions.position(latLng);//设置Marker覆盖物的位置坐标。

            View view = View.inflate(this, R.layout.view_amap_marker, null);
            TextView textCarNo = view.findViewById(R.id.text_car_no);
            ImageView img_car_status = view.findViewById(R.id.img_car_status);

            switch (bean.getStatus()) {
                case "3":
                    img_car_status.setBackgroundResource(R.drawable.map_ico_offline);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.offline));
                    textCarNo.setBackgroundResource(R.drawable.bg_offline);
                    break;
                case "1":
                    img_car_status.setBackgroundResource(R.drawable.map_ico_speed);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.speed));
                    textCarNo.setBackgroundResource(R.drawable.bg_speed);
                    break;
                case "2":
                    img_car_status.setBackgroundResource(R.drawable.map_ico_park);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.park));
                    textCarNo.setBackgroundResource(R.drawable.bg_park);
                    break;
                default:
                    break;
            }
            textCarNo.setText(bean.getCarno());
            img_car_status.setRotation(Float.parseFloat(bean.getDirection()));
            //设置Marker覆盖物的图标。
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(view)));
            view.setDrawingCacheEnabled(false);
            //设置Marker覆盖物的锚点比例。
            markerOptions.anchor(0.5f, 0.5f);

            //设置Marker覆盖物的附加信息对象。用户可以自定义Marker的属性。
            aMap.addMarker(markerOptions).setObject(bean);
        }

        try {

            int width = MeasureHelper.getScreenWidth(this);
            //按照传入的CameraUpdate参数改变地图状态。
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width * 4 / 5, width * 4 / 5, 0));
            //设置缩放等级
//            aMap.moveCamera(CameraUpdateFactory.zoomTo(6));
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
        Location.Data data = (Location.Data) marker.getObject();
        Intent intent = new Intent(this, LocationDetailActivity.class);
        intent.putExtra("carno", data.getCarno());
        intent.putExtra("vin", data.getVin());
        intent.putParcelableArrayListExtra("array", array);
        startActivity(intent);
        //true 返回true表示该点击事件已被处理，不再往下传递（如底图点击不会被触发），返回false则继续往下传递。
        return true;
    }

}
