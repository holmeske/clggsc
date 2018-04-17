package com.sc.clgg.activity.vehiclemanager.maintenance;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sc.clgg.R;
import com.sc.clgg.adapter.ViewPagerAdapter;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.MarkerBean;
import com.sc.clgg.bean.ServiceBean;
import com.sc.clgg.bean.StoreInfoBean;
import com.sc.clgg.util.DialogUtil;
import com.sc.clgg.util.MapUtil;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tool.helper.LogHelper;

/**
 * 地图（维修保养，加油加气站，物流园）
 *
 * @author lvke
 */
public class MapActivity extends AppCompatActivity implements OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,
        InfoWindowAdapter, OnPageChangeListener {

    @BindView(R.id.map) MapView mapView;// 高德地图
    @BindView(R.id.mViewPager) ViewPager mViewPager;
    private Unbinder binder;
    private AMap aMap; // 地图对象
    private LatLng currentLatLng; // 当前经纬度
    private Marker marker2;// 有跳动效果的marker对象
    private int currentIndex = 0;// 记录当前页面位置
    private List<MarkerBean> markerBeans;// 覆盖物数据
    private List<View> views;
    private List<StoreInfoBean> fiArrayList;// 商铺数据集合
    private Class cls;
    private int icon_big, icon_small;

    @OnClick(R.id.img_service_baoyang)
    void a() {
        setCurrentLocation(App.mLocationBean.getLatitude(), App.mLocationBean.getLongitude(), 14);//重新定位
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cls = getIntent().getParcelableExtra("cls");
        if (cls == MaintenanceActivity.class) {
            icon_big = R.drawable.weixiu_da;
            icon_small = R.drawable.weixiu_xiao;
        } else if (cls == FuelGasActivity.class) {
            icon_big = R.drawable.gasda;
            icon_small = R.drawable.gasxiao;
        } else if (cls == LogisticsParkActivity.class) {
            icon_big = R.drawable.parkingda;
            icon_small = R.drawable.parkingxiao;
        }


        setTitle(TextUtils.isEmpty(getIntent().getStringExtra("title")) ? "" : getIntent().getStringExtra("title"));
        setContentView(R.layout.activity_service_map);
        super.onCreate(savedInstanceState);
        binder = ButterKnife.bind(this);

        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        init();
        initMapView();
        initViewPager();
        addMarkersToMap(markerBeans);
    }

    private void init() {
//        fiArrayList = JSON.parseArray(App.mLocationBean.getList(), StoreInfoBean.class);
        fiArrayList = new Gson().fromJson(App.mLocationBean.getList(), new TypeToken<List<StoreInfoBean>>() {
        }.getType());
        markerBeans = new ArrayList<>();
        views = new ArrayList<>();
    }

    // 此方法必须重写
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    // 此方法必须重写
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // 此方法必须重写
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (binder != Unbinder.EMPTY) {
            binder.unbind();
        }
    }


    /**
     * 描述: 地图添加覆盖物
     */
    private void addMarkersToMap(List<MarkerBean> markerBeans) {

        if (aMap != null) {
            aMap.clear();
        }

        // 获取定位数据
        Double latitude = App.mLocationBean.getLatitude();
        Double longitude = App.mLocationBean.getLongitude();

        // 默认位置是北京
        if (latitude == null || longitude == null) {
            currentLatLng = new LatLng(39.90403, 116.407525);
        } else {
            currentLatLng = new LatLng(latitude, longitude);
        }

        MarkerOptions markerOption = new MarkerOptions().position(currentLatLng).draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location));
        marker2 = aMap.addMarker(markerOption);
        // marker旋转0度
        marker2.setRotateAngle(0);

        for (int i = 0, size = markerBeans.size(); i < size; i++) {

            MarkerBean m = markerBeans.get(i);

            markerOption = new MarkerOptions().position(new LatLng(m.getLongitude(), m.getLatitude())).draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(m.getIcon()));
            Marker marker = aMap.addMarker(markerOption);
            // marker.setTitle(m.getTitle());
            // marker.setSnippet(String.valueOf(i));
            // marker旋转0度
            marker.setRotateAngle(0);
        }
    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }


    // 初始化覆盖物数据
    private void initData(int index) {
        markerBeans.clear();
        for (int i = 0, size = fiArrayList.size(); i < size; i++) {
            StoreInfoBean sb = fiArrayList.get(i);
            if (i == index) {
                markerBeans.add(new MarkerBean(sb.getLat(), sb.getLng(), sb.getName(), icon_big));
            } else {
                markerBeans.add(new MarkerBean(sb.getLat(), sb.getLng(), sb.getName(), icon_small));
            }
        }
    }

    private void initMapView() {
        //初始化地图
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        }
        setCurrentLocation(App.mLocationBean.getLatitude(), App.mLocationBean.getLongitude(), 14);
    }

    // 初始化ViewPager
    private void initViewPager() {

        if (fiArrayList.isEmpty()) {
            return;
        }

        // 获取页面下标
        currentIndex = App.mLocationBean.getPosition();

        for (int i = 0, size = fiArrayList.size(); i < size; i++) {
            View view = View.inflate(this, R.layout.view_site, null);
            // 名称
            TextView company_name = (TextView) view.findViewById(R.id.company_name);
            // 距离
            TextView company_instance = (TextView) view.findViewById(R.id.company_instance);
            // 地址
            TextView company_position = (TextView) view.findViewById(R.id.company_position);
            // 电话
            TextView company_phone = (TextView) view.findViewById(R.id.company_phone);
            // 联系人
            TextView company_person = (TextView) view.findViewById(R.id.company_person);

            StoreInfoBean sb = fiArrayList.get(i);

            company_name.setText(sb.getName());

            company_instance.setText(getString(R.string.placeholders_two, Tools.calculateTwoDotsDistance(sb.getLat(), sb.getLng()), "km"));

            company_position.setText(sb.getAddress());

            company_phone.setText(sb.getPhone());

            // 缺少联系人
            String contact = sb.getContact();
            String phonenum = sb.getPhonenum();
            if (Tools.isNull(contact)) {
                company_person.setText("暂无");
            } else {
                // 缺少电话
                if (Tools.isNull(phonenum)) {
                    company_person.setText(contact);
                } else {
                    company_person.setText(contact + "(" + phonenum + ")");
                }
            }
            //  详情
            view.findViewById(R.id.line_detail).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.e("xiangqing = = " + fiArrayList.get(currentIndex).toString());
                    App.getInstance().setLocationBean(fiArrayList.get(currentIndex), false, currentIndex);
                    App.mLocationBean.setObj(new Gson().toJson(fiArrayList.get(currentIndex)));
                    startActivity();
                }
            });
            //  导航
            view.findViewById(R.id.line_map).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigation();
                }
            });
            // 添加到集合
            views.add(view);
        }

        mViewPager.setAdapter(new ViewPagerAdapter(views));
        // 设置ViewPager的默认项
        mViewPager.setCurrentItem(currentIndex);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.text_size_10));
        // 初始化数据
        initData(currentIndex);
        // 设置中心店铺坐标
        setCenterStore(currentIndex, 14);
    }

    private void navigation() {
        ArrayList<ServiceBean> list = MapUtil.getMapApp(this);
        if (list.isEmpty()) {
            return;
        }
        StoreInfoBean bean = fiArrayList.get(currentIndex);
        DialogUtil.showCustomDialog(this, bean, App.screenWidth, ActionBar.LayoutParams.WRAP_CONTENT, R.style.Theme_dialog, list);
    }

    private void startActivity() {
        startActivity(new Intent(this, BusinessInfoActivity.class).putExtra("cls", cls));
        overridePendingTransition(R.anim.push_right_in, R.anim.scale_out);
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

    @Override
    public void onInfoWindowClick(Marker arg0) {

    }

    @Override
    public void onMapLoaded() {

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

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        findViewById(R.id.rl_vp).invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageSelected(final int position) {
        // 设置中心店铺坐标
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCenterStore(position, 14);
                currentIndex = position;
                initData(currentIndex);
                addMarkersToMap(markerBeans);
            }
        }, 200);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 设置中心坐标位置
     */
    private void setCenterStore(int index, int zoom) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));// 设置缩放比例
        StoreInfoBean sb = fiArrayList.get(index);
        // 设置中心点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(sb.getLat(), sb.getLng())));
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

}
