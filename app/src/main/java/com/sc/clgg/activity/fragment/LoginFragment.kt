package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sc.clgg.R
import com.sc.clgg.activity.contact.LoginContact
import com.sc.clgg.activity.login.ForgetPasswordActivity
import com.sc.clgg.activity.presenter.LoginPresenter
import com.sc.clgg.dialog.LoadingDialogHelper
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

/**
 * @author：lvke
 * @date：2018/6/5 10:25
 */
class LoginFragment : Fragment(), LoginContact {
    private var mLoginPresenter: LoginPresenter? = null
    private var mLoadingDialogHelper: LoadingDialogHelper? = null
    override fun onSuccess(body: String?) {
    }

    override fun onError(msg: String?) {
        toast(R.string.network_anomaly)
        mLoadingDialogHelper?.dismiss()
    }

    override fun onToast(msg: String?) {
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onStartLoading() {
        mLoadingDialogHelper?.show()
    }

    override fun setButtonSuccess() {
        mLoadingDialogHelper?.dismiss()
    }

    override fun jumpOtherActivity() {
        activity?.toast("登录成功")
        activity?.finish()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLoginPresenter = LoginPresenter(this)
        mLoadingDialogHelper = LoadingDialogHelper(activity)

        btn_login.setOnClickListener {
            val userName = et_account?.text.toString().trim()
            val password = et_pwd?.text.toString().trim()
            if (TextUtils.isEmpty(userName)) {
                activity?.toast("请输入手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                activity?.toast("请输入密码")
                return@setOnClickListener
            }
            mLoginPresenter?.loginToTXJ(userName, password)
        }

        tv_forget_password.setOnClickListener {
            startActivity(Intent(activity, ForgetPasswordActivity::class.java))
        }

    }

}