package com.sc.clgg.bean

import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:26
 */
@SuppressLint("ParcelCreator")
data class UserInfoBean(var id: String? = null,
                        var userCode: String? = null,
                        var userName: String? = null,
                        var password: String? = null,
                        var passwordSalt: String? = null,
                        var gender: String? = null,
                        var email: String? = null,
                        var nickName: String? = null,
                        var identityCard: String? = null,
                        var personalPhone: String? = null,
                        var homePhone: String? = null,
                        var createdBy: String? = null,
                        var contractAddress: String? = null,
                        var userType: String? = null,
                        var updatedTime: String? = null) : BaseBean()

