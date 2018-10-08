package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/22 16:21
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class DrivingScoreBean(var max: Double = 0.toDouble(),
                            var success: Boolean = false,
                            var list: List<ListBean>? = null) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ListBean(var carno: String? = null,
                    var driverName: String? = null,
                    var mileage: Double = 0.toDouble(),
                    var score: Double = 0.toDouble()) : Parcelable
