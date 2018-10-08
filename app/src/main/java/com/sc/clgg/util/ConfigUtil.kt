package com.sc.clgg.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sc.clgg.application.App
import com.sc.clgg.bean.UserInfoBean
import com.sc.clgg.config.Constants

class ConfigUtil {
    private var sharedPreferences: SharedPreferences? = null

    var mobile: String?
        get() = sharedPreferences?.getString(Constants.MOBILE, "")
        set(mobile) {
            sharedPreferences?.edit { putString(Constants.MOBILE, mobile) }
        }

    var userid: String
        get() = sharedPreferences?.getString(Constants.USERID, "")!!
        set(userid) {
            sharedPreferences?.edit { putString(Constants.USERID, userid) }
        }

    var username: String?
        get() = sharedPreferences?.getString(Constants.USERNAME, "")
        set(username) {
            sharedPreferences?.edit { putString(Constants.USERNAME, username) }
        }
    /**
     * 无车承运人账号
     */
    var account: String?
        get() = sharedPreferences?.getString(Constants.ACCOUNT, "")
        set(account) {
            sharedPreferences?.edit { putString(Constants.ACCOUNT, account) }
        }

    var realName: String?
        get() = sharedPreferences?.getString(Constants.REALNAME, "")
        set(username) {
            sharedPreferences?.edit { putString(Constants.REALNAME, username) }
        }
    var nickName: String?
        get() = sharedPreferences?.getString(Constants.NICKNAME, "")
        set(nickName) {
            sharedPreferences?.edit { putString(Constants.NICKNAME, nickName) }
        }
    var password: String?
        get() = sharedPreferences?.getString(Constants.PASSWORD, "")
        set(password) {
            sharedPreferences?.edit { putString(Constants.PASSWORD, password) }
        }

    fun setUserInfo(bean: UserInfoBean?) {
        sharedPreferences?.edit {
            putString(Constants.USERID, bean?.userCode)
            putString(Constants.USERNAME, bean?.userName)
            putString(Constants.MOBILE, bean?.personalPhone)
            putString(Constants.PASSWORD, bean?.password)
            putString(Constants.ACCOUNT, bean?.userName)
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

}
