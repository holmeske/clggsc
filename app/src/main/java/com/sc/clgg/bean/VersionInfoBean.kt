package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/23 17:30
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class VersionInfoBean(var single: SingleBean? = null,
                           var success: Boolean = false) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SingleBean(var code: String? = null,
                      var id: Int = 0,
                      var service: Int = 0,
                      var source: Int = 0,
                      var time: Long = 0,
                      var type: Int = 0,
                      var url: String? = null,
                      var username: String? = null) : Parcelable

