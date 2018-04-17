package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/9 14:35
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PayDetailBean(var success: Boolean = false, var msg: String = "", var data: DataBean) : Parcelable {
    @Parcelize
    class DataBean(var amount: Int, var address: String, var costList: List<CostListBean>) : Parcelable {
        @Parcelize
        class CostListBean(var id: Int = 0,
                           var businessType: Int = 0,
                           var costType: String = "",
                           var costText: String = "",
                           var money: Double = 0.0,
                           var createdTm: Long = 0,
                           var createdCd: Int = 0,
                           var status: Int = 0,
                           var waybillNo: String = "") : Parcelable
    }

}



