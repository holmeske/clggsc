package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 10:52
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class VehicleLocation(var leaveList: List<VehicleLocationBean>? = null,
                           var nullList: List<VehicleLocationBean>? = null,
                           var runList: List<VehicleLocationBean>? = null,
                           var stopList: List<VehicleLocationBean>? = null,
                           var warnList: List<VehicleLocationBean>? = null,
                           var success: Boolean = false) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class VehicleLocationBean(var dayMileage: Double? = 0.toDouble(),
                               var oilcost: Double? = 0.toDouble(),
                               var oilcostphkm: Double = 0.toDouble(),
                               var carno: String? = null,
                               var direction: String? = null,
                               var driverName: String? = null,
                               var latitude: String? = null,
                               var longitude: String? = null,
                               var oilLevel: String? = null,
                               var speed: String? = null,
                               var status: String? = null,
                               var terminalid: String? = null,
                               var time: String? = null,
                               var totalMileage: String? = null,
                               var vincode: String? = null,
                               var delFlag: String? = null/*1：可删除，0：不可删除*/) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class VehicleLocationDetailBean(var data: DataBean? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class DataBean(var carno: String? = null,
                        var direction: String? = null,
                        var latitude: String? = null,
                        var longitude: String? = null,
                        var oilLevel: String? = null,
                        var speed: Double = 0.toDouble(),
                        var status: Int = 0,
                        var terminalid: String? = null,
                        var time: String? = null,
                        var totalMileage: String? = null,
                        var vincode: String? = null) : Parcelable

}


        