package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:24
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class LocationBean(var longitude: Double = 0.0,// 经度
                        var latitude: Double = 0.0,// 纬度
                        var time: Long = 0,// 时间
                        var location: String? = "",// 位置
                        var province: String? = "",// 省
                        var city: String? = "",// 市
                        var district: String? = "",// 区
                        var flag: Boolean? = false,// 跳转的标志
                        var position: Int = 0,// 店铺位置
                        var obj: String? = "",// 对象数据
                        var list: String? = ""// 集合数据
) : Parcelable

