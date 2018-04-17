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
data class TallyBookBean(var success: Boolean = false,
                         var msg: String? = null,
                         var data: DataBean? = null) : Parcelable {

    @Parcelize
    data class DataBean(var income: Double = 0.toDouble(),
                        var expend: Double = 0.toDouble(),
                        var count: MutableMap<String, String>? = null,
                        var waybillList: List<WaybillListBean>? = null) : Parcelable {

        @Parcelize
        data class WaybillListBean(var inOrOut: String? = null,
                                   var amount: String? = null,
                                   var waybillNo: String? = null,
                                   var address: String? = null,
                                   var createdTime: String? = null,
                                   var updateTime: String? = null,
                                   var show: String? = null) : Parcelable

    }

}
