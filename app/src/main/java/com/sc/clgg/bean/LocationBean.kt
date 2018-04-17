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
data class LocationBean(var longitude: Double? = null,// 经度
                        var latitude: Double? = null,// 纬度
                        var time: Long? = null,// 时间
                        var location: String? = null,// 位置
                        var province: String? = null,// 省
                        var city: String? = null,// 市
                        var district: String? = null,// 区
                        var flag: Boolean? = null,// 跳转的标志
                        var position: Int = 0,// 店铺位置
                        var obj: String? = null,// 对象数据
                        var list: String? = null// 集合数据
) : Parcelable

