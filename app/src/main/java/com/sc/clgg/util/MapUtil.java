package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.sc.clgg.R;
import com.sc.clgg.bean.AllAppInfo;
import com.sc.clgg.bean.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * @Description描述:地图工具类
 * @Author作者:lip
 * @Date日期:2014-12-12 下午4:29:52
 */
public final class MapUtil {

    /**
     * 获取已经安装的应用程序
     */
    public static ArrayList<AllAppInfo> getAllAppInfos(Context mContext) {
        ArrayList<AllAppInfo> appList = new ArrayList<>();
        List<PackageInfo> packageInfos = mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo = packageInfos.get(i);
            AllAppInfo allAppInfo = new AllAppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());// 应用程序的名称
            allAppInfo.setPackagename(pInfo.packageName);// 应用程序的包
            allAppInfo.setVersionCode(pInfo.versionCode);// 版本号
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            appList.add(allAppInfo);
        }
        return appList;
    }

    // 判断手机安装了哪些地图
    public static ArrayList<Service> getMapApp(Activity mActivity) {

        ArrayList<Service> list = new ArrayList<>();

        // 手机安装的地图
        for (Iterator<AllAppInfo> iterator = getAllAppInfos(mActivity.getApplicationContext()).iterator(); iterator.hasNext(); ) {

            AllAppInfo info = iterator.next();

            // 添加百度地图
            if ("百度地图".equals(info.getAppname()) && "com.baidu.BaiduMap".equals(info.getPackagename())) {
                list.add(new Service(mActivity.getString(R.string.baidu_map), R.drawable.baidu_map));
            }
            // 添加高德地图  18 9 499 2 4 7 14
            if ("高德地图".equals(info.getAppname()) && "com.autonavi.minimap".equals(info.getPackagename())) {
                list.add(new Service(mActivity.getString(R.string.amap), R.drawable.gaode_map));
            }

        }
        return list;
    }

}
