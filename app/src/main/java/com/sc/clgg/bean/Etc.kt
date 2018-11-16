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
data class Card(var type: String? = "",
                var number: String? = "",
                var no: String? = ""
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CertificationInfo(
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
            var imageId:String?="",
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

