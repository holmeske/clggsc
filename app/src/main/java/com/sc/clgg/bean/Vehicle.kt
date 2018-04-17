package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:41
 */

@SuppressLint("ParcelCreator")
data class VehicleDetailBean(val driverName: String? = "", val mobile: String, val listMileage: List<NearMileBean>) : BaseBean()

@SuppressLint("ParcelCreator")
@Parcelize
data class NearMileBean(val date: String, val mileage: String) : Parcelable

@SuppressLint("ParcelCreator")
data class MonRptBn(var totalOilCost: String? = null, //总油耗（即本月油耗）
                    var averageOilCostPhKm: String? = null, //平均油耗
                    var averageIdlePercentage: String? = null,  //平均怠速比
                    var totalMileage: Double? = null,  //总里程
                    var totalRunningTime: String? = null, //总运行时间（小时）
                    var list: List<Report>? = null
) : BaseBean()

@SuppressLint("ParcelCreator")
data class Report(var endWeekDate: String? = null,
                  var startWeekDate: String? = null,
                  var idlepercentage: Double = 0.toDouble(),
                  var num: Int = 0,
                  var oilcost: Double = 0.toDouble(),
                  var oilcostphkm: Double = 0.toDouble(),
                  var runningTime: Double = 0.toDouble(),
                  var workingmileage: Double = 0.toDouble()) : BaseBean()

@SuppressLint("ParcelCreator")
@Parcelize
data class PathwayBean(val gpsList: List<GPS>) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class GPS(var latitude: Double, var longitude: Double) : Parcelable

 