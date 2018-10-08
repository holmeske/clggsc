package com.sc.clgg.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


/**
 * @author lvke
 */
public class App extends Application {

    public static App instance;

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
        AppPresenterKt.start(this);
    }

}
