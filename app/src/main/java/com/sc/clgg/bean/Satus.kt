package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/26 17:09
 */
/**
 * payStatus:0表示不存在支付记录，1表示支付确认成功，2表示支付确认中，3表示支付确认失败
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class StatusBean(val status: Boolean, var checkCode: String? = "", var Mac2: String? = "", var RWasteSn: String? = "",
                      var wasteSn: String? = "", var cardNo: String? = "", var money: String? = "",
                      var carNo: String? = "", var realMoney: String? = "", var payStatus: Int? = -1, var number: Int? = -1,
                      val msg: String? = "", val code: Int, val success: Boolean) : Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class Mes(var resultCode: Int = -1, var ResultMessage: String? = "", var VIN: String? = "") : Parcelable