package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/9 11:12
 */
@SuppressLint("ParcelCreator")
@Parcelize
open class BaseBean(var success: Boolean = false,
                    var code: String? = "",
                    var msg: Int = 0) : Parcelable