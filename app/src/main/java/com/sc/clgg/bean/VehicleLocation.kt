package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author：lvke
 * @date：2018/2/24 10:52
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Location(var msg: String? = "", var code: Double? = 0.toDouble(), var data: ArrayList<Data>? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class Data(
            var carno: String? = "",
            var vin: String? = "",
            var direction: String? = "",
            var longitude: String? = "",
            var latitude: String? = "",
            var status: String? = "",
            var speed: String? = "",
            var time: String? = "") : Parcelable
}


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
data class LocationDetail(var code: Double? = 0.toDouble(), var data: Data? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class Data(
            var address: String? = "",
            var longitude: String? = "",
            var latitude: String? = "",
            var time: String? = "",
            var speed: String? = "",
            var direction: String? = "",
            var status: String? = ""
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class PathRecord(var msg: String? = "", var code: Double? = 0.toDouble(), var data: List<Path>? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class Path(
            var lng: String? = "",
            var lat: String? = "",
            var gpsTime: String? = "",
            var totalMileage: String? = "",
            var speed: String? = "",
            var dir: String? = "") : Parcelable {
        override fun equals(other: Any?): Boolean {
            if (other is Path) {
                var o: Path = other
                return o.lat == this.lat && o.lng == this.lng
            }
            return super.equals(other)
        }
    }

}

@SuppressLint("ParcelCreator")
@Parcelize
data class Mileage(var code: Double? = 0.toDouble(),
                   var totalDays: String? = "",
                   var singleTotal: String? = "",
                   var totalMileages: String? = "",
                   var chartList: List<B>? = null,
                   var details: List<A>? = null,
                   var success: Boolean = false) : Parcelable {
    @Parcelize
    data class B(
            var date: String? = "",
            var dayMileages: String? = "") : Parcelable

    @Parcelize
    data class A(
            var carNo: String? = "",
            var vin: String? = "",
            var intervalMileage: String? = "") : Parcelable
}

@Parcelize
data class Consumption(var code: Int = 0,
                       var data: Data? = null,
                       var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Data(
            var dataList: List<A>? = null,
            var totalFuel: Double = 0.0,
            var dayDetails: List<B>? = null,
            var dayMileages: String? = "",
            var totalMileage: Double = 0.0,
            var hundredFuel: Double = 0.0) : Parcelable {

        @Parcelize
        data class A(
                var carNo: String? = "",
                var vin: String? = "",
                var fuelType: Double = 0.0,
                var totalMileage: String? = "",
                var totalFuel: String? = "",
                var hundredFuel: String? = "") : Parcelable

        @Parcelize
        data class B(
                var totalMileage: String? = "",
                var totalFuel: String? = "",
                var hundredFuel: String? = "",
                var clctDate: String? = "",
                var clctTime: Long = 0) : Parcelable
    }

}

@Parcelize
data class ConsumptionDetail(var code: Int = 0,
                             var data: List<Data>? = null,
                             var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Data(
            var dayDetails: List<B>? = null,
            var oilGassEntity: A? = null) : Parcelable {

        @Parcelize
        data class A(
                var carNo: String? = "",
                var vin: String? = "",
                var fuelType: Double = 0.0,
                var totalMileage: String? = "",
                var totalFuel: String? = "",
                var hundredFuel: String? = "") : Parcelable

        @Parcelize
        data class B(
                var totalMileage: String? = "",
                var totalFuel: String? = "",
                var hundredFuel: String? = "",
                var clctDate: String? = "") : Parcelable
    }

}

@SuppressLint("ParcelCreator")
@Parcelize
data class MileageDetail(var code: Double? = 0.toDouble(),
                         var data: Data? = null,
                         var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Data(
            var carNo: String? = "",
            var vin: String? = "",
            var dailyMileage: String? = "",
            var detailDataList: List<Detail>?,

            var intervalMileage: String? = "") : Parcelable {

        @Parcelize
        data class Detail(var date: String? = "", var dayMileage: String? = "") : Parcelable

    }


}




        