package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:59
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Service(var name: String? = null, var id: Int = 0) : Parcelable

