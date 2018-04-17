package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author：lvke
 * @date：2018/2/24 14:15
 */
@SuppressLint("ParcelCreator")
data class IorderListBean(var waybillCountNew: Int = 0,
                          var waybillCountProgress: Int = 0,
                          var waybillCountReliveried: Int = 0,
                          var waybillCountWaiting: Int = 0,
                          var waybill: ArrayList<ItemOrderBean>? = null,
                          var waybillList: ArrayList<ItemOrderBean>? = null) : BaseBean()

@SuppressLint("ParcelCreator")
@Parcelize
data class ItemOrderBean(var arrivaltime: Long = 0,
                         var consigneeCity: String? = null,
                         var consigneeProvince: String? = null,
                         var corp: String? = null,
                         var createdAt: Long = 0,
                         var createdBy: String? = null,
                         var createdVia: String? = null,
                         var dispatchCity: String? = null,
                         var dispatchProvince: String? = null,
                         var dispatchtime: Long = 0,
                         var driverId: String? = null,
                         var driverName: String? = null,
                         var id: Int = 0,
                         var orderAccepterId: Int = 0,
                         var orderAmount: Double = 0.toDouble(),
                         var taskAmount: Long = 0,
                         var vehicleNo: String? = null,
                         var waybillCost: String? = null,
                         var waybillNo: String? = null,
                         var waybillStatus: String? = null,
                         var isChecked: Boolean = false) : Parcelable