package com.sc.clgg.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sc.clgg.application.App
import com.sc.clgg.bean.UserInfoBean
import com.sc.clgg.config.Constants

class ConfigUtil {
    private var sharedPreferences: SharedPreferences? = null

    val userType: String
        get() = sharedPreferences!!.getString(Constants.USERTYPE, "")

    var email: String
        get() = sharedPreferences!!.getString(Constants.EMALI, "")
        set(email) {
            sharedPreferences?.edit { putString(Constants.EMALI, email) }
        }

    var gender: String
        get() = sharedPreferences!!.getString(Constants.GENDER, "")
        set(gender) {
            sharedPreferences?.edit { putString(Constants.GENDER, gender) }
        }

    var mobile: String
        get() = sharedPreferences!!.getString(Constants.MOBILE, "")
        set(mobile) {
            sharedPreferences?.edit { putString(Constants.MOBILE, mobile) }
        }

    private var password: String
        get() = sharedPreferences!!.getString(Constants.PASSWORD, "")
        set(password) {
            sharedPreferences?.edit { putString(Constants.PASSWORD, password) }
        }

    var userid: String
        get() = sharedPreferences!!.getString(Constants.USERID, "")
        set(userid) {
            sharedPreferences?.edit { putString(Constants.USERID, userid) }
        }

    var username: String
        get() = sharedPreferences!!.getString(Constants.USERNAME, "")
        set(username) {
            sharedPreferences?.edit { putString(Constants.USERNAME, username) }
        }

    fun setUserInfo(bean: UserInfoBean?) {
        sharedPreferences?.edit {
            putString(Constants.USERID, bean?.userCode)
            putString(Constants.USERNAME, bean?.userName)
            putString(Constants.MOBILE, bean?.personalPhone)
            putString(Constants.USERTYPE, bean?.userType)
        }
    }

    fun reset() {
        userid = ""
        username = ""
        mobile = ""
        password = ""
    }

    init {
        sharedPreferences = App.getInstance().getSharedPreferences("user_info2", Context.MODE_PRIVATE)
    }

}
