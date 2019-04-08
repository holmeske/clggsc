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
                LogHelper.e("BluetoothObuCallback  ------  onTransferTimeout()")
            }

            override fun onConnectSuccess() {
                LogHelper.e("BluetoothObuCallback  ------  onConnectSuccess()")
            }

            override fun onDisconnect() {
                LogHelper.e("BluetoothObuCallback  ------  onDisconnect()")
            }

            override fun onConnectTimeout() {
                LogHelper.e("BluetoothObuCallback  ------  onConnectTimeout()")
            }

            override fun onReceiveObuCmd(p0: String?, p1: String?) {
                LogHelper.e("BluetoothObuCallback  ------  onReceiveObuCmd()")
            }

            override fun onError(p0: String?, p1: String?) {
                LogHelper.e("BluetoothObuCallback  ------  onError()")
            }
        })*/
//        mObuInterface.openLog(BuildConfig.LOG_DEBUG) //10
    }
}
