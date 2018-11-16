package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:26
 */
@SuppressLint("ParcelCreator")
data class UserInfoBean(var id: String? = "",
                        var userCode: String? = "",
                        var userName: String? = "",
                        var password: String? = "",
                        var account: String? = "",
                        var passwordSalt: String? = "",
                        var gender: String? = "",
                        var email: String? = "",
                        var nickName: String? = "",
                        var identityCard: String? = "",
                        var personalPhone: String? = "",
                        var homePhone: String? = "",
                        var createdBy: String? = "",
                        var contractAddress: String? = "",
                        var userType: String? = "",
                        var updatedTime: String? = "") : BaseBean()

@SuppressLint("ParcelCreator")
@Parcelize
data class User(var success: Boolean = false,
                var msg: String? = "",
                var code: String? = "",
                var password: String? = "",
                var isLogin: Boolean = false,
                var phone: String? = "",
                var userName: String? = "",
                var userCode: String? = "") : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PersonalData(var code: Int = 0, var data: Data, var success: Boolean? = false) : Parcelable {
    @Parcelize
    data class Data(var id: String? = "",
                    var userCode: String? = "",
                    var nickName: String? = "",
                    var userName: String? = "",
                    var realName: String? = "",
                    var headImg: String? = "",
                    var clientSign: String? = "",
                    var inviteCode: String? = "",
                    var gender: String? = "") : Parcelable
}




