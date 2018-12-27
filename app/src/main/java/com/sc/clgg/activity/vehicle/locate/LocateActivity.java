package com.sc.clgg.activity.vehicle.locate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 */
public class LocateActivity extends BaseImmersionActivity implements AMap.OnMarkerClickListener, AMap.OnMapLoadedListener {

    private MapView map;

    private boolean isVisible = true;
    private AMap aMap;
    private Call<Location> http;

    private ArrayList<Location.Data> array;

    private MutableLiveData<Location> mLiveData = new MutableLiveData<>();
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        map = findViewById(R.id.map);

        map.onCreate(savedInstanceState);

        init();

        mLiveData.observe(this, this::updateView);
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
        if (http != null) {
            http.cancel();
        }
        if (map != null) {
            map.onDestroy();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @SuppressLint("HandlerLeak")
    protected void init() {
        initTitle("GPS定位");

        aMap = map.getMap();
        //设置地图模式。
        aMap.setMapType(AMap.MAP_TYPE_NAVI);
        //设置“高德地图”Logo的位置。
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
        //设置缩放按钮是否可见。
        aMap.getUiSettings().setZoomControlsEnabled(true);
        //设置拖拽手势是否可用。
        aMap.getUiSettings().setScrollGesturesEnabled(true);
        //设置比例尺控件是否可见
        aMap.getUiSettings().setScaleControlsEnabled(true);
        //设置倾斜手势是否可用。
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        //设置旋转手势是否可用。
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        //设置marker点击事件监听接口。
        aMap.setOnMarkerClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(6));

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                LogHelper.e("isVisible = " + isVisible);
                if (isVisible) {
                    loadData();
                }
                mHandler.sendEmptyMessageDelayed(0, 10000);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    private void updateView(Location mLocation) {
        LogHelper.e("GPS定位");
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

    private void loadData() {
        showProgressDialog();
        http = new RetrofitHelper().location();
        http.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(@NonNull Call<Location> call, @NonNull Response<Location> response) {
                hideProgressDialog();
                mLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Location> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(LocateActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
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
            LatLng latLng = new LatLng(Double.parseDouble(Objects.requireNonNull(bean.getLatitude())),
                    Double.parseDouble(Objects.requireNonNull(bean.getLongitude())));
            builder.include(latLng);
            //它定义了marker 的属性信息。
            MarkerOptions markerOptions = new MarkerOptions();
            //设置Marker覆盖物的位置坐标。
            markerOptions.position(latLng);

            View view = View.inflate(this, R.layout.view_amap_marker, null);
            TextView textCarNo = view.findViewById(R.id.text_car_no);
            ImageView imgCarStatus = view.findViewById(R.id.img_car_status);

            switch (Objects.requireNonNull(bean.getStatus())) {
                case "3":
                    imgCarStatus.setBackgroundResource(R.drawable.map_ico_offline);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.offline));
                    textCarNo.setBackgroundResource(R.drawable.bg_offline);
                    break;
                case "1":
                    imgCarStatus.setBackgroundResource(R.drawable.map_ico_speed);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.speed));
                    textCarNo.setBackgroundResource(R.drawable.bg_speed);
                    break;
                case "2":
                    imgCarStatus.setBackgroundResource(R.drawable.map_ico_park);
                    textCarNo.setTextColor(ContextCompat.getColor(this, R.color.park));
                    textCarNo.setBackgroundResource(R.drawable.bg_park);
                    break;
                default:
                    break;
            }
            textCarNo.setText(bean.getCarno());
            imgCarStatus.setRotation(Float.parseFloat(Objects.requireNonNull(bean.getDirection())));
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
