package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.basic.MainActivity
import com.sc.clgg.activity.contact.LoginContact
import com.sc.clgg.activity.presenter.LoginPresenter
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.LoadingDialogHelper
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CheckHelper
import com.sc.clgg.util.Tools
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), LoginContact {
    override fun onSuccess(body: String?) {
    }

    override fun onError(msg: String?) {
    }

    override fun onToast(msg: String?) {
    }

    override fun onStartLoading() {
    }

    override fun setButtonSuccess() {
    }

    override fun jumpOtherActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    private var mLoginPresenter: LoginPresenter? = null
    private var mLoadingDialogHelper: LoadingDialogHelper? = null
    private val mCountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_send.text = String.format("%ds", millisUntilFinished / 1000)
        }

        override fun onFinish() {
            tv_send.text = "发送验证码"
            tv_send.isEnabled = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private var canRegister: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLoadingDialogHelper = LoadingDialogHelper(activity)
        mLoginPresenter = LoginPresenter(this)

        et_new_phone.setTextChangeListener {
            if (it.length == 11) {
                RetrofitHelper().phoneCheck(it).enqueue(object : Callback<StatusBean> {
                    override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                        toast(R.string.network_anomaly)
                    }

                    override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                        canRegister = if (response.body()?.status!!) {
                            true
                        } else {
                            toast("该手机号已注册")
                            false
                        }
                    }
                })
            }
        }

        tv_send.onClick {
            val phone = et_new_phone.text.toString()
            if (phone.isEmpty()) {
                toast("请输入手机号")
                return@onClick
            }
            if (!CheckHelper.isCorrectPhone(phone)) {
                toast("请输入正确的手机号")
                return@onClick
            }

            if (canRegister) {
                tv_send.isEnabled = false
                mCountDownTimer.start()
                send(et_new_phone.text.toString())
            } else {
                toast("该手机号已注册")
            }
        }

        tv_register.onClick {
            val phone = et_new_phone.text.toString()
            val pwd = et_current_password.text.toString()
            val code = et_verification_code.text.toString()

            if (phone.isEmpty()) {
                toast("请输入手机号")
                return@onClick
            }
            if (!CheckHelper.isCorrectPhone(phone)) {
                toast("请输入正确的手机号")
                return@onClick
            }
            if (!canRegister) {
                toast("该手机号已注册")
                return@onClick
            }
            if (code.isEmpty()) {
                toast("请输入验证码")
                return@onClick
            }
            if (pwd.isEmpty()) {
                toast("请输入密码")
                return@onClick
            }
            verification(phone, code)
        }


    }

    private fun send(phone: String) {
        HttpRequestHelper.sendVerificationCode(phone, object : HttpCallBack() {

            override fun onSuccess(body: String) {
                val statusBean = Gson().fromJson(body, StatusBean::class.java)
                if (statusBean.status) {

                } else {
                    Tools.Toast(getString(R.string.network_anomaly))
                }
            }
        })
    }

    private fun verification(phone: String, code: String) {
        HttpRequestHelper.verificationCodeCheck(phone, code, object : HttpCallBack() {

            override fun onSuccess(body: String) {
                if (body.isEmpty()) {
                    toast(R.string.network_anomaly)
                    return
                }
                val statusBean = Gson().fromJson(body, StatusBean::class.java)
                if (statusBean.status) {
                    register(phone, et_current_password.text.toString().trim())
                } else {
                    Tools.Toast(statusBean.msg)
                }
            }
        })
    }

    private fun register(phone: String, password: String) {
        HttpRequestHelper.register(phone, password, phone, object : HttpCallBack() {

            override fun onSuccess(body: String) {
                val statusBean = Gson().fromJson(body, StatusBean::class.java)
                if (statusBean.status) {
                    mCountDownTimer.cancel()
                    tv_send.isEnabled = true
                    tv_send.text = "发送验证码"
                    toast("注册成功")
                    mLoginPresenter?.loginToTXJ(phone, password)
                } else {
                    Tools.Toast(statusBean.msg)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer.cancel()
    }
}
