package com.sc.clgg.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.android.recharge.ObuInterface
import com.sc.clgg.service.AppService
import com.sc.clgg.util.UmengHelper
import org.jetbrains.anko.doAsync

/**
 * @author lvke
 */
class App : Application() {
    lateinit var mObuInterface: ObuInterface

    companion object {
        lateinit var app: App

        fun getInstance(): App {
            return app
        }
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        init()
        initObuInterface()
        doAsync { lateInit() }
    }

    /**
     * 延迟初始化
     */
    private fun lateInit() {
        UmengHelper().init(this)
        AppService.start(this)
        initBugly()
    }

    private fun initObuInterface() {
        mObuInterface = ObuInterface(this)
        mObuInterface.initialize()
        /*mObuInterface.initializeObu(this, object : BluetoothObuCallback {
            override fun onTransferTimeout() {
                LogHelper.v("onTransferTimeout()")
            }

            override fun onConnectSuccess() {
                LogHelper.v("onConnectSuccess()")
            }

            override fun onDisconnect() {
                LogHelper.v("onDisconnect()")
            }

            override fun onConnectTimeout() {
                LogHelper.v("onConnectTimeout()")
            }

            override fun onReceiveObuCmd(p0: String?, p1: String?) {
                LogHelper.v("onReceiveObuCmd() p0:${p0}  p1:${p1}")
            }

            override fun onError(p0: String?, p1: String?) {
                LogHelper.v("onError() p0:${p0}  p1:${p1}")
            }
        })*/
//        mObuInterface.openLog(BuildConfig.LOG_DEBUG) //10
    }
}
