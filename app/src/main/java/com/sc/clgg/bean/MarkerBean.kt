package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:26
 */

//data class MarkerBean(var longitude: Double = 0.toDouble(),// 经度
//                      var latitude: Double = 0.toDouble(),// 纬度
//                      var anchor_x: Float = 0.toFloat(),// x坐标
//                      var anchor_y: Float = 0.toFloat(),// y坐标
//                      var title: String? = "",// 标题
//                      var snippet: String? = null,// 位置
//                      var draggable: Boolean? = null,// 是否拖拽
//                      var icon: Int = 0,// 图标
//                      var position: Int = 0,
//                      var id: String? = null
//) : Parcelable
@SuppressLint("ParcelCreator")
@Parcelize
data class MarkerBean(var longitude: Double = 0.toDouble(),// 经度
                      var latitude: Double = 0.toDouble(),// 纬度
                      var title: String? = "",// 标题
                      var icon: Int = 0 // 图标
) : Parcelable



