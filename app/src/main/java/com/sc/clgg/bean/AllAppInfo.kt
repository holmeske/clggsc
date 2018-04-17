package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/9 15:41
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class AllAppInfo(var versionCode: Int = 0,
                      var appname: String = "",
                      var packagename: String = "",
                      var lastInstal: Long = 0,
                      var InstalPath: String = "") : Parcelable