package com.sc.clgg.activity

import android.content.Intent
import android.graphics.Color
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.coroutines.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions


class SplashActivity : BaseImmersionActivity() {

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.e("onCreate")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        LogHelper.e("onRestoreInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        LogHelper.e("onRestoreInstanceState双")
    }

    override fun onStart() {
        super.onStart()
        LogHelper.e("onStart")
    }*/

    override fun onResume() {
        super.onResume()
        LogHelper.e("onResume")
        if (EasyPermissions.hasPermissions(this, *ConstantValue.PERMISSION_NEED)) {
            init()
        }
    }




    private fun init() {
        when (0) {
            4 -> {
            }
            3 -> {
                //startSignContact(privatekey, zqid, user_code, no, signtype)
            }
            1 -> {
                GlobalScope.launch {
                    val deffered = async { RetrofitHelper().area.execute() }

                    val http = deffered.await()

                    withContext(Dispatchers.Main) {
                        if (http.isSuccessful) {
                            toast("成功")
                        } else {
                            LogHelper.e("失败 is ${http.errorBody().toString()}")
                            toast(R.string.network_anomaly)
                        }
                    }
                }.apply { job = this;job.start() }
            }
            2 -> {
                Thread {
                    Looper.prepare()
                    val ts = Toast.makeText(this, "", Toast.LENGTH_SHORT)
                    ts.view = Button(this).apply { text = getString(R.string._1);backgroundColor = Color.GREEN }
                    ts.show()
                    Looper.loop()
                }.start()
            }
            else -> {
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }
        }

    }

    private lateinit var job: Job

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogHelper.e("onConfigurationChanged")
    }

    override fun onPause() {
        super.onPause()
        LogHelper.e("onPause")
    }

    override fun onStop() {
        super.onStop()
        LogHelper.e("onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        LogHelper.e("onSaveInstanceState()")
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        LogHelper.e("onSaveInstanceState()双")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.e("onDestroy")
    }*/
}
