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
data class BusinessNoteList(var code: Int = 0,
                            val success: Boolean,
                            val msg: String? = "",
                            val etcBusinessNoteList: ArrayList<Note>? = ArrayList()) : Parcelable {
    @Parcelize
    data class Note(
            var id: Int? = 0,
            val title: String? = "",
            var text: String? = "",
            var createTime: String? = "",
            var rank: Int? = 0
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class RechargeOrderList(var code: Int = 0,
                             val success: Boolean,
                             val msg: String? = "",
                             val payOrdersVoList: ArrayList<Order>? = ArrayList()) : Parcelable {
    @Parcelize
    data class Order(
            val wasteSn: String? = "",
            var cardNo: String? = "",
            var payMoney: Int? = 0,
            var payTime: String? = "",
            var payFlag: String? = "",
            var payType: String? = "",
            var createTime: String? = "",
            var isLoad: String? = ""
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class CircleSave(val msg: String? = "", val success: Boolean = false,
        //圈存申请
                      var Mac2: String? = "",
                      var RChargeLsh: String? = "",
                      var RWriteTime: String? = "",
                      var RWasteSn: String? = "",
        //圈存确认
                      var cardNo: String? = "",
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
                    var RQcMoney: String? = "0",
                    var RVLP: String? = "",
                    var RAdjust: String? = "0"

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
//            var cardType: String? = "3",
//            var vlp: String? = "",
//            var cardId: String? = "",

            var id: Int? = 0,
            var carNo: String? = "",
            var cardNo: String? = ""
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
data class CertificationInfo(
        /**
         * 用户证件类型:
         * 0：无（内部机构）；
         * 1：工商营业执照号码（企业）
         * 2：身份证（个人）；
         * 3：组织机构代码证书（事业单位、政府机关、社会团体）；
         * 4: 军队代号（军队）
         */
        var certType: String? = "",
        /**
         * 用户证件号
         */
        var certSn: String? = "",

        var cardType: String? = "",
        /**
         * 1个人  2企业
         */
        var userType: String? = "2",
        var userName: String? = "",

        var linkMobile: String? = "",
        var invitationCode: String? = "",
        var idcardImgFront: String? = "",
        var idcardImgBehind: String? = "",
        var agentIdcardImgFront: String? = "",
        var agentIdcardImgBehind: String? = "",
        var businessLicenseImg: String? = "",

        var etcCardApplyVehicleVoList: ArrayList<Car>? = ArrayList(),

        var recipientsName: String? = "",
        var recipientsPhone: String? = "",
        var recipientsAddress: String? = "",

        var agentName: String? = "",
        var agentPhone: String? = "",
        var agentCertSn: String? = ""

) : Parcelable {

    @Parcelize
    data class Car(
            //车牌图片id，用来绑定图片和车辆信息的
            var carNoImageId: String? = "",
            //行驶证图片id，用来绑定图片和车辆信息的
            var vehicleImageId: String? = "",
            var vehicleFrontImg: String? = "",
            var vehicleLicenseImg: String? = "",

            var carNoColor: String? = "",
            var carColor: String? = "",
            var carNo: String? = "",
            //行驶证车辆类型
            var carOwner: String? = "",
            var address: String? = "",
            var carType: String? = "",
            //行驶证车辆类型
            var vehicleType: String? = "",
            var model: String? = "",
            var function: String? = "",
            var vinCode: String? = "",
            var engineNumber: String? = "",
            var tyreNumber: String? = "",
            var axleNumber: String? = ""
    ) : Parcelable

}

@SuppressLint("ParcelCreator")
@Parcelize
data class ApplyStateList(var code: Int = 0,
                          var success: Boolean = false,
                          var msg: String? = "",
                          var etcCardApplyOpenList: ArrayList<ApplyState>? = null
) : Parcelable {
    @Parcelize
    data class ApplyState(
            var createTime: String? = "",
            var carNo: String? = "",
            var cardId: String? = "",
            var cardType: String? = "",
            var remarkFalse: String? = "",

            var expressInfo: String? = "",
            //0审核通过  1开卡成功   2开卡失败
            var isSuccess: String? = "",
            //0审核中  2审核驳回
            var checkStatus: String? = "") : Parcelable
}
