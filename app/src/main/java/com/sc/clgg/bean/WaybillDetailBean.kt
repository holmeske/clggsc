package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/22 14:45
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class WaybillDetailBean(var message: String? = null,
                             var success: Boolean = false,
                             var wayBill: WayBillBean? = null,
                             var order: List<OrderBean>? = null,
                             var timeDeliveried: String? = null,
                             var timeProgress: String? = null,
                             var timeWait: String? = null) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class WayBillBean(var consigneeCity: String? = null,
                       var consigneeProvince: String? = null,
                       var corp: String? = null,
                       var createdAt: Long = 0,
                       var createdBy: String? = null,
                       var createdVia: String? = null,
                       var dispatchCity: String? = null,
                       var dispatchProvince: String? = null,
                       var driverId: Int = 0,
                       var driverName: String? = null,
                       var driverTel: String? = null,
                       var id: Int = 0,
                       var orderAccepterId: Int = 0,
                       var orderAmount: Int = 0,
                       var taskAmount: Int = 0,
                       var updatedAt: Long = 0,
                       var updatedBy: String? = null,
                       var vehicleNo: String? = null,
                       var waybillCost: Int = 0,
                       var waybillNo: String? = null,
                       var waybillStatus: String? = null,
                       var arrivaltime: Long = 0,
                       var dispatchtime: Long = 0) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class OrderBean(var cargoCubic: Int = 0,
                     var cargoName: String? = null,
                     var cargoQuantity: Int = 0,
                     var cargoType: String? = null,
                     var cargoWeight: Float = 0.toFloat(),
                     var consigneeAddress: String? = null,
                     var consigneeCity: String? = null,
                     var consigneeContact: String? = null,
                     var consigneeDistrict: String? = null,
                     var consigneeLatitude: Double = 0.toDouble(),
                     var consigneeLongitude: Double = 0.toDouble(),
                     var consigneeProvince: String? = null,
                     var consigneeTel: String? = null,
                     var corp: String? = null,
                     var createdAt: Long = 0,
                     var createdBy: String? = null,
                     var createdVia: String? = null,
                     var dispatchAddress: String? = null,
                     var dispatchCity: String? = null,
                     var dispatchContact: String? = null,
                     var dispatchDistrict: String? = null,
                     var dispatchLatitude: Double = 0.toDouble(),
                     var dispatchLongitude: Double = 0.toDouble(),
                     var dispatchProvince: String? = null,
                     var dispatchTel: String? = null,
                     var driverName: String? = null,
                     var driverTel: String? = null,
                     var id: Int = 0,
                     var orderAccepterId: Int = 0,
                     var orderAccepterName: String? = null,
                     var orderAccepterTel: String? = null,
                     var orderAmount: Int = 0,
                     var orderDate: Long = 0,
                     var orderNo: String? = null,
                     var orderStatus: String? = null,
                     var orderStatusTms: String? = null,
                     var ownerCode: String? = null,
                     var ownerName: String? = null,
                     var paymentType: String? = null,
                     var tplCreated: Long = 0,
                     var tplOrderNo: String? = null,
                     var updatedAt: Long = 0,
                     var updatedBy: String? = null,
                     var urgent: String? = null,
                     var vehicleNo: String? = null,
                     var wsCode: String? = null,
                     var listDetail: List<ListDetailBean>? = null) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ListDetailBean(var corp: String? = "",
                          var detailCargoCubic: Float? = 0f,
                          var detailCargoName: String? = "",
                          var detailCargoQuantity: Int? = 0,
                          var detailCargoType: String? = "",
                          var detailCargoWeight: Float? = 0f,
                          var id: Int? = 0,
                          var orderNo: String? = "") : Parcelable