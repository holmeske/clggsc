package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/26 17:09
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class StatusBean(val status: Boolean,var checkCode: String? = "",  val msg: String? = "", val code: Int, val success: Boolean) : Parcelable