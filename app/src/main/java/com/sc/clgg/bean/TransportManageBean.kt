package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:22
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TransportManageBean(var success: Boolean = false,
                               var waybillCountNew: Int = 0,
                               var waybillCountProgress: Int = 0,
                               var waybillCountReliveried: Int = 0,
                               var waybillCountWaiting: Int = 0,
                               var waybillList: List<WaybillListBean>? = null) : Parcelable {

    @Parcelize
    data class WaybillListBean(var consigneeCity: String? = null,
                               var consigneeContact: String? = null,
                               var consigneeDistrict: String? = null,
                               var consigneeProvince: String? = null,
                               var createdAt: Long = 0,
                               var dispatchCity: String? = null,
                               var dispatchContact: String? = null,
                               var dispatchDistrict: String? = null,
                               var dispatchProvince: String? = null,
                               var driverId: Int = 0,
                               var driverName: String? = null,
                               var driverTel: String? = null,
                               var orderAmount: Double = 0.toDouble(),
                               var taskAmount: Double = 0.toDouble(),
                               var updatedAt: Long = 0,
                               var vehicleNo: String? = null,
                               var waybillNo: String? = null,
                               var waybillStatus: String? = null) : Parcelable

}
