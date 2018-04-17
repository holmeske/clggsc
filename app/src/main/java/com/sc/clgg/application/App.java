package com.sc.clgg.application;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sc.clgg.BuildConfig;
import com.sc.clgg.R;
import com.sc.clgg.bean.LocationBean;
import com.sc.clgg.dialog.AlertDialogHelper;
import com.sc.clgg.http.HttpManager;
import com.sc.clgg.util.FileUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tool.helper.LogHelper;
import tool.helper.MeasureUtils;

public class App extends Application {

    public static int screenWidth, screenHeight;
    /**
     * 定位数据实体类
     */
    public static LocationBean mLocationBean = new LocationBean();
    private static App instance;
    /**
     * 用于定位管理器
     */
    private static AMapLocationClientOption mLocationOption = null;
    /**
     * 用于定位管理器
     */
    private AMapLocationClient mlocationClient = null;
    private AMapLocationListener mAMapLocationListener = amapLocation -> {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息

                mLocationBean.setLatitude(amapLocation.getLatitude());
                mLocationBean.setLongitude(amapLocation.getLongitude());
                //mLocationBean.setTime(location.getTime());
                mLocationBean.setTime(System.currentTimeMillis());
                Bundle locBundle = amapLocation.getExtras();
                if (locBundle != null) {
                    mLocationBean.setLocation(locBundle.getString("desc"));
                }

                mLocationBean.setProvince(amapLocation.getProvince());
                mLocationBean.setCity(amapLocation.getCity());
                mLocationBean.setDistrict(amapLocation.getDistrict());
                /*LogUtils.e("(维度Latitude,经度Longitude)   =   "
                        + "(" + String.valueOf(mLocationBean.getLatitude()) + "," + String.valueOf(mLocationBean.getLongitude())
                        + ")");*/
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogHelper.e("location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    };
    private List<String> permissions = new ArrayList<>();

    public static App getInstance() {
        return instance;
    }

    private static void setInstance(App application) {
        instance = application;
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 描述:初始化屏幕信息
     */
    private void initScreeenInfomation() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        LogHelper.v("width:" + screenWidth + "  height:" + screenHeight);
        LogHelper.v("widthDP:" + MeasureUtils.px2dp(this, screenWidth) + "  heightDP:" + MeasureUtils.px2dp(this, screenHeight));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);

		/* 创建APP文件夹 */
        FileUtil.getInstance().createFiles(null);

		/* 初始化屏幕信息 */
        initScreeenInfomation();

        startLocation();

        LogHelper.setLogSwitch(BuildConfig.LOG_DEBUG);

        HttpManager.init(this);
        registerActivityLifecycleCallbacks();

        /*if (!BuildConfig.LOG_DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this, PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0));
        }*/

//        initBugly();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    private void initBugly() {
        String packageName = getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "0edd50c749", !BuildConfig.LOG_DEBUG, strategy);
    }

    public void setLocationBean(Object o, boolean b, int i) {
        mLocationBean.setList(JSON.toJSONString(o));
        mLocationBean.setFlag(b);
        mLocationBean.setPosition(i);
    }

    /**
     * 销毁定位
     */
    public void stopLocation() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    /**
     * 激活定位
     */
    public void startLocation() {
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getApplicationContext());
        }
        //设置定位监听
        mlocationClient.setLocationListener(mAMapLocationListener);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //开启定位服务
        mlocationClient.startLocation();
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                LogHelper.d("activity", "onActivityCreated    " + activity.getLocalClassName());

                if (activity.findViewById(R.id.titlebar_title) != null) {
                    ((TextView) activity.findViewById(R.id.titlebar_title)).setText(activity.getTitle());
                }
                if (activity.findViewById(R.id.titlebar_left) != null) {
                    activity.findViewById(R.id.titlebar_left).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.finish();
                        }
                    });
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogHelper.d("activity", "onActivityStarted    " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogHelper.d("activity", "onActivityResumed    " + activity.getLocalClassName());

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    AlertDialogHelper.instance.show(activity,
                            "为了保证功能的正常使用，请前往设置中同意以下权限: \n\n\t\t存储\n\t\t读取位置信息\n\t\t读取本机识别码",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    activity.startActivity(intent);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            }, null);
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogHelper.d("activity", "onActivityPaused    " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogHelper.d("activity", "onActivityStopped    " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                LogHelper.d("activity", "onActivitySaveInstanceState    " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogHelper.d("activity", "onActivityDestroyed    " + activity.getLocalClassName());
            }

        });
    }
}
