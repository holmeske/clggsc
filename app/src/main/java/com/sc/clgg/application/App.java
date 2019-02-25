package com.sc.clgg.application;

import android.app.Application;
import android.content.Context;

import com.android.recharge.ObuInterface;
import com.sc.clgg.BuildConfig;
import com.sc.clgg.service.AppService;
import com.sc.clgg.util.UmengHelper;
import com.squareup.leakcanary.LeakCanary;

import androidx.multidex.MultiDex;


/**
 * @author lvke
 */
public class App extends Application {

    public static App instance;
    public ObuInterface mObuInterface;

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppPresenterKt.init(this);
    }

    /**
     * 延迟初始化
     */
    public void lateInit() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...

        mObuInterface = new ObuInterface(this);
        mObuInterface.initialize();
        mObuInterface.openLog(BuildConfig.LOG_DEBUG);

        new UmengHelper().init(this);
        AppService.start(this);
        AppPresenterKt.initBugly(this);
    }

}
