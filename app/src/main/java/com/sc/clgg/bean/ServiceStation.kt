package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/9 16:14
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class ServiceStation(var code: Double? = 0.toDouble(), var page: Page? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class Page(
            var total: Int = 0,
            var pageSize: Int = 0,
            var totalPage: Int = 0,
            var currPage: Int = 0,
            var rows: List<Station>) : Parcelable {

        @Parcelize
        data class Station(var id: Int = 0,
                           var stationName: String? = "",
                           var stationDesc: String? = "",
                           var address: String? = "",
                           var contact: String? = "",
                           var mobile: String? = "",
                           var tel: String? = "",
                           var lon: String? = "",
                           var lat: String? = "",
                           var stationType: Int? = 0,
                           var distance: Double? = null) : Parcelable
    }
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Area(var code: Int, var data: List<Province>? = null) : Parcelable {
    @Parcelize
    data class Province(var id: Int, var name: String? = "") : Parcelable
}


