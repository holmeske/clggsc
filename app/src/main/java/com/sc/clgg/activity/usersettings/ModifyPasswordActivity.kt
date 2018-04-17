package com.sc.clgg.activity.usersettings

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.basic.LoginActivity
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.dialog.LoadingDialogHelper
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.Tools
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_modify_password.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ModifyPasswordActivity : AppCompatActivity() {
    private var mLoadingDialogHelper: LoadingDialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_modify_password)
        title = getString(R.string.modify_password)
        super.onCreate(savedInstanceState)

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

        iv_delete_old_password.onClick { et_old_password!!.setText("") }
        iv_delete_new_password.onClick { et_new_password!!.setText("") }
        iv_delete_again_new_password.onClick { et_again_new_password!!.setText("") }

        confirm_btn.onClick {
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

        HttpRequestHelper.modifyPassword(ConfigUtil().username, oldPassword, newPassword, object : HttpCallBack() {
            override fun onStart() {
                super.onStart()
                mLoadingDialogHelper!!.show()
            }

            override fun onFinish() {
                super.onFinish()
                super.onStart()
                mLoadingDialogHelper!!.dismiss()
            }

            override fun onSuccess(body: String) {
                val statusBean = Gson().fromJson(body, StatusBean::class.java)
                if (statusBean.status) {
                    toast("密码修改成功")
                    startActivity(Intent(this@ModifyPasswordActivity, LoginActivity::class.java))
                    finish()
                } else {
                    toast(statusBean.msg!!)
                }
            }
        })
    }
}
