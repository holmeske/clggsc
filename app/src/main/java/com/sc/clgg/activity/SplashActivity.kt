package com.sc.clgg.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
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
    }

    override fun onResume() {
        super.onResume()
        LogHelper.e("onResume")
        if (EasyPermissions.hasPermissions(this, *ConstantValue.PERMISSION_NEED)) {
            init()
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == START_SIGN_REQUEST_CODE) {
            when (resultCode) {
                SignLibraryActivity.RESULT_SIGN_SUCCESS -> {
                    toast("签署成功")
                }
                SignLibraryActivity.RESULT_SIGN_FAIL -> {
                    toast("签署失败")
                }
                SignLibraryActivity.RESULT_SIGN_TYPE_ERROR -> {
                    toast("无效的签署方式")
                }
                SignLibraryActivity.RESULT_PRIVATE_KEY_NULL -> {
                    toast("私钥为空")
                }
                SignLibraryActivity.RESULT_SIGN_NO_NULL -> {
                    toast("合同编号为空")
                }
                SignLibraryActivity.RESULT_SIGN_USER_CODE_NULL -> {
                    toast("合同签署人")
                }
                SignLibraryActivity.RESULT_SIGN_ZQID_NULL -> {
                    toast("zqid为空")
                }
            }
        }
    }*/

    private val START_SIGN_REQUEST_CODE = 999
    private val privatekey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIca5bUFnKuZ/cL8GwT\n" +
            "xI7YQ4T8LRgOswDEIYY7B2sa2cCbKVOoG4VyIOlI0kOazC//GTJULQNXB1Bn8cP8NdGyA\n" +
            "WW7VRROITrHjg0e32K9GuJ3iuRAf5g9N3L2NaobPhwsz5QcnubLSbf97qk7IxKg4LOH6\n" +
            "e/mb/iyy9U4tIuPnAgMBAAECgYBktOzywWsmEpQwSqGNTn3zEaQEzU90H8nnArtNoT\n" +
            "yFkKoKkBk5lMVZpwXqDtrPzDiugE7Hbmr2qxOCB0TnKU1VErAhWz+RDvCfbzJ2IVhy7f/\n" +
            "nE2UUtX1evrWjoH8kb5GGydTSUNR/9wa+kL3QH4M8maYZDjxaoU/aq8GUuVSf4QJBA\n" +
            "Oo9aBDnfaCNr9aMC6D59pfHv13+fGMjgc9ftGH3WFBwNE9jyP1ncZVNxAAlapjyo3lIFL\n" +
            "F3AH8XJBwmni9JWFcCQQCTp+irV8sVHIphmWMxxB1FXkCQtSXA6PQMfc9EfsC6Df84l\n" +
            "ZMtZKZZmpkxyMrQ8iLyZJtfqUGBgXABdDWB8tbxAkB4F4ODJywBZKMCHErZ/U2wA3jZ\n" +
            "d9/N9CZZa3fPwkBE/UHUchRZ1u8k3PngGU7Nm5i0VYBGe/yFLBgXVrVDCReBAkAfTIR\n" +
            "hbaa6vuYGckE4l12tCqGdCwkJr1esHi8FUekAh18GW45rHN62N7Mpfmqfh/QodXMKA\n" +
            "BLGpO1dIYkGFrfBAkBqEAVpF2uE4NSZUw4DWc2wIqdiz+1T8UfscVx11WGhvLDcALI6A\n" +
            "8tVVFG6Rfg80GlTyXeA2iJakmwX8dP0yo/o"
    private val zqid = "ZQ8E9CD89AC28B4B7DA941891F46915890"
    private val user_code = "001"
    private val no = "001"
    private val signtype = "3"

    /**
     * 进去签署合同页
     * @param privatekey 私钥
     * @param zqid 众签id
     * @param user_code 合同签署id
     * @param no 合同编号
     * @param signtype 合同的签署方式(1手写签署不认证，2手写签署短信认证，3签章签署不认证，4签章签署短信认证)
     */
    /*private fun startSignContact(privatekey: String, zqid: String, user_code: String, no: String, signtype: String) {
        val intent = Intent(this, SignLibraryActivity::class.java)
        intent.putExtra(Constants.PRIVATEKEY, privatekey)
        intent.putExtra(Constants.SIGN_ZQ_ID, zqid)
        intent.putExtra(Constants.SIGN_USER_CODE, user_code)
        intent.putExtra(Constants.SIGN_NO, no)
        intent.putExtra(Constants.SIGN_TYPE, signtype)
        startActivityForResult(intent, START_SIGN_REQUEST_CODE)
    }*/

    private fun init() {
        val v = 0
        when (v) {
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
                    var ts = Toast.makeText(this, "", Toast.LENGTH_SHORT)
                    ts.view = Button(this).apply { text = "button";backgroundColor = Color.GREEN }
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

    override fun onConfigurationChanged(newConfig: Configuration) {
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
    }
}
