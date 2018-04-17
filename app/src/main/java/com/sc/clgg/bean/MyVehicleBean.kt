package com.sc.clgg.bean

import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:48
 */
@SuppressLint("ParcelCreator")
data class MyVehicleBean(var list: List<VehicleLocationBean>? = null) : BaseBean()

