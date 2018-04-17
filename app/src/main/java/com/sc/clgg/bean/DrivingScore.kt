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

@SuppressLint("ParcelCreator")
data class DrivingScoreDetailBean(var list: List<Behavior>? = null) : BaseBean()

@SuppressLint("ParcelCreator")
data class Behavior(var behavior: Int = 0,
                    var carno: String? = null,
                    var id: String? = null,
                    var lasting: Int = 0,
                    var latitude: Double? = null,
                    var longitude: Double? = null,
                    var occurtime: Long = 0,
                    var orgname: String? = null,
                    var terminalid: String? = null,
                    var vincode: String? = null) : BaseBean()