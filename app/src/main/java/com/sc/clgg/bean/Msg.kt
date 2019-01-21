package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:49
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Message(var msg: String? = "",
                   var code: Double = 0.toDouble(),
                   var data: Data? = null,
                   var success: Boolean = false) : Parcelable {

    @Parcelize
    data class Data(
            var total: Int = 0,
            var pageSize: Int = 0,
            var totalPage: Int = 0,
            var currPage: Int = 0,
            var rows: List<Row>? = null) : Parcelable {

        @Parcelize
        data class Row(
                var id: String? = "",
                var created: Long = 0,
                var title: String? = "",
                var plate: String? = "",
                var top: String? = "",
                var stress: String? = "",
                var article: String? = "") : Parcelable
    }
}
