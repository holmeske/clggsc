package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.sc.clgg.util.ConfigUtil
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
        var agentCertSn: String? = "",

        //银行卡卡号
        var bankCardNo: String? = "",
        //银行卡类型1为借记，其他为信用卡
        var bankCardNoType: String? = "1",
        //银行卡开户网点
        var bankCardNoCreatBrno: String? = "",
        //银行方单位编号
        var companyAGENTSERIALNO: String? = ""

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
data class CertificationInfoBean(
        var userCode: String? = ConfigUtil().userid,

        var realName: String? = ConfigUtil().realName,

        //用户类型
        var companyflag: String? = "1",
        //证件类型
        var certifiType: String? = "",
        //证件号码
        var companyLic: String? = "",

        //经办人姓名
        var relationName: String? = "",
        //经办人联系电话
        var phoneNo: String? = "",
        //经办人所在部门
        var department: String? = "",
        //经办人单位名称
        var accountName: String? = "",
        //经办人证件类型
        var agentIdType: String? = "",
        //经办人证件号码
        var agentIdNum: String? = "",

        //银行卡卡号
        var bankCardNo: String? = "",
        //银行卡类型1为借记，其他为信用卡
        var bankCardNoType: String? = "1",
        //银行卡开户网点
        var bankCardNoCreatBrno: String? = "",
        //银行方单位编号
        var companyAgentSerialNo: String? = "",


        //收货人姓名
        var contact: String? = "",
        //收货人姓名
        var ownerTel: String? = "",
        //收货人姓名
        var address: String? = "",


        //持卡人姓名
        var name: String? = "",
        //持卡人联系电话
        var ecardNoPhoneNo: String? = "",
        //持卡人出生日期
        var birthday: String? = "",
        //持卡人性别
        var sex: String? = "",
        //持卡人证件类型
        var ecardNoCertifiType: String? = "",
        //持卡人证件号码
        var certifiNo: String? = "",

        //所有人证件类型
        var ownerIdType: String? = "",
        //所有人证件号码
        var ownerIdNum: String? = "",


        //车辆颜色
        var vehicleColor: String? = "",
        //车牌号码
        var vehiclePlate: String? = "",
        //所有人名称
        var ownerName: String? = "",
        //车型
        var vehicleType: String? = "",
        //是否为客车
        var vehicleFlag: String? = "",

        //行驶证车辆类型
        var certifiVehType: String? = "",
        //行驶证品牌型号
        var model: String? = "",
        //车辆使用性质
        var useCharacter: String? = "",
        //车辆识别代码
        var vehiclelicNo: String? = "",
        //车辆发动机号
        var vehicleEngineNo: String? = ""

) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PickerBean(var id: String? = "",
                      var name: String? = ""
) : Parcelable


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

@SuppressLint("ParcelCreator")
@Parcelize
data class ApplyStateListBean(var total: Int = 0,
                              var pageSize: Int = 0,
                              var totalPage: Int = 0, var currPage: Int = 0,
                              var rows: ArrayList<A>? = null
) : Parcelable {
    @Parcelize
    data class A(
            var vehiclePlate: String? = "",
            var createTime: String? = "",
            var status: String? = "") : Parcelable
}
