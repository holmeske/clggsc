package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 11:20
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ExpendListBean(var success: Boolean = false, var msg: String? = null, var data: DataBean? = null) : Parcelable {

    @Parcelize
    data class DataBean(var countPrice: Double = 0.toDouble(), var list: List<ListBean>? = null) : Parcelable {
        @Parcelize
        data class ListBean(var money: Double = 0.toDouble(),
                            var costText: String? = null,
                            var costType: String? = null,
                            var createdCd: Int = 0) : Parcelable
    }
}

data class ExpendListDetailBean(var success: Boolean = false,
                                var msg: String? = null,
                                var data: List<DataBean>? = null) {

    data class DataBean(var dispatchCity: String? = null,
                        var consigneeCity: String? = null,
                        var money: Double = 0.toDouble(),
                        var createdTm: Long = 0)
}




