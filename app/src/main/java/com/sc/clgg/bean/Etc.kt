package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/10/16 17:22
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class EtcCardInfo(val balanceInt: Int? = 0,
                       val balanceString: String? = "",
                       var cardId: String? = "",
                       var cardStatus: Int? = 0,
                       var cardType: String? = "",
                       var cardVersion: String? = "",
                       var expiredDate: String? = "",
                       var plateColor: String? = "",
                       var provider: String? = "",
                       var signedDate: String? = "",
                       var userType: String? = "",
                       var vehicleModel: String? = "",
                       var vehicleNumber: String? = "") : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CircleSave(val msg: String? = "", val success: Boolean, var Mac2: String? = "",
                      var RChargeLsh: String? = "",
                      var RWriteTime: String? = "",
                      var RMac2: String? = "",
                      var RWasteSn: String? = "",
                      var cardNo: String? = "",
                      var money: String? = "",
                      var carNo: String? = "",
                      var realMoney: String? = ""
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CarNumberList(var code: Int = 0,
                         var success: Boolean = false,
                         var msg: String? = "",
                         var etcCardApplyList: ArrayList<String>?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CardInfo(var code: Int = 0,
                    var success: Boolean = false,
                    var msg: String? = "",
                    var RQcMoney: String? = "",
                    var RVLP: String? = "",
                    var RAdjust: String? = ""

) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CardList(var code: Int = 0,
                    var success: Boolean = false,
                    var msg: String? = "",
                    var etcCardApplyList: ArrayList<Card>? = ArrayList()
) : Parcelable {
    @Parcelize
    data class Card(
            //1：集团A卡；2：个人A卡；3：记名B卡；4：不记名B卡
            var cardType: String? = "",
            var vlp: String? = "",
            var cardId: String? = ""
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class WeChatOrder(var code: Int = 0,
                       var success: Boolean = false,
                       var msg: String? = "",
                       var data: Data? = null
) : Parcelable {
    @Parcelize
    data class Data(
            var nonce_str: String? = "",
            var appid: String? = "",
            var sign: String? = "",
            var trade_type: String? = "",
            var return_msg: String? = "",
            var result_code: String? = "",
            var mch_id: String? = "",
            var return_code: String? = "",
            var prepay_id: String? = ""
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class TrafficDetail(var consumeTime: String? = "",
                         var consumeAmount: String? = "",
                         var cardBalance: String? = "",
                         var trafficInterval: String? = "",
                         var entranceTrafficTime: String? = "",
                         var exportTrafficTime: String? = "",
                         var settlementProvince: String? = ""
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Card(var type: String? = "",
                var number: String? = "",
                var no: String? = ""
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CertificationInfo(
        var cardType: String? = "",
        var userType: String? = "",
        var userName: String? = "",
        var certSn: String? = "",
        var linkMobile: String? = "",
        var invitationCode: String? = "",
        var idcardImgFront: String? = "",
        var idcardImgBehind: String? = "",
        var businessLicenseImg: String? = "",
        var verificationCode: String? = "",

        var etcCardApplyVehicleVoList: ArrayList<Car>? = ArrayList(),

        var recipientsName: String? = "",
        var recipientsPhone: String? = "",
        var recipientsAddress: String? = ""
) : Parcelable {

    @Parcelize
    data class Car(
            var imageId: String? = "",
            var carNo: String? = "",
            var vinCode: String? = "",
            var carOwner: String? = "",
            var carWeight: String? = "",
            var carType: String? = "",
            var function: String? = "",
            var engineNumber: String? = "",
            var carColor: String? = "",
            var carNoColor: String? = "",
            var vehicleLicenseImg: String? = ""
    ) : Parcelable

}

@SuppressLint("ParcelCreator")
@Parcelize
data class Province(
        var name: String? = "",
        var city: ArrayList<City>? = null) : Parcelable {
    @Parcelize
    data class City(var name: String? = "",
                    var area: ArrayList<String>? = null) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class VerificationCode(
        var code: Int? = 0,
        var success: Boolean? = false,
        var checkCode: String? = "") : Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ApplyState(
        var time: String? = "",
        var carNo: String? = "",
        var etc: String? = "",
        var type: String? = "",
        var opinion: String? = "",
        var state: Int? = 0) : Parcelable