package com.sc.clgg.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.core.content.edit
import com.sc.clgg.activity.LoginRegisterActivity
import com.sc.clgg.application.App
import com.sc.clgg.bean.UserInfoBean

class ConfigUtil {

    private val USERID = "userid"
    private val MOBILE = "mobile"

    private val REALNAME = "realName"
    private val USERNAME = "username"
    private val NICKNAME = "nickName"

    private val PASSWORD = "password"
    private val ACCOUNT = "account"

    private var sharedPreferences: SharedPreferences? = null

    var mobile: String?
        get() = sharedPreferences?.getString(MOBILE, "")
        set(mobile) {
            sharedPreferences?.edit { putString(MOBILE, mobile) }
        }

    var userid: String
        get() = sharedPreferences?.getString(USERID, "")!!
        set(userid) {
            sharedPreferences?.edit { putString(USERID, userid) }
        }

    var username: String?
        get() = sharedPreferences?.getString(USERNAME, "")
        set(username) {
            sharedPreferences?.edit { putString(USERNAME, username) }
        }
    /**
     * 无车承运人账号
     */
    var account: String?
        get() = sharedPreferences?.getString(ACCOUNT, "")
        set(account) {
            sharedPreferences?.edit { putString(ACCOUNT, account) }
        }

    var realName: String?
        get() = sharedPreferences?.getString(REALNAME, "")
        set(username) {
            sharedPreferences?.edit { putString(REALNAME, username) }
        }
    var nickName: String?
        get() = sharedPreferences?.getString(NICKNAME, "")
        set(nickName) {
            sharedPreferences?.edit { putString(NICKNAME, nickName) }
        }
    var password: String?
        get() = sharedPreferences?.getString(PASSWORD, "")
        set(password) {
            sharedPreferences?.edit { putString(PASSWORD, password) }
        }

    fun setUserInfo(bean: UserInfoBean?) {
        sharedPreferences?.edit {
            putString(USERID, bean?.userCode)
            putString(USERNAME, bean?.userName)
            putString(MOBILE, bean?.personalPhone)
            putString(PASSWORD, bean?.password)
            putString(ACCOUNT, bean?.userName)
        }
    }

    fun clear() {
        sharedPreferences?.edit {
            clear()
        }
    }

    init {
        sharedPreferences = App.instance?.getSharedPreferences("user_info2", Context.MODE_PRIVATE)
    }

    fun isLogined(context: Context): Boolean {
        return if (TextUtils.isEmpty(userid)) {
            context.startActivity(Intent(context, LoginRegisterActivity::class.java))
            false
        } else {
            true
        }
    }
}
