package com.sc.clgg.activity.my.set

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sc.clgg.R
import com.sc.clgg.activity.login.LoginRegisterActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.LoadingDialogHelper
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.Tools
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_modify_pass.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyPasswordActivity : BaseImmersionActivity() {
    private var mLoadingDialogHelper: LoadingDialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pass)
        initTitle("修改密码")

        mLoadingDialogHelper = LoadingDialogHelper(this)
        initListener()
    }

    private fun initListener() {
        et_old_password!!.setTextChangeListener {
            iv_delete_old_password!!.visibility = if (it.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }

        et_new_password.setTextChangeListener {
            iv_delete_new_password.visibility = if (it.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }

        et_again_new_password.setTextChangeListener {
            iv_delete_again_new_password!!.visibility = if (it.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        }

        iv_delete_old_password.setOnClickListener { et_old_password!!.setText("") }
        iv_delete_new_password.setOnClickListener { et_new_password!!.setText("") }
        iv_delete_again_new_password.setOnClickListener { et_again_new_password!!.setText("") }

        confirm_btn.setOnClickListener {
            determine()
        }
    }

    private fun determine() {
        val oldPassword = et_old_password!!.text.toString()
        val newPassword = et_new_password!!.text.toString()
        val againNewPassword = et_again_new_password!!.text.toString()

        if (oldPassword.isEmpty()) {
            Tools.Toast("原始密码不能为空")
            return
        }
        if (newPassword == oldPassword) {
            Tools.Toast("新旧密码不可相同")
            return
        }
        if (newPassword.isEmpty()) {
            Tools.Toast("新密码不能为空")
            return
        }
        if (newPassword.length < 6) {
            Tools.Toast("密码长度应在6-16位，当前" + newPassword.length + "位！")
            return
        }
        if (againNewPassword.isEmpty()) {
            Tools.Toast("第二次输入新密码不能为空")
            return
        }
        if (againNewPassword != newPassword) {
            Tools.Toast("两次密码不一致")
            return
        }

        http = RetrofitHelper().modifyPassword(ConfigUtil().username, oldPassword, newPassword).apply {
            mLoadingDialogHelper!!.show()
            enqueue(object : Callback<StatusBean> {
                override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                    mLoadingDialogHelper!!.dismiss()
                }

                override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                    mLoadingDialogHelper!!.dismiss()

                    if (response.body()?.status!!) {
                        toast("密码修改成功")
                        startActivity(Intent(this@ModifyPasswordActivity, LoginRegisterActivity::class.java))
                        finish()
                    } else {
                        toast(response.body()?.msg!!)
                    }
                }
            })
        }
    }

    private var http: Call<StatusBean>? = null
    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }
}
