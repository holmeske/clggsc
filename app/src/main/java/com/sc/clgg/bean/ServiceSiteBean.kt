package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/9 16:14
 */


@SuppressLint("ParcelCreator")
@Parcelize
data class StoreInfoBean(var id: String? = "",
                         var level2: Int = 0,
                         var name: String? = "",
                         var subtitle: String? = "",
                         var head: String? = "",
                         var picture: String? = "",
                         var num: String? = "",
                         var address: String? = "",
                         var phone: String? = "",
                         var update2: String? = "",
                         var phonenum: String? = "",
                         var contact: String? = "",
                         var lat: Double = 0.0,
                         var lng: Double = 0.0,
                         var description: String? = "",
                         var status: String? = "",
                         var provincode: String? = "",
                         var citycode: String? = "") : Parcelable
