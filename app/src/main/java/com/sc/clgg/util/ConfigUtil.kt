package com.sc.clgg.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.core.content.edit
import com.sc.clgg.activity.login.LoginRegisterActivity
import com.sc.clgg.application.App
import com.sc.clgg.bean.UserInfoBean

/**
 * 保存用户信息
 */
class ConfigUtil {
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = App.getInstance().getSharedPreferences("user_info2", Context.MODE_PRIVATE)
    }

    var icon: String?
        get() = sharedPreferences?.getString("icon", "")
        set(icon) {
            sharedPreferences?.edit { putString("icon", icon) }
        }

    var mobile: String?
        get() = sharedPreferences?.getString("mobile", "")
        set(mobile) {
            sharedPreferences?.edit { putString("mobile", mobile) }
        }

    var userid: String
        get() = sharedPreferences?.getString("userid", "") ?: ""
        set(userid) {
            sharedPreferences?.edit { putString("userid", userid) }
        }

    var username: String?
        get() = sharedPreferences?.getString("username", "")
        set(username) {
            sharedPreferences?.edit { putString("username", username) }
        }
    /**
     * 无车承运人账号
     */
    var account: String?
        get() = sharedPreferences?.getString("account", "")
        set(account) {
            sharedPreferences?.edit { putString("account", account) }
        }

    var realName: String?
        get() = sharedPreferences?.getString("realName", "")
        set(username) {
            sharedPreferences?.edit { putString("realName", username) }
        }
    var nickName: String?
        get() = sharedPreferences?.getString("nickName", "")
        set(nickName) {
            sharedPreferences?.edit { putString("nickName", nickName) }
        }
    var password: String?
        get() = sharedPreferences?.getString("password", "")
        set(password) {
            sharedPreferences?.edit { putString("password", password) }
        }

    /**
     * 更新应用中存储的用户信息
     */
    fun setUserInfo(bean: UserInfoBean?) {
        sharedPreferences?.edit {
            putString("userid", bean?.userCode)
            putString("username", bean?.userName)
            putString("mobile", bean?.personalPhone)
            putString("password", bean?.password)
            putString("account", bean?.userName)
        }
    }

    /**
     * 删除应用中存储的用户信息
     */
    fun clear() {
        sharedPreferences?.edit {
            clear()
        }
    }

    /**
     * 是否已经登录
     */
    fun loggedIn(context: Context?): Boolean {
        return if (TextUtils.isEmpty(userid)) {
            context?.startActivity(Intent(context, LoginRegisterActivity::class.java))
            false
        } else {
            true
        }
    }
}
