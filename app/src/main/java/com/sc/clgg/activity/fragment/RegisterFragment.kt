package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.activity.contact.LoginContact
import com.sc.clgg.activity.presenter.LoginPresenter
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.LoadingDialogHelper
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CheckHelper
import com.sc.clgg.util.Tools
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), LoginContact {
    override fun onSuccess(body: String?) {
    }

    override fun onError(msg: String?) {
        toast(R.string.network_anomaly)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLoadingDialogHelper = LoadingDialogHelper(activity)
        mLoginPresenter = LoginPresenter(this)

        tv_send.setOnClickListener {
            val phone = et_new_phone.text.toString()
            if (phone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!CheckHelper.isCorrectPhone(phone)) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (phone.length == 11) {
                mCountDownTimer.start()

                send(et_new_phone.text.toString())
            }
        }

        tv_register.setOnClickListener {
            val phone = et_new_phone.text.toString()
            val code = et_verification_code.text.toString()
            val pwd = et_current_password.text.toString()
            val inviteCode = et_invite_code.text.toString()

            if (phone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!CheckHelper.isCorrectPhone(phone)) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (code.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (pwd.isEmpty()) {
                toast("请输入密码")
                return@setOnClickListener
            }
            register(phone, pwd, code, inviteCode)
        }
    }

    private fun send(phone: String) {
        RetrofitHelper().sendVerificationCode(phone).enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {

            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                response.body()?.run {
                    if (!status) {
                        toast("$msg")
                    }
                }
            }

        })
    }

    private fun register(account: String, password: String, code: String, inviteCode: String) {
        mLoadingDialogHelper?.show()
        RetrofitHelper().register(account, password, code, inviteCode).enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                mLoadingDialogHelper?.dismiss()
            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                mLoadingDialogHelper?.dismiss()
                response.body()?.let {
                    if (it.success) {
                        mCountDownTimer.cancel()
                        tv_send.isEnabled = true
                        tv_send.text = "发送验证码"
                        toast("注册成功")
                        mLoginPresenter?.loginToTXJ(account, password)
                    } else {
                        Tools.Toast(it.msg)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer.cancel()
    }
}
