package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:08
 */


@SuppressLint("ParcelCreator")
@Parcelize
data class TallyBook(var success: Boolean,
                     var msg: String? = "",
                     var allInfo: List<Info>?,
                     var allExpense: String? = "0.00",
                     var allIncome: String? = "0.00") : Parcelable {

    @Parcelize
    data class Info(var id: Int = 0,
                    var recordType: Int = 0,
                    var costType: Int = 0,
                    var amount: Double = 0.toDouble(),
                    var remark: String? = "",
                    var costDate: String? = "",
                    var createTime: String? = "",
                    var createUser: Int = 0,
                    var timeStamp: Long = 0,
                    var show: Boolean = false) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Check(var code: Int = 0, var url: String? = "", var msg: String? = "", var success: Boolean = false) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class NoReadInfo(var code: Int = 0, var allNotReadInfo: List<Info>? = null, var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Info(var id: String? = "",
                    var nickName: String? = "",
                    var headImg: String? = "",
                    var laudUserId: String? = "",
                    var message: String? = "",
                    var remark: String? = "",
                    var createTime: String? = "",
                    var imagesList: List<Image>? = null) : Parcelable

    @Parcelize
    data class Image(var imgUrl: String? = "") : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class IsNotReadInfo(var code: Int = 0,
                         var DriverCircleType: Boolean = false,
                         var Activities: Boolean = false,
                         var News: Boolean = false,
                         var success: Boolean = false) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Fault(var msg: String? = "", var code: Int = 0, var data: List<Data>? = null, var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Data(
            var reason: String? = "",
            var aboutpart: String? = "",
            var problem: String? = "",
            var solution: String? = "",
            var vin: String? = "",
            var ecuType: String? = "",
            var faultCode: String? = "",
            var faultType: String? = "",
            var fmi: String? = "",
            var id: String? = "",
            var saveTime: String? = "",
            var desc: String? = "") : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class FaultDetail(var carno: String? = "", var data: List<Fault.Data>? = null) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Banner(var code: Int = 0, var data: List<Bean>? = null, var success: Boolean = false) : Parcelable {
    @Parcelize
    data class Bean(var img: String? = "", var name: String? = "", var url: String? = "") : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Record(var pressImg: Int = 0, var nomalImg: Int = 0, var isChecked: Boolean = false, var name: String? = "", var typeId: Int = 0) : Parcelable