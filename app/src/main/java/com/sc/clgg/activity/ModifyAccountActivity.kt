package com.sc.clgg.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.dialog.LoadingDialogHelper
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.CheckHelper
import kotlinx.android.synthetic.main.activity_modify_account.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyAccountActivity : BaseImmersionActivity() {
    private var mLoadingDialogHelper: LoadingDialogHelper? = null
    private val mCountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_send.text = (millisUntilFinished / 1000).toString() + "s"
        }

        override fun onFinish() {
            tv_send.text = "发送验证码"
            tv_send.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)

        initTitle("修改账号")

        mLoadingDialogHelper = LoadingDialogHelper(this)

        tv_send.setOnClickListener {
            tv_send.isEnabled = false
            call_verificationCode = RetrofitHelper().verificationCode(et_new_phone.text.toString())
            mLoadingDialogHelper?.show()
            call_verificationCode?.enqueue(object : Callback<Check> {
                override fun onFailure(call: Call<Check>?, t: Throwable?) {
                    mLoadingDialogHelper?.dismiss()
                }

                override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                    mLoadingDialogHelper?.dismiss()
                    response?.body().let {

                        if (it!!.success) {
                            mCountDownTimer.start()
                        }
                    }
                }
            })
        }

        tv_commit.setOnClickListener {
            if (et_current_password.text.isEmpty()) {
                toast("请输入密码")
                return@setOnClickListener
            }
            if (et_new_phone.text.isEmpty()) {
                toast("请输入新手机号")
                return@setOnClickListener
            }
            if (!CheckHelper.isCorrectPhone(et_new_phone.text.toString())) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (et_verification_code.text.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }

            call_commit = RetrofitHelper().modifyAccount(
                    et_new_phone.text.toString(),
                    et_verification_code.text.toString(),
                    et_current_password.text.toString())
            mLoadingDialogHelper?.show()
            call_commit?.enqueue(object : Callback<Check> {
                override fun onFailure(call: Call<Check>?, t: Throwable?) {
                    mLoadingDialogHelper?.dismiss()
                }

                override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                    mLoadingDialogHelper?.dismiss()
                    response?.body().let {
                        if (it!!.success) {
                            toast("修改成功")
                            PersonalDataActivity.DATA?.userName = et_new_phone?.text.toString()
                            finish()
                        }
                    }
                }
            })

        }
    }

    private var call_verificationCode: Call<Check>? = null

    private var call_commit: Call<Check>? = null

    override fun onDestroy() {
        super.onDestroy()
        call_verificationCode?.cancel()
        mCountDownTimer.cancel()
    }
}
