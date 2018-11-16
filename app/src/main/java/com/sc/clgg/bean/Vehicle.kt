package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:48
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Vehicle(var code: Int = 0,
                   var success: Boolean = false,
                   var vehicleInfoList: List<Bean>? = null) : Parcelable {
    @Parcelize
    data class Bean(var vinNumber: String? = "",
                    var carNumber: String? = "",
                    var isChecked: Boolean = false) : Parcelable
}

