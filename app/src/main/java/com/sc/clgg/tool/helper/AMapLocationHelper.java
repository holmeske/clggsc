package com.sc.clgg.tool.helper;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sc.clgg.bean.LocationBean;

/**
 * @author：lvke
 * @date：2018/6/28 17:07
 */
public class AMapLocationHelper {
    /**
     * 用于定位管理器
     */
    private AMapLocationClientOption mLocationOption;
    /**
     * 定位数据实体类
     */
    private LocationBean mLocationBean = new LocationBean();
    /**
     * 用于定位管理器
     */
    private AMapLocationClient mlocationClient;
    /**
     * 自定义定位监听器
     */
    private OnLocationListener mOnLocationListener;
    /**
     * 高德定位监听器
     */
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息

                    mLocationBean.setLatitude(aMapLocation.getLatitude());
                    mLocationBean.setLongitude(aMapLocation.getLongitude());

                    mLocationBean.setTime(System.currentTimeMillis());

                    Bundle locBundle = aMapLocation.getExtras();
                    if (locBundle != null) {
                        mLocationBean.setLocation(locBundle.getString("desc"));
                    }
                    mLocationBean.setProvince(aMapLocation.getProvince());
                    mLocationBean.setCity(aMapLocation.getCity());
                    mLocationBean.setDistrict(aMapLocation.getDistrict());

                    if (mOnLocationListener != null) {
                        mOnLocationListener.onLocationChanged(mLocationBean);
                    }
                    LogHelper.e("(" + mLocationBean.getLatitude() + "," + mLocationBean.getLongitude() + ")" + mLocationBean.getProvince());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    LogHelper.e("location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public AMapLocationHelper(Context context, OnLocationListener listener) {
        this.mOnLocationListener = listener;
        startLocation(context);
    }

    /**
     * 开启高德定位
     */
    public void startLocation(Context context) {
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(2 * 1000);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(8000);
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
        }
        //设置定位监听
        mlocationClient.setLocationListener(mAMapLocationListener);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //开启定位服务
        mlocationClient.startLocation();
    }

    /**
     * 销毁高德定位
     */
    public void stopLocation() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    public interface OnLocationListener {
        void onLocationChanged(LocationBean bean);
    }

}
